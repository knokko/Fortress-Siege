package nl.knokko.world.tile;

import nl.knokko.util.Sprite;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class Tile {
	
	private static final byte ENCODING_NAME_TYPE_SPRITE = -128;
	
	private String name;
	private TileType type;
	private Sprite sprite;
	
	private int id = -1;
	
	public Tile(String name, TileType type, Sprite sprite, int id){
		this.name = name;
		this.type = type;
		this.sprite = sprite;
		this.id = id;
	}

	public Tile(String name, TileType type, Sprite sprite) {
		this(name, type, sprite, -1);
	}
	
	public Tile(BitInput input){
		byte encoding = input.readByte();
		if(encoding == ENCODING_NAME_TYPE_SPRITE)
			loadNameTypeSprite(input);
		else
			throw new IllegalArgumentException("Unknown encoding: " + encoding);
	}
	
	private void loadNameTypeSprite(BitInput input){
		name = input.readJavaString();
		type = TileType.values()[(int) input.readNumber(TileType.BIT_COUNT, false)];
		sprite = new Sprite(input);
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public TileType getType(){
		return type;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public void setID(int id){
		if(id < 0)
			throw new IllegalArgumentException("ID must be greater than or equal to zero!");
		if(hasID())
			throw new IllegalStateException("The ID of this tile (" + name + ")has been set already!");
		this.id = id;
	}
	
	public int getID(){
		if(!hasID())
			throw new IllegalStateException("This tile does not have an ID!");
		return id;
	}
	
	public boolean hasID(){
		return id != -1;
	}
	
	public void save(BitOutput output){
		saveNameTypeSprite(output);
	}
	
	private void saveNameTypeSprite(BitOutput output){
		output.addByte(ENCODING_NAME_TYPE_SPRITE);
		output.addJavaString(name);
		output.addNumber(type.ordinal(), TileType.BIT_COUNT, false);
		sprite.save(output);
	}
}
