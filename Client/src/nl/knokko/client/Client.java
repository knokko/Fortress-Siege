package nl.knokko.client;

import nl.knokko.account.AccountData;
import nl.knokko.client.connection.ClientConnection;
import nl.knokko.client.input.*;
import nl.knokko.client.render.GuiRenderer;
import nl.knokko.client.state.ClientState;
import nl.knokko.client.state.StateMainMenu;
import nl.knokko.client.texture.TextureLoader;
import nl.knokko.util.ConnectionCode;

public class Client {
	
	private static ClientState state;
	private static ClientChat chat;
	private static AccountData data;
	
	private static ClientState newState;
	
	private static ClientConnection connection;
	
	private static byte kickReason;
	private static byte failLogin;
	
	private static boolean isStopping;

	public static void main(String[] args) {
		prepare();
		open();
		init();
		while(shouldContinue()){
			update();
			render();
		}
		finish();
		close();
	}
	
	private static void prepare(){
		Natives.prepare();
	}
	
	private static void open(){
		ClientScreen.openScreen();
	}
	
	private static void init(){
		state = new StateMainMenu();
		state.open();
		chat = new ClientChat();
	}
	
	private static boolean shouldContinue(){
		return !isStopping && !ClientScreen.closeRequested();
	}
	
	private static void update(){
		if(newState != null){
			state.close();
			state = newState;
			state.open();
			newState = null;
		}
		for(MouseClickEvent event : MouseInput.getMouseClicks())
			state.click(event.getX(), event.getY(), event.getButton());
		for(KeyPressedEvent event : KeyInput.getCurrentPresses())
			state.keyPressed(event.getKey(), event.getCharacter());
		for(KeyReleasedEvent event : KeyInput.getCurrentReleases())
			state.keyReleased(event.getKey());
		state.update();
		KeyInput.update();
		MouseInput.update();
	}
	
	private static void render(){
		state.render();
		ClientScreen.updateScreen();
	}
	
	private static void finish(){
		state.close();
		if(connection != null)
			connection.terminate();
	}
	
	private static void close(){
		TextureLoader.clean();
		GuiRenderer.clean();
		ClientScreen.closeScreen();
	}
	
	public static void stop(){
		isStopping = true;
	}
	
	public static void setState(ClientState state){
		newState = state;
	}
	
	public static ClientState getState(){
		return state;
	}
	
	public static void setAccountData(AccountData accountData){
		data = accountData;
	}
	
	public static AccountData getAccountData(){
		return data;
	}
	
	public static void setConnection(ClientConnection connectionManager){
		connection = connectionManager;
	}
	
	public static ClientConnection getConnection(){
		return connection;
	}
	
	public static ClientChat getChat(){
		return chat;
	}
	
	public static void setKickReason(byte reason){
		kickReason = reason;
	}
	
	public static boolean hasKickReason(){
		return kickReason != 0;
	}
	
	public static String getKickReason(){
		return hasKickReason() ? ConnectionCode.getKickReason(kickReason) : "There is no kick reason!";
	}
	
	public static void clearKickReason(){
		kickReason = 0;
	}
	
	public static void setFailLoginReason(byte reason){
		failLogin = reason;
	}
	
	public static boolean hasFailLoginReason(){
		return failLogin != 0;
	}
	
	public static String getFailLoginReason(){
		return ConnectionCode.getFailedConnectReason(failLogin);
	}
	
	public static void clearFailLoginReason(){
		failLogin = 0;
	}
}
