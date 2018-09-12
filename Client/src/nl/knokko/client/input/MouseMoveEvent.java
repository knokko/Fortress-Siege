package nl.knokko.client.input;

public class MouseMoveEvent {
	
	private final int ax;
	private final int ay;
	
	private final int dx;
	private final int dy;

	public MouseMoveEvent(int ax, int ay, int dx, int dy) {
		this.ax = ax;
		this.ay = ay;
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getToX(){
		return ax;
	}
	
	public int getToY(){
		return ay;
	}
	
	public int getFromX(){
		return ax - dx;
	}
	
	public int getFromY(){
		return ay - dy;
	}
	
	public int getDeltaX(){
		return dx;
	}
	
	public int getDeltaY(){
		return dy;
	}
}
