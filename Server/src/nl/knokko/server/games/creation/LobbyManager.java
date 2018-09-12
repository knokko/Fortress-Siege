package nl.knokko.server.games.creation;

import java.util.ArrayList;
import java.util.Collection;

public class LobbyManager {
	
	private final Collection<ServerSiegeLobby> siegeLobbies;
	//private Collection<ServerWarLobby> warLobbies;

	public LobbyManager() {
		siegeLobbies = new ArrayList<ServerSiegeLobby>();
		//warLobbies = new ArrayList<ServerWarLobby>();
	}
	
	public void remove(ServerSiegeLobby lobby){
		siegeLobbies.remove(lobby);
	}
	
	public void add(ServerSiegeLobby lobby){
		siegeLobbies.add(lobby);
	}
	
	public ServerSiegeLobby getSiegeLobbyByHostName(String hostUserName){
		for(ServerSiegeLobby lobby : siegeLobbies)
			if(lobby.getHost().getUser().getData().getUsername().equals(hostUserName))
				return lobby;
		return null;
	}
}