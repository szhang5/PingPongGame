import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;

class PingPongClient {
	private JFrame frame = new JFrame("Pingpong Game");

	private static int PORT = 6666;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Timer t;
	private Pingpong a;
	private int score1 = 0;
	private int score2 = 0;
	private static String serverIP;
	private static String nickName;

	public PingPongClient(String serverAddress) throws Exception {
		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		a = new Pingpong(out, score1, score2); 
		frame.add(a);
		frame.addKeyListener(a);
		
		/* - Mouse Listener - */
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				out.println("Mouse Click");
				if(a.getBall().getX() == a.getW()/2) {
					t = new Timer(70, new TimerListener(a.getBall()));
					t.start();
				}
			}
		});
	}

	public static void main(String args[]) throws Exception {
		while (true) {
			nickName = JOptionPane.showInputDialog(null, "Nick name:", "Enter server name:", 1);
			serverIP = JOptionPane.showInputDialog(null, "ex. 127.0.0.1", "Enter server IP:", 1);
			PingPongClient client = new PingPongClient(serverIP);
			/* - JFrame Setting - */
			client.frame.setLocationRelativeTo(null);
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.frame.setSize(750, 500);
			client.frame.setVisible(true);
			client.frame.setResizable(false);
			
			client.play();
			if (!client.wantsToPlayAgain()) {
				break;
			}
		}
	}

	//*******************//
	// - Time Listener - //
	//*******************//
	class TimerListener implements ActionListener {
		private Ball b;

		public TimerListener(Ball b) {
			this.b = b;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				b.ballMove();
				out.println("Ball Move: " + b.getX() + " " + b.getY());
			} catch (Exception ee) {
				if (b.getX() > a.getW()-1) 
					out.println("Player1 get one point");
				if (b.getX() < 1) 
					out.println("Player2 get one point");
				t.stop();
			}
			frame.repaint();
		}

	}

	//*******************************************//
	// - Client receives response from Server  - //
	//*******************************************//
	public void play() throws Exception {
		String response;
		try {
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				char mark = response.charAt(8);
				out.println("NICKNAME: " + mark + nickName);
				System.out.println(response);
				frame.setTitle("Ping-Pong Game Player " + mark);
			}
			while (true) {
				response = in.readLine();
				if (response.startsWith("NickName: 1")) {
					String name = response.substring(11);
					a.updatePlayerName1(name);
				}
				if (response.startsWith("NickName: 2")) {
					String name = response.substring(11);
					a.updatePlayerName2(name);
				}
				if (response.startsWith("UP ")) {
					String player = response.substring(3);
					a.moveUp(player);	
				}else if (response.startsWith("DOWN ")) {
					String player = response.substring(5);
					a.moveDown(player);	
					
				}else if (response.equals("Mouse Click")) {
					Pingpong.message = "";
					frame.repaint();
				}else if (response.startsWith("Paddle1 Move: ")) {
					String paddle1 = response.substring(15, response.length() - 1);
					String[] tmp = paddle1.split(", ");
					int[] loc = new int[tmp.length];
					for (int i = 0; i < tmp.length; i++) {
						loc[i] = Integer.parseInt(tmp[i]);
					}
					a.updatePaddle1(loc);
				} else if (response.startsWith("Paddle2 Move: ")) {
					String paddle2 = response.substring(15, response.length() - 1);
					String[] tmp = paddle2.split(", ");
					int[] loc = new int[tmp.length];
					for (int i = 0; i < tmp.length; i++) {
						loc[i] = Integer.parseInt(tmp[i]);
					}
					a.updatePaddle2(loc);
				} else if (response.startsWith("Ball Move: ")) {
					String ball = response.substring(11);
					String[] tmp = ball.split(" ");
					int[] loc = new int[tmp.length];
					for (int i = 0; i < tmp.length; i++) {
						loc[i] = Integer.parseInt(tmp[i]);
					}
					a.updateBall(loc);
				} else if (response.startsWith("MESSAGE")) {
					Pingpong.message = response.substring(8);
					frame.repaint();
				} else if(response.startsWith("Player1: ")) {
					score1 = Integer.parseInt(response.substring(9));
					a.updateScore1(score1);
					a.getBall().setX(25);
					a.getBall().setY(15);
					Pingpong.message = "Click your mouse to start";
					frame.repaint();
				} else if(response.startsWith("Player2: ")) {
					score2 = Integer.parseInt(response.substring(9));
					a.updateScore2(score2);
					a.getBall().setX(25);
					a.getBall().setY(15);
					Pingpong.message = "Click your mouse to start";
					frame.repaint();
				} else if (response.equals("You Win!")) {
					Pingpong.message = response;
					frame.repaint();
					break;
				}else if (response.equals("You Lose!")) {
					Pingpong.message = response;
					frame.repaint();
					break;
				}
			}
			out.println("QUIT");

		} finally {
			socket.close();
		}
	}

	//*************************//
	// - Want to play again? - //
	//*************************//
	private boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(frame, "Want to play again?", "Ping-Pong is Fun Fun Fun",
				JOptionPane.YES_NO_OPTION);
		frame.dispose();
		return response == JOptionPane.YES_OPTION;
	}

}
