package nl.knokko.server.connection.protocol;

import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public class ProtocolCreateWarLobby implements ReceiveProtocol {

	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		//WarSettings settings = new ServerWarSettings(input);
		//TODO add this after you add siege mode
	}
}