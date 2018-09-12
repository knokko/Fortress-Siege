package nl.knokko.client.gui.menu.lobby;

import nl.knokko.client.gui.GuiBase;
import nl.knokko.client.state.StateLobby;

public abstract class GuiLobbyBase extends GuiBase {
	
	protected final StateLobby state;

	public GuiLobbyBase(StateLobby state, int textureCount, int buttonCount) {
		super(textureCount, buttonCount);
		this.state = state;
	}
}