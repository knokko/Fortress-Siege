package nl.knokko.server.connection.protocol;

import nl.knokko.server.Server;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.server.games.creation.ServerSiegeLobby.SiegeLobbyException;
import nl.knokko.util.bits.BitInput;

public class ProtocolLeaveSiegeLobby implements ReceiveProtocol {
	
	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		if(usc.getSiegeLobby() != null)
			leave(usc);
		else
			Server.getConsole().println("ProtocolLeaveSiegeLobby: user " + usc + " is not in a siege lobby.");
	}
	
	public static void leave(UserSocketConnection usc){
		try {
			usc.getSiegeLobby().leave(usc);
		} catch (SiegeLobbyException e) {
			Server.getConsole().println("ProtocolLeaveSiegeLobby: user " + usc + " was expecting to be in a siege lobby, but was not.");
		}
	}
}