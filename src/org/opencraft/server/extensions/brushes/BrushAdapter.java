package org.opencraft.server.extensions.brushes;

/*
 * OpenCraft License
 * 
* Copyright (c) 2009 Graham Edgecombe, Søren Enevoldsen and Brett Russell.
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

import org.opencraft.server.extensions.brushes.Brush;
import org.opencraft.server.model.BlockConstants;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;

/**
 * Shortcut for creation of Brushes
 * Only constructor and paintBlocks are necessary
 * @author Søren Enevoldsen
 *
 */

public abstract class BrushAdapter extends Brush {

	boolean useForDelete = false;
	protected int minWidth = 1;
	protected int minHeight = 1;
	protected int minLength = 1;
	
	protected int maxWidth = 7;
	protected int maxHeight = 7;
	protected int maxLength = 7;
	
	protected int radius = 0;
	protected int width = 0;
	protected int height = 0;
	protected int length = 0;
	
	/* If even dimensions, prioriterize
	 * right, front and up
	 */
	protected int dLeft = 0;
	protected int dRight = 0;
	protected int dBack = 0;
	protected int dFront = 0;
	protected int dUp = 0;
	protected int dDown = 0;
	
	public BrushAdapter() {
		setRadius(1);
	}
	
	public BrushAdapter(int radius) {
		setRadius(radius);
	}
	
	public BrushAdapter(int width, int length, int height) {
		setWidth(width);
		setLength(length);
		setHeight(height);
	}
	
	/**
	 * Bounds a value to min, max or between.
	 * @value The value
	 * @min Minimum value
	 * @max Maximum value
	 */
	protected int clamp(int value, int min, int max) {
		if (value > max)
			return max;
		if (value < min)
			return min;
		return value;
	}
	
	/**
	 * Keeps a width between min and max
	 * @param width
	 * @return The new width
	 */
	protected int clampWidth(int width) {
		return clamp(width, minWidth, maxWidth);
	}
	
	/**
	 * Keeps a height between min and max 
	 * @param height
	 * @return The new height
	 */
	protected int clampHeight(int height) {
		return clamp(height, minHeight, maxHeight);
	}
	
	/**
	 * Keeps a length between min and max
	 * @param length
	 * @return The new length
	 */
	protected int clampLength(int length) {
		return clamp(length, minLength, maxLength);
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

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void paint(Player player, Level level, int x, int y, int z, int mode, int type) {
		boolean add = (mode == 1 ? true : false); 
		if (add)	
			paintBlocks(player, level, x, y, z, add, type);
		else if (useForDelete)
			paintBlocks(player, level, x, y, z, add, BlockConstants.AIR);
		else
			World.getWorld().getLevel().setBlock(x, y, z, BlockConstants.AIR);	
	}
	
	/**
	 * Paints the blocks
	 * Should be implemented
	 * @param x
	 * @param y
	 * @param z
	 * @param build
	 * @param type
	 */
	protected abstract void paintBlocks(Player player, Level level, int x, int y, int z, boolean add, int type);

	@Override
	public int setHeight(int newHeight) {
		height = clampHeight(newHeight);
		dUp = (int)Math.ceil((height-1)/2.0);
		dDown = (int)Math.floor((height-1)/2.0);
		return height;
	}

	@Override
	public int setLength(int newLength) {
		length = clampLength(newLength);
		dFront = (int)Math.ceil((length-1)/2.0);
		dBack = (int)Math.floor((length-1)/2.0);
		return length;
	}

	@Override
	public int setWidth(int newWidth) {
		width = clampWidth(newWidth);
		dRight = (int)Math.ceil((width-1)/2.0);
		dLeft = (int)Math.floor((width-1)/2.0);
		return width;
	}
	
	@Override
	public boolean setRadius(int newRadius) {
		radius = newRadius;
		setHeight(newRadius*2+1);
		setLength(newRadius*2+1);
		setWidth(newRadius*2+1);
		return true;
	}

	@Override
	public boolean useForDelete(boolean enable) {
		boolean oldValue = useForDelete;
		useForDelete = enable;
		return oldValue;
	}

	@Override
	public int getRadius() {
		return radius;
	}

}
