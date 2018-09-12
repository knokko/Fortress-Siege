package nl.knokko.entity.fighter;

import nl.knokko.collission.Collider;
import nl.knokko.collission.ColliderCilinder;
import nl.knokko.entity.Entity;
import nl.knokko.entity.EntityType;
import nl.knokko.entity.Faction;
import nl.knokko.util.bits.BitInput;
import nl.knokko.world.World;

public class EntityUfo extends EntityFighter {
	
	public static Entity load(World world, BitInput buffer){
		int id = buffer.readInt();
		Faction faction = Faction.fromID(buffer.readBoolean());
		char x = buffer.readChar();
		int y = buffer.readByte() + 128;
		char z = buffer.readChar();
		int targetID = buffer.readInt();
		return new EntityUfo(world, faction, x, y, z).setTarget(targetID).setIndividualID(id);
	}

	public EntityUfo(World world, Faction faction, int x, int y, int z) {
		super(world, faction, x, y, z);
	}

	@Override
	public Collider createCollider() {
		return new ColliderCilinder(0, 10, 0, 0, 16);
	}

	@Override
	public EntityType getType() {
		return EntityType.FLOATING;
	}

	@Override
	public byte getTypeID() {
		return -128;
	}

	@Override
	public byte attackCooldown() {
		return 127;
	}

	@Override
	public int attackRange() {
		return 100;
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte stepCooldown() {
		return 5;
	}

	@Override
	public byte stepSize() {
		return 1;
	}
}
