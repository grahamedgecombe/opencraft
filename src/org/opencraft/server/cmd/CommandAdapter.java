package org.opencraft.server.cmd;

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

import org.opencraft.server.model.Player;

/**
 * A class designed to make commands easier to make and
 * look less messy
 * Remember to call setup()
 * @author Søren Enevoldsen
 *
 */

abstract public class CommandAdapter implements Command {
	
	private String[] arguments;
	protected Player player;
	protected String preError = "USAGE:\n";
	
	/**
	 * Sets up the getter methods 
	 * and enables changes to be done to players
	 * @param player The player
	 * @param args The argument array
	 */
	protected void setup(Player player, String[] args) {
		this.player = player;
		this.arguments = args;
	}
	
	/**
	 * Get a String at the given index
	 * @param index The index into the arguments
	 * @return The String
	 */
	protected String getStringArg(int index) {
		return arguments[index];
	}
	
	/**
	 * Get an int value at the given index
	 * @param index The index into the arguments
	 * @return The int
	 */
	protected int getIntArg(int index) {
		return Integer.parseInt(arguments[index]);
	}
	
	/**
	 * Get a double value at the given index
	 * @param index The index into the arguments
	 * @return The double
	 */
	protected double getDoubleArg(int index) {
		return Double.parseDouble(arguments[index]);
	}
	
	/**
	 * Sends an error message back
	 * @param error The error to send
	 */
	protected void sendError(String error) {
		player.getSession().getActionSender().sendChatMessage(preError);
		player.getSession().getActionSender().sendChatMessage(error);
	}
	
	/**
	 * Sends an error message back
	 * @param error The error to send
	 * @param preError Whether to send preError message
	 */
	protected void sendError(String error, boolean preError) {
		if (preError)
			sendError(error);
		else
			sendMsg(error);
	}
	
	/**
	 * Sends a message to the user
	 * @param msg The message
	 */
	protected void sendMsg(String msg) {
		player.getSession().getActionSender().sendChatMessage(msg);
	}
	
	@Override
	public void execute(Player player, String[] args) {
		setup(player, args);
		// TODO Auto-generated method stub

	}

}
