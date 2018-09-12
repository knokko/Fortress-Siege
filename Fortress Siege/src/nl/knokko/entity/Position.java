package nl.knokko.entity;

public interface Position {
	
	int getX();
	
	int getY();
	
	int getZ();
	
	int getTileX();
	
	int getTileZ();
	
	void move(float dx, float dy, float dz);
	
	void teleport(float x, float y, float z);
	
	double getDistance(Position other);
	
	double getDistance(float x, float y, float z);
	
	public static class IntPosition implements Position {
	
		private int x;
		private int y;
		private int z;

		public IntPosition(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	
		public int getX(){
			return x;
		}
	
		public int getY(){
			return y;
		}
	
		public int getZ(){
			return z;
		}
	
		public int getTileX(){
			return x / 32;
		}
	
		public int getTileZ(){
			return z / 32;
		}
	
		public void move(float dx, float dy, float dz){
			x += (int) dx;
			y += (int) dy;
			z += (int) dz;
		}
	
		public void teleport(float x, float y, float z){
			this.x = (int) x;
			this.y = (int) y;
			this.z = (int) z;
		}
	
		public double getDistance(float targetX, float targetY, float targetZ){
			targetX -= x;
			targetY -= y;
			targetZ -= z;
			return Math.sqrt((targetX * targetX) + (targetY * targetY) + (targetZ * targetZ));
		}
	
		public double getDistance(Position to){
			return getDistance(to.getX(), to.getY(), to.getZ());
		}
	}
	
	public static class FloatPosition implements Position {
		
		private float x;
		private float y;
		private float z;
		
		public FloatPosition(float x, float y, float z){
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public int getX() {
			return (int) x;
		}

		@Override
		public int getY() {
			return (int) y;
		}

		@Override
		public int getZ() {
			return (int) z;
		}

		@Override
		public int getTileX() {
			return (int) (x / 32);
		}

		@Override
		public int getTileZ() {
			return (int) (z / 32);
		}

		@Override
		public void move(float dx, float dy, float dz) {
			x += dx;
			y += dy;
			z += dz;
		}

		@Override
		public void teleport(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public double getDistance(float targetX, float targetY, float targetZ){
			targetX -= x;
			targetY -= y;
			targetZ -= z;
			return Math.sqrt((targetX * targetX) + (targetY * targetY) + (targetZ * targetZ));
		}
	
		public double getDistance(Position to){
			return getDistance(to.getX(), to.getY(), to.getZ());
		}
	}
}
