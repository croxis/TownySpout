package net.croxis.townyspout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.croxis.townyspout.db.SQLTownx;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.ConfigurationNode;

import ca.xshade.bukkit.towny.Towny;

public class TownySpout extends JavaPlugin {
	public Towny towny;
	public AppearanceManager apearancemanager;
	private final TownySpoutPlayerListener playerListener = new TownySpoutPlayerListener(this);
	private TownyxListener townyxListener;
	private Logger logger = Logger.getLogger("net.croxis.townyspout");
	public HashMap<String, String> musicdb = new HashMap<String, String>();
	public HashMap<String, String> capedb = new HashMap<String, String>();
	public HashMap<String, String> texturedb = new HashMap<String, String>();

	public void onDisable() {
		// TODO Auto-generated method stub
		
	}
	
	public void onEnable() {
		
		towny = (Towny) getServer().getPluginManager().getPlugin("Towny");
		if (towny == null){
			System.out.println("NO TOWNY DETECTED. Please check Towny configuration.");
			return;
		} 
		setupDatabase();
		loadConfig();
		apearancemanager = new AppearanceManager(this);
		
		final PluginManager pluginManager = getServer().getPluginManager();
		//final Plugin towny = (Plugin)pluginManager.getPlugin("Towny");
		townyxListener = new TownyxListener(this);
		
		pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.CUSTOM_EVENT, townyxListener, Priority.Normal, this);
		
		getCommand("townx").setExecutor(new TownXCommand(this));
		getCommand("townyadminx").setExecutor(new TownyAdminXCommand(this));
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
	
	public void loadConfig()
    {
    	getConfiguration().load();
    	
    	ConfigurationNode music = getConfiguration().getNode("music");
    	
    	try {
	    	for (String node: music.getKeys()){
	    		musicdb.put(node, music.getString(node));
	    	}
    	} catch(Exception e){}
    	
    	ConfigurationNode capes = getConfiguration().getNode("cape");
    	
    	try {
    		for (String node: capes.getKeys()){
    			capedb.put(node, capes.getString(node));
    		}
    	} catch(Exception e){}
    	
    	ConfigurationNode textures = getConfiguration().getNode("texture");
    	
    	try{
    		for (String node: textures.getKeys()){
    	
    			texturedb.put(node, textures.getString(node));
    		}
    	} catch(Exception e){}
    	
    	//getConfiguration().save();
    }

	public void addMusic(String name, String url) {
		getConfiguration().setProperty("music." + name, url);
		getConfiguration().save();
		musicdb.put(name, url);
	}

	public void addCape(String name, String url) {
		getConfiguration().setProperty("cape." + name, url);
		getConfiguration().save();
		capedb.put(name, url);
	}

	public void addTexture(String name, String url) {
		getConfiguration().setProperty("texture." + name, url);
		getConfiguration().save();
		texturedb.put(name, url);
	}

}
