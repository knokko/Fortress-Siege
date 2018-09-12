package nl.knokko.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public class Sprite {
	
	private static final byte ENCODING_BYTE_ARRAY = -128;
	
	private int width;
	private int height;
	
	private byte[] data;
	
	public Sprite(int width, int height){
		data = new byte[4 * width * height];
		this.width = width;
		this.height = height;
	}
	
	public Sprite(BitInput input){
		byte encoding = input.readByte();
		if(encoding == ENCODING_BYTE_ARRAY)
			loadByteArray(input);
		else
			throw new IllegalArgumentException("Unknown encoding: " + encoding);
	}
	
	private void loadByteArray(BitInput input){
		width = input.readInt();
		height = input.readInt();
		data = new byte[4 * width * height];
		for(int i = 0; i < data.length; i++)
			data[i] = input.readByte();
	}
	
	public Sprite(BufferedImage source){
		width = source.getWidth();
		height = source.getHeight();
		data = new byte[4 * width * height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int rgba = source.getRGB(x, y);
				Color color = new Color(rgba);
				data[x * 4 + y * width * 4] = (byte) color.getRed();
				data[x * 4 + y * width * 4 + 1] = (byte) color.getGreen();
				data[x * 4 + y * width * 4 + 2] = (byte) color.getBlue();
				data[x * 4 + y * width * 4 + 3] = (byte) color.getAlpha();
			}
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public byte[] getData(){
		return data;
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

	public Image createImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				image.setRGB(x, y, getRGB(x, y));
			}
		}
		return image;
	}
	
	public int getRGB(int x, int y){
		return new Color(data[x * 4 + y * width * 4] & (0xff), data[x * 4 + y * width * 4 + 1] & (0xff), data[x * 4 + y * width * 4 + 2] & (0xff), data[x * 4 + y * width * 4 + 3] & (0xff)).getRGB();
	}
}