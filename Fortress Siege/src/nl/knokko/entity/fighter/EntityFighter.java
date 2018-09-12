package nl.knokko.entity.fighter;

import nl.knokko.entity.Entity;
import nl.knokko.entity.Faction;
import nl.knokko.util.bits.BitOutput;
import nl.knokko.world.World;

public abstract class EntityFighter extends Entity {
	
	private Entity target;
	private int targetID = Integer.MIN_VALUE;
	
	private byte attackCooldown = -128;
	private byte stepCooldown = -128;

	public EntityFighter(World world, Faction faction, int x, int y, int z) {
		super(world, faction, x, y, z);
	}
	
	@Override
	public void save(BitOutput buffer){
		super.save(buffer);
		buffer.addInt(targetID);
	}
	
	@Override
	public void update(){
		super.update();
		if(attackCooldown > -128)
			attackCooldown--;
		else {
			Entity target = getTarget();
			if(target != null){
				if(position.getDistance(target.getPosition()) <= attackRange()){
					attack();
					attackCooldown = attackCooldown();
				}
				else
					moveTowardsTarget(target);
			}
		}
	}
	
	public EntityFighter setTarget(int targetID){
		this.targetID = targetID;
		return this;
	}
	
	public Entity getTarget(){
		if(target == null && targetID != Integer.MIN_VALUE)
			target = world.getTargetByID(this, targetID);
		if(target == null){
			target = findNearestEnemy();
			targetID = target.getIndividualID();
		}
		return target;
	}
	
	protected void moveTowardsTarget(Entity target){
		if(stepCooldown > -128)
			stepCooldown--;
		else {
			for(byte b = 0; b < stepSize(); b++){
				if(getX() > target.getX())
					moveSimple(-1, 0, 0);
				else if(getX() < target.getX())
					moveSimple(1, 0, 0);
				if(getY() > target.getY())
					moveSimple(0, -1, 0);
				else if(getY() < target.getY())
					moveSimple(0, 1, 0);
				if(getZ() > target.getZ())
					moveSimple(0, 0, -1);
				else if(getZ() < target.getZ())
					moveSimple(0, 0, 1);
			}
			stepCooldown = stepCooldown();
		}
	}
	
	protected abstract byte attackCooldown();
	
	protected abstract int attackRange();
	
	protected abstract byte stepCooldown();
	
	protected abstract byte stepSize();
	
	protected abstract void attack();
}
