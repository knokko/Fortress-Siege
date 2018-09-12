package nl.knokko.client.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitInputStream;

public class SocketConnection extends Thread implements ClientConnection {
	
	private Socket socket;
	
	private InputStream input;
	private OutputStream output;
	
	private PrintWriter log;
	
	private byte[] preData;

	public SocketConnection(PrintWriter log) {
		this.log = log;
	}

	@Override
	public void open() {
		start();
	}
	
	@Override
	public void run(){
		String host = "192.168.2.28";
		 try {
			log.println("The SocketConnection run method is being called...");
			socket = new Socket(host, 40538);
			log.println("get input stream...");
			input = socket.getInputStream();
			log.println("got input stream; get output stream...");
			output = socket.getOutputStream();
			if(preData != null)
				output.write(preData);
			log.println("got output stream; start loop...");
			BitInput bitInput = new BitInputStream(input);
			while(!socket.isClosed())
				ConnectionListener.processMessage(bitInput);
			log.println("The connection has been stopped.");
			log.close();
		} catch (Exception e) {
			log.println("The following connection error occured:");
			e.printStackTrace(log);
			log.close();
		} 
	}

	@Override
	public void sendToServer(byte... data) {
		try {
			if(output == null){
				if(preData != null)
					throw new IllegalStateException("There can only be 1 message queued before opening the connection!");
				preData = data;
			}
			else
				output.write(data);
		} catch (Exception ex) {
			log.println("Failed to send " + data.length + " bytes to the server:");
			ex.printStackTrace(log);
			log.println();
		}
	}

	@Override
	public void terminate() {
		try {
			socket.close();
			log.println("Terminated the connection!");
			log.close();
		} catch (Exception ex) {
			log.println("Failed to terminate the connection:");
			ex.printStackTrace(log);
			log.println();
		}
	}
}
