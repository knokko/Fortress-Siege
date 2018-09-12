package nl.knokko.entity.projectile;

import nl.knokko.entity.Entity;
import nl.knokko.entity.Faction;
import nl.knokko.world.World;
import nl.knokko.world.WorldObject;

public abstract class EntityProjectile extends Entity {

	public EntityProjectile(World world, Faction faction, int x, int y, int z) {
		super(world, faction, x, y, z);
	}
	
	@Override
	public WorldObject moveSimple(int dx, int dy, int dz){
		WorldObject hit = super.moveSimple(dx, dy, dz);
		if(hit != null)
			onImpact(hit);
		return hit;
	}
	
	public abstract void onImpact(WorldObject hitObject);
}
