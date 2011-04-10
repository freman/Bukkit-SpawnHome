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

public class WorldSpawns {
	static private String				filename	= "world.spawns";
	static private SpawnHome			plugin;
	static private Server				server;

	private HashMap<String, Location>	spawns      = new HashMap<String, Location>();
	private File						file;

	public WorldSpawns(SpawnHome parent) {
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
		spawns.clear();
		String inputLine;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while ((inputLine = br.readLine()) != null) {
					String splits[] = inputLine.split(",", 4);

					spawns.put(splits[0], new Location(
						server.getWorld(splits[0]),
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

	public void setSpawn(Player player) {
		String key = player.getLocation().getWorld().getName();
		spawns.put(key, player.getLocation());
		save();
	}

	public void removeSpawn(Player player) {
		String key = player.getLocation().getWorld().getName();
		if (spawns.containsKey(key)) {
			spawns.remove(key);
			save();
		}
	}

	public boolean hasSpawn(Player player) {
		String key = player.getLocation().getWorld().getName();
		return spawns.containsKey(key);
	}

	public Location getSpawn(Player player) {
		String key = player.getLocation().getWorld().getName();
		if (spawns.containsKey(key)) {
			return spawns.get(key);
		}
		return player.getWorld().getSpawnLocation();
	}

	
	public void sendToSpawn(Player player) {
		player.teleport(getSpawn(player));
	}

	public void save() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			for (String key : spawns.keySet()) {
				Location loc = spawns.get(key);
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
