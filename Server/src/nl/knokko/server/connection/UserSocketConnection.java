package nl.knokko.server.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import nl.knokko.server.Server;
import nl.knokko.server.connection.protocol.*;
import nl.knokko.server.data.UserData;
import nl.knokko.server.games.creation.ServerSiegeLobby;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitInputStream;
import static nl.knokko.util.ConnectionCode.*;

public class UserSocketConnection extends Thread {
	
	private static final ReceiveProtocol[] PROTOCOLS = new ReceiveProtocol[8];
	
	private static void registerProtocol(byte id, ReceiveProtocol protocol){
		PROTOCOLS[id + 128] = protocol;
	}
	
	static {
		registerProtocol(CtS.LOGIN, new ProtocolLogin());
		registerProtocol(CtS.REGISTER, new ProtocolRegister());
		registerProtocol(CtS.LOGOUT, new ProtocolLogout());
		registerProtocol(CtS.CHAT_MESSAGE, new ProtocolChatMessage());
		registerProtocol(CtS.OPEN_SIEGE_LOBBY, new ProtocolCreateSiegeLobby());
		registerProtocol(CtS.JOIN_SIEGE_LIST, new ProtocolJoinSiegeList());
		registerProtocol(CtS.OPEN_WAR_LOBBY, new ProtocolCreateWarLobby());
		registerProtocol(CtS.JOIN_WAR_LIST, new ProtocolJoinWarList());
	}
	
	private final Socket socket;
	
	private InputStream input;
	private OutputStream output;
	
	private final UserConnectionSpeaker speaker;
	
	private UserData data;
	
	private ServerSiegeLobby siegeLobby;

	public UserSocketConnection(Socket socket) {
		this.socket = socket;
		this.speaker = new UserConnectionSpeaker(this);
	}
	
