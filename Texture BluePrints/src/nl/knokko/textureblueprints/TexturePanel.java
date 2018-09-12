package nl.knokko.textureblueprints;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class TexturePanel extends JPanel {

	private static final long serialVersionUID = -1882639376841469752L;
	
	private static final Font FONT = new Font("TimesRoman", 0, 30);
	
	private final TextureDesigner designer;

	public TexturePanel(TextureDesigner designer) {
		this.designer = designer;
	}
	
	@Override
	public void paint(Graphics gr){
		if(designer.image != null){
			gr.setColor(Color.WHITE);
			gr.fillRect(0, 0, designer.getWidth(), designer.getHeight());
			gr.drawImage(designer.image, 0, 0, designer.image.getWidth() * designer.scale, designer.image.getHeight() * designer.scale, null);
		}
		else {
			gr.setColor(Color.BLUE);
			gr.fillRect(0, 0, designer.getWidth(), designer.getHeight());
		}
		for(Button button : designer.buttons){
			gr.setColor(Color.ORANGE);
			gr.fillRect(button.getMinX(), button.getMinY(), button.getWidth(), button.getHeight());
			gr.setColor(Color.BLACK);
			gr.drawRect(button.getMinX(), button.getMinY(), button.getWidth(), button.getHeight());
			gr.setFont(FONT);
			gr.drawString(button.getName(), button.getMinX() + button.getHeight() / 5, button.getMaxY() - button.getHeight() / 3);
		}
	}
}
