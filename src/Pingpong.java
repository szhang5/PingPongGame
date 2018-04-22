import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import javax.swing.JPanel;

public class Pingpong extends JPanel implements KeyListener {
	static int[][] map = new int[50][30];
	Paddle paddle1 = new Paddle(1, 12);
	Paddle paddle2 = new Paddle(48, 12);
	public static Ball ball = new Ball(25, 15);
	public static String message = "";

	public Pingpong() {
		newMap();
		newPaddle();
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

		for (int i = 0; i < 5; i++) {
			map[paddle1.position[i].getX()][paddle1.position[i].getY()] = 3;
			map[paddle2.position[i].getX()][paddle2.position[i].getY()] = 4;
		}
		// System.out.println(Arrays.deepToString(map));
	}

	public void LeftUp() {
		map[paddle1.position[4].getX()][paddle1.position[4].getY()] = 0;
		paddle1.goUp();
		for (int i = 0; i < 5; i++) {
			map[paddle1.position[i].getX()][paddle1.position[i].getY()] = 3;
		}
		repaint();
	}

	public void LeftDown() {
		map[paddle1.position[0].getX()][paddle1.position[0].getY()] = 0;
		paddle1.goDown();
		for (int i = 0; i < 5; i++) {
			map[paddle1.position[i].getX()][paddle1.position[i].getY()] = 3;
		}
		repaint();
	}

	public void RightUp() {
		map[paddle2.position[4].getX()][paddle2.position[4].getY()] = 0;
		paddle2.goUp();
		for (int i = 0; i < 5; i++) {
			map[paddle2.position[i].getX()][paddle2.position[i].getY()] = 4;
		}
		repaint();
	}

	public void RightDown() {
		map[paddle2.position[0].getX()][paddle2.position[0].getY()] = 0;
		paddle2.goDown();
		for (int i = 0; i < 5; i++) {
			map[paddle2.position[i].getX()][paddle2.position[i].getY()] = 4;
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W)
			LeftUp();
		if (key == KeyEvent.VK_S)
			LeftDown();
		if (key == KeyEvent.VK_UP)
			RightUp();
		if (key == KeyEvent.VK_DOWN)
			RightDown();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

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
}
