package engine.entities;

import engine.graphics.structures.TexturedModel;
import engine.maths.Vector3f;
import engine.physics.RigidBody;

public class Entity {
	protected Vector3f position, rotation, scale;
	protected TexturedModel model;
	protected RigidBody body;

	public Entity(Vector3f position, Vector3f rotation, Vector3f scale, TexturedModel model) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
		body = new RigidBody(position, rotation, 0, true);
	}
	public Entity(Vector3f position, Vector3f rotation, float scale, TexturedModel model) {
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vector3f(scale, scale, scale);
		this.model = model;
		body = new RigidBody(position, rotation, 0, true);
	}

	public void setReflection(float damper, float reflectivity) {
		model.setReflection(damper, reflectivity);
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public RigidBody getBody() {
		return body;
	}
	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public void cleanUp() {
		model.cleanUp();
		body.cleanUp();
	}

}
