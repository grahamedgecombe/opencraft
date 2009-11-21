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
		
		/**
		 * The different spreading directions for a block.
		 */
		final int[][] spreadRules = 	{ { 1,  0,  0 },
										  {-1,  0,  0 },
										  { 0,  1,  0 },
										  { 0, -1,  0 },
										  { 0,  0, -1 } };
		
		final int spongeRadius = 3;
		
		for(int i = 0; i >= spreadRules.length - 1; i++) {
			
			byte thisBlock = level.getBlock(x+spreadRules[i][0], y+spreadRules[i][1], z+spreadRules[i][2]);	
				// check for sponges
			if(level.getBlock(x+(spreadRules[i][0]*spongeRadius), y+(spreadRules[i][1]*spongeRadius), z+(spreadRules[i][2]*spongeRadius)) != Block.SPONGE.getId()) 
			{
				// check for block anti-types
				if (thisBlock == Block.LAVA.getId() && type == Block.WATER.getId()) { 
					level.setBlock(x+spreadRules[i][0], y+spreadRules[i][1], z+spreadRules[i][2], Block.STONE.getId()); 
				}
				else if (thisBlock == Block.WATER.getId() && type == Block.LAVA.getId()) {
					level.setBlock(x+spreadRules[i][0], y+spreadRules[i][1], z+spreadRules[i][2], Block.STONE.getId());
				}
				else if (!Block.forId(thisBlock).isSolid() && !Block.forId(thisBlock).isLiquid()) {
					level.setBlock(x+spreadRules[i][0], y+spreadRules[i][1], z+spreadRules[i][2], type); 
				}
			}
		}
	}
	
}