import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.swing.JPanel;

public class Pingpong extends JPanel implements KeyListener {
	
	private static final int W = 50;
	private static final int H = 30;
	private static int[][] map = new int[W][H];
	
	static Paddle p1;
	static Paddle p2;
	private Ball ball = new Ball();
	public static String message = "";
	private Font mFont = new Font("TimesRoman",Font.BOLD,20);
	private Font sFont = new Font("Arial",Font.BOLD,50);
	private Font nFont = new Font("Arial",Font.BOLD,30);
	private PrintWriter out; 
	int score1;
	int score2;
	String player1 = "";
	String player2 = "";


	public Pingpong(PrintWriter pw, int s1, int s2) {
		newMap();
		newPaddle();
		out = pw;
		score1 = s1;
		score2 = s2;
	}

	public Ball getBall() {
		return ball;
	}

	public int getH() {
		return H;
	}

	public int getW() {
		return W;
	}
	
	//************************//
	// - Map Initialization - //
	//************************//
	public void newMap() {
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				if (i == 0 || i == W-1 || j == 0 || j == H-1)
					/* - 1 stands for wall - */
					map[i][j] = 1; 
				else if (i == (W/2) && j % 2 == 0)
					/* - 2 stands for middle line - */
					map[i][j] = 2;
				else
					map[i][j] = 0;
			}
		}
	}

	public static int[][] getMap() {
		return map;
	}

	//***************************//
	// - Paddle Initialization - //
	//***************************//
	public void newPaddle() {
		p1 = new Paddle(1);
		p2 = new Paddle(2);
		for (int i = 0; i < 4; i++) {
			map[1][p1.position[i].getY()] = 3;
			map[48][p2.position[i].getY()] = 4;
		}
	}

	//**********************//
	// - Paddle 1 Move Up - //
	//**********************//
	public void LeftUp() {
		for (int i = 1; i < H-1; i++) {
			map[1][i] = 0;
		}
		p1.goUp();
		for (int i = 0; i < 4; i++) {
			map[1][p1.position[i].getY()] = 3;
		}
		repaint();
	}
	
	//************************//
	// - Paddle 1 Move Down - //
	//************************//
	public void LeftDown() {
		for (int i = 1; i < H-1; i++) {
			map[1][i] = 0;
		}
		p1.goDown();
		for (int i = 0; i < 4; i++) {
			map[1][p1.position[i].getY()] = 3;
		}
		repaint();
	}

	//**********************//
	// - Paddle 2 Move Up - //
	//**********************//
	public void RightUp() {
		for (int i = 1; i < H-1; i++) {
			map[48][i] = 0;
		}
		p2.goUp();
		for (int i = 0; i < 4; i++) {
			map[48][p2.position[i].getY()] = 4;
		}
		repaint();
	}

	//************************//
	// - Paddle 2 Move Down - //
	//************************//
	public void RightDown() {
		for (int i = 1; i < H-1; i++) {
			map[48][i] = 0;
		}
		p2.goDown();
		for (int i = 0; i < 4; i++) {
			map[48][p2.position[i].getY()] = 4;
		}
		repaint();
	}

	//*****************//
	// - KeyListener - //
	//*****************//
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_UP) {
			out.println("UP");	
		}
		if (key == KeyEvent.VK_DOWN) {
			out.println("DOWN");	
		}		
	}

	public void keyReleased(KeyEvent e) {

	}

	//*********************//
	// - Paint Component - //
	//*********************//
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.black);

		g.setFont(mFont);
		g.drawString(message, 0, 472);
		
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				if (map[i][j] == 0) {
					g.fillRect(i * 15, j * 15, 15, 15);
				}
				/* - 1 stands for wall - */
				if (map[i][j] == 1) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(i * 15, j * 15, 15, 15);
					g.setColor(Color.black);
				}
				/* - 2 stands for middle line - */
				if (map[i][j] == 2) {
					g.fillRect(i * 15, j * 15, 10, 15);
				}
				/* - 3 stands for Paddle 1 - */
				if (map[i][j] == 3) {
					g.setColor(Color.cyan);
					g.fillRect(i * 15, j * 15, 15, 15);
					g.setColor(Color.black);
				}
				/* - 4 stands for Paddle 2 - */
				if (map[i][j] == 4) {
					g.setColor(Color.red);
					g.fillRect(i * 15, j * 15, 15, 15);
					g.setColor(Color.black);
				}
				if (i == ball.getX() && j == ball.getY()) {
					g.setColor(Color.yellow);
					g.fillOval(i * 15, j * 15, 15, 15);
					g.setColor(Color.black);
				}

			}
		}
		g.setColor(Color.green);
		g.setFont(sFont);
		g.drawString(Integer.toString(score1), 180, 100);
		g.drawString(Integer.toString(score2), 570, 100);
		
		g.setColor(Color.pink);
		g.setFont(nFont);
		g.drawString(player1, 150, 350);
		g.drawString(player2, 530, 350);
		
	}

	//**********************//
	// - Update Component - //
	//**********************//
	public void updatePaddle1(int[] loc) {
		for (int i = 1; i < H-1; i++) {
			map[1][i] = 0;
		}
		for (int i = 0; i < 4; i++) {
			map[1][loc[i]] = 3;
		}
		repaint();
	}

	public void updatePaddle2(int[] loc) {
		for (int i = 1; i < H-1; i++) {
			map[48][i] = 0;
		}
		for (int i = 0; i < 4; i++) {
			map[48][loc[i]] = 4;
		}
		repaint();
	}

	public void updateBall(int[] loc) {
		ball.setX(loc[0]);
		ball.setY(loc[1]);
		repaint();
	}
	
	//************************//
	// - Update Player Move - //
	//************************//
	public void moveUp(String mark) {
		if(mark.equals("1")) {
			LeftUp();
			out.println("Paddle1 Move: " + Arrays.toString(p1.getPosition()));
		} else if(mark.equals("2")) {
			RightUp();
			out.println("Paddle2 Move: " + Arrays.toString(p2.getPosition()));
		}
	}
	
	public void moveDown(String mark) {
		if(mark.equals("1")) {
			LeftDown();
			out.println("Paddle1 Move: " + Arrays.toString(p1.getPosition()));
		} else if(mark.equals("2")) {
			RightDown();
			out.println("Paddle2 Move: " + Arrays.toString(p2.getPosition()));
		}
	}
	
	//******************//
	// - Update Score - //
	//******************//
	public void updateScore1(int s1) {
		score1 = s1;
	}
	public void updateScore2(int s2) {
		score2 = s2;
	}
	
	//**********************//
	// - Update Nick Name - //
	//**********************//
	public void updatePlayerName1(String name1) {
		player1 += name1;
		
	}
	public void updatePlayerName2(String name2) {
		player2 += name2;
	}
	

}
