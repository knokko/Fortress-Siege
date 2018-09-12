package nl.knokko.account;

import nl.knokko.util.ConnectionCode.Password;
import nl.knokko.util.ConnectionCode.Username;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class AccountData {
	
	public static String readUsername(BitInput input){
		byte length = (byte) input.readNumber(Username.LENGTH_BITS, false);
		char[] username = new char[length];
		for(byte index = 0; index < length; index++)
			username[index] = input.readChar();
		return new String(username);
	}
	
	public static String readPassword(BitInput input){
		byte length = (byte) input.readNumber(Password.LENGTH_BITS, false);
		char[] password = new char[length];
		for(byte index = 0; index < length; index++)
			password[index] = input.readChar();
		return new String(password);
	}
	
	protected final String username;
	protected final String password;

	public AccountData(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public AccountData(BitInput input){
		this(readUsername(input), readPassword(input));
	}
	
	public AccountData(String username, BitInput input){
		this(username, readPassword(input));
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void savePassword(BitOutput output){
		output.addNumber(password.length() - Password.MIN_LENGTH, Password.LENGTH_BITS, false);
		for(byte b = 0; b < password.length(); b++)
			output.addChar(password.charAt(b));
	}
	
	public void saveUsername(BitOutput output){
		output.addNumber(username.length() - Username.MIN_LENGTH, Username.LENGTH_BITS, false);
		for(byte b = 0; b < username.length(); b++)
			output.addChar(password.charAt(b));
	}
	
	public void save(BitOutput output){
		saveUsername(output);
		savePassword(output);
	}
}