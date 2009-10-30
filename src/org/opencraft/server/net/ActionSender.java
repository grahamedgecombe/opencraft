package org.opencraft.server.net;

import org.opencraft.server.model.Level;
import org.opencraft.server.model.World;
import org.opencraft.server.net.packet.PacketBuilder;
import org.opencraft.server.net.packet.PacketManager;
import org.opencraft.server.task.Task;
import org.opencraft.server.task.TaskQueue;

/**
 * A utility class for sending packets.
 * @author Graham Edgecombe
 *
 */
public class ActionSender {
	
	/**
	 * The session.
	 */
	private MinecraftSession session;
	
	/**
	 * Creates the action sender.
	 * @param session The session.
	 */
	public ActionSender(MinecraftSession session) {
		this.session = session;
	}
	
	/**
	 * Sends a login response.
	 * @param protocolVersion The protocol version.
	 * @param name The server name.
	 * @param message The server message of the day.
	 * @param op Operator flag.
	 */
	public void sendLoginResponse(int protocolVersion, String name, String message, boolean op) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(0));
		bldr.putByte("protocol_version", protocolVersion);
		bldr.putString("server_name", name);
		bldr.putString("server_message", message);
		bldr.putByte("user_type", op ? 1 : 0);
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a login failure.
	 * @param message The message to send to the client.
	 */
	public void sendLoginFailure(String message) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(14));
		bldr.putString("reason", message);
		session.send(bldr.toPacket());
		session.close();
	}

	/**
	 * Sends the level init packet.
	 */
	public void sendLevelInit() {
		session.setAuthenticated();
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(2));
		session.send(bldr.toPacket());
	}

	/**
	 * Sends a level block/chunk.
	 * @param len The length of the chunk.
	 * @param chunk The chunk data.
	 * @param percent The percentage.
	 */
	public void sendLevelBlock(int len, byte[] chunk, int percent) {
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(3));
		bldr.putShort("chunk_length", len);
		bldr.putByteArray("chunk_data", chunk);
		bldr.putByte("percent", percent);
		session.send(bldr.toPacket());
	}

	/**
	 * Sends the level finish packet.
	 */
	public void sendLevelFinish() {
		TaskQueue.getTaskQueue().push(new Task() {
			public void execute() {
				// for thread safety
				session.setReady();
			}
		});
		final Level level = World.getWorld().getLevel();
		PacketBuilder bldr = new PacketBuilder(PacketManager.getPacketManager().getOutgoingPacket(4));
		bldr.putShort("width", level.getWidth());
		bldr.putShort("height", level.getHeight());
		bldr.putShort("depth", level.getDepth());
		session.send(bldr.toPacket());
	}

}
