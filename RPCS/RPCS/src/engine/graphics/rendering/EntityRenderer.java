package engine.graphics.rendering;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import engine.entities.Entity;
import engine.graphics.structures.Texture;
import engine.graphics.shaders.StaticShader;
import engine.graphics.structures.RawMesh;
import engine.graphics.structures.TexturedModel;
import engine.maths.Matrix4f;

public class EntityRenderer {
	private StaticShader shader;

	public EntityRenderer(StaticShader shader, Matrix4f projection) {
		this.shader = shader;
		this.shader.loadProjectionMatrix(projection);
	}

	public void render(HashMap<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			Texture texture = model.getTexture();
			prepareTexture(texture);
			for (int i = 0; i < model.getMeshArray().length; i++) {
				prepareMesh(model.getMeshArray()[i]);
				List<Entity> batch = entities.get(model);
				for (Entity entity : batch) {
					loadModelMatrix(entity);
					drawElements(model.getMeshArray()[i]);
				}
				unbindMesh();
			}
			unbindTexture();
		}
	}

	private void drawElements(RawMesh mesh) {
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
	}

	private void prepareMesh(RawMesh mesh) {
		GL30.glBindVertexArray(mesh.getVAO());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());

	}

	private void unbindMesh() {
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareTexture(Texture texture) {
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}

	private void unbindTexture() {
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private void loadModelMatrix(Entity entity) {
		shader.loadTransformationMatrix(entity);
	}

}
