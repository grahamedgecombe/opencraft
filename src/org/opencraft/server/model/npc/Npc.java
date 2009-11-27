/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.model.npc;

import org.opencraft.server.model.Entity;
import org.opencraft.server.task.Task;

/**
 * @author Mark
 *
 */
public class Npc extends Entity{

	
	/**
	 * The behavior system for the npc, which dictacts how it should
	 * interact with the 3D minecraft world.
	 */
		

	@SuppressWarnings("unchecked")
	private NPCBehavior behavior;
	
	
	
	/**
	 * The path that the Npc follows.
	 */
	private final Path pathSystem = new Path(this);



	private String name;
	
	
	public Npc()
	{
		name = "Npc";
	}
	public Npc(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}
	/**
	 * Every npc must have a name, just as a player does.
	 * The name loads the skin data for an entity.
	 * 
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	/**
	 * Set's the behavior system for the Npc.
	 */
	public void setBehavior(NPCBehavior behavior)
	{
		this.behavior = behavior;
	}
	
	/**
	 * 
	 * @return The path that the npc follows.
	 */
	public Path getPath() {
		// TODO Auto-generated method stub
		return pathSystem;
	}
	
	/**
	 * Calls a behavior update for the npc.
	 */
	public void behave() {
		// TODO Auto-generated method stub
		this.behavior.updateNpc();
	}
	
	
	

}
