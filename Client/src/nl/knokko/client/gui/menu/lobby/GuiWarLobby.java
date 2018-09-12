package nl.knokko.client.gui.menu.lobby;

import nl.knokko.client.state.StateLobby;
import nl.knokko.mode.GameMode;

import java.awt.Color;

public class GuiWarLobby extends GuiGameModeLobby {
	
	static final org.lwjgl.util.Color BACKGROUND = new org.lwjgl.util.Color(250, 40, 0);
	
	static final Color BUTTON_COLOR = new Color(240, 10, 0);
	static final Color BORDER_COLOR = new Color(50, 0, 0);
	static final Color TEXT_COLOR = new Color(10, 0, 0);

	public GuiWarLobby(StateLobby state) {
		super(state, GameMode.WAR, BACKGROUND, BUTTON_COLOR, BORDER_COLOR, TEXT_COLOR);
	}

	@Override
	protected CreateGame newCreateGameMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JoinGame newJoinGameMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}