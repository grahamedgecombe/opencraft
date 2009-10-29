package org.opencraft.server.net.packet;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Represents a single packet.
 * @author Graham Edgecombe
 *
 */
public final class Packet {
	
	/**
	 * The packet definition.
	 */
	private final PacketDefinition definition;
	
	/**
	 * A map of field name to field data.
	 */
	private final Map<String, Object> fields;
	
	/**
	 * Creates the packet.
	 * @param definition The definition.
	 * @param fields The field map.
	 */
	public Packet(PacketDefinition definition, Map<String, Object> fields) {
		this.definition = definition;
		this.fields = Collections.unmodifiableMap(new HashMap<String, Object>(fields));
	}
	
	/**
	 * Gets the definition of this packet.
	 * @return The definition of this packet.
	 */
	public PacketDefinition getDefinition() {
		return definition;
	}
	
	/**
	 * Gets a numeric field.
	 * @param fieldName The name of the field.
	 * @return The value of the numeric field.
	 */
	public Number getNumericField(String fieldName) {
		return (Number) fields.get(fieldName);
	}
	
	/**
	 * Gets a string field.
	 * @param fieldName The name of the field.
	 * @return The value of the string field.
	 */
	public String getStringField(String fieldName) {
		return (String) fields.get(fieldName);
	}
	
	/**
	 * Gets a byte array field.
	 * @param fieldName The name of the field.
	 * @return The value of the byte array field.
	 */
	public IoBuffer getByteArrayField(String fieldName) {
		return (IoBuffer) fields.get(fieldName);
	}

}
