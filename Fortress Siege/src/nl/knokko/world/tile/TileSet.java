package nl.knokko.world.tile;

import java.util.Arrays;

import nl.knokko.util.Maths;
import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class TileSet {
	
	private static final byte ENCODING_TILE_ARRAY = -128;
	
	private Tile[] tiles;
	
	public TileSet(){
		tiles = new Tile[0];
	}
	
	public TileSet(Tile[] tiles){
		this.tiles = tiles;
	}
	
	public TileSet(BitInput input){
		byte encoding = input.readByte();
		if(encoding == ENCODING_TILE_ARRAY)
			loadTileArray(input);
		else
			throw new IllegalArgumentException("Unknown encoding: " + encoding);
	}
	
	private void loadTileArray(BitInput input){
		tiles = new Tile[input.readInt()];
		for(int id = 0; id < tiles.length; id++){
			Tile tile = new Tile(input);
			tile.setID(id);
			tiles[id] = tile;
		}
	}
	
	public void add(Tile tile){
		tiles = Arrays.copyOf(tiles, tiles.length + 1);
		tiles[tiles.length - 1] = tile;
		tile.setID(tiles.length - 1);
	}
	
	public void save(BitOutput output){
		saveTileArray(output);
	}
	
	private void saveTileArray(BitOutput output){
		output.addByte(ENCODING_TILE_ARRAY);
		output.addInt(tiles.length);
		for(Tile tile : tiles)
			tile.save(output);
	}
	
	public Tile fromID(int id){
		if(id < 0)
			throw new IllegalArgumentException("Id can't be smaller than 0, but this id is " + id);
		if(id >= tiles.length)
			throw new IllegalArgumentException("Id it too high (" + id + "), it can be " + (tiles.length - 1) + " at most.");
		return tiles[id];
	}
	
	public byte getBitCount(){
		return Maths.log2Up(tiles.length);
	}
	
	public boolean contains(Tile tile){
		try {
			return tile == fromID(tile.getID());
		} catch(IllegalArgumentException iae){
			return false;
		}
	}

	public int getAmount() {
		return tiles.length;
	}
}
