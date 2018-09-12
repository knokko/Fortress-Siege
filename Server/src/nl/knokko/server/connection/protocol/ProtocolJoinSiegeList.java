package nl.knokko.server.connection.protocol;

import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public class ProtocolJoinSiegeList implements ReceiveProtocol {

	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		//TODO receive filter from client and request a list of open siege lobbies
	}
}