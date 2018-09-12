package nl.knokko.client.gui.texture;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.client.texture.ITexture;

public interface GuiTexture {
	
	ITexture[] getTextures();
	
	Vector2f getTranslation();
	
	Vector2f getScale();
}
