package nl.knokko.server.chat;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.server.connection.UserSocketConnection;

public class ChatChannel {
	
	private List<UserSocketConnection> members;

	public ChatChannel() {
		members = new ArrayList<UserSocketConnection>();
	}
	
	public void broadcastMessage(String message){
		for(UserSocketConnection user : members)
			user.getSpeaker().sendChatMessage(message);
	}
	
	public void addMember(UserSocketConnection member){
		members.add(member);
		broadcastMessage(member.getData().getUsername() + " joined this channel.");
		for(int i = 0; i < 20; i++)
			broadcastMessage("spam " + i);
	}
	
	public boolean isMember(UserSocketConnection user){
		return members.contains(user);
	}
	
	public void removeMember(UserSocketConnection member){
		members.remove(member);
	}
}
