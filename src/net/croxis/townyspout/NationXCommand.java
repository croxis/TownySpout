package net.croxis.townyspout;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NationXCommand implements CommandExecutor{
	private TownySpout plugin;

	public NationXCommand(TownySpout plugin) {
		super();
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

}
