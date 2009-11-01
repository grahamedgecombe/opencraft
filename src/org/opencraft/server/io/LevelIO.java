package org.opencraft.server.io;

/*
 * This code was not written by the OpenCraft team. This code is the property of Markus Persson,
 * AKA Notch (www.minecraft.net) and is used under "fair use" policy to interpret and save
 * Minecraft game maps. The OpenCraft team claims no responsibility for this code, its use, or
 * its effectiveness.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * LevelIO loading and saving code.
 * @author Notch (Markus Persson)
 *
 */
public class LevelIO implements Serializable {
	public static final long serialVersionUID = 0L;
	public int width;
	public int height;
	public int depth;
	public byte[] blocks;
	public String name;
	public String creator;
  	public long createTime;
  	public int xSpawn;
  	public int ySpawn;
  	public int zSpawn;
  	public float rotSpawn;
  	public ArrayList entities;
  	int unprocessed = 0;
  	private int tickCount = 0;
  	public transient String filename = "server_level.dat";

  	public LevelIO() {
  	}

  	public LevelIO(String paramString) {
  		this.filename = paramString;
  	}

  	public void setSpawnPos(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
  		this.xSpawn = paramInt1;
  		this.ySpawn = paramInt2;
  		this.zSpawn = paramInt3;
  		this.rotSpawn = paramFloat;
  	}

  	public void setData(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
  		this.width = paramInt1;
  		this.depth = paramInt2;
  		this.height = paramInt3;
  		this.blocks = paramArrayOfByte;
  		System.gc();
  	}

  	public void load() throws IOException, RuntimeException {
  		FileInputStream localFileInputStream = null;
  		GZIPInputStream localGZIPInputStream = null;
  		ObjectInputStream localObjectInputStream = null;
  		DataInputStream localDataInputStream = null;
  		try {
  			localFileInputStream = new FileInputStream(this.filename);
  			localGZIPInputStream = new GZIPInputStream(localFileInputStream);
  			localDataInputStream = new DataInputStream(localGZIPInputStream);
  			if (localDataInputStream.readInt() != 656127880) {
  				throw new RuntimeException("Invalid level file");
  			}
  			if (localDataInputStream.readByte() != 2) {
  				throw new RuntimeException("Error: LevelIO version is not 2, this is unsupported!");
  			}
  			localObjectInputStream = new ObjectInputStream(localGZIPInputStream);
  			LevelIO localLevel = (LevelIO)localObjectInputStream.readObject();
  			localDataInputStream.close();
  			localObjectInputStream.close();
  			setData(localLevel.width, localLevel.depth, localLevel.height, localLevel.blocks);
  			setSpawnPos(localLevel.xSpawn, localLevel.ySpawn, localLevel.zSpawn, localLevel.rotSpawn);
  		} catch (ClassNotFoundException localClassNotFoundException) {
  			localClassNotFoundException.printStackTrace();
  		}
  	}

  	public void save(String paramString) throws IOException {
  		FileOutputStream localFileOutputStream = null;
  		GZIPOutputStream localGZIPOutputStream = null;
  		Object localObject = null;
  		DataOutputStream localDataOutputStream = null;
  		localFileOutputStream = new FileOutputStream(paramString);
  		localGZIPOutputStream = new GZIPOutputStream(localFileOutputStream);
  		localDataOutputStream = new DataOutputStream(localGZIPOutputStream);
  		localDataOutputStream.writeByte(1);
  		localDataOutputStream.writeShort(this.xSpawn);
  		localDataOutputStream.writeShort(this.ySpawn);
  		localDataOutputStream.writeShort(this.zSpawn);
  		localDataOutputStream.writeByte(0);
  		localDataOutputStream.writeByte(1);
  		localDataOutputStream.writeShort(this.width);
  		localDataOutputStream.writeShort(this.depth);
  		localDataOutputStream.writeShort(this.height);

  		for (int i = 0; i < this.width * this.depth * this.height; ++i) {
  			localDataOutputStream.writeByte(this.blocks[i]);
  		}
  		localDataOutputStream.close();
  	}
}