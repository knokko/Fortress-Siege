package nl.knokko.client.texture;

class SizedTexture implements ITexture {
	
	private final int textureID;
	
	private final short width;
	private final short height;

	public SizedTexture(int textureID, short width, short height) {
		this.textureID = textureID;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getTextureID() {
		return textureID;
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
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
