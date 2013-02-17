package com.fishrock123.entitysuppressor;

import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.fishrock123.entitysuppressor.utils.Utils;

public class EntityListener implements Listener {
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		World w = e.getLocation().getWorld();
		ESWorld esw = Utils.getESWorld(w.getName());
		
		if (esw != null) {
			
			if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
				
				if (e.getEntity() instanceof Monster && Config.lMonsters && 
						!(e.getEntity() instanceof EnderDragon || e.getEntity() instanceof Wither)) {
					
					if (Utils.countMonsters(w) >= Utils.getCurrentMax(w, Monster.class)) {
						e.setCancelled(true);
						
						if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER && Config.qSpawner) {
							esw.queueSpawn(e.getLocation(), e.getEntity());
						}
					}
					
					if (Config.uRemoveM && w.getPlayers().size() > 0) {
						double eh, ph;
						for (Player p : w.getPlayers()) {
							if (e.getLocation().distanceSquared(p.getLocation()) < Config.sqCancelDist) {
								e.setCancelled(true);
							} else if (Config.uLRemove) {
								eh = e.getLocation().getY();
								ph = p.getLocation().getY();
								if ((eh - ph > 0 ? -(eh - ph) : eh - ph) > Config.maxLDev) {
									e.setCancelled(true);
								}
							}
						}
					}
				}
				
				else if (e.getEntity() instanceof Ambient && Config.lBats && Utils.countAmbient(w) >= Utils.getCurrentMax(w, Ambient.class)) {
					e.setCancelled(true);
				}
				
				else if (e.getEntity() instanceof WaterMob && Config.lSquid && Utils.countWater(w) >= Utils.getCurrentMax(w, WaterMob.class)) {
					e.setCancelled(true);
				}
				
				else if (e.getEntity() instanceof Animals && Config.lAnimals && Utils.countAnimals(w) >= Utils.getCurrentMax(w, Animals.class)) {
					e.setCancelled(true);
				}
				
				else if (e.getEntity() instanceof NPC && Config.lNPCs && Utils.countAllTheTESTIFICATES(w) >= Utils.getCurrentMax(w, NPC.class)) {
					e.setCancelled(true);
				}
			}
		}
	}
}