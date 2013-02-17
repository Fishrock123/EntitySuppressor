package com.fishrock123.entitysuppressor.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;


public interface CustomPlotters {
	Metrics.Plotter entitiesRatio = new Metrics.Plotter("All Entities") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getEntities().size();
        	}
            return count;
        }
    };
	
	Metrics.Plotter mobsRatio = new Metrics.Plotter("Mobs") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getLivingEntities().size() - w.getPlayers().size();
        	}
            return count;
        }
    };
    
    Metrics.Plotter monstersRatio = new Metrics.Plotter("Monsters") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += Utils.countMonsters(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter animalsRatio = new Metrics.Plotter("Animals") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += Utils.countAnimals(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter squidRatio = new Metrics.Plotter("Squid") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += Utils.countWater(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter batRatio = new Metrics.Plotter("Bats") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += Utils.countAmbient(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter npcRatio = new Metrics.Plotter("Villagers") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += Utils.countAllTheTESTIFICATES(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter loadedChunksPerWorld = new Metrics.Plotter("Loaded Chunks") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getLoadedChunks().length;
        	}
            return count;
        }
    };
}