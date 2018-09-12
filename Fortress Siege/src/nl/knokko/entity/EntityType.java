package nl.knokko.entity;

import nl.knokko.world.tile.TileType;

public enum EntityType {
	
	FLOATING,
	FLYING,
	SWIMMING,
	WALKING;
	
	public boolean canMoveTo(TileType type){
		if(type == TileType.BARRIER)
			return false;
		if(type == TileType.VOID)
			return this == FLOATING;
		if(type == TileType.AIR)
			return this == FLOATING || this == FLYING;
		if(type == TileType.WATER)
			return this == FLOATING || this == FLYING || this == SWIMMING;
		if(type == TileType.LAND)
			return this == FLOATING || this == FLYING || this == WALKING;
		if(type == null)
			throw new NullPointerException("Type is null!");
		throw new Error("Unknown TileType: " + type);
	}
}
