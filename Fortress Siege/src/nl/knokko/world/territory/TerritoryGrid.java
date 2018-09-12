package nl.knokko.world.territory;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class TerritoryGrid {
	
	private static final byte ENCODING_BYTE_ARRAY = -128;
	
	private byte[] data;
	
	private int width;
	private int height;

	public TerritoryGrid(int width, int height) {
		this.width = width;
		this.height = height;
		data = new byte[width * height];
	}
	
	public TerritoryGrid(BitInput input){
		byte encoding = input.readByte();
		if(encoding == ENCODING_BYTE_ARRAY)
			loadByteArray(input);
		else
			throw new IllegalArgumentException("Unknown encoding: " + encoding);
	}
	
	public void save(BitOutput output){
		saveByteArray(output);
	}
	
	private void saveByteArray(BitOutput output){
		output.addByte(ENCODING_BYTE_ARRAY);
		output.addInt(width);
		output.addInt(height);
		for(byte b : data)
			output.addByte(b);
	}
	
	private void loadByteArray(BitInput input){
		width = input.readInt();
		height = input.readInt();
		data = new byte[width * height];
		for(int i = 0; i < data.length; i++)
			data[i] = input.readByte();
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getDepth(){
		return height;
	}
	
	public Territory getTerritory(int tileX, int tileY){
		return new Territory(data[tileX + tileY * width]);
	}
	
	public void setTerritory(Territory territory, int tileX, int tileY){
		data[tileX + tileY * width] = territory.toByte();
	}
}