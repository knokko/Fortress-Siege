package nl.knokko.util.designer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TypingButton extends Button {
	
	protected boolean activated;

	public TypingButton(Color bodyColor, Color borderColor, Color textColor, Font font, String firstText, int minX, int minY, int maxX, int maxY) {
		super(bodyColor, borderColor, textColor, font, firstText, minX, minY, maxX, maxY);
	}

	public TypingButton(Color bodyColor, String firstText, int minX, int minY, int maxX, int maxY) {
		super(bodyColor, firstText, minX, minY, maxX, maxY);
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(activated){
			g.setColor(Color.YELLOW);
			g.drawRect(minX, minY, maxX - minX + 1, maxY - minY + 1);
		}
	}

	@Override
	public void click() {
		activated = !activated;
	}
	
	@Override
	public void click(int x, int y){
		if(x >= minX && x <= maxX && y >= minY && y <= maxY)
			click();
		else
			activated = false;
	}
	
	@Override
	public void type(char character){
		if(activated && (character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z' || character >= '0' && character <= '9' || character == ' '))
			text += character;
	}
	
	@Override
	public void press(int keycode){
		if(keycode == KeyEvent.VK_BACK_SPACE && text.length() > 0)
			text = text.substring(0, text.length() - 1);
		if(keycode == KeyEvent.VK_ESCAPE || keycode == KeyEvent.VK_ENTER)
			activated = false;
	}
}
