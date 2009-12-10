package org.opencraft.server.model;

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

import java.util.HashMap;
import java.util.Map;

import org.opencraft.server.net.ActionSender;
import org.opencraft.server.net.MinecraftSession;

/**
 * Represents a connected player.
 * @author Graham Edgecombe
 */
public final class Player extends Entity {
	
	/**
	 * The player's session.
	 */
	private final MinecraftSession session;
	
	/**
	 * The player's name.
	 */
	private final String name;
	
	/**
	 * A map of attributes that can be attached to this player.
	 */
	private final Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * Creates the player.
	 * @param name The player's name.
	 */
	public Player(MinecraftSession session, String name) {
		this.session = session;
		this.name = name;
	}
	
	/**
	 * Sets an attribute of this player.
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 * @return The old value of the attribute, or <code>null</code> if there was
	 * no previous attribute with that name.
	 */
	public Object setAttribute(String name, Object value) {
		return attributes.put(name, value);
	}
	
	/**
	 * Gets an attribute.
	 * @param name The name of the attribute.
	 * @return The attribute, or <code>null</code> if there is not an attribute
	 * with that name.
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	/**
	 * Checks if an attribute is set.
	 * @param name The name of the attribute.
	 * @return <code>true</code> if set, <code>false</code> if not.
	 */
	public boolean isAttributeSet(String name) {
		return attributes.containsKey(name);
	}
	
	/**
	 * Removes an attribute.
	 * @param name The name of the attribute.
	 * @return The old value of the attribute, or <code>null</code> if an
	 * attribute with that name did not exist.
	 */
	public Object removeAttribute(String name) {
		return attributes.remove(name);
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
	 * Gets this player's action sender.
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return session.getActionSender();
	}
	
}
