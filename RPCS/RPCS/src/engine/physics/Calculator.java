package engine.physics;

import java.util.List;

import engine.terrains.Terrain;

public class Calculator implements Runnable{
	public static int TICK_RATE = 60;
	
	private Thread calc;
	private List<RigidBody> rigidBodies;
	private Terrain terrain;
	private boolean running = true;
	
	
	public Calculator(List<RigidBody> rigidBodies, Terrain terrain) {
		this.rigidBodies = rigidBodies;
		this.terrain = terrain;
	}
	
	
	public void calculateCollisions() {
		for (RigidBody body1 : rigidBodies) {
			for (RigidBody body2 : rigidBodies) {
				if (body1 != body2) {
					if (RigidBody.canCollide(body1, body2)) {
						RigidBody.collide(body1, body2);
					}	
				}
			}
			if (!body1.isStatic()) body1.update(terrain);
			
		}
	}

	public void start() {
		calc = new Thread(this, "calculator");
		calc.start();
	}
	@Override
	public void run() {
		while (running)
			calculateCollisions();
		
	}
	public void stop() {
		running = false;
	}
}
