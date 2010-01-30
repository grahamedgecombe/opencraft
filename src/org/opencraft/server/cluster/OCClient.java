/*
 * OpenCraft License
 * 
 * Copyright (c) 2009 Graham Edgecombe, Søren Enevoldsen, Mark Farrell and Brett Russell.
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
package org.opencraft.server.cluster;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.opencraft.server.net.Connectable;
import org.opencraft.server.net.OCSession;
import org.opencraft.server.net.State;
import org.opencraft.server.net.codec.MinecraftCodecFactory;
import org.opencraft.server.net.packet.Packet;

/**
 * @author Mark Farrell
 * A client that is connecting to a server in the cluster.
 */
public abstract class OCClient extends Connectable{

	/**
	 * The task's logging system.
	 */
	private static final Logger logger = Logger.getLogger(OCClient.class.getCanonicalName());

	
	private static final int AWAIT_TIME = 3000;
	private final String IP;
	private final int PORT;

	
	/**
	 * What connects to the info server.
	 */
	private SocketConnector connector;


	/**
	 * The protocol factory for the client.
	 */
	private final MinecraftCodecFactory factory;
	
	public OCClient(String ip, int port, MinecraftCodecFactory factory)
	{
		this.IP = ip;
		this.PORT = port;
		this.factory = factory;
		this.connector = new NioSocketConnector();
	}
	/**
	 * Attempt to connect to the info server.
	 */
	public void connect()
	{
		try
		{
			ConnectFuture future = connector.connect(new InetSocketAddress(IP, PORT));
			future.awaitUninterruptibly(AWAIT_TIME);

			IoSession session = future.getSession();
			session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(factory));
			state = State.READY;
			this.onConnect(session);
		}
		catch(RuntimeIoException e)
		{
			this.onFailure(e);
		}
	}

	/**
	 * Requires what to do when it connects or fails to connect.
	 */
	public abstract void onConnect(IoSession session);
	public abstract void onFailure(Throwable e);
}
