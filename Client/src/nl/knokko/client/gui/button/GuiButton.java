package nl.knokko.client.gui.button;

import nl.knokko.client.gui.texture.GuiTexture;

public interface GuiButton extends GuiTexture {
	
	boolean isHit(float x, float y);
	
	void click(float x, float y, int button);
	
	void keyTyped(int keyCode, char character);
}
