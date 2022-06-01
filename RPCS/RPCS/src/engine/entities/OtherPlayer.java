package engine.entities;

import engine.graphics.structures.TexturedModel;
import engine.maths.Vector3f;

public class OtherPlayer extends Entity{

	public OtherPlayer(Vector3f position, Vector3f rotation, float scale, TexturedModel model) {
		super(position, rotation, scale, model);
	}

	public void move(Vector3f position, Vector3f rotation) {
		this.setPosition(position);
		this.setRotation(rotation);
		
	}

}
