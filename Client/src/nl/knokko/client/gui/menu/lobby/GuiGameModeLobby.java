package nl.knokko.client.gui.menu.lobby;

import nl.knokko.client.gui.button.ActionButton;
import nl.knokko.client.gui.button.LinkButton;
import nl.knokko.client.state.StateLobby;
import nl.knokko.client.texture.TextureCreator;
import nl.knokko.mode.GameMode;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public abstract class GuiGameModeLobby extends GuiLobbyBase {
	
	protected org.lwjgl.util.Color backgroundColor;
	
	protected final GameMode mode;
	
	protected final Color buttonColor;
	protected final Color borderColor;
	protected final Color textColor;

	public GuiGameModeLobby(StateLobby state, GameMode mode, org.lwjgl.util.Color background, Color buttonColor, Color borderColor, Color textColor) {
		super(state, 0, 3);
		this.mode = mode;
		this.backgroundColor = background;
		this.buttonColor = buttonColor;
		this.borderColor = borderColor;
		this.textColor = textColor;
	}

	@Override
	public org.lwjgl.util.Color getBackgroundColor() {
		return backgroundColor;
	}
	
	@Override
	public void initialise(){
		if(!buttons.isEmpty()) return;
		addButton(new LinkButton(new Vector2f(-0.6f, -0.7f), new Vector2f(0.2f, 0.1f), state.getMainLobby(), state, TextureCreator.createButtonTexture("Back", 0.2f, 0.1f, buttonColor, borderColor, textColor)));
		addButton(new ActionButton(new Vector2f(0.4f, 0.1f), new Vector2f(0.3f, 0.1f), TextureCreator.createButtonTexture("Create Game", 0.3f, 0.1f, buttonColor, borderColor, textColor)){

			@Override
			public void click(float x, float y, int button) {
				state.setGui(newCreateGameMenu());
			}
		});
		addButton(new ActionButton(new Vector2f(0.4f, -0.2f), new Vector2f(0.3f, 0.1f), TextureCreator.createButtonTexture("Join Game", 0.3f, 0.1f, buttonColor, borderColor, textColor)){

			@Override
			public void click(float x, float y, int button) {
				state.setGui(newJoinGameMenu());
			}
		});
	}
	
	protected abstract CreateGame newCreateGameMenu();
	
	protected abstract JoinGame newJoinGameMenu();
	
	protected abstract class CreateGame extends GuiLobbyBase {

		public CreateGame(int buttonCount, int textureCount) {
			super(GuiGameModeLobby.this.state, buttonCount, textureCount);
		}
		
		@Override
		public void initialise(){
			addButton(new LinkButton(new Vector2f(-0.6f, -0.7f), new Vector2f(0.2f, 0.1f), GuiGameModeLobby.this, state, TextureCreator.createButtonTexture("Back", 0.2f, 0.1f, buttonColor, borderColor, textColor)));
		}
	}
	
	protected abstract class JoinGame extends GuiLobbyBase {
		
		public JoinGame(){
			super(GuiGameModeLobby.this.state, 0, 0);
			//TODO Choose array list sizes later
		}
		
		@Override
		public void initialise(){
			addButton(new LinkButton(new Vector2f(-0.6f, -0.7f), new Vector2f(0.2f, 0.1f), GuiGameModeLobby.this, state, TextureCreator.createButtonTexture("Back", 0.2f, 0.1f, buttonColor, borderColor, textColor)));
			//TODO set some filters first
		}
	}
}