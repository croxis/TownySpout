package net.croxis.townyspout;

import java.util.ArrayList;
import java.util.List;

import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.xshade.bukkit.towny.TownyException;
import ca.xshade.bukkit.towny.TownySettings;
import ca.xshade.bukkit.towny.db.SQLTown;
import ca.xshade.bukkit.towny.object.Resident;
import ca.xshade.bukkit.towny.object.Town;
import ca.xshade.bukkit.util.ChatTools;
import ca.xshade.util.StringMgmt;

public class TownCommand implements CommandExecutor{
private TownySpout plugin;

	public TownCommand(TownySpout instance) {
		plugin = instance;
		//plugin2 = instance;
	}	
	

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			parseTownCommand(player, args);
		}
		return true;
	}

	private void parseTownCommand(Player player, String[] split) {
		if (split.length == 0){
			player.sendMessage("Need additional info");
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
			player.sendMessage(ChatTools.formatCommand("", "/townx set", "music [url]", ""));
		} else {
			if (split[0].equalsIgnoreCase("music")){
				SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", town.getName()).findUnique();
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_id", sqltown.getId()).findUnique();
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownId(sqltown.getId());
					plugin.towny.sendDebugMsg("Townyx town: " + sqltownx.getId());
				}
				if (split.length == 1)
					sqltownx.setMusicURL(null);
				else
					sqltownx.setMusicURL(split[1]);
				plugin.getDatabase().save(sqltownx);
			}
		}
	}
}
