package engine.graphics.shaders;

import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.camera.Camera;
import engine.maths.Matrix4f;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/shaders/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "/shaders/fragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		bindAttributes();
		getAllUniformLocations();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoord");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("model");
		location_projectionMatrix = super.getUniformLocation("projection");
		location_viewMatrix = super.getUniformLocation("view");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");

	}

	public void loadShineVariables(float damper, float reflectivity) {
		super.setUniform(location_shineDamper, damper);
		super.setUniform(location_reflectivity, reflectivity);
	}

	public void loadLight(Light light) {
		super.setUniform(location_lightPosition, light.getPosition());
		super.setUniform(location_lightColor, light.getColor());
	}

	public void loadTransformationMatrix(Entity entity) {
		super.setUniform(location_transformationMatrix,
				Matrix4f.transform(entity.getPosition(), entity.getRotation(), entity.getScale()));
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.setUniform(location_projectionMatrix, projectionMatrix);
	}

	public void loadViewMatrix(Camera camera) {
		super.setUniform(location_viewMatrix, Matrix4f.view(camera.getPosition(), camera.getRotation()));
	}

	public int getLocation_transformationMatrix() {
		return location_transformationMatrix;
	}

	public int getLocation_projectionMatrix() {
		return location_projectionMatrix;
	}

	public int getLocation_viewMatrix() {
		return location_viewMatrix;
	}

	public int getLocation_lightPosition() {
		return location_lightPosition;
	}

	public int getLocation_lightColor() {
		return location_lightColor;
	}
}
