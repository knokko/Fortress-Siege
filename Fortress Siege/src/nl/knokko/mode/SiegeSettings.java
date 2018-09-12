package nl.knokko.mode;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public abstract class SiegeSettings {
	
	private int duration;
	private int attackingTeamSize;
	private int defendingTeamSize;
	
	private int coinDelayAtt;
	private int coinIncomeAtt;
	private boolean shareCoinsAtt;
	
	private int coinDelayDef;
	private int coinIncomeDef;
	private boolean shareCoinsDef;

	public SiegeSettings(int duration, int attackingTeamSize, int defendingTeamSize, int coinDelayAtt, int coinIncomeAtt, boolean shareCoinsAtt, int coinDelayDef, int coinIncomeDef, boolean shareCoinsDef) {
		this.duration = duration;
		this.attackingTeamSize = attackingTeamSize;
		this.defendingTeamSize = defendingTeamSize;
		this.coinDelayAtt = coinDelayAtt;
		this.coinIncomeAtt = coinIncomeAtt;
		this.shareCoinsAtt = shareCoinsAtt;
		this.coinDelayDef = coinDelayDef;
		this.coinIncomeDef = coinIncomeDef;
		this.shareCoinsDef = shareCoinsDef;
	}
	
	public SiegeSettings(BitInput input){
		this(input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readInt(), input.readBoolean(), input.readInt(), input.readInt(), input.readBoolean());
	}
	
	public SiegeSettings(){
		this(900, 4, 4, 1, 10, false, 1, 10, false);
	}
	
	public void toBits(BitOutput output){
		output.addInt(duration);
		output.addInt(attackingTeamSize);
		output.addInt(defendingTeamSize);
		output.addInt(coinDelayAtt);
		output.addInt(coinIncomeAtt);
		output.addBoolean(shareCoinsAtt);
		output.addInt(coinDelayDef);
		output.addInt(coinIncomeDef);
		output.addBoolean(shareCoinsDef);
	}
	
	/**
	 * @return The time the defending team needs to protect their fortress, in seconds.
	 */
	public int getDuration(){
		return duration;
	}
	
	/**
	 * @return True if the defending team can't win, but can only survive as long as possible.
	 */
	public boolean isEndless(){
		return duration <= 0;
	}
	
	/**
	 * @return The maximum amount of players the attacking team can have
	 */
	public int getAttackingTeamSize(){
		return attackingTeamSize;
	}
	
	/**
	 * @return The maximum amount of players the defending team can have
	 */
	public int getDefendingTeamSize(){
		return defendingTeamSize;
	}
	
	/**
	 * @return True if all members of the attacking team share their coins, false if they all get their own coins to spend.
	 */
	public boolean doAttackersShareCoins(){
		return shareCoinsAtt;
	}
	
	/**'
	 * @return True if all members of the defending team share their coins, false if they all get their own coins to spend.
	 */
	public boolean doDefendersShareCoins(){
		return shareCoinsDef;
	}
	
	/**
	 * The coin delay is the time in seconds between receiving 2 coin incomes.
	 * @return The coin delay of the attacking team
	 */
	public int getAttackingCoinDelay(){
		return coinDelayAtt;
	}
	
	/**
	 * The coin delay is the time in seconds between receiving 2 coin incomes.
	 * @return The coin delay of the defending team
	 */
	public int getDefendingCoinDelay(){
		return coinDelayDef;
	}
	
	/**
	 * The coin income is the amount of coins the team or members receive after every coin delay.
	 * @return The coin income of the attacking team
	 */
	public int getAttackingCoinIncome(){
		return coinIncomeAtt;
	}
	
	/**
	 * The coin income is the amount of coins the team or members receive after every coin delay.
	 * @return The coin income of the defending team
	 */
	public int getDefendingCoinIncome(){
		return coinIncomeDef;
	}
	
	public void changeDuration(int seconds){
		checkChange();
		this.duration = seconds;
		onChange();
	}
	
	public void changeAttackingTeamSize(int players){
		checkChange();
		this.attackingTeamSize = players;
		onChange();
	}
	
	public void changeDefendingTeamSize(int players){
		checkChange();
		this.defendingTeamSize = players;
		onChange();
	}
	
	public void changeAttackingCoinDelay(int delay){
		checkChange();
		this.coinDelayAtt = delay;
		onChange();
	}
	
	public void changeDefendingCoinDelay(int delay){
		checkChange();
		this.coinDelayDef = delay;
		onChange();
	}
	
	public void changeAttackingCoinIncome(int amount){
		checkChange();
		this.coinIncomeAtt = amount;
		onChange();
	}
	
	public void changeDefendingCoinIncome(int amount){
		checkChange();
		this.coinIncomeDef = amount;
		onChange();
	}
	
	public void changeAttackingCoinSharing(boolean share){
		checkChange();
		this.shareCoinsAtt = share;
		onChange();
	}
	
	public void changeDefendingCoinSharing(boolean share){
		checkChange();
		this.shareCoinsDef = share;
		onChange();
	}
	
	/**
	 * @return True if the user can change the settings, false otherwise.
	 */
	public abstract boolean canChange();
	
	/**
	 * Do whatever needs to happen to inform the server or other players that the settings have changed.
	 */
	protected abstract void onChange();
	
	protected void checkChange(){
		if(!canChange())
			throw new UnsupportedOperationException("The settings of " + getClass() + " can't be changed directly.");
	}
}