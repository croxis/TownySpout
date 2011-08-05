package net.croxis.townyspout;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
			} else {
				player.sendMessage("Type must be music, cape, or texture");
			}
		}
	}
	
	
}
