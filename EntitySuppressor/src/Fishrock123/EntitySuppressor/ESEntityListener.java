package Fishrock123.EntitySuppressor;

import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ESEntityListener implements Listener {
	private EntitySuppressor m;
	private ESMethods methods;
	public ESEntityListener(EntitySuppressor instance) {
		m = instance;
		methods = m.methods;
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		World w = e.getLocation().getWorld();
		ESWorld esw = methods.getESWorld(w.getName());
		
		if (!e.isCancelled() 
				&& esw != null) {
			
			if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL 
					|| (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER 
							|| (esw.haslSpawners() == true && esw.getlSpawners() == true))) {
					
				if (e.getEntity() instanceof Monster
						&& m.lMonsters == true) {
					if (ESMethods.countMonsters(w) >= methods.getCurrentMax(w, "Monster")) {
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
						double pdist = 0;
						for (Player p : w.getPlayers()) {
							pdist = e.getLocation().distanceSquared(p.getLocation());
							if (pdist > m.sqCancelDist) {
								pdc++;
								if (sdist == 0 || pdist < sdist) {
									sdist = pdist;
								}
							}
						}
						if (pdc == w.getPlayers().size()) {
							e.setCancelled(true);
							if (m.d == true) {
								m.l.info("ES Debug: Distance too great (" + (long)Math.sqrt(sdist) + "), cancelled spawn.");
							}
						}
					}
				} 
				if (e.getEntity() instanceof Animals
						&& m.lAnimals == true
						&& ESMethods.countAnimals(w) >= methods.getCurrentMax(w, "Animal")) {
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
						&& ESMethods.countSquid(w) >= methods.getCurrentMax(w, "Squid"))) {
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