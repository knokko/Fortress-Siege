package nl.knokko.client.games.creation;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.client.Client;
import nl.knokko.lobby.SiegeLobby;
import nl.knokko.mode.SiegeSettings;
import nl.knokko.team.SiegeTeam;
import nl.knokko.util.bits.BitInput;

public class ClientSiegeLobby extends SiegeLobby {
	
	protected Member host;
	
	protected List<Member> players;
	
	protected List<Member> attackers;
	protected List<Member> defenders;
	protected List<Member> spectators;

	public ClientSiegeLobby(SiegeSettings settings) {
		super(settings);
		host = new Member(Client.getAccountData().getUsername());
		players = new ArrayList<Member>(settings.getAttackingTeamSize() + settings.getDefendingTeamSize());
		attackers = new ArrayList<Member>(settings.getAttackingTeamSize());
		defenders = new ArrayList<Member>(settings.getDefendingTeamSize());
		spectators = new ArrayList<Member>(1);
		players.add(host);
		spectators.add(host);
	}
	
	public ClientSiegeLobby(BitInput input){
		super(new ClientSiegeSettings(input), input);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void readMembers(BitInput input){
		String hostName = input.readJavaString();
		int playerCount = input.readInt();
		players = new ArrayList<Member>(playerCount);
		attackers = new ArrayList<Member>(settings.getAttackingTeamSize());
		defenders = new ArrayList<Member>(settings.getDefendingTeamSize());
		spectators = new ArrayList<Member>(0);
		SiegeTeam[] teams = SiegeTeam.values();
		for(int i = 0; i < playerCount; i++){
			Member player = new Member(input.readJavaString());
			player.setTeam(teams[(int) input.readNumber((byte) 2, false)]);
			player.setReady(input.readInt());
			players.add(player);
			((List<Member>)getByTeam(player.getTeam())).add(player);//should be safe
			if(player.getName().equals(hostName))
				host = player;
		}
	}
	
	@Override
	public Member getHost() {
		return host;
	}

	@Override
	protected List<Member> getPlayers() {
		return players;
	}

	@Override
	protected List<Member> getAttackers() {
		return attackers;
	}

	@Override
	protected List<Member> getDefenders() {
		return defenders;
	}

	@Override
	protected List<Member> getSpectators() {
		return spectators;
	}
	
	public static class Member extends SiegeLobby.Member {
		
		protected final String name;
		
		public Member(String name){
			super();
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}