package nl.knokko.server.games.creation;

import java.util.List;

import nl.knokko.mode.SiegeSettings;
import nl.knokko.server.games.creation.ServerSiegeLobby.Member;
import nl.knokko.util.bits.BitInput;

public class ServerSiegeSettings extends SiegeSettings {
	
	protected List<Member> players;

	public ServerSiegeSettings(BitInput input) {
		super(input);
	}
	
	public ServerSiegeSettings(){
		super();
	}
	
	public void setPlayers(List<Member> players){
		this.players = players;
	}

	@Override
	public boolean canChange() {
		return true;
	}

	@Override
	protected void onChange() {}//perform every update per changed attribute
	
	public void changeDuration(int seconds){
		super.changeDuration(seconds);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeDuration(seconds);
	}
	
	public void changeAttackingTeamSize(int max){
		super.changeAttackingTeamSize(max);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeAttackingTeamSize(max);
	}
	
	public void changeDefendingTeamSize(int max){
		super.changeDefendingTeamSize(max);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeDefendingTeamSize(max);
	}
	
	public void changeAttackingCoinDelay(int delay){
		super.changeAttackingCoinDelay(delay);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeAttackingCoinDelay(delay);
	}
	
	public void changeDefendingCoinDelay(int delay){
		super.changeDefendingCoinDelay(delay);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeDefendingCoinDelay(delay);
	}
	
	public void changeAttackingCoinIncome(int amount){
		super.changeAttackingCoinIncome(amount);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeAttackingCoinIncome(amount);
	}
	
	public void changeDefendingCoinIncome(int amount){
		super.changeDefendingCoinIncome(amount);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeDefendingCoinIncome(amount);
	}
	
	public void changeAttackingCoinSharing(boolean share){
		super.changeAttackingCoinSharing(share);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeAttackingCoinSharing(share);
	}
	
	public void changeDefendingCoinSharing(boolean share){
		super.changeDefendingCoinSharing(share);
		for(Member player : players)
			player.getUser().getSpeaker().sendSiegeLobbyChangeDefendingCoinSharing(share);
	}
}