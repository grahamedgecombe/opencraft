package org.opencraft.server.extensions.brushes;

import org.opencraft.server.cmd.CommandAdapter;
import org.opencraft.server.model.Player;

public final class BrushCommand extends CommandAdapter {
	
	private static final BrushCommand INSTANCE = new BrushCommand(); 
	
	public static BrushCommand getBrushCommand() {
		return INSTANCE;
	}
	
	public Brush getDefaultBrush() {
		return new SquareBrush(0);
	}
	
	protected void usage() {
		sendError("/brush radius [radius]", true);
		sendError("/brush [standard|default", false);
		sendError("/brush delete [1|0]", false);
		sendError("/brush type [square|diamond|line|flat]", false);
	}
	
	public void execute(Player player, String args[]) {
		setup(player, args);

		String action = getStringArg(0);
		
		if (args.length == 1) {
			if (action.equals("default") ||
					action.equals("standard")) {
				player.setAttribute("brush", getDefaultBrush());
				sendMsg("Now using standard brush");
			}
			else
				usage();
		}
		else if (args.length == 2) {
			
			if (action.equals("radius")) {
				try {
					int newRadius = getIntArg(1);
					((Brush)player.getAttribute("brush")).setRadius(newRadius);
					sendMsg("Brush radius changed");
				} catch (Exception e) {
					sendError("/brush radius [radius]");
				}
				
				
			}
			else if (action.equals("delete")) {
				String onOff = getStringArg(1);
				if (onOff.equals("1")) {
					((Brush)player.getAttribute("brush")).setUseForDelete(true);
					sendMsg("Using this brush to delete");
				}
				else if (onOff.equals("0")) {
					((Brush)player.getAttribute("brush")).setUseForDelete(false);
					sendMsg("Using standard brush to delete");
				}
				else
					sendError("/brush delete [1|0]");
			}
			else if (action.equals("type")) {
				String brush = getStringArg(1);
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
					sendError("/brush type [square|diamond|line|flat]");
					return;
				}
				newBrush.setRadius(bRadius);
				player.setAttribute("brush", newBrush);
				sendMsg("Brush type changed to " + brush);
			}
			else
				usage();
		}
		else
			usage();
	}
}
