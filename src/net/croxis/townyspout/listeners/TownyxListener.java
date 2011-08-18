package net.croxis.townyspout.listeners;

import net.croxis.townyspout.TownySpout;
import net.croxis.townyspout.db.SQLNationx;
import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.event.Event.Type;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.api.AreaEnterEvent;
import com.palmergames.bukkit.towny.api.NationRenameEvent;
import com.palmergames.bukkit.towny.api.TownEnterEvent;
import com.palmergames.bukkit.towny.api.TownPermissionSetEvent;
import com.palmergames.bukkit.towny.api.TownRenameEvent;
import com.palmergames.bukkit.towny.api.TownyListener;
import com.palmergames.bukkit.towny.api.WildEnterEvent;
import com.palmergames.bukkit.towny.db.SQLTown;


public class TownyxListener extends TownyListener{
	TownySpout plugin;
	
	
	public TownyxListener(TownySpout plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void onTownEnter(TownEnterEvent event) {
		plugin.towny.sendDebugMsg("TownEnterEvent");
		String musicUrl = null;
		String textureUrl = null;
		SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", event.getTown().getName()).findUnique();
		if (event.getTown().hasNation()){
			try {
				SQLNationx sqlnationx = plugin.getDatabase().find(SQLNationx.class).where().eq("nation_name", event.getTown().getNation().getName()).findUnique();
				if (sqlnationx != null){
					musicUrl = sqlnationx.getMusicURL();
					textureUrl = sqlnationx.getTexturePackURL();
				}
			} catch (NotRegisteredException e) {
				e.printStackTrace();
			}			
		}
		SpoutPlayer spoutPlayer = SpoutManager.getPlayer(event.getPlayer());
		if (sqltownx != null){			
			/*if (sqltownx.getMusicURL() != null){
				plugin.towny.sendDebugMsg("TownEnterEventPLAYING: " + sqltownx.getMusicURL());
				SpoutManager.getSoundManager().stopMusic(spoutPlayer, false, 0);
				SpoutManager.getSoundManager().playCustomMusic(plugin, spoutPlayer, sqltownx.getMusicURL(), true);
			}
			if (sqltownx.getTexturePackURL() != null){
				plugin.towny.sendDebugMsg("TownEnterEventTexture: " + sqltownx.getTexturePackURL());
				spoutPlayer.setTexturePack(sqltownx.getTexturePackURL());
			}*/
			musicUrl = sqltownx.getMusicURL();
			textureUrl = sqltownx.getTexturePackURL();
		}
		if (musicUrl != null){
			SpoutManager.getSoundManager().stopMusic(spoutPlayer, false, 0);
			SpoutManager.getSoundManager().playCustomMusic(plugin, spoutPlayer, musicUrl, true);
		}
		if (textureUrl != null)
			spoutPlayer.setTexturePack(textureUrl);
	}
	
	@Override
	public void onWildEnter(WildEnterEvent event){
		plugin.towny.sendDebugMsg("WildEnterEvent");
		SpoutPlayer player = SpoutManager.getPlayer(event.getPlayer());
		SpoutManager.getSoundManager().stopMusic(player, false, 2000);
		SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", "wild").findUnique();
		if (sqltownx != null){
			SpoutPlayer spoutPlayer = SpoutManager.getPlayer(event.getPlayer());
			String musicUrl = sqltownx.getMusicURL();
			String textureUrl = sqltownx.getTexturePackURL();
			if (musicUrl != null){
				SpoutManager.getSoundManager().playCustomMusic(plugin, spoutPlayer, musicUrl, true);
			}
			if (textureUrl != null)
				spoutPlayer.setTexturePack(textureUrl);
		}
		
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
	
	@Override
	public void onNationRename(NationRenameEvent event){
		// Stupid bukkit, forcing us to not link db cross plugins
		SQLNationx sqlnationx = plugin.getDatabase().find(SQLNationx.class).where().eq("nation_name", event.getOldName()).findUnique();
		sqlnationx.setNationName(event.getNewName());
		plugin.getDatabase().save(sqlnationx);
	}

}
