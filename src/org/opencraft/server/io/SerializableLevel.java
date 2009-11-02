package org.opencraft.server.io;

/*
 * This code was not written by the OpenCraft team. This code is the property of Markus Persson,
 * AKA Notch (www.minecraft.net) and is used under "fair use" policy to interpret and save
 * Minecraft game maps. The OpenCraft team claims no responsibility for this code, its use, or
 * its effectiveness.
 */

import org.opencraft.server.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Level loading and saving code.
 * Maintains compatibility with map editors and other server softwares.
 * @author Brett Russell <br />
 * <i>With code from www.minecraftwiki.net and Minecraft (www.minecraft.net), authored by Markus Persson (AKA Notch).</i>
 */
public class SerializableLevel implements Serializable {

	/**
	 * The serial version UID for this object.
	 */
	public static final long serialVersionUID = 0L;
	
	/**
	 * The width of the map.
	 */
	public int width;
	
	/**
	 * The height of the map.
	 */
	public int height;
	
	/**
	 * The depth of the map.
	 */
	public int depth;
	
	/**
	 * The blocks in the map (stored as an array of bytes)
	 */
	public byte[] blocks;
	
	/**
	 * The name of the map.
	 */
	public String name;
	
	/**
	 * The name of the map's creator.
	 */
	public String creator;
	
	/**
	 * The date of the map's creation.
	 */
	public long createTime;
	
	/**
	 * The X spawn coordinate.
	 */
	public int xSpawn;
	
	/**
	 * The Y spawn coordinate.
	 */
	public int ySpawn;
	
	/**
	 * The Z spawn coordinate.
	 */
	public int zSpawn;
	
	/**
	 * The player's spawning rotation.
	 */
	public float rotSpawn;
	
	/**
	 * Kept for consistency. Unused.
	 */
	private boolean networkMode = false;
	
	/**
	 * Kept for consistency. Unused.
	 */
	public boolean creativeMode;
	
	/**
	 * Kept for consistency. Unused.
	 */
	public int waterLevel, skyColor, fogColor, cloudColor;
	
	/**
	 * Kept for consistency. Unused.
	 */
	int unprocessed = 0;
	
	/**
	 * Kept for consistency. Unused.
	 */
	private int tickCount = 0;
	
	/**
	 * The map's filename.
	 */
	private transient String filename = Configuration.getMapFilename();
	
	/**
	 * The logger instance.
	 */
	private transient Logger logger = Logger.getLogger(SerializableLevel.class.getName());

	/**
	 * Constructor.
	 */
  	public SerializableLevel() {
  	}

  	/**
  	 * Match this object to a locally created copy made when loading a map.
  	 */
  	public void setData(int width, int depth, int height, byte[] blocks, 
  			int xSpawn, int ySpawn, int zSpawn, float rotSpawn, 
  			String name, String creator, long createTime, boolean networkMode, boolean creativeMode, 
  			int waterLevel, int skyColor, int fogColor, int cloudColor, int unprocessed, int tickCount) {
  		this.width = width;
  		this.depth = depth;
  		this.height = height;
  		this.blocks = blocks;
  		this.xSpawn = xSpawn;
  		this.ySpawn = ySpawn;
  		this.zSpawn = zSpawn;
  		this.rotSpawn = rotSpawn;
  		this.name = name;
  		this.creator = creator;
  		this.createTime = createTime;
  		this.networkMode = networkMode;
  		this.creativeMode = creativeMode;
  		this.waterLevel = waterLevel;
  		this.skyColor = skyColor;
  		this.fogColor = fogColor;
  		this.cloudColor = cloudColor;
  		this.unprocessed = unprocessed;
  		this.tickCount = tickCount;
  		System.gc();
  	}

  	/**
  	 * Load a map.
  	 * @throws IOException
  	 * @throws RuntimeException
  	 * @throws ClassNotFoundException 
  	 */
  	public boolean load() throws IOException, RuntimeException, ClassNotFoundException {
  		try {
  			FileInputStream fis = new FileInputStream("./data/" + this.filename);
  			GZIPInputStream gzis = new GZIPInputStream(fis);
  			DataInputStream dis = new DataInputStream(gzis);
  			if (dis.readInt() != 656127880) {
  				logger.log(Level.WARNING, "Map file is invalid.");
  	  			dis.close();
  	  			return false;
  			}
  			if (dis.readByte() != 2) {
  				logger.log(Level.WARNING, "Map file is not of version 2. Cannot be loaded.");
  	  			dis.close();
  	  			return false;
  			}
  			ObjectInputStream ois = new ObjectInputStream(gzis);
  			SerializableLevel localLevel = (SerializableLevel)ois.readObject();
  			dis.close();
  			ois.close();
  			setData(localLevel.width, localLevel.depth, localLevel.height, localLevel.blocks, 
  					localLevel.xSpawn, localLevel.ySpawn, localLevel.zSpawn, localLevel.rotSpawn, 
  					localLevel.name, localLevel.creator, localLevel.createTime, localLevel.networkMode, 
  					localLevel.creativeMode, localLevel.waterLevel, localLevel.skyColor, localLevel.fogColor,
  					localLevel.cloudColor, localLevel.unprocessed, localLevel.tickCount);
  			return true;
  		} catch(IOException e) {
  			e.printStackTrace();
  			return false;
  		}
  	}

	// save in file called filename
	public void save() {
		FileOutputStream fos = null;
		GZIPOutputStream gzos = null;
		ObjectOutputStream out = null;
		DataOutputStream outputstream = null;
		try {
			fos = new FileOutputStream("./data/" + this.filename);
			gzos = new GZIPOutputStream(fos);
			outputstream = new DataOutputStream(gzos);
			outputstream.writeInt(0x271bb788);
			outputstream.writeByte(2);
			out = new ObjectOutputStream(gzos);
			out.writeObject(this);
			outputstream.close();
			out.close();
			logger.info("Successfully saved " + this.filename + ".");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}