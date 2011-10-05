package Fishrock123.EntitySuppressor;

import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class ESEntityListener extends EntityListener {
	public static EntitySuppressor p;
	private static int rTask;
	public ESEntityListener(EntitySuppressor instance) {
		p = instance;
	}

	public void onCreatureSpawn(CreatureSpawnEvent e) {
		
		if ((!e.isCancelled()) && (
				(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) 
				|| (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER)) && (
						p.wl != null) && (
								p.wl.contains(e.getLocation().getWorld()))) {
				
			HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(e.getLocation().getWorld().getLivingEntities());
			int lEs = eSet.size() - e.getLocation().getWorld().getPlayers().size();
				
			if ((p.d == true) && (
					(e.getCreatureType() == CreatureType.CHICKEN) 
					|| (e.getCreatureType() == CreatureType.COW) 
					|| (e.getCreatureType() == CreatureType.PIG) 
					|| (e.getCreatureType() == CreatureType.SHEEP) 
					|| (e.getCreatureType() == CreatureType.WOLF))) {
				p.l.info("Spawned " + e.getCreatureType().getName() + " in " + e.getLocation().getWorld().getName());
			}

			if ((e.getEntity() instanceof Monster) && (lEs >= p.maxM)) {
				e.setCancelled(true);
				e.getLocation().getWorld().setSpawnFlags(false, e.getLocation().getWorld().getAllowAnimals());
					if (p.d == true) {
						p.l.info("Monsters Disabled in " + e.getLocation().getWorld().getName());
				}
			} 
			else if ((e.getCreatureType() == CreatureType.SQUID) && (p.lS == true) && (lEs < p.maxM)) {
				e.setCancelled(true);
			}
		}
	}

	public static void init() {
		if (p.wl != null) {
			Runnable r = new Runnable() {
				public void run() {
					for (World w : p.wl) {
					
						HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
						int lEs = eSet.size() - w.getPlayers().size();

						if ((lEs >= p.maxM) && (!w.getAllowMonsters() == false)) {
							w.setSpawnFlags(false, w.getAllowAnimals());
							if (p.d == true) {
								p.l.info("Monsters Disabled in " + w.getName());
							}
						}
						else if ((lEs < (p.maxM - p.cD)) && (!w.getAllowMonsters() == true)) {
							w.setSpawnFlags(true, w.getAllowAnimals());
							if (p.d == true) {
								p.l.info("Monsters Enabled in " + w.getName());
							}
						}
					}
				}
			};
			rTask = p.getServer().getScheduler().scheduleSyncRepeatingTask(p, r, p.i, p.i);
			r.run();
		}
		//Else: Take a snooze.
	}

	public static void deinit() {
		p.getServer().getScheduler().cancelTask(rTask);
  	}
}