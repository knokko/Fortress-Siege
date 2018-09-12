package nl.knokko.server.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import nl.knokko.account.AccountData;
import nl.knokko.server.Server;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitInputStream;
import nl.knokko.util.bits.BooleanArrayBitOutput;
import static nl.knokko.util.ConnectionCode.*;

public class UserData extends AccountData {
	
	public static UserData fromFile(File file, String username){
		try {
			FileInputStream input = new FileInputStream(file);
			BitInput buffer = new BitInputStream(input);
			String password = readPassword(buffer);
			input.close();
			return new UserData(username, password);
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	UserData(String username, String password) {
		super(username, password);
	}
	
	public void save(File file){
		try {
			BooleanArrayBitOutput buffer = new BooleanArrayBitOutput(Password.LENGTH_BITS + 16 * password.length());
			buffer.addNumber(password.length() - Password.MIN_LENGTH, Password.LENGTH_BITS, false);
			for(byte b = 0; b < password.length(); b++)
				buffer.addChar(password.charAt(b));
			FileOutputStream output = new FileOutputStream(file);
			output.write(buffer.toBytes());
			output.close();
		} catch(Exception ex){
			Server.getConsole().println("Failed to save user data for " + getUsername() + ":");
			ex.printStackTrace(Server.getConsole().getOutput());
		}
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
}
