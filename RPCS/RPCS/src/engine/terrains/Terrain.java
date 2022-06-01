package engine.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.graphics.structures.RawMesh;
import engine.graphics.structures.Texture;
import engine.graphics.structures.Vertex;
import engine.maths.Maths;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Terrain {

	public static final float SIZE = 300;
	public static final float MAX_HEIGHT = 40;
	public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
//	public static final int VERTEX_COUNT = 128;
	public static final Vector3f SCALE_UNIVERSAL = new Vector3f(1, 1, 1);
	public static final Vector3f ROTATION_UNIVERSAL = new Vector3f(0, 0, 0);

	private float x, z;
	private RawMesh mesh;
	private Texture blendMap;
	private TexturePack texturePack;
	private String heightMap;
	
	private float[][] heights;
	private Vector3f[][] normals;

	public Terrain(int centerX, int centerZ, TexturePack texturePack, Texture blendMap, String heightMap) {
		this.blendMap = blendMap;
		this.texturePack = texturePack;
		this.x = centerX - SIZE / 2;
		this.z = centerZ - SIZE / 2;
		this.heightMap = heightMap;
	}

	public void create() {
		this.mesh = generateTerrain(heightMap);
		this.mesh.create();
		try {
			this.blendMap.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		texturePack.create();
	}

	private RawMesh generateTerrain(String heightMap) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(heightMap));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		normals = new Vector3f[VERTEX_COUNT][VERTEX_COUNT];
		int vertexCount = VERTEX_COUNT * VERTEX_COUNT;
		Vertex[] vertices = new Vertex[vertexCount];

		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				Vector3f normal = calculateNormal(j, i, image);
				normals[j][i] = normal;
				vertices[vertexPointer] = new Vertex(
						new Vector3f((float) j / ((float) VERTEX_COUNT - 1) * SIZE, height,
								(float) i / ((float) VERTEX_COUNT - 1) * SIZE),
						normal,
						new Vector2f((float) j / ((float) VERTEX_COUNT - 1), (float) i / ((float) VERTEX_COUNT - 1)));
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		return new RawMesh(vertices, indices);
	}
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX < 0 || gridX >= heights.length - 1 || gridZ < 0 || gridZ >= heights.length - 1) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}
	
	public Vector3f getNormalOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float)normals.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX < 0 || gridX >= normals.length - 1 || gridZ < 0 || gridZ >= normals.length - 1) {
			return Vector3f.AXIS_Y;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		Vector3f answer;
		if (xCoord <= (1-zCoord)) {
			answer = Vector3f.normalize(Vector3f.add(normals[gridX][gridZ], Vector3f.add(normals[gridX + 1][gridZ], normals[gridX][gridZ + 1])));
			
		}else {
			answer = Vector3f.normalize(Vector3f.add(normals[gridX + 1][gridZ], Vector3f.add(normals[gridX + 1][gridZ + 1], normals[gridX][gridZ+1])));
		}
		return answer;
		
	}

	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal = Vector3f.normalize(normal);
		return normal;
	}

	private float getHeight(int x, int z, BufferedImage image) {
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return (height >= 0) ? height * 2 : height / 2;

	}

	public static float getSize() {
		return SIZE;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawMesh getMesh() {
		return mesh;
	}

	public TexturePack getTexturePack() {
		return texturePack;
	}

	public Texture getBlendMap() {
		return blendMap;
	}

	public void cleanUp() {
		blendMap.cleanUp();
		texturePack.cleanUp();
	}
}
