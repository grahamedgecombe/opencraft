package org.opencraft.server.game.impl;

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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.opencraft.server.extensions.brushes.Brush;
import org.opencraft.server.extensions.brushes.BrushCommand;
import org.opencraft.server.extensions.brushes.StandardBrush;
import org.opencraft.server.game.GameModeAdapter;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;

/**
 * An experimental game mode. Useful for testing things. Currently logs players
 * in memory and greets them accordingly. Now also has the ability to use
 * brushes Has some of the official commands of which op and deop relies on
 * memory
 * @author Søren Enevoldsen
 */

public class ExperimentalGameMode extends GameModeAdapter {
	
	/**
	 * A map of players who have connected.
	 */
	private Map<String, Date> visitors = new HashMap<String, Date>();
	
	/**
	 * Registers the experimental game mode commands.
	 */
	public ExperimentalGameMode() {
		registerCommand("brush", BrushCommand.getBrushCommand());
	}
	
	/**
	 * Get default 1 size block brush
	 * @return A 1 size block brush
	 */
	private Brush getDefaultBrush() {
		return new StandardBrush();
	}
	
	@Override
	public void playerConnected(Player player) {
		String name = player.getName();
		// New player?
		if (!visitors.containsKey(name)) {
			World.getWorld().broadcast("Welcome " + name + ".");
		} else {
			// Welcome back.
			String lastConnectDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(visitors.get(name));
			World.getWorld().broadcast("Welcome back " + name + ".");
			player.getSession().getActionSender().sendChatMessage("You last connect was: " + lastConnectDate + ".");
			
		}
		// Remember connection time
		visitors.put(name, new Date());
		
		// Give them a brush, if they do not have one already
		if(!player.isAttributeSet("brush")) {
			player.setAttribute("brush", getDefaultBrush());
		}
	}
	
	@Override
	public void playerDisconnected(Player player) {
		// we persist this :) player.removeAttribute("brush");
	}
	
	@Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
		((Brush) player.getAttribute("brush")).paint(player, level, x, y, z, mode, type);
	}
	
	@Override
	public void broadcastChatMessage(Player player, String message) { // TODO:
		// rank
		// colors?
		World.getWorld().broadcast(player, player.getName() + ": " + message);
	}
	
}
