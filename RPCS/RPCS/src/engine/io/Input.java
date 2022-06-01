package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static boolean[] previousKeys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] previousButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;
	public static double scrollX, scrollY;
	
	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;
	
	public Input() {
		this.keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				previousKeys[key] = keys[key];
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
		this.mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		this.mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				previousButtons[button] = buttons[button];
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};
		this.mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
		
	}
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}
	public static boolean isKeyUp(int key) {
		return !keys[key];
	}
	public static boolean isButtonUp(int button) {
		return !buttons[button];
	}

	public static boolean wasKeyPressed(int key) {
		if (previousKeys[key] && !keys[key]) return true;
		return false;
	}

	
	

	public void cleanUp() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}
	
	public static double getMouseX() {
		return mouseX;
	}

	public static  double getMouseY() {
		return mouseY;
	}
	public static double getScrollX() {
		return scrollX;
	}
	public static  double getScrollY() {
		return scrollY;
	}


	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}
	
}
