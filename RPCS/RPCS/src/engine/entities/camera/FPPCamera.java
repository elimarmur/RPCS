package engine.entities.camera;

import org.lwjgl.glfw.GLFW;

import engine.Movable;
import engine.io.Input;
import engine.io.Window;
import engine.maths.Vector3f;
import engine.terrains.Terrain;

public class FPPCamera extends Camera implements Movable {
	public static final float NORMAL_SPEED = 1.0f;

	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public FPPCamera(Vector3f position, Vector3f rotation) {
		super(position, rotation);
	}

	public void move(Terrain terrain) {

		mouseChange();

		rotation.add(-dy * SENSITIVITY * 0.1f, -dx * SENSITIVITY * 0.1f, 0);
		float distance = RUN_SPEED * Window.getFrameTimeMillis();
		float x = (float) Math.sin(Math.toRadians(rotation.getY())) * distance;
		float z = (float) Math.cos(Math.toRadians(rotation.getY())) * distance;

		
		float y = upwardsSpeed * Window.getFrameTimeMillis();
		
		if (Input.isKeyDown(GLFW.GLFW_KEY_A))
			position.add(-z, 0, x);
		if (Input.isKeyDown(GLFW.GLFW_KEY_D))
			position.add(z, 0, -x);
		if (Input.isKeyDown(GLFW.GLFW_KEY_W))
			position.add(-x, 0, -z);
		if (Input.isKeyDown(GLFW.GLFW_KEY_S))
			position.add(x, 0, z);
		if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			jump();
		}
		position.add(0, y, 0);
		upwardsSpeed += GRAVITY * Window.getFrameTimeMillis();
		
		float terrainHeight = terrain.getHeightOfTerrain(position.getX(), position.getZ());
		if (position.getY() < terrainHeight + 2) {
			upwardsSpeed = 0;
			isInAir = false;
			position.setY(terrainHeight + 2);
		}

	}

	private void jump() {
		if (!isInAir) {
			upwardsSpeed = JUMP_FORCE;
			isInAir = true;
		}
	}

}
