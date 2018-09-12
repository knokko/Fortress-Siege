package nl.knokko.client.connection.protocol;

import nl.knokko.client.Client;
import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.util.bits.BitInput;

public class ProtocolChatMessage implements ReceiveProtocol {

	@Override
	public void process(BitInput input) {
		int size = 128 + ConnectionSpeaker.MIN_MESSAGE_LENGTH + input.readByte();
		char[] message = new char[size];
		for(int i = 0; i < size; i++)
			message[i] = input.readChar();
		Client.getChat().receiveMessage(new String(message));
	}
}