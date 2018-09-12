package nl.knokko.client.connection.protocol;

import nl.knokko.client.Client;
import nl.knokko.util.ConnectionCode;
import nl.knokko.util.bits.BitInput;

public class ProtocolCantJoinSiegeLobby implements ReceiveProtocol {
	
	@Override
	public void process(BitInput input) {
		Client.getChat().receiveMessage(ConnectionCode.SLJE.getReason(input.readByte()));
	}
}