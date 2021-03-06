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

import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;

/**
 * Makes a diamond
 * @author Søren Enevoldsen
 */

public class DiamondBrush extends BrushAdapter {
	
	public DiamondBrush() {
	}
	
	public DiamondBrush(int radius) {
		setRadius(radius);
	}
	
	@Override
	protected void paintBlocks(Player player, Level level, int x, int y, int z, boolean adding, int type) {
		for (int offsetZ = -height; offsetZ <= radius; offsetZ++)
			for (int offsetY = -radius; offsetY <= radius; offsetY++)
				for (int offsetX = -radius; offsetX <= radius; offsetX++)
					if (positionIsBuildable(offsetX + x, offsetY + y, offsetZ + z) == adding && Math.abs(offsetX) + Math.abs(offsetY) + Math.abs(offsetZ) <= Math.abs(radius))
						level.setBlock(offsetX + x, offsetY + y, offsetZ + z, type);
	}
}
