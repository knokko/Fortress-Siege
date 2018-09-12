package nl.knokko.entity;

import java.util.List;

import nl.knokko.collission.Collider;
import nl.knokko.entity.fighter.EntityUfo;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;
import nl.knokko.world.World;
import nl.knokko.world.WorldObject;

public abstract class Entity implements WorldObject {
	
	private static final Class<?>[] ID_MAP = {EntityUfo.class};
	
	protected final World world;
	protected final Faction faction;
	
	protected final Collider collider;
	protected final Position position;
	
	private int id = Integer.MIN_VALUE;
	
	public static Entity load(World world, BitInput buffer){
		byte id = buffer.readByte();
		try {
			return (Entity) ID_MAP[id + 128].getMethod("load", World.class, BitInput.class).invoke(null, world, buffer);
		} catch (Exception ex){
			throw new IllegalArgumentException("Failed to load entity with id " + id, ex);
		}
	}

	public Entity(World world, Faction faction, int x, int y, int z) {
		this.world = world;
		this.faction = faction;
		this.collider = createCollider();
		this.position = createPosition(x, y, z);
	}
	
	public Entity setIndividualID(int id){
		this.id = id;
		return this;
	}
	
	public int getIndividualID(){
		return id;
	}
	
	@Override
	public int getX(){
		return position.getX();
	}
	
	@Override
	public int getY(){
		return position.getY();
	}
	
	@Override
	public int getZ(){
		return position.getZ();
	}
	
	@Override
	public Collider getCollider(){
		return collider;
	}
	
	public Position getPosition(){
		return position;
	}
	
	public void update(){
		if(id == Integer.MIN_VALUE)
			throw new IllegalStateException("Entity " + toString() + " does not have an individual ID!");
	}
	
	public Faction getFaction(){
		return faction;
	}
	
	public World getWorld(){
		return world;
	}
	
	public WorldObject moveSimple(int dx, int dy, int dz){
		WorldObject wo = world.canMoveTo(this, getCollider(), getType(), getX() + dx, getY() + dy, getZ() + dz);
		if(wo == null)
			position.move(dx, dy, dz);
		return wo;
	}
	
	public void save(BitOutput buffer){
		buffer.addByte(getTypeID());
		buffer.addInt(id);
		buffer.addBoolean(faction.getID());
		buffer.addChar((char) position.getX());
		if(canLeaveGround())
			buffer.addByte((byte) (position.getY() - 128));
		buffer.addChar((char) position.getZ());
	}
	
	public boolean canLeaveGround(){
		return getType() == EntityType.FLOATING || getType() == EntityType.FLYING;
	}
	
	public Entity findNearestEnemy(){
		List<Entity> enemies = world.getEnemies(this);
		double distance = 0;
		Entity nearest = null;
		for(Entity enemy : enemies){
			double dis = position.getDistance(enemy.position);
			if(nearest == null ||  dis < distance){
				distance = dis;
				nearest = enemy;
			}
		}
		return nearest;
	}
	
	public Position createPosition(int x, int y, int z){
		return new Position.IntPosition(x, y, z);
	}
	
	public abstract Collider createCollider();
	
	public abstract EntityType getType();
	
	public abstract byte getTypeID();
}
