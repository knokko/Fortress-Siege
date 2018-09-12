package nl.knokko.server.connection;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import nl.knokko.server.Server;

public class ConnectionServer extends Thread {
	
	private ServerSocket socket;
	
	private List<UserSocketConnection> users = new ArrayList<UserSocketConnection>(2);
	
	@Override
	public void run(){
		try {
			socket = new ServerSocket(40538);
		} catch(Exception ex){
			Server.getConsole().getOutput().println("Failed to start the server connection:");
			ex.printStackTrace(Server.getConsole().getOutput());
			Server.getConsole().getOutput().println("Stopping server");
			Server.stop();
			return;
		}
		try {
			Server.getConsole().println("Opened connection with host address " + InetAddress.getLocalHost().getHostAddress() + " at port " + socket.getLocalPort());
		} catch (UnknownHostException e) {
			e.printStackTrace(Server.getConsole().getOutput());
		}
		while(!Server.isStopping()){
			try {
				UserSocketConnection user = new UserSocketConnection(socket.accept());
				users.add(user);
				Server.getConsole().println("Opened connection with user " + user.getSocket());
				user.start();
			} catch(Exception ex){
				Server.getConsole().getOutput().println("Failed to connect with a client socket: " + ex.getLocalizedMessage());
			}
		}
		Server.getConsole().println("Stopped looking for client connections");
	}
	
	public void removeConnection(UserSocketConnection connection){
		users.remove(connection);
	}
	
	public void close(){
		Server.getConsole().println("Closing all connections...");
		for(UserSocketConnection user : users)
			user.getSpeaker().closeKick();
		users.clear();
		Server.getConsole().println("Closed all connections; close server socket...");
		try {
			socket.close();
		} catch (Exception ex) {
			Server.getConsole().println("Failed to close server socket!");
			ex.printStackTrace(Server.getConsole().getOutput());
		}
		Server.getConsole().println("Stopped the server socket");
	}
}
