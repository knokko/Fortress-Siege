package nl.knokko.client.input;

public class KeyPressedEvent {
	
	private final int key;
	private final char character;

	public KeyPressedEvent(int key, char character) {
		this.key = key;
		this.character = character;
	}
	
	public int getKey(){
		return key;
	}
	
	public char getCharacter(){
		return character;
	}
}
