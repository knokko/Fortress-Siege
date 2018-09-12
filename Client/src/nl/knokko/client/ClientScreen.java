package nl.knokko.client;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class ClientScreen {
	
	public static void openScreen(){
		try {
			ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
			int size = getSize();
			Display.setDisplayMode(new DisplayMode(size, size));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Fortress Siege");
			Display.setVSyncEnabled(true);
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void updateScreen(){
		Display.sync(fps());
		Display.update();
	}
	
	public static void closeScreen(){
		Display.destroy();
	}
	
	public static int fps(){
		return 64;
	}
	
	public static int getWidth(){
		return Display.getWidth();
	}
	
	public static int getHeight(){
		return Display.getHeight();
	}
	
	public static boolean closeRequested(){
		return Display.isCloseRequested();
	}
	
	private static int getSize(){
		return 800;
	}
}
