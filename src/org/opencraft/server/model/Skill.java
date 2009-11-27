/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.model;

import org.opencraft.server.Configuration;

/**
 * @author Mark
 *
 */
public class Skill {

	/**
	 * The minimal time between block builds for the
	 * user to receive full experience value. (Discourages spamming)
	 */
	private final static int timeModifier = 1000;
	
	
	/**
	 * The multiplier to determine how much experience
	 * is needed for a player to level up. 
	 * [Example:]
	 * If the multiplier was set to 9 and the player was level 2, 
	 * they would require 18 experience for them to level up.
	 */
	private final static int levelRequirementMultiplier = 4;
	
	
	/**
	 * The player's level representation.
	 * Every so many experience points, the player levels up.
	 */
	protected int level;
	
	/**
	 * The experience obtained from building blocks.
	 */
	protected int experience;
	
	/**
	 * The player representation for this skill class.
	 */
	protected Player player;
	
	/**
	 * The last time experience was gained.
	 * This very is implemented to reduce block spamming.
	 */
	protected long lastBlockConstructTime;



	/**
	 * The constructor for the Skill class.
	 * @param player
	 *
	 */
	public Skill(Player player)
	{
		this.player = player;
		this.lastBlockConstructTime = System.currentTimeMillis();
		this.level = 1;
		this.experience = 0;
	}
	/**
	 * The "father" to all other methods related to experience gain.
	 * @param amount
	 * The amount of experience to gain excluding all multipliers.
	 * @param mode
	 * The block mode
	 * @param type
	 * The block type
	 */
	public void addExperience(int amount, int mode, int type)
	{
		int toAdd = calculateGain(amount);		
		experience += toAdd;
		checkForLevelUp();
	}
	
	
	/**
	 * Checks to see if the player's experience is high enough
	 * for him/her to increase in rank.
	 */
	protected void checkForLevelUp() {
		// TODO Auto-generated method stub
		boolean leveledUp = false;
		while(experience >= level * levelRequirementMultiplier)
		{
			experience -= level * levelRequirementMultiplier;
			level++;	
			leveledUp = true;
		}
		if(leveledUp)
		{
			player.getSession().getActionSender().sendChatMessage(
					"Congratulations! You are now level " + level + ".");
		}
	}

	/**
	 * 
	 * @param amount
	 * @return The integer amount of experience that should be gained 
	 * for the player based on the input amount.
	 * This does not actually modify the player's experience, but just
	 * calculates a formula.
	 */
	private int calculateGain(int amount)
	{
		int toAdd = amount * Configuration.getExpRate();
		long currentExecuteTime = System.currentTimeMillis();
		if((currentExecuteTime - lastBlockConstructTime) < timeModifier)
		{
			toAdd = 0;
		}
		else
		{
			lastBlockConstructTime = currentExecuteTime;
		}
		return toAdd;
	}
	public int getLevel() {
		// TODO Auto-generated method stub
		return level;
	}
}
