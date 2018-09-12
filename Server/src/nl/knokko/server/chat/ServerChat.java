package nl.knokko.server.chat;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.server.Server;
import nl.knokko.server.connection.UserSocketConnection;

public class ServerChat {
	
	public static final short MAX_MESSAGE_LENGTH = (short) (256);
	public static final byte MIN_MESSAGE_LENGTH = 1;
	
	private List<String> messages;
	
	private ChatChannel mainLobby;

	public ServerChat() {
		messages = new ArrayList<String>();
		mainLobby = new ChatChannel();
	}
	
	public List<String> getMessages(){
		return messages;
	}
	
	public void addMessage(String message, UserSocketConnection sender){
		Server.getConsole().println(message);
		messages.add(message);
		if(mainLobby.isMember(sender))
			mainLobby.broadcastMessage(message);
	}
	
	public ChatChannel getMainLobbyChannel(){
		return mainLobby;
	}
	
	public static void verifyMessage(String message){
		if(message.length() > MAX_MESSAGE_LENGTH)
			throw new IllegalArgumentException("The length of a message can't exceed the " + MAX_MESSAGE_LENGTH + " characters!");
		if(message.length() < MIN_MESSAGE_LENGTH)
			throw new IllegalArgumentException("A message has to contain at least " + MIN_MESSAGE_LENGTH + " characters!");
	}
}
