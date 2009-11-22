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
import org.opencraft.server.Configuration;

/**
 * Handles sponge behaviour.
 * @author Brett Russell
 *
 */

public class SpongeBehaviour implements BlockBehaviour {

	public void apply(Level level, int x, int y, int z, int type, boolean build) {
		int spongeRadius = Configuration.getConfiguration().getSpongeRadius();
		
		if(build) { // building a sponge...
			for(int spongeX = -1 * spongeRadius; spongeX >= spongeRadius; spongeX++) {
				for(int spongeY = -1 *spongeRadius; spongeY >= spongeRadius; spongeY++) {
					for(int spongeZ = -1 * spongeRadius; spongeZ >= spongeRadius; spongeZ++) {
						if ((level.getBlock(x+spongeX, y+spongeY, z+spongeZ) == Block.WATER.getId()) || (level.getBlock(x+spongeX, y+spongeY, z+spongeZ) == Block.STILL_WATER.getId())) 
							level.setBlock(x+spongeX, y+spongeY, z+spongeZ, Block.AIR.getId());
					}
				}
			}
		} else if(!build) { // breaking a sponge...
			for(int spongeX = -1 * (spongeRadius + 1); spongeX >= spongeRadius + 1; spongeX++) {
				for(int spongeY = -1 * (spongeRadius + 1); spongeY >= spongeRadius + 1; spongeY++) {
					for(int spongeZ = -1 * (spongeRadius + 1); spongeZ >= spongeRadius + 1; spongeZ++) {
						if (level.getBlock(x+spongeX, y+spongeY, z+spongeZ) == Block.STILL_WATER.getId())
							level.setBlock(x+spongeX, y+spongeY, z+spongeZ, Block.WATER.getId());
					}
				}
			}
		} else return;
		
	}

	@Override
	public void apply(Level level, int x, int y, int z, int type) {
		apply(level, x, y, z, type, false);
	}
	
}
