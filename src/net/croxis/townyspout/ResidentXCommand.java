package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLNationx;
import net.croxis.townyspout.db.SQLResidence;
import net.croxis.townyspout.db.SQLResidentx;
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
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;

public class ResidentXCommand implements CommandExecutor{
	private TownySpout plugin;

	public ResidentXCommand(TownySpout plugin) {
		super();
		this.plugin = plugin;
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
		/*if (split.length == 0){
			//player.sendMessage("Need additional info");
			TownGui townGui = new TownGui(plugin);
			townGui.create(player);
			Resident resident = plugin.towny.getTownyUniverse().getResident(player.getName());
			SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
			if (resident.hasTown())
				plugin.addTownGui(sPlayer, resident.getTown().getName(), townGui);
			else
				plugin.addTownGui(sPlayer, "wild", townGui);
			return;
		}*/
			String[] newSplit = StringMgmt.remFirstArg(split);
			
			if (split[0].equalsIgnoreCase("set"))
				nationSet(player, newSplit);			
	}

	private void nationSet(Player player, String[] split) {
		Resident resident;
		try {
			resident = plugin.towny.getTownyUniverse().getResident(player.getName());
		} catch (TownyException x) {
			plugin.towny.sendErrorMsg(player, x.getError());
			return;
		}
		
		if (split.length == 0) {
			player.sendMessage(ChatTools.formatTitle("/residentx set"));
			player.sendMessage(ChatTools.formatCommand("", "/residentx set", "music [node]", ""));
			player.sendMessage(ChatTools.formatCommand("", "/residentx set", "cape [node]", ""));
		} else {
			if (split[0].equalsIgnoreCase("music")){
				SQLResidence sqlresidentx = plugin.getDatabase().find(SQLResidence.class).where().eq("player_name", resident.getName()).findUnique();
				if (sqlresidentx == null){
					plugin.towny.sendDebugMsg("No Resident");
					sqlresidentx = new SQLResidence();
					sqlresidentx.setPlayerName(player.getName());
					plugin.towny.sendDebugMsg("Townyx resident: " + resident.getName());
				}
				if (split.length == 1)
					sqlresidentx.setMusicURL(null);
				else{
					if (!plugin.musicdb.containsKey(split[1])){
						player.sendMessage("There is no song by that name");
						return;
					}
					sqlresidentx.setMusicURL(plugin.musicdb.get(split[1]));
					player.sendMessage("Personal plot song set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqlresidentx);
			} else if (split[0].equalsIgnoreCase("cape")){
				SQLResidence sqlresidentx = plugin.getDatabase().find(SQLResidence.class).where().eq("player_name", player.getName()).findUnique();
				if (sqlresidentx == null){
					plugin.towny.sendDebugMsg("No Residentx");
					sqlresidentx = new SQLResidence();
					sqlresidentx.setPlayerName(player.getName());
				}
				if (split.length == 1)
					sqlresidentx.setCapeURL(null);
				else{
					if (!plugin.capedb.containsKey(split[1])){
						player.sendMessage("There is no cape by that name");
						return;
					}
					sqlresidentx.setCapeURL(plugin.capedb.get(split[1]));
					player.sendMessage("Nation cape set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqlresidentx);
			} 
		}
	}
}
