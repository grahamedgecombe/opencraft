package org.opencraft.server.model.impl;

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

import org.opencraft.server.model.Block;
import org.opencraft.server.model.BlockBehaviour;
import org.opencraft.server.model.Level;

/**
 * A block behaviour that handles liquids (outward and downward expansion).
 * @author Brett Russell
 *
 */
public class LiquidBehaviour implements BlockBehaviour {

	@Override
	public void apply(Level level, int x, int y, int z, int type) {
		if(Block.forId(type).isLiquid()) {
			if(type == Block.WATER.getId()) {
				waterFlow(level, x, y, z);
			} else if(type == Block.LAVA.getId()) {
				lavaFlow(level, x, y, z);
			} else {
			}
		}
	}
	
	/**
	 * Handles water behaviour specifically. Takes into account water's preference to flow down before flowing outward,
	 * and the effects of sponges, as well as water's reaction when making contact with lava.
	 * @param level The level.
	 * @param x The block's x-coordinate.
	 * @param y The block's y-coordinate.
	 * @param z The block's z-coordinate.
	 */
	private void waterFlow(Level level, int x, int y, int z) {
		
	}
	
	/**
	 * Handles lava behaviour specifically. Lava has no preference for directional flow, except it flows much slower than water. 
	 * Takes into account lava's reaction when making contact with water.
	 * @param level The level.
	 * @param x The block's x-coordinate.
	 * @param y The block's y-coordinate.
	 * @param z The block's z-coordinate.
	 */
	private void lavaFlow(Level level, int x, int y, int z) {
		
	}
		
}
