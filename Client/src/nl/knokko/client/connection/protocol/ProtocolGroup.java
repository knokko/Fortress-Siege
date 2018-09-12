package nl.knokko.client.connection.protocol;

import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.util.bits.BitInput;

public class ProtocolGroup implements ReceiveProtocol {
	
	protected final ReceiveProtocol[] protocols;

	public ProtocolGroup(int amount) {
		protocols = new ReceiveProtocol[amount];
	}

	@Override
	public void process(BitInput input) {
		int index = input.readByte() - Byte.MIN_VALUE;
		if(index < protocols.length){
			protocols[index].process(input);//TODO now actually use this class...
		}
		else {
			System.out.println("The server has sent a message of type " + getClass().getSimpleName() + " with unknown sub id " + (index + Byte.MIN_VALUE));
			ConnectionSpeaker.terminate();
		}
	}
	
	protected void register(byte id, ReceiveProtocol protocol){
		protocols[id - Byte.MIN_VALUE] = protocol;
	}
}