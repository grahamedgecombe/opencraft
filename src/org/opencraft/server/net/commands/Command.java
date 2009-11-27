/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.net.commands;

import java.util.logging.Logger;

import org.opencraft.server.model.Player.Rights;
import org.opencraft.server.net.MinecraftSession;





/**
 * @author Mark
 *
 * The basic command class for chat commands.
 */
public abstract class Command {

	private Logger logger = Logger.getLogger("Command");
	protected int playerAccessLevel = 0;
	
	
	/**
	 * The default syntax for a command; can be overwritten.
	 * @param session
	 * The client to send the syntax to.
	 */
	public void showSyntax(MinecraftSession session)
	{
		session.getActionSender().sendChatMessage("No syntax specified for this command.");
	}
	
	/**
	 * Calls the protected, abstract execute method.
	 * Some of the functions that a command does are constant,
	 * and must be carried out in this method.
	 * @param session
	 * @param args
	 * The input arguments for the command.
	 * @param showSyntax
	 * If the command should be executed or if the player requested
	 * help on the command's syntax.
	 */
	public void execute(MinecraftSession session, String[] args, boolean showSyntax)
	{
		setAccessLevel();

		if(showSyntax)
		{
			showSyntax(session);
		}
		else if(playerAccessLevel <= session.getPlayer().getRights().toInteger())
		{
			try
			{
					logger.info(
								session.getPlayer().getName()
									+ " has executed: <br>" 
									+ "<b>" + this.getClass().getName() + "</b>"
					);
					
					logger.info(formatArgumentsForOutput(args));

				execute(session, args);
			}
			catch(Exception e)
			{
				try {
					logger.info("Error while executing command.");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					
				}
			}
		}
		else
		{
			session.getActionSender().sendChatMessage("You do not have the right access level of " 
						+ ((playerAccessLevel == 1) ? Rights.MODERATOR.toString() : Rights.ADMINISTRATOR.toString()) 
							+ " to do this command.");
		}
	}
	/**
	 * Meant to be used for a logging system, for administrative purposes.
	 * @param args
	 * @return
	 */
	private String formatArgumentsForOutput(String[] args)
	{
		String displayArgs = "[ARGUMENTS]: ";
		for(int i = 0; i < args.length; i++)
		{
			displayArgs += "(" + args[i] + "), ";
		}
		displayArgs = displayArgs.substring(0, displayArgs.length() -2);
		
		return displayArgs;

	}
	/**
	 * What to do when the subclass command is executed.
	 * @param session
	 * @param args
	 * @throws Exception
	 */
	protected abstract void execute(MinecraftSession session, String[] args) throws Exception;

	/**
	 * 
	 * Set the permission levels for this command, such as player, administrator
	 * or moderator.
	 */
	protected abstract void setAccessLevel();
}
