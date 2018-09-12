package nl.knokko.mapdesigner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import nl.knokko.util.FileSearcher;
import nl.knokko.util.Sprite;
import nl.knokko.util.bits.BooleanArrayBitInput;
import nl.knokko.util.bits.BooleanArrayBitOutput;
import nl.knokko.world.tile.Tile;
import nl.knokko.world.tile.TileGrid;
import nl.knokko.world.tile.TileType;

public class MapDesigner extends JFrame implements KeyListener, MouseListener, MouseWheelListener {
	
	//private static final String[] NAME_MAP = {"air", "grass", "water", "rock"};
	//private static final Color[][] COLOR_MAP = {{}, {}, {}, {}};
	
	private static final byte AFTER_NAMING_NOTHING = 0;
	private static final byte AFTER_NAMING_SAVE = 1;
	
	private static final Font TILE_FONT = new Font("TimesRoman", 0, 30);
	
	static final int WIDTH = 1200;
	static final int HEIGHT = 800;
	
	static boolean showFileBar;
	static boolean showTileBar;
	
	private static final int SPEED = 15;
	
	private static final long serialVersionUID = -7386872495505553451L;
	
	private static final Image addtiles = getImage("addtiles");
	
	private static Map<Tile,Image> imageMap = new HashMap<Tile,Image>();
	
	static TileGrid tiles;
	static String name;
	
	private static byte afterNaming;
	static FileSearcher fileSearcher;
	
	private static BufferedImage image;
	static BufferedImage tilesImage;
	
	static MapDesigner frame;
	private static MapRenderer renderer;
	
	static Tile currentTile;
	
	private static int minCameraX;
	private static int minCameraY;
	
	private static int maxCameraX;
	private static int maxCameraY;
	
	static int tileScrolling;
	
	private static byte renderCounter = 15;
	
	private static boolean movingWest;
	private static boolean movingEast;
	private static boolean movingNorth;
	private static boolean movingSouth;
	
	private static boolean mousePressed;
	
	private static boolean changed;
	private static boolean stopping;
	static boolean choosingName;

	public static void main(String[] args) throws InterruptedException {
		init();//TODO save/load trouble
		while(!stopping){
			update();
			if(changed){
				renderer.repaint();
				changed = false;
			}
			Thread.sleep(20);
		}
	}
	
	private static void loadMap(File file){
		name = file.getName().substring(0, file.getName().length() - 4);
		try {
			tiles = TileGrid.loadInt(BooleanArrayBitInput.fromFile(file));
		} catch(Exception ex){
			throw new RuntimeException("Failed to load map " + name + ": " + ex.getLocalizedMessage(), ex);
		}
		currentTile = tiles.getTileSet().fromID(0);
		image = createImage();
		Graphics2D g = image.createGraphics();
		for(int x = 0; x < tiles.getWidth(); x++)
			for(int z = 0; z < tiles.getHeight(); z++)
				g.drawImage(getImage(tiles.getTile(x, z)), (x + 1) * 32, (z + 1) * 32, null);
		g.dispose();
		recreateTilesImage();
		change();
	}
	
	private static void newMap(){
		tiles = TileGrid.createInt(50, 50);
		tiles.getTileSet().add(new Tile("Barrier", TileType.BARRIER, loadTileImage("barrier")));
		tiles.getTileSet().add(new Tile("Void", TileType.VOID, loadTileImage("void")));
		tiles.getTileSet().add(new Tile("Air", TileType.AIR, loadTileImage("air")));
		tiles.getTileSet().add(new Tile("Water", TileType.WATER, loadTileImage("water")));
		tiles.getTileSet().add(new Tile("Rock", TileType.LAND, loadTileImage("rock")));
		tiles.getTileSet().add(new Tile("Grass", TileType.LAND, loadTileImage("grass")));
		currentTile = tiles.getTileSet().fromID(2);
		image = createImage();
		recreateTilesImage();
		change();
	}
	
