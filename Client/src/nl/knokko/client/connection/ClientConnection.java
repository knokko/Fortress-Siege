package nl.knokko.client.connection;

public interface ClientConnection {
	
	void open();
	
	void sendToServer(byte... data);
	
	void terminate();
}