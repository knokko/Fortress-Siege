package nl.knokko.server.connection.protocol;

import static nl.knokko.util.ConnectionCode.Password;
import static nl.knokko.util.ConnectionCode.Username;
import nl.knokko.server.Server;
import nl.knokko.server.connection.RegisterException;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public class ProtocolRegister implements ReceiveProtocol {

	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		byte nameLength = (byte) (input.readNumber(Username.LENGTH_BITS, false) + Username.MIN_LENGTH);
		byte passLength = (byte) (input.readNumber(Password.LENGTH_BITS, false) + Password.MIN_LENGTH);
		char[] name = new char[nameLength];
		for(byte b = 0; b < nameLength; b++)
			name[b] = input.readChar();
		char[] pass = new char[passLength];
		for(byte b = 0; b < passLength; b++)
			pass[b] = input.readChar();
		try {
			String username = new String(name);
			Server.getConsole().println(usc.getDisplayName() + " is trying to register with name " + username);
			usc.setData(Server.getAccountManager().register(username, new String(pass)));
			Server.getConsole().println(usc.getDisplayName() + " registered with name " + username);
			usc.getSpeaker().sendRegisterMessage();
			Server.getChat().getMainLobbyChannel().addMember(usc);
		} catch(RegisterException ex){
			usc.getSpeaker().refuseConnection(ex.getReason());
		}
	}
}