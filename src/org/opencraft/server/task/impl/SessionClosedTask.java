package org.opencraft.server.task.impl;

import org.apache.mina.core.session.IoSession;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.task.Task;

/**
 * A task which closes a session.
 * @author Graham Edgecombe
 *
 */
public final class SessionClosedTask implements Task {
	
	/**
	 * The session.
	 */
	private final IoSession session;

	/**
	 * Creates the session closed task.
	 * @param session The session.
	 */
	public SessionClosedTask(IoSession session) {
		this.session = session;
	}

	@Override
	public void execute() {
		((MinecraftSession) session.removeAttribute("attachment")).destroy();
	}

}
