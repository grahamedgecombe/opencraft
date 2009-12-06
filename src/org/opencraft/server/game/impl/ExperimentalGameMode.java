package org.opencraft.server.game.impl;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe and Brett Russell.
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

import org.opencraft.server.cmd.Command;
import org.opencraft.server.extensions.brushes.Brush;
import org.opencraft.server.extensions.brushes.DiamondBrush;
import org.opencraft.server.extensions.brushes.SquareBrush;
import org.opencraft.server.extensions.brushes.LineBrush;

import org.opencraft.server.game.GameModeAdapter;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;
import org.opencraft.server.net.ActionSender;

/**
 * An experimental game mode. Useful for testing things.
 * Currently logs players in memory and greets them accordingly.
 * Now also has the ability to use brushes
 * @author Søren Enevoldsen
 */

public class ExperimentalGameMode extends GameModeAdapter {

	//People who have connected
	private Map<String,Date> visitors = new HashMap<String,Date>();
	//Their brushes
	public Map<String,Brush> brushes = new HashMap<String, Brush>();
	
	
	public ExperimentalGameMode() {
		registerCommand("brush", brushCommand);
	}
	
	/**
	 * Get default 1 size block brush
	 * @return A 1 size block brush
	 */
	private Brush getDefaultBrush() {
		return new SquareBrush(0);
	}
	
	@Override
	public void playerConnected(Player player) {
		String name = player.getName();
		//New player?
		if (!visitors.containsKey(name)) {
			World.getWorld().broadcast("Welcome " + name + ".");
		}
		else {
			//Welcome back.
			String lastConnectDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
					DateFormat.SHORT).format(visitors.get(name));
			World.getWorld().broadcast("Welcome back " + name + ".");
			player.getSession().getActionSender().sendChatMessage("You last connect was: " + lastConnectDate + ".");
			
		}
		//Remember connection time
		visitors.put(name, new Date());
		
		//Give them a brush
		brushes.put(name, getDefaultBrush());
	}
	
	@Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
			brushes.get(player.getName()).paint(player, level, x, y, z, mode, type);
	}
	
	//Handles the /brush command
	Command brushCommand = new Command() {
		private String[] usage = new String[] {
				"/brush radius [0-3]",
				"/brush default|standard",
				"/brush delete [1|0]",
				"/brush type [square|diamond|line]"
		};
		
		public void execute(Player player, String args[]) {
			ActionSender as = player.getSession().getActionSender();
			try {
				if (args[0].equals("radius")) {
					try {
					int radius = Integer.parseInt(args[1]);
					brushes.get(player.getName()).setRadius(radius);
					as.sendChatMessage("Brush radius changed.");
					}
					catch (Exception e) {
						usage(player, 0);
					}
				}
				else if (args[0].equals("default") || args[0].equals("standard")) {
					brushes.remove(player.getName());
					brushes.put(player.getName(), getDefaultBrush());
					as.sendChatMessage("Using standard brush");
				}
				else if (args[0].equals("delete")) {
					if (args[1].equals("1")) {
						brushes.get(player.getName()).setUseForDelete(true);
						as.sendChatMessage("Now using this brush to delete");
					}
					else if (args[1].equals("0")) {
						brushes.get(player.getName()).setUseForDelete(false);
						as.sendChatMessage("No longer using this brush to delete");
					}
					else
						usage(player, 2);
				}
				else if (args[0].equals("type")) {
					String brush;
					try {
						brush = args[1];
					} catch (Exception e) {
						usage(player, 3);
						return;
					}
					int bRadius = brushes.get(player.getName()).getRadius();
					Brush newBrush;
					if (brush.equals("square"))
						newBrush = new SquareBrush();
					else if (brush.equals("diamond"))
						newBrush = new DiamondBrush();
					else if (brush.equals("line"))
						newBrush = new LineBrush();
					else {
						usage(player, 3);
						return;
					}	
					newBrush.setRadius(bRadius);
					brushes.remove(player.getName());
					brushes.put(player.getName(), newBrush);
					as.sendChatMessage("Brush type changed");
				}
				else {
					usage(player);
				}
			} catch (Exception e) {
				as.sendChatMessage("Error occurred. Wrong arguments perhaps.");
				usage(player);
			}
		}
		
		//Send usage message
		public void usage(Player player) {
			ActionSender as = player.getSession().getActionSender();
			as.sendChatMessage("Usage:");
			for (String use : usage)	
				as.sendChatMessage(use);
		}
		
		//Send a specific usage message
		public void usage(Player player, int usageIndex)
		{
			player.getSession().getActionSender().sendChatMessage(usage[usageIndex]);
		}
	};
}

