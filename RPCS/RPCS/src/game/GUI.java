package game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import networking.Client;

//screen1 = enter user name 
//screen2 = first connection to server
//screen3 = main screen

public class GUI {
	public static final String TITLE = "RPCS.exe";
	public static final String IP = "127.0.0.1";//"89.138.149.212";
	public static final int PORT = 33333;

	private String userName;
	private JFrame frame;
	private JPanel panel;
	private GridBagConstraints gc;

	public Client player;

	public void openGUI() {
		frame = new JFrame();
		panel = new JPanel(new GridBagLayout());
		gc = new GridBagConstraints();
		frame.setTitle(TITLE);
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

	private void frameStuff() {
//		System.out.println("frameStuff()");
		frame.add(panel);
		frame.setVisible(true);
	}

	public void Main() {
		screen1();
		while (player == null) {
			System.out.print("");
		}
		;
		while (!player.isConnected()) { // check every 0.5 seconds if player is connected to server
			System.out.println("stuck here2");
			wait(500);
		}
		screen3();
	}

	/*
	 * First screen the user sees. includes: user name entry, exit button, next
	 * button, help button. Gets input from the user, sends it to the server as his
	 * user name and connects the player to the server.
	 */
	private void screen1() {
		clearPanel();
//		System.out.println("screen1()");
		gc.weightx = 0.5;
		gc.weighty = 0.5;

		/// Label Top of screen//////
		JLabel topLB = new JLabel("Welcome To RPCS!");
		topLB.setFont(new Font("Ariel", Font.PLAIN, 45));
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weighty = 2;
		gc.gridwidth = 3;
		panel.add(topLB, gc);

		/// Label above text field////////
		JLabel nameLB = new JLabel("Enter User Name:");
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 1;
		gc.weighty = 2;
		panel.add(nameLB, gc);

		/// User name text field/////////
		JTextField userText = new JTextField(20);
		userText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleUserName(userText); /// if pressed enter
			}
		});
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 2;
		gc.weighty = 1;
		panel.add(userText, gc);

		//// Start button//////////
		JButton nextBTN = new JButton("Next");
		nextBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleUserName(userText); /// if pressed next
			}
		});
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 3;
		gc.weighty = 2;
		panel.add(nextBTN, gc);

		//// Exit button//////////
		gc.weighty = 0;
		JButton exitBTN = new JButton("Exit");
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		gc.anchor = GridBagConstraints.SOUTHWEST;
		gc.gridx = 0;
		gc.gridy = 4;
		panel.add(exitBTN, gc);

		//// Help button//////////
		JButton helpBTN = new JButton("Help");
		helpBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpScreen("screen1");
			}
		});
		gc.anchor = GridBagConstraints.SOUTHEAST;
		gc.gridx = 2;
		gc.gridy = 4;
		panel.add(helpBTN, gc);

		frameStuff();
	}

	private void handleUserName(JTextField userText) {
//		System.out.println("handleUserName()");
		this.userName = userText.getText();
		if (this.userName.length() == 0) {
			return;
		} else {
			System.out.println("Username: " + this.userName);
			screen2();

		}
		return;
	}

	/*
	 * waiting screen to connect to server. includes: displays
	 * "connecting to server" until connection is complete.
	 */
	private void screen2() {
//		System.out.println("screen2()");
		clearPanel();
		frame.setTitle(TITLE + " | " + userName);

		// code for connecting to server
		connect();

		String msg = "Connecting to server";
		JLabel msgLB = new JLabel(msg);
		msgLB.setFont(new Font("Ariel", Font.PLAIN, 40));
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.CENTER;
		panel.add(msgLB, gc);
		frameStuff();
		
	}

	/*
	 * main screen. includes: play button, exit button, help button. waits for the
	 * player to start a game.
	 */
	private void screen3() {
//		System.out.println("screen3()");
		clearPanel();

		ImageIcon image = new ImageIcon("C:\\Users\\Lenovo\\eclipse-workspace\\MyFirstGUI\\src\\main_screen.png");
		JLabel label = new JLabel(image);
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weighty = 0;

		panel.add(label, gc);

		//// Play button//////////
		JButton playBTN = new JButton("Play");
		playBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				screen4();
			}
		});
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 2;
		gc.weighty = 3;
		panel.add(playBTN, gc);

		/// Label Top of screen//////
		JLabel topLB = new JLabel("Welcome " + this.userName);
		topLB.setFont(new Font("Ariel", Font.PLAIN, 20));
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 1;
		gc.weighty = 2;
		gc.gridwidth = 3;
		panel.add(topLB, gc);

		//// Exit button//////////
		gc.weighty = 0;
		JButton exitBTN = new JButton("Exit");
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		gc.anchor = GridBagConstraints.SOUTHWEST;
		gc.gridx = 0;
		gc.gridy = 4;
		panel.add(exitBTN, gc);

		//// Help button//////////
		JButton helpBTN = new JButton("Help");
		helpBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpScreen("screen3");
			}
		});
		gc.anchor = GridBagConstraints.SOUTHEAST;
		gc.gridx = 2;
		gc.gridy = 4;
		panel.add(helpBTN, gc);

		frameStuff();

	}

	/*
	 * waiting screen to start a match. includes: displays "searching for players"
	 * until connection is complete.
	 */
	private void screen4() {
		System.out.println("screen4()");

		clearPanel();
		String msg = "Searching for players";
		JLabel msgLB = new JLabel(msg);
		msgLB.setFont(new Font("Ariel", Font.PLAIN, 40));
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.CENTER;
		panel.add(msgLB, gc);

		//// Exit button//////////
		gc.weighty = 0;
		JButton exitBTN = new JButton("Exit");
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		gc.anchor = GridBagConstraints.SOUTHWEST;
		gc.gridx = 0;
		gc.gridy = 4;
		panel.add(exitBTN, gc);

		frameStuff();

		gameScreen();

//		while (player.game.isAlive()) {wait(500);} // check every 0.5 seconds if player started a match
		while (player.isConnected()) {wait(500);} // check every 0.5 seconds if player started a match
			
		screen5();

	}

	/*
	 * end screen. includes: play again button, exit button, main menu button. waits
	 * for the player to start a game.
	 */
	private void screen5() {
//		System.out.println("screen5()");
		clearPanel();

//		//// Play button//////////
//		JButton playBTN = new JButton("Play Again");
//		playBTN.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				screen4();
//			}
//		});
//		gc.anchor = GridBagConstraints.CENTER;
//		gc.gridx = 1;
//		gc.gridy = 2;
//		gc.weighty = 3;
//		panel.add(playBTN, gc);

		/// Label Top of screen//////
		JLabel topLB = new JLabel(winnerMsg());
		topLB.setFont(new Font("Ariel", Font.PLAIN, 45));
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weighty = 2;
		gc.gridwidth = 3;
		panel.add(topLB, gc);

		//// Exit button//////////
		gc.weighty = 0;
		JButton exitBTN = new JButton("Exit");
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		gc.anchor = GridBagConstraints.SOUTHWEST;
		gc.gridx = 0;
		gc.gridy = 4;
		panel.add(exitBTN, gc);

//		//// Main Menu button//////////
//		JButton menuBTN = new JButton("Main Menu");
//		menuBTN.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				screen3();
//			}
//		});
//		gc.anchor = GridBagConstraints.SOUTHEAST;
//		gc.gridx = 2;
//		gc.gridy = 4;
//		panel.add(menuBTN, gc);

		frameStuff();
	}


	private void helpScreen(String screen) {
		clearPanel();

		/// Label Top of screen//////
		JLabel topLB = new JLabel("Instructions");
		topLB.setFont(new Font("Ariel", Font.PLAIN, 45));
		gc.anchor = GridBagConstraints.NORTH;
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weighty = 2;
		gc.gridwidth = 3;
		panel.add(topLB, gc);

		//// Back button//////////
		JButton backBTN = new JButton("Back");
		backBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (screen == "screen1") {
					screen1();
					return;
				} else if (screen == "screen3") {
					screen3();
					return;
				}
			}
		});
		gc.anchor = GridBagConstraints.SOUTHWEST;
		gc.gridx = 0;
		gc.gridy = 2;
		panel.add(backBTN, gc);
//
//		ImageIcon image = new ImageIcon("/RPCS/resources/textures/lava.png");
//		JLabel label = new JLabel(image);
//		gc.anchor = GridBagConstraints.CENTER;
//		gc.gridx = 1;
//		gc.gridy = 1;
//		gc.weighty = 10;
//
//		panel.add(label, gc);

		frameStuff();
	}

	private void clearPanel() {
		panel.removeAll();
		panel.updateUI();
	}

	/*
	 * should return the winner message.
	 */
	private String winnerMsg() {
		if (player.getWinStatus())
			return "You Win!";
		return "You Lose!";
		
	}
	private void connect() {
		try {
			player = new Client(IP, PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gameScreen() {
		System.out.println("gameScreen()");
		// code for starting a match
		player.start();
		clearPanel();
		frameStuff();
	}

	public void exit() {
		System.exit(0);
	}

	public static void wait(int ms) {
		// System.out.println("wait()");
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static void main(String[] args) {
		GUI myGUI = new GUI();
		myGUI.openGUI();
		myGUI.Main();
	}

}
