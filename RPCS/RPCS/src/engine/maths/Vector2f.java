package engine.maths;

public class Vector2f {
	private float x, y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public static Vector2f geoToCoords(float d, float angle) {
		float x = (float) (d * Math.sin(Math.toRadians(angle)));
		float y = (float) (d * Math.cos(Math.toRadians(angle)));
		return new Vector2f(x, y);
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		return "Vector2f [x=" + x + ", y=" + y + "]";
	}
	
	public static Vector2f negate(Vector2f vec) {
		return new Vector2f(-vec.getX(), -vec.getY());
	}
	public static Vector2f add(Vector2f vec1, Vector2f vec2) {
		return new Vector2f((vec1.getX() + vec2.getX()), (vec1.getY() + vec2.getY()));
	}
	public static Vector2f sub(Vector2f vec1, Vector2f vec2) {
		return new Vector2f((vec1.getX() - vec2.getX()), (vec1.getY() - vec2.getY()));
	}
	public static Vector2f multiply(Vector2f vec1, Vector2f vec2) {
		return new Vector2f((vec1.getX() * vec2.getX()), (vec1.getY() * vec2.getY()));
	}
	public static Vector2f divide(Vector2f vec1, Vector2f vec2) {
		return new Vector2f((vec1.getX() / vec2.getX()), (vec1.getY() / vec2.getY()));
	}
	public static float length(Vector2f vector) {
		return (float)Math.sqrt(vector.getX()*vector.getX() + vector.getY()*vector.getY());
	}
	// Return a unit vector
	public static Vector2f normalize(Vector2f vector) {
		float len = Vector2f.length(vector);
		return Vector2f.divide(vector, new Vector2f(len, len));
	}
	public static float dot(Vector2f vec1, Vector2f vec2) {
		return vec1.getX()*vec2.getX() + vec1.getY()*vec2.getY();
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
}
