/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.model.npc;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import org.opencraft.server.task.ScheduledTask;


import org.opencraft.server.model.Position;
import org.opencraft.server.model.Rotation;

/**
 * @author Mark
 *
 */
public class Path{

	private Npc caller;
	protected final Logger logger = Logger.getLogger("Paths");
	protected final BlockingQueue<PathData> destinations = new LinkedBlockingQueue<PathData>();


	public Path(Npc caller) {
		
		this.caller = caller;
		// TODO Auto-generated constructor stub
	}

	
	public void put(Position position, int delay)
	{
		try {
			destinations.put(new PathData(position, delay));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.info("Unable to put path:" 
					+ "[x:" + position.getX() 
					+ "][y: " + position.getY() 
					+ "][z: " + position.getZ()
					+ "][delay: " + delay + "]"
					+ " into npc path.");
		}
	}
	
	
	public void navigate() {
		cleanUp();
		// TODO Auto-generated method stub
		PathData destination = destinations.peek();

		if(destination.isReady())
		{
			caller.setPosition(destination.getPosition());
			caller.setRotation(calculateRotation(
											destination.getPosition().getX(), 
												destination.getPosition().getY(), 
													caller.getPosition().getX(), 
														caller.getPosition().getY()
														)
									);
			destinations.remove(destination);
		}
		/*
		if(caller.getPosition().equals(destination.getPosition()))
		{
			logger.info("Destroying peath queue");
			destinations.remove(destination);
			return;
		}
		
		if(destination.isReady())
		{
			logger.info("Destination is ready!");
			int xDiff = caller.getPosition().getX() - destination.getPosition().getX();
			int yDiff = caller.getPosition().getY() - destination.getPosition().getY();
			int zDiff = caller.getPosition().getZ() - destination.getPosition().getZ();
			Position gotoPosition = new Position(
					
					caller.getPosition().getX() + getDistanceToMove(xDiff), 
					
					caller.getPosition().getY() + getDistanceToMove(yDiff), 
					
					caller.getPosition().getZ() + getDistanceToMove(zDiff)
											);
			caller.setPosition(gotoPosition);
		}*/
		
	}
	
	
	private Rotation calculateRotation(int destinedX, int destinedY, int currentX, int currentY) {
		// TODO Auto-generated method stub
		int value = (int) (Math.atan2(
				(destinedY - currentY), (destinedX-currentX)));
		Rotation rot = new Rotation(value, value);
		return rot;
	}


	protected void cleanUp() {
		// TODO Auto-generated method stub
	}


	private int getDistanceToMove(int cartDiff)
	{
		int default_distance = 1;
		return ((cartDiff == 0) ? 
					0 : 
						(
							cartDiff > 0 ? 
								-1 * default_distance 
									: 1 * default_distance
						)
			);
	}
	
	private class PathData
	{
		private long executionTime;
		private Position position;
		
		/**
		 * 
		 * @param position
		 * @param delay The amount of time to delay the path execution, in seconds.
		 */
		public PathData(Position position, int delay)
		{
			this.position = position;
			this.executionTime = System.currentTimeMillis() + (delay * 1000);
		}
		
		public boolean isReady()
		{
			boolean ready = (System.currentTimeMillis() >= executionTime) ? true : false;
			return ready;
		}
		public Position getPosition()
		{
			return position;
		}
	}


	
	
}
