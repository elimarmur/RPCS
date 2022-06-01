package engine.entities.camera;

import org.lwjgl.glfw.GLFW;

import engine.Movable;
import engine.entities.Player;
import engine.io.Input;
import engine.maths.Vector3f;
import engine.terrains.Terrain;

public class TPPCamera extends Camera implements Movable {
	public static final float MIN_PITCH = 0, MAX_PITCH = 90, MIN_DISTANCE = 2, MAX_DISTANCE = 200, MIN_ANGLE = -110,
			MAX_ANGLE = 110, Y_OFFSET = 0;
	float distanceFromPlayer, pitch, angleAroundPlayer = 0;
	float horizontalDistance, verticalDistance;
	Player player;

	public TPPCamera(Player player, float distanceFromPlayer, float pitch) {
		super(null, null);
		this.player = player;
		this.distanceFromPlayer = distanceFromPlayer;
		this.pitch = pitch;
	}

	private void calculatePosition() {
		float totalAngle = player.getRotation().getY() + angleAroundPlayer;

		float offsetX = horizontalDistance * (float) Math.sin(Math.toRadians(totalAngle));
		float offsetZ = horizontalDistance * (float) Math.cos(Math.toRadians(totalAngle));

		this.rotation = new Vector3f(-pitch, -(180 - totalAngle), 0);
		this.position = Vector3f.add(player.getPosition(),
				new Vector3f(-offsetX, verticalDistance + Y_OFFSET, -offsetZ));
	}

	public void move(Terrain terrain) {
		mouseChange();
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		calculateHorizontalDistance();
		calculateVerticalDistance();
		calculatePosition();
	}

	public void calculateHorizontalDistance() {
		horizontalDistance = distanceFromPlayer * (float) Math.cos(Math.toRadians(pitch));
	}

	public void calculateVerticalDistance() {
		verticalDistance = distanceFromPlayer * (float) Math.sin(Math.toRadians(pitch));
	}

	private void calculateZoom() {
		float zoomLevel = dsy;
		distanceFromPlayer -= zoomLevel;
		if (distanceFromPlayer > MAX_DISTANCE)
			distanceFromPlayer = MAX_DISTANCE;
		else if (distanceFromPlayer < MIN_DISTANCE)
			distanceFromPlayer = MIN_DISTANCE;
	}

	private void calculatePitch() {
		if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			float pitchChange = dy * SENSITIVITY * 0.1f;
			pitch += pitchChange;
		}
		if (pitch > MAX_PITCH)
			pitch = MAX_PITCH;
		else if (pitch < MIN_PITCH)
			pitch = MIN_PITCH;
	}

	private void calculateAngleAroundPlayer() {
		if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			float angleChange = dx * SENSITIVITY * 0.3f;
			angleAroundPlayer -= angleChange;
			if (angleAroundPlayer > MAX_ANGLE)
				angleAroundPlayer = MAX_ANGLE;
			else if (angleAroundPlayer < MIN_ANGLE)
				angleAroundPlayer = MIN_ANGLE;
		}else {
			angleAroundPlayer = 0;
		}
		
	}

	public Player getPlayer() {
		return player;
	}
}