package engine.graphics.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import engine.graphics.shaders.TerrainShader;
import engine.maths.Matrix4f;
import engine.terrains.Terrain;
import engine.terrains.TexturePack;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projection) {
		this.shader = shader;
		this.shader.loadProjectionMatrix(projection);
		shader.connectTextureUnits();
	}
	public void render (List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			drawElements(terrain);
			unbindTerrain();
		}
	}
	private void drawElements(Terrain terrain) {
		GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
	}

	
	private void prepareTerrain(Terrain terrain) {
		GL30.glBindVertexArray(terrain.getMesh().getVAO());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, terrain.getMesh().getIBO());
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);		
	}

	private void unbindTerrain() {
		GL30.glBindVertexArray(0);
		GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		unbindTextures();
	}

	private void bindTextures(Terrain terrain) {
		TexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getID());
	}
	private void unbindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	private void loadModelMatrix(Terrain terrain) {
		shader.loadTransformationMatrix(terrain);
	}
}
