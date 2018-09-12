package nl.knokko.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearcher {
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 400;
	
	public static final Color BACKGROUND = new Color(230, 250, 40);
	
	public static final Font FONT = new Font("TimesRoman", 0, 30);
	
	private final String extention;
	
	private File directory;
	private File[] files;
	private File selectedFile;
	
	private BufferedImage image;
	
	private int scroll;
	
	private boolean hasChanged;
	
	public FileSearcher(String extention, File startDirectory){
		this.extention = "." + extention;
		directory = startDirectory.getAbsoluteFile();
		recreateImage();
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public File getSelectedFile(){
		return selectedFile;
	}
	
	public String getExtention(){
		return extention;
	}
	
	public int getMinX(){
		return 0;
	}
	
	public int getMinY(){
		return scroll;
	}
	
	public int getMaxX(){
		return WIDTH - 1;
	}
	
	public int getMaxY(){
		return Math.min(scroll + HEIGHT - 1, image.getHeight() - 1);
	}
	
	public int getWidth(){
		return WIDTH;
	}
	
	public int getHeight(){
		return Math.min(HEIGHT, image.getHeight() - scroll);//maybe + 1 or -1
	}
	
	public void click(int x, int y){
		y += scroll;
		if(y < 70){
			File parent = directory.getParentFile();
			if(parent != null){
				directory = parent;
				recreateImage();
			}
		}
		else {
			y -= 70;
			int index = y / 50;
			if(index < files.length){
				File selected = files[index];
				if(selected.isDirectory()){
					directory = selected;
					recreateImage();
				}
				else
					selectedFile = selected;
			}
		}
	}
	
	public void scroll(int pixels){
		scroll += pixels;
		if(scroll < 0)
			scroll = 0;
		if(scroll + 70 > image.getHeight())
			scroll = image.getHeight() - 70;
		hasChanged = true;
	}
	
	public boolean hasChanged(){
		if(hasChanged){
			hasChanged = false;
			return true;
		}
		return false;
	}
	
	private void recreateImage(){
		scroll = 0;
		files = directory.listFiles();
		if(files == null)
			throw new IllegalArgumentException("Invalid directory: " + directory.getAbsolutePath());
		List<File> list = new ArrayList<File>(files.length);
		for(File file : files)
			if(file.isDirectory() || file.getName().endsWith(extention))
				list.add(file);
		files = new File[list.size()];
		list.toArray(files);
		image = new BufferedImage(WIDTH, 71 + files.length * 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, WIDTH, image.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, WIDTH, image.getHeight());
		g.drawLine(0, 70, WIDTH - 1, 70);
		g.setFont(FONT);
		g.drawString("To parent directory", 20, 50);
		int y = 70;
		for(File file : files){
			g.drawString(file.getName(), 20, y + 40);
			y += 50;
			g.drawLine(0, y, WIDTH - 1, y);
		}
		g.dispose();
		hasChanged = true;
	}
}
