package rbadia.voidspace.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;
import rbadia.voidspace.model.PowerUp;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class Level1State extends LevelState {

	private static final long serialVersionUID = 1L;
	//protected GraphicsManager graphicsManager;
	protected BufferedImage backBuffer;
	protected MegaMan megaMan;
	protected PowerUp powerUp;
	protected Asteroid[] asteroids;
	protected List<Bullet> bullets;
	protected List<BigBullet> bigBullets;
	protected Floor[] floor;	
	protected int numPlatforms=8;
	protected Platform[] platforms;	
	protected int megaManSide = 0;
	protected static final int NEW_MEGAMAN_DELAY = 500;
	protected static final int NEW_ASTEROID_DELAY = 500;
	protected long lastAsteroidTime;
	protected long lastLifeTime;
	protected int powerUpOfLevel=0;
	protected Rectangle asteroidExplosion;
	protected Random rand;
	protected Font originalFont;
	protected Font bigFont;
	protected Font biggestFont;	
	protected Graphics2D g2dOut = getGraphics2D();
	private int[] initialPlatformPositions;
	protected int[] initialAsteroidsXPositions;
	protected int[] initialAsteroidsYPositions;
	protected int numOfAsteroids;



	// Constructors
	public Level1State(int level, MainFrame frame, GameStatus status, 
			LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super();
		this.setSize(new Dimension((int)(frame.getWidth()*0.9), (int)(frame.getWidth()*0.81)));
		this.setPreferredSize(new Dimension((int)(frame.getWidth()*0.9), (int)(frame.getWidth()*0.81)));
		this.setBackground(Color.BLACK);
		this.setLevel(level);
		this.setMainFrame(frame);
		this.setGameStatus(status);
		this.setGameLogic(gameLogic);
		this.setInputHandler(inputHandler);
		this.setSoundManager(soundMan);
		this.setGraphicsManager(graphicsMan);		
		backBuffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setGraphics2D(backBuffer.createGraphics());
		this.setNumOfAsteroids(3);
		this.setNumOfPlatforms(8);
		rand = new Random();

	}

	// Getters
	public MegaMan getMegaMan() 				{ return megaMan;}
	public Floor[] getFloor()					{ return floor;}
	public int getNumPlatforms()				{ return numPlatforms;}
	public Platform[] getPlatforms()			{ return platforms;}
	public Asteroid[] getAsteroid() 			{ return asteroids; 		}
	public List<Bullet> getBullets() 			{ return bullets;}
	public List<BigBullet> getBigBullets()		{ return bigBullets;}
	public PowerUp getPowerUp()					{ return powerUp;}
	public int getNumberOfAsteroids()			{ return numOfAsteroids;}


	//Setters
	protected void setNumOfPlatforms(int platforms) 	{this.numPlatforms = platforms;}
	protected void setMegaManSide(int side)				{this.megaManSide = side;}
	protected void setNumOfAsteroids(int numOfAsteroids){this.numOfAsteroids = numOfAsteroids;}

	// Level state methods
	// The method associated with the current level state will be called 
	// repeatedly during each LevelLoop iteration until the next a state 
	// transition occurs
	// To understand when each is invoked see LevelLogic.stateTransition() & LevelLoop class

	@Override
	public void doStart() {
		setStartState(START_STATE);
		setCurrentState(getStartState());
		// init game variables
		bullets = new ArrayList<Bullet>();
		bigBullets = new ArrayList<BigBullet>();
		this.asteroids = new Asteroid[getNumberOfAsteroids()];
		this.initialAsteroidsYPositions = new int[getNumberOfAsteroids()];
		this.initialAsteroidsXPositions = new int[getNumberOfAsteroids()];
		this.initialPlatformPositions = new int[getNumPlatforms()];

		GameStatus status = this.getGameStatus();

		status.setAsteroidsDestroyed(0); // set the asteroids destroyed to zero everytime it starts.

		status.setGameOver(false);
		status.setNewAsteroid(false);

		// init the life and the asteroid
		newMegaMan();
		newFloor(this, 9);
		newPlatforms(getNumPlatforms());
		newAsteroid(this, getNumberOfAsteroids());

		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		lastLifeTime = -NEW_MEGAMAN_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// Display initial values for scores
		getMainFrame().getDestroyedValueLabel().setForeground(Color.BLACK);
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));

	}

	@Override
	public void doInitialScreen() {
		setCurrentState(INITIAL_SCREEN);
		clearScreen();
		getGameLogic().drawInitialMessage();
	};



	@Override
	public void doGettingReady() {	
		setCurrentState(GETTING_READY);
		getGameLogic().drawGetReady();
		repaint();
		LevelLogic.delay(2000);
		//Changes music from "menu music" to "ingame music"
		MegaManMain.audioClip.close();
		MegaManMain.audioFile = new File("audio/mainGame.wav");
		try {
			MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.audioFile);
			MegaManMain.audioClip.open(MegaManMain.audioStream);
			MegaManMain.audioClip.start();
			MegaManMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	};

	@Override
	public void doPlaying() {
		setCurrentState(PLAYING);
		updateScreen();
	};

	@Override
	public void doNewMegaman() {
		setCurrentState(NEW_MEGAMAN);
	};

	@Override
	public void doLevelWon(){
		MegaManMain.audioClip.stop();
		setCurrentState(LEVEL_WON);
		getGameLogic().drawYouWin();
		repaint();
		LevelLogic.delay(500);
	}

	@Override
	public void doNextLevel() { 
		MegaManMain.audioClip.stop();
		setCurrentState(NEXT_LEVEL);
		LevelLogic.delay(500);
	}

	@Override
	public boolean isNextLevel() {
		if (getCurrentState() == LevelState.NEXT_LEVEL) { // makes the condition false to exit to while loop at LevelLoop.java
			return false;
		}else return true;
	}

	@Override
	public void doGameOverScreen(){
		MegaManMain.audioClip.stop();
		setCurrentState(GAME_OVER_SCREEN);
		getGameLogic().drawGameOver();
		getMainFrame().getDestroyedValueLabel().setForeground(new Color(128, 0, 0));
		repaint();
		LevelLogic.delay(1500);
	}

	@Override
	public void doGameOver(){
		this.getGameStatus().setGameOver(true);
	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(getSize().getWidth()/SCREEN_WIDTH,getSize().getHeight()/SCREEN_HEIGHT);
		g2.drawImage(backBuffer, 0, 0,  this);
	}

	/**
	 * Update the game screen's backbuffer image.
	 */
	public void updateScreen(){
		Graphics2D g2d = getGraphics2D();
		GameStatus status = this.getGameStatus();

		// save original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		clearScreen();
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();
		drawBullets();
		drawBigBullets();
		movePlatform();
		checkBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();

		if(powerUpOfLevel==1) {
			drawPowerUP();
			checkMegaManPowerUpCollision();
		}

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}

	protected void checkAsteroidFloorCollisions() {
		for (Asteroid asteroid : asteroids) {
			for(int i=0; i<9; i++){
				if(asteroid.intersects(floor[i])){
					removeAsteroid(asteroid);
					this.getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, getGraphics2D(), this);
				}
			}
		}
	}

	protected void checkMegaManAsteroidCollisions() {
		GameStatus status = getGameStatus();
		MegaMan megaMan = getMegaMan();
		for (Asteroid asteroid: asteroids) {
			if(asteroid.intersects(megaMan)){
				status.setLivesLeft(status.getLivesLeft() - 1);
				removeAsteroid(asteroid);
				this.getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, getGraphics2D(), this);
			}
		}
	}

	/***
	 * Verifies if MegaMan collide with a PowerUp.
	 */
	protected void checkMegaManPowerUpCollision() {
		GameStatus status = getGameStatus();
		MegaMan megaMan = getMegaMan();
		if(getPowerUp().intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() +5);
			getPowerUp().setLocation(-getPowerUp().getPixelsWide(), -getPowerUp().getPixelsTall());
			powerUpOfLevel =3;
		}
	}

	protected void checkBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for (Asteroid asteroid : asteroids) {
			for(int i=0; i<bigBullets.size(); i++){
				BigBullet bigBullet = bigBullets.get(i);
				if(asteroid.intersects(bigBullet)){
					// increase asteroids destroyed count
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
					removeAsteroid(asteroid);
					this.getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, getGraphics2D(), this);
				}
			}
		}
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
						powerUp = new PowerUp((int)(SCREEN_WIDTH/2), 0); //The PowerUp will spawn in the upper-middle of the Screen 
						powerUpOfLevel =+1;
					}
					// remove bullet
					bullets.remove(i);
					break;
				}
			}
		}
	}

	protected void drawBigBullets() {
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			getGraphicsManager().drawBigBullet(bigBullet, g2d, this);

			boolean remove = this.moveBigBullet(bigBullet);
			if(remove){
				bigBullets.remove(i);
				i--;
			}
		}
	}

	protected void drawBullets() {
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			getGraphicsManager().drawBullet(bullet, g2d, this);

			boolean remove =   this.moveBullet(bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
	}

	/***
	 * Draw Asteroids from three directions. Can spawn from the Left, Upper and Right side of the screen at different constants speeds.
	 */
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		GameStatus status = getGameStatus();

		for (int i=0; i <asteroids.length;i++) {
			if((asteroids[i].getX() + asteroids[i].getWidth() >  0) && (asteroids[i].getX() < getWidth())){
				if (initialAsteroidsXPositions[i] == SCREEN_WIDTH-asteroids[i].getWidth()) {
					asteroids[i].translate(-asteroids[i].getSpeed(),asteroids[i].getSpeed()/2);
					getGraphicsManager().drawAsteroid(asteroids[i], g2d, this);	
				}else if (initialAsteroidsYPositions[i] ==0){
					asteroids[i].translate(0, asteroids[i].getSpeed());
					getGraphicsManager().drawAsteroid(asteroids[i], g2d, this);
				} else {
					asteroids[i].translate(asteroids[i].getSpeed(), asteroids[i].getSpeed()/2);
					getGraphicsManager().drawAsteroid(asteroids[i], g2d, this);
				}

			} else {


				long currentTime = System.currentTimeMillis();
				if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
					int[] coordinates = randomCoordinates();
					status.setNewAsteroid(false);
					asteroids[i].setSpeed(rand.ints(5,10).findFirst().getAsInt());
					asteroids[i].setLocation(coordinates[0], coordinates[1]);
					initialAsteroidsXPositions[i] = coordinates[0];
					initialAsteroidsYPositions[i] = coordinates[1];


					// draw a new asteroid
					lastAsteroidTime = currentTime;
					status.setNewAsteroid(false);
				}

				else{
					// draw explosion
					if(megaManSide == 0) {
						getGraphicsManager().drawAsteroidExplosion(asteroids[i], g2d, this);
					} else {
						getGraphicsManager().drawAsteroidExplosionLeft(asteroids[i], g2d, this);
					}
				}
			}
		}
	}

	protected void drawMegaMan() {
		//draw one of three possible MegaMan poses according to situation
		Graphics2D g2d = getGraphics2D();
		GameStatus status = getGameStatus();
		if(!status.isNewMegaMan()){
			if((Gravity() == true) || ((Gravity() == true) && (Fire() == true || Fire2() == true))){
				if(megaManSide == 0) {
					megaManSide = getGraphicsManager().drawMegaFallR(megaMan, g2d, this);
				} else {
					megaManSide = getGraphicsManager().drawMegaFallL(megaMan, g2d, this);

				}
			}
		}

		if((Fire() == true || Fire2()== true) && (Gravity()==false)){
			if(megaManSide == 0) {
				megaManSide = getGraphicsManager().drawMegaFireR(megaMan, g2d, this);
			} else {
				megaManSide = getGraphicsManager().drawMegaFireL(megaMan, g2d, this);
			}
		}

		if((Gravity()==false) && (Fire()==false) && (Fire2()==false)){
			if(megaManSide == 0) {
				megaManSide = getGraphicsManager().drawMegaMan(megaMan, g2d, this);
			} else {
				megaManSide = getGraphicsManager().drawMegaManLeft(megaMan, g2d, this);
			}
		}
	}

	/***
	 * Draw PowerUp. It will stay on a platform or fall to the ground.
	 */
	protected void drawPowerUP() {
		PowerUp powerUp =getPowerUp();
		if(powerUpGravity() == true) {
			getGraphicsManager().drawPowerUp(powerUp, getGraphics2D(), this);
			powerUp.translate(0, -1);
		} else if(powerUpOnPlatform() == true) {
			getGraphicsManager().drawPowerUp(powerUp, getGraphics2D(), this);
			powerUp.translate(0, -1);
		} else {
			getGraphicsManager().drawPowerUp(powerUp, getGraphics2D(), this);
		}
		if(powerUpFall()==false) {
			getGraphicsManager().drawPowerUp(powerUp, getGraphics2D(), this);
			powerUp.translate(0, -1);
		}
	}

	protected void drawPlatforms() {
		//draw platforms
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<getNumPlatforms(); i++){
			getGraphicsManager().drawPlatform2(platforms[i], g2d, this, i);
		}
	}


	protected void drawFloor() {
		//draw Floor
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<9; i++){
			getGraphicsManager().drawFloor(floor[i], g2d, this, i);	
		}
	}

	protected void clearScreen() {
		// clear screen
		Graphics2D g2d = getGraphics2D();
		g2d.drawImage(getGraphicsManager().getSkyBackgroud(), 0, 0, this);
	}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	protected void drawStars(int numberOfStars) {
		Graphics2D g2d = getGraphics2D();
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	@Override
	public boolean isLevelWon() {
		return getGameStatus().getAsteroidsDestroyed() >= 500;
	}

	protected boolean Gravity(){
		MegaMan megaMan = this.getMegaMan();
		Floor[] floor = this.getFloor();
		for(int i=0; i<9; i++){
			if((megaMan.getY() + megaMan.getHeight() -17 < SCREEN_HEIGHT - floor[i].getHeight()/2) 
					&& Fall() == true){
				megaMan.translate(0 , 2);
				return true;

			}
		}
		return false;
	}

	/***
	 * Adds gravity to the PowerUp.
	 * @return True if the PowerUp is falling.
	 */
	protected boolean powerUpGravity() {
		Floor [] floors = this.getFloor();
		PowerUp powerUp = this.getPowerUp();
		for (Floor floor : floors) {
			if((powerUp.getY()+ powerUp.getHeight() -22 < SCREEN_HEIGHT - floor.getHeight()/2) 
					&& powerUpFall() == true) {
				powerUp.translate(0, 2);
				return true;
			}
		}
		return false;
	}

	//Bullet fire pose
	protected boolean Fire(){
		MegaMan megaMan = this.getMegaMan();
		List<Bullet> bullets = this.getBullets();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if((bullet.getX() > megaMan.getX() + megaMan.getWidth()) && (bullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			} else if ((bullet.getX() <= megaMan.getX()) && (bullet.getX() > megaMan.getX() -30)){
				return true;
			}
		}
		return false;
	}

	//BigBullet fire pose
	protected boolean Fire2(){
		MegaMan megaMan = this.getMegaMan();
		List<BigBullet> bigBullets = this.getBigBullets();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if((bigBullet.getX() > megaMan.getX() + megaMan.getWidth()) && 
					(bigBullet.getX() <= megaMan.getX() + megaMan.getWidth() + 60)){
				return true;
			}else if ((bigBullet.getX() <= megaMan.getX()) && (bigBullet.getX() > megaMan.getX() -30)){
				return true;
			}
		}
		return false;
	}

	//Platform Gravity
	public boolean Fall(){
		MegaMan megaMan = this.getMegaMan(); 
		Platform[] platforms = this.getPlatforms();
		for(int i=0; i<getNumPlatforms(); i++){
			if((((platforms[i].getX() < megaMan.getX()) && (megaMan.getX()< platforms[i].getX() + platforms[i].getWidth())) 
					|| ((platforms[i].getX() < megaMan.getX() + megaMan.getWidth()) 
							&& (megaMan.getX() + megaMan.getWidth()< platforms[i].getX() + platforms[i].getWidth())))
					&& megaMan.getY() + megaMan.getHeight() == platforms[i].getY()
					){
				return false;
			}
		}
		return true;
	}

	
	public boolean powerUpFall() {
		PowerUp powerUp = this.getPowerUp();
		Platform[] platforms = this.getPlatforms();
		for(Platform platform : platforms) {
			if((((platform.getX() < powerUp.getX()) && (powerUp.getX()< platform.getX() + platform.getWidth())) 
					|| ((platform.getX() < powerUp.getX() + powerUp.getWidth()) 
							&& (powerUp.getX() + powerUp.getWidth()< platform.getX() + platform.getWidth())))
					&& powerUp.getY() + powerUp.getHeight() == platform.getY()){
				return false;
			}
		}
		return true;
	}

	/***
	 * Verifies if the PowerUp is on a platform.
	 * @return Returns true if the PowerUp is on a platform.
	 */
	public boolean powerUpOnPlatform() {
		PowerUp powerUp = this.getPowerUp();
		Platform[] platforms = this.getPlatforms();
		for(Platform platform : platforms) {
			if((((platform.getX() < powerUp.getX()) && (powerUp.getX()< platform.getX() + platform.getWidth())) 
					|| ((platform.getX() < powerUp.getX() + powerUp.getWidth()) 
							&& (powerUp.getX() + powerUp.getWidth()< platform.getX() + platform.getWidth())))
					&& powerUp.getY() + powerUp.getHeight() == platform.getY()){
				powerUp.translate(0,-1);
				return true;
			}
		}
		return false;
	}

	/***
	 * Removes the asteroid given in the parameter.
	 * @param asteroid Asteroid to be removed.
	 */
	public void removeAsteroid(Asteroid asteroid){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.getPixelsWide(),
				asteroid.getPixelsTall());
		asteroid.setLocation(-asteroid.getPixelsWide(), -asteroid.getPixelsTall());
		this.getGameStatus().setNewAsteroid(true);
		lastAsteroidTime = System.currentTimeMillis();
		// play asteroid explosion sound
		this.getSoundManager().playAsteroidExplosionSound();
	} 

	/**
	 * Fire a bullet from life.
	 */
	public void fireBullet(){
		MegaMan megaMan = this.getMegaMan();
		Bullet bullet;
		if (megaManSide == 0) {
			bullet = new Bullet(megaMan.x + megaMan.width - Bullet.WIDTH/2, megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
		} else {
			bullet = new Bullet(megaMan.x, megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
		}
		bullets.add(bullet);
		this.getSoundManager().playBulletSound();
	}

	/**
	 * Fire the "Power Shot" bullet
	 */
	public void fireBigBullet(){
		MegaMan megaMan = this.getMegaMan();
		BigBullet  bigBullet;
		//BigBullet bigBullet = new BigBullet(megaMan);
		if (megaManSide == 0) {
			bigBullet = new BigBullet(megaMan.x + megaMan.width - Bullet.WIDTH/2, megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
		} else {
			bigBullet = new BigBullet(megaMan.x, megaMan.y + megaMan.width/2 - Bullet.HEIGHT +2);
		}
		bigBullets.add(bigBullet);
		this.getSoundManager().playBulletSound();
	}

	/**
	 * Move a bullet once fired.
	 * @param bullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBullet(Bullet bullet){
		MegaMan megaMan = this.getMegaMan();
		if(megaManSide == 0 && (bullet.getX() + bullet.getSpeed() + bullet.width < getWidth())){ // verifies that megaman is facing right and that the bullet is inside the screen 
			if(bullet.getX() > 0 && (bullet.getX()+bullet.getSpeed()< megaMan.getX())) { //if the bullet is left megaman, it will continue its trajectory
				bullet.translate(-bullet.getSpeed(), 0);
				return false;
			} else if(bullet.getX() - bullet.getSpeed() < 0){ // if the bullet is in the left edge, it will be removed.
				return true;
			} else {
				bullet.translate(bullet.getSpeed(), 0);
				return false;
			}
		} else if (megaManSide == 1 && (bullet.getX() - bullet.getSpeed() >= 0)) { // verifies if megaman is facing left and the bullet to be moved is inside the screen
			if(bullet.getX()+bullet.getSpeed()+bullet.getWidth()> megaMan.getX()+ megaMan.getWidth()) {// if the bullet is right to megaman, it will continue the old trajectory
				bullet.translate(bullet.getSpeed(), 0);
				return false;
			} else {
				bullet.translate(-bullet.getSpeed(), 0);
				return false;
			}

		} else {
			return true;
		}
	}


	/** Move a "Power Shot" bullet once fired.
	 * @param bigBullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBigBullet(BigBullet bigBullet){
		MegaMan megaMan = this.getMegaMan();
		if(megaManSide == 0 && (bigBullet.getX() + bigBullet.getSpeed() + bigBullet.width < getWidth())){ // verifies that megaman is facing right and that the bullet is inside the screen 
			if(bigBullet.getX() > 0 && (bigBullet.getX()+bigBullet.getSpeed()< megaMan.getX())) { //if the bullet is left megaman, it will continue its trajectory
				bigBullet.translate(-bigBullet.getSpeed(), 0);
				return false;
			} else if(bigBullet.getX() - bigBullet.getSpeed() < 0){ // if the bullet is in the left edge, it will be removed.
				return true;
			} else {
				bigBullet.translate(bigBullet.getSpeed(), 0);
				return false;
			}
		} else if (megaManSide == 1 && (bigBullet.getX() - bigBullet.getSpeed() >= 0)) { // verifies if megaman is facing left and the bullet to be moved is inside the screen
			if(bigBullet.getX()+bigBullet.getSpeed()+bigBullet.getWidth()> megaMan.getX()+ megaMan.getWidth()) {// if the bullet is right to megaman, it will continue the old trajectory
				bigBullet.translate(bigBullet.getSpeed(), 0);
				return false;
			} else {
				bigBullet.translate(-bigBullet.getSpeed(), 0);
				return false;
			}

		} else {
			return true;
		}
	}

	/**
	 * Create a new MegaMan (and replace current one).
	 */
	public MegaMan newMegaMan(){
		this.megaMan = new MegaMan((SCREEN_WIDTH - MegaMan.WIDTH) / 2, (SCREEN_HEIGHT - MegaMan.HEIGHT - MegaMan.Y_OFFSET) / 2);
		return megaMan;
	}

	public Floor[] newFloor(Level1State screen, int n){
		floor = new Floor[n];
		for(int i=0; i<n; i++){
			this.floor[i] = new Floor(0 + i * Floor.WIDTH, SCREEN_HEIGHT- Floor.HEIGHT/2);
		}

		return floor;
	}

	/**
	 * Creates an array of platforms in different positions.
	 * @param The number of platforms to be created
	 * @return The array with the platforms objects
	 */
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		int k=2;
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<4 && i%2==1){
				platforms[i].setLocation((SCREEN_WIDTH/4) - platforms[i].width/2, SCREEN_HEIGHT/2 + 140 - i*40);
				initialPlatformPositions[i] = (SCREEN_WIDTH/4) +75 - platforms[i].width/2;
			} else if (i<4 && i%2==0){
				platforms[i].setLocation((SCREEN_WIDTH/4) - platforms[i].width/2, SCREEN_HEIGHT/2 + 140 - i*40);
				initialPlatformPositions[i] = (SCREEN_WIDTH/4) -75 - platforms[i].width/2;
			} else if(i==4) {
				platforms[i].setLocation((SCREEN_WIDTH/4)*3 - platforms[i].width/2, SCREEN_HEIGHT/2 + 140 - 3*40);
				initialPlatformPositions[i]= (SCREEN_WIDTH/4)*3 -75 - platforms[i].width/2;
			} else if(i>4 && i%2==1){	
				platforms[i].setLocation((SCREEN_WIDTH/4)*3 - platforms[i].width/2, SCREEN_HEIGHT/2 + 20 + (i-k)*40 );
				initialPlatformPositions[i]= (SCREEN_WIDTH/4)*3 + 75 - platforms[i].width/2;
				k=k+2;
			} else if (i>4 && i%2==0){
				platforms[i].setLocation((SCREEN_WIDTH/4)*3 - platforms[i].width/2, SCREEN_HEIGHT/2 + 20 + (i-k)*40 );
				initialPlatformPositions[i]= (SCREEN_WIDTH/4)*3- 75 - platforms[i].width/2;
				k=k+2;
			}
		}
		return platforms;
	}

	/**
	 * This method creates an array of asteroids to be displayed on screen
	 * @param screen
	 * @return The array of asteroids.
	 */
	public Asteroid[] newAsteroid(Level1State screen, int n){
		Asteroid ast;
		for (int i=0; i<n;i++) {
			int[] coordinates = randomCoordinates();

			ast = new Asteroid(coordinates[0], coordinates[1]);
			ast.setSpeed(rand.ints(5,10).findFirst().getAsInt());
			asteroids[i] = ast;
			initialAsteroidsXPositions[i] = coordinates[0];
			initialAsteroidsYPositions[i] = coordinates[1];
		}
		return asteroids;
	}

	public PowerUp newPowerUp(int x, int y) {
		this.powerUp = new PowerUp(x, y);
		return powerUp;
	}

	/**
	 * This will randomly choose one side of the screen for the X coordinate
	 * @return The X coordinate of one side of the screen.
	 */
	public int [] randomCoordinates() {
		int [] asteroidCoordinates = new int[2];
		int randPos = rand.ints(0,3).findFirst().getAsInt();

		switch(randPos) {
		case 0:
			asteroidCoordinates[0] = 0;
			asteroidCoordinates[1] = rand.nextInt((int)(SCREEN_HEIGHT - Asteroid.HEIGHT- 32));
			break;
		case 1:
			asteroidCoordinates[0] = (int) (SCREEN_WIDTH - Asteroid.WIDTH); //right side of screen
			asteroidCoordinates[1] = asteroidCoordinates[1] = rand.nextInt((int)(SCREEN_HEIGHT - Asteroid.HEIGHT- 32));
			break;
		case 2:
			asteroidCoordinates[0] = rand.nextInt(SCREEN_WIDTH);//upper side of screen
			asteroidCoordinates[1] = 0;
			break;
//		default:
//			asteroidCoordinates[0] = 0;
//			asteroidCoordinates[1] = rand.nextInt((int)(SCREEN_HEIGHT - Asteroid.HEIGHT- 32));
//			break;
		}
		return asteroidCoordinates;
	}

	/**
	 * This moves the platforms 150 pixels to both sides.
	 */
	public void movePlatform() {
		Platform[] platforms = getPlatforms();
		for (int i=0; i<platforms.length; i++) {
			if(platforms[i].getX() <= initialPlatformPositions[i]) {
				platforms[i].translate(1, 0);
				if (platforms[i].getX() == initialPlatformPositions[i]) {
					initialPlatformPositions[i] = initialPlatformPositions[i] -150;}
			} else if (platforms[i].getX() >= initialPlatformPositions[i] ) {
				platforms[i].translate(-1, 0);
				if (platforms[i].getX() == initialPlatformPositions[i]) {
					initialPlatformPositions[i] = initialPlatformPositions[i] + 150;
				}
			}
		}
	}

	/**
	 * Move the megaMan up
	 * @param megaMan the megaMan
	 */
	public void moveMegaManUp(){
		if(megaMan.getY() - megaMan.getSpeed() >= 0){
			megaMan.translate(0, -megaMan.getSpeed()*2);
		}
	}

	/**
	 * Move the megaMan down
	 * @param megaMan the megaMan
	 */
	public void moveMegaManDown(){
		for(int i=0; i<9; i++){
			if(megaMan.getY() + megaMan.getSpeed() + megaMan.height < SCREEN_HEIGHT - floor[i].getHeight()/2){
				megaMan.translate(0, 2);
			}
		}
	}

	/**
	 * Move the megaMan left
	 * @param megaMan the megaMan
	 */
	public void moveMegaManLeft(){
		megaManSide = 1;
		if(megaMan.getX() - megaMan.getSpeed() >= 0){
			megaMan.translate(-megaMan.getSpeed(), 0);
		}
	}

	/**
	 * Move the megaMan right
	 * @param megaMan the megaMan
	 */
	public void moveMegaManRight(){
		megaManSide = 0;
		if(megaMan.getX() + megaMan.getSpeed() + megaMan.width < getWidth()){
			megaMan.translate(megaMan.getSpeed(), 0);
		}
	}

	public void speedUpMegaMan() {
		megaMan.setSpeed(megaMan.getDefaultSpeed() * 2 +1);
	}

	public void slowDownMegaMan() {
		megaMan.setSpeed(megaMan.getDefaultSpeed());
	}
}
