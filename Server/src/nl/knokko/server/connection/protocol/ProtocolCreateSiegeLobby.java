package nl.knokko.server.connection.protocol;

import nl.knokko.server.Server;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.server.games.creation.ServerSiegeLobby;
import nl.knokko.server.games.creation.ServerSiegeSettings;
import nl.knokko.util.bits.BitInput;

public class ProtocolCreateSiegeLobby implements ReceiveProtocol {

	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		ServerSiegeSettings settings = new ServerSiegeSettings(input);
		if(usc.getSiegeLobby() != null)
			ProtocolLeaveSiegeLobby.leave(usc);
		//if(usc.getWarLobby() != null){
			//usc.leaveWarLobby();
		//}
		ServerSiegeLobby lobby = new ServerSiegeLobby(usc, settings);
		settings.setPlayers(lobby.getPlayers());
		usc.setSiegeLobby(lobby);
		Server.getLobbyManager().add(lobby);
		//usc.openSiegeLobby(settings);
	}
}