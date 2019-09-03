package rbadia.voidspace.model;


public class PowerUp extends GameObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	public static final int Y_OFFSET = 14;

	public PowerUp(int xPos, int yPos) {
		super(xPos, yPos, WIDTH, HEIGHT);
	}
}
