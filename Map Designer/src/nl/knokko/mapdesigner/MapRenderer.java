package nl.knokko.mapdesigner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import nl.knokko.util.FileSearcher;

class MapRenderer extends JPanel {

	private static final long serialVersionUID = -4683880495234065740L;
	
	private static final Font NAME_FONT = new Font("Calibri", 0, 48);
	private static final Color BACKGROUND = Color.GREEN;
	
	private static Image upperbar = getImage("upperbar");
	private static Image fileBarOpened = getImage("toolbar/map/file");
	private static Image fileBarClosed = getImage("toolbar/nomap/file");
	
	@Override
	public void paint(Graphics g){
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, MapDesigner.frame.getWidth(), MapDesigner.frame.getHeight());
		g.drawImage(MapDesigner.getImage(), 0, 0, MapDesigner.frame.getWidth() - 1, MapDesigner.frame.getHeight() - 1, MapDesigner.getMinCameraX(), MapDesigner.getMinCameraY(), MapDesigner.getMaxCameraX(), MapDesigner.getMaxCameraY(), null);
		g.drawImage(upperbar, 0, 0, null);
		if(MapDesigner.showFileBar){
			if(!MapDesigner.hasMap())
				g.drawImage(fileBarClosed, 0, 100, null);
			else
				g.drawImage(fileBarOpened, 0, 100, null);
		}
		if(MapDesigner.showTileBar && MapDesigner.hasMap()){
			int maxScreenY = 100 + Math.min(MapDesigner.tilesImage.getHeight() - MapDesigner.tileScrolling, MapDesigner.frame.getHeight() - 100);
			g.drawImage(MapDesigner.tilesImage, 158, 100, 479, maxScreenY, 0, MapDesigner.tileScrolling, 321, Math.min(MapDesigner.tileScrolling + MapDesigner.frame.getHeight() - 111, MapDesigner.tilesImage.getHeight() - 1), null);
		}
		if(MapDesigner.hasMap()){
			g.setFont(NAME_FONT);
			g.setColor(Color.BLACK);
			g.drawString(MapDesigner.name, 600, 70);
			//g.setColor(Color.RED);
			//g.drawRect(MapDesigner.getMinCameraX() - 1, MapDesigner.getMinCameraY() - 1, MapDesigner.tiles.getWidth() * 32 + 2, MapDesigner.tiles.getHeight() * 32 + 2);
		}
		if(MapDesigner.choosingName){
			g.setColor(new Color(139, 139, 139));
			g.fillRect(200, 200, 600, 100);
			g.setColor(Color.BLACK);
			g.drawRect(200, 200, 600, 100);
			g.setFont(NAME_FONT);
			g.drawString(MapDesigner.name, 220, 270);
		}
		FileSearcher fs = MapDesigner.fileSearcher;
		if(fs != null)
			g.drawImage(fs.getImage(), 100, 100, 100 + fs.getWidth(), 100 + fs.getHeight(), fs.getMinX(), fs.getMinY(), fs.getMaxX(), fs.getMaxY(), null);
	}
	
	static Image getImage(String resource){
		return Toolkit.getDefaultToolkit().getImage(MapRenderer.class.getClassLoader().getResource("sprites/mapdesigner/" + resource + ".png"));
	}
}
