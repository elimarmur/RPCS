package engine.graphics.structures;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {
	private int textureID, width, height;
	private String filePath;
	
	private float shineDamper = 1, reflectivity = 0;

	public Texture(String filePath) {
		this.filePath = filePath;
		
	}
	public void create() throws Exception{
		ByteBuffer buffer;
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);
			
			buffer = STBImage.stbi_load(filePath, w, h, c, 4);
			if(buffer == null)
				throw new Exception("Image file could't be loaded from " + filePath + " | stbi_failure_reason: " + STBImage.stbi_failure_reason());
			width = w.get(0);
			height = h.get(0);
		}
		
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS, -1);
		STBImage.stbi_image_free(buffer);
	}
	
	
	public int getID() {
		return textureID;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public float getShineDamper() {
		return shineDamper;
	}
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	public void cleanUp() {
		GL11.glDeleteTextures(textureID);
	}
}
