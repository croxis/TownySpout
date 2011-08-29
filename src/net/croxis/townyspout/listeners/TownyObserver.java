package net.croxis.townyspout.listeners;

import java.util.Observable;
import java.util.Observer;

import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyObject;
import com.palmergames.bukkit.towny.object.TownyObservableType;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;

public class TownyObserver implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof TownyUniverse && arg instanceof TownyObservableType) {
			TownyUniverse universe = (TownyUniverse) o;
			TownyObservableType type = (TownyObservableType) arg;
				switch(type) {
			    	case NEW_DAY: doStuff(); break;
				    case TOGGLE_MOB_REMOVAL: doThings(); break;
				    case AREA_ENTER: onAreaEnter(universe, type); break;
				}
			}

	}
	
	private void onAreaEnter(TownyUniverse universe, TownyObservableType type) {
		Player player = type.getPlayer();
		WorldCoord worldCoords = type.getWorldCoord();
		try {
			TownBlock plot = worldCoords.getTownBlock();
			if (plot.hasTown())
				player.sendMessage(plot.getTown().getName());
		} catch (NotRegisteredException e) {
			player.sendMessage("Wild");
		}
		
	}

	private void doThings() {
		// TODO Auto-generated method stub
		
	}

	private void doStuff() {
		// TODO Auto-generated method stub
		
	}	

}
