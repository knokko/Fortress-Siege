package nl.knokko.client.connection.protocol;

import nl.knokko.client.Client;
import nl.knokko.client.gui.menu.lobby.GuiSiegeLobby;
import nl.knokko.client.gui.menu.lobby.GuiSiegeLobby.Create;
import nl.knokko.client.state.StateLobby;
import nl.knokko.util.bits.BitInput;

public class ProtocolCreateSiegeLobby implements ReceiveProtocol {
	
	@Override
	public void process(BitInput input) {
		if(Client.getState() instanceof StateLobby){
			StateLobby sl = (StateLobby) Client.getState();
			if(sl.getGui() instanceof GuiSiegeLobby.Create){
				((Create) sl.getGui()).opened();
			}
			else {
				System.out.println("A siege lobby has been opened on the server, but the user has already closed this menu.");
			}
		}
		else {
			System.out.println("A siege lobby has been opened on the server, but the user has closed the lobby already.");
		}
	}
}