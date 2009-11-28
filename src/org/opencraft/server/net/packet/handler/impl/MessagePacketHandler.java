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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opencraft.server.cmd.Command;
import org.opencraft.server.model.World;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.net.packet.Packet;
import org.opencraft.server.net.packet.handler.PacketHandler;

/**
 * A class which handles message and comamnd packets.
 * @author Graham Edgecombe
 *
 */
public class MessagePacketHandler implements PacketHandler {

	@Override
	public void handlePacket(MinecraftSession session, Packet packet) {
		if(!session.isAuthenticated()) {
			return;
		}
		String message = packet.getStringField("message");
		if(message.startsWith("/")) {
			// interpret as command
			String tokens = message.substring(1);
			String[] parts = tokens.split(" ");
			final Map<String, Command> commands = World.getWorld().getGameMode().getCommands();
			Command c = commands.get(parts[0]);
			if(c != null) {
				parts[0] = null;
				List<String> partsList = new ArrayList<String>();
				for(String s : parts) {
					if(s != null) {
						partsList.add(s);
					}
				}
				parts = partsList.toArray(new String[0]);
				c.execute(session.getPlayer(), parts);
			} else {
				session.getActionSender().sendChatMessage("Invalid command.");
			}
		} else {
			World.getWorld().broadcast(session.getPlayer(), message);
		}
	}

}
