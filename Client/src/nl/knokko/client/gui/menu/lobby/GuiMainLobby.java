package nl.knokko.client.gui.menu.lobby;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.client.Client;
import nl.knokko.client.ClientScreen;
import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.client.gui.button.ActionButton;
import nl.knokko.client.gui.button.GuiButton;
import nl.knokko.client.gui.button.LinkButton;
import nl.knokko.client.gui.texture.GuiTexture;
import nl.knokko.client.input.MouseInput;
import nl.knokko.client.render.GuiRenderer;
import nl.knokko.client.state.StateLobby;
import nl.knokko.client.texture.TextureCreator;

public class GuiMainLobby extends GuiLobbyBase {
	
	static final org.lwjgl.util.Color BACKGROUND = new org.lwjgl.util.Color(150, 80, 30);
	
	static final Color BUTTON_COLOR = new Color(120, 70, 10);
	static final Color BORDER_COLOR = new Color(50, 0, 0);
	static final Color TEXT_COLOR = new Color(200, 150, 0);

	public GuiMainLobby(StateLobby state) {
		super(state, 0, 3);
	}
	
	@Override
	public void initialise(){
		System.out.println("GuiMainLobby.initialise buttons is " + buttons);
		if(!buttons.isEmpty()) return;
		addButton(new ActionButton(new Vector2f(-0.7f, -0.8f), new Vector2f(0.2f, 0.1f), TextureCreator.createButtonTexture("Log Out", 0.2f, 0.1f, BUTTON_COLOR, BORDER_COLOR, TEXT_COLOR)){

			@Override
			public void click(float x, float y, int button) {
				ConnectionSpeaker.logout();
				MouseInput.setCooldown(ClientScreen.fps() / 2);
			}
		});
		addButton(new LinkButton(new Vector2f(-0.7f, 0.8f), new Vector2f(0.2f, 0.1f), state.getSiegeLobby(), state, TextureCreator.createButtonTexture("Siege", 0.2f, 0.1f, BUTTON_COLOR, BORDER_COLOR, TEXT_COLOR)));
		addButton(new LinkButton(new Vector2f(-0.7f, 0.5f), new Vector2f(0.2f, 0.1f), state.getWarLobby(), state, TextureCreator.createButtonTexture("War", 0.2f, 0.1f, BUTTON_COLOR, BORDER_COLOR, TEXT_COLOR)));
	}

	@Override
	public org.lwjgl.util.Color getBackgroundColor() {
		return BACKGROUND;
	}
	
	@Override
	public void render(){
		GuiRenderer.start();
		GuiRenderer.renderBackGround(getBackgroundColor());
		for(GuiTexture texture : getTextures())
			GuiRenderer.renderTextures(texture.getTranslation(), texture.getScale(), texture.getTextures());
		for(GuiButton button : getButtons())
			GuiRenderer.renderTextures(button.getTranslation(), button.getScale(), button.getTextures());
		Client.getChat().render(new Vector2f(0f, -1f), new Vector2f(1f, 1f), Color.BLACK, 19);
		GuiRenderer.stop();
	}
}
