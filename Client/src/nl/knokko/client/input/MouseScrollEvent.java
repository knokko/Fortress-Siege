package nl.knokko.client.input;

public class MouseScrollEvent {
	
	private int deltaScroll;

	public MouseScrollEvent(int deltaScroll) {
		this.deltaScroll = deltaScroll;
	}
	
	public int getDeltaScroll(){
		return deltaScroll;
	}
}
