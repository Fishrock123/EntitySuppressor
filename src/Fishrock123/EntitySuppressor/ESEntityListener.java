package Fishrock123.EntitySuppressor;

import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class ESEntityListener extends EntityListener {
	private static EntitySuppressor m;
	private static ESMethods methods;
	public ESEntityListener(EntitySuppressor instance) {
		m = instance;
		methods = m.methods;
	}

	public void onCreatureSpawn(CreatureSpawnEvent e) {
		World w = e.getLocation().getWorld();
		ESWorld esw = methods.getESWorld(w);
		
		if (!e.isCancelled()) {
			
			if ((e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL 
					|| (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER 
							|| esw.getlSpawners() == true))
					&& esw != null) {
					
				if (e.getEntity() instanceof Monster
						&& m.lMonsters == true) {
					if (methods.countMonsters(w) >= methods.getCurrentMax(w, "Monster")) {
						e.setCancelled(true);
						if (m.uSFlags == true) {
							w.setSpawnFlags(false, w.getAllowAnimals());
							if (m.d == true) {
								m.l.info("ES Debug: Monsters Disabled in " + w.getName());
							}
						}
					}
					if (m.uRemoveM == true
							&& w.getPlayers().size() >= 1) {
						int pdc = 0;
						double sdist = 0;
						for (Player p : w.getPlayers()) {
							if (e.getLocation().distance(p.getLocation()) > m.cdist) {
								pdc++;
								if (sdist == 0 || e.getLocation().distance(p.getLocation()) < sdist) {
									sdist = e.getLocation().distance(p.getLocation());
								}
							}
						}
						if (pdc == w.getPlayers().size()) {
							e.setCancelled(true);
							if (m.d == true) {
								m.l.info("ES Debug: Distance too great (" + (long)sdist + "), cancelled spawn.");
							}
						}
					}
				} 
				if (e.getEntity() instanceof Animals
						&& m.lAnimals == true
						&& methods.countAnimals(w) >= methods.getCurrentMax(w, "Animal")) {
					e.setCancelled(true);
					if (m.uSFlags == true) {
						w.setSpawnFlags(w.getAllowMonsters(), false);
						if (m.d == true) {
							m.l.info("ES Debug: Monsters Disabled in " + w.getName());
						}
					}
				}
				if ((e.getCreatureType() == CreatureType.SQUID 
						&& m.lSquid == true 
						&& methods.countSquid(w) >= methods.getCurrentMax(w, "Squid"))) {
					e.setCancelled(true);
				}
				if (m.d == true 
						&& !e.isCancelled()
						&& (e.getEntity() instanceof Animals)) {
					m.l.info("ES Debug: Spawned " + e.getCreatureType().getName() + " in " + w.getName());
				}
			}
		}
	}
}