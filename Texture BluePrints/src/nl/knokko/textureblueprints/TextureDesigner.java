package nl.knokko.textureblueprints;

import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import javax.swing.JFrame;

import nl.knokko.client.texture.TextureBluePrint;
import nl.knokko.util.Compressor;

public class TextureDesigner extends JFrame implements KeyListener, MouseListener, MouseWheelListener {
	
	private static final TextureDesigner INSTANCE = new TextureDesigner();

	private static final long serialVersionUID = -2866885585294178540L;
	
	private static final Color[] DEFINITIONS = {new Color(70, 70, 70), new Color(63, 63, 63), new Color(66, 66, 66), new Color(72, 72, 72), new Color(62, 62, 62), new Color(80, 80, 80), new Color(58, 58, 58), new Color(50, 48, 63)};
	
	private static final Button[] START_BUTTONS = {new Button("quit", 100, 50, 200, 100), new Button("width: 16", 150, 200, 300, 250), new Button("decrease width", 400, 200, 590, 250), new Button("increase width", 600, 200, 790, 250), new Button("height: 16", 150, 300, 300, 350), new Button("decrease height", 400, 300, 590, 350), new Button("increase height", 600, 300, 790, 350), new Button("next", 100, 400, 200, 440), new Button("load", 250, 400, 350, 440)};
	private static final Button[] TEXTURE_BUTTONS = {new Button("quit", 650, 350, 750, 400), new Button("save", 500, 350, 600, 400)};
	
	private final TexturePanel panel = new TexturePanel(this);
	
	Button[] buttons;
	
	private TextureBluePrint texture;
	
	BufferedImage image;
	
	int scale = 10;
	
	int width = 16;
	int height = 16;
	
	private byte currentColor = 1;

	public TextureDesigner(){}

	public static void main(String[] args) {
		INSTANCE.init();
		INSTANCE.run();
	}
	
	public void init(){
		buttons = START_BUTTONS;
		add(panel);
		setUndecorated(true);
		setLocation(400, 200);
		setSize(800, 500);
		setTitle("Texture Designer");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}
	
	public void run(){
		try {
			while(true){
				Thread.sleep(1000);
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void save(){
		if(texture != null){
			try {
				FileOutputStream output = new FileOutputStream("generated.tbp");
				output.write(Compressor.compress(texture.save()));
				output.close();
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void buttonClicked(Button button){
		if(button.getName().equals("quit"))
			dispose();
		else if(button.getName().equals("save"))
			save();
		else if(button.getName().equals("increase width")){
			if(width < 511)
				width++;
			buttons[1].setName("width: " + width);
			panel.repaint();
		}
		else if(button.getName().equals("decrease width")){
			if(width > 1)
				width--;
			buttons[1].setName("width: " + width);
			panel.repaint();
		}
		else if(button.getName().equals("increase height")){
			if(height < 511)
				height++;
			buttons[4].setName("height: " + height);
			panel.repaint();
		}
		else if(button.getName().equals("decrease height")){
			if(height > 1)
				height--;
			buttons[4].setName("height: " + height);
			panel.repaint();
		}
		else if(button.getName().equals("next")){
			texture = new TextureBluePrint((short) width, (short) height);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			buttons = TEXTURE_BUTTONS;
		}
		else if(button.getName().equals("load")){
			try {
				texture = new TextureBluePrint(Compressor.decompress(Files.readAllBytes(new File("generated.tbp").toPath())));
			} catch(Exception ex){
				ex.printStackTrace();
			}
			image = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_RGB);
			for(int x = 0; x < texture.getWidth(); x++)
				for(int y = 0; y < texture.getHeight(); y++)
					image.setRGB(x, y, DEFINITIONS[texture.getPixel(x, y)].getRGB());
			buttons = TEXTURE_BUTTONS;
		}
		panel.repaint();
	}
	
	@Override
	public void dispose(){
		save();
		System.exit(0);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event){
		if(texture != null){
			scale -= event.getUnitsToScroll();
			if(scale < 1)
				scale = 1;
			panel.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent event){
		int x = event.getX();
		int y = event.getY();
		for(Button button : buttons){
			if(button.isHit(x, y)){
				buttonClicked(button);
				return;
			}
		}
		if(texture != null){
			x /= scale;
			y /= scale;
			if(x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()){
				image.setRGB(x, y, DEFINITIONS[currentColor].getRGB());
				texture.setPixel(x, y, currentColor);
				panel.repaint();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){
		panel.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e){}

	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void keyPressed(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.VK_0)
			currentColor = 0;
		if(event.getKeyCode() == KeyEvent.VK_1)
			currentColor = 1;
		if(event.getKeyCode() == KeyEvent.VK_2)
			currentColor = 2;
		if(event.getKeyCode() == KeyEvent.VK_3)
			currentColor = 3;
		if(event.getKeyCode() == KeyEvent.VK_4)
			currentColor = 4;
		if(event.getKeyCode() == KeyEvent.VK_5)
			currentColor = 5;
		if(event.getKeyCode() == KeyEvent.VK_6)
			currentColor = 6;
		if(event.getKeyCode() == KeyEvent.VK_7)
			currentColor = 7;
	}

	@Override
	public void keyReleased(KeyEvent e){}
}
