
public class Ball {
	private int x;
	private int y;
	int m = 1;
	int n = 1;

	public Ball() {
		/* - Initial ball in the middle of the map - */
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

	//***************//
	// - Ball Move - //
	//***************//
	public void ballMove() {
		int[][] newMap = Pingpong.getMap();

		x += m;
		y += n;
		
		/* - When ball hits paddle 2 - */
		if (newMap[x][y] == 4) {
			m = -m;
		}
		/* - When ball hits paddle 1 - */
		if (newMap[x][y] == 3) {
			m = 1;
		}
		/* - When ball hits the wall - */
		if (y >= 28) {
			n = -n;
		}
		/* - When ball hits the wall - */
		if (y <= 1) {
			n = 1;
		}

	}
}
