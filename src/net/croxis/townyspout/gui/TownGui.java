package net.croxis.townyspout.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import net.croxis.townyspout.TownySpout;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.Widget;
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
	//private Label //setTextColor;
	

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
			townGui(sPlayer, resident, "main");
		//else
		//	wildGui(townScreen, sPlayer, resident);	
		
		sPlayer.getMainScreen().attachPopupScreen(townScreen);
	}
	
	public void update(Player player, String screenType){
		sPlayer = SpoutManager.getPlayer(player);
		townScreen.removeWidgets(plugin);
		
		//activeTownScreens.put(sPlayer, new HashMap<UUID, String>());
		try {
			resident = plugin.towny.getTownyUniverse().getResident(player.getName());
		} catch (NotRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		if (resident.hasTown())
			townGui(sPlayer, resident, screenType);
		//else
		//	wildGui(townScreen, sPlayer, resident);	
		
		sPlayer.getMainScreen().attachPopupScreen(townScreen);
	}

	private void townGui(SpoutPlayer sPlayer, Resident resident, String screenType) {
		Town town;
		try {
			town = resident.getTown();
		} catch (NotRegisteredException e) {
			e.printStackTrace();
			return;
		}
		
		// Variables for screen building
		int y = 5;
		int yd = 12;
		int x = 10;
		int x2 = 10;
		
		ArrayList<Widget> menuButtons = new ArrayList<Widget>();
		Widget menuButton = new GenericButton("Town Main").setWidth(50);
		menuButtons.add(menuButton);
		menuButton = new GenericButton("Town Permissions").setWidth(90);
		menuButtons.add(menuButton);
		//GenericContainer menu = new GenericContainer();
		//menu.setLayout(ContainerType.HORIZONTAL);

		for (Widget b : menuButtons){
			b.setX(x2);
			x2 = x2 + b.getWidth();
			b.setY(y);
			b.setHeight(12);
			townScreen.attachWidget(plugin, b);
		}
		
		y = y + yd;
		
		//GenericContainer mainScreen = new GenericContainer();
		System.out.println("Activate: " + screenType);
		if (screenType.equalsIgnoreCase("main")){
			ArrayList<GenericLabel> labels = new ArrayList<GenericLabel>();
			//ArrayList<GenericButton> buttons = new ArrayList<GenericButton>();
			
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
			
			//GenericLabel permissionLabel = new GenericLabel("Permissions");
			//labels.add(permissionLabel);
			
			for (GenericLabel l : labels){
				//l.setHexColor(16777215);
				//setTextColor = l.setTextColor(new Color(1.0, 1.0, 1.0, 1.0));
				l.setX(x);
				l.setY(y);
				y = y + yd;
				townScreen.attachWidget(plugin, l);
				//mainScreen.addChild(l);
			}
		} else if (screenType.equalsIgnoreCase("permissions")){
			//Width of permission buttons
			int bx = 70;
			x2 = x;
			ArrayList<Widget> widgets = new ArrayList<Widget>(); 
			ArrayList<GenericButton> buttons = new ArrayList<GenericButton>();
			
			//GenericContainer master = new GenericContainer();
			//master.addChild(menu);
			//master.addChild(mainScreen);
			//townScreen.attachWidget(plugin, master);
			
			TownyPermission permissions = town.getPermissions();
			GenericButton button;
			
			widgets.add(new GenericLabel(""));
			widgets.add(new GenericLabel("Build"));
			widgets.add(new GenericLabel("Destroy"));
			widgets.add(new GenericLabel("Switch"));
			widgets.add(new GenericLabel("Items"));
			
			widgets.add(new GenericLabel("Resident"));
			
			button = new GenericButton("" + permissions.residentBuild);
			//button.setColor(getBoolColor(permissions.residentBuild));
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "residentBuild");
			
			button = new GenericButton("" + permissions.residentDestroy);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "residentDestroy");
			button = new GenericButton("" + permissions.residentSwitch);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "residentSwitch");
			button = new GenericButton("" + permissions.residentItemUse);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "residentItemUse");

			
			widgets.add(new GenericLabel("Ally"));
			
			button = new GenericButton("" + permissions.allyBuild);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "allyBuild");
			
			button = new GenericButton("" + permissions.allyDestroy);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "allyDestroy");
			button = new GenericButton("" + permissions.allySwitch);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "allySwitch");
			button = new GenericButton("" + permissions.allyItemUse);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "allyItemUse");
			
			widgets.add(new GenericLabel("Outsider"));
			button = new GenericButton("" + permissions.outsiderBuild);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "outsiderBuild");
			
			button = new GenericButton("" + permissions.outsiderDestroy);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "outsiderDestroy");
			button = new GenericButton("" + permissions.outsiderSwitch);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "outsiderSwitch");
			button = new GenericButton("" + permissions.outsiderItemUse);
			widgets.add(button);
			buttons.add(button);
			permissionButtons.put(button.getId(), "outsiderItemUse");
			
			x2 = x;
			Iterator<Widget> iterator = widgets.iterator();
			for (int i=0; i<4; i++){
				for (int j=0; j<5; j++){
					Widget w = iterator.next();
					w.setX(x2).setY(y);
					x2 = x2 + bx;
					w.setWidth(bx);
					w.setHeight(12);
					townScreen.attachWidget(plugin, w);
				}
				x2 = x;
				y = y + 12;
			}
			
			for (GenericButton b : buttons){
				if (!isTownAdmin(resident, town))
					b.setEnabled(false);
			}
			
			for (UUID uuid : permissionButtons.keySet()){
				reversePermissionButtons.put(permissionButtons.get(uuid), uuid);
			}
			
			/*
			
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
			
			town.isPublic();*/
			
			}
			
		
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
		/*if (name.startsWith("residentBuild"))
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
			line = "Fire Spread: ";*/
		
		line = line + value;
		
		button.setText(line);
		button.setDirty(true);
		
	}
	
	private Color getBoolColor(Boolean n){
		if (n)
			return new Color(0.1F, 0.1F, 1.0F, 1.0F);
		return new Color(1.0F, 0.1F, 0.1F, 1.0F);
	}

}
