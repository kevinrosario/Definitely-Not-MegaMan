package rbadia.voidspace.main;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.sounds.SoundManager;

public class Level3State extends Level2State {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Boss boss;
	private Point2D.Double megaManInitialPointOnContactWithBoss; // Registers the position at which MegaMan touched the boss.
	private int moveMegaManAway =0; //set the condition to move away MegaMan from the boss.
	private int bossSide = 1; //saves the boss side to be displayed.
	private int roundsOfAsteroidsFired =0;// Rounds fired to by the boss. When 3 are reached, the boss will move.
	private int bossNextPosition =0;//records the boss next position.


	public Level3State(int level, MainFrame frame, GameStatus status, LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
		// TODO Auto-generated constructor stub
		setNumOfAsteroids(4);
	}

	public Boss getBoss()	{return boss;}

	@Override
	public void doStart() {	
		super.doStart();
		GameStatus status = this.getGameStatus();
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
		setMegaManSide(1);
		newBoss();
		newAsteroidsFromBoss(this, 4);
		lastAsteroidTime = -NEW_ASTEROID_DELAY;

		getMainFrame().getBossLiveValueLabel().setText(Integer.toString(status.getBossLivesLeft()));
	}

	@Override
	protected void drawPlatforms() {
		//draw platforms
		super.drawPlatforms();
		Graphics2D g2d = getGraphics2D();
		for(int i=0; i<getNumPlatforms(); i++){
			getGraphicsManager().drawPlatform3(platforms[i], g2d, this, i);
		}
	}

	@Override
	public void updateScreen(){
		super.updateScreen();
		GameStatus status = this.getGameStatus();
		drawBoss();
		checkMegaManBossCollisions();
		checkBullletBossCollisions();
		checkBigBulletBossCollisions();
		drawAsteroidFromBoss();
		checkBullletBossCollisions();
		checkMegaManAsteroidFromBossCollisions();
		if(moveMegaManAway == 1) {
			moveMegaManAwayFromBoss();
		}

		if(roundsOfAsteroidsFired ==3) {
			moveBoss();
		}

		getMainFrame().getBossLiveValueLabel().setText(Integer.toString(status.getBossLivesLeft()));
	}

	public Asteroid[] newAsteroidsFromBoss(Level1State screen, int n){
		Asteroid asteroidFromBoss;
		Boss boss = getBoss();
		for(int i=0; i<n;i++) {
			int xPos;
			if(bossSide ==0) {
				xPos = (int)(boss.getX());
			} else {
				xPos = (int)(boss.getX()-boss.width);
			}

			int yPos = (int)(boss.getY());
			asteroidFromBoss = new Asteroid(xPos, yPos);
			asteroidFromBoss.setSpeed(4);
			asteroids[i] = asteroidFromBoss;
		}
		return asteroids;
	}

	/***
	 * Set the Winning to be when the boss have 0 lives.
	 */
	@Override
	public boolean isLevelWon() {
		return getGameStatus().getBossLivesLeft() == 0;
	}

	public Boss newBoss(){
		this.boss = new Boss(((SCREEN_WIDTH) /4)-(Boss.WIDTH*2), (SCREEN_HEIGHT - Boss.HEIGHT- Boss.Y_OFFSET));
		return boss;
	}

	public MegaMan newMegaMan(){
		this.megaMan = new MegaMan(3*(SCREEN_WIDTH )/4, (SCREEN_HEIGHT - MegaMan.HEIGHT -MegaMan.Y_OFFSET-17));
		return megaMan;
	}

	public void moveMegaManAwayFromBoss() {
		MegaMan megaMan = getMegaMan();
		if(bossSide == 1) {
			if(megaMan.getX() <= megaManInitialPointOnContactWithBoss.getX() || megaMan.getY() >= megaManInitialPointOnContactWithBoss.getY()) {
				megaMan.translate(10, -10);
			} else {
				moveMegaManAway =0;
			}
		} else {
			if(megaMan.getX() >= megaManInitialPointOnContactWithBoss.getX() || megaMan.getY() >= megaManInitialPointOnContactWithBoss.getY()) {
				megaMan.translate(-10, -10);
			} else {
				moveMegaManAway =0;
			}
		}
	}

