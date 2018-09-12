package nl.knokko.util.designer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public abstract class Button {
	
	public static final Font DEFAULT_FONT = new Font("TimesRoman", 0, 35);
	public static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
	public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
	
	protected Color body;
	protected Color border;
	protected Color textColor;
	
	protected String text;
	protected Font font;
	
	protected int minX;
	protected int maxX;
	protected int minY;
	protected int maxY;

	public Button(Color bodyColor, Color borderColor, Color textColor, Font font, String text, int minX, int minY, int maxX, int maxY) {
		body = bodyColor;
		border = borderColor;
		this.textColor = textColor;
		this.font = font;
		this.text = text;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public Button(Color bodyColor, String text, int minX, int minY, int maxX, int maxY){
		this(bodyColor, DEFAULT_BORDER_COLOR, DEFAULT_TEXT_COLOR, DEFAULT_FONT, text, minX, minY, maxX, maxY);
	}
	
	public void click(int x, int y){
		if(x >= minX && x <= maxX && y >= minY && y <= maxY)
			click();
	}
	
	public void paint(Graphics g){
		g.setColor(body);
		g.fillRect(minX, minY, maxX - minX + 1, maxY - minY + 1);
		g.setColor(border);
		g.drawRect(minX, minY, maxX - minX + 1, maxY - minY + 1);
		g.setFont(font);
		g.setColor(textColor);
		g.drawString(text, minX + (maxX - minX) / 10, maxY - (maxY - minY) / 5);
	}
	
	public abstract void click();
	
	public void type(char character){}
	
	public void press(int keycode){}

	public String getText() {
		return text;
	}
}
