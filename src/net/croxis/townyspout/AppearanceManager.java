package net.croxis.townyspout;

import org.bukkit.entity.Player;

import org.getspout.spoutapi.SpoutManager;

import ca.xshade.bukkit.towny.NotRegisteredException;

public class AppearanceManager {
	TownySpout plugin;

	public AppearanceManager(TownySpout plugin) {
		this.plugin = plugin;
	}

	public void setGlobalTitle(Player player){
		String display = "";
		try {
			if (plugin.towny.getTownyUniverse().getResident(player.getName()).hasNation()){
				display = "[" + plugin.towny.getTownyUniverse().getResident(player.getName()).getTown().getNation().getName() + "] ";
			}
		} catch (NotRegisteredException e1) {
			e1.printStackTrace();
		}
		try {
			if (plugin.towny.getTownyUniverse().getResident(player.getName()).hasTown()){
				display = display + "[" + plugin.towny.getTownyUniverse().getResident(player.getName()).getTown().getName() + "] ";
			}
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		}
		display = display + player.getName();
		SpoutManager.getAppearanceManager().setGlobalTitle(SpoutManager.getPlayer(player), display);
	}

}
