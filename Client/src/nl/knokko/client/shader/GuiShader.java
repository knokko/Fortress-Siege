package nl.knokko.client.shader;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends Shader {
    
    private static final String VERTEX_FILE = "shaders/gui.vsh";
    private static final String FRAGMENT_FILE = "shaders/gui.fsh";
     
    private int location_transformationMatrix;
 
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
