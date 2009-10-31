package org.opencraft.server.net;

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

import org.opencraft.server.model.Entity;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.World;
import org.opencraft.server.net.packet.PacketBuilder;
import org.opencraft.server.net.packet.PacketManager;
import org.opencraft.server.task.Task;
import org.opencraft.server.task.TaskQueue;

/**
 * A utility class for sending packets.
 * @author Graham Edgecombe
 *
 */
public class ActionSender {
	
	/**
	 * The session.
	 */
	private MinecraftSession session;
	
	/**
	 * Creates the action sender.
	 * @param session The session.
	 */
	public ActionSender(MinecraftSession session) {
		this.session = session;
	}
	
	/**
	 * Sends a login response.
	 * @param protocolVersion The protocol version.
	 * @param name The server name.
	 * @param message The server message of the day.
	 * @param op Operator flag.
	 */
	public void sendLoginResponse(int protocolVersion, String name, String message, boolean op) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(0));
		bldr.putByte("protocol_version", protocolVersion);
		bldr.putString("server_name", name);
		bldr.putString("server_message", message);
		bldr.putByte("user_type", op ? 1 : 0);
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a login failure.
	 * @param message The message to send to the client.
	 */
	public void sendLoginFailure(String message) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(14));
		bldr.putString("reason", message);
		session.send(bldr.toPacket());
		session.close();
	}

	/**
	 * Sends the level init packet.
	 */
	public void sendLevelInit() {
		session.setAuthenticated();
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(2));
		session.send(bldr.toPacket());
	}

	/**
	 * Sends a level block/chunk.
	 * @param len The length of the chunk.
	 * @param chunk The chunk data.
	 * @param percent The percentage.
	 */
	public void sendLevelBlock(int len, byte[] chunk, int percent) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(3));
		bldr.putShort("chunk_length", len);
		bldr.putByteArray("chunk_data", chunk);
		bldr.putByte("percent", percent);
		session.send(bldr.toPacket());
	}

	/**
	 * Sends the level finish packet.
	 */
	public void sendLevelFinish() {
		TaskQueue.getTaskQueue().push(new Task() {
			public void execute() {
				// for thread safety
				session.setReady();
				World.getWorld().completeRegistration(session);
			}
		});
		final Level level = World.getWorld().getLevel();
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(4));
		bldr.putShort("width", level.getWidth());
		bldr.putShort("height", level.getHeight());
		bldr.putShort("depth", level.getDepth());
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends the add entity packet.
	 * @param entity The entity being added.
	 */
	public void sendAddEntity(Entity entity) {
		
	}
	
	/**
	 * Sends the update entity packet.
	 * @param entity The entity being updated.
	 */
	public void sendUpdateEntity(Entity entity) {
		
	}
	
	/**
	 * Sends the remove entity packet.
	 * @param entity The entity being removed.
	 */
	public void sendRemoveEntity(Entity entity) {
		
	}

	/**
	 * Sends a chat message. 
	 * @param message The message.
	 */
	public void sendChatMessage(String message) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(13));
		bldr.putByte("id", -1);
		bldr.putString("message", message);
		session.send(bldr.toPacket());
	}

	/**
	 * Sends a block.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param type Block type.
	 */
	public void sendBlock(int x, int y, int z, byte type) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(6));
		bldr.putShort("x", x);
		bldr.putShort("y", y);
		bldr.putShort("z", z);
		bldr.putByte("type", type);
		session.send(bldr.toPacket());
	}

	/**
	 * Sends a chat message.
	 * @param id The source player id.
	 * @param message The message.
	 */
	public void sendChatMessage(int id, String message) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(13));
		bldr.putByte("id", id);
		bldr.putString("message", message);
		session.send(bldr.toPacket());
	}

}
