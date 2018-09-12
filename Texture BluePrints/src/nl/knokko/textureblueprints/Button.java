package nl.knokko.textureblueprints;

public class Button {
	
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private String name;

	public Button(String name, int minX, int minY, int maxX, int maxY) {
		this.name = name;
		this.x = minX;
		this.y = minY;
		this.width = maxX - minX;
		this.height = maxY - minY;
	}
	
	public String getName(){
		return name;
	}
	
	public int getMinX(){
		return x;
	}
	
	public int getMinY(){
		return y;
	}
	
	public int getMaxX(){
		return x + width;
	}
	
	public int getMaxY(){
		return y + height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public boolean isHit(int mouseX, int mouseY){
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public void setName(String newName){
		name = newName;
	}
}
