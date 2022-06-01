package engine.graphics.structures;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class RawMesh {
	private Vertex[] vertices;
	private int[] indices;
	private int vao, pbo, ibo, nbo, tbo; // vertex array object, position buffer object, index buffer object, color
											// buffer object

	public RawMesh(Vertex[] vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
	}

	public void create() {
		
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			positionData[i * 3] = vertices[i].getPosition().getX();
			positionData[i * 3 + 1] = vertices[i].getPosition().getY();
			positionData[i * 3 + 2] = vertices[i].getPosition().getZ();
		}
		positionBuffer.put(positionData).flip();

		pbo = storeData(positionBuffer, 0, 3);

		FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
		float[] textureData = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			textureData[i * 2] = vertices[i].getTextureCoord().getX();
			textureData[i * 2 + 1] = vertices[i].getTextureCoord().getY();
		}
		textureBuffer.put(textureData).flip();

		tbo = storeData(textureBuffer, 1, 2);

		FloatBuffer normalBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] normalData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			normalData[i * 3] = vertices[i].getNormal().getX();
			normalData[i * 3 + 1] = vertices[i].getNormal().getY();
			normalData[i * 3 + 2] = vertices[i].getNormal().getZ();
		}
		normalBuffer.put(normalData).flip();

		nbo = storeData(normalBuffer, 2, 3);

		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();

		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private int storeData(FloatBuffer buffer, int index, int size) {
		int bufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return bufferID;
	}

	public void cleanUp() {
		GL15.glDeleteBuffers(pbo);
		GL15.glDeleteBuffers(nbo);
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(tbo);

		GL30.glDeleteVertexArrays(vao);

	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getVAO() {
		return vao;
	}

	public int getPBO() {
		return pbo;
	}

	public int getIBO() {
		return ibo;
	}

	public int getNBO() {
		return nbo;
	}

	public int getTBO() {
		return tbo;
	}

}
