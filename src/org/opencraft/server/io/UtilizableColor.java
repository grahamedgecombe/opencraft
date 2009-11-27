/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.io;

import java.util.Random;

/**
 * @author Mark
 * This class contains the enum UtilizableColors 
 * for generating colors based on an artistic category.
 * In earlier versions of the source, this file was also responsible for 
 * index utilities for blocks.
 */
	public enum UtilizableColor
	{
		WARM(21, 22, 23, 30, 31, 32, 33),
		CHECKERED(49, 36),
		GRASSY(24, 25);
		
		
		private int[] colors;
		
		
		/**
		 * 
		 * @param colors
		 * 
		 * A dynamic array of colors that should fall within the category.
		 */
		private UtilizableColor(int... colors)
		{
			this.colors = colors;
		}
		
		/**
		 * 
		 * @return
		 * A color based on the default random key.
		 */
		public byte generateColor()
		{
			Random blockRandom = new Random();
			return (byte) colors[blockRandom.nextInt(colors.length)];
		}
	}


