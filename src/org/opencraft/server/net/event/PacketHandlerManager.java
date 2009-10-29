package org.opencraft.server.net.event;

import java.util.Map;
import java.util.logging.Logger;

import org.opencraft.server.io.PersistenceManager;
import org.opencraft.server.net.MinecraftSession;
import org.opencraft.server.net.packet.Packet;

/**
 * A class which manages <code>PacketHandler</code>s.
 * @author Graham Edgecombe
 *
 */
public class PacketHandlerManager {
	
	/**
	 * The singleton instance.
	 */
	private static final PacketHandlerManager INSTANCE = new PacketHandlerManager();
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(PacketHandlerManager.class.getName());

	/**
	 * Gets the packet handler manager instance.
	 * @return The packet handler manager instance.
	 */
	public static PacketHandlerManager getPacketHandlerManager() {
		return INSTANCE;
	}
	
	/**
	 * An array of packet handlers.
	 */
	private PacketHandler[] handlers = new PacketHandler[256];
	
	/**
	 * Default private constructor.
	 */
	@SuppressWarnings("unchecked")
	private PacketHandlerManager() {
		Map<Integer, PacketHandler> handlers = (Map<Integer, PacketHandler>) PersistenceManager.getPersistenceManager().load("data/packetHandlers.xml");
		for(Map.Entry<Integer, PacketHandler> handler : handlers.entrySet()) {
			this.handlers[handler.getKey()] = handler.getValue();
		}
	}

	/**
	 * Handles a packet.
	 * @param session The session.
	 * @param packet The packet.
	 */
	public void handlePacket(MinecraftSession session, Packet packet) {
		PacketHandler handler = handlers[packet.getDefinition().getOpcode()];
		if(handler != null) {
			handler.handlePacket(session, packet);
		} else {
			logger.info("Unhandled packet : " + packet + ".");
		}
	}

}
