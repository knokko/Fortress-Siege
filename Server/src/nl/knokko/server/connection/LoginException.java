package nl.knokko.server.connection;

import nl.knokko.util.ConnectionCode;

public class LoginException extends Exception {

	private static final long serialVersionUID = 7468655552758430370L;
	
	private final byte reason;

	public LoginException(byte reason) {
		super(ConnectionCode.getFailedLoginReason(reason));
		this.reason = reason;
	}
	
	public byte getReason(){
		return reason;
	}
}
