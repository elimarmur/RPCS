package engine.physics.hitbox;


import java.io.FileNotFoundException;

import engine.maths.Vector3f;

public abstract class HitBox {

	protected Vector3f origin, rotation;

	public HitBox(Vector3f origin, Vector3f rotation) {
		this.origin = origin;
		this.rotation = rotation;
	}

	protected abstract void loadFromFile(String filepath) throws FileNotFoundException;
	public abstract float findLowestSpot();
	public abstract void setLowestSpot(float f);
	public abstract float offset();
	public abstract boolean areColliding(HitBox hitbox);

	public Vector3f getOrigin() {
		return origin;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
}
