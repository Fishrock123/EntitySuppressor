package Fishrock123.EntitySuppressor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EntitySuppressor extends JavaPlugin implements CustomPlotters {
	public int i;
	public Map<String, ESWorld> eswLookup = new HashMap<String, ESWorld>();
	public boolean lMonsters;
	public boolean lSquid;
	public boolean lAnimals;
	public boolean d;
	public boolean lSpawners;
	public boolean uSFlags;
	public boolean uRemoveM;
	public int sqRemovalDist;
	public int sqCancelDist;

	public Logger l;
	public ESConfig config;
	public ESEntityListener eListener;
	public ESScanner scanner;
	public ESMethods methods;
	public ESCommands commands;

	@Override
	public void onEnable() {
		final long startTime = System.nanoTime();
        final long endTime;
        
        l = getLogger();
        
        methods = new ESMethods(this);
        config = new ESConfig(this);
        eListener = new ESEntityListener(this);
        scanner = new ESScanner(this);
        
        this.initialize();
        
        commands = new ESCommands(this);
        
  		getServer().getPluginManager().registerEvents(eListener, this);
		endTime = System.nanoTime();
		l.info("is enabled! {" + methods.convTime(endTime - startTime) + " ms}");
		if (getDescription().getVersion().contains("TEST")) {
			l.info("ES Disclaimer: ");
			l.info("You are running a Testing version of EntitySuppressor!");
			l.info("This version may contain unwanted bugs, ");
			l.info(" and new features may not be fully functioning.");
		}
		
		try {
		    Metrics metrics = new Metrics();

		    metrics.addCustomData(this, totalEntities);
		    metrics.addCustomData(this, totalMobs);
		    metrics.addCustomData(this, totalMonsters);
		    metrics.addCustomData(this, totalAnimals);
		    metrics.addCustomData(this, totalSquid);
		    metrics.addCustomData(this, totalLoadedChunks);

		    metrics.beginMeasuringPlugin(this);
		    
		} catch (IOException e) {
		    l.info(e.getMessage());
		}
	}
	
	public void initialize() {
		config.generate();
		config.load();
		
  		for (Entry<String, ESWorld> e : eswLookup.entrySet()) {
  			ESWorld esw = e.getValue();
  			l.info("Current Maximums for `" + e.getKey() + "`:" 
  			+ (esw.hasMonsterMaximum() ? " Monsters(" + methods.getCurrentMax(esw, "Monster") + ")" : "") 
  			+ (esw.hasAnimalMaximum() ? " Animals(" + methods.getCurrentMax(esw, "Animal") + ")"  : "") 
  			+ (esw.hasSquidMaximum() ? " Squid(" + methods.getCurrentMax(esw, "Squid") + ")"   : "")
  			);
  		}
		
		scanner.init();
	}

	@Override
	public void onDisable() {  
		scanner.deinit();
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String cLabel, String[] args) {
		boolean bol = commands.commandProcess(s, cmd, cLabel, args);
		return bol;
	}
}