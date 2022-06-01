package engine.terrains;

import engine.graphics.structures.Texture;

public class TexturePack {

	private Texture backgroundTexture;
	private Texture rTexture;
	private Texture gTexture;
	private Texture bTexture;

	public TexturePack(Texture backgroundTexture, Texture rTexture, Texture gTexture, Texture bTexture) {
		super();
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public void create() {
		try {
			backgroundTexture.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			rTexture.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			gTexture.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			bTexture.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}

	public Texture getrTexture() {
		return rTexture;
	}

	public Texture getgTexture() {
		return gTexture;
	}

	public Texture getbTexture() {
		return bTexture;
	}
	public void cleanUp() {
		backgroundTexture.cleanUp();
		rTexture.cleanUp();
		gTexture.cleanUp();
		bTexture.cleanUp();
	}

}
