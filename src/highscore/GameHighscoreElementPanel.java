package highscore;

import games.Modus;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import start.X;

public class GameHighscoreElementPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GameHighscoreElement highscoreElement;
	public GameHighscoreElement getHighscoreElement() {
		return highscoreElement;
	}

	private JLabel platzLabel;
	private Modus modus;
	private JLabel nameLabel;
	private JLabel spieleLabel;
	private JLabel prozentLabel;
	private JLabel siegeLabel;
	
	public GameHighscoreElementPanel(GameHighscoreElement highscoreElement, Modus modus){
		this.highscoreElement = highscoreElement;
		this.modus = modus;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new GridLayout(1,5,10,0));
		Font schriftFont = X.buttonFont;
		platzLabel = new JLabel();
		platzLabel.setFont(schriftFont);
		nameLabel = new JLabel(highscoreElement.getName());
		nameLabel.setFont(schriftFont);
		int spiele = highscoreElement.getSpiele(modus);
		spieleLabel = new JLabel(spiele+"");
		spieleLabel.setFont(schriftFont);
		int siege = highscoreElement.getSiege(modus);
		siegeLabel = new JLabel(siege+"");
		siegeLabel.setFont(schriftFont);
		double prozent = highscoreElement.getSiegeInProzent(modus);
		prozent *= 1000;
		prozent = Math.round(prozent);
		prozent /= 10;
		prozentLabel = new JLabel(prozent+"");
		prozentLabel.setFont(schriftFont);
		this.add(platzLabel);
		this.add(nameLabel);
		this.add(spieleLabel);
		this.add(siegeLabel);
		this.add(prozentLabel);
	}

	public static JPanel getBeschriftungsPanel() {
		JPanel beschriftungsPanel = new JPanel();
		beschriftungsPanel.setLayout(new GridLayout(1, 5, 10, 0));
		beschriftungsPanel.add(new JLabel("Rang"));
		beschriftungsPanel.add(new JLabel("Name"));
		beschriftungsPanel.add(new JLabel("Spiele"));
		beschriftungsPanel.add(new JLabel("Siege"));
		beschriftungsPanel.add(new JLabel("Prozent der Siege"));
		return beschriftungsPanel;
	}

	public void setPlatz(String platz) {
		platzLabel.setText(platz);
	}

}
