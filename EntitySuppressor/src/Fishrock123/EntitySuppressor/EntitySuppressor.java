package Fishrock123.EntitySuppressor;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EntitySuppressor extends JavaPlugin {
	public int maxM;
	public int i;
	public int cD;
	public boolean lS;
	public boolean d = false;
	public List<World> wl;
	public List<String> eW;
	Config c = new Config(this);

	public final Logger l = Logger.getLogger("Minecraft");
	private final ESEntityListener eListener = new ESEntityListener(this);

	public void onEnable() { 
		this.c.configCheck();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.CREATURE_SPAWN, this.eListener, Event.Priority.Normal, this);
		PluginDescriptionFile pdfFile = getDescription();
		this.l.info(pdfFile.getName() + " BETA " + " version " + pdfFile.getVersion() + " is enabled! ^_^ ");
		ESEntityListener.init();
		if (this.d == true) {
			this.l.info("ES NOTICE: Running in debug mode!");
		}
	}

	public void onDisable() {  
		ESEntityListener.deinit();
		this.l.info("EntitySuppressor Disabled!");
	}

	public void processWL() {
		wl = Bukkit.getServer().getWorlds();
		if (eW != null) {
			for (String s : eW) {
				for (ListIterator<World> it = wl.listIterator(); it.hasNext(); ) { 
					if (it.next().getName().equalsIgnoreCase(s)) {
						it.remove();
						this.l.info("ES NOTICE: Not limiting world: " + s);
						if (wl == null) {
							this.l.info("ES NOTICE: Not using limiter as all worlds have been exempted.");
						}
					}
					//Else: Skip rope.
				}
			}
		}
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String cLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("es")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("count")) {
    	  
				if ((s instanceof Player)) {
					if (!((Player)s).hasPermission("esuppressor.count")) {
        	  
						s.sendMessage(ChatColor.DARK_RED + "Oh Noes! You don't have Permission to use that!");
						return true;
					}
					if ((args.length == 2) && (args[1].equalsIgnoreCase("all"))) {
						
						for (World w : Bukkit.getServer().getWorlds()) {
							
							HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
							int lEs = eSet.size() - w.getPlayers().size();

							s.sendMessage("Entity Count = " + lEs + " in " + w.getName());
							continue;
						}
						return true;
						
					} else if (args.length == 1) {
						
						World w = ((Player)s).getWorld();

						HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
						int lEs = eSet.size() - w.getPlayers().size();

						s.sendMessage("Entity Count = " + lEs + " in " + w.getName());
						return true;
					}
					return false;
				}

				for (World w : Bukkit.getServer().getWorlds()) {

					HashSet<LivingEntity> eSet = new HashSet<LivingEntity>(w.getLivingEntities());
					int lEs = eSet.size() - w.getPlayers().size();

					this.l.info("Entity Count = " + lEs + " in " + w.getName());
					continue;
				}
				return true;
			}
		}
		return false;
	}
}