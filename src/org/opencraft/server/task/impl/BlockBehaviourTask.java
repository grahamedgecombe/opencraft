package org.opencraft.server.task.impl;

import org.opencraft.server.model.Block;
import org.opencraft.server.model.World;
import org.opencraft.server.task.ScheduledTask;

public class BlockBehaviourTask extends ScheduledTask {

	/**
	 * The delay.
	 */
	private static final long DELAY = 50;
	
	/**
	 * Creates the update task with a delay of 50ms.
	 */
	public BlockBehaviourTask() {
		super(DELAY);
	}

	@Override
	public void execute() {
		int count = 1;
		if(count >= 20) {
			World.getWorld().getLevel().applyScheduledBlockBehaviour(Block.LAVA.getId());
			count = 1;
		}
		count++;
		if(count%2 == 0) {
			World.getWorld().getLevel().applyScheduledBlockBehaviour(Block.WATER.getId());
		}
		World.getWorld().getLevel().applyPassiveBlockBehaviour();
	}

}
