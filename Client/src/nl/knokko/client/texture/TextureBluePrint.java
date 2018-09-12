package nl.knokko.client.texture;

import java.io.IOException;
import java.io.InputStream;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitInputStream;
import nl.knokko.util.bits.BooleanArrayBitInput;
import nl.knokko.util.bits.BooleanArrayBitOutput;

import org.lwjgl.util.Color;

public class TextureBluePrint {
	
	private static final byte BIT_COUNT_TYPE = 3;
	private static final byte BIT_COUNT_SIZE = 10;
	
	private final byte[] data;
	
	private final short width;
	private final short height;

	public TextureBluePrint(short width, short height) {
		data = new byte[width * height];
		this.width = width;
		this.height = height;
	}
	
	public TextureBluePrint(InputStream input) throws IOException {
		BitInput buffer = new BitInputStream(input);
		byte bitCount = (byte) buffer.readNumber(BIT_COUNT_TYPE, false);
		width = (short) buffer.readNumber(BIT_COUNT_SIZE, false);
		height = (short) buffer.readNumber(BIT_COUNT_SIZE, false);
		data = new byte[width * height];
		for(int i = 0; i < data.length; i++)
			data[i] = (byte) buffer.readNumber(bitCount, false);
	}
	
	public TextureBluePrint(byte[] input){
		BitInput buffer = new BooleanArrayBitInput(input);
		byte bitCount = (byte) buffer.readNumber(BIT_COUNT_TYPE, false);
		width = (short) buffer.readNumber(BIT_COUNT_SIZE, false);
		height = (short) buffer.readNumber(BIT_COUNT_SIZE, false);
		data = new byte[width * height];
		for(int i = 0; i < data.length; i++)
			data[i] = (byte) buffer.readNumber(bitCount, false);
	}
	
	public byte[] save(){
		byte bitCount = 4;
		BooleanArrayBitOutput buffer = new BooleanArrayBitOutput(BIT_COUNT_TYPE + 2 * BIT_COUNT_SIZE + data.length * bitCount);
		buffer.addNumber(bitCount, BIT_COUNT_TYPE, false);
		buffer.addNumber(width, BIT_COUNT_SIZE, false);
		buffer.addNumber(height, BIT_COUNT_SIZE, false);
		for(byte b : data)
			buffer.addNumber(b, bitCount, false);
		return buffer.toBytes();
	}
	
	private byte[] createTextureData(boolean alpha, Color... colors){
		byte factor = alpha ? (byte) 4 : (byte) 3;
		byte[] texture = new byte[factor * data.length];
		for(int i = 0; i < data.length; i++){
			Color color = colors[data[i]];
			texture[i * factor] = color.getRedByte();
			texture[i * factor + 1] = color.getGreenByte();
			texture[i * factor + 2] = color.getBlueByte();
			if(alpha)
				texture[i * factor + 3] = color.getAlphaByte();
		}
		return texture;
	}
	
	public SimpleTexture createSimpleTexture(boolean alpha, Color... colors){
		return TextureLoader.createSimpleTexture(createTextureData(alpha, colors), width, height, alpha);
	}
	
	public SizedTexture createSizedTexture(boolean alpha, Color... colors){
		return TextureLoader.createSizedTexture(createTextureData(alpha, colors), width, height, alpha);
	}
	
	public void setPixel(int x, int y, byte pixel){
		data[x + y * width] = pixel;
	}
	
	public byte getPixel(int x, int y){
		return data[x + y * width];
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
