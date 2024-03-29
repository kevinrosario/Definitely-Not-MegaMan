package rbadia.voidspace.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.model.Asteroid;
//import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
//import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Floor;
//import rbadia.voidspace.model.BulletBoss;
//import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;

/**
 * Manages and draws game graphics and images.
 */
public class GraphicsManager {
	private BufferedImage megaManImg;
	private BufferedImage megaManLeftImg;
	private BufferedImage megaFallRImg;
	private BufferedImage megaFallLImg;
	private BufferedImage megaFireRImg;
	private BufferedImage megaFireLImg;
	private BufferedImage floorImg;
	private BufferedImage platformImg;
	private BufferedImage bulletImg;
	private BufferedImage bigBulletImg;
	private BufferedImage asteroidImg;
	private BufferedImage asteroidExplosionImg;
	private BufferedImage megaManExplosionImg;
	private BufferedImage bigAsteroidExplosionImg;
	private BufferedImage powerUpImg;
	private BufferedImage bossImg;
	private BufferedImage bossReverseImg;
	private BufferedImage platform2Img;
	private BufferedImage skyBackground;
	private BufferedImage moonBackground;	
	private BufferedImage platform3Img;
	private BufferedImage bossFiresImg;



	/**
	 * Creates a new graphics manager and loads the game images.
	 */
	public GraphicsManager(){
		// load images
		try {
			this.megaManImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3.png"));
			this.megaFallRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallRight.png"));
			this.megaFireRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireRight.png"));
			this.floorImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFloor.png"));
			this.platformImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform3.png"));
			this.asteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroid.png"));
			this.asteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroidExplosion.png"));
			this.bulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bullet.png"));
			this.bigBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));
			this.megaManLeftImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3reverse.png"));
			this.megaFallLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallLeft.png"));
			this.megaFireLImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireLeft.png"));
			this.powerUpImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/powerUp.png"));
			this.bossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/boss.png"));
			this.bossReverseImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bossReverse.png"));
			this.platform2Img = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform.png"));
			this.skyBackground = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/sky.png"));
			this.moonBackground = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/moon.png"));
			this.platform3Img = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform4.png"));
			this.bossFiresImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bossFires.png"));

			

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"MegaMan!!! - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public BufferedImage getSkyBackgroud() {
		return skyBackground;
	}

	public BufferedImage getMoonBackgroud() {
		return moonBackground;
	}
	/**
	 * Draws a MegaMan image to the specified graphics canvas.
	 * @param MegaMan the ship to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 * @return TODO
	 */
	public int drawMegaMan (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaManImg, megaMan.x, megaMan.y, observer);
		return 0;
	}

	public int drawMegaManLeft (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaManLeftImg, megaMan.x, megaMan.y, observer);	
		return 1;
	}

	public int drawMegaFallR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFallRImg, megaMan.x, megaMan.y, observer);	
		return 0;
	}

	public int drawMegaFallL (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFallLImg, megaMan.x, megaMan.y, observer);	
		return 1;
	}

	public int drawMegaFireR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFireRImg, megaMan.x, megaMan.y, observer);	
		return 0;
	}

	public int drawMegaFireL (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFireLImg, megaMan.x, megaMan.y, observer);	
		return 1;
	}

	public void drawFloor (Floor floor, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(floorImg, floor.x, floor.y, observer);				
	}
	public void drawPlatform(Platform platform, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(platformImg, platform.x , platform.y, observer);	
	}

	public void drawPlatform2 (Platform platform, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(platform2Img, platform.x , platform.y, observer);	
	}

	public void drawPlatform3 (Platform platform, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(platform3Img, platform.x , platform.y, observer);	
	}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBullet(Bullet bullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bulletImg, bullet.x, bullet.y, observer);
	}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bigBullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBigBullet(BigBullet bigBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigBulletImg, bigBullet.x, bigBullet.y, observer);
	}

	/**
	 * Draws an asteroid image to the specified graphics canvas.
	 * @param asteroid the asteroid to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroid(Asteroid asteroid, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidImg, asteroid.x, asteroid.y, observer);
	}

	/**
	 * Draws a MegaMan explosion image to the specified graphics canvas.
	 * @param megaManExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawMegaManExplosion(Rectangle megaManExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(megaManExplosionImg, megaManExplosion.x, megaManExplosion.y, observer);
	}

	/**
	 * Draws an asteroid explosion image to the specified graphics canvas.
	 * @param asteroidExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroidExplosion(Rectangle asteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidExplosionImg, asteroidExplosion.x, asteroidExplosion.y, observer);
	}
	
	public void drawBossFires(Asteroid asteroid, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bossFiresImg, asteroid.x, asteroid.y, observer);
	}

	public void drawAsteroidExplosionLeft(Rectangle asteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidExplosionImg, asteroidExplosion.x-32, asteroidExplosion.y-32, observer);
	}

	public void drawBigAsteroidExplosion(Rectangle bigAsteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigAsteroidExplosionImg, bigAsteroidExplosion.x, bigAsteroidExplosion.y, observer);
	}

	public void drawPowerUp(Rectangle powerUp, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(powerUpImg, powerUp.x, powerUp.y, observer);
	}

	public int drawBoss (Boss boss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bossImg, boss.x, boss.y, observer);
		return 0;
	}

	public int drawReverseBoss (Boss boss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bossReverseImg, boss.x, boss.y, observer);
		return 1;
	}
}
