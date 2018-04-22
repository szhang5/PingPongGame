import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PingPongServer {

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(6666);
		System.out.println("Ping-Pong Server is Running");

		// Socket socket = listener.accept();
		try {
			while (true) {
				Game game = new Game();
				Game.Player playerB = game.new Player(listener.accept(), 'B');
				Game.Player playerR = game.new Player(listener.accept(), 'R');
				playerB.setOpponent(playerR);
				playerR.setOpponent(playerB);
				game.currentPlayer = playerB;
				playerB.start();
				playerR.start();
			}
		} finally {
			listener.close();
		}
	}
}

class Game {

	Player currentPlayer;

	public synchronized boolean legalMove(Player player) {
		int[][] newMap = Pingpong.getMap();
		if (player == currentPlayer) {
			currentPlayer = currentPlayer.opponent;
			return true;
		}
		return false;
	}

	class Player extends Thread {
		char mark;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		public Player(Socket socket, char mark) {
			this.socket = socket;
			this.mark = mark;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("WELCOME " + mark);
				output.println("MESSAGE Waiting for opponent to connect");
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			}
		}

		public boolean hasWinner() {
			boolean result = false;
			String respone;
			try {
				respone = input.readLine();
				if (respone.equals("GAME OVER")) {
					result = true;
				}
			} catch (Exception e) {
			}
			return result;

		}

		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		public void otherPlayerMoved() {
			output.println(hasWinner() ? "DEFEAT" : "");
		}

		public void run() {
			try {
				output.println("MESSAGE All players connected");
				

				if (mark == 'B') {
					output.println("START");
				}

				while (true) {
					String command = input.readLine();
					System.out.println(command);

					if (legalMove(this)) {
						output.println(hasWinner() ? "VICTORY" : "");
					} else if (command.startsWith("QUIT")) {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
