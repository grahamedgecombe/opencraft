package org.opencraft.server.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.opencraft.server.Configuration;
import org.opencraft.server.Constants;
import org.opencraft.server.heartbeat.HeartbeatManager;
import org.opencraft.server.io.LevelGzipper;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.util.PlayerList;

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
	 * The player list.
	 */
	private final PlayerList playerList = new PlayerList();
	
	/**
	 * Default private constructor.
	 */
	private World() {
		/* empty */
	}
	
	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public PlayerList getPlayerList() {
		return playerList;
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
		for(Player p : playerList.getPlayers()) {
			if(p.getName().equalsIgnoreCase(username)) {
				p.getSession().getActionSender().sendLoginFailure("Logged in from another computer.");
				break;
			}
		}
		// attempt to add the player
		final Player player = new Player(session, username);
		if(!playerList.add(player)) {
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
	 * Unregisters a session.
	 * @param session
	 */
	public void unregister(MinecraftSession session) {
		if(session.isAuthenticated()) {
			playerList.remove(session.getPlayer());
		}
	}

	/**
	 * Completes registration of a session.
	 * @param session The sessino.
	 */
	public void completeRegistration(MinecraftSession session) {
		session.getActionSender().sendChatMessage("Welcome to OpenCraft!");
	}

	/**
	 * Broadcasts a chat message.
	 * @param player The source player.
	 * @param message The message.
	 */
	public void broadcast(Player player, String message) {
		for(Player otherPlayer : playerList.getPlayers()) {
			otherPlayer.getSession().getActionSender().sendChatMessage(player.getId(), message);
		}
	}
	
	/**
	 * Broadcasts a server message.
	 * @param message The message.
	 */
	public void broadcast(String message) {
		for(Player player : playerList.getPlayers()) {
			player.getSession().getActionSender().sendChatMessage(message);
		}
	}

}
