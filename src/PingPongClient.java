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

	public PingPongClient(String serverAddress) throws Exception {
		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		Pingpong a = new Pingpong();
		frame.add(a);
		frame.addKeyListener(a);
		
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				t = new Timer(70, new TimerListener(Pingpong.ball));
				t.start();
			}
		});
		
	}

	public static void main(String args[]) throws Exception {
		while(true) {
			String serverAddress = (args.length == 0) ? "localhost" : args[1];
			PingPongClient client = new PingPongClient(serverAddress);

			client.frame.setLocationRelativeTo(null);
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.frame.setSize(750, 500);
			// client.frame.setTitle("Pingpong Test");
			client.frame.setVisible(true);
			client.frame.setResizable(false);
			client.play();
			if (!client.wantsToPlayAgain()) {
				break;
			}
		}
	}

	class TimerListener implements ActionListener {
		private Ball b;

		public TimerListener(Ball b) {
			this.b = b;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				b.ballMove();
			}catch(Exception ee){
				Pingpong.message = "GAME OVER";
				out.println("GAME OVER");
				t.stop();
			}
			

			frame.repaint();
		}
	}

	public void play() throws Exception {
		String response;
		try {
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				char mark = response.charAt(8);
				System.out.println(response);
				frame.setTitle("Ping-Pong Game Player " + mark);
			}
			while(true) {
				response = in.readLine();
//				if(response.equals("START")) {
//					
//				}
				if (response.startsWith("VICTORY")) {
					Pingpong.message = "You win";
					frame.repaint();
					break;
				} else if (response.startsWith("DEFEAT")) {
					Pingpong.message = "You lose";
					frame.repaint();
					break;
				}else if (response.startsWith("MESSAGE")) {
					Pingpong.message = response.substring(8);
					frame.repaint();
				}
			}
			out.println("QUIT");

		} finally {
			socket.close();
		}
	}
	
	private boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(frame, "Want to play again?", "Tic Tac Toe is Fun Fun Fun",
				JOptionPane.YES_NO_OPTION);
		frame.dispose();
		return response == JOptionPane.YES_OPTION;
	}

}
