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
import org.opencraft.server.cmd.CommandAdapter;
import org.opencraft.server.extensions.brushes.Brush;
import org.opencraft.server.extensions.brushes.DiamondBrush;
import org.opencraft.server.extensions.brushes.SquareBrush;
import org.opencraft.server.extensions.brushes.LineBrush;

import org.opencraft.server.game.GameModeAdapter;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;

/**
 * An experimental game mode. Useful for testing things.
 * Currently logs players in memory and greets them accordingly.
 * Now also has the ability to use brushes
 * @author Søren Enevoldsen
 */

public class ExperimentalGameMode extends GameModeAdapter {

	//People who have connected
	private Map<String,Date> visitors = new HashMap<String,Date>();
		
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
		player.setAttribute("brush", getDefaultBrush());
	}
	
	@Override
	public void playerDisconnected(Player player) {
		player.removeAttribute("brush");
	}
	
	@Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
			((Brush)player.getAttribute("brush")).paint(player, level, x, y, z, mode, type);
	}
	
	//Handles the /brush command
	Command brushCommand = new CommandAdapter() {
		
		public void execute(Player player, String args[]) {
			setup(player, args);

			String action = nextString("error");
			
			if (action.equals("radius")) {
				int newRadius = nextInt(-1);
				if (newRadius != -1) {
					((Brush)player.getAttribute("brush")).setRadius(newRadius);
					sendMsg("Brush radius changed");
				}
				else
					sendError("/brush radius [radius]");
				return;
			}
			else if (action.equals("default") || action.equals("standard")) {
				player.removeAttribute("brush");
				player.setAttribute("brush", getDefaultBrush());
				sendMsg("Now using standard brush");
				return;
			}
			else if (action.equals("delete")) {
				String onOff = nextString("error");
				if (onOff.equals("1")) {
					((Brush)player.getAttribute("brush")).setUseForDelete(true);
					sendMsg("Using this brush to delete");
				}
				else if (onOff.equals("0")) {
					((Brush)player.getAttribute("brush")).setUseForDelete(false);
					sendMsg("Using standard brush to delete");
				}
				else
					sendError("/brush delete [1|0]");
				return;
			}
			else if (action.equals("type")) {
				String brush = nextString("error");
				int bRadius = ((Brush)player.getAttribute("brush")).getRadius();
				Brush newBrush;
				if (brush.equals("square"))
					newBrush = new SquareBrush();
				else if (brush.equals("diamond"))
					newBrush = new DiamondBrush();
				else if (brush.equals("line"))
					newBrush = new LineBrush();
				else {			
					sendError("/brush type [square|diamond|line]");
					return;
				}
				newBrush.setRadius(bRadius);
				player.setAttribute("brush", newBrush);
				sendMsg("Brush type changed to " + brush);
				return;
			}
			else {
				sendError("/brush radius [radius]", true);
				sendError("/brush [standard|default", false);
				sendError("/brush delete [1|0]", false);
				sendError("/brush type [square|diamond|line]", false);
			}
		}
	};
}

