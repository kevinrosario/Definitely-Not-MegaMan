package rbadia.voidspace.model;

public class Boss extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_SPEED = 5;
	public static final int Y_OFFSET = 14;

	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	//public static final int speed = DEFAULT_SPEED;

	public Boss(int xPos, int yPos){
		super(xPos, yPos, WIDTH, HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
	}

	/**
	 * Returns the default ship speed.
	 * @return the default ship speed
	 */
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}

}
