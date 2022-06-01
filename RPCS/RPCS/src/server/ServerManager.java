package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

	List<Socket> clients;
	ServerSocket serverSocket;

	public ServerManager() throws IOException {
		clients = new ArrayList<Socket>();
		serverSocket = new ServerSocket(33333);
	}

	public void startPairing() throws IOException {
		while (true) {
			System.out.println(clients.toString());
			Socket clientSocket = serverSocket.accept();
			clients.add(clientSocket);
			if (clients.size() == 2) {
				PairServer pairServer = new PairServer(clients.get(0), clients.get(1));
				pairServer.start();
				clients.remove(0);
				clients.remove(0);
			}
		}

	}

	public static void main(String[] args) throws IOException {
		ServerManager serverManager = new ServerManager();
		serverManager.startPairing();
	}
}
