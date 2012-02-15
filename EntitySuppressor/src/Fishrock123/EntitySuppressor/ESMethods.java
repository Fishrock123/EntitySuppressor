package Fishrock123.EntitySuppressor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Squid;

import Fishrock123.AnimalRights.ARDatabase;

public class ESMethods {
	private EntitySuppressor m;
	public ESMethods(EntitySuppressor instance) {
		m = instance;
	}
	
	public ESWorld getESWorld(String s) {
		if (m.wlist.containsKey(s)) {
			return m.wlist.get(s);
		} else {
			return null;
		}
	}
	
	public int getCurrentMax(World w, String t) {
		try {
			return getCurrentMax(w.getName(), t);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getCurrentMax(World w) {
		return getCurrentMax(w, "null");
	}
	
	public int getCurrentMax(String s, String t) {
		ESWorld esw = getESWorld(s);
		
		if (m.wlist != null
				&& esw != null) {
			
			if (t.equals("Monster") 
					&& esw.hasMonsterMaximum()) {
				if (esw.haspChunkMonsterMaximum()
						&& ((esw.getpChunkMonsterMaximum() * esw.getLoadedChunks()) / 256) <= esw.getMonsterMaximum()) {
						return ((esw.getpChunkMonsterMaximum() * esw.getLoadedChunks()) / 256);
				} else {
					return esw.getMonsterMaximum();
				}
			}
			if (t.equals("Squid")  
					&& esw.hasSquidMaximum()) {
				if (esw.haspChunkSquidMaximum()
						&& ((esw.getpChunkSquidMaximum() * esw.getLoadedChunks()) / 256) <= esw.getSquidMaximum()) {
						return ((esw.getpChunkSquidMaximum() * esw.getLoadedChunks()) / 256);
				} else {
					return esw.getSquidMaximum();
				}
			}
			if (t.equals("Animal")  
					&& esw.hasAnimalMaximum()) {
				if (esw.haspChunkAnimalMaximum()
						&& ((esw.getpChunkAnimalMaximum() * esw.getLoadedChunks()) / 256) <= esw.getAnimalMaximum()) {
						return ((esw.getpChunkAnimalMaximum() * esw.getLoadedChunks()) / 256);
				} else {
					return esw.getAnimalMaximum();
				}
			}
			if (t.equals("null")) {
				return ((esw.getpChunkAnimalMaximum() * esw.getLoadedChunks()) / 256) + ((esw.getpChunkSquidMaximum() * esw.getLoadedChunks()) / 256) + ((esw.getpChunkMonsterMaximum() * esw.getLoadedChunks()) / 256);
			}
			
		} else {
			return 0;	
		}
		
		return 0;
	}
	
	public static int countAnimals(World w) {
		List<LivingEntity> animals = new ArrayList<LivingEntity>();
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Animals) {
				 animals.add(a);
			}
		}
			
		return animals.size();
	}
	
	public static int countSquid(World w) {
		List<LivingEntity> squid = new ArrayList<LivingEntity>();
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Squid) {
				 squid.add(a);
			}
		}
			
		return squid.size();
	}
	
	public static int countMonsters(World w) {
		List<LivingEntity> monsters = new ArrayList<LivingEntity>();
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Monster) {
				 monsters.add(a);
			}
		}
			
		return monsters.size();
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
