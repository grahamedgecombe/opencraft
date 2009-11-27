package org.opencraft.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.opencraft.server.Configuration;
import org.opencraft.server.model.Entity;
import org.opencraft.server.model.Player;

/**
 * A class which manages the list of connected players.
 * @author Graham Edgecombe
 *
 */
public class EntityList<T extends Entity> {
	
	/**
	 * The maximum number of players.
	 */
	public static final int DEFAULT_MAXIMUM = Configuration.getConfiguration().getMaximumPlayers();
	
	/**
	 * The entity array.
	 */
	private ArrayList<T> entities;
	
	/**
	 * Default public constructor.
	 */
	public EntityList() {
		/* empty */
		 this.entities = new ArrayList<T>(DEFAULT_MAXIMUM);
		 System.out.println(this.entities.size() + " <- max length");
	}
	
	/**
	 * Public constructor with a specified entity limit.
	 */
	public EntityList(int length)
	{
		this.entities = new ArrayList<T>(length);
	}
	/**
	 * Gets a list of online players.
	 * @return A list of online players.
	 */
	public Collection<Entity> getEntities() {
		List<Entity> playerList = new LinkedList<Entity>();
		for(Entity p : entities) {
			if(p != null) {
				playerList.add(p);
			}
		}
		return Collections.unmodifiableCollection(playerList);
	}
	
	/**
	 * Constructs an entity list based on a previously existing array list.
	 */
	public EntityList(ArrayList<T> arr)
	{
		entities = arr;
	}
	
	/**
	 * Adds a player.
	 * @param entity The new player.
	 */
	public boolean add(T entity) {
			boolean success = entities.add(entity);
			entity.setId(entities.size() - 1);
			return success;
			
	}
	
	/**
	 * Removes a player.
	 * @param player The player to remove.
	 */
	public void remove(T player) {
		int id = player.getId();
		if(id != -1 && entities.get(id) == player) {
			entities.remove(id);
		}
		player.setId(-1);
	}

	/**
	 * Gets the number of online players.
	 * @return The player list size.
	 */
	public int size() {
		return entities.size();
	}
	
}
