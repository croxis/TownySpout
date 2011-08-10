package net.croxis.townyspout.listeners;

import net.croxis.townyspout.TownySpout;
import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.event.Event.Type;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import ca.xshade.bukkit.towny.api.AreaEnterEvent;
import ca.xshade.bukkit.towny.api.TownEnterEvent;
import ca.xshade.bukkit.towny.api.TownPermissionSetEvent;
import ca.xshade.bukkit.towny.api.TownRenameEvent;
import ca.xshade.bukkit.towny.api.TownyListener;
import ca.xshade.bukkit.towny.api.WildEnterEvent;
import ca.xshade.bukkit.towny.db.SQLTown;

public class TownyxListener extends TownyListener{
	TownySpout plugin;
	
	
	public TownyxListener(TownySpout plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void onTownEnter(TownEnterEvent event) {
		plugin.towny.sendDebugMsg("TownEnterEvent");
		SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", event.getTown().getName()).findUnique();
		SpoutPlayer spoutPlayer = SpoutManager.getPlayer(event.getPlayer());
		if (sqltownx != null){
			plugin.towny.sendDebugMsg("TownEnterEventTownINDB");
			if (sqltownx.getMusicURL() != null){
				plugin.towny.sendDebugMsg("TownEnterEventPLAYING: " + sqltownx.getMusicURL());
				SpoutManager.getSoundManager().stopMusic(spoutPlayer, false, 0);
				SpoutManager.getSoundManager().playCustomMusic(plugin, spoutPlayer, sqltownx.getMusicURL(), true);
			}
			if (sqltownx.getTexturePackURL() != null){
				plugin.towny.sendDebugMsg("TownEnterEventTexture: " + sqltownx.getTexturePackURL());
				spoutPlayer.setTexturePack(sqltownx.getTexturePackURL());
			}
		}
	}
	
	@Override
	public void onWildEnter(WildEnterEvent event){
		plugin.towny.sendDebugMsg("WildEnterEvent");
		SpoutPlayer player = SpoutManager.getPlayer(event.getPlayer());
		SpoutManager.getSoundManager().stopMusic(player, false, 2000);
	}
	
	@Override
	public void onTownPermissionSet(TownPermissionSetEvent event) {
		plugin.updateTownGui(event);
	}
	
	@Override
	public void onTownRename(TownRenameEvent event){
		// Stupid bukkit, forcing us to not link db cross plugins
		SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", event.getOldName()).findUnique();
		sqltownx.setTownName(event.getNewName());
		plugin.getDatabase().save(sqltownx);
	}

}
