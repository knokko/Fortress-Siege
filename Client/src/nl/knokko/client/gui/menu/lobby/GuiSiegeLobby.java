package nl.knokko.client.gui.menu.lobby;

import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.client.gui.button.ActionButton;
import nl.knokko.client.state.StateLobby;
import nl.knokko.client.texture.TextureCreator;
import nl.knokko.mode.GameMode;
import nl.knokko.mode.SiegeSettings;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class GuiSiegeLobby extends GuiGameModeLobby {
	
	static final org.lwjgl.util.Color BACKGROUND = new org.lwjgl.util.Color(250, 200, 0);
	
	static final Color BUTTON_COLOR = new Color(120, 100, 0);
	static final Color BORDER_COLOR = new Color(40, 30, 0);
	static final Color TEXT_COLOR = new Color(10, 5, 0);

	public GuiSiegeLobby(StateLobby state) {
		super(state, GameMode.SIEGE, BACKGROUND, BUTTON_COLOR, BORDER_COLOR, TEXT_COLOR);
	}

	@Override
	protected CreateGame newCreateGameMenu() {
		return new Create();
	}

	@Override
	protected JoinGame newJoinGameMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class Create extends CreateGame {
		
		protected Settings settings;
		
		protected byte openState;
		
		protected Create(){
			super(0, 0);
		}

		@Override
		public org.lwjgl.util.Color getBackgroundColor() {
			return BACKGROUND;
		}
		
		@Override
		public void initialise(){
			settings = new Settings();
			addButton(new ActionButton(new Vector2f(0f, -0.5f), new Vector2f(0.3f, 0.15f), TextureCreator.createButtonTexture("Open", 0.3f, 0.15f, new Color(0, 200, 50), new Color(0, 50, 200), textColor)){

				@Override
				public void click(float x, float y, int button) {
					ConnectionSpeaker.openSiegeLobby(settings);
					openState = 1;
					//TODO make sure the server actually opens a siege lobby
				}
			});
		}
		
		public void opened(){
			openState = 2;
		}
		
		protected class Settings extends SiegeSettings {

			@Override
			public boolean canChange() {
				return true;
			}

			@Override
			protected void onChange() {
				if(openState > 0){
					//TODO send changes to the server
				}
			}
		}
	}
}