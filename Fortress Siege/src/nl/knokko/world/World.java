package nl.knokko.world;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.collission.Collider;
import nl.knokko.collission.ColliderBox;
import nl.knokko.entity.Entity;
import nl.knokko.entity.EntityType;
import nl.knokko.entity.Faction;
import nl.knokko.world.tile.Tile;
import nl.knokko.world.tile.TileGrid;
import nl.knokko.world.tile.TileType;
import nl.knokko.world.tile.WorldTile;

public class World {
	
	public static final ColliderBox TILE_COLLIDER = new ColliderBox(0, 0, 0, 32, 0, 32);
	
	public static final int TICKS_PER_SECOND = 64;
	
	private final TileGrid tiles;
	
	private final List<FactionList> entities;
	private final List<Entity> entitiesToSpawn;
	
	private int nextID = Integer.MIN_VALUE + 1;
	
	public World(TileGrid tiles){
		this.tiles = tiles;
		entities = new ArrayList<FactionList>();
		entitiesToSpawn = new ArrayList<Entity>();
	}

	public World(int width, int height) {
		tiles = TileGrid.createInt(width, height);
		entities = new ArrayList<FactionList>();
		entitiesToSpawn = new ArrayList<Entity>();
	}
	
	public void update(){
		for(Entity entity : entitiesToSpawn){
			for(FactionList fl : entities){
				if(entity.getFaction() == fl.faction){
					fl.members.add(entity);
					break;
				}
			}
		}
		entitiesToSpawn.clear();
		for(FactionList fl : entities)
			for(Entity entity : fl.members)
				entity.update();
	}
	
	public void spawnEntity(Entity entity){
		entitiesToSpawn.add(entity);
		nextID++;
		entity.setIndividualID(nextID);
	}
	
	public TileGrid getTiles(){
		return tiles;
	}
	
	public Tile getTile(int tileX, int tileZ){
		return tiles.getTile(tileX, tileZ);
	}
	
	public TileType getTileType(int tileX, int tileZ){
		return tiles.getTileType(tileX, tileZ);
	}
	
	public List<Entity> getEnemies(Entity entity){
		for(FactionList fl : entities)
			if(fl.faction != entity.getFaction())
				return fl.members;
		throw new IllegalStateException("Failed to get enemies of entity " + entity + ": entities = " + entities);
	}
	
	public WorldObject canMoveTo(Entity entityToIgnore, Collider collider, EntityType type, int x, int y, int z){
		int minX = collider.getMinX(x) / 32;
		int minZ = collider.getMinZ(z) / 32;
		int maxX = collider.getMaxX(x) / 32;
		int maxZ = collider.getMaxZ(z) / 32;
		for(int tx = minX; tx <= maxX; tx++)
			for(int tz = minZ; tz <= maxZ; tz++)
				if(!type.canMoveTo(getTileType(tx, tz)) && collider.intersect(x, y, z, TILE_COLLIDER, tx * 32, 0, tz * 32))
					return new WorldTile(getTile(tx, tz), tx, tz);
		for(FactionList fl : entities)
			for(Entity entity : fl.members)
				if(entity != entityToIgnore && entity.getCollider().intersect(entity.getX(), entity.getY(), entity.getZ(), collider, x, y, z))
					return entity;
		return null;
	}
	
	public Entity getEntityByID(int entityID){
		for(FactionList fl : entities)
			for(Entity entity : fl.members)
				if(entity.getIndividualID() == entityID)
					return entity;
		return null;
	}
	
	public Entity getTargetByID(Entity searcher, int targetID){
		for(FactionList fl : entities)
			if(fl.faction != searcher.getFaction())
				for(Entity entity : fl.members)
					if(entity.getIndividualID() == targetID)
						return entity;
		return null;
	}
	
	private static class FactionList {
		
		private final Faction faction;
		private final List<Entity> members;
		
		private FactionList(Faction faction){
			this.faction = faction;
			this.members = new ArrayList<Entity>();
		}
	}
}
