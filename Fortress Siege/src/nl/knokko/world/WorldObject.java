package nl.knokko.world;

import nl.knokko.collission.Collider;

public interface WorldObject {
	
	Collider getCollider();
	
	int getX();
	
	int getY();
	
	int getZ();
}
