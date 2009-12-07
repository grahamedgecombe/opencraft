package org.opencraft.server.cmd.impl;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Søren Enevoldsen.
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

import org.opencraft.server.cmd.Command;
import org.opencraft.server.cmd.CommandParameters;
import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;

/**
 * Official /op command
 *  **NEEDS PERSISTENCE**
 * @author Søren Enevoldsen
 *
 */

public class OperatorCommand implements Command {

	@Override
	public void execute(Player player, CommandParameters params) {
		//Player using command is OP?
		if (player.getAttribute("isOperator") != null && player.getAttribute("IsOperator").equals("true")) {
			if (params.getArgumentCount() == 1) {		
				for (Player other : World.getWorld().getPlayerList().getPlayers()) {
					if (other.getName().equals(params.getStringArgument(0))) {
						other.setAttribute("IsOperator", "true");
						other.getActionSender().sendChatMessage("You are now an OP");
						player.getActionSender().sendChatMessage(other.getName() + " is now an OP");
						return;
					}
				}
				//Player not found
				player.getActionSender().sendChatMessage(params.getStringArgument(0) + " was not found");
			}
			else
				player.getActionSender().sendChatMessage("Wrong number of arguments");
				player.getActionSender().sendChatMessage("/op <name>");
		}
		else
			player.getActionSender().sendChatMessage("You must be OP to do that");			
	}
}
