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

import java.util.Map;

import org.opencraft.server.io.PersistenceManager;

/**
 * A class which manages <code>PacketHandler</code>s.
 * @author Graham Edgecombe
 *
 */
public final class BlockBehaviourManager {
	
	/**
	 * The singleton instance.
	 */
	private static final BlockBehaviourManager INSTANCE = new BlockBehaviourManager();

	/**
	 * Gets the block behaviour manager instance.
	 * @return The block behaviour manager instance.
	 */
	public static BlockBehaviourManager getPacketHandlerManager() {
		return INSTANCE;
	}
	
	/**
	 * An array of block behaviours.
	 */
	private BlockBehaviour[] behaviours = new BlockBehaviour[256];
	
	/**
	 * Default private constructor.
	 */
	@SuppressWarnings("unchecked")
	private BlockBehaviourManager() {
		try {
			Map<Integer, String> behaviours = (Map<Integer, String>) PersistenceManager.getPersistenceManager().load("data/blockBehaviours.xml");
			for(Map.Entry<Integer, String> behaviour : behaviours.entrySet()) {
				this.behaviours[behaviour.getKey()] = (BlockBehaviour) Class.forName(behaviour.getValue()).newInstance();
			}
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Handles a block behaviour.
	 * @param level The level on which the block is located.
	 * @param x The x-coordinate of the block.
	 * @param y The y-coordinate of the block.
	 * @param z The z-coordinate of the block.
	 * @param type The type of block.
	 */
	public void handlePassiveBehaviour(Level level, int x, int y, int z, int type) {
		BlockBehaviour behaviour = behaviours[type];
		if(behaviour != null) {
			behaviour.handlePassive(World.getWorld().getLevel(), x, y, z, type);
		}
	}
	
	/**
	 * Handles a block behaviour.
	 * @param level The level on which the block is located.
	 * @param x The x-coordinate of the block.
	 * @param y The y-coordinate of the block.
	 * @param z The z-coordinate of the block.
	 * @param type The type of block.
	 */
	public void handleBuildBehaviour(Level level, int x, int y, int z, int type) {
		BlockBehaviour behaviour = behaviours[type];
		if(behaviour != null) {
			behaviour.handleBuild(World.getWorld().getLevel(), x, y, z, type);
		}
	}

	/**
	 * Handles a block behaviour.
	 * @param level The level on which the block is located.
	 * @param x The x-coordinate of the block.
	 * @param y The y-coordinate of the block.
	 * @param z The z-coordinate of the block.
	 * @param type The type of block.
	 */
	public void handleBreakBehaviour(Level level, int x, int y, int z, int type) {
		BlockBehaviour behaviour = behaviours[type];
		if(behaviour != null) {
			behaviour.handleBreak(World.getWorld().getLevel(), x, y, z, type);
		}
	}
}