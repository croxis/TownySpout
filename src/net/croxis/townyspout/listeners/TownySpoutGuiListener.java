package net.croxis.townyspout.listeners;

import java.util.UUID;

import net.croxis.townyspout.TownySpout;

import org.bukkit.event.Event;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.player.SpoutPlayer;

import ca.xshade.bukkit.towny.NotRegisteredException;
import ca.xshade.bukkit.towny.object.Resident;
import ca.xshade.bukkit.towny.object.Town;

public class TownySpoutGuiListener extends SpoutListener{
	private TownySpout plugin;

	public TownySpoutGuiListener(TownySpout townySpout) {
		this.plugin = townySpout;
	}

	public void onCustomEvent(Event event){
		if (event instanceof ButtonClickEvent){
			// We are going to need some other way to differentiate button events
			Button button = ((ButtonClickEvent) event).getButton();
			UUID uuid = button.getId();
			SpoutPlayer sPlayer = ((ButtonClickEvent) event).getPlayer();
			try {
				Resident resident = plugin.towny.getTownyUniverse().getResident(sPlayer.getName());
				Town town = resident.getTown();
				String permString = plugin.activeTownScreens.get(sPlayer).permissionButtons.get(uuid);
				boolean perm = false;
				if (permString.equalsIgnoreCase("pvp")) {
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
	            }else{
					perm = town.getPermissions().getPerm(permString);
	            }
				town.setTownPerm(permString, !perm);
			} catch (NotRegisteredException e) {
				sPlayer.sendMessage("There was an error");
				e.printStackTrace();
			}
			
		}
	}

}
