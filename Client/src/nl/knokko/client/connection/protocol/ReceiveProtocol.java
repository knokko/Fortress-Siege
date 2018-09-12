package nl.knokko.client.connection.protocol;

import nl.knokko.util.bits.BitInput;

public interface ReceiveProtocol {
	
	void process(BitInput input);
}