	@Override
	public void run(){
		try {
			input = socket.getInputStream();
			output = socket.getOutputStream();
		} catch(Exception ex){
			Server.getConsole().println("Failed to get input and output for client " + socket + ":");
			ex.printStackTrace(Server.getConsole().getOutput());
		}
		BitInput bitInput = new BitInputStream(input);
		while(!socket.isClosed())
			readMessage(bitInput);
		Server.getConsole().println("Thread for connection " + getDisplayName() + " has ended.");
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	/*
	
	public void kick(byte reason){
		try {
			sendKickMessage(reason);
			socket.close();
			Server.getConnection().removeConnection(this);
			Server.getChat().getMainLobbyChannel().removeMember(this);
			if(data != null)
				Server.getAccountManager().logout(data);
			Server.getConsole().println("Kicked " + getDisplayName() + " because: " + getKickReason(reason));
		} catch (Exception ex) {
			Server.getConsole().println("Couldn't kick client " + getDisplayName() + ":");
			ex.printStackTrace(Server.getConsole().getOutput());
		}
	}
	
	public void closeKick(){
		try {
			sendKickMessage(KR_CLOSE);
			socket.close();
			if(data != null)
				Server.getAccountManager().logout(data);
			Server.getConsole().println("Kicked " + getDisplayName() + " because the server is stopping.");
		} catch (Exception ex) {
			Server.getConsole().println("Couldn't kick client " + getDisplayName() + ":");
			ex.printStackTrace(Server.getConsole().getOutput());
		}
	}
	
	*/
	
	public String getDisplayName(){
		if(data == null)
			return "User " + socket.getInetAddress().getHostAddress();
		return "User " + data.getUsername() + "(" + socket.getInetAddress().getHostAddress() + ")";
	}
	
	@Override
	public String toString(){
		return getDisplayName();
	}
	
	/*
	
	public void refuseConnection(byte reason){
		try {
			sendCloseMessage(reason);
			socket.close();
			Server.getConnection().removeConnection(this);
			Server.getConsole().println("Refused the connection with " + getDisplayName() + " because: " + getFailedConnectReason(reason));
		} catch (Exception ex) {
			Server.getConsole().println("Couldn't refuse connection with client " + getDisplayName() + ":");
			ex.printStackTrace(Server.getConsole().getOutput());
		}
	}
	*/
	
	void sendToClient(byte... data){
		try {
			output.write(data);
		} catch(Exception ex){
			Server.getConsole().println("Failed to send data to " + getDisplayName() + ": " + ex.getMessage());
			Server.getConnection().removeConnection(this);
		}
	}
	
	/*
	
	public void sendKickMessage(byte reason){
		sendToClient(new byte[]{StC.KICK, reason});
	}
	
	public void sendCloseMessage(byte reason){
		sendToClient(new byte[]{StC.REFUSE_CONNECTION, reason});
	}
	
	public void sendChatMessage(String message){
		ServerChat.verifyMessage(message);
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(16 + 16 * message.length());
		output.addByte(StC.CHAT_MESSAGE);
		output.addByte((byte) (message.length() - ServerChat.MIN_MESSAGE_LENGTH - 128));
		for(int i = 0; i < message.length(); i++)
			output.addChar(message.charAt(i));
		sendToClient(output.toBytes());
	}
	
	public void sendLoginMessage(){
		sendToClient(StC.LOGIN);
	}
	
	public void sendRegisterMessage(){
		sendToClient(StC.REGISTER);
	}
	
	public void sendSiegeLobbyTeamSwitch(String username, SiegeTeam newTeam){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(48 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_SWITCH_TEAM);
		output.addCharArray(username.toCharArray());
		output.addByte((byte) newTeam.ordinal());
		sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyJoin(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_JOIN_OTHER);
		output.addCharArray(username.toCharArray());
		sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyLeave(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_LEAVE);
		output.addCharArray(username.toCharArray());
		sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyReady(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_READY);
		output.addCharArray(username.toCharArray());
		sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyChangeHost(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_HOST);
		output.addCharArray(username.toCharArray());
		sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyOpen(boolean open){
		sendToClient(StC.SIEGE_LOBBY_OPEN, open ? (byte) 1 : (byte) 0);
	}
	
	public void sendSiegeLobbyFrozen(boolean frozen){
		sendToClient(StC.SIEGE_LOBBY_FROZEN, frozen ? (byte) 1 : (byte) 0);
	}
	
	*/
	
	private void readMessage(BitInput buffer){
		try {
			byte type = buffer.readByte();
			int typeIndex = type + 128;
			if(typeIndex >= PROTOCOLS.length || PROTOCOLS[typeIndex] == null){
				speaker.kick(KR_ERROR);
				return;
			}
			PROTOCOLS[typeIndex].read(buffer, this);
		} catch(Throwable ex){
			Server.getConsole().println("Failed to read the next data: " + ex.getMessage());
			speaker.kick(KR_ERROR);
		}
	}
	
	public UserConnectionSpeaker getSpeaker(){
		return speaker;
	}
	
	public UserData getData(){
		return data;
	}
	
	public ServerSiegeLobby getSiegeLobby(){
		return siegeLobby;
	}
	
	public void setSiegeLobby(ServerSiegeLobby lobby){
		siegeLobby = lobby;
	}
	
	/*
	public void leaveSiegeLobby(){
		if(siegeLobby != null){
			try {
				siegeLobby.leave(this);
			} catch (SiegeLobbyException e) {
				Server.getConsole().println("The user " + getDisplayName() + " is not in this siege lobby.");
			}
			siegeLobby = null;
		}
		else {
			Server.getConsole().println("Leaving the siege lobby has been requested, but this user is not in a siege lobby.");
		}
	}
	
	public void joinSiegeLobby(ServerSiegeLobby lobby){
		siegeLobby = lobby;
		try {
			lobby.join(this);
		} catch (SiegeLobbyException e) {
			Server.getConsole().println("The user " + getDisplayName() + " is already in his lobby.");
			return;
		}
		BooleanArrayBitOutput output = new BooleanArrayBitOutput();
		output.addByte(StC.SIEGE_LOBBY_JOIN_YOU);
		lobby.toBits(output);
		sendToClient(output.toBytes());
	}
	
	public void openSiegeLobby(SiegeSettings settings){
		siegeLobby = new ServerSiegeLobby(this, settings);
		Server.getLobbyManager().add(siegeLobby);
	}
	*/
	
	public void setData(UserData data){
		this.data = data;
	}
}
