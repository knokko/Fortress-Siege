package nl.knokko.client.texture;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import nl.knokko.util.Loader;
import nl.knokko.util.Maths;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public final class TextureLoader {
	
	private static List<Integer> textures = new ArrayList<Integer>();
	
	private static void bindTextureID(int textureID, byte[] data, int width, int height, boolean alpha){
		glTexImage2D(GL_TEXTURE_2D, 0, alpha ? GL_RGBA8 : GL_RGB8, width, height, 0, alpha ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)BufferUtils.createByteBuffer(data.length).put(data).flip());
	}
	
	private static void bindTextureID(int textureID, ByteBuffer data, int width, int height, boolean alpha){
		glTexImage2D(GL_TEXTURE_2D, 0, alpha ? GL_RGBA8 : GL_RGB8, width, height, 0, alpha ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, data);
	}
	
	private static int createTextureID(boolean linear){
		 int textureID = glGenTextures();
		 glBindTexture(GL_TEXTURE_2D, textureID);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, linear ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, linear ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		 textures.add(textureID);
		 return textureID;
	}
	
	private static int createTextureID(byte[] data, int width, int height, boolean alpha){
		 int textureID = createTextureID(false);
		 bindTextureID(textureID, data, width, height, alpha);
		 return textureID;
	}
	
	private static int createTextureID(ByteBuffer data, int width, int height, boolean alpha){
		 int textureID = createTextureID(false);
		 bindTextureID(textureID, data, width, height, alpha);
		 return textureID;
	}
	
	public static TextureBluePrint loadTextureBluePrint(String name){
		try {
			return new TextureBluePrint(Loader.loadInternalResource("textures/blueprint/" + name + ".tbp"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static SimpleTexture createSimpleTexture(byte[] data, int width, int height, boolean alpha){
		 return new SimpleTexture(createTextureID(data, width, height, alpha));
	}
	
	public static SizedTexture createSizedTexture(byte[] data, int width, int height, boolean alpha){
		if(width > Short.MAX_VALUE)
			throw new IllegalArgumentException("Width is too large: " + width);
		if(height > Short.MAX_VALUE)
			throw new IllegalArgumentException("Height is too large: " + height);
		return new SizedTexture(createTextureID(data, width, height, alpha), (short) width, (short) height);
	}
	
	private static int loadImage(BufferedImage image, boolean allowAlpha){
	    ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * (allowAlpha ? 4 : 3)); //4 for RGBA, 3 for RGB
	    for(int y = 0; y < image.getHeight(); y++){
	        for(int x = 0; x < image.getWidth(); x++){
	        	java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
	        	if(allowAlpha)
	        		color = new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), image.getAlphaRaster().getPixel(x, y, new int[1])[0]);
	            buffer.put((byte) color.getRed());
	            buffer.put((byte) color.getGreen());
	            buffer.put((byte) color.getBlue());
	            if(allowAlpha)
	            	buffer.put((byte) color.getAlpha());
	        }
	    }
	    buffer.flip();
	    return createTextureID(buffer, image.getWidth(), image.getHeight(), allowAlpha);
	}
	
	public static ITexture loadBufferedImage(BufferedImage image, boolean allowAlpha){
		if(Maths.powerOf2(image.getWidth()) && Maths.powerOf2(image.getHeight()))
			return new SimpleTexture(loadImage(image, allowAlpha));
		BufferedImage newImage = new BufferedImage(Maths.next2Power(image.getWidth()), Maths.next2Power(image.getHeight()), allowAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, newImage.getWidth(), newImage.getHeight(), null);
		g.dispose();
		return new SizedTexture(loadImage(newImage, allowAlpha), (short) image.getWidth(), (short) image.getHeight());
	}
	
	public static void clean(){
		for(int texture : textures)
			GL11.glDeleteTextures(texture);
	}
}
