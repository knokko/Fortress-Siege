package nl.knokko.client.state;

import nl.knokko.client.gui.Gui;
import nl.knokko.client.gui.menu.main.GuiCreateAccount;
import nl.knokko.client.gui.menu.main.GuiMainMenu;

public class StateMainMenu implements ClientState {
	
	private GuiMainMenu mainMenu;
	private GuiCreateAccount createAccount;
	
	private Gui gui;

	public StateMainMenu() {
		mainMenu = new GuiMainMenu(this);
		createAccount = new GuiCreateAccount(this);
	}

	@Override
	public void update() {}

	@Override
	public void render() {
		gui.render();
	}

	@Override
	public void open() {
		setGui(mainMenu);
	}

	@Override
	public void close() {}
	
	public void setGui(Gui newGui){
		gui = newGui;
		gui.initialise();
	}

	@Override
	public void click(float mouseX, float mouseY, int mouseButton) {
		gui.click(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyPressed(int keyCode, char character) {
		gui.keyTyped(keyCode, character);
	}

	@Override
	public void keyReleased(int keyCode) {}
	
	public Gui getGui(){
		return gui;
	}
	
	public GuiMainMenu getMainMenu(){
		return mainMenu;
	}
	
	public GuiCreateAccount getCreateMenu(){
		return createAccount;
	}
}
