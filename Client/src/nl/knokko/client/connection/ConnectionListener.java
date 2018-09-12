package nl.knokko.client.connection;

import nl.knokko.client.connection.protocol.*;
import nl.knokko.util.bits.BitInput;

import static nl.knokko.util.ConnectionCode.StC;

public class ConnectionListener {
	
	private static final ReceiveProtocol[] PROTOCOLS = new ReceiveProtocol[14];
	
	static {
		register(StC.LOGIN, new ProtocolLogin());
		register(StC.REGISTER, new ProtocolRegister());
		register(StC.CHAT_MESSAGE, new ProtocolChatMessage());
		register(StC.KICK, new ProtocolKick());
		register(StC.REFUSE_CONNECTION, new ProtocolRefuseConnection());
		register(StC.SIEGE_LOBBY_CREATE, new ProtocolCreateSiegeLobby());
		register(StC.SIEGE_LOBBY_JOIN_YOU, new ProtocolYouJoinSiegeLobby());
		register(StC.SIEGE_LOBBY_CANT_JOIN, new ProtocolCantJoinSiegeLobby());
		register(StC.SIEGE_LOBBY_JOIN_OTHER, new ProtocolOtherJoinSiegeLobby());
		register(StC.SIEGE_LOBBY_LEAVE, new ProtocolLeaveSiegeLobby());
		register(StC.SIEGE_LOBBY_SWITCH_TEAM, new ProtocolSwitchSiegeLobbyTeam());
		register(StC.SIEGE_LOBBY_FROZEN, new ProtocolFreezeSiegeLobby());
		register(StC.SIEGE_LOBBY_OPEN, new ProtocolOpenSiegeLobby());
		register(StC.SIEGE_LOBBY_READY, new ProtocolReadySiegeLobby());
	}
	
	private static void register(byte id, ReceiveProtocol protocol){
		PROTOCOLS[id - Byte.MIN_VALUE] = protocol;
	}
	
	public static void processMessage(BitInput message){
		byte type = message.readByte();
		if(type - Byte.MIN_VALUE < PROTOCOLS.length){
			PROTOCOLS[type - Byte.MIN_VALUE].process(message);
		}
		else {
			System.out.println("Server has sent a message with unknown id " + type + "; terminating connection");
			ConnectionSpeaker.terminate();
		}
	}
}