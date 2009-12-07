package org.opencraft.server.extensions.brushes;

import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;


/**
 * The standard brush used to paint with
 * @author Søren Enevoldsen
 *
 */

public class StandardBrush extends BrushAdapter {
	
	public StandardBrush() {
		maxWidth = 1;
		maxHeight = 1;
		maxLength = 1;
		setRadius(0);
		useForDelete(true);
	}

	@Override
	protected void paintBlocks(Player player, Level level, int x, int y, int z, boolean add,	int type) {
		if ((positionIsBuildable(x,y,z) == add))
		level.setBlock(x, y, z, type);
	}
}
