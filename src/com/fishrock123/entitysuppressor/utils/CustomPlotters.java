package com.fishrock123.entitysuppressor.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.fishrock123.entitysuppressor.ESMethods;

public interface CustomPlotters {
	Metrics.Plotter totalEntities = new Metrics.Plotter("Entities") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getEntities().size();
        	}
            return count;
        }
    };
	
	Metrics.Plotter totalMobs = new Metrics.Plotter("Mobs") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getLivingEntities().size() - w.getPlayers().size();
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalMonsters = new Metrics.Plotter("Monsters") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += ESMethods.countMonsters(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalAnimals = new Metrics.Plotter("Animals") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += ESMethods.countAnimals(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalSquid = new Metrics.Plotter("Squid") {
        @Override
        public synchronized int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += ESMethods.countSquid(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalLoadedChunks = new Metrics.Plotter("Loaded Chunks") {
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