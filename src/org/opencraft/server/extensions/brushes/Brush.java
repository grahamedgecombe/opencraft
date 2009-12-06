package org.opencraft.server.extensions.Brushes;

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

import org.opencraft.server.model.BlockConstants;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;

/**
 * Represents the brush used to "paint" the level with.
 * @author Søren Enevoldsen
 *
 */

abstract public class Brush {
	//0 is a point, eg. one block
	protected int radius = 0;
	private int minRadius = 0;
	private int maxRadius = 3;
	private boolean useForDelete = false;
	
	/**
	 * Handles the "painting". Implementation should be done with paintBlocks
	 * @param level The level
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @param mode  
	 * @param type Type of block
	 */
	public void paint(Player player, Level level, int x, int y, int z, int mode, int type) {
		boolean build = (mode == 1 ? true : false); 
		if (build)	
			paintBlocks(player, x, y, z, build, type);
		else if (useForDelete)
			paintBlocks(player, x, y, z, build, BlockConstants.AIR);
		else
			World.getWorld().getLevel().setBlock(x, y, z, BlockConstants.AIR);
	}

	/**
	 * Paints the blocks
	 * @param x
	 * @param y
	 * @param z
	 * @param build
	 * @param type
	 */
	protected void paintBlocks(Player player, int x, int y, int z, boolean build, int type)
	{
		if ((positionIsBuildable(x,y,z) == build))
			World.getWorld().getLevel().setBlock(x, y, z, type);
	}
	
	/**
	 * Set radius of brush
	 * @param radius The radius
	 */
	public void setRadius(int newRadius) {
		this.radius = clampRadius(newRadius);
	}
	
	/**
	 * Gets the radius of brush
	 * @return The radius
	 */
	public int getRadius() {
		return this.radius;
	}
	
	/**
	 * Sets the max radius
	 * @param newMaxRadius The new max radius
	 */
	protected void setMaxRadius(int newMaxRadius) {
		this.maxRadius = newMaxRadius;
	}
	
	/**
	 * Bounds a value to min, max or between.
	 * @value The value
	 * @min Minimum value
	 * @max Maxium value
	 */
	protected int clamp(int value, int min, int max) {
		if (value > max)
			return max;
		if (value < min)
			return min;
		return value;
	}
	
	/**
	 * Returns a valid radius.
	 * @param value The value
	 * @return Valid radius
	 */
	private int clampRadius(int value) {
		return clamp(value, minRadius, maxRadius);
	}
	
	/**
	 * Checks to see if a type can be built upon
	 * @param type The type questioned
	 * @return Returns true if the type is build-able (changeable)
	 */
	protected boolean typeIsBuildable(int type) {
		if (type == BlockConstants.AIR ||
				type == BlockConstants.WATER ||
				type == BlockConstants.STILL_WATER)
			return true;
		return false;
	}
	
	/**
	 * Checks to see if a block is build-able
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @return Returns true if block at position is build-able (changeable)
	 */
	protected boolean positionIsBuildable(int x, int y, int z) {
		return typeIsBuildable(World.getWorld().getLevel().getBlock(x, y, z));
	}
	
	/**
	 * Sets whether this brush will be used for deleting
	 * @param b Boolean indicating whether this brush will be used to delete.
	 */
	public void setUseForDelete(boolean b) {
		this.useForDelete = b;
	}
}
