package Fishrock123.EntitySuppressor;

import java.io.File;
import org.bukkit.util.config.Configuration;

public class Config {
	private static EntitySuppressor p;
	public String directory = "plugins" + File.separator + "EntitySuppressor";
	File f = new File(this.directory + File.separator + "config.yml");

	public Config(EntitySuppressor instance) {
		p = instance;
	}

  	public void configCheck() {
  		new File(this.directory).mkdir();

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

  	private void write(String root, Object x) {
  		Configuration config = load();
  		config.setProperty(root, x);
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
  		write("ESConfigVersion; DO NOT CHANGE!", Double.valueOf(0.1D));
  		write("maxMonsters", Integer.valueOf(64));
   	 	write("checkDifference", Integer.valueOf(8));
   	 	write("limitSquid", Boolean.valueOf(true));
   	 	write("SpawnFlagsCheckInterval", Integer.valueOf(200));
   	 	write("debug", Boolean.valueOf(false));
   	 	loadkeys();
  	}

  	private void loadkeys() {
  		p.l.info("ES Loading Config File... :D");
  		p.maxM = readInt("maxMonsters");
  		p.cD = readInt("checkDifference");
  		p.lS = readBoolean("limitSquid").booleanValue();
  		p.i = readInt("SpawnFlagsCheckInterval");
  		p.d = readBoolean("debug").booleanValue();
  	}
}