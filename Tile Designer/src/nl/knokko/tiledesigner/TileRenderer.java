package nl.knokko.tiledesigner;

import java.awt.Graphics;

import javax.swing.JPanel;

class TileRenderer extends JPanel {

	private static final long serialVersionUID = 1755690171024359153L;

	public TileRenderer() {}
	
	@Override
	public void paint(Graphics g){
		TileDesigner.subFrame.paint(g);
	}
}