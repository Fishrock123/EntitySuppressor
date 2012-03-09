package com.fishrock123.entitysuppressor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.fishrock123.entitysuppressor.utils.math.RootMath;

public class ESScanner {
	private EntitySuppressor m;
	private ESMethods methods;
	public ESScanner(EntitySuppressor instance) {
		m = instance;
		methods = m.methods;
	}
	
	Runnable r = new Runnable() {
		public void run() {
	  		for (World w : m.getServer().getWorlds()) {
	  			if (m.eswLookup.containsKey(w.getName())) {
	  				
	  				if (m.uRemoveM == true) {
	  					for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Monster
									&& w.getPlayers().size() > 0) {
								int i = 0;
								int pdc = 0;
								double sdist = 0;
								double pdist = 0;
								for (Player p : w.getPlayers()) {
									i++;
									pdist = e.getLocation().distanceSquared(p.getLocation());
									if (pdist > m.sqRemovalDist) {
										pdc++;
										if (sdist == 0 || pdist < sdist) {
											sdist = pdist;
										}
									}
								}
								if (pdc == i) {
									e.remove();
									if (m.d == true) {
										m.l.info("Debug: Distance too great (" + (int)RootMath.sqrt((float)sdist) + "), removed creature.");
									}
								}
							}
						}
	  				}
	  				
	  				if (m.uSFlags == true) {
	  					int currentMax = 0;
	  					
	  					if (m.lMonsters == true) {
	  						int currentMonsters = ESMethods.countMonsters(w);
	  						currentMax = methods.getCurrentMax(w, Monster.class);
					
	  						if (w.getAllowMonsters() != true && currentMonsters < currentMax - (int)RootMath.sqrtApprox(currentMax)) {
	  							w.setSpawnFlags(true, w.getAllowAnimals());
	  							if (m.d == true) {
	  								m.l.info("Debug: Monsters Enabled in `" + w.getName() + '`');
	  							}
							}
	  						else if (w.getAllowMonsters() != false && currentMonsters >= currentMax) {
								w.setSpawnFlags(false, w.getAllowAnimals());
								if (m.d == true) {
									m.l.info("Debug: Monsters Disabled in `" + w.getName() + '`');
								}
	  						}
						}
	  					if (m.lAnimals == true) {
	  						int currentAnimals = ESMethods.countAnimals(w);
	  						currentMax = methods.getCurrentMax(w, Animals.class);
					
	  						if (w.getAllowAnimals() != true && currentAnimals < currentMax - (int)RootMath.sqrtApprox(currentMax)) {
	  							w.setSpawnFlags(w.getAllowMonsters(), true);
	  							if (m.d == true) {
	  								m.l.info("Debug: Animals Enabled in `" + w.getName() + '`');
	  							}
							}
	  						else if (w.getAllowAnimals() != false && currentAnimals >= currentMax) {
								w.setSpawnFlags(w.getAllowMonsters(), false);
								if (m.d == true) {
									m.l.info("Debug: Animals Disabled in `" + w.getName() + '`');
								}
	  						}
						}
	  				}
	  			}
	  		}
		}
	};
	
	Runnable u = new Runnable() {
		public void run() {
			List<String> worldnames = new ArrayList<String>();
			
			for (World w : Bukkit.getWorlds()) {
				worldnames.add(w.getName());
			}
			
			for (Entry<String, ESWorld> e : m.eswLookup.entrySet()) {
				if (worldnames.contains(e.getKey())) {
					e.getValue().update(Bukkit.getWorld(e.getKey()).getLoadedChunks().length);
				}
			}
		}
	};
	
	public void init() {
		m.getServer().getScheduler().scheduleSyncRepeatingTask(m, r, m.i, m.i);
		r.run();
		m.getServer().getScheduler().scheduleSyncRepeatingTask(m, u, 200, 200);
		u.run();
	}

	public void deinit() {
		m.getServer().getScheduler().cancelTasks(m);
  	}
}
