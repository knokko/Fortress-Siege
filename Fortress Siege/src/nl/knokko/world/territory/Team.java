package nl.knokko.world.territory;

import java.awt.Color;

public enum Team {
	
	BLUE(new Color(0, 0, 255)),
	RED(new Color(255, 0, 0)),
	GREEN(new Color(0, 255, 0)),
	BLACK(new Color(0, 0, 0)),
	WHITE(new Color(255, 255, 255)),
	YELLOW(new Color(255, 255, 0)),
	PINK(new Color(255, 0, 255)),
	CYAN(new Color(0, 255, 255));
	
	private final byte id;
	
	Team(Color color){
		id = (byte) ordinal();
	}
	
	public byte getID(){
		return id;
	}
}