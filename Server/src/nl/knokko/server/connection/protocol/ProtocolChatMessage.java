package nl.knokko.server.connection.protocol;

import nl.knokko.server.Server;
import nl.knokko.server.chat.ServerChat;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public class ProtocolChatMessage implements ReceiveProtocol {

	@Override
	public void read(BitInput buffer, UserSocketConnection usc) {
		int size = 128 + ServerChat.MIN_MESSAGE_LENGTH + buffer.readByte();
		char[] message = new char[size];
		for(int i = 0; i < size; i++)
			message[i] = buffer.readChar();
		if(usc.getData() != null){
			String string = usc.getData().getUsername() + ": " + new String(message);
			Server.getChat().addMessage(string, usc);
		}
	}
}