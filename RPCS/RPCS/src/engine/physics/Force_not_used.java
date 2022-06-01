package engine.physics;

import engine.maths.Vector3f;

public class Force_not_used {
	private Vector3f force;
	private long startTime;
	private float duration;
	private boolean isApplied;
	public Force_not_used(Vector3f force, float duration) {
		this.force = force;
		this.duration = duration;
		this.isApplied = false;
	}
	public float getStartTime() {
		return startTime;
	}
	public Vector3f getForce() {
		return force;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public void apply() {
		this.startTime = System.currentTimeMillis();
		this.isApplied = true;
	}
	public boolean isApplied() {
		return this.isApplied;
	}
	public boolean isFinished() {
		long time = System.currentTimeMillis();
		float delta = (float)(time - startTime) / 1000f;
		if (delta >= duration) {
			return true;
		}
		return false;
	}
	
}
