package Fishrock123.EntitySuppressor;

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
		        
		        m.reloadConfig();
				
		        m.initialize();
		        
				endTime = System.nanoTime();
				s.sendMessage("EntitySuppressor version " + m.getDescription().getVersion() + " was reloaded! {" + methods.convTime(endTime - startTime) + " ms}");
				if (!(s instanceof ConsoleCommandSender)) {
					m.l.info("EntitySuppressor version " + m.getDescription().getVersion() + " was reloaded! {" + methods.convTime(endTime - startTime) + " ms}");
				}
				
				return true;
			}
			//Print -- This is for debug only.
			if (args[0].equalsIgnoreCase("print")
					&& m.d == true) {
				if (s instanceof Player 
						&& !((Player)s).hasPermission("esuppressor.print")) {
        	  
					s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
					return true;
				}
				for (World w : m.getServer().getWorlds()) {
					m.l.info("Printing all living entities in " + w);
					m.l.info(w.getLivingEntities().toString());
					continue;
				}
				return true;
			}
			//Count
			if (args[0].equalsIgnoreCase("count")) {
				
				if ((s instanceof Player)
						&& !s.getName().equals("Fishrock123")
						&& !((Player)s).hasPermission("esuppressor.count")) {
        	  
						s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
						return true;
					}
				
				List<World> ewl = new ArrayList<World>();
				if (args.length == 2 && args[1].equalsIgnoreCase("all")) ewl.addAll(m.getServer().getWorlds());
				if (args.length == 2 && m.getServer().getWorlds().contains(Bukkit.getWorld(args[1]))) ewl.add(m.getServer().getWorld(args[1]));
				if (args.length == 1 && s instanceof Player) ewl.add(((Player)s).getWorld());
				if (!(s instanceof Player)) ewl.addAll(m.getServer().getWorlds());
					
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
					
					s.sendMessage("ES Debug: " + w.getLoadedChunks().length + " chunks loaded in `" + w.getName() + "` (" + ((double)w.getLoadedChunks().length / 256D) + ")");
					s.sendMessage("ES: Maximum monsters in " + w.getName() + " is: " + methods.getCurrentMax(w, "Monster"));
					s.sendMessage("ES: Maximum squid in " + w.getName() + " is: " + methods.getCurrentMax(w, "Squid"));
					s.sendMessage("ES: Maximum animals in " + w.getName() + " is: " + methods.getCurrentMax(w, "Animal"));
					s.sendMessage("ES Count: (" + (ms + as + ss + ps) + "): " + ms + " monsters, " + as + " animals, " + ss + " Squid, and " + ps + " players in `" + w.getName() + "`");
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
				if (args.length == 3 && m.getServer().getWorlds().contains(Bukkit.getWorld(args[2]))) ewl.add(m.getServer().getWorld(args[2]));
				if (args.length == 2 && s instanceof Player) ewl.add(((Player)s).getWorld());
				if (args.length == 2 && !(s instanceof Player)) ewl.addAll(m.getServer().getWorlds());
				
				//All
				if (args.length == 2 
						&& args[1].equalsIgnoreCase("all")) {
					
					for (World w : ewl) {
						for (LivingEntity e : w.getLivingEntities()) {
							if (!(e instanceof Player)) e.remove();
							continue;
						}

						s.sendMessage("Removed all living entities in " + w.getName());
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all living entities in " + w.getName());
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
							if (e instanceof Animals) e.remove();
							continue;
						}

						s.sendMessage("Removed all animals in " + w.getName());
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all animals in " + w.getName());
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
							if (e instanceof Monster) e.remove();
							continue;
						}

						s.sendMessage("Removed all monsters in " + w.getName());
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all monsters in " + w.getName());
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
							if (e instanceof Squid) e.remove();
							continue;
						}

						s.sendMessage("Removed all squid in " + w.getName());
						if (!(s instanceof ConsoleCommandSender)) {
							m.l.info("Removed all squid in " + w.getName());
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
