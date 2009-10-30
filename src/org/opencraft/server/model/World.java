package org.opencraft.server.model;

import org.opencraft.server.Constants;
import org.opencraft.server.io.LevelGzipper;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.util.PlayerList;

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
	 * The player list.
	 */
	private final PlayerList playerList = new PlayerList();
	
	/**
	 * Default private constructor.
	 */
	private World() {
		/* empty */
	}
	
	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public PlayerList getPlayerList() {
		return playerList;
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
		char[] nameChars = username.toCharArray();
		for(char nameChar : nameChars) {
			if(nameChar < ' ' || nameChar > '\177') {
				session.getActionSender().sendLoginFailure("Invalid name!");
				return;
			}
		}
		for(Player p : playerList.getPlayers()) {
			if(p.getName().equalsIgnoreCase(username)) {
				p.getSession().getActionSender().sendLoginFailure("Logged in from another computer.");
			}
		}
		final Player player = new Player(session, username);
		if(!playerList.add(player)) {
			player.getSession().getActionSender().sendLoginFailure("Too many players online!");
			return;
		}
		session.setPlayer(player);
		session.getActionSender().sendLoginResponse(Constants.PROTOCOL_VERSION, "OpenCraft", "Loading...", false);
		LevelGzipper.getLevelGzipper().gzipLevel(session);
	}

	/**
	 * Unregisters a session.
	 * @param session
	 */
	public void unregister(MinecraftSession session) {
		if(session.isAuthenticated()) {
			playerList.remove(session.getPlayer());
		}
	}

	/**
	 * Completes registration of a session.
	 * @param session The sessino.
	 */
	public void completeRegistration(MinecraftSession session) {
		session.getActionSender().sendChatMessage("Welcome to OpenCraft!");
	}

}
