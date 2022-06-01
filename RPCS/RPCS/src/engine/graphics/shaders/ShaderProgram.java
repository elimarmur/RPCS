package engine.graphics.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import engine.maths.Matrix4f;
import engine.maths.Vector2f;
import engine.maths.Vector3f;
import engine.utils.FileUtils;

public abstract class ShaderProgram {
	protected int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public String vertexFile, fragmentFile;

	public ShaderProgram(String vertexPath, String fragmentPath) {
		vertexShaderID = loadShader(vertexPath, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentPath, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programID));
			return;
		}
		GL20.glValidateProgram(programID);
		if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(programID));
			return;
		}
		getAllUniformLocations();
	}

	private static int loadShader(String filePath, int type) {
		String shaderSource = FileUtils.loadAsString(filePath);
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		return shaderID;
	}

	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}

	protected void setUniform(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void setUniform(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	protected void setUniform(int location, boolean value) {
		GL20.glUniform1i(location, value ? 1 : 0);
	}

	protected void setUniform(int location, Vector2f value) {
		GL20.glUniform2f(location, value.getX(), value.getY());
	}

	protected void setUniform(int location, Vector3f value) {
		GL20.glUniform3f(location, value.getX(), value.getY(), value.getZ());
	}

	protected void setUniform(int location, Matrix4f value) {
		FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
		matrix.put(value.getAll()).flip();
		GL20.glUniformMatrix4fv(location, true, matrix);
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	protected abstract void bindAttributes();

	protected abstract void getAllUniformLocations();

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	public int getProgramID() {
		return programID;
	}

}