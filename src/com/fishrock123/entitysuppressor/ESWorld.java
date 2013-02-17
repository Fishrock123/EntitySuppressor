package com.fishrock123.entitysuppressor;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class ESWorld {
	public String name;
	public int loadedChunks = 0;
	public int MonsterMax;
	public int WaterMax;
	public int AnimalMax;
	public int AmbientMax;
	public int NPCMax;
	public int ccMonsterMax;
	public int ccWaterMax;
	public int ccAnimalMax;
	public int ccAmbientMax;
	public int ccNPCMax;
	private LinkedList<QueuedSpawn> queue = new LinkedList<QueuedSpawn>();
	
	public ESWorld(World w) {
		this(w.getName());
	}
	
	public ESWorld(String name) {
		this.name = name;
	}
	
	public void update(Integer i) {
		loadedChunks = i;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(name);
	}
	public String getName() {
		return name;
	}
	
	public void queueSpawn(Location loc, Entity e) {
		if (queue.size() < 20) {
			queue.add(new QueuedSpawn(loc, e.getType()));
		}
	}
	
	public void spawn(World world) {
		world.spawnEntity(queue.getFirst().loc, queue.getFirst().type);
		queue.removeFirst();
	}
	
	public boolean hasQueuedSpawn() {
		return queue.size() > 0;
	}
	
	public void setMaxByString(String key, int value) {
		if (key == "MonsterMaximums") MonsterMax = value;
		else if (key == "SquidMaximums") WaterMax = value;
		else if (key == "AnimalMaximums") AnimalMax = value;
		else if (key == "BatMaximums") AmbientMax = value;
		else if (key == "NPCMaximums") NPCMax = value;
		else if (key == "ChunkCalculatedMonsterMaximums") ccMonsterMax = value;
		else if (key == "ChunkCalculatedSquidMaximums") ccWaterMax = value;
		else if (key == "ChunkCalculatedAnimalMaximums") ccAnimalMax = value;
		else if (key == "ChunkCalculatedBatMaximums") ccAmbientMax = value;
		else if (key == "ChunkCalculatedNPCMaximums") ccNPCMax = value;
	}
	
	public boolean hasMonsterMax() {
		return MonsterMax >= 0 ?  true : false;
	}
	public boolean hasWaterMax() {
		return WaterMax >= 0 ?  true : false;
	}
	public boolean hasAnimalMax() {
		return AnimalMax >= 0 ?  true : false;
	}
	public boolean hasAmbientMax() {
		return AmbientMax >= 0 ?  true : false;
	}
	public boolean hasNPCMax() {
		return NPCMax >= 0 ?  true : false;
	}
	public boolean hasCCMonsterMax() {
		return ccMonsterMax >= 0 ?  true : false;
	}
	public boolean hasCCWaterMax() {
		return ccWaterMax >= 0 ?  true : false;
	}
	public boolean hasCCAnimalMax() {
		return ccAnimalMax >= 0 ?  true : false;
	}
	public boolean hasCCAmbientMax() {
		return ccAmbientMax >= 0 ?  true : false;
	}
	public boolean hasCCNPCMax() {
		return ccNPCMax >= 0 ?  true : false;
	}
}
