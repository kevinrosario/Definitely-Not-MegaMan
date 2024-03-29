package rbadia.voidspace.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The game's main frame. Contains all the game's labels, file menu, and game screen.
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private LevelState levelState = null;

	private JLabel destroyedLabel;
	private JLabel destroyedValueLabel;

	private JLabel levelLabel;
	private JLabel levelValueLabel;

	private JLabel livesLabel;
	private JLabel livesValueLabel;

	private JLabel bossLivesLabel;
	private JLabel bossLiveValueLabel;

	private  boolean lastLevel = false;
	private int fontSize;

	public MainFrame() {
		super();
		initialize();
	}

	public synchronized boolean isLastLevel() {
		return lastLevel;
	}

	public synchronized void setLastLevel(boolean lastLevel) {
		this.lastLevel = lastLevel;
	}



	public JLabel getBossLivesLabel() {
		return bossLivesLabel;
	}

	public void setBossLivesLabel(JLabel bossLivesLabel) {
		this.bossLivesLabel = bossLivesLabel;
	}

	public JLabel getBossLiveValueLabel() {
		return bossLiveValueLabel;
	}

	public void setBossLiveValueLabel(JLabel bossLiveValueLabel) {
		this.bossLiveValueLabel = bossLiveValueLabel;
	}

	public JLabel getLevelLabel() {
		return levelLabel;
	}

	public JLabel getLevelValueLabel() {
		return levelValueLabel;
	}

	public JLabel getLivesLabel() {
		return livesLabel;
	}

	public JLabel getLivesValueLabel() {
		return livesValueLabel;
	}

	public JLabel getDestroyedValueLabel() {
		return destroyedValueLabel;
	}

	public void setDestroyedValueLabel(JLabel destroyedValueLabel) {
		this.destroyedValueLabel = destroyedValueLabel;
	}

	public void setLevelLabel(JLabel levelLabel) {
		this.levelLabel = levelLabel;
	}

	public void setLevelValueLabel(JLabel levelValueLabel) {
		this.levelValueLabel = levelValueLabel;
	}

	public void setLivesLabel(JLabel livesLabel) {
		this.livesLabel = livesLabel;
	}

	public void setLivesValueLabel(JLabel livesValueLabel) {
		this.livesValueLabel = livesValueLabel;
	}

	public LevelState getLevelState() {
		return levelState;
	}

	public void setLevelState(LevelState levelState) {
		this.levelState = levelState;
		this.jContentPane = null;
		this.setContentPane(getJContentPane());
	}

	public int getFontSize(){
		return fontSize;
	}

	public void setFontSize(int fontSize){
		this.fontSize = fontSize;
	}

	private void initialize() {
		Dimension dim = this.getToolkit().getScreenSize();
		this.setSize((dim.width/2), (int)((dim.width/2)*(0.905)));
		this.setTitle("Mega Man");
		this.setFontSize((int)((dim.width/2)*(0.905)*0.0225));
		Rectangle bounds = this.getBounds();
		this.setLocation(
				(dim.width - bounds.width) / 2,
				(dim.height - bounds.height) / 2);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.gridx = 5;

			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.gridx = 4;

			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridx = 3;

			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.anchor = GridBagConstraints.EAST;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.gridx = 2;

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridx = 1;

			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridx = 0;

			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.gridwidth = 6;	
			livesLabel = new JLabel("Lives Left: ");
			livesLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
			livesValueLabel = new JLabel("3");
			livesValueLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
			destroyedLabel = new JLabel("Score: ");
			destroyedLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
			destroyedValueLabel = new JLabel("0");
			destroyedValueLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));

			bossLivesLabel = new JLabel("Boss Lives Left: ");
			bossLivesLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
			bossLiveValueLabel = new JLabel("0");
			bossLiveValueLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));

			levelLabel = new JLabel("Level: ");
			levelLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
			levelValueLabel = new JLabel("1");
			levelValueLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));

			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getLevelState(), gridBagConstraints);
			jContentPane.add(livesLabel, gridBagConstraints1);
			jContentPane.add(livesValueLabel, gridBagConstraints2);

			if (lastLevel == true) {
				jContentPane.add(bossLivesLabel, gridBagConstraints3);
				jContentPane.add(bossLiveValueLabel, gridBagConstraints4);
			} else {
				jContentPane.add(destroyedLabel, gridBagConstraints3);
				jContentPane.add(destroyedValueLabel, gridBagConstraints4);
			}
			jContentPane.add(levelLabel, gridBagConstraints5);
			jContentPane.add(levelValueLabel, gridBagConstraints6);
		}
		return jContentPane;
	}



}  //  @jve:decl-index=0:visual-constraint="10,10"
