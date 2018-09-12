package nl.knokko.server.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.knokko.server.connection.LoginException;
import nl.knokko.server.connection.RegisterException;
import nl.knokko.util.ConnectionCode;
import nl.knokko.util.Loader;

public class AccountManager {
	
	private List<UserData> onlineUsers;
	
	public AccountManager(){
		onlineUsers = new ArrayList<UserData>(2);
	}
	
	public UserData login(String username, String password) throws LoginException {
		if(isOnline(username))
			throw new LoginException(ConnectionCode.LE_ALREADY_ONLINE);
		UserData data = loadUserData(username);
		if(data == null)
			throw new LoginException(ConnectionCode.LE_NO_USERNAME);
		if(!data.getPassword().equals(password))
			throw new LoginException(ConnectionCode.LE_WRONG_PASSWORD);
		onlineUsers.add(data);
		return data;
	}
	
	public UserData register(String username, String password) throws RegisterException {
		if(isOnline(username) || loadUserData(username) != null)
			throw new RegisterException(ConnectionCode.RE_NAME_NOT_AVAILABLE);
		UserData data = new UserData(username, password);
		onlineUsers.add(data);
		return data;
	}
	
	public void logout(UserData data){
		if(onlineUsers.remove(data))
			data.save(getPath(data.getUsername()));
	}
	
	public void finish(){
		for(UserData data : onlineUsers)
			data.save(getPath(data.getUsername()));
	}
	
	private boolean isOnline(String name){
		for(UserData user : onlineUsers)
			if(name.equals(user.getUsername()))
				return true;
		return false;
	}
	
	private UserData loadUserData(String name){
		File file = getPath(name);
		if(file != null && file.exists())
			return UserData.fromFile(file, name);
		return null;
	}
	
	private File getPath(String name){
		Loader.getRelativeFile("server data").mkdir();
		Loader.getRelativeFile("server data", "users").mkdir();
		StringBuilder builder = new StringBuilder(name.length() * 3);
		for(int i = 0; i < name.length(); i++)
			builder.append((int) name.charAt(i) + " ");
		builder.deleteCharAt(builder.length() - 1);
		return Loader.getRelativeFile("server data", "users", builder.toString() + ".user");
	}
}
