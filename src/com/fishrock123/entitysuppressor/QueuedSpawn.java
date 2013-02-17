package com.fishrock123.entitysuppressor;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public final class QueuedSpawn {
	public final Location loc;
	public final EntityType type;

	public QueuedSpawn(Location loc, EntityType type) {
		this.loc = loc;
		this.type = type;
	}
}
