package nl.knokko.client.state;

import nl.knokko.client.gui.Gui;

public interface ClientState {
	
	void update();
	
	void render();
	
	void open();
	
	void close();
	
	void click(float mouseX, float mouseY, int mouseButton);
	
	void keyPressed(int keyCode, char character);
	
	void keyReleased(int keyCode);
	
	void setGui(Gui gui);
}
