package nl.knokko.server.games.creation;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.lobby.SiegeLobby;
import nl.knokko.mode.SiegeSettings;
import nl.knokko.server.Server;
import nl.knokko.server.connection.UserSocketConnection;
import nl.knokko.team.SiegeTeam;
import nl.knokko.util.bits.BitInput;

public class ServerSiegeLobby extends SiegeLobby {
	
	protected Member host;
	protected List<Member> players;
	
	protected List<Member> attackers;
	protected List<Member> defenders;
	protected List<Member> spectators;

	public ServerSiegeLobby(UserSocketConnection hostCon, SiegeSettings settings) {
		super(settings);
		this.host = new Member(hostCon);
		this.players = new ArrayList<Member>(1);
		this.players.add(host);
		this.spectators.add(host);
	}
	
	public Member getHost(){
		return host;
	}
	
	public void setSettings(SiegeSettings settings){
		this.settings = settings;
		modCount++;
	}
	
	public void close(){
		super.close();
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyOpen(false);
	}
	
	public void open(){
		super.open();
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyOpen(true);
	}
	
	public void freeze(){
		super.freeze();
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyFrozen(true);
	}
	
	public void free(){
		super.free();
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyFrozen(false);
	}
	
	public void join(UserSocketConnection user) throws SiegeLobbyException {
		Member member = getMember(user);
		if(member != null)
			throw new SiegeLobbyException("The user (" + user + ") is already in this lobby.");
		member = new Member(user);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyJoin(user.getData().getUsername());
		players.add(member);
		spectators.add(member);
		modCount++;
	}
	
	public void leave(UserSocketConnection user) throws SiegeLobbyException {
		Member member = getMember(user);
		if(member == null)
			throw new SiegeLobbyException("The user (" + user + ") is not in this lobby.");
		getByTeam(member.getTeam()).remove(member);
		players.remove(member);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyLeave(user.getData().getUsername());
		if(players.isEmpty())
			Server.getLobbyManager().remove(this);
		modCount++;
	}
	
	public void switchTeam(UserSocketConnection user, SiegeTeam newTeam) throws SiegeLobbyException {
		Member member = getMember(user);
		if(member == null)
			throw new SiegeLobbyException("The user (" + user + ") is not in this lobby.");
		if(member.getTeam() == newTeam)
			throw new SiegeLobbyException("The user (" + user + ") is already in team " + newTeam);
		if(newTeam == SiegeTeam.ATTACKING && attackers.size() == settings.getAttackingTeamSize())
			throw new SiegeLobbyException("The attacking team is full.");
		if(newTeam == SiegeTeam.DEFENDING && defenders.size() == settings.getDefendingTeamSize())
			throw new SiegeLobbyException("The defending team is full.");
		getByTeam(member.getTeam()).remove(member);
		getByTeam(newTeam).add(member);
		member.setTeam(newTeam);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyTeamSwitch(user.getData().getUsername(), newTeam);
		modCount++;
	}
	
	protected List<Member> getByTeam(SiegeTeam team){
		if(team == SiegeTeam.ATTACKING)
			return attackers;
		if(team == SiegeTeam.DEFENDING)
			return defenders;
		if(team == SiegeTeam.SPECTATORS)
			return spectators;
		throw new IllegalArgumentException("Unknown team: " + team);
	}
	
	private Member getMember(UserSocketConnection user){
		for(Member member : players)
			if(member.getUser() == user)
				return member;
		return null;
	}
	
	public static class Member extends SiegeLobby.Member {
		
		private final UserSocketConnection user;
		
		private Member(UserSocketConnection user){
			this.user = user;
		}
		
		public UserSocketConnection getUser(){
			return user;
		}

		@Override
		public String getName() {
			return user.getData().getUsername();
		}
	}
	
	public static class SiegeLobbyException extends Exception {

		private static final long serialVersionUID = 4223002224801056008L;
		
		public SiegeLobbyException(String error){
			super(error);
		}
	}

	@Override
	public List<Member> getPlayers() {
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

	@Override
	protected void readMembers(BitInput input) {
		throw new UnsupportedOperationException("The server should not load the members!");
	}
}