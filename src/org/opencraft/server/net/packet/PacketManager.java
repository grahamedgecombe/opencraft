package org.opencraft.server.net.packet;

import java.util.LinkedList;
import java.util.List;

import org.opencraft.server.io.PersistenceManager;

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

/**
 * A utility class for managing the whole packet system.
 * @param Graham Edgecombe
 * 
 */
public final class PacketManager {
	
	/**
	 * The packet manager instance.
	 */
	private static PacketManager instance;
	
	/**
	 * Initializes the packet manager.
	 * @throws IllegalStateException if the packet manager is already initialised.
	 */
	public static void init() {
		synchronized(PacketManager.class) {
			if(instance != null) {
				throw new IllegalStateException("Packet manager already initialised.");
			}
			instance = (PacketManager) PersistenceManager.getPersistenceManager().load("data/packets.xml");
		}
	}
	
	/**
	 * Gets the packet manager.
	 * @return The packet manager.
	 * @throws IllegalStateException if the packet manager has not been initialised.
	 */
	public static PacketManager getPacketManager() {
		synchronized(PacketManager.class) {
			if(instance == null) {
				throw new IllegalStateException("Packet manager not initialised - did you call init()?");
			}
			return instance;
		}
	}
	
	/**
	 * Incoming packets.
	 */
	private List<PacketDefinition> incoming = new LinkedList<PacketDefinition>();
	
	/**
	 * Outgoing packets.
	 */
	private List<PacketDefinition> outgoing = new LinkedList<PacketDefinition>();
	
	/**
	 * The incoming packet array (faster access by opcode than list iteration).
	 */
	private transient PacketDefinition[] incomingArray;
	
	/**
	 * The outgoing packet array (faster access by opcode than list iteration).
	 */
	private transient PacketDefinition[] outgoingArray;
	
	/**
	 * Default private constructor.
	 */
	private PacketManager() {
		/* empty */
	}
	
	/**
	 * Resolves the packet manager after deserialization.
	 * @return The resolved object.
	 */
	private Object readResolve() {
		incomingArray = new PacketDefinition[256];
		for(PacketDefinition def : incoming) {
			incomingArray[def.getOpcode()] = def;
		}
		outgoingArray = new PacketDefinition[256];
		for(PacketDefinition def : outgoing) {
			outgoingArray[def.getOpcode()] = def;
		}
		return this;
	}

	/**
	 * Gets an incoming packet definition.
	 * @param opcode The opcode.
	 * @return The packet definition.
	 */
	public PacketDefinition getIncomingPacket(int opcode) {
		return incomingArray[opcode];
	}

}
