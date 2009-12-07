package org.opencraft.server.extensions.brushes;

import org.opencraft.server.cmd.Command;
import org.opencraft.server.cmd.CommandParameters;
import org.opencraft.server.model.Player;

public final class BrushCommand implements Command {
	
	private static final BrushCommand INSTANCE = new BrushCommand(); 
	
	public static BrushCommand getBrushCommand() {
		return INSTANCE;
	}
	
	private void usage(Player player) {
		player.getActionSender().sendChatMessage("/brush radius [radius]");
		player.getActionSender().sendChatMessage("/brush [standard|default");
		player.getActionSender().sendChatMessage("/brush delete [1|0]");
		player.getActionSender().sendChatMessage("/brush type [square|diamond|line|flat]");
	}
	
	public void execute(Player player, CommandParameters parameters) {

		String action = parameters.getStringArgument(0);
		
		if (parameters.getArgumentCount() == 1) {
			if (action.equals("default") ||
					action.equals("standard")) {
				player.setAttribute("brush", SquareBrush.DEFAULT_BRUSH);
				player.getActionSender().sendChatMessage("Now using standard brush");
			}
			else
				usage(player);
		}
		else if (parameters.getArgumentCount() == 2) {
			
			if (action.equals("radius")) {
				try {
					int newRadius = parameters.getIntegerArgument(1);
					((Brush)player.getAttribute("brush")).setRadius(newRadius);
					player.getActionSender().sendChatMessage("Brush radius changed");
				} catch (Exception e) {
					player.getActionSender().sendChatMessage("/brush radius [radius]");
				}
				
				
			}
			else if (action.equals("delete")) {
				String onOff = parameters.getStringArgument(1);
				if (onOff.equals("1")) {
					((Brush)player.getAttribute("brush")).setUseForDelete(true);
					player.getActionSender().sendChatMessage("Using this brush to delete");
				}
				else if (onOff.equals("0")) {
					((Brush)player.getAttribute("brush")).setUseForDelete(false);
					player.getActionSender().sendChatMessage("Using standard brush to delete");
				}
				else
					player.getActionSender().sendChatMessage("/brush delete [1|0]");
			}
			else if (action.equals("type")) {
				String brush = parameters.getStringArgument(1);
				int bRadius = ((Brush)player.getAttribute("brush")).getRadius();
				Brush newBrush;
				if (brush.equals("square"))
					newBrush = new SquareBrush();
				else if (brush.equals("diamond"))
					newBrush = new DiamondBrush();
				else if (brush.equals("line"))
					newBrush = new LineBrush();
				else if (brush.equals("flat"))
					newBrush = new FlatBrush();
				else {
					player.getActionSender().sendChatMessage("/brush type [square|diamond|line|flat]");
					return;
				}
				newBrush.setRadius(bRadius);
				player.setAttribute("brush", newBrush);
				player.getActionSender().sendChatMessage("Brush type changed to " + brush);
			}
			else
				usage(player);
		}
		else
			usage(player);
	}
}
