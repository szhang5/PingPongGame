
class Paddle {
	Point[] position = new Point[5];

	private int player, x, y;

	boolean upAccel, downAccel;

	public Paddle(int player) {
		upAccel = false;
		downAccel = false;
		y = 12;
		if (player == 1)
			x = 1;
		else
			x = 48;
		int n = position.length;
		for (int i = 0; i < n; i++, y++) {
			position[i] = new Point(x, y);
		}
	}

	public void goUp() {
		if (position[0].getY() > 1) {
			int n = position.length;
			for (int i = 0; i < n; i++)
				position[i].setY(position[i].getY() - 1);
		}
	}

	public void goDown() {
		if (position[4].getY() < 28) {
			int n = position.length;
			for (int i = 0; i < n; i++)
				position[i].setY(position[i].getY() + 1);
		}
	}

	public int getX() {
		return x;
	}

	public Point[] getPosition() {
		return position;
	}
}
