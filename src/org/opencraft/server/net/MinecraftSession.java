package org.opencraft.server.net;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe, Søren Enevoldsen and Brett Russell.
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

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.mina.core.session.IoSession;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;
import org.opencraft.server.net.packet.Packet;
import org.opencraft.server.net.packet.handler.PacketHandlerManager;

/**
 * Manages a connected Minecraft session.
 * @author Graham Edgecombe
 */
public final class MinecraftSession {
	
	/**
	 * Various connection states.
	 * @author Graham Edgecombe
	 */
	public enum State {
		
		/**
		 * Indicates the connection is new and has just connected.
		 */
		CONNECTED,

		/**
		 * Indicates the connection has been authenticated but is not yet ready.
		 */
		AUTHENTICATED,

		/**
		 * Indicates the connection is ready for use.
		 */
		READY;
	}
	
	/**
	 * The <code>IoSession</code> associated with this
	 * <code>MinecraftSession</code>.
	 */
	private final IoSession session;
	
	/**
	 * The action sender associated with this session.
	 */
	private final ActionSender actionSender = new ActionSender(this);
	
	/**
	 * Packet queue.
	 */
	private final Queue<Packet> queuedPackets = new ArrayDeque<Packet>();
	
	/**
	 * State.
	 */
	private State state = State.CONNECTED;
	
	/**
	 * The player associated with this session.
	 */
	private Player player;
	
	/**
	 * Creates the Minecraft session.
	 * @param session The <code>IoSession</code>.
	 */
	public MinecraftSession(IoSession session) {
		this.session = session;
	}
	
	/**
	 * Gets the action sender associated with this session.
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return actionSender;
	}
	
	/**
	 * Sets the player associated with this session.
	 * @param player The player.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Sets the state to authenticated.
	 */
	public void setAuthenticated() {
		this.state = State.AUTHENTICATED;
	}
	
	/**
	 * Sets the state to ready.
	 */
	public void setReady() {
		this.state = State.READY;
	}
	
	/**
	 * Gets the player associated with this session.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Checks if this session is authenticated.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isAuthenticated() {
		return player != null;
	}
	
	/**
	 * Handles a packet.
	 * @param packet The packet to handle.
	 */
	public void handle(Packet packet) {
		PacketHandlerManager.getPacketHandlerManager().handlePacket(this, packet);
	}
	
	/**
	 * Sends a packet. This method may be called from multiple threads.
	 * @param packet The packet to send.
	 */
	public void send(Packet packet) {
		synchronized (this) {
			final String name = packet.getDefinition().getName();
			final boolean unqueuedPacket = name.equals("authentication_response") || name.endsWith("level_init") || name.equals("level_block") || name.equals("level_finish") || name.equals("disconnect");
			if (state == State.READY) {
				if (queuedPackets.size() > 0) {
					for (Packet queuedPacket : queuedPackets) {
						session.write(queuedPacket);
					}
					queuedPackets.clear();
				}
				session.write(packet);
			} else if (unqueuedPacket) {
				session.write(packet);
			} else {
				queuedPackets.add(packet);
			}
		}
	}
	
	/**
	 * Closes this session.
	 */
	public void close() {
		session.close(false);
	}
	
	/**
	 * Called when this session is to be destroyed, should release any
	 * resources.
	 */
	public void destroy() {
		World.getWorld().unregister(this);
	}
	
}
