package engine.maths;

import java.util.Arrays;

public class Matrix4f {
	public static final int SIZE = 4;
	private float[] elements = new float[SIZE * SIZE];

	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				result.set(i, j, 0);
			}
		}
		result.set(0, 0, 1);
		result.set(1, 1, 1);
		result.set(2, 2, 1);
		result.set(3, 3, 1);

		return result;
	}

	@Override
	public String toString() {
		return "Matrix4f [elements=" + Arrays.toString(elements) + "]";
	}

	public static Matrix4f translation(Vector3f translate) {
		Matrix4f result = Matrix4f.identity();

		result.set(3, 0, translate.getX());
		result.set(3, 1, translate.getY());
		result.set(3, 2, translate.getZ());

		return result;
	}

	public static Matrix4f rotation(float angle, Vector3f axis) {
		Matrix4f result = Matrix4f.identity();
		axis = Vector3f.normalize(axis);
		
		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));
		float C = (1 - cos);
		float X = axis.getX(), Y = axis.getY(), Z = axis.getZ();

		result.set(0, 0, cos + X * X * C);
		result.set(0, 1, X * Y * C + Z * sin);
		result.set(0, 2, X * Z * C - Y * sin);
		result.set(1, 0, X * Y * C - Z * sin);
		result.set(1, 1, cos + Y * Y * C);
		result.set(1, 2, Z * Y * C + X * sin);
		result.set(2, 0, X * Z * C + Y * sin);
		result.set(2, 1, Y * Z * C - X * sin);
		result.set(2, 2, cos + Z * Z * C);

		return result;
	}

	public static Matrix4f scale(Vector3f scaler) {
		Matrix4f result = Matrix4f.identity();

		result.set(0, 0, scaler.getX());
		result.set(1, 1, scaler.getY());
		result.set(2, 2, scaler.getZ());

		return result;
	}

	public static Matrix4f transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		Matrix4f result = Matrix4f.identity();

		Matrix4f translationMatrix = Matrix4f.translation(position);
		Matrix4f rotXMatrix = Matrix4f.rotation(rotation.getX(), Vector3f.AXIS_X);
		Matrix4f rotYMatrix = Matrix4f.rotation(rotation.getY(), Vector3f.AXIS_Y);
		Matrix4f rotZMatrix = Matrix4f.rotation(rotation.getZ(), Vector3f.AXIS_Z);
		Matrix4f scaleMatrix = Matrix4f.scale(scale);

		Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));

		result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));

		return result;
	}

	// Using projection matrix formula
	public static Matrix4f projection(float fov, float aspect, float near, float far) {
		Matrix4f result = Matrix4f.identity();

		float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
		float range = far - near;

		result.set(0, 0, 1.0f / (aspect * tanFOV));
		result.set(1, 1, 1.0f / (tanFOV));
		result.set(2, 2, -((far + near) / range));
		result.set(2, 3, -1.0f);
		result.set(3, 2, -((2 * far * near) / range));
		result.set(3, 3, 0.0f);

		return result;
	}

	public static Matrix4f view(Vector3f position, Vector3f rotation) {
		Matrix4f result = Matrix4f.identity();

		Vector3f negPosition = Vector3f.negate(position);
		Matrix4f translationMatrix = Matrix4f.translation(negPosition);

		Vector3f negRotation = Vector3f.negate(rotation);
		Matrix4f rotXMatrix = Matrix4f.rotation(negRotation.getX(), Vector3f.AXIS_X);
		Matrix4f rotYMatrix = Matrix4f.rotation(negRotation.getY(), Vector3f.AXIS_Y);
		Matrix4f rotZMatrix = Matrix4f.rotation(negRotation.getZ(), Vector3f.AXIS_Z);

		Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));

		result = Matrix4f.multiply(rotationMatrix, translationMatrix);

		return result;
	}

	public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
		Matrix4f result = Matrix4f.identity();
		float cell;
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				cell = 0;
				for (int i = 0; i < SIZE; i++) {
					cell += matrix.get(i, row) * other.get(col, i);
				}
				result.set(col, row, cell);
			}
		}
		return result;
		
	}

	public float get(int col, int row) {
		return elements[row * SIZE + col];
	}

	public void set(int col, int row, float value) {
		elements[row * SIZE + col] = value;
	}

	public float[] getAll() {
		return elements;
	}
}
