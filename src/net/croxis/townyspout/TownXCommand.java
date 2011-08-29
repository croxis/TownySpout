package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLTownx;
import net.croxis.townyspout.gui.TownGui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.TownyException;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.db.SQLTown;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;


public class TownXCommand implements CommandExecutor{
private TownySpout plugin;

	public TownXCommand(TownySpout instance) {
		plugin = instance;
	}	
	

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			try {
				parseTownCommand(player, args);
			} catch (NotRegisteredException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private void parseTownCommand(Player player, String[] split) throws NotRegisteredException {
		if (split.length == 0){
			TownGui townGui = new TownGui(plugin);
			townGui.create(player);
			Resident resident = plugin.towny.getTownyUniverse().getResident(player.getName());
			SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
			if (resident.hasTown())
				plugin.addTownGui(sPlayer, resident.getTown().getName(), townGui);
			//else
			//	plugin.addTownGui(sPlayer, "wild", townGui);
			return;
		}
			String[] newSplit = StringMgmt.remFirstArg(split);
			
			if (split[0].equalsIgnoreCase("set"))
				townSet(player, newSplit);			
	}

	private void townSet(Player player, String[] split) {
		Resident resident;
		Town town;
		try {
			resident = plugin.towny.getTownyUniverse().getResident(player.getName());
			town = resident.getTown();
			if (!resident.isMayor())
				if (!town.hasAssistant(resident))
					throw new TownyException(TownySettings.getLangString("msg_not_mayor_ass"));
		} catch (TownyException x) {
			plugin.towny.sendErrorMsg(player, x.getError());
			return;
		}
		
		if (split.length == 0) {
			player.sendMessage(ChatTools.formatTitle("/townx set"));
			player.sendMessage(ChatTools.formatCommand("", "/townx set", "music [node]", ""));
		} else {
			if (split[0].equalsIgnoreCase("music")){
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", town.getName()).findUnique();
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownName(town.getName());
					plugin.towny.sendDebugMsg("Townyx town: " + town.getName());
				}
				if (split.length == 1)
					sqltownx.setMusicURL(null);
				else{
					if (!plugin.musicdb.containsKey(split[1])){
						player.sendMessage("There is no song by that name");
						return;
					}
					sqltownx.setMusicURL(plugin.musicdb.get(split[1]));
					player.sendMessage("Town song set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqltownx);
			} else if (split[0].equalsIgnoreCase("cape")){
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", town.getName()).findUnique();
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownName(town.getName());
				}
				if (split.length == 1)
					sqltownx.setCapeURL(null);
				else{
					if (!plugin.capedb.containsKey(split[1])){
						player.sendMessage("There is no cape by that name");
						return;
					}
					sqltownx.setCapeURL(plugin.capedb.get(split[1]));
					player.sendMessage("Town cape set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqltownx);
			} else if (split[0].equalsIgnoreCase("texture")){
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", town.getName()).findUnique();
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownName(town.getName());
					plugin.towny.sendDebugMsg("Townyx town: " + sqltownx.getId());
				}
				if (split.length == 1)
					sqltownx.setTexturePackURL(null);
				else{
					if (!plugin.texturedb.containsKey(split[1])){
						player.sendMessage("There is no texture by that name");
						return;
					}
					sqltownx.setTexturePackURL(plugin.texturedb.get(split[1]));
					player.sendMessage("Town texture set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqltownx);
			}
		}
	}
}
