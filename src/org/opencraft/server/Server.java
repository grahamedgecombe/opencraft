package org.opencraft.server;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.opencraft.server.net.SessionHandler;
import org.opencraft.server.task.TaskQueue;
import org.opencraft.server.task.impl.HeartbeatTask;
import org.opencraft.server.task.impl.UpdateTask;

/**
 * The core class of the OpenCraft server.
 * @author Graham Edgecombe
 * 
 */
public final class Server {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * The entry point of the server application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Server().start();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "An error occurred whilst loading the server.", t);
		}
	}
	
	/**
	 * The socket acceptor.
	 */
	private final IoAcceptor acceptor = new NioSocketAcceptor();
	
	/**
	 * Creates the server.
	 * @throws IOException if an I/O error occurs.
	 * @throws FileNotFoundException if the configuration file is not found.
	 */
	public Server() throws FileNotFoundException, IOException {
		logger.info("Starting OpenCraft server...");
		logger.info("Configuring...");
		Configuration.readConfiguration();
		acceptor.setHandler(new SessionHandler());
		TaskQueue.getTaskQueue().schedule(new UpdateTask());
		TaskQueue.getTaskQueue().schedule(new HeartbeatTask());
	}
	
	/**
	 * Starts the server.
	 * @throws IOException if an I/O error occurs.
	 */
	public void start() throws IOException {
		logger.info("Binding to port " + Constants.PORT + "...");
		acceptor.bind(new InetSocketAddress(Constants.PORT));
		logger.info("Ready for connections.");
	}

}
