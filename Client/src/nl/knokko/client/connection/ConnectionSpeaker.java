package nl.knokko.client.connection;

import nl.knokko.client.Client;
import nl.knokko.client.state.StateLobby;
import nl.knokko.client.state.StateMainMenu;
import nl.knokko.mode.SiegeSettings;
import nl.knokko.util.ConnectionCode.CtS;
import nl.knokko.util.ConnectionCode.Password;
import nl.knokko.util.ConnectionCode.Username;
import nl.knokko.util.bits.BitOutput;
import nl.knokko.util.bits.BooleanArrayBitOutput;

public class ConnectionSpeaker {
	
	public static final short MAX_MESSAGE_LENGTH = (short) (254 - Username.MAX_LENGTH);
	public static final short MIN_MESSAGE_LENGTH = 1;
	
	private static boolean loggedIn;
	
	public static void open(){
		Client.getConnection().open();
	}
	
	public static void terminate(){
		Client.getConnection().terminate();
		Client.setConnection(null);
		loggedIn = false;
		Client.getChat().disable();
		Client.setState(new StateMainMenu());
	}
	
	public static void logout(){
		Client.getConnection().sendToServer(CtS.LOGOUT);
		terminate();
		//it appears that the logout message doesn't receive the server, which means that the server thinks a connection error occurred
		//fortunately, the server doesn't really care
	}
	
	public static void login(String username, String password){
		verifyUserName(username);
		verifyPassword(password);
		BooleanArrayBitOutput buffer = new BooleanArrayBitOutput(8 + Username.LENGTH_BITS + Password.LENGTH_BITS + 16 * username.length() + 16 * password.length());
		buffer.addByte(CtS.LOGIN);
		buffer.addNumber(username.length() - Username.MIN_LENGTH, Username.LENGTH_BITS, false);
		buffer.addNumber(password.length() - Password.MIN_LENGTH, Password.LENGTH_BITS, false);
		for(int i = 0; i < username.length(); i++)
			buffer.addChar(username.charAt(i));
		for(int i = 0; i < password.length(); i++)
			buffer.addChar(password.charAt(i));
		Client.getConnection().sendToServer(buffer.toBytes());
	}
	
	public static void register(String username, String password){
		verifyUserName(username);
		verifyPassword(password);
		BooleanArrayBitOutput buffer = new BooleanArrayBitOutput(8 + Username.LENGTH_BITS + Password.LENGTH_BITS + 16 * username.length() + 16 * password.length());
		buffer.addByte(CtS.REGISTER);
		buffer.addNumber(username.length() - Username.MIN_LENGTH, Username.LENGTH_BITS, false);
		buffer.addNumber(password.length() - Password.MIN_LENGTH, Password.LENGTH_BITS, false);
		for(int i = 0; i < username.length(); i++)
			buffer.addChar(username.charAt(i));
		for(int i = 0; i < password.length(); i++)
			buffer.addChar(password.charAt(i));
		Client.getConnection().sendToServer(buffer.toBytes());
	}
	
	public static void openSiegeLobby(SiegeSettings settings){
		BooleanArrayBitOutput input = new BooleanArrayBitOutput();
		settings.toBits(input);
		byte[] bytes = input.toBytes();
		byte[] newBytes = new byte[bytes.length + 1];
		System.arraycopy(bytes, 0, newBytes, 1, bytes.length);
		newBytes[0] = CtS.OPEN_SIEGE_LOBBY;
		Client.getConnection().sendToServer(newBytes);
	}
	
	public static void sendChatMessage(String message){
		byte[] data = new byte[2 + message.length() * 2];
		data[0] = CtS.CHAT_MESSAGE;
		data[1] = (byte) (message.length() - MIN_MESSAGE_LENGTH - 128);
		for(int i = 0; i < message.length(); i++){
			data[2 + 2 * i] = BitOutput.char0(message.charAt(i));
			data[3 + 2 * i] = BitOutput.char1(message.charAt(i));
		}
		Client.getConnection().sendToServer(data);
	}
	
	public static boolean isLoggedIn(){
		return loggedIn;
	}
	
	public static void setLoggedIn(){
		loggedIn = true;
		Client.getChat().enable();
		Client.setState(new StateLobby());
	}
	
	public static void verifyUserName(String username){
		if(username.length() > Username.MAX_LENGTH)
			throw new IllegalArgumentException("Your username can't be longer than " + Username.MAX_LENGTH + " characters!");
		if(username.length() < Username.MIN_LENGTH)
			throw new IllegalArgumentException("Your username can't be shorter than " + Username.MIN_LENGTH + " characters!");
	}
	
	public static void verifyPassword(String password){
		if(password.length() > Password.MAX_LENGTH)
			throw new IllegalArgumentException("Your password can't be longer than " + Password.MAX_LENGTH + " characters!");
		if(password.length() < Password.MIN_LENGTH)
			throw new IllegalArgumentException("Your password can't be shorter than " + Password.MIN_LENGTH + " characters!");
	}
	
	public static void verifyMessage(String message){
		if(message.length() > MAX_MESSAGE_LENGTH)
			throw new IllegalArgumentException("The length of a message can't exceed the " + MAX_MESSAGE_LENGTH + " characters!");
		if(message.length() < MIN_MESSAGE_LENGTH)
			throw new IllegalArgumentException("A message has to contain at least " + MIN_MESSAGE_LENGTH + " characters!");
	}
}