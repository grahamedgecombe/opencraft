package org.opencraft.server.model.impl;

import org.opencraft.server.model.Block;
import org.opencraft.server.model.BlockBehaviour;
import org.opencraft.server.model.Level;

public class StillLavaBehaviour implements BlockBehaviour {

	@Override
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}

	@Override
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.setBlock(x, y, z, Block.LAVA.getId());
	}

	@Override
	public void handleScheduledBehaviour(Level level, int x, int y, int z,
			int type) {
		
	}

}
