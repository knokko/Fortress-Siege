package nl.knokko.util;

public final class Maths {

	private static final int[] POWERS = new int[30];
	
	static {
		int power = 1;
		for(int i = 0; i < POWERS.length; i++){
			POWERS[i] = power;
			power *= 2;
		}
	}
	
	public static boolean powerOf2(int number){
		for(int power : POWERS)
			if(number == power)
				return true;
		return false;
	}
	
	public static byte log2Up(int number){
		if(number < 1)
			throw new IllegalArgumentException("Number (" + number + ") is too small!");
		for(byte i = 0; i < POWERS.length; i++){
			if(number <= POWERS[i])
				return i;
		}
		throw new IllegalArgumentException("Number (" + number + ") is greater than 2^30 !");
	}
	
	public static int next2Power(int number){
		if(number < 1)
			throw new IllegalArgumentException("Number (" + number + ") is too small!");
		for(int i = 0; i < POWERS.length; i++){
			if(number <= POWERS[i])
				return POWERS[i];
		}
		throw new IllegalArgumentException("Number (" + number + ") is greater than 2^30 !");
	}
}
