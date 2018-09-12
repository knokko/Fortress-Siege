package nl.knokko.client.games.creation;

import nl.knokko.mode.SiegeSettings;
import nl.knokko.util.bits.BitInput;

public class ClientSiegeSettings extends SiegeSettings {

	public ClientSiegeSettings(int duration, int attackingTeamSize,
			int defendingTeamSize, int coinDelayAtt, int coinIncomeAtt,
			boolean shareCoinsAtt, int coinDelayDef, int coinIncomeDef,
			boolean shareCoinsDef) {
		super(duration, attackingTeamSize, defendingTeamSize, coinDelayAtt,
				coinIncomeAtt, shareCoinsAtt, coinDelayDef, coinIncomeDef,
				shareCoinsDef);
	}

	public ClientSiegeSettings(BitInput input) {
		super(input);
	}

	public ClientSiegeSettings() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canChange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onChange() {
		// TODO Auto-generated method stub

	}

}
