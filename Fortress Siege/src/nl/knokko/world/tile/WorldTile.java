package nl.knokko.world.tile;

import nl.knokko.collission.Collider;
import nl.knokko.world.World;
import nl.knokko.world.WorldObject;

public class WorldTile implements WorldObject {
	
	private final Tile tile;
	
	private final int tileX;
	private final int tileZ;

	public WorldTile(Tile tile, int tileX, int tileZ) {
		this.tile = tile;
		this.tileX = tileX;
		this.tileZ = tileZ;
	}

	@Override
	public Collider getCollider() {
		return World.TILE_COLLIDER;
	}

	@Override
	public int getX() {
		return tileX * 32;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getZ() {
		return tileZ * 32;
	}
	
	public Tile getTile(){
		return tile;
	}
}
