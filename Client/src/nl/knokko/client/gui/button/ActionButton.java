package nl.knokko.client.gui.button;

import nl.knokko.client.texture.ITexture;

import org.lwjgl.util.vector.Vector2f;

public abstract class ActionButton implements GuiButton {
	
	protected ITexture[] textures;
	
	protected Vector2f position;
	protected Vector2f size;

	public ActionButton(Vector2f translation, Vector2f scale, ITexture... textures) {
		this.textures = textures;
		position = translation;
		size = scale;
	}

	@Override
	public ITexture[] getTextures() {
		return textures;
	}

	@Override
	public Vector2f getTranslation() {
		return position;
	}

	@Override
	public Vector2f getScale() {
		return size;
	}

	@Override
	public boolean isHit(float x, float y) {
		return x >= position.x - size.x && x <= position.x + size.x && y >= position.y - size.y && y <= position.y + size.y;
	}
	
	@Override
	public void keyTyped(int code, char c){}
}
