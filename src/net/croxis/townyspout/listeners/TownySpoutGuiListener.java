package net.croxis.townyspout.listeners;

import java.util.UUID;

import net.croxis.townyspout.TownySpout;
import net.croxis.townyspout.gui.TownGui;

import org.bukkit.event.Event;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;


public class TownySpoutGuiListener extends SpoutListener{
	private TownySpout plugin;

	public TownySpoutGuiListener(TownySpout townySpout) {
		this.plugin = townySpout;
	}

	public void onCustomEvent(Event event){
		if (event instanceof ButtonClickEvent){
			System.out.println("Button click event");
			ButtonClickEvent ev = (ButtonClickEvent) event;
			Button button = ev.getButton();
			UUID uuid = button.getId();
			SpoutPlayer sPlayer = ev.getPlayer();
			if (button.getText().equalsIgnoreCase("Town Main")){
				TownGui gui = plugin.playerTownScreens.get(sPlayer);
				gui.update(ev.getPlayer(), "main");
			} else if (button.getText().equalsIgnoreCase("Town Permissions")){
				TownGui gui = plugin.playerTownScreens.get(sPlayer);
				gui.update(ev.getPlayer(), "permissions");
			} else if (plugin.playerTownScreens.get(sPlayer).permissionButtons.containsKey(uuid)){
				System.out.println("Permissions Button click event");
				try {
					Resident resident = plugin.towny.getTownyUniverse().getResident(sPlayer.getName());
					Town town = resident.getTown();
					String permString = plugin.playerTownScreens.get(sPlayer).permissionButtons.get(uuid);
					boolean perm = false;
					/* Relocate for the "toggle" buttons
					 * 
					 * if (permString.equalsIgnoreCase("pvp")) {
						perm = town.isPVP();
					} else if (permString.equalsIgnoreCase("public")) {
						perm = town.isPublic();
					} else if (permString.equalsIgnoreCase("explosion")) {
						perm = town.isBANG();
					} else if (permString.equalsIgnoreCase("fire")) {
						perm = town.isFire();
					} else if (permString.equalsIgnoreCase("mobs")) {
						perm = town.hasMobs();
		            }else if (permString.equalsIgnoreCase("taxpercent")) {
						perm = town.isTaxPercentage();
		            }else{*/
					perm = town.getPermissions().getPerm(permString);
					//town.getPermissions().set(permString, !perm);
					//town.getPermissions().getPerm(permString);
					//town.setP
					town.setPermission(permString, !perm);
					//town.setPermissions(line)
					//town.getPermissions().
						//plugin.towny.
		            //}
					// There is a nasty bug in towny that prevents this from working
					// We will use nasty chat commands instead
					//town.setTownPerm(permString, !perm);
					//plugin.towny.updateCache();
				} catch (NotRegisteredException e) {
					sPlayer.sendMessage("There was an error");
					e.printStackTrace();
				}
			}

		}
	}

}
