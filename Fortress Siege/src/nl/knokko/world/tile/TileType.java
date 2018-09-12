package nl.knokko.world.tile;

public enum TileType {
	
	BARRIER("No units can pass this tile."),
	VOID("Only floating units can pass this tile."),
	AIR("Only floating and flying units can pass this tile."),
	WATER("Only land units can't pass this tile."),
	LAND("Only water units can't pass this tile.");
	
	public static final byte BIT_COUNT = 3;
	
	private final String description;
	
	TileType(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
}
