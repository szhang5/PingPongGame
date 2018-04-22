import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
				Game.Player playerB = game.new Player(listener.accept(), 'B', Pingpong.p1);
				Game.Player playerR = game.new Player(listener.accept(), 'R', Pingpong.p2);
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

class Game  {

	Player currentPlayer;

	class Player extends Thread{
		char mark;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;
		Paddle p;

		public Player(Socket socket, char mark, Paddle p) {
			this.socket = socket;
			this.mark = mark;
			this.p = p;
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

		

		public void run() {
			
			try {
				output.println("MESSAGE All players connected");

				if (mark == 'B') {
					output.println("Right Click Your Mouse to Start");
				}
				

				while (true) {
					String command = input.readLine();
					
					if(command.startsWith("Ball Move: ")) {
						System.out.println(command);
						moveBall(command);
					}

					if(command.startsWith("Paddle1 Move: ")) {
						movePaddle(command);
					}
					if(command.startsWith("Paddle2 Move: ")) {
						movePaddle(command);
					}
					if(command.startsWith("QUIT"))
						break;
				
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
		
		public void movePaddle(String pos) {
			currentPlayer = this.opponent;
			currentPlayer.otherPaddleMoved(pos);
		}
		
		public void moveBall(String pos) {
			currentPlayer = this.opponent;
			currentPlayer.otherPaddleMoved(pos);
		}
		
		public void otherPaddleMoved(String location) {
			output.println(location);
		}
	}
}
