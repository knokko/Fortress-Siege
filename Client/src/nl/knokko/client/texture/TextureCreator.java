package nl.knokko.client.texture;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import nl.knokko.client.ClientScreen;

public final class TextureCreator {
	
	private static final Font DEFAULT_FONT = new Font("TimesRoman", 0, 100);
	
	public static ITexture createButtonTexture(String text, int width, int height, Color buttonColor, Color borderColor, Color textColor){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(buttonColor);
		g.fillRect(1, 1, width - 2, height - 2);
		g.setColor(borderColor);
		g.drawRect(0, 0, width - 1, height - 1);
		if(!text.isEmpty()){
			Rectangle2D bounds = DEFAULT_FONT.getStringBounds(text, g.getFontRenderContext());
			double factor = Math.min(width / bounds.getWidth(), height / bounds.getHeight());
			g.setFont(new Font("TimesRoman", 0, (int) (100 * factor)));
			g.setColor(textColor);
			g.drawString(text, 0, height / 2 + height / 12);
			g.dispose();
		}
		return TextureLoader.loadBufferedImage(image, false);
	}
	
	public static ITexture createButtonTexture(String text, float width, float height, Color buttonColor, Color borderColor, Color textColor){
		return createButtonTexture(text, (int) (ClientScreen.getWidth() * width), (int) (ClientScreen.getHeight() * height), buttonColor, borderColor, textColor);
	}
	
	public static ITexture createTextTexture(String text, int width, int height, Color color){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		Rectangle2D bounds = DEFAULT_FONT.getStringBounds(text, g.getFontRenderContext());
		double factor = Math.min(width / bounds.getWidth(), height / bounds.getHeight());
		g.setFont(new Font("TimesRoman", 0, (int) (100 * factor)));
		g.setColor(color);
		g.drawString(text, 0, height / 2 + height / 12);
		g.dispose();
		return TextureLoader.loadBufferedImage(image, true);
	}
	
	public static ITexture createChatTexture(String text, int width, int height, Color color){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(color);
		g.setFont(new Font("TimesRoman", 0, 25));
		g.drawString(text, 0, height / 2 + height / 4);
		g.dispose();
		return TextureLoader.loadBufferedImage(image, true);
	}
}
