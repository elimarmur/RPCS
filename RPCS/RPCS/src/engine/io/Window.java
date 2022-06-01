package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.maths.Matrix4f;
import engine.maths.Vector3f;

public class Window {
	private int width, height;
	private String title;
	private long window;
	private int fps;
	private static long fps_time, spf_oldTime, spf_newTime, spf_delta;
	private Vector3f backGroundColor = new Vector3f(0, 0, 0);
	public Input input;
	private GLFWWindowSizeCallback sizeCallback;
	private boolean isResized, isFullscreen;
	private int[] windowPosX = new int[1], windowPosY = new int[1];
	private Matrix4f projection;
	private final float FOV = 70.0f, NEAR = 0.1f, FAR = 1000.0f;

	public Window(int width, int height, int fps, String title, boolean isFullScreen) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.fps = fps;
		this.isFullscreen = isFullScreen;
		this.backGroundColor = new Vector3f(0.0f, 0.0f, 0.0f);
		projection = Matrix4f.projection(FOV, (float) width / (float) height, NEAR, FAR);
	}

	public void create() {
		// Initialize Graphics Library Framework (GLFW).
		if (!GLFW.glfwInit()) {
			System.err.println("ERROR: Couldn't initialize GLFW");
			System.exit(-1);
		}
		// Set the window to not be resizable to maintain aspect ratio.
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		// Create new instance of Input.
		input = new Input();
		// Create the window in GLFW.
		window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		if (window == 0) {
			System.out.println("ERROR: Window couldn't be created");
			System.exit(-1);
		}
		// Set the window to the center of the display.
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		windowPosX[0] = (videoMode.width() - width) / 2;
		windowPosY[0] = (videoMode.height() - height) / 2;
		GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);
		// Make the window context current for this thread.
		GLFW.glfwMakeContextCurrent(window);
		// Initialize GL.
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		createCallbacks();
		GLFW.glfwShowWindow(window);
		GLFW.glfwSwapInterval(60 / fps); // 60/arg = fps

		fps_time = System.currentTimeMillis();
		spf_oldTime = System.currentTimeMillis();
	}

	private void createCallbacks() {
		sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};

		GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
		GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
		GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());

	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public void stop() {
		GLFW.glfwTerminate();
	}

	public void update() {
		if (isResized) {
			GL11.glViewport(0, 0, width, height);
			isResized = false;
		}
		GL11.glClearColor(backGroundColor.getX(), backGroundColor.getY(), backGroundColor.getZ(), 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GLFW.glfwPollEvents();
		fps++;
		// Counter of how many times window was updated in the last second.
		if (System.currentTimeMillis() > fps_time + 1000) {
			GLFW.glfwSetWindowTitle(window, title + "| FPS:" + fps);
			fps_time = System.currentTimeMillis();
			fps = 0;
		}
		// Counter of how long it took to render a frame.
		spf_newTime = System.currentTimeMillis();
		spf_delta = (spf_newTime - spf_oldTime);
		spf_oldTime = spf_newTime;
	}
	public static long getFrameTimeMillis() {
		return spf_delta;
	}

	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	public void mouseState(boolean lock) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		isResized = true;
		if (isFullscreen) {
			GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
		} else {
			GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
		}
	}

	public long getWindow() {
		return window;
	}

	public void setBackgroundColor(float r, float g, float b) {
		backGroundColor = new Vector3f(r, g, b);

	}

	public Matrix4f getProjectionMatrix() {
		return projection;
	}

	public void cleanUp() {
		input.cleanUp();
		sizeCallback.free();
		GLFW.glfwWindowShouldClose(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
}
