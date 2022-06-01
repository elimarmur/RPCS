package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class PairServer extends Thread {
	private Socket clientSocket1;
	private Socket clientSocket2;
	private PrintWriter out1;
	private BufferedReader in1;
	private PrintWriter out2;
	private BufferedReader in2;

	public PairServer(Socket client1, Socket client2) throws IOException {
		clientSocket1 = client1;
		clientSocket2 = client2;
		out1 = new PrintWriter(clientSocket1.getOutputStream(), true);
		in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
		out2 = new PrintWriter(clientSocket2.getOutputStream(), true);
		in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
		System.out.println("pair server online");
	}

	public void sendData(String d1, String d2) throws IOException {
		out1.println(d2);
		out2.println(d1);

	}

	public void communicate() throws Exception {
		String locations1 = in1.readLine();
		String locations2 = in2.readLine();
		out1.println(locations2);
		out2.println(locations1);
		while (true) {
			String location1 = in1.readLine();
			String location2 = in2.readLine();
			if (location1 == null) location1 = "0";
			if (location2 == null) location2 = "0";
			System.out.println(location1 + "\n" + location2);
			sendData(location1, location2);
			String status1 = in1.readLine();
			String status2 = in2.readLine();
			System.out.println(status1 + "\n" + status2);
			sendData(status1, status2);
			if (status1 == null || status2 == null || status1.equals("win_game") || status2.equals("win_game")) {
				return;
			}
		}
	}

	public void run() {
		try {
			communicate();
			finishThread();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void finishThread() throws IOException {
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		in1.close();
		out1.close();
		clientSocket1.close();
		in2.close();
		out2.close();
		clientSocket2.close();
	}
}
