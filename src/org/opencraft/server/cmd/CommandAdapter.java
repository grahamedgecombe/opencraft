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
import org.opencraft.server.net.ActionSender;

/**
 * A class designed to make commands easier to make and
 * look less messy
 * Remember to call setup()
 * @author Søren Enevoldsen
 *
 */

abstract public class CommandAdapter implements Command {
	
	private int index = 0;
	private String[] arguments;
	ActionSender as;
	protected String preError = "USAGE:\n";
	
	/**
	 * Returns next argument as a int
	 * @param onerror Value to return on error (eg. exception)
	 * @return Next value as int
	 */
	protected int nextInt(int onError) {
		int result;
		try {
			result = Integer.parseInt(arguments[index++]);
		} catch (Exception e) {
			return onError;
		}
		return result;
	}
	
	/**
	 * Returns next argument as a String
	 * @param onError Value to return on error (eg. exception)
	 * @return Next value as String
	 */
	protected String nextString(String onError) {
		String result;
		try {
			result = arguments[index++];
		} catch (Exception e) {
			return onError;
		}
		return result;
	}
	
	/**
	 * Returns next argument as a double
	 * @param onError Value to return on error (eg. exception)
	 * @return Next value as double
	 */
	protected double nextDouble(Float onError) {
		double result;
		try {
			result = Double.parseDouble(arguments[index++]);
		} catch (Exception e) {
			return onError;
		}
		return result;
	}
	
	/**
	 * Resets the argument index to zero
	 */
	protected void reset() {
		index = 0;
	}
	
	/**
	 * Resets the argument index to specific value
	 * @param newArgIndex The new arg index value
	 */
	protected void reset(int newArgIndex) {
		index = newArgIndex;
	}
	
	/**
	 * Setup the internal counters and arguments
	 * and error handling
	 */
	protected void setup(Player player, String args[]) {
		reset();
		arguments = args;
		as = player.getSession().getActionSender();
	}
	
	/**
	 * Sends an error message back
	 * @param error The error to send
	 */
	protected void sendError(String error) {
		as.sendChatMessage(preError);
		as.sendChatMessage(error);
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
		as.sendChatMessage(msg);
	}
	
	@Override
	public void execute(Player player, String[] args) {
		setup(player, args);
		// TODO Auto-generated method stub

	}

}
