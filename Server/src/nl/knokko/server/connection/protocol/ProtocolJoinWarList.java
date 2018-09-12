package nl.knokko.server.connection.protocol;

import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public class ProtocolJoinWarList implements ReceiveProtocol {

	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		// TODO receive a filter from the client and return a list of available war lobbies
	}
}