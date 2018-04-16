import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class PingPongGameClient {

	private JFrame frame = new JFrame("PingPong Game");
	private JLabel messageLabel = new JLabel("");
	private ImageIcon icon;
	private ImageIcon opponentIcon;

	private Square[] board = new Square[9];
	private Square currentSquare;

	private static int PORT = 8901;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;


	public PingPongGameClient(String serverAddress) throws Exception {

		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		messageLabel.setBackground(Color.lightGray);
		frame.getContentPane().add(messageLabel, "South");

		JPanel boardPanel = new JPanel();
		boardPanel.setBackground(Color.black);
		boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
		for (int i = 0; i < board.length; i++) {
			final int j = i;
			board[i] = new Square();
			board[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					currentSquare = board[j];
					System.out.println(j);
					System.out.println(currentSquare);
					out.println("MOVE " + j);
				}
			});
			boardPanel.add(board[i]);
		}
		frame.getContentPane().add(boardPanel, "Center");
	}

	public void play() throws Exception {
		String response;
		String xGif = "/Users/zhangshiyun/eclipse-workspace/TicTacToe/src/x.png";
		String oGif = "/Users/zhangshiyun/eclipse-workspace/TicTacToe/src/o.png";
		try {
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				char mark = response.charAt(8);
				icon = new ImageIcon(mark == 'X' ? xGif : oGif);
				System.out.println(icon);
				System.out.println(response);
				opponentIcon = new ImageIcon(mark == 'X' ? oGif : xGif);
				frame.setTitle("Tic Tac Toe - Player " + mark);
			}
			while (true) {
				response = in.readLine();
				if (response.startsWith("VALID_MOVE")) {
					messageLabel.setText("Valid move, please wait");
					currentSquare.setIcon(icon);
					currentSquare.repaint();
				} else if (response.startsWith("OPPONENT_MOVED")) {
					int loc = Integer.parseInt(response.substring(15));
					board[loc].setIcon(opponentIcon);
					board[loc].repaint();
					messageLabel.setText("Opponent moved, your turn");
				} else if (response.startsWith("VICTORY")) {
					messageLabel.setText("You win");
					break;
				} else if (response.startsWith("DEFEAT")) {
					messageLabel.setText("You lose");
					break;
				} else if (response.startsWith("TIE")) {
					messageLabel.setText("You tied");
					break;
				} else if (response.startsWith("MESSAGE")) {
					messageLabel.setText(response.substring(8));
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

	static class Square extends JPanel {
		JLabel label = new JLabel((Icon) null);

		public Square() {
			setBackground(Color.white);
			add(label);
		}

		public void setIcon(Icon icon) {
			label.setIcon(icon);
		}
	}

	public static void main(String[] args) throws Exception {
		while (true) {
			String serverAddress = (args.length == 0) ? "localhost" : args[1];
			PingPongGameClient client = new PingPongGameClient(serverAddress);
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.frame.setSize(500, 500);
			client.frame.setVisible(true);
			client.frame.setResizable(false);
			client.play();
			if (!client.wantsToPlayAgain()) {
				break;
			}
		}
	}
}

