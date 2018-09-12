package nl.knokko.world.tile;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public abstract class TileGrid {
	
	public static TileGrid createInt(int width, int height){
		TileGridInt grid = new TileGridInt(new TileSet());
		grid.width = width;
		grid.height = height;
		grid.data = new int[width * height];
		return grid;
	}
	
	public static TileGrid loadInt(BitInput input){
		TileSet set = new TileSet(input);
		TileGridInt grid = new TileGridInt(set);
		grid.width = input.readInt();
		grid.height = input.readInt();
		grid.loadData(input);
		return grid;
	}
	
	public static TileGrid loadBest(BitInput input){
		TileSet set = new TileSet(input);
		byte bitCount = set.getBitCount();
		TileGrid grid;
		//maybe add TileGridShort
		if(bitCount <= 8)
			grid = new TileGridByte(set);
		else
			grid = new TileGridInt(set);
		grid.width = input.readInt();
		grid.height = input.readInt();
		grid.loadData(input);
		return grid;
	}
	
	final TileSet tileSet;
	
	int width;
	int height;
	
	private TileGrid(TileSet set){
		tileSet = set;
	}
	
	public TileSet getTileSet(){
		return tileSet;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public final void save(BitOutput output){
		tileSet.save(output);
		output.addInt(width);
		output.addInt(height);
		saveData(output);
	}
	
	public abstract Tile getTile(int x, int y);
	
	public TileType getTileType(int x, int y){
		return getTile(x, y).getType();
	}
	
	public abstract void setTile(Tile tile, int x, int y);
	
	public void addLeftRow(){
		int newWidth = width + 1;
		Object data = getData();
		Object newData = createData(newWidth, height);
		for(int z = 0; z < height; z++)
			System.arraycopy(data, z * width, newData, z * newWidth + 1, width);
		width = newWidth;
		setData(newData);
	}
	
	public void addRightRow(){
		int newWidth = width + 1;
		Object data = getData();
		Object newData = createData(newWidth, height);
		for(int z = 0; z < height; z++)
			System.arraycopy(data, z * width, newData, z * newWidth, width);
		width = newWidth;
		setData(newData);
	}
	
	public void addLowColumn(){
		int newHeight = height + 1;
		Object data = getData();
		Object newData = createData(width, newHeight);
		System.arraycopy(data, 0, newData, width, width * height);
		height = newHeight;
		setData(newData);
	}
	
	public void addHighColumn(){
		int newHeight = height + 1;
		Object data = getData();
		Object newData = createData(width, newHeight);
		System.arraycopy(data, 0, newData, 0, width * height);
		height = newHeight;
		setData(newData);
	}
	
	abstract Object getData();
	
	abstract Object createData(int width, int height);
	
	abstract void setData(Object newData);
	
	abstract void saveData(BitOutput output);
	
	abstract void loadData(BitInput input);
	
	public abstract int getExpectedBitSize();
	
	private static class TileGridByte extends TileGrid {
		
		private static final byte ENCODING_ARRAY_BITCOUNT = -128;
		
		private byte[] data;
		
		private TileGridByte(TileSet set){
			super(set);
		}

		@Override
		public Tile getTile(int x, int y) {
			return tileSet.fromID(data[x + y * width]);
		}

		@Override
		public void setTile(Tile tile, int x, int y) {
			int id = tile.getID();
			if(id > 255 || id < 0)
				throw new IllegalArgumentException("Id is out of bounds (" + id + ")");
			data[x + y * width] = (byte) id;
		}

		@Override
		void saveData(BitOutput output) {
			saveArrayBitcount(output);
		}
		
		private void saveArrayBitcount(BitOutput output){
			output.addByte(ENCODING_ARRAY_BITCOUNT);
			byte bitCount = tileSet.getBitCount();
			for(byte b : data)
				output.addNumber(b & (0xff), bitCount, false);
		}

		@Override
		void loadData(BitInput input) {
			byte encoding = input.readByte();
			if(encoding == ENCODING_ARRAY_BITCOUNT)
				loadArrayBitcount(input);
			else
				throw new IllegalArgumentException("Unknown encoding: " + encoding);
		}
		
		private void loadArrayBitcount(BitInput input){
			byte bitCount = tileSet.getBitCount();
			if(bitCount > 8)
				throw new IllegalArgumentException("This tile grid doesn't support a bit count that is greater than 8!");
			data = new byte[width * height];
			for(int i = 0; i < data.length; i++)
				data[i] = (byte) input.readNumber(bitCount, false);
		}

		@Override
		public int getExpectedBitSize() {
			return width * height * 8 + 1000;
		}

		@Override
		Object getData() {
			return data;
		}

		@Override
		Object createData(int width, int height) {
			return new byte[width * height];
		}

		@Override
		void setData(Object newData) {
			data = (byte[]) newData;
		}
	}
	
	private static class TileGridInt extends TileGrid {
		
		private static final byte ENCODING_ARRAY_BITCOUNT = -128;
		
		private int[] data;
		
		private TileGridInt(TileSet set){
			super(set);
		}

		@Override
		public Tile getTile(int x, int y) {
			return tileSet.fromID(data[x + y * width]);
		}

		@Override
		public void setTile(Tile tile, int x, int y) {
			data[x + y * width] = tile.getID();
		}

		@Override
		void saveData(BitOutput output) {
			saveArrayBitcount(output);
		}
		
		private void saveArrayBitcount(BitOutput output){
			output.addByte(ENCODING_ARRAY_BITCOUNT);
			byte bitCount = tileSet.getBitCount();
			for(int i : data)
				output.addNumber(i, bitCount, false);
		}

		@Override
		void loadData(BitInput input) {
			byte encoding = input.readByte();
			if(encoding == ENCODING_ARRAY_BITCOUNT)
				loadArrayBitcount(input);
			else
				throw new IllegalArgumentException("Unknown encoding: " + encoding);
		}
		
		private void loadArrayBitcount(BitInput input){
			data = new int[width * height];
			byte bitCount = tileSet.getBitCount();
			for(int i = 0; i < data.length; i++)
				data[i] = (int) input.readNumber(bitCount, false);
		}

		@Override
		public int getExpectedBitSize() {
			return width * height * 32 + 1000;
		}

		@Override
		Object getData() {
			return data;
		}

		@Override
		Object createData(int width, int height) {
			return new int[width * height];
		}

		@Override
		void setData(Object newData) {
			data = (int[]) newData;
		}
	}
}