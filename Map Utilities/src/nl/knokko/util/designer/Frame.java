package nl.knokko.util.designer;

import java.awt.Graphics;

public interface Frame {
	
	void click(int x, int y);
	
	void paint(Graphics g);
	
	void type(char character);
	
	void press(int key);
	
	void release(int key);
	
	void scroll(int pixels);
}