package org.opencraft.server.net.packet.handler.impl;

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

import java.util.logging.Logger;

import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.net.packet.Packet;
import org.opencraft.server.net.packet.PacketBuilder;
import org.opencraft.server.net.packet.PacketManager;
import org.opencraft.server.net.packet.handler.PacketHandler;

/**
 * Handles the incoming authentication packet.
 * @author Graham Edgecombe
 *
 */
public class AuthenticationPacketHandler implements PacketHandler {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(AuthenticationPacketHandler.class.getName());

	@Override
	public void handlePacket(MinecraftSession session, Packet packet) {
		String username = packet.getStringField("username");
		String verificationKey = packet.getStringField("verification_key");
		int protocolVersion = packet.getNumericField("protocol_version").intValue();
		logger.info("Received authentication packet : username=" + username + ", verificationKey=" + verificationKey + ", protocolVersion=" + protocolVersion + ".");
		
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(0));
		bldr.putByte("protocol_version", protocolVersion);
		bldr.putString("server_name", "OpenCraft");
		bldr.putString("server_message", "http://opencraft.sf.net/");
		bldr.putByte("user_type", 0);
		session.send(bldr.toPacket());
	}

}
