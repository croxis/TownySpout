package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.entity.Player;

import org.getspout.spoutapi.SpoutManager;

import ca.xshade.bukkit.towny.NotRegisteredException;
import ca.xshade.bukkit.towny.db.SQLTown;

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
	
	public void setGlobalCape(Player player){
		try {
			if (plugin.towny.getTownyUniverse().getResident(player.getName()).hasTown()){
				String townName = plugin.towny.getTownyUniverse().getResident(player.getName()).getTown().getName();
				SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", townName).findUnique();
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_id", sqltown.getId()).findUnique();
				if (sqltownx != null){
					if (sqltownx.getCapeURL() != null){
						SpoutManager.getAppearanceManager().setGlobalCloak(SpoutManager.getPlayer(player), sqltownx.getCapeURL());
					}
				}
			}
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		}
	}

}
