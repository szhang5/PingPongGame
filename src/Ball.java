
public class Ball {
	private int x;
	private int y;
	int m = 1;
	int n = 1;

	public Ball() {
		this.x = 25;
		this.y = 15;

	}

	public String toString() {
		return "(" + x + ". " + y + ")";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void ballMove() {
		int[][] newMap = Pingpong.getMap();

		x += m;
		y += n;
		if (newMap[x][y] == 4) {
			m = -m;
		}
		if (newMap[x][y] == 3) {
			m = 1;
		}
		// if (x >= 48) {
		// m = -m;
		// }
		// if (x <= 1) {
		// m = 1;
		// }
		if (y >= 28) {
			n = -n;
		}
		if (y <= 1) {
			n = 1;
		}

	}
}
