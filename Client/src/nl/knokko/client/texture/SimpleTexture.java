package nl.knokko.client.texture;

class SimpleTexture implements ITexture {
	
	private final int textureID;

	public SimpleTexture(int textureID) {
		this.textureID = textureID;
	}

	@Override
	public int getTextureID() {
		return textureID;
	}
	
	@Override
	public String toString(){
		return "SimpleTexture(" + textureID + ")";
	}

	@Override
	public float getMinU() {
		return 0f;
	}

	@Override
	public float getMinV() {
		return 0f;
	}

	@Override
	public float getMaxU() {
		return 1f;
	}

	@Override
	public float getMaxV() {
		return 1f;
	}

	@Override
	public int getWidth() {
		throw new IllegalStateException("A SimpleTexture does not know it's width!");
	}

	@Override
	public int getHeight() {
		throw new IllegalStateException("A SimpleTexture does not know it's height!");
	}
}
