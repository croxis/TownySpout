package net.croxis.townyspout.listeners;

import net.croxis.townyspout.TownySpout;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.TownyException;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.api.TownEnterEvent;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.palmergames.bukkit.towny.object.WorldCoord;


public class TownySpoutPlayerListener extends PlayerListener {
	TownySpout plugin;
	
	public TownySpoutPlayerListener(TownySpout plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.apearancemanager.setGlobalTitle(player);
		plugin.apearancemanager.setGlobalCape(player);
		// Try to play music if player spawns in town
		try {
			TownyWorld world = plugin.towny.getTownyUniverse().getWorld(player.getWorld().getName());
			WorldCoord coord = new WorldCoord(world, Coord.parseCoord(player.getLocation()));
			plugin.towny.sendDebugMsg("[Townyx] [onPLayerJoin]: " + world + " " + coord);
			TownBlock plot = world.getTownBlock(coord.getX(), coord.getZ());
			if (plot.hasTown()){
				TownEnterEvent ev = new TownEnterEvent(player, plot.getTown());
				plugin.getServer().getPluginManager().callEvent(ev);
			}
			
		} catch (NotRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create GUI interfaces
		// Mayor button
		// Going to do something different instead, access screens using /townx /nationx /plotx commands
		// We can do labels and other non control things here however.
		/*
		Resident resident;
		Town town;
		try {
			resident = plugin.towny.getTownyUniverse().getResident(player.getName());
			town = resident.getTown();
			SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
			if (resident.isMayor() || town.hasAssistant(resident)){
				GenericButton mayorButton = new GenericButton("Town");
				mayorButton.setCentered(true);
				mayorButton.setX(10);
				mayorButton.setY(10);
				mayorButton.setVisible(true);
				//mayorButton.setWidth(50);
				//mayorButton.setHeight(25);
				sPlayer.getMainScreen().attachWidget(mayorButton);
				//sPlayer.getMainScreen().setDirty(true);
				}
					
		} catch (TownyException x) {
			plugin.towny.sendErrorMsg(player, x.getError());
			return;
		}*/
	}

}
