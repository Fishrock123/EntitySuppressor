package Fishrock123.EntitySuppressor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class EntitySuppressor extends JavaPlugin {
	public int dMax;
	public int i;
	public int cD;
	public List<World> wl;
	public boolean lSQ;
	public boolean d;
	public boolean lS;
	public Map<String, Object> wMs;
	public Map<String, Object> lSwl;
	public boolean uSF;
	public boolean cDne = false;

	public final Logger l = Logger.getLogger("Minecraft");
	private final ESEntityListener eListener = new ESEntityListener(this);

	public void onEnable() {
		
		genConfig(); 
		loadConfig();
		processWL();
		
  		for (World w : getServer().getWorlds()) {
  			if (wl.contains(w)) {
  				l.info("ES: Maximum monsters in " + w.getName() + " is: " + ESEntityListener.getwMax(w));
  			}
  		}
  		
  		getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, eListener, Event.Priority.Normal, this);
		l.info(getDescription().getName() + " version " + getDescription().getVersion() + " is enabled! ^_^");
		ESEntityListener.init();
	}

	public void onDisable() {  
		ESEntityListener.deinit();
		l.info("EntitySuppressor Disabled!");
	}
	
	public void genConfig() {
		if (!new File(getDataFolder(), "Config.yml").exists()) {
			final FileConfiguration c = getConfig();
			l.info("ES Generating Config File... :D");
			c.addDefault("DefaultMaximum", 64);
			c.addDefault("limitSquid", true);
   	 		c.createSection("WorldMaximums");
   	 		c.getConfigurationSection("WorldMaximums").addDefault("null_example_peace_world", true);
   	 		c.getConfigurationSection("WorldMaximums").addDefault("null_example_world_nether", false);
   	 		c.getConfigurationSection("WorldMaximums").addDefault("null_example_chaos_world", 82);
   	 		c.addDefault("SpawnFlagsCheckInterval", 200);
   	 		c.options().copyDefaults(true);
   		    saveConfig();
		}   
	}
	
	public void odCheck() {
		final FileConfiguration c = getConfig();
		if (c.contains("maxMonsters") 
  				|| c.contains("ESConfigVersion; DO NOT CHANGE!")
  				|| c.contains("nonLimitedWorlds")) {
  			if (!c.contains("DefaultMaximum")) {
  				c.addDefault("DefaultMaximum", c.getInt("maxMonsters"));
  				c.options().copyDefaults(true);
  				saveConfig();
  			}
  			if (c.contains("nonLimitedWorlds")) {
  				for (Object nlw : c.getList("nonLimitedWorlds")) {
  					if (!c.contains("WorldMaximums")) c.createSection("WorldMaximums");
  		   	 		c.getConfigurationSection("WorldMaximums").addDefault(nlw.toString(), false);
  		   	 		continue;
  				}
  		   	 	c.options().copyDefaults(true);
  				saveConfig();
  			}
  			l.info("ES ALERT: Outdated Config! Please refer to");
			l.info("http://dev.bukkit.org/server-mods/entitysuppressor/pages/main/configuration/");
			l.info("for information about updating your config.");
  		}
	}
	
	private void loadConfig() {
		final FileConfiguration c = getConfig();
  		l.info("ES Loading Config File... :D");
  		odCheck();
  		dMax = c.getInt("DefaultMaximum");
  		if (dMax == 0) dMax = 64;
  		cD = c.getInt("checkDifference");
  		if (!c.contains("checkDifference")) cDne = true;
  		lSQ = c.getBoolean("limitSquid");
  		if (!c.contains("limitSquid")) lSQ = true;
  		i = c.getInt("SpawnFlagsCheckInterval");
  		if (!c.contains("SpawnFlagsCheckInterval")) i = 200;
  		if (i < 10) {
  			i = 10;
  			l.info("ES NOTICE: Interval time is too low! Using 10 ticks instead.");
  		}
  		if (c.contains("WorldMaximums")) wMs = c.getConfigurationSection("WorldMaximums").getValues(true);
  		if (c.contains("LimitSpawners")) lSwl = c.getConfigurationSection("LimitSpawners").getValues(true);
		if (lSwl == null) lS = c.getBoolean("LimitSpawners");
		if (!c.contains("LimitSpawners")) lS = true;
		uSF = c.getBoolean("UseSpawnFlags");
		if (!c.contains("uSF")) uSF = true;
		d = c.getBoolean("debug");
		if (d == true) l.info("ES NOTICE: Running in debug mode!");
  	}
	
	public void processWL() {
		wl = getServer().getWorlds();
		for (ListIterator<World> it = wl.listIterator(); it.hasNext(); ) {
			String n = it.next().getName();
			if (wMs.containsKey(n) 
					&& wMs.get(n) instanceof Boolean
				 	&& ((Boolean)wMs.get(n) == false)) {
				it.remove();
				l.info("ES: No monster limit in " + n);
				if (wl == null) {
					l.info("ES NOTICE: Not using limiter as all worlds have been exempted.");
				}
			}
			if (wMs.containsKey(n) 
					&& ((wMs.get(n) instanceof Boolean
							&& ((Boolean)wMs.get(n) == true)) 
					|| (wMs.get(n) instanceof Integer
							&& (Integer)wMs.get(n) == 0))) {
				it.remove();
				World w = getServer().getWorld(n);
				w.setSpawnFlags(false, w.getAllowAnimals());
				l.info("ES: Monsters disabled in " + n);
			}
			//Else: Skip rope.
		}
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String cLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("es")) {
			if (args.length == 0) {
				return false;
			}
			//Print
			if (args[0].equalsIgnoreCase("print")
					&& d == true) {
				if (s instanceof Player 
						&& !((Player)s).hasPermission("esuppressor.print")) {
        	  
					s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
					return true;
				}
				for (World w : getServer().getWorlds()) {
					l.info("Printing all living entities in " + w);
					l.info(w.getLivingEntities().toString());
					continue;
				}
			}
			//Count
			if (args[0].equalsIgnoreCase("count")) {
				
				if ((s instanceof Player)
						&& !((Player)s).hasPermission("esuppressor.count")) {
        	  
						s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
						return true;
					}
				
				List<World> ewl = new ArrayList<World>();
				if (args.length == 2 && args[1].equalsIgnoreCase("all")) ewl.addAll(getServer().getWorlds());
				if (args.length == 2 && getServer().getWorlds().contains(args[1])) ewl.add(getServer().getWorld(args[1]));
				if (args.length == 1 && s instanceof Player) ewl.add(((Player)s).getWorld());
				if (!(s instanceof Player)) ewl.addAll(getServer().getWorlds());
					
				for (World w : ewl) {
					int ms = 0;
					int as = 0;
					int ps = 0;
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Monster) ms++;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Animals) as++;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Player) ps++;
						continue;
					}
					
					s.sendMessage("ES Count: (" + (ms + as + ps) + "): " + ms + " monsters, " + as + " animals, and " + ps + " players in " + w.getName());
					continue;
				}
				return true;
			}
			//remove
			if (args[0].equalsIgnoreCase("remove")) {
		    	  
				if ((s instanceof Player)
						&& !((Player)s).hasPermission("esuppressor.remove")) {
        	  
					s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
					return true;
				}
				//All
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("all")) {
						
					List<World> ewl = new ArrayList<World>();
					if (args.length == 3 && args[2].equalsIgnoreCase("all")) ewl.addAll(getServer().getWorlds());
					if (args.length == 3 && getServer().getWorlds().contains(args[2])) ewl.add(getServer().getWorld(args[2]));
					if (args.length == 2 && s instanceof Player) ewl.add(((Player)s).getWorld());
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (!(e instanceof Player)) e.remove();
						}

						s.sendMessage("Removed all living entities in " + w.getName());
						l.info("Removed all living entities in " + w.getName());
					}
					//Do nothing, you must specify the world if you are not in-game.
					return true;	
				} 
				//Animals
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("animals")) {
						
					List<World> ewl = new ArrayList<World>();
					if (args.length == 3 && args[2].equalsIgnoreCase("all")) ewl.addAll(getServer().getWorlds());
					if (args.length == 3 && getServer().getWorlds().contains(args[2])) ewl.add(getServer().getWorld(args[2]));
					if (args.length == 2 && s instanceof Player) ewl.add(((Player)s).getWorld());
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Animals) e.remove();
						}

						s.sendMessage("Removed all animals in " + w.getName());
						l.info("Removed all animals in " + w.getName());
					}
					//Do nothing, you must specify the world if you are not in-game.
					return true;		
				} 
				//Monsters
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("monsters")) {
						
					List<World> ewl = new ArrayList<World>();
					if (args.length == 3 && args[2].equalsIgnoreCase("all")) ewl.addAll(getServer().getWorlds());
					if (args.length == 3 && getServer().getWorlds().contains(args[2])) ewl.add(getServer().getWorld(args[2]));
					if (args.length == 2 && s instanceof Player) ewl.add(((Player)s).getWorld());
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Monster) e.remove();
						}

						s.sendMessage("Removed all monsters in " + w.getName());
						l.info("Removed all monsters in " + w.getName());
					}
					//Do nothing, you must specify the world if you are not in-game.
					return true;		
				}
				return false;
			}
			return false;
		}
		return false;
	}
}