package com.fishrock123.entitysuppressor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;

public class ESCommands {
	private EntitySuppressor m;
	private ESMethods methods;
	private ESScanner scanner;
	public ESCommands(EntitySuppressor instance) {
		m = instance;
		methods = m.methods;
		scanner = m.scanner;
	}
	
	public boolean commandProcess(CommandSender s, Command cmd, String cLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("es")) {
			if (args.length == 0) {
				return false;
			}
			//Version
			if (args[0].equalsIgnoreCase("version")) {
				s.sendMessage("This server is running EntitySuppressor version " + m.getDescription().getVersion());
				return true;
			}
			//Reload
			if (args[0].equalsIgnoreCase("reload")) {
				if (s instanceof Player 
						&& !((Player)s).hasPermission("esuppressor.reload")) {
        	  
					s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
					return true;
				}
				final long startTime = System.nanoTime();
		        final long endTime;
		        
		        scanner.deinit();
		        
		        m.eswLookup.clear();
		        
		        m.reloadConfig();
				
		        m.initialize();
		        
				endTime = System.nanoTime();
				s.sendMessage("EntitySuppressor version " + m.getDescription().getVersion() + " was reloaded! {" + methods.convTime(endTime - startTime) + " ms}");
				if (!(s instanceof ConsoleCommandSender)) {
					m.l.info("EntitySuppressor version " + m.getDescription().getVersion() + " was reloaded! {" + methods.convTime(endTime - startTime) + " ms}");
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
					
				for (World w : ewl) {
					int ms = 0;
					int as = 0;
					int ss = 0;
					int ps = 0;
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Monster) ms++;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Animals) as++;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Squid) ss++;
						continue;
					}
					for (LivingEntity e : w.getLivingEntities()) {
						if (e instanceof Player) ps++;
						continue;
					}
					
					s.sendMessage("ES: " + w.getLoadedChunks().length + " chunks loaded in `" + w.getName() + "` (CCM: " + (w.getLoadedChunks().length >> 8) + ')'); //Integer bit hacking, >> 8 is equal / 256
					s.sendMessage("ES: Maximum monsters in `" + w.getName() + "` is: " + methods.getCurrentMax(w, Monster.class));
					s.sendMessage("ES: Maximum squid in `" + w.getName() + "` is: " + methods.getCurrentMax(w, Squid.class));
					s.sendMessage("ES: Maximum animals in `" + w.getName() + "` is: " + methods.getCurrentMax(w, Animals.class));
					s.sendMessage("ES Count: (" + (ms + as + ss + ps) + "): " + ms + " monsters, " + as + " animals, " + ss + " squid, and " + ps + " players in `" + w.getName() + '`');
					continue;
				}
				return true;
			}
			//remove
			if (args[0].equalsIgnoreCase("remove")) {
		    	  
				if ((s instanceof Player)
						&& !((Player)s).hasPermission("esuppressor.remove")) {
        	  
					s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
					return true;
				}
				List<World> ewl = new ArrayList<World>();
				if (args.length == 3 && args[2].equalsIgnoreCase("all")) ewl.addAll(m.getServer().getWorlds());
				else if (args.length == 3 && m.getServer().getWorlds().contains(Bukkit.getWorld(args[2]))) ewl.add(m.getServer().getWorld(args[2]));
				else if (args.length == 2 && s instanceof Player) ewl.add(((Player)s).getWorld());
				else if (args.length == 2 && s instanceof ConsoleCommandSender) ewl.addAll(m.getServer().getWorlds());
				
				//All
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("all")) {
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (!(e instanceof Player) && !methods.isProtected(e) ) e.remove();
							continue;
						}

						s.sendMessage("Removed all mobs in `" + w.getName() + '`');
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all mobs in `" + w.getName() + '`');
						}
						continue;
					}
					return true;	
				} 
				//Animals
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("animals")) {
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Animals && !methods.isProtected(e)) e.remove();
							continue;
						}

						s.sendMessage("Removed all animals in `" + w.getName() + '`');
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all animals in `" + w.getName() + '`');
						}
						continue;
					}
					return true;		
				} 
				//Monsters
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("monsters")) {
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Monster && !methods.isProtected(e)) e.remove();
							continue;
						}

						s.sendMessage("Removed all monsters in `" + w.getName() + '`');
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all monsters in `" + w.getName() + '`');
						}
						continue;
					}
					return true;		
				}
				//Squid
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("squid")) {
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (e instanceof Squid && !methods.isProtected(e)) e.remove();
							continue;
						}

						s.sendMessage("Removed all squid in `" + w.getName() + '`');
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all squid in `" + w.getName() + '`');
						}
						continue;
					}
					return true;		
				}
				return true;
			}
			return false;
		}
		return false;
	}
}
