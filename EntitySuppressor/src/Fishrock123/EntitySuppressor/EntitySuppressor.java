package Fishrock123.EntitySuppressor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class EntitySuppressor extends JavaPlugin {
	public int i;
	public Map<String, ESWorld> wlist = new HashMap<String, ESWorld>();
	public boolean lMonsters;
	public boolean lSquid;
	public boolean lAnimals;
	public boolean d;
	public boolean lSpawners;
	public boolean uSFlags;
	public boolean uRemoveM;
	public int rdist;
	public int cdist;

	public Logger l;
	public ESConfig config;
	public ESEntityListener eListener;
	public ESScanner scanner;
	public ESMethods methods;
	public ESCommands commands;

	public void onEnable() {
		final long startTime = System.nanoTime();
        final long endTime;
        
        l = Logger.getLogger("Minecraft");
        
        config = new ESConfig(this);
        methods = new ESMethods(this);
        eListener = new ESEntityListener(this);
        scanner = new ESScanner(this);
        
        this.initialize();
        
        commands = new ESCommands(this);
        
  		getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, eListener, Event.Priority.Normal, this);
		endTime = System.nanoTime();
		l.info("EntitySuppressor version " + getDescription().getVersion() + " is enabled! {" + methods.convTime(endTime - startTime) + " ms}");
		if (getDescription().getVersion().contains("TEST")) {
			l.info("ES Disclaimer: ");
			l.info("You are running a Testing version of EntitySuppressor!");
			l.info("This version may contain unwanted bugs, ");
			l.info(" and new features may not be fully functioning.");
		}
	}
	
	public void initialize() {
		config.generate();
		config.load();
		//processWL();
		
  		for (Entry<String, ESWorld> e : wlist.entrySet()) {
  			l.info("ES: Maximum monsters in `" + e.getKey() + "` is currently: " + methods.getCurrentMax(e.getKey(), "Monster"));
  			l.info("ES: Maximum squid in `" + e.getKey() + "` is currently: " + methods.getCurrentMax(e.getKey(), "Squid"));
  			l.info("ES: Maximum animals in `" + e.getKey() + "` is currently: " + methods.getCurrentMax(e.getKey(), "Animal"));
  		}
		
		scanner.init();
	}

	public void onDisable() {  
		scanner.deinit();
		l.info("EntitySuppressor Disabled!");
	}
	
	/*public void processWL() {
		wlist = getServer().getWorlds();
		for (ListIterator<World> it = wlist.listIterator(); it.hasNext(); ) {
			String n = it.next().getName();
			if (wMaximums.containsKey(n) 
					&& wMaximums.get(n) instanceof Boolean
				 	&& ((Boolean)wMaximums.get(n) == false)) {
				it.remove();
				l.info("ES: No monster limit in " + n);
				if (wlist == null) {
					l.info("ES NOTICE: Not using limiter as all worlds have been exempted.");
				}
			}
			if (wMaximums.containsKey(n) 
					&& ((wMaximums.get(n) instanceof Boolean
							&& (Boolean)wMaximums.get(n) == true) 
					|| (wMaximums.get(n) instanceof Integer
							&& (Integer)wMaximums.get(n) == 0))) {
				it.remove();
				World w = getServer().getWorld(n);
				w.setSpawnFlags(false, w.getAllowAnimals());
				l.info("ES: Monsters disabled in " + n);
			}
			//Else: Skip rope.
		}
	}*/

	public boolean onCommand(CommandSender s, Command cmd, String cLabel, String[] args) {
		return commands.commandProcess(s, cmd, cLabel, args);
	}
}