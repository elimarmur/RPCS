package engine.graphics.shaders;

import engine.entities.Light;
import engine.entities.camera.Camera;
import engine.maths.Matrix4f;
import engine.maths.Vector3f;
import engine.terrains.Terrain;

public class TerrainShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/shaders/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/shaders/terrainFragmentShader.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;

	public TerrainShader() {
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
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
	}

	public void connectTextureUnits() {
		super.setUniform(location_backgroundTexture, 0);
		super.setUniform(location_rTexture, 1);
		super.setUniform(location_gTexture, 2);
		super.setUniform(location_bTexture, 3);
		super.setUniform(location_blendMap, 4);
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.setUniform(location_shineDamper, damper);
		super.setUniform(location_reflectivity, reflectivity);
	}

	public void loadLight(Light light) {
		super.setUniform(location_lightPosition, light.getPosition());
		super.setUniform(location_lightColor, light.getColor());
	}

	public void loadTransformationMatrix(Terrain terrain) {
		super.setUniform(location_transformationMatrix, Matrix4f.transform(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), Terrain.ROTATION_UNIVERSAL, Terrain.SCALE_UNIVERSAL));
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
