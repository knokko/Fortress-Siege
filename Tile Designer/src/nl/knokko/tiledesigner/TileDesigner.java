package nl.knokko.tiledesigner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import nl.knokko.util.FileSearcher;
import nl.knokko.util.Sprite;
import nl.knokko.util.bits.BooleanArrayBitOutput;
import nl.knokko.util.designer.Buttons;
import nl.knokko.util.designer.Button;
import nl.knokko.util.designer.Frame;
import nl.knokko.util.designer.TypingButton;
import nl.knokko.world.tile.Tile;
import nl.knokko.world.tile.TileType;

public class TileDesigner extends JFrame implements KeyListener, MouseListener, MouseWheelListener {

	private static final long serialVersionUID = 6561432126269806282L;
	
	private static final Frame MAIN_FRAME = new Frame(){
		
		private final Color BACKGROUND = new Color(200, 50, 50);
		private final Color BORDER = new Color(100, 0, 0);
		
		private final Color BUTTON = new Color(100, 200, 50);
		private final Color BUTTON_BORDER = new Color(50, 100, 0);
		private final Color BUTTON_TEXT = new Color(0, 50, 0);
		
		private final Font BUTTON_FONT = new Font("TimesRoman", 0, 45);
		
		private Buttons buttons = new Buttons(new Button(BUTTON, BUTTON_BORDER, BUTTON_TEXT, BUTTON_FONT, "Quit", 200, 100, 600, 170){
			
			@Override
			public void click(){
				frame.dispose();
			}
		}, new Button(BUTTON, BUTTON_BORDER, BUTTON_TEXT, BUTTON_FONT, "Create simple tile", 200, 250, 600, 320){

			@Override
			public void click() {
				subFrame = new SimpleTileFrame();
			}
		});

		@Override
		public void click(int x, int y) {
			buttons.click(x, y);
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(BACKGROUND);
			g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
			g.setColor(BORDER);
			g.drawRect(0, 0, frame.getWidth(), frame.getHeight());
			buttons.paint(g);
			if(mainMessage != null){
				g.setColor(Color.BLACK);
				g.drawString(mainMessage, 20, 70);
			}
		}

		@Override
		public void type(char character) {}

		@Override
		public void scroll(int pixels) {}

		@Override
		public void press(int key) {}

		@Override
		public void release(int key) {}
	};
	
	private static String mainMessage;
	
	static TileDesigner frame;
	static TileRenderer panel;
	
	static Frame subFrame;
	
	private static boolean stopping;
	
	public static void main(String[] args) throws InterruptedException {
		subFrame = MAIN_FRAME;
		panel = new TileRenderer();
		frame = new TileDesigner();
		while(shouldContinue()){
			panel.repaint();
			Thread.sleep(100);
		}
	}
	
	private static boolean shouldContinue(){
		return !stopping;
	}
	
	private TileDesigner(){
		super();
		add(panel);
		setSize(800, 600);
		setTitle("Tile Designer");
		setUndecorated(true);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}
	
