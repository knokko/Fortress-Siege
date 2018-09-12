package nl.knokko.server.connection.protocol;

import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public interface ReceiveProtocol {
	
	void read(BitInput input, UserSocketConnection usc);
}