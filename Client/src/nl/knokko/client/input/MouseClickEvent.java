package nl.knokko.client.input;

public class MouseClickEvent {
	
	private final float x;
	private final float y;
	
	private final int button;
	private final boolean pressed;

	public MouseClickEvent(float x, float y, int button, boolean state) {
		this.x = x;
		this.y = y;
		this.button = button;
		this.pressed = state;
	}
	
	public boolean wasPressed(){
		return pressed;
	}
	
	public boolean wasReleased(){
		return !pressed;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public int getButton(){
		return button;
	}
}
