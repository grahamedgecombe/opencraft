package org.opencraft.server.util;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe, Søren Enevoldsen and Brett Russell.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages sets of users such as operators and banned users.
 * @author Graham Edgecombe
 *
 */
public class SetManager {
	
	/**
	 * The singleton instance.
	 */
	private static SetManager INSTANCE = new SetManager();
	
	/**
	 * Gets the set manager instance.
	 * @return The set manager instance.
	 */
	public static SetManager getSetManager() {
		return INSTANCE;
	}
	
	/**
	 * Default private constructor.
	 */
	private SetManager() {
		/* empty */
	}
	
	/**
	 * A map of sets.
	 */
	private Map<String, Set<String>> sets = new HashMap<String, Set<String>>();
	
	/**
	 * Gets a set.
	 * @param name The name of the set.
	 * @return The set (read only).
	 * @throws IllegalArgumentException if the set does not exist.
	 */
	public Set<String> getSet(String name) {
		if(!sets.containsKey(name)) {
			throw new IllegalArgumentException("Set does not exist.");
		}
		return Collections.unmodifiableSet(sets.get(name));
	}
	
	/**
	 * Adds the name of a player to the specified set.
	 * @param name The name of the set.
	 * @param player The name of the player.
	 */
	public void addToSet(String name, String player) {
		if(sets.containsKey(name)) {
			sets.get(name).add(player);
		}
	}
	
	/**
	 * Removes the name of a player from the specified set.
	 * @param name The name of the set.
	 * @param player The name of the player.
	 */
	public void removeFromSet(String name, String player) {
		if(sets.containsKey(name)) {
			sets.get(name).remove(player);
		}
	}
	
	/**
	 * Removes all player's names from the specified set.
	 * @param name The name of the set.
	 */
	public void removeAllFromSet(String name) {
		if(sets.containsKey(name)) {
			sets.get(name).clear();
		}
	}
	
	/**
	 * Reloads sets from the file system.
	 * @throws IOException if an I/O error occurs.
	 */
	public void reloadSets() throws IOException {
		reloadSet("ops");
		reloadSet("banned");
	}

	/**
	 * Reloads the specified set from the file system.
	 * @param name The set.
	 * @throws IOException if an I/O error occurs.
	 */
	private void reloadSet(String name) throws IOException {
		Set<String> set = sets.get(name);
		if(set == null) {
			set = new HashSet<String>();
			sets.put(name, set);
		}
		File f = new File("./data/" + name + ".txt");
		if(!f.exists()) {
			f.createNewFile();
		}
		BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		try {
			String line;
			while((line = rdr.readLine()) != null) {
				set.add(line.trim());
			}
		} finally {
			rdr.close();
		}
	}
	
}
