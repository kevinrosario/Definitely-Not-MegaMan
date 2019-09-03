package rbadia.voidspace.main;
import java.awt.Graphics2D;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.PowerUp;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Level very similar to LevelState1.  
 * Platforms arranged in triangular form. 
 * Asteroids travel at 225 degree angle
 */
public class Level2State extends Level1State {

	private static final long serialVersionUID = -2094575762243216079L;
	private int[] initialPosition = new int[getNumPlatforms()];
	// Constructors
	public Level2State(int level, MainFrame frame, GameStatus status, 
			LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}

	@Override
	public void doStart() {	
		super.doStart();
		setNumOfPlatforms(7);
		newPlatforms(7);
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
	}

	@Override
	protected void drawMegaMan() {
		super.drawMegaMan();
		MegaMan megaMan =getMegaMan();
		if(Fall() == false) {
			megaMan.translate(0, -1);
		}
	}

	protected void clearScreen() {
		Graphics2D g2d = getGraphics2D();
		g2d.drawImage(getGraphicsManager().getMoonBackgroud(), 0, 0, this);
	}

	/***
	 * Draw platforms like a stair.
	 */
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i==0) {
				platforms[i].setLocation(((SCREEN_WIDTH)/(8))- platforms[i].width, (SCREEN_HEIGHT)/(8));
				initialPosition[i] = ((1)*SCREEN_HEIGHT)/(8) -25;
			} else if(i%2==1) {
				platforms[i].setLocation((((8-i)*SCREEN_WIDTH)/(8))- platforms[i].width, ((8-i)*SCREEN_HEIGHT)/(8));
				initialPosition[i] = ((8-i)*SCREEN_HEIGHT)/(8) - 25;
			} else if (i%2==0){
				platforms[i].setLocation((((8-i)*SCREEN_WIDTH)/(8))- platforms[i].width, ((8-i)*SCREEN_HEIGHT)/(8));
				initialPosition[i] = ((8-i)*SCREEN_HEIGHT)/(8) + 25;
			}
		}
		return platforms;
	}

	protected void checkBullletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for (Asteroid asteroid : asteroids) {
			for(int i=0; i<bullets.size(); i++){
				Bullet bullet = bullets.get(i);
				if(asteroid.intersects(bullet)){
					// increase asteroids destroyed count
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
					removeAsteroid(asteroid);
					this.getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, getGraphics2D(), this);

					if(powerUpOfLevel == 0 && rand.nextBoolean()) { //creates only 1 randomly powerUp per level
						powerUp = new PowerUp(((SCREEN_WIDTH)/(8)-22),0); //get the coordinated of the asteroid destroyed
						powerUpOfLevel =+1;
						System.out.println("Power Up released");
					}
					// remove bullet
					bullets.remove(i);
					break;
				}
			}
		}
	}

	protected void drawPlatforms() {
		//draw platforms
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<getNumPlatforms(); i++){
			getGraphicsManager().drawPlatform3(platforms[i], g2d, this, i);
		}
	}

	/***
	 * This method moves platforms up and down.
	 */
	public void movePlatform() {
		Platform[] platforms = getPlatforms();
		for (int i=0; i<platforms.length; i++) {

			if(platforms[i].getY() <= initialPosition[i]) {				
				platforms[i].setYSpeed(1);
				platforms[i].translate(0, 1);
				if (platforms[i].getY() == initialPosition[i]) {
					initialPosition[i] = initialPosition[i] -50;}
			} else if (platforms[i].getY() >= initialPosition[i] ) {
				platforms[i].setYSpeed(-1);
				platforms[i].translate(0, -1);
				if (platforms[i].getY() == initialPosition[i]) {
					initialPosition[i] = initialPosition[i] + 50;
				}
			}
		}
	}

	/***
	 * Set the Score to 700.
	 */
	@Override
	public boolean isLevelWon() {
		// TODO Auto-generated method stub
		return getGameStatus().getAsteroidsDestroyed() >= 700;
	}
}
