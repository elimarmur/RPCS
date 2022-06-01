package engine.io;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import engine.graphics.structures.Texture;
import engine.graphics.structures.RawMesh;
import engine.graphics.structures.TexturedModel;
import engine.graphics.structures.Vertex;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class ModelLoader {
	
	
	public static TexturedModel loadModel(String filePath, String texturePath) {
		AIScene scene = Assimp.aiImportFile(filePath,
				Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_FlipUVs | Assimp.aiProcess_Triangulate);

		if (scene == null) {
			System.err.println("Error: Couldn't load model from " + filePath);
			System.exit(-1);
		}
		PointerBuffer buffer = scene.mMeshes();
		RawMesh[] meshes = new RawMesh[buffer.limit()];

		Texture texture = new Texture(texturePath);

		for (int i = 0; i < buffer.limit(); i++) {
			AIMesh mesh = AIMesh.create(buffer.get(i));

			int vertexCount = mesh.mNumVertices();

			AIVector3D.Buffer vertices = mesh.mVertices();
			AIVector3D.Buffer normals = mesh.mNormals();

			Vertex[] vertexList = new Vertex[vertexCount];

			for (int j = 0; j < vertexCount; j++) {
				AIVector3D vertex = vertices.get(j);
				Vector3f meshVertex = new Vector3f(vertex.x(), vertex.y(), vertex.z());

				AIVector3D normal = normals.get(j);
				Vector3f meshNormal = new Vector3f(normal.x(), normal.y(), normal.z());

				Vector2f meshTextureCoord = new Vector2f(0, 0);
				if (mesh.mNumUVComponents().get(0) != 0) {
					AIVector3D textureCoords = mesh.mTextureCoords(0).get(j);
					meshTextureCoord.setX(textureCoords.x());
					meshTextureCoord.setY(textureCoords.y());
				}

				vertexList[j] = new Vertex(meshVertex, meshNormal, meshTextureCoord);
			}

			int faceCount = mesh.mNumFaces();
			AIFace.Buffer indices = mesh.mFaces();
			int[] indicesList = new int[faceCount * 3];

			for (int j = 0; j < faceCount; j++) {
				AIFace face = indices.get(j);
				indicesList[j * 3 + 0] = face.mIndices().get(0);
				indicesList[j * 3 + 1] = face.mIndices().get(1);
				indicesList[j * 3 + 2] = face.mIndices().get(2);
			}
			meshes[i] = new RawMesh(vertexList, indicesList);
		}
		return new TexturedModel(meshes, texture);
	}
}
