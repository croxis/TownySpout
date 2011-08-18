package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;

public class TownyAdminXCommand implements CommandExecutor{
	private TownySpout plugin;
	
	

	public TownyAdminXCommand(TownySpout plugin) {
		super();
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			parseTownAdminXCommand(player, args);
			return true;
		}
		return false;
	}



	private void parseTownAdminXCommand(Player player, String[] args) {
		if (!plugin.towny.isTownyAdmin(player)){
			player.sendMessage("You need to be Towny Admin");
			return;
		} else if (args.length == 0) {
			player.sendMessage("Additional arguments needed");
		} else if (args[0].equalsIgnoreCase("set")) {
			if (args.length < 4){
				player.sendMessage("Format is /townyadminx set type name url");
				return;
			}
			if (args[1].equalsIgnoreCase("music")) {
				plugin.addMusic(args[2], args[3]);
				player.sendMessage("Added song: " + args[2]);
			} else if (args[1].equalsIgnoreCase("cape")){
				plugin.addCape(args[2], args[3]);
				player.sendMessage("Added cape: " + args[2]);
			} else if (args[1].equalsIgnoreCase("texture")){
				plugin.addTexture(args[2], args[3]);
				player.sendMessage("Added texture pack: " + args[2]);
			} else if (args[1].equalsIgnoreCase("wildset")){
				String[] newSplit = StringMgmt.remFirstArg(args);
				wildSet(player, newSplit);
				player.sendMessage("Type must be music, cape, or texture, or wildset");
			}
		}
	}

	private void wildSet(Player player, String[] split) {
		if (split.length == 0) {
			player.sendMessage(ChatTools.formatTitle("/townyadminx wildset"));
			player.sendMessage(ChatTools.formatCommand("", "/townyadminx wildset", "<music/cape/texture> [node]", ""));
		} else {
			SQLTownx sqltownx = plugin.getDatabase().find(SQLTownx.class).where().eq("town_name", "wild").findUnique();
			if (sqltownx == null){
				plugin.towny.sendDebugMsg("No Townyx wild");
				sqltownx = new SQLTownx();
				sqltownx.setTownName("wild");
				plugin.towny.sendDebugMsg("Townyx wild set");
			}
			if (split[0].equalsIgnoreCase("music")){
				
				if (split.length == 1)
					sqltownx.setMusicURL(null);
				else{
					if (!plugin.musicdb.containsKey(split[1])){
						player.sendMessage("There is no song by that name");
						return;
					}
					sqltownx.setMusicURL(plugin.musicdb.get(split[1]));
					player.sendMessage("Wild song set to " + split[1]);
				}
				plugin.getDatabase().save(sqltownx);
			} else if (split[0].equalsIgnoreCase("cape")){
				if (split.length == 1)
					sqltownx.setCapeURL(null);
				else{
					if (!plugin.capedb.containsKey(split[1])){
						player.sendMessage("There is no cape by that name");
						return;
					}
					sqltownx.setCapeURL(plugin.capedb.get(split[1]));
					player.sendMessage("Wild cape set to " + split[1]);
				}
				plugin.getDatabase().save(sqltownx);
			} else if (split[0].equalsIgnoreCase("texture")){
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
