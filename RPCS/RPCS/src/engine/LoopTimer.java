package engine;

public class LoopTimer {

	private float duration, start, end, delta;

	public LoopTimer(float duration) {
		this.duration = duration;
	}
	public void start() {
		this.start = System.currentTimeMillis();
	}
	public boolean isFinished() {
		end = System.currentTimeMillis();
		delta = (end - start) / 1000f;
		return !(delta < duration);
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}




	
	
}
