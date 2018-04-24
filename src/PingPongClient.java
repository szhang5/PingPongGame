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

	public PingPongClient(String serverAddress) throws Exception {
		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		a = new Pingpong(out); 
		frame.add(a);
		frame.addKeyListener(a);
		
		/* - Mouse Listener - */
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(a.getBall().getX() == a.getW()/2) {
					t = new Timer(100, new TimerListener(a.getBall()));
					t.start();
				}
			}
		});
	}

	public static void main(String args[]) throws Exception {
		while (true) {
			/* - Change "127.0.0.1" to your server IP address - */
			String serverAddress = "127.0.0.1"; 
			PingPongClient client = new PingPongClient(serverAddress);
			
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
					score1++;
				if (b.getX() < 1)
					score2++;
				if (score1 < 3 && score2 < 3) 
					out.println("Score: Player 1 : Player 2 = " + score1 + " : " + score2);				
				else if (score1 == 3)
					out.println("GAME OVER: Player 1 win");
				else if (score2 == 3)
					out.println("GAME OVER: Player 2 win");
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
				System.out.println(response);
				frame.setTitle("Ping-Pong Game Player " + mark);
			}
			while (true) {
				response = in.readLine();
				if (response.startsWith("UP ")) {
					String player = response.substring(3);
					a.moveUp(player);	
				}else if (response.startsWith("DOWN ")) {
					String player = response.substring(5);
					a.moveDown(player);	
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
				} else if (response.startsWith("Score: ")) {
					score1 = Integer.parseInt(response.substring(29, 30));
					score2 = Integer.parseInt(response.substring(33));
					System.out.println(score1 + ":" + score2);
					a.getBall().setX(25);
					a.getBall().setY(15);
					Pingpong.message = response.substring(7);
					frame.repaint();
				} else if (response.startsWith("GAME OVER: ")) {
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
