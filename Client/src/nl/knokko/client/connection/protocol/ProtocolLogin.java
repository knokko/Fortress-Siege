package nl.knokko.client.connection.protocol;

import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.util.bits.BitInput;

public class ProtocolLogin implements ReceiveProtocol {
	
	@Override
	public void process(BitInput input) {
		ConnectionSpeaker.setLoggedIn();
	}
}