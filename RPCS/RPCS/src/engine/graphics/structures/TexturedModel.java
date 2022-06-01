package engine.graphics.structures;

public class TexturedModel {
	private RawMesh[] meshArray;
	private Texture texture;

	public TexturedModel(RawMesh[] meshArray, Texture texture) {
		this.meshArray = meshArray;
		this.texture = texture;
	}
	
	public void create() {
		try {
			texture.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < meshArray.length; i++) {
			meshArray[i].create();
		}
	}

	public void setReflection(float damper, float reflectivity) {
			texture.setShineDamper(damper);
			texture.setReflectivity(reflectivity);
	}
	
	public void cleanUp() {
		for (int i = 0; i < meshArray.length; i++) {
			meshArray[i].cleanUp();
		}
		texture.cleanUp();
	}
	
	public Texture getTexture() {
		return texture;
	}

	public RawMesh[] getMeshArray() {
		return meshArray;
	}
}
