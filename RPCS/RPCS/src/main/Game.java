package main;

import java.util.ArrayList;
import java.util.List;

import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.OtherPlayer;
import engine.entities.Player;
import engine.entities.camera.TPPCamera;
import engine.graphics.rendering.MasterRenderer;
import engine.graphics.structures.Texture;
import engine.graphics.structures.TexturedModel;
import engine.io.ModelLoader;
import engine.io.Window;
import engine.maths.Vector3f;
import engine.physics.Calculator;
import engine.physics.RigidBody;
import engine.terrains.Terrain;
import engine.terrains.TexturePack;
import networking.Client;

public class Game extends Thread {
	public Window window;
	public MasterRenderer renderer;
	public Calculator calculator;
	public final int WIDTH = 600, HEIGHT = 400, FPS = 60;
	public final boolean FULL_SCREEN = false;

	public boolean running = true;
	public Entity[] stuff = new Entity[2];
	
	public Client client;
	
	public List<TexturedModel> models = new ArrayList<>();

	TexturedModel house = ModelLoader.loadModel("resources/models/Cottage_FREE.obj",
			"resources/textures/Cottage_Clean_Base_Color.png");
	TexturedModel tree = ModelLoader.loadModel("resources/models/lowPolyTree.obj",
			"resources/textures/lowPolyTree.png");
	TexturedModel human = ModelLoader.loadModel("resources/models/human.obj", "resources/textures/white.png");
	
	TexturedModel car = ModelLoader.loadModel("resources/models/Jeep_Renegade_2016_centered1.obj", "resources/textures/car_jeep_ren.jpg");
	TexturedModel redCar = ModelLoader.loadModel("resources/models/Jeep_Renegade_2016_centered1.obj", "resources/textures/car_jeep_ren_red.png");
	
	
	TexturedModel ballModel = ModelLoader.loadModel("resources/models/ball.obj", "resources/textures/ball.jpg");
	
//	TexturedModel goalPost = ModelLoader.loadModel("resources/models/goal.fbx", "resources/textures/goal.png");

	public Player player = new Player(new Vector3f(135, 0, -3), new Vector3f(0, 0, 0), 2f, car, 1);
	public OtherPlayer otherPlayer = new OtherPlayer(new Vector3f(135, 0, -3), new Vector3f(0, 0, 0), 2f, redCar);
	
//	public Ball ball = new Ball(new Vector3f(0, 30, 5), new Vector3f(0, 0, 0), 0.5f, ballModel, 1);

	public TPPCamera camera = new TPPCamera(player, 50, 30);

	public Light light = new Light(new Vector3f(3000, 3000, 0), new Vector3f(1, 1, 1));

	public Texture blendMap = new Texture("resources/textures/game_blend_map1.png");
	public Texture backgroundTexture = new Texture("resources/textures/grass.png");
	public Texture rTexture = new Texture("resources/textures/path.png");
	public Texture gTexture = new Texture("resources/textures/lava.png");
	public Texture bTexture = new Texture("resources/textures/brick.png");
	public Terrain terrain = new Terrain(0, 0, new TexturePack(backgroundTexture, rTexture, gTexture, bTexture),
			blendMap, "resources/textures/game_height_map.png");



	public void init() {
		window = new Window(WIDTH, HEIGHT, FPS, "LWJGL3", FULL_SCREEN);
		window.setBackgroundColor(0.8f, 0.5f, 0.5f);
		window.create();
		renderer = new MasterRenderer(window);
		
		models.add(house);
		models.add(human);
		models.add(tree);
		models.add(car);
		models.add(redCar);
//		models.add(ballModel);
//		models.add(goalPost);
		
		car.setReflection(1, 0.3f);
		redCar.setReflection(1, 0.3f);
		
		for (TexturedModel model : models) {
			model.create();
		}
				
		terrain.create();

		List<RigidBody> bodies = new ArrayList<>();
		for (int i = 0; i < stuff.length; i++) {
//			float x = (float) (Math.random() * 1000 - 500);
//			float z = (float) (Math.random() * 1000 - 500);
			float x = 145 - 20*i;
			float z = -2;
			stuff[i] = new Entity(
					new Vector3f(x, terrain.getHeightOfTerrain(x, z) -2, z),
					new Vector3f(0, 0, 0), 2, tree);
			bodies.add(stuff[i].getBody());
		}
		player.getBody().setSphereHitbox(1.3f);
		bodies.add(player.getBody());

		
		calculator = new Calculator(bodies, terrain);
		calculator.start();
	}

	public void run() {
		init();
		window.mouseState(true);
		while (!window.shouldClose() && !player.didWin() && !client.didLose()) {
			update();
			render();
		}
		client.setWinStatus(player.didWin());
		close();
	}

	private void update() {
		window.update();
		camera.move(terrain);
		player.move(terrain);
		
	}

	private void render() {
		for (int i = 0; i < stuff.length; i++) {
			renderer.processEntity(stuff[i]);
		}
		renderer.processEntity(player);
		renderer.processEntity(otherPlayer);
		renderer.processTerrain(terrain);
		renderer.render(light, camera);
		window.swapBuffers();
	}

	private void close() {
		calculator.stop();
		window.cleanUp();
		for (int i = 0; i < stuff.length; i++) {
			stuff[i].cleanUp();
		}
		terrain.cleanUp();
		
		player.cleanUp();
		otherPlayer.cleanUp();
				
		for (TexturedModel model : models) {
			model.cleanUp();
		}
		renderer.cleanUp();
	}

//	public static void main(String[] args) {
//		new Game().start();
//
//	}

}
