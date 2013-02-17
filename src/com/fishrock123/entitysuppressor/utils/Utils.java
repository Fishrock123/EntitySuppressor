package com.fishrock123.entitysuppressor.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.WaterMob;

import com.fishrock123.animalrights.ARDatabase;
import com.fishrock123.entitysuppressor.ESPlugin;
import com.fishrock123.entitysuppressor.ESWorld;

public class Utils {
	public static Map<String, ESWorld> eswLookup = new HashMap<String, ESWorld>();
	
	public static ESWorld getESWorld(String s) {
		if (eswLookup.containsKey(s)) {
			return eswLookup.get(s);
		} else {
			return null;
		}
	}
	
	public static int getCurrentMax(World w, Class<? extends Entity> c) {
		try {
			return getCurrentMax(w.getName(), c);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getCurrentMax(World w) {
		return getCurrentMax(w, null);
	}
	
	public static int getCurrentMax(String s, Class<? extends Entity> c) {
		return getCurrentMax(getESWorld(s), c);
	}
	
	public static int getCurrentMax(ESWorld esw, Class<? extends Entity> c) {
		if (eswLookup != null && esw != null) {
			
			//Integer bit hacking, >> 8 is equal to / 256
			if (c == null) {
				return ((esw.ccAnimalMax * esw.loadedChunks) >> 8) + ((esw.ccWaterMax * esw.loadedChunks) >> 8) + ((esw.ccMonsterMax * esw.loadedChunks) >> 8) + ((esw.ccAmbientMax * esw.loadedChunks) >> 8) + ((esw.ccNPCMax * esw.loadedChunks) >> 8);
			}
			else if (c == Monster.class && esw.hasMonsterMax()) {
				if (esw.hasCCMonsterMax() && ((esw.ccMonsterMax * esw.loadedChunks) >> 8) <= esw.MonsterMax) {
					return ((esw.ccMonsterMax * esw.loadedChunks) >> 8);
				} else {
					return esw.MonsterMax;
				}
			}
			else if (c == Ambient.class && esw.hasAmbientMax()) {
				if (esw.hasCCAmbientMax() && ((esw.ccAmbientMax * esw.loadedChunks) >> 8) <= esw.AmbientMax) {
					return ((esw.ccAmbientMax * esw.loadedChunks) >> 8);
				} else {
					return esw.AmbientMax;
				}
			}
			else if (c == WaterMob.class && esw.hasWaterMax()) {
				if (esw.hasCCWaterMax() && ((esw.ccWaterMax * esw.loadedChunks) >> 8) <= esw.WaterMax) {
					return ((esw.ccWaterMax * esw.loadedChunks) >> 8);
				} else {
					return esw.WaterMax;
				}
			}
			else if (c == Animals.class && esw.hasAnimalMax()) {
				if (esw.hasCCAnimalMax() && ((esw.ccAnimalMax * esw.loadedChunks) >> 8) <= esw.AnimalMax) {
					return ((esw.ccAnimalMax * esw.loadedChunks) >> 8);
				} else {
					return esw.AnimalMax;
				}
			}
			else if (c == NPC.class && esw.hasNPCMax()) {
				if (esw.hasCCNPCMax() && ((esw.ccNPCMax * esw.loadedChunks) >> 8) <= esw.NPCMax) {
					return ((esw.ccNPCMax * esw.loadedChunks) >> 8);
				} else {
					return esw.NPCMax;
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
	
	public static int countWater(World w) {
		int i = 0;
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof WaterMob) {
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
	
	public static int countAmbient(World w) {
		int i = 0;
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof Ambient) {
				 i++;
			}
		}		
		return i;
	}
	
	public static int countAllTheTESTIFICATES(World w) {
		int i = 0;
		for (LivingEntity a : w.getLivingEntities()) {
			if (a instanceof NPC) {
				 i++;
			}
		}		
		return i;
	}
	
	public static long convTime(long time) {
		return TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
	}
	
	public static boolean isProtected(ESPlugin m, Entity e) {
		if (Bukkit.getPluginManager().isPluginEnabled("AnimalRights")) {
			try {
				if (ARDatabase.hasProtection(e)) {
					return true;
				}
			} catch (Exception ex) {
				m.log("Error connecting to AnimalRights, assuming all entites are protected.");
				return true;
			}
		}
		return false;
	}
}
