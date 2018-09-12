package nl.knokko.client.gui;

import java.util.List;

import nl.knokko.client.gui.button.GuiButton;
import nl.knokko.client.gui.texture.GuiTexture;

import org.lwjgl.util.Color;

public interface Gui {
	
	List<GuiButton> getButtons();
	
	List<GuiTexture> getTextures();
	
	Color getBackgroundColor();
	
	void click(float mouseX, float mouseY, int button);
	
	void keyTyped(int keyCode, char character);
	
	void render();
	
	void initialise();
}
