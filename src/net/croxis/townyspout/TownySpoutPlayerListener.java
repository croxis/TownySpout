package net.croxis.townyspout;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class TownySpoutPlayerListener extends PlayerListener {
	TownySpout plugin;
	
	public TownySpoutPlayerListener(TownySpout plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.apearancemanager.setGlobalTitle(player);
	}

}
