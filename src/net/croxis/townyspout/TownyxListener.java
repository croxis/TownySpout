package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.event.Event.Type;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import ca.xshade.bukkit.towny.api.AreaEnterEvent;
import ca.xshade.bukkit.towny.api.TownEnterEvent;
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
		SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", event.getTown().getName()).findUnique();
		SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_id", sqltown.getId()).findUnique();
		SpoutPlayer spoutPlayer = SpoutManager.getPlayer(event.getPlayer());
		if (sqltownx != null){
			plugin.towny.sendDebugMsg("TownEnterEventTownINDB");
			if (sqltownx.getMusicURL() != null){
				plugin.towny.sendDebugMsg("TownEnterEventPLAYING: " + sqltownx.getMusicURL());
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

}
