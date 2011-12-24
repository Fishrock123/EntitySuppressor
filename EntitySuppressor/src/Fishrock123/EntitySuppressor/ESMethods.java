package Fishrock123.EntitySuppressor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Squid;

public class ESMethods {
	public static EntitySuppressor m;
	public ESMethods(EntitySuppressor instance) {
		m = instance;
	}
	
	public ESWorld getESWorld(World w) {
		return m.wlist.get(w.getName());
	}
	
	public int getCurrentMax(World w) {
		return getCurrentMax(w, "null");
	}
	
	public int getCurrentMax(World w, String s) {
		ESWorld esw = getESWorld(w);
		
		if (m.wlist != null
				&& esw != null) {
			
			if (s.equals("Monster") 
					&& esw.hasMonsterMaximum()) {
				if (esw.haspChunkMonsterMaximum()
						&& ((esw.getpChunkMonsterMaximum() * w.getLoadedChunks().length) / 256) <= esw.getMonsterMaximum()) {
						return ((esw.getpChunkMonsterMaximum() * w.getLoadedChunks().length) / 256);
				} else {
					return esw.getMonsterMaximum();
				}
			}
			if (s.equals("Squid")  
					&& esw.hasSquidMaximum()) {
				if (esw.haspChunkSquidMaximum()
						&& ((esw.getpChunkSquidMaximum() * w.getLoadedChunks().length) / 256) <= esw.getSquidMaximum()) {
						return ((esw.getpChunkSquidMaximum() * w.getLoadedChunks().length) / 256);
				} else {
					return esw.getSquidMaximum();
				}
			}
			if (s.equals("Animal")  
					&& esw.hasAnimalMaximum()) {
				if (esw.haspChunkAnimalMaximum()
						&& ((esw.getpChunkAnimalMaximum() * w.getLoadedChunks().length) / 256) <= esw.getAnimalMaximum()) {
						return ((esw.getpChunkAnimalMaximum() * w.getLoadedChunks().length) / 256);
				} else {
					return esw.getAnimalMaximum();
				}
			}
			if (s.equals("null")) {
				return ((esw.getpChunkAnimalMaximum() * w.getLoadedChunks().length) / 256) + ((esw.getpChunkSquidMaximum() * w.getLoadedChunks().length) / 256) + ((esw.getpChunkMonsterMaximum() * w.getLoadedChunks().length) / 256);
			}
			
		} else {
			return 0;	
		}
		
		return 0;
	}
	
	public int countAnimals(World w) {
		List<LivingEntity> animals = new ArrayList<LivingEntity>();
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Animals) {
				 animals.add(a);
			}
		}
			
		return animals.size();
	}
	
	public int countSquid(World w) {
		List<LivingEntity> squid = new ArrayList<LivingEntity>();
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Squid) {
				 squid.add(a);
			}
		}
			
		return squid.size();
	}
	
	public int countMonsters(World w) {
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
}
