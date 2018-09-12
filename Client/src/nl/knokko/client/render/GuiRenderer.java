package nl.knokko.client.render;

import nl.knokko.client.model.Model;
import nl.knokko.client.model.ModelLoader;
import nl.knokko.client.shader.GuiShader;
import nl.knokko.client.texture.ITexture;
import nl.knokko.client.util.ClientMaths;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class GuiRenderer {
	
	private static final float[] QUADS = {-1,1, -1,-1, 1,1, 1,-1};
	
	private static final Model QUAD = ModelLoader.loadToVAO(QUADS);
	
	private static final GuiShader shader = new GuiShader();
	
	public static void start(){
		shader.start();
		GL30.glBindVertexArray(QUAD.getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public static void renderBackGround(Color color){
		GL11.glClearColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void renderTextures(Vector2f translation, Vector2f scale, ITexture... textures){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		Matrix4f matrix = ClientMaths.createTransformationMatrix(translation, scale);
		shader.loadTransformation(matrix);
		for(ITexture texture : textures){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		}
	}
	
	public static void stop(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public static void clean(){
		shader.clean();
	}
}
