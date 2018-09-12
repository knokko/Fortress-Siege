package nl.knokko.client.gui;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.client.gui.button.GuiButton;
import nl.knokko.client.gui.texture.GuiTexture;
import nl.knokko.client.render.GuiRenderer;

public abstract class GuiBase implements Gui {
	
	protected List<GuiTexture> textures;
	protected List<GuiButton> buttons;

	public GuiBase(int textureCount, int buttonCount) {
		textures = new ArrayList<GuiTexture>(textureCount);
		buttons = new ArrayList<GuiButton>(buttonCount);
	}

	@Override
	public List<GuiTexture> getTextures() {
		return textures;
	}
	
	@Override
	public List<GuiButton> getButtons(){
		return buttons;
	}

	@Override
	public void click(float mouseX, float mouseY, int mouseButton) {
		for(GuiButton button : buttons)
			if(button.isHit(mouseX, mouseY))
				button.click(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(int code, char character){
		for(GuiButton button : buttons)
			button.keyTyped(code, character);
	}
	
	@Override
	public void render(){
		GuiRenderer.start();
		GuiRenderer.renderBackGround(getBackgroundColor());
		for(GuiTexture texture : getTextures())
			GuiRenderer.renderTextures(texture.getTranslation(), texture.getScale(), texture.getTextures());
		for(GuiButton button : getButtons())
			GuiRenderer.renderTextures(button.getTranslation(), button.getScale(), button.getTextures());
		GuiRenderer.stop();
	}
	
	public void initialise(){}
	
	protected void addButton(GuiButton button){
		buttons.add(button);
	}
	
	protected void addTexture(GuiTexture texture){
		textures.add(texture);
	}
}
