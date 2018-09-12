package nl.knokko.server;

import nl.knokko.server.chat.ServerChat;
import nl.knokko.server.connection.ConnectionServer;
import nl.knokko.server.data.AccountManager;
import nl.knokko.server.games.creation.LobbyManager;

public class Server {
	
	private static Console console;
	
	private static ConnectionServer connection;
	private static AccountManager accountManager;
	private static LobbyManager lobbyManager;
	private static ServerChat chat;
	
	private static boolean stopping;

	public static void main(String[] args){
		init();
		while(!stopping){
			try {
				update();
				Thread.sleep(100);
			} catch(Exception ex){
				throw new RuntimeException(ex);
			}
		}
		close();
	}
	
	private static void init(){
		console = new Console();
		console.start();
		connection = new ConnectionServer();
		accountManager = new AccountManager();
		chat = new ServerChat();
		lobbyManager = new LobbyManager();
		connection.start();
	}
	
	private static void update(){
		
	}
	
	private static void close(){
		accountManager.finish();
		connection.close();
	}
	
	public static void stop(){
		stopping = true;
	}
	
	public static boolean isStopping(){
		return stopping;
	}
	
	public static Console getConsole(){
		return console;
	}
	
	public static AccountManager getAccountManager(){
		return accountManager;
	}
	
	public static LobbyManager getLobbyManager(){
		return lobbyManager;
	}
	
	public static ConnectionServer getConnection(){
		return connection;
	}
	
	public static ServerChat getChat(){
		return chat;
	}
}
