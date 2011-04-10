package au.net.fremnet.bukkit.SpawnHome;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PlayerHomes {
	static private String				filename	= "player.homes";
	static private SpawnHome			plugin;
	static private Server				server;

	private HashMap<String, Location>	homes       = new HashMap<String, Location>();
	private File						file;

	public PlayerHomes(SpawnHome parent) {
		plugin = parent;
		server = plugin.getServer();
		File dataFolder = plugin.getDataFolder();

		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		file = new File(dataFolder, filename);
		load();
	}

	public void load() {
		homes.clear();
		String inputLine;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while ((inputLine = br.readLine()) != null) {
					String splits[] = inputLine.split(",", 4);
					String splitz[] = splits[0].split(":", 2);

					homes.put(splits[0], new Location(
						server.getWorld(splitz[1]),
						Double.parseDouble(splits[1]),
						Double.parseDouble(splits[2]),
						Double.parseDouble(splits[3]))
					);
				}
				br.close();
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setHome(Player player) {
		String key = player.getName() + ":" + player.getLocation().getWorld().getName();
		homes.put(key, player.getLocation());
		save();
	}

	public Location getHome(Player player) {
		String key = player.getName() + ":" + player.getLocation().getWorld().getName();
		if (homes.containsKey(key)) {
			return homes.get(key);
		}
		return null;
	}
		
	public void removeHome(Player player) {
		String key = player.getName() + ":" + player.getLocation().getWorld().getName();
		if (homes.containsKey(key)) {
			homes.remove(key);
			save();
		}
	}

	public boolean hasHome(Player player) {
		String key = player.getName() + ":" + player.getLocation().getWorld().getName();
		return homes.containsKey(key);
	}

	public void sendHome(Player player) {
		String key = player.getName() + ":" + player.getLocation().getWorld().getName();
		if (homes.containsKey(key)) {
			player.teleport(homes.get(key));
		}
	}

	public void save() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			for (String key : homes.keySet()) {
				Location loc = homes.get(key);
				bw.write(String.format("%s,%f,%f,%f\n", key, loc.getX(), loc.getZ(), loc.getZ()));
			}
			bw.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
