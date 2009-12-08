package org.opencraft.server.game;

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

import java.util.Map;

import org.opencraft.server.cmd.Command;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;

/**
 * An interface which represents a specific type of game mode.
 * @author Graham Edgecombe
 * @author Søren Enevoldsen
 */
public interface GameMode {
	
	/**
	 * Gets a map of commands that are supported in this game mode.
	 * @return The map of commands.
	 */
	public Map<String, Command> getCommands();

	/**
	 * Notification of player connected
	 * @param player The connected player
	 */
	public void playerConnected(Player player);
	
	/**
	 * Event handler for a player disconnect
	 * Remember player has already disconnected!
	 * @param player The disconnected player
	 */
	public void playerDisconnected(Player player);
	
	/**
	 * Handles block adding and removing
	 * @param player The player setting the block
	 * @param level The level
	 * @param mode 1/0 adding/removing
	 * @param type typeId of the block
	 */
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type);
	
	/**
	 * Broadcasts a chat message.
	 * @param player The sending player.
	 * @param message The chat message.
	 */
	public void broadcastChatMessage(Player player, String message);
}
