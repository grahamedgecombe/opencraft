package org.opencraft.server.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.opencraft.server.Configuration;
import org.opencraft.server.model.Player;

/**
 * A class which manages the list of connected players.
 * @author Graham Edgecombe
 *
 */
public class PlayerList {
	
	/**
	 * The maximum number of players.
	 */
	private final int maximumPlayers = Configuration.getConfiguration().getMaximumPlayers();
	
	/**
	 * The player array.
	 */
	private final Player[] players = new Player[maximumPlayers];
	
	/**
	 * The size of the player array.
	 */
	private int size = 0;
	
	/**
	 * Default public constructor.
	 */
	public PlayerList() {
		/* empty */
	}
	
	/**
	 * Gets a list of online players.
	 * @return A list of online players.
	 */
	public Collection<Player> getPlayers() {
		List<Player> playerList = new LinkedList<Player>();
		for(Player p : players) {
			if(p != null) {
				playerList.add(p);
			}
		}
		return Collections.unmodifiableCollection(playerList);
	}
	
	/**
	 * Adds a player.
	 * @param player The new player.
	 * @return <code>true</code> if they could be added, <code>false</code>
	 * if not.
	 */
	public boolean add(Player player) {
		for(int i = 0; i < players.length; i++) {
			if(players[i] == null) {
				players[i] = player;
				player.setId(i);
				size++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes a player.
	 * @param player The player to remove.
	 */
	public void remove(Player player) {
		int id = player.getId();
		if(id != -1 && players[id] == player) {
			players[id] = null;
			size--;
		}
		player.setId(-1);
	}

	/**
	 * Gets the number of online players.
	 * @return The player list size.
	 */
	public int size() {
		return size;
	}
	
}
