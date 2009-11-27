package org.opencraft.server.model;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the OpenCraft nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.opencraft.server.Configuration;
import org.opencraft.server.Constants;
import org.opencraft.server.heartbeat.HeartbeatManager;
import org.opencraft.server.io.LevelGzipper;
import org.opencraft.server.model.npc.Npc;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.util.EntityList;
import org.opencraft.server.util.GlobalList;

/**
 * Manages the in-game world.
 * @author Graham Edgecombe
 *
 */
public final class World {
	
	/**
	 * The singleton instance.
	 */
	private static final World INSTANCE = new World();
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return INSTANCE;
	}
	
	/**
	 * The level.
	 */
	private final Level level = new Level();
	
	/**
	 * The list containing all of the server's entities.
	 */
	private final GlobalList globalList = new GlobalList(EntityList.DEFAULT_MAXIMUM, 2000);
	private World() {
		/* empty */
	}
	
	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public GlobalList getEntityControl() {
		return globalList;
	}
	
	/**
	 * Gets the level.
	 * @return The level.
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Registers a session.
	 * @param session The session.
	 * @param username The username.
	 * @param verificationKey The verification key.
	 */
	public void register(MinecraftSession session, String username, String verificationKey) {
		// verify name
		if(Configuration.getConfiguration().isVerifyingNames()) {
			long salt = HeartbeatManager.getHeartbeatManager().getSalt();
			String hash = new StringBuilder().append(String.valueOf(salt)).append(username).toString();
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("No MD5 algorithm!");
			}
			digest.update(hash.getBytes());
			if(!verificationKey.equals(new BigInteger(1, digest.digest()).toString(16))) {
				session.getActionSender().sendLoginFailure("Illegal name.");
				return;
			}
		}
		// check if name is valid
		char[] nameChars = username.toCharArray();
		for(char nameChar : nameChars) {
			if(nameChar < ' ' || nameChar > '\177') {
				session.getActionSender().sendLoginFailure("Invalid name!");
				return;
			}
		}
		// disconnect any existing players with the same name
		for(Entity p : globalList.getPlayers().getEntities()) {
			if(p.getName().equalsIgnoreCase(username)) {
				((Player) p).getSession().getActionSender().sendLoginFailure("Logged in from another computer.");
				break;
			}
		}
		// attempt to add the player
		final Player player = new Player(session, username);
		if(!globalList.add(player)) {
			player.getSession().getActionSender().sendLoginFailure("Too many players online!");
			return;
		}
		// final setup
		session.setPlayer(player);
		final Configuration c = Configuration.getConfiguration();
		session.getActionSender().sendLoginResponse(Constants.PROTOCOL_VERSION, c.getName(), c.getMessage(), false);
		LevelGzipper.getLevelGzipper().gzipLevel(session);
	}

	/**
	 * Registers an npc into the npc list.
	 */
	public void register(Npc npc)
	{
		globalList.add(npc);
	}
	/**
	 * Unregisters a session.
	 * @param session
	 */
	public void unregister(Entity entity) {
		if(entity instanceof Player)
		{
			MinecraftSession session = ((Player) entity).getSession();
			if(session.isAuthenticated()) {
				globalList.remove(session.getPlayer());
				broadcast(session.getPlayer().getName() + " disconnected.");
				session.setPlayer(null);
			}
		}
		else if(entity instanceof Npc)
		{
			globalList.remove((Npc)entity);
		}
	}

	/**
	 * Completes registration of a session.
	 * @param session The sessino.
	 */
	public void completeRegistration(MinecraftSession session) {
		session.getActionSender().sendChatMessage("Welcome to MineCraft!");
		broadcast(session.getPlayer().getName() + " connected.");
	}

	/**
	 * Broadcasts a chat message.
	 * @param player The source player.
	 * @param message The message.
	 */
	public void broadcast(Player player, String message) {
		for(Entity otherPlayer : globalList.getPlayers().getEntities()) {
			((Player) otherPlayer).getSession().getActionSender().sendChatMessage(player.getId(), message);
		}
	}
	
	/**
	 * Broadcasts a server message.
	 * @param message The message.
	 */
	public void broadcast(String message) {
		for(Entity player : globalList.getPlayers().getEntities()) {
			((Player) player).getSession().getActionSender().sendChatMessage(message);
		}
	}
	
	/**
	 * 
	 * Broadcasts a server message that has the sender's name and level as a prefix.
	 */
	public void broadcastWithHeader(Player player, String message)
	{
		String send = player.getName() + "&agdgd&a(Level:" + player.getSkill().getLevel() + "):  ";
		broadcast(player, send + message);
	}

}
