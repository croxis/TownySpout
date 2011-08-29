package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLNationx;
import net.croxis.townyspout.db.SQLResidence;
import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;


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
				Town town = plugin.towny.getTownyUniverse().getResident(player.getName()).getTown();
				
				//SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", townName).findUnique();
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", town.getName()).findUnique();
				SQLResidence sqlresidentx = plugin.getDatabase().find(SQLResidence.class).where().eq("player_name", player.getName()).findUnique();
				String capeUrl = null;
				
				if (town.hasNation()){
					SQLNationx sqlnationx = plugin.getDatabase().find(SQLNationx.class).where().eq("nation_name", town.getNation().getName()).findUnique();
					if (sqlnationx != null)
						capeUrl = sqlnationx.getCapeURL();
				}
				if (sqltownx != null)
					capeUrl = sqltownx.getCapeURL();
				
				if (sqlresidentx != null)
					capeUrl = sqlresidentx.getCapeURL();
				
				if (capeUrl != null)
					SpoutManager.getAppearanceManager().setGlobalCloak(SpoutManager.getPlayer(player), capeUrl);
			} else {
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", "wild").findUnique();
				if (sqltownx != null)
					SpoutManager.getAppearanceManager().setGlobalCloak(SpoutManager.getPlayer(player), sqltownx.getCapeURL());
			}
		} catch (NotRegisteredException e) {
			e.printStackTrace();
		}
	}

}
