package nl.knokko.server.connection.protocol;

import nl.knokko.server.Server;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.util.bits.BitInput;

public class ProtocolLogout implements ReceiveProtocol {

	@Override
	public void read(BitInput input, UserSocketConnection usc) {
		if(usc.getData() != null)
			Server.getAccountManager().logout(usc.getData());
		else
			Server.getConsole().println(usc.getDisplayName() + " logged out before logging in.");
		Server.getConnection().removeConnection(usc);
		Server.getChat().getMainLobbyChannel().removeMember(usc);
		try {
			usc.getSocket().close();
		} catch (Exception ex) {
			Server.getConsole().println("Failed to terminate the connection with the left " + usc.getDisplayName());
			ex.printStackTrace(Server.getConsole().getOutput());
		}
	}
}
