package Fishrock123.EntitySuppressor;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class ESScanner {
	private int Scan;
	private int Update;
	private EntitySuppressor m;
	private ESMethods methods;
	public ESScanner(EntitySuppressor instance) {
		m = instance;
		methods = m.methods;
	}
	
	Runnable r = new Runnable() {
		public void run() {
	  		for (World w : m.getServer().getWorlds()) {
	  			if (m.wlist.containsKey(w.getName())) {
	  				
	  				if (m.uRemoveM == true) {
	  					for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Monster 
									&& w.getPlayers().size() >= 1) {
								int pdc = 0;
								double sdist = 0;
								double pdist = 0;
								for (Player p : w.getPlayers()) {
									pdist = e.getLocation().distance(p.getLocation());
									if (pdist > m.rdist) {
										pdc++;
										if (sdist == 0 || pdist < sdist) {
											sdist = pdist;
										}
									}
								}
								if (pdc == w.getPlayers().size()) {
									e.remove();
									if (m.d == true) {
										m.l.info("ES Debug: Distance too great (" + (long)sdist + "), removed creature.");
									}
								}
							}
						}
	  				}
	  				
	  				if (m.uSFlags == true) {
	  					
	  					int mobs = w.getLivingEntities().size() - w.getPlayers().size();
	  					
	  					if (m.lMonsters == true) {
					
	  						if (mobs >= methods.getCurrentMax(w, "Monster") 
	  								&& !w.getAllowMonsters() == false) {
								w.setSpawnFlags(false, w.getAllowAnimals());
								if (m.d == true) {
									m.l.info("ES Debug: Monsters Disabled in " + w.getName());
								}
	  						}
	  						if (mobs < (methods.getCurrentMax(w, "Monster") - (int)Math.ceil(Math.sqrt(methods.getCurrentMax(w, "Monster")))) 
	  								&& !w.getAllowMonsters() == true) {
	  							w.setSpawnFlags(true, w.getAllowAnimals());
	  							if (m.d == true) {
	  								m.l.info("ES Debug: Monsters Enabled in " + w.getName());
	  							}
							}
						}
	  					if (m.lAnimals == true) {
					
	  						if (mobs >= methods.getCurrentMax(w, "Animal") 
	  								&& !w.getAllowAnimals() == false) {
								w.setSpawnFlags(w.getAllowMonsters(), false);
								if (m.d == true) {
									m.l.info("ES Debug: Animals Disabled in " + w.getName());
								}
	  						}
	  						if (mobs < (methods.getCurrentMax(w, "Animal") - (int)Math.ceil(Math.sqrt(methods.getCurrentMax(w, "Animal")))) 
	  								&& !w.getAllowAnimals() == true) {
	  							w.setSpawnFlags(w.getAllowMonsters(), true);
	  							if (m.d == true) {
	  								m.l.info("ES Debug: Animals Enabled in " + w.getName());
	  							}
							}
						}
	  				}
	  			//Else: Take a snooze.
	  			}
	  		}
		}
	};
	
	Runnable u = new Runnable() {
		public void run() {
			for (Entry<String, ESWorld> e : m.wlist.entrySet()) {
				e.getValue().update(Bukkit.getWorld(e.getKey()).getLoadedChunks().length);
			}
		}
	};
	
	public void init() {
		Scan = m.getServer().getScheduler().scheduleSyncRepeatingTask(m, r, m.i, m.i);
		r.run();
		Update = m.getServer().getScheduler().scheduleSyncRepeatingTask(m, u, 20, 20);
		u.run();
	}

	public void deinit() {
		m.getServer().getScheduler().cancelTask(Scan);
		m.getServer().getScheduler().cancelTask(Update);
  	}
}
