package net.croxis.townyspout.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.croxis.townyspout.TownySpout;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyPermission;


public class TownGui {
	private TownySpout plugin;
	//public HashMap<SpoutPlayer, HashMap<UUID, String>> activeTownScreens = new HashMap<SpoutPlayer, HashMap<UUID, String>>();
	public SpoutPlayer sPlayer;
	public Resident resident;
	public HashMap<UUID, String> permissionButtons = new HashMap<UUID, String>();
	public HashMap<String, UUID> reversePermissionButtons = new HashMap<String, UUID>();
	public GenericPopup townScreen = new GenericPopup();

	public TownGui(TownySpout townySpout) {
		this.plugin = townySpout;
	}

	public void create(Player player){
		sPlayer = SpoutManager.getPlayer(player);
		
		//activeTownScreens.put(sPlayer, new HashMap<UUID, String>());
		try {
			resident = plugin.towny.getTownyUniverse().getResident(player.getName());
		} catch (NotRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		if (resident.hasTown())
			townGui(townScreen, sPlayer, resident);
		else
			wildGui(townScreen, sPlayer, resident);	
		
		sPlayer.getMainScreen().attachPopupScreen(townScreen);
	}

	private void wildGui(GenericPopup townScreen, SpoutPlayer sPlayer, Resident resident) {
		// TODO Auto-generated method stub
		
	}

	private void townGui(GenericPopup townScreen, SpoutPlayer sPlayer, Resident resident) {
		Town town;
		try {
			town = resident.getTown();
		} catch (NotRegisteredException e) {
			e.printStackTrace();
			return;
		}
		int y = 5;
		int yd = 12;
		int x = 10;
		//Width of permission buttons
		int bx = 120;
		ArrayList<GenericLabel> labels = new ArrayList<GenericLabel>();
		ArrayList<GenericButton> buttons = new ArrayList<GenericButton>();
		
		GenericLabel townName = new GenericLabel();
		if (town.isCapital())
			townName.setText(TownySettings.getCapitalPrefix(town) + town.getName().replaceAll("_", " ") +
				TownySettings.getCapitalPostfix(town));
		else
			townName.setText(town.getName().replaceAll("_", " "));
		labels.add(townName);
		
		GenericLabel townBoard = new GenericLabel("Board: " + town.getTownBoard());
		labels.add(townBoard);
		
		GenericLabel townSize = new GenericLabel("Town Size: " + town.getTownBlocks().size() + 
				" / " + TownySettings.getMaxTownBlocks(town) + " [Bonus: " + town.getBonusBlocks() + "]");
		labels.add(townSize);
		
		GenericLabel mayor = new GenericLabel();
		if (resident.isMayor())
			mayor.setText("Mayor: " + TownySettings.getMayorPrefix(resident) + resident.getName().replaceAll("_", " ") + TownySettings.getMayorPostfix(resident));
		else
			mayor.setText("Mayor: " + resident.getName().replaceAll("_", " "));
		labels.add(mayor);
		
		
		if (town.getAssistants().size() > 0){
			GenericLabel assistants = new GenericLabel();
			String output = "";
			for (Resident r : town.getAssistants()){
				output = output + r.getName() + ", ";
			}
			assistants.setText("Assistants: " + output);
			labels.add(assistants);
		}
		
		GenericLabel residents = new GenericLabel();
		String output = "Residents [" + town.getNumResidents() + "]: ";
		for (Resident r : town.getResidents()){
			output = output + r.getName() + ", ";
		}
		residents.setText(output);
		labels.add(residents);
		
		GenericLabel permissionLabel = new GenericLabel("Permissions");
		labels.add(permissionLabel);
		
		for (GenericLabel l : labels){
			l.setHexColor(16777215);
			l.setX(x);
			l.setY(y);
			y = y + yd;
			townScreen.attachWidget(plugin, l);
		}
		
		
		TownyPermission permissions = town.getPermissions();
		GenericButton button;
		
		button = new GenericButton("Resident Build: " + permissions.residentBuild);
		button.setX(x).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "residentBuild");
		button = new GenericButton("Resident Destroy: " + permissions.residentDestroy);
		button.setX(x + bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "residentDestoy");
		button = new GenericButton("Resident Switch: " + permissions.residentSwitch);
		button.setX(x + 2*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "residentSwitch");
		button = new GenericButton("Resident Item Use: " + permissions.residentItemUse);
		button.setX(x + 3*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "residentItemUse");
		y = y + yd;
		
		button = new GenericButton("Ally Build: " + permissions.allyBuild);
		button.setX(x).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "allyBuild");
		button = new GenericButton("Ally Destroy: " + permissions.allyDestroy);
		button.setX(x + bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "allyDestroy");
		button = new GenericButton("Ally Switch: " + permissions.allySwitch);
		button.setX(x + 2*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "allySwitch");
		button =  new GenericButton("Ally Item Use: " + permissions.allyItemUse);
		button.setX(x + 3*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "allyItemUse");
		y = y + yd;
		
