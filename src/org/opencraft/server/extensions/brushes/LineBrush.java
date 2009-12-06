package org.opencraft.server.extensions.brushes;

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

import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;

/**
 * A brush that makes a line away from player
 * @author Søren Enevoldsen
 *
 */

public class LineBrush extends Brush {

	private static int BLOCKSIZE = 32;
	
	public LineBrush() {
		setMaxRadius(6);
	};
	
	public LineBrush(int radius) {
		setMaxRadius(6);
		setRadius(radius);
	}
	
	@Override
	protected void paintBlocks(Player player, int x, int y, int z, boolean build, int type) {
		int[] playerPosition = new int[] { player.getPosition().getX()/BLOCKSIZE,
				player.getPosition().getY()/BLOCKSIZE, player.getPosition().getZ()/BLOCKSIZE};
		
		
		int dx = x-playerPosition[0];
		int dy = y-playerPosition[1];
		int dz = z-playerPosition[2]+1;
		int adx = Math.abs(dx);
		int ady = Math.abs(dy);
		int adz = Math.abs(dz);
				
		int offsetZ = 0;
		int offsetY = 0;
		int offsetX = 0;
		
		if (adx > Math.max(ady, adz) && adx >= 1)
			offsetX = clamp(dx, -1, 1);
		else if (ady > Math.max(adx, adz) && ady >= 1)
			offsetY = clamp(dy, -1, 1);
		else if (adz > Math.max(adx, ady) && adz >= 1)
			offsetZ = clamp(dz, -1, 1);
		else
			return;
				
		for (int nthBlock=0; nthBlock<=radius; nthBlock++)
			if (positionIsBuildable(offsetX*nthBlock+x, offsetY*nthBlock+y, offsetZ*nthBlock+z) == build &&
					Math.abs(offsetX)+Math.abs(offsetY)+Math.abs(offsetZ) <= Math.abs(radius))
				World.getWorld().getLevel().setBlock(offsetX*nthBlock+x, offsetY*nthBlock+y, offsetZ*nthBlock+z, type);
	}
}
