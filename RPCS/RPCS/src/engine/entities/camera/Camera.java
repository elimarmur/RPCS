package engine.entities.camera;

import engine.io.Input;
import engine.maths.Vector3f;

public abstract class Camera {
	protected Vector3f position, rotation;
	float dx, dy, dsy;
	private double MouseX = 0, MouseY = 0, ScrollY = 0;
	
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	public void mouseChange() {
		double newMouseX = Input.getMouseX();
		double newMouseY = Input.getMouseY();
		double newScrollY = Input.getScrollY();

		dx = (float) (newMouseX - MouseX);
		dy = (float) (newMouseY - MouseY);
		dsy = (float) (newScrollY - ScrollY);

		MouseX = newMouseX;
		MouseY = newMouseY;
		ScrollY = newScrollY;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getRotation() {
		return rotation;
	}
}
