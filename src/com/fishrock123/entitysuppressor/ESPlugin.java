package com.fishrock123.entitysuppressor;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.WaterMob;
import org.bukkit.plugin.java.JavaPlugin;

import com.fishrock123.entitysuppressor.utils.CustomPlotters;
import com.fishrock123.entitysuppressor.utils.Metrics;
import com.fishrock123.entitysuppressor.utils.Utils;

public class ESPlugin extends JavaPlugin implements CustomPlotters {
	private Logger l;
	public Config config;
	public EntityListener eListener;
	public EntityScanner scanner;
	public CommandHandler commandHandler;

	@Override
	public void onEnable() {
		final long startTime = System.nanoTime();
        final long endTime;
        
        l = getLogger();
        
        config = new Config(this);
        eListener = new EntityListener();
        scanner = new EntityScanner(this);
        
        this.initialize();
        
        commandHandler = new CommandHandler(this);
        
  		getServer().getPluginManager().registerEvents(eListener, this);
		
  		if (!getDescription().getVersion().contains("TEST")) {
			try {
			    Metrics metrics = new Metrics(this);
			    
			    //Custom data disabled in this version. TODO: Add better custom data.
			    
			    metrics.start();
			    
			} catch (IOException e) {
				log(e.getMessage());
			}
  		}
		
		endTime = System.nanoTime();
		log("is enabled! {" + Utils.convTime(endTime - startTime) + " ms}");
		if (getDescription().getVersion().contains("TEST")) {
			log("ES Disclaimer: ");
			log("You are running a Testing version of EntitySuppressor!");
			log("This version may contain unwanted bugs, ");
			log(" and new features may not be fully functioning.");
		}
	}
	
	public void log(String s) {
		l.info(s);
	}
	
	public void initialize() {
		config.generate();
		config.load();
		
  		for (Entry<String, ESWorld> e : Utils.eswLookup.entrySet()) {
  			ESWorld esw = e.getValue();
  			log("Current Maximums for `" + e.getKey() + "`:" 
  			+ (esw.hasMonsterMax() ? " Monsters(" + Utils.getCurrentMax(esw, Monster.class) + ')' : "") 
  			+ (esw.hasAnimalMax() ? " Animals(" + Utils.getCurrentMax(esw, Animals.class) + ')' : "") 
  			+ (esw.hasWaterMax() ? " Squid(" + Utils.getCurrentMax(esw, WaterMob.class) + ')' : "")
  			+ (esw.hasAmbientMax() ? " Bats(" + Utils.getCurrentMax(esw, Ambient.class) + ')' : "")
  			+ (esw.hasNPCMax() ? " NPCs(" + Utils.getCurrentMax(esw, NPC.class) + ')' : "")
  			);
  		}
		
		scanner.init();
		
		for (World w : Bukkit.getWorlds()) {
  			w.setTicksPerMonsterSpawns(Config.ticksPerMonsterSpawns);
  			w.setTicksPerAnimalSpawns(Config.ticksPerAnimalSpawns);
  			if (Config.KSIM) w.setKeepSpawnInMemory(Config.keepSpawnInMem);
  		}
	}

	@Override
	public void onDisable() {  
		getServer().getScheduler().cancelTasks(this);
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String cLabel, String[] args) {
		return commandHandler.process(s, cmd, cLabel, args);
	}
}