package Fishrock123.EntitySuppressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ESConfig {
	public static EntitySuppressor m;
	public ESConfig(EntitySuppressor instance) {
		m = instance;
	}
	
	public void generate() {
		final FileConfiguration c = m.getConfig();
		if (!new File(m.getDataFolder(), "config.yml").exists()) {
			m.l.info("ES: Generating New Config File... ");
			c.addDefault("LimitMonsters", true);
			c.addDefault("LimitSquid", true);
			c.addDefault("LimitAnimals", false);
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
   	 		c.addDefault("UseMonsterDistanceRemoval", true);
   	 		c.addDefault("RemoveAtDistance", 128);
   	 		c.addDefault("CancelSpawnAtDistance", 92);
   	 		c.options().copyDefaults(true);
   		    m.saveConfig();
   		    
			m.l.info("ES: Adding Header to config.yml... ");
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
		if (c.contains("maxMonsters") 
  				|| c.contains("ESConfigVersion; DO NOT CHANGE!")
  				|| c.contains("nonLimitedWorlds")
  				|| c.contains("SpawnFlagsCheckInterval")
  				|| c.contains("checkDifference")
  				|| c.contains("DefaultMaximum")
  				|| c.contains("limitSquid")) {
  			m.l.info("ES ALERT: Outdated Config! Please refer to");
			m.l.info("http://dev.bukkit.org/server-mods/entitysuppressor/pages/main/configuration/");
			m.l.info("or re-generate your config for...");
			m.l.info("...information about updating your config.");
  		}
		if (!c.contains("MonsterMaximums")) {
			c.addDefault("LimitMonsters", true);
			c.addDefault("LimitSquid", true);
			c.addDefault("LimitAnimals", false);
   	 		c.createSection("MonsterMaximums").addDefault("world", 256);
   	 		c.getConfigurationSection("MonsterMaximums").addDefault("world_nether", 128);
   	 		c.getConfigurationSection("MonsterMaximums").addDefault("world_the_end", 256);
   	 		c.createSection("ChunkCalculatedMonsterMaximums").addDefault("world", 23);
	 		c.getConfigurationSection("ChunkCalculatedMonsterMaximums").addDefault("world_nether", 23);
	 		c.createSection("SquidMaximums").addDefault("world", 32);
   	 		c.createSection("ChunkCalculatedSquidMaximums").addDefault("world", 6);
	 		c.createSection("AnimalMaximums").addDefault("world", 128);
   	 		c.createSection("ChunkCalculatedAnimalMaximums").addDefault("world", 23);
   	 		c.addDefault("UseMonsterDistanceRemoval", true);
   	 		c.addDefault("RemoveAtDistance", 128);
   	 		c.addDefault("CancelSpawnAtDistance", 92);
		}
	}
	
	public void load() {
		final FileConfiguration c = m.getConfig();
  		m.l.info("ES: Loading Config File... ");
  		odCheck();
  		m.lMonsters = c.getBoolean("LimitMonsters", false);
  		m.lSquid = c.getBoolean("LimitSquid", false);
  		m.lAnimals = c.getBoolean("LimitAnimals", false);
  		m.i = c.getInt("ScanInterval", 100);
  		if (m.i < 10) {
  			m.i = 10;
  			m.l.info("ES NOTICE: Interval time is too low! Using 10 ticks instead.");
  		}
  		for (World w : Bukkit.getServer().getWorlds()) {
  			m.wlist.put(w.getName().trim(), new ESWorld(w));
  			continue;
  		}
  		if (c.contains("MonsterMaximums")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("MonsterMaximums").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setMonsterMaximum(Integer.parseInt(entry.getValue().toString().trim()));
  				continue;
  			}
  		}
  		if (c.contains("SquidMaximums")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("SquidMaximums").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setSquidMaximum(Integer.parseInt(entry.getValue().toString().trim()));
  				continue;
  			}
  		}
  		if (c.contains("AnimalMaximums")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("AnimalMaximums").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setAnimalMaximum(Integer.parseInt(entry.getValue().toString().trim()));
  				continue;
  			}
  		}
  		if (c.contains("ChunkCalculatedMonsterMaximums")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("ChunkCalculatedMonsterMaximums").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setpChunkMonsterMaximum(Integer.parseInt(entry.getValue().toString().trim()));
  				continue;
  			}
  		}
  		if (c.contains("ChunkCalculatedSquidMaximums")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("ChunkCalculatedSquidMaximums").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setpChunkSquidMaximum(Integer.parseInt(entry.getValue().toString().trim()));
  				continue;
  			}
  		}
  		if (c.contains("ChunkCalculatedAnimalMaximums")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("ChunkCalculatedAnimalMaximums").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setpChunkAnimalMaximum(Integer.parseInt(entry.getValue().toString().trim()));
  				continue;
  			}
  		}
  		if (c.contains("LimitSpawners")) {
  			for (Entry<String, Object> entry : c.getConfigurationSection("LimitSpawners").getValues(true).entrySet()) {
  				ESWorld esw =  m.wlist.get(entry.getKey().trim());
  				esw.setlSpawners((Boolean)entry.getValue());
  				continue;
  			}
  		}
		m.uSFlags = c.getBoolean("UseSpawnFlags", true);
		m.uRemoveM = c.getBoolean("UseMonsterDistanceRemoval", true);
		m.rdist = c.getInt("RemoveAtDistance", 128);
		m.cdist = c.getInt("CancelSpawnAtDistance", 92);
		m.d = c.getBoolean("Debug", false);
		if (m.d == true) m.l.info("ES NOTICE: Running in debug mode!");
  	}
}
