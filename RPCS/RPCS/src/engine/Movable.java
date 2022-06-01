package engine;

import engine.terrains.Terrain;

public interface Movable {
	public float SENSITIVITY = 1.0f;
	public float RUN_SPEED = 0.05f;
	public float TURN_SPEED = 0.1f;
	public float GRAVITY = -0.0001f;
	public float JUMP_FORCE = 0.2f;
//	public float ACCELARATION = 0.01f;
	public void move(Terrain terrain);
}
