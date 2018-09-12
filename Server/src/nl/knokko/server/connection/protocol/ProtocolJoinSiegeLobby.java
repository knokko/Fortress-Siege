package nl.knokko.server.connection.protocol;

import nl.knokko.server.Server;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.server.games.creation.ServerSiegeLobby;
import nl.knokko.server.games.creation.ServerSiegeLobby.SiegeLobbyException;
import nl.knokko.util.ConnectionCode.SLJE;
import nl.knokko.util.bits.BitInput;

public class ProtocolJoinSiegeLobby implements ReceiveProtocol {
	
	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		if(usc.getSiegeLobby() != null)
			ProtocolLeaveSiegeLobby.leave(usc);
		String hostUserName = input.readJavaString();
		ServerSiegeLobby lobby = Server.getLobbyManager().getSiegeLobbyByHostName(hostUserName);
		if(lobby != null){
			if(lobby.isOpen()){
				try {
					lobby.join(usc);
					usc.setSiegeLobby(lobby);
					usc.getSpeaker().sendSiegeLobbyJoinYou(lobby);
				} catch (SiegeLobbyException e) {
					//user is already in that lobby
					usc.getSpeaker().sendSiegeLobbyCantJoin(SLJE.ALREADY_IN);
				}
			}
			else {
				usc.getSpeaker().sendSiegeLobbyCantJoin(SLJE.CLOSED);
			}
		}
		else {//lobby must have been removed (or corrupted client)
			usc.getSpeaker().sendSiegeLobbyCantJoin(SLJE.NO_HOST);
		}
	}
}