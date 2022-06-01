package engine.maths;

public class Vector3f {
	private float x, y, z;
	public static final Vector3f AXIS_X = new Vector3f(1, 0, 0);
	public static final Vector3f AXIS_Y = new Vector3f(0, 1, 0);
	public static final Vector3f AXIS_Z = new Vector3f(0, 0, 1);
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3f(Vector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public static Vector3f negate(Vector3f vec) {
		return new Vector3f(-vec.getX(), -vec.getY(), -vec.getZ());
	}
	public static Vector3f add(Vector3f vec1, Vector3f vec2) {
		return new Vector3f((vec1.getX() + vec2.getX()), (vec1.getY() + vec2.getY()), (vec1.getZ() + vec2.getZ()));
	}
	public void add(Vector3f vector) {
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;
	}
	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public static Vector3f sub(Vector3f vec1, Vector3f vec2) {
		return new Vector3f((vec1.getX() - vec2.getX()), (vec1.getY() - vec2.getY()), (vec1.getZ() - vec2.getZ()));
	}
	public static Vector3f multiply(Vector3f vec1, Vector3f vec2) {
		return new Vector3f((vec1.getX() * vec2.getX()), (vec1.getY() * vec2.getY()), (vec1.getZ() * vec2.getZ()));
	}
	public static Vector3f divide(Vector3f vec1, Vector3f vec2) {
		return new Vector3f((vec1.getX() / vec2.getX()), (vec1.getY() / vec2.getY()), (vec1.getZ() / vec2.getZ()));
	}
	public static float length(Vector3f vector) {
		return (float)Math.sqrt(vector.getX()*vector.getX() + vector.getY()*vector.getY() + vector.getZ()*vector.getZ());
	}
	// Return a unit vector
	public static Vector3f normalize(Vector3f vector) {
		float len = Vector3f.length(vector);
		return Vector3f.divide(vector, new Vector3f(len, len, len));
	}
	public static float dot(Vector3f vec1, Vector3f vec2) {
		return vec1.getX()*vec2.getX() + vec1.getY()*vec2.getY() + vec1.getZ()*vec2.getZ();
	}
	public static Vector3f normalToRotation(Vector3f normal) {
		return new Vector3f((float) Math.toDegrees(normal.x), (float) Math.toDegrees(normal.y), (float) Math.toDegrees(normal.z));
	}
	public static Vector3f findNormal(Vector3f vec1, Vector3f vec2, Vector3f vec3) {
		return Vector3f.normalize(Vector3f.multiply(Vector3f.sub(vec2, vec1), Vector3f.sub(vec3, vec1)));
	}
	
	public static float distance(Vector3f pos1, Vector3f pos2) {
		return (float) Math.sqrt(Math.pow((pos1.x - pos2.x), 2) + Math.pow((pos1.y - pos2.y), 2) + Math.pow((pos1.z - pos2.z), 2));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
