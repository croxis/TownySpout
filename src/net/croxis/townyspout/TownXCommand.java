package net.croxis.townyspout;

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

public class TownXCommand implements CommandExecutor{
private TownySpout plugin;

	public TownXCommand(TownySpout instance) {
		plugin = instance;
		//plugin2 = instance;
	}	
	

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
			player.sendMessage(ChatTools.formatCommand("", "/townx set", "music [node]", ""));
		} else {
			if (split[0].equalsIgnoreCase("music")){
				SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", town.getName()).findUnique();
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_id", sqltown.getId()).findUnique();
				if (sqltown == null){
					plugin.towny.sendDebugMsg("No Towny town");
				}
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownId(sqltown.getId());
					plugin.towny.sendDebugMsg("Townyx town: " + sqltownx.getId());
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
				SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", town.getName()).findUnique();
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_id", sqltown.getId()).findUnique();
				if (sqltown == null){
					plugin.towny.sendDebugMsg("No Towny town");
				}
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownId(sqltown.getId());
					plugin.towny.sendDebugMsg("Townyx town: " + sqltownx.getId());
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
				SQLTown sqltown = plugin.towny.getDatabase().find(SQLTown.class).where().ieq("name", town.getName()).findUnique();
				SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_id", sqltown.getId()).findUnique();
				if (sqltown == null){
					plugin.towny.sendDebugMsg("No Towny town");
				}
				if (sqltownx == null){
					plugin.towny.sendDebugMsg("No Townyx town");
					sqltownx = new SQLTownx();
					sqltownx.setTownId(sqltown.getId());
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
