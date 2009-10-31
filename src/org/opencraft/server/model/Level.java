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

/**
 * Represents the actual level.
 * @author Graham Edgecombe
 *
 */
public final class Level {
	
	/**
	 * The level width.
	 */
	private final int width = 256;
	
	/**
	 * The level height.
	 */
	private final int height = 256;
	
	/**
	 * The level depth.
	 */
	private final int depth = 64;
	
	/**
	 * The blocks.
	 */
	private final byte[][][] blocks = new byte[width][height][depth];
	
	/**
	 * Creates the level.
	 */
	public Level() {
		for(int i = 0; i < (depth / 2); i++) {
			for(int j = 0; j < width; j++) {
				for(int k = 0; k < height; k++) {
					blocks[j][k][i] = 3;
				}
			}
		}
	}
	
	/**
	 * Gets all of the blocks.
	 * @return All of the blocks.
	 */
	public byte[][][] getBlocks() {
		return blocks;
	}

	/**
	 * Gets the width of the level.
	 * @return The width of the level.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the level.
	 * @return The height of the level.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the depth of the level.
	 * @return The depth of the level.
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Sets a block.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The type id.
	 */
	public void setBlock(int x, int y, int z, byte type) {
		if(x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
			return;
		}
		blocks[x][y][z] = type;
		for(Player player : World.getWorld().getPlayerList().getPlayers()) {
			player.getSession().getActionSender().sendBlock(x, y, z, type);
		}
	}

}
