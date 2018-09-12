package nl.knokko.server.connection;

import nl.knokko.util.ConnectionCode;

public class RegisterException extends Exception {

	private static final long serialVersionUID = 5065759591518703326L;
	
	private final byte reason;

	public RegisterException(byte reason) {
		super(ConnectionCode.getFailedRegisterReason(reason));
		this.reason = reason;
	}
	
	public byte getReason(){
		return reason;
	}
}
