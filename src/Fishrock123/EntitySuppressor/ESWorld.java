package Fishrock123.EntitySuppressor;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ESWorld {
	private String name;
	private short MonsterMaximum;
	private short SquidMaximum;
	private short AnimalMaximum;
	private short pChunkMonsterMaximum;
	private short pChunkSquidMaximum;
	private short pChunkAnimalMaximum;
	private boolean lSpawners = true;
	
	public ESWorld(World w) {
		name = w.getName().trim();
	}
	
	public void setMonsterMaximum(int i) {
		MonsterMaximum = (short)i;
	}
	public void setSquidMaximum(int i) {
		SquidMaximum = (short)i;
	}
	public void setAnimalMaximum(int i) {
		AnimalMaximum = (short)i;
	}
	public void setpChunkMonsterMaximum(int i) {
		pChunkMonsterMaximum = (short)i;
	}
	public void setpChunkSquidMaximum(int i) {
		pChunkSquidMaximum = (short)i;
	}
	public void setpChunkAnimalMaximum(int i) {
		pChunkAnimalMaximum = (short)i;
	}
	public void setlSpawners(boolean b) {
		lSpawners = b;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(name);
	}
	public String getName() {
		return name;
	}
	public int getMonsterMaximum() {
		return MonsterMaximum;
	}
	public int getSquidMaximum() {
		return SquidMaximum;
	}
	public int getAnimalMaximum() {
		return AnimalMaximum;
	}
	public int getpChunkMonsterMaximum() {
		return pChunkMonsterMaximum;
	}
	public int getpChunkSquidMaximum() {
		return pChunkSquidMaximum;
	}
	public int getpChunkAnimalMaximum() {
		return pChunkAnimalMaximum;
	}
	public boolean getlSpawners() {
		return lSpawners;
	}
	
	public boolean hasMonsterMaximum() {
		if (MonsterMaximum > 0) {
			return true;
		}
		return false;
	}
	public boolean hasSquidMaximum() {
		if (SquidMaximum > 0) {
			return true;
		}
		return false;
	}
	public boolean hasAnimalMaximum() {
		if (AnimalMaximum > 0) {
			return true;
		}
		return false;
	}
	public boolean haspChunkMonsterMaximum() {
		if (pChunkMonsterMaximum > 0) {
			return true;
		}
		return false;
	}
	public boolean haspChunkSquidMaximum() {
		if (pChunkSquidMaximum > 0) {
			return true;
		}
		return false;
	}
	public boolean haspChunkAnimalMaximum() {
		if (pChunkAnimalMaximum > 0) {
			return true;
		}
		return false;
	}
}
