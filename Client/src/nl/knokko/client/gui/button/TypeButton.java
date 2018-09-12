package nl.knokko.client.gui.button;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import nl.knokko.client.ClientScreen;
import nl.knokko.client.texture.ITexture;
import nl.knokko.client.texture.TextureCreator;

public class TypeButton extends ActionButton {
	
	protected String text;
	
	protected Color buttonColor;
	protected Color borderColor;
	protected Color textColor;
	
	protected boolean enabled;

	public TypeButton(Vector2f translation, Vector2f scale, String text, Color buttonColor, Color borderColor, Color textColor) {
		super(translation, scale, TextureCreator.createButtonTexture(text, (int) (scale.x * ClientScreen.getWidth()), (int) (scale.y * ClientScreen.getHeight()), buttonColor, borderColor, textColor));
		this.text = text;
		this.buttonColor = buttonColor;
		this.borderColor = borderColor;
		this.textColor = textColor;
	}

	@Override
	public void click(float x, float y, int button) {
		enabled = true;
		refreshTexture();
	}
	
	@Override
	public boolean isHit(float x, float y){
		if(!super.isHit(x, y)){
			enabled = false;
			refreshTexture();
			return false;
		}
		return true;
	}
	
	@Override
	public void keyTyped(int code, char character){
		if(enabled && (int) character != 0){
			if(code == Keyboard.KEY_BACK || code == Keyboard.KEY_DELETE){
				if(text.length() > 0)
					text = text.substring(0, text.length() - 1);
			}
			else if(code == Keyboard.KEY_RETURN || code == Keyboard.KEY_ESCAPE)
				enabled = false;
			else
				text += character;
			refreshTexture();
		}
	}
	
	public String getText(){
		return text;
	}
	
	protected void refreshTexture(){
		textures = new ITexture[]{TextureCreator.createButtonTexture(text, (int) (size.x * ClientScreen.getWidth()), (int) (size.y * ClientScreen.getHeight()), buttonColor, enabled ? Color.YELLOW : borderColor, textColor)};
	}
}
