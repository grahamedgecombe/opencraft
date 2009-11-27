package org.opencraft.server.model;

/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe.
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

import org.opencraft.server.net.MinecraftSession;

/**
 * Represents a connected player.
 * @author Graham Edgecombe
 *
 */
public final class Player extends Entity {
	/**
	 * Represents the rights of a player.
	 * @author Graham Edgecombe
	 *
	 */
	public enum Rights {
		
		/**
		 * A standard account.
		 */
		PLAYER(0),
		
		/**
		 * A player-moderator account.
		 */
		MODERATOR(1),
		
		/**
		 * An administrator account.
		 */
		ADMINISTRATOR(2);
		
		/**
		 * The integer representing this rights level.
		 */
		private int value;
		
		/**
		 * Creates a rights level.
		 * @param value The integer representing this rights level.
		 */
		private Rights(int value) {
			this.value = value;
		}
		
		/**
		 * Gets an integer representing this rights level.
		 * @return An integer representing this rights level.
		 */
		public int toInteger() {
			return value;
		}

		/**
		 * Gets rights by a specific integer.
		 * @param value The integer returned by {@link #toInteger()}.
		 * @return The rights level.
		 */
		public static Rights getRights(int value) {
			if(value == 1) {
				return MODERATOR;
			} else if(value == 2) {
				return ADMINISTRATOR;
			} else {
				return PLAYER;
			}
		}
	}
	/**
	 * The player's session.
	 */
	private final MinecraftSession session;
	
	/**
	 * The player's name.
	 */
	private final String name;
	
	
	/**
	 * The player's authorization level.
	 */
	
	private final Rights rights = Rights.PLAYER;
	
	
	/**
	 * The player's skill which increases as 
	 * he/she builds structures.
	 */
	
	private final Skill skill;
	/**
	 * Creates the player.
	 * @param name The player's name.
	 */
	public Player(MinecraftSession session, String name) {
		this.session = session;
		this.name = name;
		this.skill = new Skill(this);
	}
	
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets the player's session.
	 * @return The session.
	 */
	public MinecraftSession getSession() {
		return session;
	}
	
	
	/**
	 * Returns the player's rights which is private.
	 */
	public Rights getRights()
	{
		return rights;
	}

	public Skill getSkill() {
		// TODO Auto-generated method stub
		return skill;
	}
}