	private static BufferedImage createImage(){
		BufferedImage buf = new BufferedImage((tiles.getWidth() + 2) * 32, (tiles.getHeight() + 2) * 32, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buf.createGraphics();
		for(int x = 0; x < tiles.getWidth(); x++){
			g.drawImage(addtiles, (x + 1) * 32, 0, null);
			g.drawImage(addtiles, (x + 1) * 32, (tiles.getHeight() + 1) * 32, null);
		}
		for(int z = 0; z < tiles.getHeight(); z++){
			g.drawImage(addtiles, 0, (z + 1) * 32, null);
			g.drawImage(addtiles, 32 * (tiles.getWidth() + 1), (z + 1) * 32, null);
		}
		g.dispose();
		return buf;
	}
	
	private static void recreateTilesImage(){
		int amount = tiles.getTileSet().getAmount();
		tilesImage = new BufferedImage(321, 50 + amount * 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tilesImage.createGraphics();
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, tilesImage.getWidth(), tilesImage.getHeight());
		g.setColor(Color.BLACK);
		g.setFont(TILE_FONT);
		g.drawLine(0, 0, tilesImage.getWidth() - 1, 0);
		g.drawLine(0, 50, tilesImage.getWidth() - 1, 50);
		g.drawString("Load tile...", 20, 40);
		for(int index = 0; index < amount; index++){
			Tile tile = tiles.getTileSet().fromID(index);
			g.drawLine(0, 100 + 50 * index, tilesImage.getWidth() - 1, 100 + 50 * index);
			g.drawString(tile.getName(), 20, 90 + 50 * index);
		}
		g.dispose();
	}
	
	private static void save(){
		new File("maps").mkdirs();
		BooleanArrayBitOutput output = new BooleanArrayBitOutput(tiles.getExpectedBitSize());
		tiles.save(output);
		try {
			output.save(new File("maps/" + name + ".map"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void init(){
		/*
		try {
			tiles = TileGrid.loadInt(new BitBuffer(new File("generated.map")));
		} catch(Exception ex){
			ex.printStackTrace();
			tiles = TileGrid.createInt(50, 50);
		}
		image = new BufferedImage(tiles.getWidth() * 32, tiles.getHeight() * 32, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		for(int x = 0; x < tiles.getWidth(); x++)
			for(int z = 0; z < tiles.getHeight(); z++)
				g.drawImage(getImage(tiles.getTile(x, z)), x * 32, z * 32, null);
		g.dispose();
		*/
		renderer = new MapRenderer();
		frame = new MapDesigner();
		maxCameraX = WIDTH - 1;
		maxCameraY = HEIGHT - 1;
	}
	
	private static void update(){
		if(movingWest){
			minCameraX -= SPEED;
			maxCameraX -= SPEED;
			change();
		}
		if(movingEast){
			minCameraX += SPEED;
			maxCameraX += SPEED;
			change();
		}
		if(movingNorth){
			minCameraY -= SPEED;
			maxCameraY -= SPEED;
			change();
		}
		if(movingSouth){
			minCameraY += SPEED;
			maxCameraY += SPEED;
			change();
		}
		if(mousePressed && currentTile != null && !choosingName && frame.getMousePosition().y > 100 && fileSearcher == null)
			preSetTile(mouseToTileX(frame.getMousePosition().x), mouseToTileY(frame.getMousePosition().y), false);
		if(fileSearcher != null){
			File selected = fileSearcher.getSelectedFile();
			if(selected != null){
				String ex = fileSearcher.getExtention();
				if(ex.equals(".map"))
					loadMap(selected);
				else if(ex.equals(".tile")){
					try {
						currentTile = new Tile(BooleanArrayBitInput.fromFile(selected));
						tiles.getTileSet().add(currentTile);
						recreateTilesImage();
						change();
					} catch (IOException e) {
						System.out.println("Failed to load tile " + selected.getName() + ": " + e.getLocalizedMessage());
					}
				}
				else
					throw new IllegalStateException("Invalid file searcher extention: " + ex);
				fileSearcher = null;
			}
		}
		if(renderCounter == 0){
			renderCounter = 50;
			change();
		}
		renderCounter--;
	}
	
	private static void preSetTile(int tileX, int tileZ, boolean allowResize){
		if(tileX >= 0 && tileX < tiles.getWidth() && tileZ >= 0 && tileZ < tiles.getHeight())
			setTile(currentTile, tileX, tileZ);
		else if(allowResize){
			if(tileX == -1){
				tiles.addLeftRow();
				BufferedImage newImage = createImage();
				Graphics2D g = newImage.createGraphics();
				g.drawImage(image, 64, 32, newImage.getWidth() - 32, newImage.getHeight() - 32, 32, 32, image.getWidth() - 32, image.getHeight() - 32, null);
				g.dispose();
				image = newImage;
				change();
			}
			else if(tileX == tiles.getWidth()){
				tiles.addRightRow();
				BufferedImage newImage = createImage();
				Graphics2D g = newImage.createGraphics();
				g.drawImage(image, 32, 32, newImage.getWidth() - 64, newImage.getHeight() - 32, 32, 32, image.getWidth() - 32, image.getHeight() - 32, null);
				g.dispose();
				image = newImage;
				change();
			}
			else if(tileZ == -1){
				tiles.addLowColumn();
				BufferedImage newImage = createImage();
				Graphics2D g = newImage.createGraphics();
				g.drawImage(image, 32, 64, newImage.getWidth() - 32, newImage.getHeight() - 32, 32, 32, image.getWidth() - 32, image.getHeight() - 32, null);
				g.dispose();
				image = newImage;
				change();
			}
			else if(tileZ == tiles.getHeight()){
				tiles.addHighColumn();
				BufferedImage newImage = createImage();
				Graphics2D g = newImage.createGraphics();
				g.drawImage(image, 32, 32, newImage.getWidth() - 32, newImage.getHeight() - 64, 32, 32, image.getWidth() - 32, image.getHeight() - 32, null);
				g.dispose();
				image = newImage;
				change();
			}
		}
	}
	
	public static BufferedImage getImage(){
		return image;
	}
	
	public static void setTile(Tile tile, int tileX, int tileZ){
		if(tiles.getTile(tileX, tileZ) == tile)
			return;
		tiles.setTile(tile, tileX, tileZ);
		Graphics2D g = image.createGraphics();
		g.drawImage(getImage(tile), (tileX + 1) * 32, (tileZ + 1) * 32, null);
		g.dispose();
		changed = true;
	}
	
	public static int getMinCameraX(){
		return minCameraX;
	}
	
	public static int getMinCameraY(){
		return minCameraY;
	}
	
	public static int getMaxCameraX(){
		return maxCameraX;
	}
	
	public static int getMaxCameraY(){
		return maxCameraY;
	}
	
	public static void change(){
		changed = true;
	}
	
	private static void afterNaming(){
		if(afterNaming == AFTER_NAMING_SAVE)
			save();
		choosingName = false;
		afterNaming = AFTER_NAMING_NOTHING;
	}
	
	private static Image getImage(Tile tile){
		//TextureBluePrint tbp = TextureLoader.loadTextureBluePrint("tiles/" + NAME_MAP[tile.getID() + 128]);
		//return getImage(tbp, COLOR_MAP[tile.getID() + 128]);
		Image image = imageMap.get(tile);
		if(image == null){
			Sprite sprite = tile.getSprite();
			image = sprite.createImage();
			imageMap.put(tile, image);
		}
		return image;
	}
	
	/*
	private static Image getImage(TextureBluePrint tbp, Color... colors){
		BufferedImage image = new BufferedImage(tbp.getWidth(), tbp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < image.getWidth(); x++)
			for(int z = 0; z < image.getHeight(); z++)
				image.setRGB(x, z, colors[tbp.getPixel(x, z)].getRGB());
		return image;
	}
	*/
	
	private MapDesigner(){
		add(renderer);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocation(400, 0);
		setTitle("Map Designer");
		setUndecorated(true);
		setVisible(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if(choosingName)
			return;
		int x = event.getX();
		int y = event.getY();
		if(fileSearcher != null){
			if(x > 100 && y > 100){
				fileSearcher.click(x - 100, y - 100);
				if(fileSearcher.hasChanged())
					change();
			}
			return;
		}
		if(y < 100){
			if(x > 0 && x < 158){
				showFileBar = !showFileBar;
				change();
			}
			else if(x > 158 && x < 479){
				showTileBar = !showTileBar;
				tileScrolling = 0;
				change();
			}
		}
		else {
			if(hasMap()){
				if(showFileBar && x < 158 && y < 280){
					if(y < 160)
						save();
					else if(y < 220){
						choosingName = true;
						afterNaming = AFTER_NAMING_SAVE;
					}
					else {
						tiles = null;
						name = null;
						image = null;
						currentTile = null;
						imageMap = new HashMap<Tile,Image>();
						minCameraX = 0;
						minCameraY = 0;
						maxCameraX = WIDTH - 1;
						maxCameraY = HEIGHT - 1;
					}
					showFileBar = false;
					change();
					return;
				}
				if(showTileBar && x >= 158 && x < 479){
					int ty = y - 100 + tileScrolling;
					int index = ty / 50;
					showTileBar = false;
					if(index == 0){
						File directory = new File("tiles").getAbsoluteFile();
						directory.mkdirs();
						fileSearcher = new FileSearcher("tile", directory);
						change();
						return;
					}
					index--;
					if(index >= 0 && index < tiles.getTileSet().getAmount())
						currentTile = tiles.getTileSet().fromID(index);
				}
				else {
					preSetTile(mouseToTileX(x), mouseToTileY(y), true);
				}
			}
			else {
				if(y < 100){
					showFileBar = false;
					change();
				}
				else if(showFileBar && x < 158 && y < 280){
					if(y < 160){
						choosingName = true;
						name = "Type your name here";
						newMap();
					}
					else if(y < 220){
						File directory = new File("maps").getAbsoluteFile();
						directory.mkdirs();
						fileSearcher = new FileSearcher("map", directory);
					}
					else {
						frame.dispose();
					}
					showFileBar = false;
					change();
				}
			}
		}
	}
	
	static int mouseToWorldX(int mouseX){
		return mouseX + minCameraX - 32;
	}
	
	static int worldToTileX(int worldX){
		if(worldX < 0)
			worldX -= 32;
		return worldX / 32;
	}
	
	static int mouseToTileX(int mouseX){
		return worldToTileX(mouseToWorldX(mouseX));
	}
	
	static int mouseToWorldY(int mouseY){
		return mouseY + minCameraY - 32;
	}
	
	static int worldToTileY(int worldY){
		if(worldY < 0)
			worldY -= 32;
		return worldY / 32;
	}
	
	static int mouseToTileY(int mouseY){
		return worldToTileY(mouseToWorldY(mouseY));
	}

	@Override
	public void mousePressed(MouseEvent event) {
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		mousePressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		change();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		change();
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if(choosingName){
			char key = event.getKeyChar();
			if(isValid(key)){
				name += key;
				changed = true;
			}
		}
	}
	
	private boolean isValid(char c){
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == ' ';
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		if(c == KeyEvent.VK_LEFT)
			movingWest = true;
		else if(c == KeyEvent.VK_RIGHT)
			movingEast = true;
		else if(c == KeyEvent.VK_UP)
			movingNorth = true;
		else if(c == KeyEvent.VK_DOWN)
			movingSouth = true;
		if(choosingName){
			if(c == KeyEvent.VK_BACK_SPACE && name.length() > 0){
				name = name.substring(0, name.length() - 1);
				changed = true;
			}
			if(c == KeyEvent.VK_ENTER && name.length() > 0){
				afterNaming();
				changed = true;
			}
			if(c == KeyEvent.VK_ESCAPE){
				//TODO add possibility to cancel name change
			}
		}
		if(fileSearcher != null && c == KeyEvent.VK_ESCAPE){
			fileSearcher = null;
			change();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int c = e.getKeyCode();
		if(c == KeyEvent.VK_LEFT)
			movingWest = false;
		else if(c == KeyEvent.VK_RIGHT)
			movingEast = false;
		else if(c == KeyEvent.VK_UP)
			movingNorth = false;
		else if(c == KeyEvent.VK_DOWN)
			movingSouth = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		if(fileSearcher != null){
			fileSearcher.scroll(event.getUnitsToScroll() * 5);
			fileSearcher.hasChanged();
			change();
			return;
		}
		if(showTileBar){
			tileScrolling += event.getUnitsToScroll() * 5;
			if(tileScrolling < 0)
				tileScrolling = 0;
			change();
			return;
		}
	}
	
	@Override
	public void dispose(){
		stopping = true;
		super.dispose();
	}
	
	public static boolean hasMap(){
		return tiles != null;
	}
	
	private static Sprite loadTileImage(String name){
		try {
			return new Sprite(ImageIO.read(MapDesigner.class.getClassLoader().getResource("sprites/mapdesigner/tiles/" + name + ".png")));
		} catch (IOException e) {
			System.out.println("Failed to load default tile " + name + ": " + e.getLocalizedMessage());
			frame.dispose();
			stopping = true;
			return null;
		}
	}
	
	private static Image getImage(String name){
		try {
			return ImageIO.read(MapRenderer.class.getClassLoader().getResource("sprites/mapdesigner/" + name + ".png"));
		} catch (IOException e) {
			throw new Error(e);
		}
	}
}
