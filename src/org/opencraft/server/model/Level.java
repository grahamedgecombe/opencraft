package org.opencraft.server.model;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe and Brett Russell.
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
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


/**
 * Represents the actual level.
 * @author Graham Edgecombe
 * @author Brett Russell
 *
 */
public final class Level {
	
	/**
	 * The level width.
	 */
	private int width;
	
	/**
	 * The level height.
	 */
	private int height;
	
	/**
	 * The level depth.
	 */
	private int depth;
	
	/**
	 * The blocks.
	 */
	private byte[][][] blocks;
	
	/**
	 * The spawn rotation.
	 */
	private Rotation spawnRotation;
	
	/**
	 * The spawn position.
	 */
	private Position spawnPosition;
	
	/**
	 * The active "thinking" blocks on the map.
	 */
	private Map<Integer, ArrayDeque<Position>> activeBlocks = new HashMap<Integer, ArrayDeque<Position>>();
	
	/**
	 * The timers for the active "thinking" blocks on the map.
	 */
	private Map<Integer, Long> activeTimers = new HashMap<Integer, Long>();
	
	/**
	 * A queue of positions to update at the next tick.
	 */
	private Queue<Position> updateQueue = new ArrayDeque<Position>();
	
	/**
	 * Generates a level.
	 */
	public Level() {
		this.width = 256;
		this.height = 256;
		this.depth = 64;
		this.blocks = new byte[width][height][depth];
		this.spawnPosition = new Position(0, 0, 50);
		this.spawnRotation = new Rotation(0, 0);
		for(int i = 0; i < 256; i++) {
			BlockDefinition b = BlockManager.getBlockManager().getBlock(i);
			if(b != null && b.doesThink()) {
				activeBlocks.put(i, new ArrayDeque<Position>());
				activeTimers.put(i, System.currentTimeMillis());
			}
		}
		// temporary:
		/*
		for(int z = 0; z < depth / 2; z++) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					int type = z == (depth / 2 - 1) ? BlockDefinition.GRASS.getId() : BlockDefinition.DIRT.getId();
					this.blocks[x][y][z] = (byte) type;
				}
			}
		}
		*/
		for(int z = 0; z < 7; z++) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					if(z <= 5) {
						this.blocks[x][y][z] = (byte) BlockConstants.DIRT;
					} else {
						if(y < 40) {
							this.blocks[x][y][z] = (byte) BlockConstants.WATER;
						} else if(y == 50) {
							this.blocks[x][y][z-1] = (byte) BlockConstants.GRASS;
							this.queueActiveBlockUpdate(x, y, z-1);
						} else if(y > 60) {
							this.blocks[x][y][z] = (byte) BlockConstants.LAVA;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Performs physics updates on queued blocks.
	 */
	public void applyBlockBehaviour() {
		Queue<Position> currentQueue = new ArrayDeque<Position>(updateQueue);
		updateQueue.clear();
		for(Position pos : currentQueue) {
			BlockManager.getBlockManager().getBlock(this.getBlock(pos.getX(), pos.getY(), pos.getZ())).behavePassive(this, pos.getX(), pos.getY(), pos.getZ());
		}
		// we only process up to 20 of each type of thinking block every tick, or we'd probably be here all day.
		for(int type = 0; type < 256; type++) {
			if(activeBlocks.containsKey(type)) {
				if(System.currentTimeMillis() - activeTimers.get(type) > BlockManager.getBlockManager().getBlock(type).getTimer()) {
					int cyclesThisTick = (activeBlocks.get(type).size() > 20 ? 20 : activeBlocks.get(type).size());
					for(int i = 0; i < cyclesThisTick; i++) {
						Position pos = activeBlocks.get(type).poll();
						if(pos == null)
							break;
						// the block  that occupies this space might have changed.
						if(this.getBlock(pos.getX(), pos.getY(), pos.getZ()) == type) {
							// World.getWorld().broadcast("Processing thinker at ("+pos.getX()+","+pos.getY()+","+pos.getZ()+")");
							BlockManager.getBlockManager().getBlock(type).behaveSchedule(this, pos.getX(), pos.getY(), pos.getZ());
						}
					}
					activeTimers.put(type, System.currentTimeMillis());
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
	 * Sets a block and updates the neighbours.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The type id.
	 */
	public void setBlock(int x, int y, int z, int type) {
		setBlock(x, y, z, type, true);
	}

	/**
	 * Sets a block.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The type id.
	 * @param updateNeighbors Update neighbours flag.
	 */
	public void setBlock(int x, int y, int z, int type, boolean updateSelf) {
		if(x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
			return;
		}
		byte formerBlock = this.getBlock(x, y, z);
		blocks[x][y][z] = (byte) type;
		for(Player player : World.getWorld().getPlayerList().getPlayers()) {
			player.getSession().getActionSender().sendBlock(x, y, z, (byte)type);
		}
		if(updateSelf) {
			queueTileUpdate(x, y, z);
		}
		if(type == 0) {
			BlockManager.getBlockManager().getBlock(formerBlock).behaveDestruct(this, x, y, z);
			updateNeighboursAt(x, y, z);
		}
		if(BlockManager.getBlockManager().getBlock(type).doesThink()) {
			activeBlocks.get(type).add(new Position(x, y, z));
		}

	}
	
	/**
	 * Updates neighbours at the specified coordinate.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	private void updateNeighboursAt(int x, int y, int z) {
		queueTileUpdate(x - 1, y, z);
		queueTileUpdate(x, y - 1, z);
		queueTileUpdate(x + 1, y, z);
		queueTileUpdate(x, y + 1, z);
		queueTileUpdate(x, y, z - 1);
		queueTileUpdate(x, y, z + 1);
	}
	
	/**
	 * Queues a tile update.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	private void queueTileUpdate(int x, int y, int z) {
		if(x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth) {
			Position pos = new Position(x, y, z);
			if(!updateQueue.contains(pos)) {
				updateQueue.add(pos);
			}
		}
	}
	
	/**
	 * Forces a tile update to be queued. Use with caution.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	public void queueActiveBlockUpdate(int x, int y, int z) {
		if(x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth) {
			int blockAt = this.getBlock(x, y, z);
			if(BlockManager.getBlockManager().getBlock(blockAt).doesThink()) {
				activeBlocks.get(blockAt).add(new Position(x, y, z));
			}
		}
	}

	/**
	 * Gets a block.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @return The type id.
	 */
	public byte getBlock(int x, int y, int z) {
		if(x >= 0 && y >= 0 && z >= 0 && x < width && y < height && z < depth) {
			return blocks[x][y][z];
		} else {
			return 0;
		}
	}

	/**
	 * Set the rotation of the character when spawned.
	 * @param spawnRotation The rotation.
	 */
	public void setSpawnRotation(Rotation spawnRotation) {
		this.spawnRotation = spawnRotation;
	}

	/**
	 * Get the spawning rotation.
	 * @return The spawning rotation.
	 */
	public Rotation getSpawnRotation() {
		return spawnRotation;
	}

	/**
	 * Set the spawn position.
	 * @param spawnPosition The spawn position.
	 */
	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}

	/**
	 * Get the spawn position.
	 * @return The spawn position.
	 */
	public Position getSpawnPosition() {
		return spawnPosition;
	}

}