		button = new GenericButton("Outsider Build: " + permissions.outsiderBuild);
		button.setX(x).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "outsiderBuild");
		button = new GenericButton("Outsider Destroy: " + permissions.outsiderDestroy);
		button.setX(x + bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "outsiderDestroy");
		button = new GenericButton("Outsider Switch: " + permissions.outsiderSwitch);
		button.setX(x + 2 * bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "outsiderSwitch");
		button = new GenericButton("Outsider Item Use: " + permissions.outsiderItemUse);
		button.setX(x + 3*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "outsiderItemUse");	
		y = y + yd;
		
		button = new GenericButton("PVP: " + town.isPVP());
		button.setX(x).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "pvp");	
		button = new GenericButton("Explosions: " + town.isBANG());
		button.setX(x + bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "explosion");	
		button = new GenericButton("Fire Spread: " + town.isFire());
		button.setX(x + 2*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "fire");	
		button = new GenericButton("Mob Spawns: " + town.hasMobs());
		button.setX(x + 3*bx).setY(y);
		buttons.add(button);
		permissionButtons.put(button.getId(), "mobs");	
		y = y + yd;
		
		for (GenericButton b : buttons){
			if (isTownAdmin(resident, town)){
				b.setWidth(bx);
				b.setHeight(12);
			} else
				b.setEnabled(false);
			//b.setCentered(false);
			//b.setX(x);
			//b.setY(y);
			//y = y + yd;
			townScreen.attachWidget(plugin, b);
		}
		
		for (UUID uuid : permissionButtons.keySet()){
			reversePermissionButtons.put(permissionButtons.get(uuid), uuid);
		}
		
		town.getPlotPrice();
		town.getPlotTax();
		town.getHoldingFormattedBalance();
		town.getMayor();
		if (town.hasNation()){
			try {
				if (town.isCapital())
					townName.setText(town.getName() + " Capital of " +  town.getNation().getName());
				else
					townName.setText(town.getName() + " Member of " +  town.getNation().getName());
			} catch (NotRegisteredException e) {
			}
		}
		town.getAssistants();
		town.getResidents();
		town.getTaxes();
		town.getTownBlocks();
		
		town.isPublic();
		
		
		
		
	}
	
	public boolean isTownAdmin(Resident resident, Town town){
		if (town.isMayor(resident) || town.getAssistants().contains(resident))
			return true;
		return false;
	}
	
	public void updatePermissionButton(String name, boolean value){
		UUID uuid = reversePermissionButtons.get(name);
		GenericButton button = (GenericButton) townScreen.getWidget(uuid);
		String line = "";
		if (name.startsWith("residentBuild"))
			line = "Resident Build: ";
		if (name.startsWith("residentDestroy"))
			line = "Resident Destroy: ";
		if (name.startsWith("residentItemUse"))
			line = "Resident Item Use: ";
		if (name.startsWith("residentSwitch"))
			line = "Resident Switch: ";
		if (name.startsWith("allyBuild"))
			line = "Ally Build: ";
		if (name.startsWith("allyDestroy"))
			line = "Ally Destroy: ";
		if (name.startsWith("allyItemUse"))
			line = "Ally Item Use: ";
		if (name.startsWith("allySwitch"))
			line = "Ally Switch: ";
		if (name.startsWith("outsiderBuild"))
			line = "Outsider Build: ";
		if (name.startsWith("outsiderDestroy"))
			line = "Outsider Destroy: ";
		if (name.startsWith("outsiderItemUse"))
			line = "Outsider Item Use: ";
		if (name.startsWith("outsiderSwitch"))
			line = "Outsider Switch: ";
		if (name.startsWith("pvp"))
			line = "PVP: ";
		if (name.startsWith("mobs"))
			line = "Mob Spawn: ";
		if (name.startsWith("explosion"))
			line = "Explosions: ";
		if (name.startsWith("fire"))
			line = "Fire Spread: ";
		
		line = line + value;
		
		button.setText(line);
		button.setDirty(true);
		
	}

}
