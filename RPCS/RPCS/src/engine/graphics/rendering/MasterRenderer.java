package engine.graphics.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.camera.Camera;
import engine.graphics.shaders.StaticShader;
import engine.graphics.shaders.TerrainShader;
import engine.graphics.structures.TexturedModel;
import engine.io.Window;
import engine.maths.Matrix4f;
import engine.terrains.Terrain;

public class MasterRenderer {

	private StaticShader staticShader;
	private TerrainShader terrainShader;
	
	private EntityRenderer staticRenderer;
	private TerrainRenderer terrainRenderer;
	
	private Matrix4f projectionMatrix;

	private HashMap<TexturedModel, List<Entity>> entities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public void render(Light sun, Camera camera) {
		
		staticShader.start();
		staticShader.loadLight(sun);
		staticShader.loadViewMatrix(camera);
		staticShader.loadProjectionMatrix(projectionMatrix);
		staticRenderer.render(entities);
		staticShader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadProjectionMatrix(projectionMatrix);
		terrainRenderer.render(terrains);
		terrainShader.stop();

		entities.clear();
		terrains.clear();
	}
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
	}

	public MasterRenderer(Window window) {
		enableCulling();
		this.projectionMatrix = window.getProjectionMatrix();
		staticShader = new StaticShader();
		staticRenderer = new EntityRenderer(staticShader, projectionMatrix);
		terrainShader = new TerrainShader();
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
}