	protected void drawBoss() {
		Graphics2D g2d = getGraphics2D();
		if(bossSide ==0) {
			bossSide = getGraphicsManager().drawBoss(boss, g2d, this);
		} else {
			bossSide = getGraphicsManager().drawReverseBoss(boss, g2d, this);
		}
	}

	/***
	 * Moves boss in the screen.
	 */
	public void moveBoss() {
		Boss boss =getBoss();
		if(bossSide == 0 && boss.getX() >= bossNextPosition) {
			boss.translate(-5, 0);
		} else if( bossSide == 1 && boss.getX() <= bossNextPosition) {
			boss.translate(5, 0);
		} else {
			if(bossSide ==0) {
				bossSide = getGraphicsManager().drawReverseBoss(boss, getGraphics2D(), this);
			} else {
				bossSide = getGraphicsManager().drawBoss(boss, getGraphics2D(), this);
			}
			roundsOfAsteroidsFired =0;
		}
	}



	/***
	 * This method draws the Asteroids fired from the boss. Will have different angles and the same speed.
	 */
	protected void drawAsteroidFromBoss() {
		Graphics2D g2d = getGraphics2D();
		GameStatus status = getGameStatus();
		Boss boss = getBoss();
		for (int i=0; i<asteroids.length; i++) {
			if((asteroids[i].getX() + asteroids[i].getWidth() >  0) && (asteroids[i].getX()< getWidth())){
				if(bossSide == 0) {
					asteroids[i].translate(-asteroids[i].getSpeed(), -i+1/asteroids[i].getSpeed());
					getGraphicsManager().drawBossFires(asteroids[i], g2d, this);	
				} else {
					asteroids[i].translate(asteroids[i].getSpeed(), -i+1/asteroids[i].getSpeed());
					getGraphicsManager().drawBossFires(asteroids[i], g2d, this);	
				}
			} else {
				long currentTime = System.currentTimeMillis();

				if((currentTime - lastAsteroidTime) > 200){
					int xPos = (int)(boss.getX());
					int yPos = (int)(boss.getY());
					status.setNewAsteroid(false);
					asteroids[i].setSpeed(4);
					asteroids[i].setLocation(xPos, yPos);
					// draw a new asteroid
					lastAsteroidTime = currentTime;
					status.setNewAsteroid(false);
					asteroids[i].setLocation((int)(boss.getX()), (int)(boss.getY()));
					if(i ==2) {
						roundsOfAsteroidsFired+=1;
						if(roundsOfAsteroidsFired == 2) {
							if(bossSide == 0) {
								bossNextPosition = ((SCREEN_WIDTH) /4)-(Boss.WIDTH*2);
							} else {
								bossNextPosition = 3*(SCREEN_WIDTH)/4;
							}
						}
					}
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

	protected void checkBullletBossCollisions() {
		GameStatus status = getGameStatus();
		Boss boss = getBoss();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(boss.intersects(bullet)){
				status.setBossLivesLeft(status.getBossLivesLeft()-1);
				bullets.remove(i);
				break;
			}
		}
	}

	protected void checkBigBulletBossCollisions() {
		GameStatus status = getGameStatus();
		Boss boss = getBoss();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(boss.intersects(bigBullet)){
				status.setBossLivesLeft(status.getBossLivesLeft() - 4);
				bigBullets.remove(i);
				break;
			}
		}
	}

	protected void checkMegaManBossCollisions() {
		GameStatus status = getGameStatus();
		MegaMan megaMan = getMegaMan();
		if(boss.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			if(bossSide == 1) {
				megaManInitialPointOnContactWithBoss = new Point2D.Double(megaMan.getCenterX()+150, megaMan.getY()-200);
			}else {
				megaManInitialPointOnContactWithBoss = new Point2D.Double(megaMan.getCenterX()-150, megaMan.getY()-200);
			}
			moveMegaManAway =1;
		}
	}

	protected void checkMegaManAsteroidFromBossCollisions() {
		super.checkMegaManAsteroidCollisions();
		GameStatus status = getGameStatus();
		MegaMan megaMan = getMegaMan();
		for(Asteroid asteroid: asteroids) {
			if(asteroid.intersects(megaMan)){
				status.setLivesLeft(status.getLivesLeft() - 1);
				removeAsteroid(asteroid);
				this.getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, getGraphics2D(), this);
			}
		}
	}

	@Override
	protected void drawAsteroid() {
	}

	@Override
	protected void checkMegaManAsteroidCollisions() {
	}
}
