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

		try {
			while (true) {
				Game game = new Game();
				Game.Player player1 = game.new Player(listener.accept(), '1', 0);
				Game.Player player2 = game.new Player(listener.accept(), '2', 0);
				
				/*set opponent*/
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				game.currentPlayer = player1;
				
				/*start thread*/
				player1.start();
				player2.start();
			}
		} finally {
			listener.close();
		}
	}
}

class Game {

	Player currentPlayer;

	class Player extends Thread {
		char mark;
		int score;
		String nickName;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		public Player(Socket socket, char mark, int score) {
			this.socket = socket;
			this.mark = mark;
			this.score = score;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("WELCOME " + mark);
				output.println("MESSAGE Waiting for opponent to connect");
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			}
		}

		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		public void run() {

			try {
				output.println("MESSAGE All players connected");	
				output.println("MESSAGE Click Your Mouse to Start");
				

				//******************************************//
				// - Server receives request from clients - //
				//******************************************//
				while (true) {
					String command = input.readLine();
					if(command.startsWith("NICKNAME: 1")) {
						if(this.mark == '1') {
							this.nickName = command.substring(11);
							output.println("NickName: 1" + this.nickName);
							updateOppnent("NickName: 1" + this.nickName);
						} else if(this.mark == '2') {
							this.opponent.nickName = command.substring(11);
							output.println("NickName: 1" + this.opponent.nickName);
							updateOppnent("NickName: 1" + this.opponent.nickName);
						}
					}
					if(command.startsWith("NICKNAME: 2")) {
						if(this.mark == '2') {
							this.nickName = command.substring(11);
							output.println("NickName: 2" + this.nickName);
							updateOppnent("NickName: 2" + this.nickName);
						} else if(this.mark == '1') {
							this.opponent.nickName = command.substring(11);
							output.println("NickName: 2" + this.opponent.nickName);
							updateOppnent("NickName: 2" + this.opponent.nickName);
						}
					}
					if (command.equals("UP")) {
						output.println("UP " + this.mark);
					}
					if (command.equals("DOWN")) {
						output.println("DOWN " + this.mark);
					}
					if(command.equals("Mouse Click")) {
						updateOppnent(command);
						output.println(command);
					}
					if (command.startsWith("Ball Move: ")) {
						updateOppnent(command);
					}
					if (command.startsWith("Paddle1 Move: ")) {
						updateOppnent(command);
					}
					if (command.startsWith("Paddle2 Move: ")) {
						updateOppnent(command);
					}
					if (command.startsWith("GAME OVER: ")) {
						output.println(command);
						updateOppnent(command);
					}
					if (command.equals("Player1 get one point")) {
						if(this.mark == '1') {
							this.score++;
							updateOppnent("Player1: " + this.score);
							output.println("Player1: " + this.score);
						} else {
							this.opponent.score++;
							updateOppnent("Player1: " + this.opponent.score);
							output.println("Player1: " + this.opponent.score);
						}
						checkWinner();
					}
					if (command.equals("Player2 get one point")) {
						if(this.mark == '2') {
							this.score++;
							updateOppnent("Player2: " + this.score);
							output.println("Player2: " + this.score);
						} else {
							this.opponent.score++;
							updateOppnent("Player2: " + this.opponent.score);
							output.println("Player2: " + this.opponent.score);
						}
						checkWinner();
					}
					if (command.startsWith("QUIT"))
						break;
				}
			} catch (IOException e) {
				System.out.println("GAME OVER " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}

		//***************************************************************************//
		// - Server receives request from client but response to client's opponent - //
		//***************************************************************************//
		public void updateOppnent(String message) {
			currentPlayer = this.opponent;
			currentPlayer.otherOppnent(message);
		}

		public void otherOppnent(String message) {
			output.println(message);
		}
		
		public void checkWinner() {
			if(this.score == 3) {
				updateOppnent("You Lose!");
				output.println("You Win!");
			}else if(this.opponent.score == 3) {
				updateOppnent("You Win!");
				output.println("You Lose!");
			}
		}
	}
}
