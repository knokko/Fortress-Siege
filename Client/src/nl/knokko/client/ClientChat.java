package nl.knokko.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import nl.knokko.client.connection.ConnectionSpeaker;
import nl.knokko.client.input.KeyInput;
import nl.knokko.client.input.KeyPressedEvent;
import nl.knokko.client.render.GuiRenderer;
import nl.knokko.client.texture.ITexture;
import nl.knokko.client.texture.TextureCreator;

public class ClientChat {
	
	/**
	 * messages.get(0) returns the oldest message
	 * messages.get(messages.size() - 1) returns the newest message
	 */
	private List<String> messages;
	private List<String> messagesToAdd;
	private Map<TextViewer, List<ITexture>> textures;
	
	private String currentMessage;
	private ITexture currentMessageTexture;
	
	private boolean recreateCurrentMessage;
	private boolean enabled;
	
	public ClientChat(){
		messages = new ArrayList<String>();
		messagesToAdd = new ArrayList<String>(1);
		textures = new HashMap<TextViewer, List<ITexture>>();
		currentMessage = "";
		recreateCurrentMessage = true;
	}
	
	private static final int SPLIT_LENGTH = 35;
	
	public void receiveMessage(String message){
		try {
			addMessage(message);
		} catch(Exception ex){
			messagesToAdd.add(message);
		}
	}
	
	private void addMessage(String completeMessage){
		int i = 0;
		while(i < completeMessage.length()){
			int rawIndex = Math.min(i + SPLIT_LENGTH, completeMessage.length());
			String firstSub = completeMessage.substring(i, rawIndex);
			int preferredIndex = firstSub.lastIndexOf(" ");
			String message;
			if(preferredIndex != -1 && firstSub.length() == SPLIT_LENGTH)
				message = completeMessage.substring(i, i + preferredIndex);
			else
				message = firstSub;
			messages.add(message);
			Iterator<Entry<TextViewer, List<ITexture>>> it = textures.entrySet().iterator();
			while(it.hasNext()){
				Entry<TextViewer, List<ITexture>> entry = it.next();
				entry.getValue().add(TextureCreator.createChatTexture(message, entry.getKey().getWidth(), entry.getKey().getHeight(), entry.getKey().getColor()));
			}
			i += message.length() + 1;
		}
	}
	
	private List<ITexture> getMessageTextures(TextViewer view){
		List<ITexture> list = textures.get(view);
		if(list == null){
			list = new ArrayList<ITexture>(messages.size());
			for(String message : messages)
				list.add(TextureCreator.createChatTexture(message, view.getWidth(), view.getHeight(), view.getColor()));
			textures.put(view, list);
		}
		return list;
	}
	
	private void updateCurrentMessage(){
		if(isEnabled()){
			List<KeyPressedEvent> events = KeyInput.getCurrentPresses();
			for(KeyPressedEvent event : events){
				if((int) event.getCharacter() != 0){
					if(event.getKey() == Keyboard.KEY_BACK || event.getKey() == Keyboard.KEY_DELETE)
						removeCharacter();
					else if(event.getKey() == Keyboard.KEY_RETURN)
						sendCurrentMessage();
					else
						addCharacter(event.getCharacter());
				}
			}
		}
	}
	
	public void render(Vector2f min, Vector2f max, Color color, int amount){
		updateCurrentMessage();
		for(String message : messagesToAdd)
			addMessage(message);
		messagesToAdd.clear();
		int width = (int) ((max.x - min.x) * ClientScreen.getWidth() / 2);
		float midX = (max.x + min.x) / 2;
		int height = (int) ((max.y - min.y) * 0.5 * ClientScreen.getHeight() / (amount + 1));
		Vector2f scale = new Vector2f((max.x - min.x) / 2, ((max.y - min.y) / (amount + 1) / 2));
		TextViewer view = new TextViewer(color, width, height);
		List<ITexture> list = getMessageTextures(view);
		for(int i = 0; i < amount && i < list.size(); i++)
			GuiRenderer.renderTextures(new Vector2f(midX, min.y + i * scale.y * 2 + scale.y * 3), scale, list.get(list.size() - i - 1));
		createCurrentMessage(width, height);
		GuiRenderer.renderTextures(new Vector2f(midX, min.y + scale.y), scale, currentMessageTexture);
	}
	
	private void createCurrentMessage(int width, int height){
		if(recreateCurrentMessage){
			currentMessageTexture = TextureCreator.createChatTexture(currentMessage, width, height, Color.GREEN);
			recreateCurrentMessage = false;
		}
	}
	
	private void addCharacter(char c){
		currentMessage += c;
		recreateCurrentMessage = true;
	}
	
	private void removeCharacter(){
		if(currentMessage.length() > 0){
			currentMessage = currentMessage.substring(0, currentMessage.length() - 1);
			recreateCurrentMessage = true;
		}
	}
	
	private void setCurrentMessage(String message){
		currentMessage = message;
		recreateCurrentMessage = true;
	}
	
	private void sendCurrentMessage(){
		try {
			ConnectionSpeaker.verifyMessage(currentMessage);
			ConnectionSpeaker.sendChatMessage(currentMessage);
			currentMessage = "";
			recreateCurrentMessage = true;
		} catch(Exception ex){
			setCurrentMessage(ex.getLocalizedMessage());
		}
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void enable(){
		enabled = true;
	}
	
	public void disable(){
		enabled = false;
	}
	
	public static class TextViewer {
		
		private final int width;
		private final int height;
		
		private final Color color;
		
		public TextViewer(Color color, int width, int height){
			this.color = color;
			this.width = width;
			this.height = height;
		}
		
		@Override
		public boolean equals(Object other){
			if(other instanceof TextViewer){
				TextViewer v = (TextViewer) other;
				return v.color.equals(color) && v.width == width && v.height == height;
			}
			return false;
		}
		
		@Override
		public int hashCode(){
			return width * height * color.getRGB();
			
		}
		
		public Color getColor(){
			return color;
		}
		
		public int getWidth(){
			return width;
		}
		
		public int getHeight(){
			return height;
		}
	}
}
