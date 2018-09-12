package nl.knokko.client.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public final class KeyInput {
	
	private static ArrayList<KeyPressedEvent> presses = new ArrayList<KeyPressedEvent>();
	private static ArrayList<KeyReleasedEvent> releases = new ArrayList<KeyReleasedEvent>();
	
	public static void update(){
		presses.clear();
		releases.clear();
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState())
				presses.add(new KeyPressedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
			else
				releases.add(new KeyReleasedEvent(Keyboard.getEventKey()));
		}
	}
	
	/**
	 * @return an ArrayList that contains all keys that were pressed during the current tick
	 */
	public static ArrayList<KeyPressedEvent> getCurrentPresses(){
		return presses;
	}
	
	/**
	 * @return an ArrayList that contains all keys that were released during the current tick
	 */
	public static ArrayList<KeyReleasedEvent> getCurrentReleases(){
		return releases;
	}
	
	public static boolean isKeydown(int key){
		return Keyboard.isKeyDown(key);
	}
}
