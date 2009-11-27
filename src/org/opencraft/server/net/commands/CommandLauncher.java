/**
 *  This code belongs to Mark Farrell.
 *  Do not leech or steal my code in any way, shape or form.
 */
package org.opencraft.server.net.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.opencraft.server.model.Player;
import org.opencraft.server.model.World;
import org.opencraft.server.net.MinecraftSession;





/**
 * @author Mark
 * 
 * The CommandLauncher literally launches a command object based on given arguments.
 *
 */
public class CommandLauncher {

	
	/**
	 * The package path for implemented commands.
	 */
	protected String orgPath = "org.opencraft.server.net.commands.execs.";
	protected ArrayList<String> args;
	protected MinecraftSession session;
	protected boolean showSyntax = false;
	/**
	 * 
	 * @param commandString
	 * The construct splits the commandString into different arguments
	 * after every space and removes the first argument which is the command name.
	 * @param session
	 */
	public CommandLauncher(String commandString, MinecraftSession session)
	{
		this.session = session;
		
		args = new ArrayList();
		
		
		String[] input = commandString.split(" ");
		
		
		for(int i = 0; i < input.length; i++)
		{
			input[i].trim();
			if(i == 0 && input[i].equalsIgnoreCase("help"))
			{
				showSyntax = true;
				continue;
			}
			args.add(input[i]);
		}
		
		
		orgPath += pasculateCommand(getArgs()[0].toLowerCase());
		
		args.remove(0);
		
		execute();
	}
	
	
	/**
	 * Apply pascal format which is where every word 
	 * starts with a capital letter.
	 * @param string
	 * @return
	 */
	private String pasculateCommand(String string) {
		// TODO Auto-generated method stub
		
		string = string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
		string += "Command";
		return string;
	}

	public String getPath()
	{
		return orgPath;
	}

	public String[] getArgs() {
		// TODO Auto-generated method stub
		return (String[]) args.toArray(new String[args.size()]);
	}
	
	protected void execute()
	{
			try {
				getCommand().execute(session, getArgs(), showSyntax);
	
		} catch(Exception ex) {
			
			session.getActionSender().sendChatMessage("Error while processing command.",
													"Base Input: " + getPath());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Creates a new command subclass object based on the user's input.
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public Command getCommand() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		return ((Command) Class.forName(getPath()).newInstance());
	}
}
