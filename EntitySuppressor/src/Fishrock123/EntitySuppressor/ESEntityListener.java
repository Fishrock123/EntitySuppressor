package Fishrock123.EntitySuppressor;

import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class ESEntityListener extends EntityListener {
	public static EntitySuppressor p;
	private static int rTask;
	public ESEntityListener(EntitySuppressor instance) {
		p = instance;
	}
	
	public static int getwMax(World w) {
		if (p.wMs != null
				&& p.wMs.containsKey(w.getName())) {
			int wMax = (Integer)p.wMs.get(w.getName());
		
			return wMax;
			
		} else {
			return p.dMax;
			
		}
	}
	
	public static boolean getlS(World w) {
		if (p.lSwl != null
				&& p.lSwl.containsKey(w.getName())) {
			boolean wlS = (Boolean)p.lSwl.get(w.getName());
		
			return wlS;
			
		} else {
			return p.lS;
			
		}
	}

	public void onCreatureSpawn(CreatureSpawnEvent e) {
		World w = e.getLocation().getWorld();
		
		if (!e.isCancelled()) {
			
			if (p.d == true 
					&& (e.getCreatureType() == CreatureType.CHICKEN
						|| e.getCreatureType() == CreatureType.COW 
						|| e.getCreatureType() == CreatureType.PIG 
						|| e.getCreatureType() == CreatureType.SHEEP 
						|| e.getCreatureType() == CreatureType.WOLF)) {
				p.l.info("Spawned " + e.getCreatureType().getName() + " in " + w.getName());
			}
			
			if ((e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL 
					|| (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER 
							|| getlS(w) == true))
					&& p.wl.contains(w)) {
				
				int lEs = w.getLivingEntities().size() - w.getPlayers().size();
					
				if (e.getEntity() instanceof Monster 
						&& lEs >= getwMax(w)) {
					e.setCancelled(true);
					if (p.uSF == true) {
						w.setSpawnFlags(false, w.getAllowAnimals());
						if (p.d == true) {
							p.l.info("Monsters Disabled in " + w.getName());
						}
					}
				} 
				else if ((e.getCreatureType() == CreatureType.SQUID) 
						&& (p.lSQ == true) 
						&& (lEs < getwMax(w))) {
					e.setCancelled(true);
				}
			}
		}
	}

	public static void init() {
		if (p.uSF == true) {
			Runnable r = new Runnable() {
				public void run() {
			  		for (World w : p.getServer().getWorlds()) {
			  			if (p.wl.contains(w)) {
			  				int lEs = w.getLivingEntities().size() - w.getPlayers().size();
							int wcD;
							
							if (p.cDne = true) {
								wcD = getwMax(w) / 8;
							} else {
								wcD = p.cD;
							}

							if (lEs >= getwMax(w) 
									&& !w.getAllowMonsters() == false) {
								w.setSpawnFlags(false, w.getAllowAnimals());
								if (p.d == true) {
									p.l.info("Monsters Disabled in " + w.getName());
								}
							}
							else if (lEs < (getwMax(w) - wcD) 
									&& !w.getAllowMonsters() == true) {
								w.setSpawnFlags(true, w.getAllowAnimals());
								if (p.d == true) {
									p.l.info("Monsters Enabled in " + w.getName());
								}
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