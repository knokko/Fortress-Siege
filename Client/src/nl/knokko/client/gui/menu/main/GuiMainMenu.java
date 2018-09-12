package nl.knokko.client.gui.menu.main;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Arrays;

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
import nl.knokko.client.texture.ITexture;
import nl.knokko.client.texture.TextureCreator;

import org.lwjgl.util.vector.Vector2f;

public class GuiMainMenu extends GuiBase {
	
	static final org.lwjgl.util.Color BACK_GROUND = new org.lwjgl.util.Color(30, 30, 150);
	
	static final Color BUTTON_COLOR = new Color(30, 100, 200);
	
	private StateMainMenu state;
	
	private boolean isConnecting;

	public GuiMainMenu(StateMainMenu state) {
		super(3, 5);
		this.state = state;
	}
	
	private String getUserName(){
		return ((TypeButton) buttons.get(3)).getText();
	}
	
	private String getPassword(){
		return ((TypeButton) buttons.get(4)).getText();
	}
	
	@Override
	public void render(){
		if(Client.hasFailLoginReason()){
			setError(Client.getFailLoginReason());
			Client.clearFailLoginReason();
			isConnecting = false;
		}
		if(Client.hasKickReason()){
			setError(Client.getKickReason());
			Client.clearKickReason();
			isConnecting = false;
		}
		super.render();
	}

	@Override
	public org.lwjgl.util.Color getBackgroundColor(){
		return BACK_GROUND;
	}
	
	@Override
	public void initialise(){
		if(!buttons.isEmpty())
			return;
		addButton(new ActionButton(new Vector2f(-0.4f, -0.7f), new Vector2f(0.25f, 0.1f), TextureCreator.createButtonTexture("quit game", 0.25f, 0.1f, BUTTON_COLOR, Color.BLACK, Color.BLACK)){

			@Override
			public void click(float x, float y, int button) {
				Client.stop();
			}
		});
		addButton(new ActionButton(new Vector2f(0.3f, -0.4f), new Vector2f(0.2f, 0.1f), TextureCreator.createButtonTexture("log in", 0.2f, 0.1f, Color.GREEN, Color.BLACK, Color.BLACK)){

			@Override
			public void click(float x, float y, int button){
				try {
					if(!isConnecting){
						setError("logging in...");
						//ConnectionManagerMain.verifyUserName(getUserName());
						//ConnectionManagerMain.verifyPassword(getPassword());
						ConnectionSpeaker.verifyUserName(getUserName());
						ConnectionSpeaker.verifyPassword(getPassword());
						Client.setConnection(new SocketConnection(new PrintWriter("main connection.log")));
						ConnectionSpeaker.open();
						ConnectionSpeaker.login(getUserName(), getPassword());
						//ConnectionManagerMain manager = new ConnectionManagerMain(new SocketConnection(new PrintWriter("main connection.log")));
						//manager.open();
						//manager.login(getUserName(), getPassword());
						//Client.setConnection(manager);
						isConnecting = true;
						MouseInput.setCooldown(ClientScreen.fps());
					}
				} catch (Exception e) {
					e.printStackTrace();
					setError(e.getLocalizedMessage());
				}
			}
		});
		addButton(new LinkButton(new Vector2f(0.3f, -0.7f), new Vector2f(0.4f, 0.1f), state.getCreateMenu(), state, TextureCreator.createButtonTexture("create new account", 0.4f, 0.1f, BUTTON_COLOR, Color.BLACK, Color.BLACK)));
		addButton(new TypeButton(new Vector2f(0.3f, 0.6f), new Vector2f(0.4f, 0.1f), "", new Color(100, 100, 150), Color.BLACK, Color.BLACK));
		addButton(new TypeButton(new Vector2f(0.3f, 0.3f), new Vector2f(0.4f, 0.1f), "", new Color(100, 100, 150), Color.BLACK, Color.BLACK){
			
			@Override
			protected void refreshTexture(){
				textures = new ITexture[]{TextureCreator.createButtonTexture(getPassString(text), (int) (size.x * ClientScreen.getWidth()), (int) (size.y * ClientScreen.getHeight()), buttonColor, enabled ? Color.YELLOW : borderColor, textColor)};
			}
		});
		addTexture(new GuiTextTexture(new Vector2f(-0.4f, 0.6f), new Vector2f(0.25f, 0.1f), "Username:", Color.BLACK));
		addTexture(new GuiTextTexture(new Vector2f(-0.4f, 0.3f), new Vector2f(0.25f, 0.1f), "Password:", Color.BLACK));
		addTexture(new GuiTextTexture(new Vector2f(0f, 0f), new Vector2f(0.8f, 0.1f), "", Color.RED));
	}
	
	private static String getPassString(String text){
		char[] value = new char[text.length()];
		Arrays.fill(value, 'o');
		return new String(value);
	}
	
	private void setError(String error){
		((GuiTextTexture) textures.get(2)).setText(error, Color.RED);
	}
}
