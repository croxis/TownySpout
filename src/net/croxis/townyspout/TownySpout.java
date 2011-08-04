package net.croxis.townyspout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.xshade.bukkit.towny.Towny;

public class TownySpout extends JavaPlugin {
	public Towny towny;
	public AppearanceManager apearancemanager;
	private final TownySpoutPlayerListener playerListener = new TownySpoutPlayerListener(this);
	private TownyxListener townyxListener;
	private Logger logger = Logger.getLogger("net.croxis.townyspout");

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		
		towny = (Towny) getServer().getPluginManager().getPlugin("Towny");
		if (towny == null){
			System.out.println("NO TOWNY DETECTED!!!!!!!@!!! FAILLUUURESSSSS!");
			return;
		} 
		setupDatabase();
		apearancemanager = new AppearanceManager(this);
		
		final PluginManager pluginManager = getServer().getPluginManager();
		//final Plugin towny = (Plugin)pluginManager.getPlugin("Towny");
		townyxListener = new TownyxListener(this);
		
		pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.CUSTOM_EVENT, townyxListener, Priority.Normal, this);
		
		getCommand("townx").setExecutor(new TownCommand(this));
	}
	
	private void setupDatabase()
	{
		try
		{
		getDatabase().find(SQLTownx.class).findRowCount();
		}
		catch(PersistenceException ex)
		{
			logger.info("Installing database for " + getDescription().getName() + " due to first time usage");
			installDDL();
		}
	}
	
	@Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(SQLTownx.class);
        return list;
    }

}
