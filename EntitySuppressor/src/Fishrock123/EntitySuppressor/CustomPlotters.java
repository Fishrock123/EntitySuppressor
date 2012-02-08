package Fishrock123.EntitySuppressor;

import org.bukkit.Bukkit;
import org.bukkit.World;

public interface CustomPlotters {
	Metrics.Plotter totalEntities = new Metrics.Plotter() {
    	
        @Override
        public String getColumnName() {
            return "Entities";
        }

        @Override
        public int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getEntities().size();
        	}
            return count;
        }
    };
	
	Metrics.Plotter totalMobs = new Metrics.Plotter() {
    	
        @Override
        public String getColumnName() {
            return "Mobs";
        }

        @Override
        public int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getLivingEntities().size() - w.getPlayers().size();
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalMonsters = new Metrics.Plotter() {
    	
        @Override
        public String getColumnName() {
            return "Monsters";
        }

        @Override
        public int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += ESMethods.countMonsters(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalAnimals = new Metrics.Plotter() {
    	
        @Override
        public String getColumnName() {
            return "Animals";
        }

        @Override
        public int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += ESMethods.countAnimals(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalSquid = new Metrics.Plotter() {
    	
        @Override
        public String getColumnName() {
            return "Squid";
        }

        @Override
        public int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += ESMethods.countSquid(w);
        	}
            return count;
        }
    };
    
    Metrics.Plotter totalLoadedChunks = new Metrics.Plotter() {
    	
        @Override
        public String getColumnName() {
            return "Loaded Chunks";
        }

        @Override
        public int getValue() {
        	int count = 0;
        	for (World w : Bukkit.getWorlds()) {
        		count += w.getLoadedChunks().length;
        	}
            return count;
        }
    };
}
