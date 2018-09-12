package nl.knokko.collission;

public class ColliderCilinder extends Collider {
	
	final int minY;
	final int maxY;
	
	final int x;
	final int z;
	
	final int r;

	public ColliderCilinder(int minY, int maxY, int x, int z, int radius) {
		this.minY = minY;
		this.maxY = maxY;
		this.x = x;
		this.z = z;
		this.r = radius;
	}

	@Override
	public boolean intersect(int wx, int y, int wz, Collider other, int ox, int oy, int oz) {
		if(other instanceof ColliderCilinder){
			ColliderCilinder c = (ColliderCilinder) other;
			return Collider.intersectCilinderWithCilinder(minY + y, maxY + y, x + wx, z + wz, r, c.minY + oy, c.maxY + oy, c.x + ox, c.z + oz, c.r);
		}
		if(other instanceof ColliderBox){
			ColliderBox b = (ColliderBox) other;
			return Collider.intersectBoxWithCilinder(b.minX + ox, b.minY + oy, b.minZ + oz, b.maxX + ox, b.maxY + oy, b.maxZ + oz, minY + y, maxY + y, x + wx, z + wz, r);
		}
		throw new IllegalArgumentException("Unknown collider class: " + other.getClass());
	}

	@Override
	public int getMinX(int x) {
		return x + this.x - r;
	}

	@Override
	public int getMinY(int y) {
		return y + minY;
	}

	@Override
	public int getMinZ(int z) {
		return z + this.z - r;
	}

	@Override
	public int getMaxX(int x) {
		return x + this.x + r;
	}

	@Override
	public int getMaxY(int y) {
		return y + maxY;
	}

	@Override
	public int getMaxZ(int z) {
		return z + this.z + r;
	}

}
