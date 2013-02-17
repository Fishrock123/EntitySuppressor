package com.fishrock123.entitysuppressor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;

import com.fishrock123.entitysuppressor.utils.Utils;

public class CommandHandler {
	private ESPlugin m;
	
	public CommandHandler(ESPlugin instance) {
		m = instance;
	}
	
	public boolean process(CommandSender s, Command cmd, String cLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("es")) {
			if (args.length == 0) {
				return false;
			}
			//Version
			if (args[0].equalsIgnoreCase("version")) {
				s.sendMessage("This server is running EntitySuppressor v" + m.getDescription().getVersion());
				return true;
			}
			//Reload
			if (args[0].equalsIgnoreCase("reload")) {
				if (s instanceof Player && !((Player)s).hasPermission("esuppressor.reload")) {
					s.sendMessage(ChatColor.DARK_RED + "Permissions llama is not satisfied by your credentials.");
					return true;
				}
				final long startTime = System.nanoTime();
		        final long endTime;
		        
		        m.getServer().getScheduler().cancelTasks(m);
		        
		        Utils.eswLookup.clear();
		        
		        m.reloadConfig();
				
		        m.initialize();
		        
				endTime = System.nanoTime();
				s.sendMessage("EntitySuppressor version " + m.getDescription().getVersion() + " was reloaded! {" + Utils.convTime(endTime - startTime) + " ms}");
				if (!(s instanceof ConsoleCommandSender)) {
					m.log("EntitySuppressor version " + m.getDescription().getVersion() + " was reloaded! {" + Utils.convTime(endTime - startTime) + " ms}");
				}
				
				return true;
			}
			//Count
			if (args[0].equalsIgnoreCase("count")) {
				
				List<World> ewl = new ArrayList<World>();
				if (args.length == 2 && args[1].equalsIgnoreCase("all")) ewl.addAll(m.getServer().getWorlds());
				else if (args.length == 2 && m.getServer().getWorlds().contains(Bukkit.getWorld(args[1]))) ewl.add(m.getServer().getWorld(args[1]));
				else if (args.length == 1 && s instanceof Player) ewl.add(((Player)s).getWorld());
				else if (s instanceof ConsoleCommandSender) ewl.addAll(m.getServer().getWorlds());	
				
				int mc, ac, abc, wc, nc, pc;
				for (World w : ewl) {
					mc = ac = abc = wc = nc = pc = 0;
					
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Monster) ++mc;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Animals) ++ac;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Ambient) ++abc;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof WaterMob) ++wc;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof NPC) ++nc;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Player) ++pc;
						continue;
					}
					
					if (pc != w.getPlayers().size()) {
						m.log("ES CRITICAL ERROR: INCORRECT PLAYER COUNT.");
					}
					
					s.sendMessage("ES: " + w.getLoadedChunks().length + " chunks loaded in `" + w.getName() + "` (CCM: " + (w.getLoadedChunks().length >> 8) + ')'); //Integer bit hacking, >> 8 is equal / 256
					s.sendMessage("ES: Maximum monsters in `" + w.getName() + "` is: " + Utils.getCurrentMax(w, Monster.class));
					s.sendMessage("ES: Maximum squid in `" + w.getName() + "` is: " + Utils.getCurrentMax(w, WaterMob.class));
					s.sendMessage("ES: Maximum animals in `" + w.getName() + "` is: " + Utils.getCurrentMax(w, Animals.class));
					s.sendMessage("ES: Maximum bats in `" + w.getName() + "` is: " + Utils.getCurrentMax(w, Ambient.class));
		  			s.sendMessage("ES: Maximum NPCs in `" + w.getName() + "` is: " + Utils.getCurrentMax(w, NPC.class));
					s.sendMessage("ES Count: (" + (mc + ac + wc + pc) + "): " + mc + " monsters, " + ac + " animals, " + abc + " bats, " + wc + " squid, " + nc + " NPCs, and " + pc + " players in `" + w.getName() + '`');
					continue;
				}
				return true;
			}
			//remove
			if (args[0].equalsIgnoreCase("remove")) {
		    	  
				if ((s instanceof Player) && !((Player)s).hasPermission("esuppressor.remove")) {
					s.sendMessage(ChatColor.DARK_RED + "Permissions llama is not satisfied by your credentials.");
					return true;
				}
				List<World> ewl = new ArrayList<World>();
				if (args.length == 3 && args[2].equalsIgnoreCase("all")) ewl.addAll(m.getServer().getWorlds());
				else if (args.length == 3 && m.getServer().getWorlds().contains(Bukkit.getWorld(args[2]))) ewl.add(m.getServer().getWorld(args[2]));
				else if (args.length == 2 && s instanceof Player) ewl.add(((Player)s).getWorld());
				else if (args.length == 2 && s instanceof ConsoleCommandSender) ewl.addAll(m.getServer().getWorlds());
				
				if (args.length == 2) { 
					//All
					if (args[1].equalsIgnoreCase("all")) {
						
						for (World w : ewl) {
							for (LivingEntity e : w.getLivingEntities()) {
								if (!(e instanceof Player) && !Utils.isProtected(m, e)) e.remove();
								continue;
							}
	
							s.sendMessage("Removed all mobs in `" + w.getName() + '`');
							if (!(s instanceof ConsoleCommandSender)) {
								m.log("Removed all mobs in `" + w.getName() + '`');
							}
							continue;
						}
						return true;	
					} 
					//Animals
					else if (args[1].equalsIgnoreCase("animals")) {
						
						for (World w : ewl) {
							for (LivingEntity e : w.getLivingEntities()) {
								if (e instanceof Animals && !Utils.isProtected(m, e)) e.remove();
								continue;
							}
	
							s.sendMessage("Removed all animals in `" + w.getName() + '`');
							if (!(s instanceof ConsoleCommandSender)) {
								m.log("Removed all animals in `" + w.getName() + '`');
							}
							continue;
						}
						return true;
					} 
					//Monsters
					else if (args[1].equalsIgnoreCase("monsters")) {
						
						for (World w : ewl) {
							for (LivingEntity e : w.getLivingEntities()) {
								if (e instanceof Monster && !Utils.isProtected(m, e)) e.remove();
								continue;
							}
	
							s.sendMessage("Removed all monsters in `" + w.getName() + '`');
							if (!(s instanceof ConsoleCommandSender)) {
								m.log("Removed all monsters in `" + w.getName() + '`');
							}
							continue;
						}
						return true;
					}
					//Squid
					else if (args[1].equalsIgnoreCase("squid")) {
						
						for (World w : ewl) {
							for (LivingEntity e : w.getLivingEntities()) {
								if (e instanceof WaterMob && !Utils.isProtected(m, e)) e.remove();
								continue;
							}
	
							s.sendMessage("Removed all squid in `" + w.getName() + '`');
							if (!(s instanceof ConsoleCommandSender)) {
								m.log("Removed all squid in `" + w.getName() + '`');
							}
							continue;
						}
						return true;
					}
					//Bats
					else if (args[1].equalsIgnoreCase("bats")) {
						
						for (World w : ewl) {
							for (LivingEntity e : w.getLivingEntities()) {
								if (e instanceof Ambient && !Utils.isProtected(m, e)) e.remove();
								continue;
							}
	
							s.sendMessage("Removed all bats in `" + w.getName() + '`');
							if (!(s instanceof ConsoleCommandSender)) {
								m.log("Removed all bats in `" + w.getName() + '`');
							}
							continue;
						}
						return true;
					}
					//NPCs
					else if (args[1].equalsIgnoreCase("npcs") || args[1].equalsIgnoreCase("villagers")) {
						
						for (World w : ewl) {
							for (LivingEntity e : w.getLivingEntities()) {
								if (e instanceof NPC && !Utils.isProtected(m, e)) e.remove();
								continue;
							}
	
							s.sendMessage("Removed all npcs in `" + w.getName() + '`');
							if (!(s instanceof ConsoleCommandSender)) {
								m.log("Removed all npcs in `" + w.getName() + '`');
							}
							continue;
						}
						return true;
					}
				}
				return true;
			}
		}
		return false;
	}
}
