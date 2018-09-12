package nl.knokko.client.gui.texture;

import java.awt.Color;

import nl.knokko.client.ClientScreen;
import nl.knokko.client.texture.ITexture;
import nl.knokko.client.texture.TextureCreator;

import org.lwjgl.util.vector.Vector2f;

public class GuiTextTexture implements GuiTexture {
	
	private ITexture[] texture;
	
	private Vector2f position;
	private Vector2f scale;

	public GuiTextTexture(Vector2f position, Vector2f scale, String text, Color color) {
		this.position = position;
		this.scale = scale;
		setText(text, color);
	}

	@Override
	public ITexture[] getTextures() {
		return texture;
	}

	@Override
	public Vector2f getTranslation() {
		return position;
	}

	@Override
	public Vector2f getScale() {
		return scale;
	}
	
	public void setText(String text, Color color){
		texture = new ITexture[]{TextureCreator.createTextTexture(text, (int) (scale.x * ClientScreen.getWidth()), (int) (scale.y * ClientScreen.getHeight()), color)};
	}
}
