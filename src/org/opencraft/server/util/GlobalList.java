/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.util;

import java.util.ArrayList;

import org.opencraft.server.model.Entity;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.npc.Npc;
import org.opencraft.server.task.TaskQueue;

/**
 * @author Mark
 *
 */
public class GlobalList {
	


	/**
	 * The player list.
	 */
	private final EntityList<Player> playerList;
	
	/**
	 * The list of npcs.
	 */
	
	private final EntityList<Npc> npcList;
	
	
	/**
	 * Default public constructor.
	 */
	public GlobalList(int playerCap, int npcCap) {
		// TODO Auto-generated constructor stub
		this.playerList = new EntityList<Player>(playerCap);
		this.npcList = new EntityList<Npc>(npcCap);
	}
	
	/**
	 * Combines the list of both players and npcs, 
	 * to simplify other parts of the application,
	 * such as the UpdateTask.
	 */
	public EntityList<Entity> getAllEntities()
	{
		ArrayList<Entity> combo = new ArrayList<Entity>();
		combo.addAll(npcList.getEntities());
		combo.addAll(playerList.getEntities());
		EntityList<Entity> ent = new EntityList<Entity>(combo.size());
		for(int i = 0; i < combo.size(); i++)
		{
			ent.add(combo.get(i));
		}
		return ent;
	}
	
	public EntityList<Player> getPlayers()
	{
		return playerList;
	}
	
	public EntityList<Npc> getNpcs()
	{
		return npcList;
	}

	public boolean add(Entity entity) {
		// TODO Auto-generated method stub
		if(entity instanceof Player)
		{
			return playerList.add((Player) entity);
		}
		else if(entity instanceof Npc)
		{
			return npcList.add((Npc) entity);
		}
		return false;
	}

	public void remove(Entity entity) {
		// TODO Auto-generated method stub
		if(entity instanceof Player)
		{
			 playerList.remove((Player) entity);
		}
		else if(entity instanceof Npc)
		{
			 npcList.remove((Npc) entity);
		}
	}

	public boolean isEntityAt(int x, int y, int z, int radius) {
		// TODO Auto-generated method stub
		ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.addAll(npcList.getEntities());
		entities.addAll(playerList.getEntities());
		
		
		for(int i = 0; i < entities.size(); i++)
		{
			System.out.println("" +
					"TNT X: " + x + " :: ENTITY X: + entities.get(i).getPosition().getX()" + entities.get(i).getPosition().getX()
				);
				if(
					Math.abs(entities.get(i).getPosition().getX() - x) <= radius
					 	&& Math.abs(entities.get(i).getPosition().getY() - y) <= radius
					 		&& Math.abs(entities.get(i).getPosition().getZ() - z) <= radius
								)
									{
										return true;
									}
		}
		return false;
	}

}
