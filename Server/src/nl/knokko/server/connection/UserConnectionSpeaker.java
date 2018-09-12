package nl.knokko.server.connection;

import java.io.IOException;

import nl.knokko.server.Server;
import nl.knokko.server.chat.ServerChat;
import nl.knokko.server.games.creation.ServerSiegeLobby;
import nl.knokko.team.SiegeTeam;
import nl.knokko.util.ConnectionCode;
import nl.knokko.util.ConnectionCode.StC;
import nl.knokko.util.bits.BooleanArrayBitOutput;

public class UserConnectionSpeaker {
	
	private final UserSocketConnection user;
	
	public UserConnectionSpeaker(UserSocketConnection user) {
		this.user = user;
	}
	
	public void kick(byte reason){
		sendKickMessage(reason);
		Server.getConsole().println("Kicking user " + user + " because: " + ConnectionCode.getKickReason(reason));
		terminate();
	}
	
	public void closeKick(){
		sendKickMessage(ConnectionCode.KR_CLOSE);
		Server.getConsole().println("Kicking " + user.getDisplayName() + " because the server is stopping.");
		if(user.getData() != null)
			Server.getAccountManager().logout(user.getData());
		try {
			user.getSocket().close();
		} catch(IOException ex){
			Server.getConsole().println("Could not close the socket " + user.getSocket() + " while the server is stopping: " + ex.getMessage());
		}
	}
	
	public void refuseConnection(byte reason){
		sendCloseMessage(reason);
		terminate();
		Server.getConsole().println("Refused the connection with " + user + " because: " + ConnectionCode.getFailedConnectReason(reason));
	}
	
	public void sendChatMessage(String message){
		ServerChat.verifyMessage(message);
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(16 + 16 * message.length());
		output.addByte(StC.CHAT_MESSAGE);
		output.addByte((byte) (message.length() - ServerChat.MIN_MESSAGE_LENGTH - 128));
		for(int i = 0; i < message.length(); i++)
			output.addChar(message.charAt(i));
		user.sendToClient(output.toBytes());
		/*
		byte[] data = new byte[2 + message.length() * 2];
		data[0] = ID_CHAT_MESSAGE;
		data[1] = (byte) (message.length() - ServerChat.MIN_MESSAGE_LENGTH - 128);
		for(int i = 0; i < message.length(); i++){
			data[2 + 2 * i] = BitBuffer.char0(message.charAt(i));
			data[3 + 2 * i] = BitBuffer.char1(message.charAt(i));
		}
		sendToClient(data);
		*/
	}
	
	public void sendLoginMessage(){
		user.sendToClient(StC.LOGIN);
	}
	
	public void sendRegisterMessage(){
		user.sendToClient(StC.REGISTER);
	}
	
	public void sendSiegeLobbyTeamSwitch(String username, SiegeTeam newTeam){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(48 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_SWITCH_TEAM);
		output.addJavaString(username);
		output.addByte((byte) newTeam.ordinal());
		user.sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyJoin(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_JOIN_OTHER);
		output.addJavaString(username);
		user.sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyJoinYou(ServerSiegeLobby lobby){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput();
		output.addByte(StC.SIEGE_LOBBY_JOIN_YOU);
		lobby.toBits(output);
		user.sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyCantJoin(byte reason){
		user.sendToClient(new byte[]{StC.SIEGE_LOBBY_CANT_JOIN, reason});
	}
	
	public void sendSiegeLobbyLeave(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_LEAVE);
		output.addJavaString(username);
		user.sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyReady(String username){
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(40 + 16 * username.length());
		output.addByte(StC.SIEGE_LOBBY_READY);
		output.addJavaString(username);
		user.sendToClient(output.toBytes());
	}
	
	public void sendSiegeLobbyOpen(boolean open){
		user.sendToClient(StC.SIEGE_LOBBY_CREATE, open ? (byte) 1 : (byte) 0);
	}
	
	public void sendSiegeLobbyFrozen(boolean frozen){
		user.sendToClient(StC.SIEGE_LOBBY_FROZEN, frozen ? (byte) 1 : (byte) 0);
	}
	
	private void sendKickMessage(byte reason){
		user.sendToClient(new byte[]{StC.KICK, reason});
	}
	
	private void sendCloseMessage(byte reason){
		user.sendToClient(new byte[]{StC.REFUSE_CONNECTION, reason});
	}
	
	private void terminate(){
		try {
			user.getSocket().close();
		} catch(IOException ex){
			Server.getConsole().println("Could not terminate connection with user " + user + " because: " + ex.getMessage());
		}
		Server.getConnection().removeConnection(user);
		if(user.getData() != null){
			Server.getChat().getMainLobbyChannel().removeMember(user);
			Server.getAccountManager().logout(user.getData());
		}
	}
}