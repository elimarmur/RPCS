package engine.entities;

import org.lwjgl.glfw.GLFW;

import engine.Movable;
import engine.graphics.structures.TexturedModel;
import engine.io.Input;
import engine.maths.Vector3f;
import engine.terrains.Terrain;

public class Player extends Entity implements Movable {
	private static final float ACCELARATION = 30;
	public static final Vector3f START_POSITION = new Vector3f(135, 0, -3);
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	public Player(Vector3f position, Vector3f rotation, float scale, TexturedModel model, float mass) {
		super(position, rotation, scale, model);
		this.body.setStatic(false);
		this.body.setMass(mass);
	}
 
	public void move(Terrain terrain) {
//		checkInputs();

		
		float distance = ACCELARATION;
		float dx = (float) (distance * Math.sin(Math.toRadians(rotation.getY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(rotation.getY())));
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
			body.applyForce(new Vector3f(dx, 0, dz));
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
			body.applyForce(new Vector3f(-dx, 0, -dz));
		} 
		if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
			body.getRotation().add(0, -1, 0);
		} 
		if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
			body.getRotation().add(0, 1, 0);
		}			
		
		if (currentSpeed != 0)
			body.setRotationalVelocity(new Vector3f(0, currentTurnSpeed, 0));
		
		
	}
	public boolean didWin() {
		return body.win_status;
	}




}
