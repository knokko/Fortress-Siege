package nl.knokko.util;

public final class ConnectionCode {
	
	/**
	 * Server to Client byte constants
	 */
	public static class StC {
		
		public static final byte LOGIN = -128;//the client logged in successfully
		public static final byte REGISTER = -127;//the client registered successfully
		public static final byte CHAT_MESSAGE = -126;//a chat message was sent to the client
		public static final byte KICK = -125;//the client was kicked
		public static final byte REFUSE_CONNECTION = -124;//the client failed to log in or register
		public static final byte SIEGE_LOBBY_CREATE = -123;//the client opened a siege lobby successfully
		public static final byte SIEGE_LOBBY_JOIN_YOU = -122;//the client joined a siege lobby successfully
		public static final byte SIEGE_LOBBY_CANT_JOIN = -121;//the client could not join a siege lobby
		public static final byte SIEGE_LOBBY_JOIN_OTHER = -120;//another player joined the siege lobby the client is in
		public static final byte SIEGE_LOBBY_LEAVE = -119;//another player left the siege lobby the client is in
		public static final byte SIEGE_LOBBY_SWITCH_TEAM = -118;//a player has changed his team (possibly the client)
		public static final byte SIEGE_LOBBY_FROZEN = -117;//the host has frozen or unfrozen the lobby
		public static final byte SIEGE_LOBBY_OPEN = -116;//the host opens or closes the lobby
		public static final byte SIEGE_LOBBY_READY = -115;//a players claims to be ready for the specified modcount
	}
	
	/**
	 * Client to Server byte constants
	 */
	public static class CtS {
		
		public static final byte LOGIN = -128;
		public static final byte REGISTER = -127;
		public static final byte LOGOUT = -126;
		public static final byte CHAT_MESSAGE = -125;
		public static final byte OPEN_SIEGE_LOBBY = -124;
		public static final byte JOIN_SIEGE_LIST = -123;
		public static final byte OPEN_WAR_LOBBY = -122;
		public static final byte JOIN_WAR_LIST = -121;
	}
	
	public static class Password {
		
		public static final byte MIN_LENGTH = 6;
		public static final byte MAX_LENGTH = 37;
		public static final byte LENGTH_BITS = 5;
	}
	
	public static class Username {
		
		public static final byte MIN_LENGTH = 2;
		public static final byte MAX_LENGTH = 17;
		public static final byte LENGTH_BITS = 4;
	}
	
	/**
	 * Siege Lobby join error codes
	 */
	public static class SLJE {
		
		public static final byte NO_HOST = -128;
		public static final byte ALREADY_IN = -127;
		public static final byte CLOSED = -126;
		
		public static String getReason(byte code){
			if(code == NO_HOST)
				return "There is no siege lobby with that host (anymore).";
			if(code == ALREADY_IN)
				return "You are already in thay siege lobby.";
			if(code == CLOSED)
				return "That siege lobby is no longer open.";
			throw new IllegalArgumentException("Unknown Siege Lobby Join Error Code: " + code);
		}
	}
	
	public static final byte KR_CLOSE = -128;
	public static final byte KR_ERROR = -127;
	
	public static final byte LE_NO_USERNAME = -128;
	public static final byte LE_WRONG_PASSWORD = -127;
	public static final byte LE_ALREADY_ONLINE = -126;
	public static final byte RE_NAME_NOT_AVAILABLE = -125;
	
	public static String getFailedLoginReason(byte reason){
		if(reason == LE_NO_USERNAME)
			return "There is no account with this username!";
		if(reason == LE_WRONG_PASSWORD)
			return "This password is incorrect!";
		throw new IllegalArgumentException("Unknown login fail reason: " + reason);
	}
	
	public static String getFailedRegisterReason(byte reason){
		if(reason == RE_NAME_NOT_AVAILABLE)
			return "There is already an account with this username!";
		throw new IllegalArgumentException("Unknown register fail reason: " + reason);
	}
	
	public static String getFailedConnectReason(byte reason){
		if(reason == LE_NO_USERNAME)
			return "There is no account with this username!";
		if(reason == LE_WRONG_PASSWORD)
			return "This password is incorrect!";
		if(reason == RE_NAME_NOT_AVAILABLE)
			return "There is already an account with this username!";
		throw new IllegalArgumentException("Unknown connect fail reason: " + reason);
	}
	
	public static String getKickReason(byte reason){
		if(reason == KR_CLOSE)
			return "The server has stopped.";
		if(reason == KR_ERROR)
			return "A connection error occured.";
		throw new IllegalArgumentException("Unknown kick reason: " + reason);
	}
}