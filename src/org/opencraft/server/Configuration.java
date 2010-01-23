package org.opencraft.server;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.opencraft.server.game.impl.CreativeGameMode;

/**
 * Manages server configuration.
 * @author Graham Edgecombe
 */
public class Configuration {
	
	/**
	 * The configuration instance.
	 */
	private static Configuration configuration;
	
	/**
	 * Reads and parses the configuration.
	 * @throws FileNotFoundException if the configuration file is not present.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void readConfiguration() throws FileNotFoundException, IOException {
		synchronized (Configuration.class) {
			Properties props = new Properties();
			InputStream is = new FileInputStream("./data/opencraft.properties");
			try {
				props.load(is);
				configuration = new Configuration(props);
			} finally {
				is.close();
			}
		}
	}
	
	/**
	 * Gets the configuration instance.
	 * @return The configuration instance.
	 */
	public static Configuration getConfiguration() {
		synchronized (Configuration.class) {
			return configuration;
		}
	}
	
	/**
	 * The server name.
	 */
	private String name;
	
	/**
	 * The server MOTD.
	 */
	private String message;
	
	/**
	 * The filename of the map file.
	 */
	private static String mapFilename;
	
	/**
	 * The maximum allowed player count.
	 */
	private int maximumPlayers;
	
	/**
	 * The radius of a sponge's effectiveness.
	 */
	private int spongeRadius;
	
	/**
	 * Public server flag.
	 */
	private boolean publicServer;
	
	/**
	 * Verify names flag.
	 */
	private boolean verifyNames;
	
	/**
	 * The game mode.
	 */
	private String gameMode;
	
	/**
	 * The script name.
	 */
	private String scriptName;
	
	/**
	 * Creates the configuration from the specified properties object.
	 * @param props The properties object.
	 */
	public Configuration(Properties props) {
		name = props.getProperty("name", "OpenCraft Server");
		message = props.getProperty("message", "http://opencraft.sf.net/");
		maximumPlayers = Integer.valueOf(props.getProperty("max_players", "16"));
		publicServer = Boolean.valueOf(props.getProperty("public", "false"));
		verifyNames = Boolean.valueOf(props.getProperty("verify_names", "false"));
		mapFilename = props.getProperty("filename", "server_level.dat");
		spongeRadius = Integer.valueOf(props.getProperty("sponge_radius", "2"));
		gameMode = props.getProperty("game_mode", CreativeGameMode.class.getName());
		scriptName = props.getProperty("script_name", null);
	}
	
	/**
	 * Gets the server name.
	 * @return The server name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the server MOTD.
	 * @return The server MOTD.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the maximum player count.
	 * @return The maximum player count.
	 */
	public int getMaximumPlayers() {
		return maximumPlayers;
	}
	
	/**
	 * Gets the public server flag.
	 * @return The public server flag.
	 */
	public boolean isPublicServer() {
		return publicServer;
	}
	
	/**
	 * Gets the verify names flag.
	 * @return The verify names flag.
	 */
	public boolean isVerifyingNames() {
		return verifyNames;
	}
	
	/**
	 * Gets the map filename.
	 * @return The map's filename.
	 */
	public String getMapFilename() {
		return mapFilename;
	}
	
	/**
	 * Gets the range at which a sponge is effective.
	 * @return The sponge radius.
	 */
	public int getSpongeRadius() {
		return spongeRadius;
	}
	
	/**
	 * Gets the game mode class.
	 * @return The game mode class.
	 */
	public String getGameMode() {
		return gameMode;
	}
	
	/**
	 * Gets the script name.
	 * @return The script name.
	 */
	public String getScriptName() {
		return scriptName;
	}
	
}
