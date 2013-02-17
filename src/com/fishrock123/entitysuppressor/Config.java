package com.fishrock123.entitysuppressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

import com.fishrock123.entitysuppressor.utils.Utils;

public class Config {
	public static int interval;
	public static boolean lMonsters;
	public static boolean lSquid;
	public static boolean lAnimals;
	public static boolean lBats;
	public static boolean lNPCs;
	public static boolean d;
	public static boolean lSpawners;
	public static boolean uRemoveM;
	public static boolean uLRemove;
	public static int maxLDev;
	public static int sqRemovalDist;
	public static int sqCancelDist;
	public static int entitiesPerScan;
	public static int ticksPerMonsterSpawns;
	public static int ticksPerAnimalSpawns;
	public static boolean qSpawner;
	public static boolean keepSpawnInMem;
	public static boolean KSIM;
	
	private ESPlugin m;
	
	public Config(ESPlugin instance) {
		m = instance;
	}
	
	public void generate() {
		final FileConfiguration c = m.getConfig();
		if (!new File(m.getDataFolder(), "config.yml").exists()) {
			m.log("ES: Generating New Config File... ");
			c.addDefault("LimitMonsters", true);
			c.addDefault("LimitSquid", true);
			c.addDefault("LimitAnimals", true);
			c.addDefault("LimitBats", true);
			c.addDefault("LimitNPCs", true);
   	 		c.createSection("MonsterMaximums").addDefault("world", 256);
   	 		c.getConfigurationSection("MonsterMaximums").addDefault("world_nether", 128);
   	 		c.getConfigurationSection("MonsterMaximums").addDefault("world_the_end", 256);
   	 		c.createSection("ChunkCalculatedMonsterMaximums").addDefault("world", 23);
	 		c.getConfigurationSection("ChunkCalculatedMonsterMaximums").addDefault("world_nether", 23);
	 		c.createSection("SquidMaximums").addDefault("world", 32);
   	 		c.createSection("ChunkCalculatedSquidMaximums").addDefault("world", 6);
	 		c.createSection("AnimalMaximums").addDefault("world", 128);
   	 		c.createSection("ChunkCalculatedAnimalMaximums").addDefault("world", 23);
   	 		c.addDefault("ScanInterval", 100);
   	 		c.addDefault("EntitiesPerScan", 64);
   	 		c.addDefault("UseMonsterDistanceRemoval", true);
   	 		c.addDefault("RemoveAtDistance", 82);
   	 		c.addDefault("CancelSpawnAtDistance", 64);
   	 		c.addDefault("OnlySpawnOnCloseLayers", true);
   	 		c.addDefault("MaximumSpawnLayerDeviation", 20);
   	 		c.addDefault("TicksPerMonsterSpawns", 10);
   	 		c.addDefault("TicksPerAnimalSpawns", 800);
   	 		//c.addDefault("KeepSpawnChunksInMemory", false);
   	 		c.options().copyDefaults(true);
   		    m.saveConfig();
   		    
			m.log("ES: Adding Header to config.yml... ");
			StringBuffer contents = new StringBuffer();
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(m.getClass().getResourceAsStream("configheader.txt")));
				String text = new String();
				while ((text = reader.readLine()) != null) {
					contents.append(text).append(System.getProperty("line.separator"));
				}
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				c.options().header(contents.toString());
				c.options().copyHeader(true);
				m.saveConfig();
			}
		}
	}
	
	public void odCheck() {
		final FileConfiguration c = m.getConfig();
		if (c.contains("UseSpawnFlags") || c.contains("LimitSpawners")) {
  			m.log("ES ALERT: Outdated Config! Please refer to");
			m.log("http://dev.bukkit.org/server-mods/entitysuppressor/pages/main/configuration/");
			m.log("or re-generate your config for...");
			m.log("...information about updating your config.");
  		}
	}
	
	public void load() {
		final FileConfiguration c = m.getConfig();
  		m.log("ES: Loading Config File... ");
  		odCheck();
  		lMonsters = c.getBoolean("LimitMonsters", false);
  		lSquid = c.getBoolean("LimitSquid", false);
  		lAnimals = c.getBoolean("LimitAnimals", false);
  		lBats = c.getBoolean("LimitBats", false);
  		lNPCs = c.getBoolean("LimitNPCs", false);
  		interval = c.getInt("ScanInterval", 100);
  		if (interval < 20) {
  			interval = 20;
  			m.log("ES NOTICE: Interval time is too low! Using 20 ticks instead.");
  		}
		entitiesPerScan = c.getInt("EntitiesPerScan", 64);
  		
  		loadESWByKey("MonsterMaximums", c);
  		loadESWByKey("SquidMaximums", c);
  		loadESWByKey("AnimalMaximums", c);
  		loadESWByKey("BatMaximums", c);
  		loadESWByKey("NPCMaximums", c);
  		loadESWByKey("ChunkCalculatedMonsterMaximums", c);
  		loadESWByKey("ChunkCalculatedSquidMaximums", c);
  		loadESWByKey("ChunkCalculatedAnimalMaximums", c);
  		loadESWByKey("ChunkCalculatedBatMaximums", c);
  		loadESWByKey("ChunkCalculatedNPCMaximums", c);
  		
		uRemoveM = c.getBoolean("UseMonsterDistanceRemoval", true);
		sqRemovalDist = (int)Math.pow(c.getInt("RemoveAtDistance", 128), 2);
		sqCancelDist = (int)Math.pow(c.getInt("CancelSpawnAtDistance", 92), 2);
		uLRemove = c.getBoolean("OnlySpawnOnCloseLayers", true);
		maxLDev = c.getInt("MaximumSpawnLayerDeviation", 20);
		ticksPerMonsterSpawns = c.getInt("TicksPerMonsterSpawns", 10);
		if (ticksPerMonsterSpawns < 5) {
			ticksPerMonsterSpawns = 5;
			m.log("ES NOTICE: to few ticks between monster spawns! Using 5 ticks instead.");
		}
		else if (ticksPerMonsterSpawns > 20) {
			ticksPerMonsterSpawns = 20;
			m.log("ES NOTICE: to many ticks between monster spawns! Using 20 ticks instead.");
		}
		ticksPerAnimalSpawns = c.getInt("TicksPerAnimalSpawns", 800);
		if (ticksPerAnimalSpawns < 400) {
			ticksPerAnimalSpawns = 400;
			m.log("ES NOTICE: to few ticks between animal spawns! Using 400 ticks instead.");
		}
		else if (ticksPerAnimalSpawns > 1600) {
			ticksPerAnimalSpawns = 1600;
			m.log("ES NOTICE: to many ticks between animal spawns! Using 1600 ticks instead.");
		}
		qSpawner = c.getBoolean("QueueSpawnerSpawns", true);
		KSIM = c.contains("KeepSpawnChunksInMemory");
		keepSpawnInMem = c.getBoolean("KeepSpawnChunksInMemory", false);
		d = c.getBoolean("Debug", false);
		if (d) m.log("ES NOTICE: Running in debug mode!");
  	}
	
	private void loadESWByKey(String key, FileConfiguration c) {
  		if (c.contains(key)) {
  			for (Entry<String, Object> entry : c.getConfigurationSection(key).getValues(true).entrySet()) {
  				if (!Utils.eswLookup.containsKey(entry.getKey())) {
  					Utils.eswLookup.put(entry.getKey(), new ESWorld(entry.getKey()));
  				}
  				Utils.eswLookup.get(entry.getKey()).setMaxByString(key, Integer.parseInt(entry.getValue().toString().trim()));
  			}
  		}
	}
}
