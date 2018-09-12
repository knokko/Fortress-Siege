package nl.knokko.lobby;

import java.util.List;

import nl.knokko.mode.SiegeSettings;
import nl.knokko.team.SiegeTeam;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public abstract class SiegeLobby {
	
	protected SiegeSettings settings;
	
	protected int modCount;
	
	protected boolean open;
	protected boolean frozen;

	public SiegeLobby(SiegeSettings settings) {
		setSettings(settings);
		modCount = 0;
		open = false;
		frozen = false;
	}
	
	public SiegeLobby(SiegeSettings settings, int modCount, boolean open, boolean frozen){
		setSettings(settings);
		this.modCount = modCount;
		this.open = open;
		this.frozen = frozen;
	}
	
	public SiegeLobby(SiegeSettings settings, BitInput input){
		this(settings);
		modCount = input.readInt();
		open = input.readBoolean();
		frozen = input.readBoolean();
		readMembers(input);
	}
	
	public SiegeSettings getSettings(){
		return settings;
	}
	
	public void setSettings(SiegeSettings settings){
		this.settings = settings;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public boolean isFrozen(){
		return frozen;
	}
	
	public void close(){
		open = false;
	}
	
	public void open(){
		open = true;
	}
	
	public void freeze(){
		frozen = true;
	}
	
	public void free(){
		frozen = false;
	}
	
	protected List<? extends Member> getByTeam(SiegeTeam team){
		if(team == SiegeTeam.ATTACKING)
			return getAttackers();
		if(team == SiegeTeam.DEFENDING)
			return getDefenders();
		if(team == SiegeTeam.SPECTATORS)
			return getSpectators();
		throw new IllegalArgumentException("Unknown team: " + team);
	}
	
	public void toBits(BitOutput output){
		settings.toBits(output);
		output.addInt(modCount);
		output.addBoolean(open);
		output.addBoolean(frozen);
		output.addJavaString(getHost().getName());
		List<? extends Member> players = getPlayers();
		output.addInt(players.size());
		for(Member player : players){
			output.addJavaString(player.getName());
			output.addNumber(player.getTeam().ordinal(), (byte) 2, false);
			output.addInt(player.getReadyModCount());
		}
	}
	
	public abstract Member getHost();
	
	protected abstract void readMembers(BitInput input);
	protected abstract List<? extends Member> getPlayers();
	protected abstract List<? extends Member> getAttackers();
	protected abstract List<? extends Member> getDefenders();
	protected abstract List<? extends Member> getSpectators();
	
	public static abstract class Member {
		
		protected SiegeTeam team;
		
		protected int readyModCount;
		
		public Member(){
			team = SiegeTeam.SPECTATORS;
			readyModCount = -1;
		}
		
		public abstract String getName();
		
		public SiegeTeam getTeam(){
			return team;
		}
		
		public boolean isReady(SiegeLobby lobby){
			return readyModCount == lobby.modCount;
		}
		
		public void setReady(int modCount){
			readyModCount = modCount;
		}
		
		public int getReadyModCount(){
			return readyModCount;
		}
		
		public void setTeam(SiegeTeam team){
			this.team = team;
		}
	}
}