import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.swing.JPanel;

public class Pingpong extends JPanel implements KeyListener {
	static int[][] map = new int[50][30];
	static Paddle p1;
	static Paddle p2;
	private Ball ball = new Ball();
	public static String message = "";
	private PrintWriter out;

	public Pingpong(PrintWriter pw) {
		newMap();
		newPaddle();
		out = pw;
	}

	public Ball getBall() {
		return ball;
	}

	public void newMap() {
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 30; j++) {
				if (i == 0 || i == 49 || j == 0 || j == 29)
					map[i][j] = 1; // 1 stands for wall;
				else if (i == 25 && j % 2 == 0)
					map[i][j] = 2;
				else
					map[i][j] = 0;
			}
		}
		// System.out.println(Arrays.deepToString(map));
	}

	public static int[][] getMap() {
		return map;
	}

	public void newPaddle() {
		p1 = new Paddle(1);
		p2 = new Paddle(2);
		for (int i = 0; i < 5; i++) {
			map[1][p1.position[i].getY()] = 3;
			map[48][p2.position[i].getY()] = 4;
		}
	}

	public void LeftUp() {
		for (int i = 1; i < 29; i++) {
			map[1][i] = 0;
		}
		p1.goUp();
		for (int i = 0; i < 5; i++) {
			map[1][p1.position[i].getY()] = 3;
		}
		repaint();
	}

	public void LeftDown() {
		for (int i = 1; i < 29; i++) {
			map[1][i] = 0;
		}
		p1.goDown();
		for (int i = 0; i < 5; i++) {
			map[1][p1.position[i].getY()] = 3;
		}
		repaint();
	}

	public void RightUp() {
		for (int i = 1; i < 29; i++) {
			map[48][i] = 0;
		}
		p2.goUp();
		for (int i = 0; i < 5; i++) {
			map[48][p2.position[i].getY()] = 4;
		}
		repaint();
	}

	public void RightDown() {
		for (int i = 1; i < 29; i++) {
			map[48][i] = 0;
		}
		p2.goDown();
		for (int i = 0; i < 5; i++) {
			map[48][p2.position[i].getY()] = 4;
		}
		repaint();
	}

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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.black);

		g.drawString(message, 0, 472);

		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 30; j++) {
				if (map[i][j] == 0) {
					g.fillRect(i * 15, j * 15, 15, 15);
				}
				if (map[i][j] == 1) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(i * 15, j * 15, 15, 15);
					g.setColor(Color.black);
				}
				if (map[i][j] == 2) {
					g.fillRect(i * 15, j * 15, 10, 15);
				}
				if (map[i][j] == 3) {
					g.setColor(Color.cyan);
					g.fillRect(i * 15, j * 15, 15, 15);
					g.setColor(Color.black);
				}
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
	}

	public void updatePaddle1(int[] loc) {
		for (int i = 1; i < 29; i++) {
			map[1][i] = 0;
		}
		for (int i = 0; i < 5; i++) {
			map[1][loc[i]] = 3;
		}
		repaint();
	}

	public void updatePaddle2(int[] loc) {
		for (int i = 1; i < 29; i++) {
			map[48][i] = 0;
		}
		for (int i = 0; i < 5; i++) {
			map[48][loc[i]] = 4;
		}
		repaint();
	}

	public void updateBall(int[] loc) {
		ball.setX(loc[0]);
		ball.setY(loc[1]);
		repaint();
	}
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

}
