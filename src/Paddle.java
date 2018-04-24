
class Paddle {
	Point[] position = new Point[4];

	private int x, y;

	public Paddle(int player) {
		y = 13;
		if (player == 1)
			x = 1;
		else
			x = 48;
		int n = position.length;
		for (int i = 0; i < n; i++, y++) {
			position[i] = new Point(x, y);
		}
	}

	//********************//
	// - Paddle Move Up - //
	//********************//
	public void goUp() {
		int n = position.length;
		if (position[0].getY() > 1) {
			for (int i = 0; i < n; i++)
				position[i].setY(position[i].getY() - 3);
		}
	}

	//**********************//
	// - Paddle Move Down - //
	//**********************//
	public void goDown() {
		int n = position.length;
		if (position[n-1].getY() < 28) {
			for (int i = 0; i < n; i++)
				position[i].setY(position[i].getY() + 3);
		}
	}

	public int getX() {
		return x;
	}

	public Point[] getPosition() {
		return position;
	}
}
