package au.net.fremnet.bukkit.SpawnHome;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SHPlayerListener extends PlayerListener {
	
	@Override
    public void onPlayerJoin(PlayerEvent event) {
		
    }
	
	@Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (SpawnHome.homes.hasHome(player)) {
			event.setRespawnLocation(SpawnHome.homes.getHome(player));
		}
		else {
			event.setRespawnLocation(SpawnHome.spawns.getSpawn(player));
		}
    }
}
