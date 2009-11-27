/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.model.npc;

import java.util.Random;

import org.opencraft.server.model.Player;
import org.opencraft.server.model.Position;

/**
 * @author Mark
 *
 */
public class FollowerBehavior<T extends Npc> extends NPCBehavior<T> {

	private Player leader;

	
	public FollowerBehavior(T caller, Player leader) {
		super(caller);
		this.leader = leader;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateNpc() {
		// TODO Auto-generated method stub
		
		//if(checkForBreak()) return;
		
		caller.getPath().put(leader.getPosition(), 3);
	}
	


}
