package net.croxis.townyspout;

import net.croxis.townyspout.db.SQLNationx;
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

public class NationXCommand implements CommandExecutor{
	private TownySpout plugin;

	public NationXCommand(TownySpout plugin) {
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
		Nation nation;
		try {
			resident = plugin.towny.getTownyUniverse().getResident(player.getName());
			if (!resident.getTown().hasNation()){
				player.sendMessage("Your town is not part of a Nation");
				return;
			}
			nation = resident.getTown().getNation();
			if (!resident.isKing())
				if (!nation.hasAssistant(resident))
					throw new TownyException(TownySettings.getLangString("msg_not_king_ass"));
		} catch (TownyException x) {
			plugin.towny.sendErrorMsg(player, x.getError());
			return;
		}
		
		if (split.length == 0) {
			player.sendMessage(ChatTools.formatTitle("/nationx set"));
			player.sendMessage(ChatTools.formatCommand("", "/nationx set", "music [node]", ""));
			player.sendMessage(ChatTools.formatCommand("", "/nationx set", "cape [node]", ""));
			player.sendMessage(ChatTools.formatCommand("", "/nationx set", "texture [node]", ""));
		} else {
			if (split[0].equalsIgnoreCase("music")){
				SQLNationx sqlnationx = plugin.getDatabase().find(SQLNationx.class).where().eq("nation_name", nation.getName()).findUnique();
				if (sqlnationx == null){
					plugin.towny.sendDebugMsg("No Nationx nation");
					sqlnationx = new SQLNationx();
					sqlnationx.setNationName(nation.getName());
					plugin.towny.sendDebugMsg("Townyx nation: " + nation.getName());
				}
				if (split.length == 1)
					sqlnationx.setMusicURL(null);
				else{
					if (!plugin.musicdb.containsKey(split[1])){
						player.sendMessage("There is no song by that name");
						return;
					}
					sqlnationx.setMusicURL(plugin.musicdb.get(split[1]));
					player.sendMessage("Town song set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqlnationx);
			} else if (split[0].equalsIgnoreCase("cape")){
				SQLNationx sqlnationx = plugin.getDatabase().find(SQLNationx.class).where().eq("nation_name", nation.getName()).findUnique();
				if (sqlnationx == null){
					plugin.towny.sendDebugMsg("No Nationyx nation");
					sqlnationx = new SQLNationx();
					sqlnationx.setNationName(nation.getName());
				}
				if (split.length == 1)
					sqlnationx.setCapeURL(null);
				else{
					if (!plugin.capedb.containsKey(split[1])){
						player.sendMessage("There is no cape by that name");
						return;
					}
					sqlnationx.setCapeURL(plugin.capedb.get(split[1]));
					player.sendMessage("Nation cape set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqlnationx);
			} else if (split[0].equalsIgnoreCase("texture")){
				SQLNationx sqlnationx = plugin.getDatabase().find(SQLNationx.class).where().eq("nation_name", nation.getName()).findUnique();
				if (sqlnationx == null){
					plugin.towny.sendDebugMsg("No Nationyx nation");
					sqlnationx = new SQLNationx();
					sqlnationx.setNationName(nation.getName());
					plugin.towny.sendDebugMsg("Nationyx nation: " + sqlnationx.getId());
				}
				if (split.length == 1)
					sqlnationx.setTexturePackURL(null);
				else{
					if (!plugin.texturedb.containsKey(split[1])){
						player.sendMessage("There is no texture by that name");
						return;
					}
					sqlnationx.setTexturePackURL(plugin.texturedb.get(split[1]));
					player.sendMessage("Nation texture set to " + split[1]);
				}
					
				plugin.getDatabase().save(sqlnationx);
			}
		}
	}
}
