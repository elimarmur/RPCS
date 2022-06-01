package engine.entities;

import engine.graphics.structures.TexturedModel;
import engine.maths.Vector3f;

public class Ball extends Entity{

	public Ball(Vector3f position, Vector3f rotation, float scale, TexturedModel model, float mass) {
		super(position, rotation, scale, model);
		this.body.setStatic(false);
		this.body.setMass(mass);
	}

}
