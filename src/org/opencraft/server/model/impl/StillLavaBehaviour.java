package org.opencraft.server.model.impl;

import org.opencraft.server.model.BlockBehaviour;
import org.opencraft.server.model.BlockConstants;
import org.opencraft.server.model.BlockDefinition;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.World;

public class StillLavaBehaviour implements BlockBehaviour {

	@Override
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}

	@Override
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.setBlock(x, y, z, BlockConstants.LAVA);
	}

	@Override
	public void handleScheduledBehaviour(Level level, int x, int y, int z,
			int type) {
		
	}


}
