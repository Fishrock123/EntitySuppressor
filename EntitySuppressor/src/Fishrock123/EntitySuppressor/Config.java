package Fishrock123.EntitySuppressor;

import java.io.File;
import java.util.List;

import org.bukkit.util.config.Configuration;

public class Config {
	private static EntitySuppressor p;
	public String dir = "plugins" + File.separator + "EntitySuppressor";
	File f = new File(this.dir + File.separator + "config.yml");

	public Config(EntitySuppressor instance) {
		p = instance;
	}

  	public void configCheck() {
  		new File(this.dir).mkdir();

  		if (!this.f.exists()) {
  			try {
  				this.f.createNewFile();
  				addDefaults();
  			}
  			catch (Exception exC) {
  				exC.printStackTrace();
  			}
    	}
  		else {
  			loadkeys();
  		}
  	}

  	private void write(String root, Object o) {
  		Configuration config = load();
  		config.setProperty(root, o);
  		config.save();
  	}

  	private Boolean readBoolean(String root) {
  		Configuration config = load();
  		return Boolean.valueOf(config.getBoolean(root, true));
  	}

  	private int readInt(String root) {
  		Configuration config = load();
  		return config.getInt(root, 0);
  	}
  	
    private List<String> readStringList(String root){
        Configuration config = load();
        return config.getStringList(root, null);
    }
  	
  	private Configuration load() {
  		try {
  			Configuration config = new Configuration(this.f);
  			config.load();
  			return config;
  		}
  		catch (Exception exL) {
  			exL.printStackTrace();
  		}
  		return null;
  	}

  	private void addDefaults() {
  		p.l.info("ES Generating Config File... :D");
  		write("ESConfigVersion; DO NOT CHANGE!", 0.1);
  		write("maxMonsters", 64);
   	 	write("checkDifference", 8);
   	 	write("limitSquid", true);
   	 	write("SpawnFlagsCheckInterval", 200);
   	 	String[] dsl = {"null_example_world", "null_example_world_nether"};
   	 	write("nonLimitedWorlds", dsl);
   	 	write("debug", false);
   	 	loadkeys();
  	}

  	private void loadkeys() {
  		p.l.info("ES Loading Config File... :D");
  		p.maxM = readInt("maxMonsters");
  		p.cD = readInt("checkDifference");
  		p.lS = readBoolean("limitSquid");
  		p.i = readInt("SpawnFlagsCheckInterval");
  		p.eW = readStringList("nonLimitedWorlds");
  		p.d = readBoolean("debug");
  		p.processWL();
  	}
}