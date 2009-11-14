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

import java.util.EnumSet;

/**
 * Represents an individual block.
 * @author Brett Russell
 *
 */
public enum Block {
	// id, translucency, solid, liquid, plant, halfblock
	AIR(0, 1, 0, 0, 0, 0),
	
	STONE(1, 0, 1, 0, 0, 0),
	
	GRASS(2, 0, 1, 0, 0, 0),
	
	DIRT(3, 0, 1, 0, 0, 0),
	
	COBBLESTONE(4, 0, 1, 0, 0, 0),
	
	PLANKS(5, 0, 1, 0, 0, 0),
	
	PLANKS_BOOKSHELF(47, 0, 1, 0, 0, 0),
	
	SHRUB(6, 1, 0, 0, 1, 0),
	
	ADMINIUM(7, 0, 1, 0, 0, 0),
	
	WATER(8, 0, 0, 1, 0, 0),
	
	STILL_WATER(9, 0, 0, 1, 0, 0),
	
	LAVA(10, 0, 0, 1, 0, 0),
	
	STILL_LAVA(11, 0, 0, 1, 0, 0),
	
	SAND(12, 0, 1, 0, 0, 0),
	
	GRAVEL(13, 0, 1, 0, 0, 0),
	
	ORE_GOLD(14, 0, 1, 0, 0, 0),
	
	ORE_IRON(15, 0, 1, 0, 0, 0),
	
	COAL(16, 0, 1, 0, 0, 0),
	
	TREETRUNK(17, 0, 1, 0, 0, 0),
	
	LEAVES(18, 1, 1, 0, 0, 0),
	
	SPONGE(19, 0, 1, 0, 0, 0),
	
	GLASS(20, 1, 1, 0, 0, 0),
	
	CLOTH_RED(21, 0, 1, 0, 0, 0),
	
	CLOTH_ORANGE(22, 0, 1, 0, 0, 0),
	
	CLOTH_YELLOW(23, 0, 1, 0, 0, 0),
	
	CLOTH_YELLOWGREEN(24, 0, 1, 0, 0, 0),
	
	CLOTH_GREEN(25, 0, 1, 0, 0, 0),
	
	CLOTH_GREENBLUE(26, 0, 1, 0, 0, 0),
	
	CLOTH_CYAN(27, 0, 1, 0, 0, 0),
	
	CLOTH_LIGHTBLUE(28, 0, 1, 0, 0, 0),
	
	CLOTH_BLUE(29, 0, 1, 0, 0, 0),
	
	CLOTH_PURPLE(30, 0, 1, 0, 0, 0),
	
	CLOTH_INDIGO(31, 0, 1, 0, 0, 0),
	
	CLOTH_VIOLET(32, 0, 1, 0, 0, 0),
	
	CLOTH_PINK(33, 0, 1, 0, 0, 0),
	
	CLOTH_DARKGRAY(34, 0, 1, 0, 0, 0),
	
	CLOTH_GRAY(35, 0, 1, 0, 0, 0),
	
	CLOTH_WHITE(36, 0, 1, 0, 0, 0),
	
	FLOWER_YELLOW(37, 1, 0, 0, 1, 0),
	
	FLOWER_RED(38, 1, 0, 0, 1, 0),
	
	MUSHROOM_RED(40, 1, 0, 0, 1, 0),
	
	MUSHROOM_BROWN(39, 1, 0, 0, 1, 0),
	
	BAR_GOLD(41, 0, 1, 0, 0, 0),
	
	BAR_IRON(42, 0, 1, 0, 0, 0),
	
	BRICK_RED(45, 0, 1, 0, 0, 0),
	
	OBSIDIAN(49, 0, 1, 0, 0, 0),
	
	STAIR(44, 0, 1, 0, 0, 1),
	
	TNT(46, 0, 1, 0, 0, 0),
	
	COBBLESTONE_MOSSY(48, 0, 1, 0, 0, 0),
	
	STAIR_DOUBLE(43, 0, 1, 0, 0, 0);
	
	/**
	 * The block's ID.
	 */
	private byte id;
	
	/**
	 * Properties of an individual block.
	 */
	private boolean translucent, solid, liquid, plant, halfblock;

	/**
	 * Constructor.
	 * @param id The block's ID.
	 * @param translucent Whether the block permits light to pass through.
	 * @param solid Whether the block is tangible.
	 * @param liquid Whether the block is a liquid (swimmable)
	 * @param plant Whether the block is a plant.
	 * @param halfblock Whether the block is a half-block, or stair.
	 */
	private Block(int id, int translucent, int solid, int liquid, int plant, int halfblock) {
		this.id = (byte) id;
		this.translucent = translucent > 0 ? true : false;
		this.solid = solid > 0 ? true : false;
		this.liquid = liquid > 0 ? true : false;
		this.plant = plant > 0 ? true : false;
		this.halfblock = halfblock > 0 ? true : false;
	}

	/**
	 * Gets the blocks ID, as a byte.
	 * @return The block's ID as a byte.
	 */
	public byte getId() {
		return id;
	}
	
	/**
	 * Get the block's translucency.
	 * @return The block's translucency.
	 */
	public boolean isTranslucent() {
		return translucent;
	}
	
	/**
	 * Get the block's tangibility.
	 * @return The block's tangibility.
	 */
	public boolean isSolid() {
		return solid;
	}
	
	/**
	 * Get the block's fluidity.
	 * @return The block's fluidity.
	 */
	public boolean isLiquid() {
		return liquid;
	}
	
	/**
	 * Get the block's organic state.
	 * @return The block's organic state.
	 */
	public boolean isPlant() {
		return plant;
	}
	
	/**
	 * Get the block's type (full or half)
	 * @return The block's type. (true = half, false = full)
	 */
	public boolean isHalfBlock() {
		return halfblock;
	}
	
	/**
	 * An array populated by the different types of blocks.
	 */
	private static Block[] blocks = new Block[256];
	
	/**
	 * Populate the array.
	 */
	static {
		for(Block b : EnumSet.allOf(Block.class)) {
			blocks[b.getId()] = b;
		}
	}
	
	/**
	 * Get a block's information by its ID.
	 * @param id The block's ID.
	 * @return A block, and its information.
	 */
	public static Block forId(int id) {
		return blocks[id];
	}
	
}
	

