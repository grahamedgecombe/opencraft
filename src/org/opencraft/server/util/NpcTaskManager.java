
	/**
	 *  This code belongs to Mark Farrell.
	 *  Do not leech or steal my code in any way, shape or form.
	 */
	package org.opencraft.server.util;

	import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.opencraft.server.model.Entity;
import org.opencraft.server.model.World;
import org.opencraft.server.model.npc.Npc;
import org.opencraft.server.task.ScheduledTask;
import org.opencraft.server.task.TaskQueue;

	/**
	 * @author Mark
	 *
	 */
	public class NpcTaskManager extends ScheduledTask{

		
		private final ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newCachedThreadPool();
		
		private final EntityList<Npc> list = new EntityList<Npc>();

		private final static Logger logger = Logger.getLogger("Npc Task Manager");

		public final static NpcTaskManager INSTANCE = new NpcTaskManager(0);
		
		
		private NpcTaskManager(int delay) {
			super(delay);
			
			TaskQueue.getTaskQueue().schedule(this);
			// TODO Auto-generated constructor stub
		}
		
		private class InvokableNpcTask implements Runnable
		{

			private Npc npc;
			
			public InvokableNpcTask(Npc npc)
			{
				this.npc = npc;
				list.add(npc);
			}
			@Override
			public void run() {
				// TODO Auto-generated method stub
				npc.behave();
				npc.getPath().navigate();
			}

			
		}

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub

			Iterator<Entity> npcIterator = World.getWorld().getEntityControl().getNpcs().getEntities().iterator();
			OUTER_ITER:
			while(npcIterator.hasNext())
			{
				
				Iterator<Entity> localIterator = list.getEntities().iterator();
				Npc currentNpc = (Npc) npcIterator.next();
				while(localIterator.hasNext())
				{
					if(((Npc)localIterator.next()).equals(currentNpc))
					{
						continue OUTER_ITER;
					}

				}
				Npc npc = (Npc)localIterator.next();
				logger .info("Scheduling new task...");
				executor.schedule(new InvokableNpcTask(currentNpc), 5, TimeUnit.MILLISECONDS);
				
			}
		}
		
		

	}

