package Fishrock123.EntitySuppressor;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EntitySuppressor extends JavaPlugin {
	public int dMax;
	public int i;
	public int cD;
	public boolean lSQ;
	public boolean d;
	public boolean dlS;
	public List<World> wl;
	public List<String> eW;
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
		
  		if (i < 10) {
  			i = 10;
  			this.l.info("ES NOTICE: Interval time is too low! Using 10 ticks instead.");
  		}
		if (this.d == true) this.l.info("ES NOTICE: Running in debug mode!");
  		for (World w : wl) this.l.info("ES: Maximum monsters in " + w.getName() + " is: " + ESEntityListener.getwMax(w));
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.CREATURE_SPAWN, this.eListener, Event.Priority.Normal, this);
		PluginDescriptionFile pdfFile = getDescription();
		this.l.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled! ^_^");
		ESEntityListener.init();
	}

	public void onDisable() {  
		ESEntityListener.deinit();
		this.l.info("EntitySuppressor Disabled!");
	}
	
	public void genConfig() {
		if (!new File(getDataFolder(), "Config.yml").exists()) {
			final FileConfiguration c = this.getConfig();
			this.l.info("ES Generating Config File... :D");
			c.addDefault("DefaultMaximum", 64);
			c.addDefault("limitSquid", true);
				String[] dnLW = {"null_example_world", "null_example_world_nether"};
   	 		c.addDefault("nonLimitedWorlds", Arrays.asList(dnLW));
   	 		c.createSection("WorldMaximums");
   	 		c.getConfigurationSection("WorldMaximums").addDefault("null_example_world", 64);
   	 		c.getConfigurationSection("WorldMaximums").addDefault("null_example_world_nether", 64);
   	 		c.addDefault("SpawnFlagsCheckInterval", 200);
   	 		c.options().copyDefaults(true);
   		    this.saveConfig();
		}   
	}
	
	public void depCheck() {
		final FileConfiguration c = this.getConfig();
		if (c.contains("maxMonsters") 
  				|| c.contains("ESConfigVersion; DO NOT CHANGE!")) {
  			if (!c.contains("DefaultMaximum")) {
  				c.addDefault("DefaultMaximum", c.getInt("maxMonsters"));
  				c.options().copyDefaults(true);
  				this.saveConfig();
  			}
  			this.l.info("ES ALERT: Depreciated Config! Please refer to");
			this.l.info("http://dev.bukkit.org/server-mods/entitysuppressor/pages/main/configuration/");
			this.l.info("for information about updating your config.");
  		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadConfig() {
		final FileConfiguration c = this.getConfig();
  		this.l.info("ES Loading Config File... :D");
  		depCheck();
  		dMax = c.getInt("DefaultMaximum");
  		if (dMax == 0) dMax = 64;
  		cD = c.getInt("checkDifference");
  		if (!c.contains("checkDifference")) cDne = true;
  		lSQ = c.getBoolean("limitSquid");
  		if (!c.contains("limitSquid")) lSQ = true;
  		i = c.getInt("SpawnFlagsCheckInterval");
  		if (!c.contains("SpawnFlagsCheckInterval")) i = 200;
  		eW = c.getList("nonLimitedWorlds", eW);
  		if (c.contains("WorldMaximums")) wMs = c.getConfigurationSection("WorldMaximums").getValues(true);
  		if (c.contains("LimitSpawners")) lSwl = c.getConfigurationSection("LimitSpawners").getValues(true);
		if (lSwl == null) dlS = c.getBoolean("LimitSpawners");
		if (!c.contains("LimitSpawners")) dlS = true;
		uSF = c.getBoolean("UseSpawnFlags");
		if (!c.contains("uSF")) uSF = true;
		d = c.getBoolean("debug");
  	}
	
	public void processWL() {
		wl = Bukkit.getServer().getWorlds();
		if (eW != null) {
			for (String s : eW) {
				for (ListIterator<World> it = wl.listIterator(); it.hasNext(); ) { 
					if (it.next().getName().equalsIgnoreCase(s)) {
						it.remove();
						this.l.info("ES NOTICE: Not limiting world: " + s);
						if (wl == null) {
							this.l.info("ES NOTICE: Not using limiter as all worlds have been exempted.");
						}
					}
					//Else: Skip rope.
				}
			}
		}
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String cLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("es")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("count")) {
    	  
				if ((s instanceof Player)) {
					if (!((Player)s).hasPermission("esuppressor.count")) {
        	  
						s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
						return true;
					}
					if (args.length == 2 
							&& args[1].equalsIgnoreCase("all")) {
						
						for (World w : Bukkit.getServer().getWorlds()) {
							
							HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
							int lEs = eSet.size() - w.getPlayers().size();

							s.sendMessage("Entity Count = " + lEs + " in " + w.getName());
							continue;
						}
						return true;
						
					} 
					else if (args.length == 1) {
						
						World w = ((Player)s).getWorld();

						HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
						int lEs = eSet.size() - w.getPlayers().size();

						s.sendMessage("Entity Count = " + lEs + " in " + w.getName());
						return true;
					}
					return false;
				}

				for (World w : Bukkit.getServer().getWorlds()) {

					HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
					int lEs = eSet.size() - w.getPlayers().size();

					this.l.info("Entity Count = " + lEs + " in " + w.getName());
					continue;
				}
				return true;
			}
		}
		return false;
	}
}