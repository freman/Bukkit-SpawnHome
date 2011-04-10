package au.net.fremnet.bukkit.SpawnHome;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import au.net.fremnet.libs.bukkit.Copyright;

public class SpawnHome extends JavaPlugin {
	static SHPlayerListener	playerListener			= new SHPlayerListener();
	static WorldSpawns spawns;
	static PlayerHomes homes;
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();

		Copyright.show(this, "2011 - Shannon Wynter (http://fremnet.net)");
		
		homes = new PlayerHomes(this);
		spawns = new WorldSpawns(this);
		
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Monitor, this);
		
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled :)");

	}

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	String commandName = command.getName().toLowerCase();
    	if (sender instanceof Player) {
    		Player player = (Player) sender;
    		if (commandName.equals("home")) {
    			if (args.length == 0) {
    				if (homes.hasHome(player)) {
    					homes.sendHome(player);
    				}
    				else {
    					player.sendMessage("You are currently homeless, use '/home set' or '/home help'");
    				}
        			return true;
        		}
    			else if (args.length == 1) {
    				String arg = args[0].toLowerCase();
    				if (arg.equalsIgnoreCase("set")) {
    					homes.setHome(player);
    					player.sendMessage("Home has been set to your current location");
    				}
    				else if (arg.equalsIgnoreCase("unset") || arg.equalsIgnoreCase("remove") || arg.equalsIgnoreCase("delete") || arg.equalsIgnoreCase("rm")) {
    					if (homes.hasHome(player)) {
    						homes.removeHome(player);
    						player.sendMessage("Your home is gone");
    					}
    					else {
    						player.sendMessage("You have no home!");
    					}
    				}
    				else if (arg.equalsIgnoreCase("help")) {
    					player.sendMessage("Home Help");
    					player.sendMessage("------------------");
    					player.sendMessage("/home        - Will send you home");
    					player.sendMessage("/home set    - Will set your location to home");
    					player.sendMessage("/home unset  - Will remove your current home");
    				}
        			return true;
        		}

    		}
    		else if (commandName.equals("spawn")) {
    			if (args.length == 0) {
   					spawns.sendToSpawn(player);
   	    			return true;
    			}
    			else if (args.length == 1) {
    				String arg = args[0].toLowerCase();
    				System.out.println((player.isOp() ? "yes" : "no") + " " + arg);
    				if (player.isOp()) {
    					if (arg.equalsIgnoreCase("set")) {
    						spawns.setSpawn(player);
    						player.sendMessage("Spawn has been set to your current location");
    					}
    				}
    				else {
    					player.sendMessage("You're not an op, go away!");
    				}
        			return true;
    			}
       		}
    	}
    	return false;
    }
}
