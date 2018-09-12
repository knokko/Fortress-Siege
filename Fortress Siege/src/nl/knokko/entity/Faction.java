package nl.knokko.entity;

public enum Faction {
	
	ATTACKER,
	DEFENDER;
	
	public static Faction fromID(boolean id){
		return id ? ATTACKER : DEFENDER;
	}
	
	public boolean isAttacker(){
		return this == ATTACKER;
	}
	
	public boolean isDefender(){
		return this == DEFENDER;
	}
	
	public boolean getID(){
		return this == ATTACKER;
	}
}
