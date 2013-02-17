package com.fishrock123.entitysuppressor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import com.fishrock123.entitysuppressor.utils.Utils;

public class EntityScanner {
	private ESPlugin m;
	private int wIndex = 0;
	private World world;
	private ESWorld esw;
	private int eIndex = Integer.MAX_VALUE;
	
	public EntityScanner(ESPlugin instance) {
		m = instance;
	}
	
	Runnable removalScan = new Runnable() {
		public void run() {
			int thisCount = 0;
			world = Bukkit.getServer().getWorlds().get(wIndex);
			if (eIndex > world.getLivingEntities().size() || !Utils.eswLookup.containsKey(world.getName())) {
				wIndex = (wIndex < Bukkit.getServer().getWorlds().size() - 1 ? wIndex + 1 : 0);
				return;
			}
			
			esw = Utils.getESWorld(world.getName());
			
			Entity e;
			boolean r;
			for (thisCount = 0; thisCount < Config.entitiesPerScan && eIndex < world.getLivingEntities().size(); eIndex += thisCount++) {
				e = world.getLivingEntities().get(eIndex);
				if (e instanceof Monster && !(e instanceof EnderDragon || e instanceof Wither)) {
					if (world.getPlayers().size() <= 0) {
						e.remove();
					} else {
						for (Player p : world.getPlayers()) {
							if (e.getLocation().distanceSquared(p.getLocation()) > Config.sqRemovalDist) {
								r = true;
								for (ItemStack is : ((Monster)e).getEquipment().getArmorContents()) {
									if (is.getTypeId() != 0 || (is.getType() == Material.BOW && !is.getEnchantments().isEmpty())) {
										r = false;
										break;
									}
								}
								if (r) {
									e.remove();
									if (esw.hasQueuedSpawn()) esw.spawn(world);
								}
							}
						}
					}
				}
			}
		}
	};
	
	Runnable chunkQuery = new Runnable() {
		public void run() {
			List<String> worldnames = new ArrayList<String>();
			
			for (World w : Bukkit.getWorlds()) {
				if (!worldnames.contains(w.getName())) worldnames.add(w.getName());
			}
			
			for (Entry<String, ESWorld> e : Utils.eswLookup.entrySet()) {
				if (worldnames.contains(e.getKey())) {
					e.getValue().update(Bukkit.getWorld(e.getKey()).getLoadedChunks().length);
				}
			}
		}
	};
	
	public void init() {
		boolean on = false;
		for (World w : Bukkit.getWorlds()) {
			if (Utils.eswLookup.containsKey(w.getName())) {
				on = true; 
				break;
			}
		}
		
		if (on) {
			if (Config.uRemoveM) {
				m.getServer().getScheduler().scheduleSyncRepeatingTask(m, removalScan, Config.interval, Config.interval);
				removalScan.run();
			}
			m.getServer().getScheduler().scheduleSyncRepeatingTask(m, chunkQuery, 200, 200);
			chunkQuery.run();
		}
	}
}
