package org.opencraft.server.net.commands.execs;

import org.opencraft.server.model.Entity;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;
import org.opencraft.server.model.npc.FollowerBehavior;
import org.opencraft.server.model.npc.NPCBehavior;
import org.opencraft.server.model.npc.Npc;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.net.commands.Command;

public class EntityCommand extends Command{

	@Override
	protected void execute(MinecraftSession session, String[] args)
			throws Exception {
		// TODO Auto-generated method stub
		final String name = "&a" + args[0];
		Npc ent = new Npc(name);
		ent.setBehavior(new FollowerBehavior(ent, session.getPlayer()));
		ent.setId(0);
		ent.setPosition(session.getPlayer().getPosition());
		ent.setRotation(session.getPlayer().getRotation());
		World.getWorld().register(ent);
	}

	@Override
	protected void setAccessLevel() {
		// TODO Auto-generated method stub
		
	}

}
