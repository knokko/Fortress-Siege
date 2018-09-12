package nl.knokko.collission;

import nl.knokko.entity.Position;

public abstract class Collider {
	
	public static boolean intersectBoxWithBox(int minX1, int minY1, int minZ1, int maxX1, int maxY1, int maxZ1, int minX2, int minY2, int minZ2, int maxX2, int maxY2, int maxZ2){
		return minX1 <= maxX2 && minY1 <= maxY2 && minZ1 <= maxZ2 && minX2 <= maxX1 && minY2 <= maxY1 && minZ2 <= maxZ1;
	}
	
	public static boolean intersectCilinderWithCilinder(int minY1, int maxY1, int x1, int z1, int r1, int minY2, int maxY2, int x2, int z2, int r2){
		return minY1 <= maxY2 && minY2 <= maxY1 && Math.hypot(x2 - x1, z2 - z1) <= r1 + r2;
	}
	
	public static boolean intersectBoxWithCilinder(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int minCY, int maxCY, int x, int z, int radius){
		if(minY <= maxCY && minCY <= maxY){
			int cx = x;
			if(cx > maxX)
				cx = maxX;
			if(cx < minX)
				cx = minX;
			int cz = z;
			if(cz > maxZ)
				cz = maxZ;
			if(cz < minZ)
				cz = minZ;
			return Math.hypot(cx - x, cz - z) <= radius;
		}
		return false;
	}
	
	public abstract boolean intersect(int ownX, int ownY, int ownZ, Collider other, int otherX, int otherY, int otherZ);
	
	public boolean intersect(Position position, Collider other, Position otherPosition){
		return intersect(position.getX(), position.getY(), position.getZ(), other, otherPosition.getX(), otherPosition.getY(), otherPosition.getZ());
	}
	
	public abstract int getMinX(int x);
	public abstract int getMinY(int y);
	public abstract int getMinZ(int z);
	public abstract int getMaxX(int x);
	public abstract int getMaxY(int y);
	public abstract int getMaxZ(int z);
}
