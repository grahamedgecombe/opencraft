/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.model.npc;


/**
 * @author Mark
 *
 */
public abstract class NPCBehavior<T extends Npc> {

	
	protected T caller;
	
	public NPCBehavior(T caller)
	{
		this.caller = caller;
	}

	public abstract void updateNpc();
}
