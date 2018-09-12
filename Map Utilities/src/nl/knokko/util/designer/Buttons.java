package nl.knokko.util.designer;

import java.awt.Graphics;
import java.util.Arrays;

public class Buttons {
	
	protected Button[] buttons;

	public Buttons(Button... buttons) {
		this.buttons = buttons;
	}
	
	public void paint(Graphics g){
		for(Button button : buttons)
			button.paint(g);
	}
	
	public void click(int x, int y){
		for(Button button : buttons)
			button.click(x, y);
	}
	
	public void type(char character){
		for(Button button : buttons)
			button.type(character);
	}
	
	public void press(int keycode){
		for(Button button : buttons)
			button.press(keycode);
	}
	
	public void clear(){
		buttons = new Button[0];
	}
	
	public void add(Button button){
		buttons = Arrays.copyOf(buttons, buttons.length + 1);
		buttons[buttons.length - 1] = button;
	}
	
	public Button get(int index){
		return buttons[index];
	}
}
