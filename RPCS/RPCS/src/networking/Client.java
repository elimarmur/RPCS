package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import engine.maths.Vector3f;
import main.Game;

public class Client extends Thread {

	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean connected = false;
	private boolean winStatus = false, loseStatus = false;
	public Game game;

	public Client(String ip, int port) throws UnknownHostException, IOException {
		super();
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		game = new Game();
		connected = true;
		game.client = this;
		System.out.println("client instansiated");
	}

	public void run() {
		System.out.println("clinet running");
		String res = "";
		while (res != "waiting") {
			try {
				LocationExchange(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				res = StatusExchange("waiting");
				break;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		game.start();
		while (game.isAlive()) {
			try {
				Vector3f[] vertex = LocationExchange(game.player.getPosition(), game.player.getRotation());
				game.otherPlayer.setPosition(vertex[0]);
				game.otherPlayer.setRotation(vertex[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				res = StatusExchange("okay");
				if (res != null) {
					if (res.equals("okay")) {
					}
					if (res.equals("win_game")) {
						System.out.println("win recieved");
						loseStatus = true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (winStatus) {
				System.out.println("win sent");
				try {
					LocationExchange(game.player.getPosition(), game.player.getRotation());
					res = StatusExchange("win_game");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		try {
			stopConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = false;
	}

//	public void startGame() {
//		game.start();
//	}

	public String sendMessage(String msg) throws IOException {
		out.println(msg);
		String resp = in.readLine();
		return resp;
	}

	public void stopConnection() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}

	public String StatusExchange(String status) throws IOException {
		String rsp = sendMessage(status);
		return rsp;
	}

	public Vector3f[] LocationExchange(Vector3f position, Vector3f rotation) throws IOException {
		String rsp = sendMessage(VertexToString(position, rotation));
		if (rsp.equals("null") || rsp.equals(null))
			rsp = "0.0 0.0 0.0 0.0 0.0 0.0";
		return StringToVertex(rsp);
	}

	private String VertexToString(Vector3f position, Vector3f rotation) {
		return VectorToString(position) + " " + VectorToString(rotation);
	}

	private String VectorToString(Vector3f v) {
		String str = String.valueOf(v.getX()) + " " + String.valueOf(v.getY()) + " " + String.valueOf(v.getZ());
		return str;
	}

	private Vector3f[] StringToVertex(String str) {
		String[] arr = str.split(" ");
		Vector3f v1 = new Vector3f(Float.parseFloat(arr[0]), Float.parseFloat(arr[1]), Float.parseFloat(arr[2]));
		Vector3f v2 = new Vector3f(Float.parseFloat(arr[3]), Float.parseFloat(arr[4]), Float.parseFloat(arr[5]));
		Vector3f[] vertex = { v1, v2 };
		return vertex;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setWinStatus(boolean win) {
		winStatus = win;
	}

	public boolean getWinStatus() {
		return winStatus;
	}

	public boolean didLose() {
		return loseStatus;
	}
}
