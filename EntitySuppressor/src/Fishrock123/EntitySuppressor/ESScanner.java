package Fishrock123.EntitySuppressor;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class ESScanner {
	private static int Scan;
	private static EntitySuppressor m;
	private static ESMethods methods;
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
								for (Player p : w.getPlayers()) {
									if (e.getLocation().distance(p.getLocation()) > m.rdist) {
										pdc++;
										if (sdist == 0 || e.getLocation().distance(p.getLocation()) < sdist) {
											sdist = e.getLocation().distance(p.getLocation());
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
	  					
	  					int lEntities = w.getLivingEntities().size() - w.getPlayers().size();
	  					
	  					if (m.lMonsters == true) {
					
	  						if (lEntities >= methods.getCurrentMax(w, "Monster") 
	  								&& !w.getAllowMonsters() == false) {
								w.setSpawnFlags(false, w.getAllowAnimals());
								if (m.d == true) {
									m.l.info("ES Debug: Monsters Disabled in " + w.getName());
								}
	  						}
	  						if (lEntities < (methods.getCurrentMax(w, "Monster") - (int)Math.ceil(Math.sqrt(methods.getCurrentMax(w, "Monster")))) 
	  								&& !w.getAllowMonsters() == true) {
	  							w.setSpawnFlags(true, w.getAllowAnimals());
	  							if (m.d == true) {
	  								m.l.info("ES Debug: Monsters Enabled in " + w.getName());
	  							}
							}
						}
	  					if (m.lAnimals == true) {
					
	  						if (lEntities >= methods.getCurrentMax(w, "Animal") 
	  								&& !w.getAllowAnimals() == false) {
								w.setSpawnFlags(w.getAllowMonsters(), false);
								if (m.d == true) {
									m.l.info("ES Debug: Animals Disabled in " + w.getName());
								}
	  						}
	  						if (lEntities < (methods.getCurrentMax(w, "Animal") - (int)Math.ceil(Math.sqrt(methods.getCurrentMax(w, "Animal")))) 
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
	
	public void init() {
		Scan = m.getServer().getScheduler().scheduleSyncRepeatingTask(m, r, m.i, m.i);
		r.run();
	}

	public void deinit() {
		m.getServer().getScheduler().cancelTask(Scan);
  	}
}
