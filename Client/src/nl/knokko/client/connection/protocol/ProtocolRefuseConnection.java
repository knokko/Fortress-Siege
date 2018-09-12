package nl.knokko.client.connection.protocol;

import nl.knokko.client.Client;
import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.util.bits.BitInput;

public class ProtocolRefuseConnection implements ReceiveProtocol {

	@Override
	public void process(BitInput input) {
		Client.setFailLoginReason(input.readByte());
		ConnectionSpeaker.terminate();
	}
}