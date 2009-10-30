package org.opencraft.server.model;

import org.opencraft.server.Constants;
import org.opencraft.server.io.LevelGzipper;
import org.opencraft.server.net.MinecraftSession;

/**
 * Manages the in-game world.
 * @author Graham Edgecombe
 *
 */
public final class World {
	
	/**
	 * The singleton instance.
	 */
	private static final World INSTANCE = new World();
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return INSTANCE;
	}
	
	/**
	 * The level.
	 */
	private final Level level = new Level();
	
	/**
	 * Default private constructor.
	 */
	private World() {
		/* empty */
	}
	
	/**
	 * Gets the level.
	 * @return The level.
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Registers a session.
	 * @param session The session.
	 * @param username The username.
	 * @param verificationKey The verification key.
	 */
	public void register(MinecraftSession session, String username, String verificationKey) {
		session.getActionSender().sendLoginResponse(Constants.PROTOCOL_VERSION, "OpenCraft", "Loading...", false);
		LevelGzipper.getLevelGzipper().gzipLevel(session);
	}

}
