package nl.knokko.client.gui.button;

import nl.knokko.client.ClientScreen;
import nl.knokko.client.gui.Gui;
import nl.knokko.client.input.MouseInput;
import nl.knokko.client.state.ClientState;
import nl.knokko.client.texture.ITexture;

import org.lwjgl.util.vector.Vector2f;

public class LinkButton extends ActionButton {
	
	private Gui link;
	private ClientState state;

	public LinkButton(Vector2f translation, Vector2f scale, Gui link, ClientState state, ITexture... textures) {
		super(translation, scale, textures);
		this.link = link;
		this.state = state;
	}

	@Override
	public void click(float x, float y, int button) {
		state.setGui(link);
		MouseInput.setCooldown(ClientScreen.fps() / 2);
	}
}
