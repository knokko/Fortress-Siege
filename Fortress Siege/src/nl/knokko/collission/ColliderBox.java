package nl.knokko.collission;

public class ColliderBox extends Collider {
	
	final int minX;
	final int minY;
	final int minZ;
	
	final int maxX;
	final int maxY;
	final int maxZ;

	public ColliderBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	@Override
	public boolean intersect(int x, int y, int z, Collider other, int ox, int oy, int oz) {
		if(other instanceof ColliderBox){
			ColliderBox b = (ColliderBox) other;
			return Collider.intersectBoxWithBox(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z, b.minX + ox, b.minY + oy, b.minZ + oz, b.maxX + x, b.maxY + y, b.maxZ + z);
		}
		if(other instanceof ColliderCilinder){
			ColliderCilinder c = (ColliderCilinder) other;
			return Collider.intersectBoxWithCilinder(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z, c.minY + oy, c.maxY + oy, c.x + ox, c.z + oz, c.r);
		}
		throw new IllegalArgumentException("Unknown collider class: " + other.getClass());
	}

	@Override
	public int getMinX(int x) {
		return x + minX;
	}

	@Override
	public int getMinY(int y) {
		return y + minY;
	}

	@Override
	public int getMinZ(int z) {
		return z + minZ;
	}

	@Override
	public int getMaxX(int x) {
		return x + maxX;
	}

	@Override
	public int getMaxY(int y) {
		return y + maxY;
	}

	@Override
	public int getMaxZ(int z) {
		return z + maxZ;
	}

}
