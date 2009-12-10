package org.opencraft.server.cmd;

/**
 * An immutable class which holds and parses the parameters passed to a
 * commmand.
 * @author Graham Edgecombe
 */
public class CommandParameters {
	
	/**
	 * Arguments array.
	 */
	private final String[] args;
	
	/**
	 * Creates the command parameters class.
	 * @param args The arguments of the command.
	 */
	public CommandParameters(String[] args) {
		this.args = args.clone();
	}
	
	/**
	 * Gets the number of arguments.
	 * @return The number of arguments.
	 */
	public int getArgumentCount() {
		return args.length;
	}
	
	/**
	 * Gets the string argument at <code>pos</code>.
	 * @param pos The index in the array.
	 * @return The argument.
	 */
	public String getStringArgument(int pos) {
		return args[pos];
	}
	
	/**
	 * Gets the double argument at <code>pos</code>.
	 * @param pos The index in the array.
	 * @return The argument.
	 * @throws NumberFormatException if the argument is not a double.
	 */
	public double getDoubleArgument(int pos) {
		return Double.valueOf(args[pos]);
	}
	
	/**
	 * Gets the integer argument at <code>pos</code>.
	 * @param pos The index in the array.
	 * @return The argument.
	 * @throws NumberFormatException if the argument is not an integer.
	 */
	public int getIntegerArgument(int pos) {
		return Integer.valueOf(args[pos]);
	}
	
}
