
class Paddle {
	Point[] position = new Point[5];

	public Paddle(int x, int y) {
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
}
