package nl.knokko.client.state;

import nl.knokko.client.Client;
import nl.knokko.client.gui.Gui;
import nl.knokko.client.gui.menu.lobby.*;

public class StateLobby implements ClientState {
	
	private final GuiMainLobby mainLobby;
	private final GuiWarLobby warLobby;
	private final GuiSiegeLobby siegeLobby;
	
	private Gui gui;
	
	public StateLobby(){
		mainLobby = new GuiMainLobby(this);
		warLobby = new GuiWarLobby(this);
		siegeLobby = new GuiSiegeLobby(this);
	}

	@Override
	public void update() {
		if(Client.getConnection() == null)
			Client.setState(new StateMainMenu());
	}

	@Override
	public void render() {
		gui.render();
	}

	@Override
	public void open() {
		setGui(mainLobby);
	}

	@Override
	public void close() {}

	@Override
	public void click(float mouseX, float mouseY, int button) {
		gui.click(mouseX, mouseY, button);
	}

	@Override
	public void keyPressed(int keyCode, char character) {
		gui.keyTyped(keyCode, character);
	}

	@Override
	public void keyReleased(int keyCode) {}

	@Override
	public void setGui(Gui gui) {
		this.gui = gui;
		gui.initialise();
	}
	
	public Gui getGui(){
		return gui;
	}
	
	public GuiMainLobby getMainLobby(){
		return mainLobby;
	}
	
	public GuiWarLobby getWarLobby(){
		return warLobby;
	}
	
	public GuiSiegeLobby getSiegeLobby(){
		return siegeLobby;
	}
}
