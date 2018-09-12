package nl.knokko.world.territory;

import nl.knokko.util.bits.BooleanArrayBitInput;
import nl.knokko.util.bits.BooleanArrayBitOutput;

public class Territory {
	
	private final boolean[] data;
	
	public Territory(byte b){
		data = BooleanArrayBitOutput.byteToBinary(b);
	}
	
	public Territory(Team... users){
		data = new boolean[8];
		for(Team user : users)
			data[user.ordinal()] = true;
	}
	
	public Territory(boolean blue, boolean red, boolean green, boolean black, boolean white, boolean yellow, boolean pink, boolean cyan){
		data = new boolean[]{blue, red, green, black, white, yellow, pink, cyan};
	}
	
	public boolean canPlace(Team team){
		return data[team.ordinal()];
	}
	
	public byte toByte(){
		return BooleanArrayBitInput.byteFromBinary(data);
	}
}