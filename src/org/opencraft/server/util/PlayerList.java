package org.opencraft.server.util;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the OpenCraft nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

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