	@Override
	public void dispose(){
		stopping = true;
		super.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		subFrame.click(event.getX(), event.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent event) {
		subFrame.type(event.getKeyChar());
	}

	@Override
	public void keyPressed(KeyEvent event) {
		subFrame.press(event.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent event) {
		subFrame.release(event.getKeyCode());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		subFrame.scroll(event.getUnitsToScroll() * 5);
	}
	
	private static class SimpleTileFrame implements Frame {
		
		private static final Color BACKGROUND = new Color(150, 150, 50);
		
		private static final Color BUTTON_COLOR = new Color(100, 200, 200);
		private static final Color TYPE_COLOR = new Color(200, 100, 100);
		
		private Button[] createTypeButtons(){
			final TileType[] values = TileType.values();
			Button[] array = new Button[values.length];
			for(int index = 0; index < array.length; index++)
				array[index] = new Button(TYPE_COLOR, values[index].name(), 400, 100 + 100 * index, 600, 150 + 100 * index){

					@Override
					public void click() {
						type = TileType.valueOf(text);
						showTypeButtons = false;
					}};
			return array;
		}
		
		private FileSearcher searcher;
		
		private Sprite image;
		private TileType type;
		
		private String error;
		
		private boolean showTypeButtons;
		
		private Buttons buttons = new Buttons(new Button(BUTTON_COLOR, "Back", 100, 100, 300, 150){

			@Override
			public void click() {
				subFrame = MAIN_FRAME;
			}
			
		}, new TypingButton(BUTTON_COLOR, "Type name", 100, 200, 300, 250), 
				new Button(BUTTON_COLOR, "Select type", 100, 300, 300, 350){

			@Override
			public void click() {
				showTypeButtons = true;
			}
			
		}, new Button(BUTTON_COLOR, "Select image", 100, 400, 300, 450){

			@Override
			public void click() {
				File directory = new File("images");
				directory.mkdirs();
				searcher = new FileSearcher("png", directory);
			}
		}, new Button(BUTTON_COLOR, "Create tile", 100, 500, 300, 550){

			@Override
			public void click() {
				error = null;
				String name = buttons.get(1).getText();
				if(name.startsWith(" "))
					error = "The name can't start with a whitespace character.";
				if(name.endsWith(" "))
					error = "The name can't end with a whitespace character.";
				for(int i = 0; i < name.length(); i++){
					char c = name.charAt(i);
					if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == ' ')){
						error = "Name contains the invalid character " + c + " (this is the " + (i + 1) + "th character";
						break;
					}
				}
				if(image == null)
					error = "You need to select an image.";
				else if(image.getWidth() != 32 || image.getHeight() != 32){
					error = "The image must be 32 x 32 pixels";
					image = null;
				}
				if(type == null)
					error = "You need to select a tile type.";
				if(error == null){
					try {
						Tile tile = new Tile(name, type, image);
						BooleanArrayBitOutput output = new BooleanArrayBitOutput();
						tile.save(output);
						new File("tiles").mkdirs();
						output.save(new File("tiles/" + name + ".tile"));
						mainMessage = "Your tile has been saved in the tiles folder.";
						subFrame = MAIN_FRAME;
					} catch(Exception ex){
						error = "Failed to create the tile: " + ex.getLocalizedMessage();
					}
				}
			}
		});
		
		private Buttons typeButtons = new Buttons(createTypeButtons());

		@Override
		public void click(int x, int y) {
			if(searcher == null){
				buttons.click(x, y);
				if(showTypeButtons)
					typeButtons.click(x, y);
			}
			else
				searcher.click(x - 100, y - 100);
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(BACKGROUND);
			g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, frame.getWidth(), frame.getHeight());
			buttons.paint(g);
			if(showTypeButtons)
				typeButtons.paint(g);
			else {
				g.setColor(Color.BLACK);
				if(image != null)
					g.drawString("Done", 350, 440);
				if(type != null)
					g.drawString(type.name(), 350, 340);
			}
			if(searcher != null){
				g.drawImage(searcher.getImage(), 100, 100, 100 + searcher.getWidth(), 100 + searcher.getHeight(), searcher.getMinX(), searcher.getMinY(), searcher.getMaxX(), searcher.getMaxY(), null);
				File file = searcher.getSelectedFile();
				if(file != null){
					searcher = null;
					try {
						BufferedImage buf = ImageIO.read(file);
						image = new Sprite(buf);
						if(image.getWidth() != 32 || image.getHeight() != 32){
							error = "The image must be 32 x 32 pixels";
							image = null;
						}
					} catch(Exception ex){
						error = "Failed to load image: " + ex.getLocalizedMessage();
					}
				}
			}
			if(error != null){
				g.setColor(Color.RED);
				g.drawString(error, 20, 100);
			}
		}

		@Override
		public void type(char character) {
			if(searcher == null)
				buttons.type(character);
		}

		@Override
		public void press(int key) {
			if(searcher == null)
				buttons.press(key);
			else if(key == KeyEvent.VK_ESCAPE)
				searcher = null;
		}

		@Override
		public void release(int key) {}

		@Override
		public void scroll(int pixels) {
			if(searcher != null)
				searcher.scroll(pixels);
		}
	}
}
