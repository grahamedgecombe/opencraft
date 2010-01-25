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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.opencraft.server.Configuration;
import org.opencraft.server.game.GameModeAdapter;
import org.opencraft.server.model.Level;
import org.opencraft.server.model.Player;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * A game mode which delegates methods to a script.
 * @author Graham Edgecombe
 *
 */
public class ScriptedGameMode extends GameModeAdapter {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ScriptedGameMode.class.getName());
	
	/**
	 * The python interpreter.
	 * We were origianlly using javax.script but that had some issues with
	 * commands.
	 */
	private PythonInterpreter interpreter = new PythonInterpreter();
	
	/**
	 * Creates the scripted game mode.
	 * @throws IOException if an I/O error occurs.
	 */
	public ScriptedGameMode() throws IOException {
		init();
	}
	
	/**
	 * Initializes the script engine and evaluates the script.
	 * @throws IOException if an I/O error occurs.
	 */
	private void init() throws IOException {
		String name = Configuration.getConfiguration().getScriptName();
		
		logger.info("Evaluating script...");
		InputStream is = new FileInputStream("./data/scripts/" + name);
		try {
			interpreter.execfile(is);
		} finally {
			is.close();
		}
		
		logger.info("Initializing script...");
		delegate("init", this);
	}
	
	/**
	 * Delegates a call to the engine.
	 * @param method The method name.
	 * @param args The arguments.
	 */
	private boolean delegate(String method, Object... args) {
		PyObject m = interpreter.get(method);
		if(m != null) {
			try {
				m.__call__(Py.javas2pys(args));
			} catch (Exception ex) {
				logger.log(java.util.logging.Level.SEVERE, "Error invoking method.", ex);
			}
		} else {
			return false;
		}
		return true;
	}
	
	@Override
	public void playerConnected(Player player) {
		if(!delegate("playerConnected", player)) {
			super.playerConnected(player);
		}
	}
	
	@Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
		if(!delegate("setBlock", player, level, x, y, z, mode, type)) {
			super.setBlock(player, level, x, y, z, mode, type);
		}
	}
	
	@Override
	public void playerDisconnected(Player player) {
		if(!delegate("playerDisconnected", player)) {
			super.playerDisconnected(player);
		}
	}
	
	@Override
	public void broadcastChatMessage(Player player, String message) {
		if(!delegate("broadcastChatMessage", player, message)) {
			super.broadcastChatMessage(player, message);
		}
	}
	
	@Override
	public void tick() {
		if(!delegate("tick")) {
			super.tick();
		}
	}
	
}
