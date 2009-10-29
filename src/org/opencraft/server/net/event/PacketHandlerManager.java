package org.opencraft.server.net.event;

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

import java.util.Map;
import java.util.logging.Logger;

import org.opencraft.server.io.PersistenceManager;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.net.packet.Packet;

/**
 * A class which manages <code>PacketHandler</code>s.
 * @author Graham Edgecombe
 *
 */
public class PacketHandlerManager {
	
	/**
	 * The singleton instance.
	 */
	private static final PacketHandlerManager INSTANCE = new PacketHandlerManager();
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(PacketHandlerManager.class.getName());

	/**
	 * Gets the packet handler manager instance.
	 * @return The packet handler manager instance.
	 */
	public static PacketHandlerManager getPacketHandlerManager() {
		return INSTANCE;
	}
	
	/**
	 * An array of packet handlers.
	 */
	private PacketHandler[] handlers = new PacketHandler[256];
	
	/**
	 * Default private constructor.
	 */
	@SuppressWarnings("unchecked")
	private PacketHandlerManager() {
		Map<Integer, PacketHandler> handlers = (Map<Integer, PacketHandler>) PersistenceManager.getPersistenceManager().load("data/packetHandlers.xml");
		for(Map.Entry<Integer, PacketHandler> handler : handlers.entrySet()) {
			this.handlers[handler.getKey()] = handler.getValue();
		}
	}

	/**
	 * Handles a packet.
	 * @param session The session.
	 * @param packet The packet.
	 */
	public void handlePacket(MinecraftSession session, Packet packet) {
		PacketHandler handler = handlers[packet.getDefinition().getOpcode()];
		if(handler != null) {
			handler.handlePacket(session, packet);
		} else {
			logger.info("Unhandled packet : " + packet + ".");
		}
	}

}
