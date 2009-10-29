package org.opencraft.server.task.impl;

import org.apache.mina.core.session.IoSession;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.task.Task;

/**
 * A task which opens a session.
 * @author Graham Edgecombe
 *
 */
public final class SessionOpenedTask implements Task {
	
	/**
	 * The session.
	 */
	private final IoSession session;

	/**
	 * Creates the session opened task.
	 * @param session The session.
	 */
	public SessionOpenedTask(IoSession session) {
		this.session = session;
	}

	@Override
	public void execute() {
		session.setAttribute("attachment", new MinecraftSession(session));
	}

}
