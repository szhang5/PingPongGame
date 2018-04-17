import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class PingPongClient {
	private JFrame frame = new JFrame("Pingpong Game");

	private static int PORT = 6666;
	private Socket socket;


	public PingPongClient(String serverAddress) throws Exception {
		socket = new Socket(serverAddress, PORT);

		Pingpong a = new Pingpong();
		frame.add(a);
		frame.addKeyListener(a);
		Timer t = new Timer(70, new TimerListener(Pingpong.ball));
		t.start();
	}

	public static void main(String args[]) throws Exception {
		String serverAddress = (args.length == 0) ? "localhost" : args[1];
		PingPongClient client = new PingPongClient(serverAddress);
		client.frame.setLocationRelativeTo(null);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(750, 472);
		client.frame.setTitle("Pingpong Test");
		client.frame.setVisible(true);
		client.frame.setResizable(false);
	}

	class TimerListener implements ActionListener {
		private Ball b;

		public TimerListener(Ball b) {
			this.b = b;
		}

		public void actionPerformed(ActionEvent e) {
			b.ballMove();

			frame.repaint();
		}
	}
}
