package nl.knokko.client.gui.menu.main;

import java.awt.Color;
import java.io.PrintWriter;

import org.lwjgl.util.vector.Vector2f;

import nl.knokko.client.ClientScreen;
import nl.knokko.client.Client;
import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.client.connection.SocketConnection;
import nl.knokko.client.gui.GuiBase;
import nl.knokko.client.gui.button.ActionButton;
import nl.knokko.client.gui.button.LinkButton;
import nl.knokko.client.gui.button.TypeButton;
import nl.knokko.client.gui.texture.GuiTextTexture;
import nl.knokko.client.input.MouseInput;
import nl.knokko.client.state.StateMainMenu;
import nl.knokko.client.texture.TextureCreator;

public class GuiCreateAccount extends GuiBase {
	
	private StateMainMenu state;
	
	private boolean isConnecting;

	public GuiCreateAccount(StateMainMenu state) {
		super(3, 4);
		this.state = state;
	}
	
	private String getUserName(){
		return ((TypeButton) buttons.get(0)).getText();
	}
	
	private String getPassword(){
		return ((TypeButton) buttons.get(1)).getText();
	}

	@Override
	public org.lwjgl.util.Color getBackgroundColor() {
		return GuiMainMenu.BACK_GROUND;
	}
	
	@Override
	public void render(){
		if(Client.hasFailLoginReason()){
			setError(Client.getFailLoginReason());
			Client.clearFailLoginReason();
			isConnecting = false;
		}
		super.render();
	}
	
	@Override
	public void initialise(){
		if(!buttons.isEmpty()) return;
		addButton(new TypeButton(new Vector2f(0.3f, 0.6f), new Vector2f(0.4f, 0.1f), "", new Color(100, 100, 150), Color.BLACK, Color.BLACK));
		addButton(new TypeButton(new Vector2f(0.3f, 0.3f), new Vector2f(0.4f, 0.1f), "", new Color(100, 100, 150), Color.BLACK, Color.BLACK));
		addButton(new ActionButton(new Vector2f(0.3f, -0.4f), new Vector2f(0.25f, 0.1f), TextureCreator.createButtonTexture("register", 0.25f, 0.1f, Color.GREEN, Color.BLACK, Color.BLACK)){

			@Override
			public void click(float x, float y, int button) {
				try {
					if(!isConnecting){
						Client.clearFailLoginReason();
						//ConnectionManagerMain.verifyUserName(getUserName());
						//ConnectionManagerMain.verifyPassword(getPassword());
						ConnectionSpeaker.verifyUserName(getUserName());
						ConnectionSpeaker.verifyPassword(getPassword());
						Client.setConnection(new SocketConnection(new PrintWriter("main connection.log")));
						ConnectionSpeaker.open();
						ConnectionSpeaker.register(getUserName(), getPassword());
						//ConnectionManagerMain manager = new ConnectionManagerMain(new SocketConnection(new PrintWriter("main connection.log")));
						//manager.open();
						//manager.register(getUserName(), getPassword());
						//Client.setConnection(manager);
						MouseInput.setCooldown(ClientScreen.fps());
						isConnecting = true;
					}
				} catch (Exception e) {
					setError(e.getLocalizedMessage());
				}
			}
		});
		addButton(new LinkButton(new Vector2f(0.3f, -0.7f), new Vector2f(0.2f, 0.1f), state.getMainMenu(), state, TextureCreator.createButtonTexture("back", 0.2f, 0.1f, GuiMainMenu.BUTTON_COLOR, Color.BLACK, Color.BLACK)));
		addTexture(new GuiTextTexture(new Vector2f(-0.4f, 0.6f), new Vector2f(0.25f, 0.1f), "Username:", Color.BLACK));
		addTexture(new GuiTextTexture(new Vector2f(-0.4f, 0.3f), new Vector2f(0.25f, 0.1f), "Password:", Color.BLACK));
		addTexture(new GuiTextTexture(new Vector2f(0f, 0f), new Vector2f(0.8f, 0.1f), "", Color.RED));
	}
	
	private void setError(String error){
		((GuiTextTexture) textures.get(2)).setText(error, Color.RED);
	}
}
