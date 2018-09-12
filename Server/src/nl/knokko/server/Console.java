package nl.knokko.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Console extends Thread {
	
	private final PrintStream out;
	private final BufferedReader in;

	public Console() {
		out = System.out;
		in = new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Override
	public void run(){
		while(!Server.isStopping()){
			try {
				executeCommand(in.readLine());
			} catch (IOException e) {
				e.printStackTrace(out);
			}
		}
	}
	
	public void executeCommand(String command){
		executeCommand(command.split(" "));
	}
	
	public void executeCommand(String[] args){
		if(args[0].equals("stop") || args[0].equals("quit")){
			out.println("Stopping server");
			Server.stop();
			return;
		}
		if(args[0].equals("terminate")){
			out.println("Terminating server!");
			System.exit(0);
		}
	}
	
	public PrintStream getOutput(){
		return out;
	}
	
	public void println(String message){
		out.println(message);
	}
}
