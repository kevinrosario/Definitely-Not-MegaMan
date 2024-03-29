package rbadia.voidspace.main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game class. Starts the game.
 */
public class MegaManMain {

	//Starts playing menu music as soon as the game frame is created

	public static AudioInputStream audioStream;
	public static Clip audioClip;
	public static File audioFile;	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException, IOException  {


		MainFrame frame = new MainFrame();              		// Main Game Window
		LevelLogic gameLogic = new LevelLogic();        		// Coordinates among various levels
		InputHandler inputHandler = new InputHandler(); 		// Keyboard listener
		GraphicsManager graphicsMan = new GraphicsManager(); // Draws all graphics for game objects
		SoundManager soundMan = new SoundManager();			// Loads and plays all sounds during the game

		audioFile = new File("audio/menuScreen.wav");
		try {
			audioStream = AudioSystem.getAudioInputStream(audioFile);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}

		frame.addKeyListener(inputHandler);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int playAgain = 2;
		while(playAgain != 1) {

			GameStatus gameStatus = new GameStatus();
			gameStatus.setLivesLeft(3);
			gameStatus.setBossLivesLeft(20);
			LevelState level1State = new Level1State(1, frame, gameStatus, gameLogic, inputHandler, graphicsMan, soundMan);
			LevelState level2State = new Level2State(2, frame, gameStatus, gameLogic, inputHandler, graphicsMan, soundMan);
			LevelState level3State = new Level3State(3, frame, gameStatus, gameLogic, inputHandler, graphicsMan, soundMan);
			LevelState levels[] = {level1State, level2State,level3State};

			String outcome = "CONGRATS!! YOU WON!!";
			for (LevelState nextLevel : levels) {

				if(nextLevel == level3State) {
					frame.setLastLevel(true);
				} else {
					frame.setLastLevel(false);
				}
				System.out.println("Next Level Started");
				frame.setLevelState(nextLevel);
				gameLogic.setLevelState(nextLevel);
				inputHandler.setLevelState(nextLevel);
				gameStatus.setLevel(nextLevel.getLevel());

				frame.setVisible(true);  // TODO verify whether this is necessary
				startInitialMusic();

				// init main game loop
				Thread nextLevelLoop = new Thread(new LevelLoop(nextLevel));
				nextLevelLoop.start();
				nextLevelLoop.join();

				if (nextLevel.getGameStatus().isGameOver()) {
					outcome = "SORRY YOU LOST";
					break;
				}

			}
			playAgain = JOptionPane.showConfirmDialog(null, outcome + " ... Play Again?", "", JOptionPane.YES_NO_OPTION);
		}
		System.exit(0);
	}

	public static void startInitialMusic() throws InterruptedException, IOException {
		// Music 
		// allows music to be played while playing

		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);

		try {
			audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.start();
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
