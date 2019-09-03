package rbadia.voidspace.model;

import java.awt.Rectangle;

public class Platform extends Rectangle {
	private static final long serialVersionUID = 1L;
	private int ySpeed = 0;

	private static final int WIDTH = 44;
	private static final int HEIGHT = 4;
	
	public Platform(int xPos, int yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
	}

	public void setYSpeed(int speed) {
		this.ySpeed = speed;
	}
	
	public int getYSpeed() {
		return ySpeed;
	}
}
