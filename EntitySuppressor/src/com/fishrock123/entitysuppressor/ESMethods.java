package com.fishrock123.entitysuppressor;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Squid;

import com.fishrock123.animalrights.ARDatabase;

public class ESMethods {
	private EntitySuppressor m;
	public ESMethods(EntitySuppressor instance) {
		m = instance;
	}
	
	public ESWorld getESWorld(String s) {
		if (m.eswLookup.containsKey(s)) {
			return m.eswLookup.get(s);
		} else {
			return null;
		}
	}
	
	public int getCurrentMax(World w, Class<? extends Entity> c) {
		try {
			return getCurrentMax(w.getName(), c);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getCurrentMax(World w) {
		return getCurrentMax(w, null);
	}
	
	public int getCurrentMax(String s, Class<? extends Entity> c) {
		return getCurrentMax(getESWorld(s), c);
	}
	
	public int getCurrentMax(ESWorld esw, Class<? extends Entity> c) {
		if (m.eswLookup != null
				&& esw != null) {
			
			if (c == null) {
				return ((esw.getpChunkAnimalMaximum() * esw.getLoadedChunks()) / 256) + ((esw.getpChunkSquidMaximum() * esw.getLoadedChunks()) / 256) + ((esw.getpChunkMonsterMaximum() * esw.getLoadedChunks()) / 256);
			}
			if (c.equals(Monster.class) 
					&& esw.hasMonsterMaximum()) {
				if (esw.haspChunkMonsterMaximum()
						&& ((esw.getpChunkMonsterMaximum() * esw.getLoadedChunks()) / 256) <= esw.getMonsterMaximum()) {
						return ((esw.getpChunkMonsterMaximum() * esw.getLoadedChunks()) / 256);
				} else {
					return esw.getMonsterMaximum();
				}
			}
			if (c.equals(Squid.class)  
					&& esw.hasSquidMaximum()) {
				if (esw.haspChunkSquidMaximum()
						&& ((esw.getpChunkSquidMaximum() * esw.getLoadedChunks()) / 256) <= esw.getSquidMaximum()) {
						return ((esw.getpChunkSquidMaximum() * esw.getLoadedChunks()) / 256);
				} else {
					return esw.getSquidMaximum();
				}
			}
			if (c.equals(Animals.class)  
					&& esw.hasAnimalMaximum()) {
				if (esw.haspChunkAnimalMaximum()
						&& ((esw.getpChunkAnimalMaximum() * esw.getLoadedChunks()) / 256) <= esw.getAnimalMaximum()) {
						return ((esw.getpChunkAnimalMaximum() * esw.getLoadedChunks()) / 256);
				} else {
					return esw.getAnimalMaximum();
				}
			}	
		}
		return 0;
	}
	
	public static int countAnimals(World w) {
		int i = 0;
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Animals) {
				 i++;
			}
		}	
		return i;
	}
	
	public static int countSquid(World w) {
		int i = 0;
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Squid) {
				 i++;
			}
		}		
		return i;
	}
	
	public static int countMonsters(World w) {
		int i = 0;
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Monster) {
				 i++;
			}
		}	
		return i;
	}
	
	public long convTime(long time) {
        return TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);  
    }
	
	public boolean isProtected(Entity e) {
		if (Bukkit.getPluginManager().isPluginEnabled("AnimalRights")) {
			try {
				if (ARDatabase.hasProtection(e)) {
					return true;
				}
			} catch (Exception ex) {
				m.l.info("Error connecting to AnimalRights, assuming all entites are protected.");
				return true;
			}
		}
		return false;
	}
}
