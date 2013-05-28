package highscore;

import games.Modus;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import start.X;

public class GameHighscorePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GameHighscore highscore;
	private JLabel gameNameLabel;
	private JTabbedPane modusTabbedPane;
	private GameHighscoreListePanel soloHighscoreListe;
	private GameHighscoreListePanel duellHighscoreListe;
	private GameHighscoreListePanel trippleHighscoreListe;
	private GameHighscoreListePanel viererHighscoreListe;
	private Modus modus;
	
	public GameHighscorePanel(GameHighscore highscore, Modus modus){
		this.modus = modus;
		this.highscore = highscore;
		initGUI();
	}
	
	public GameHighscorePanel(GameHighscore highscore){
		this(highscore, Modus.DUELL);
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		initGameNameLabel();
		this.add(gameNameLabel, BorderLayout.NORTH);
		initModusTabbedPane();
		this.add(modusTabbedPane, BorderLayout.CENTER);
		setSelectedPanel();
	}

	private void initGameNameLabel() {
		String gameName = highscore.getGameName();
		String firstLetter = gameName.substring(0, 1).toUpperCase();
		gameName = firstLetter + gameName.substring(1);
		gameNameLabel = new JLabel(gameName);
		gameNameLabel.setFont(X.buttonFont);
		gameNameLabel.setHorizontalAlignment(JLabel.CENTER);
	}

	private void initModusTabbedPane() {
		modusTabbedPane = new JTabbedPane();
		soloHighscoreListe = new GameHighscoreListePanel(highscore, Modus.SOLO);
		modusTabbedPane.add(soloHighscoreListe, "Solo");
		duellHighscoreListe = new GameHighscoreListePanel(highscore, Modus.DUELL);
		modusTabbedPane.add(duellHighscoreListe, "Duell");
		trippleHighscoreListe = new GameHighscoreListePanel(highscore, Modus.TRIPPLE);
		modusTabbedPane.add(trippleHighscoreListe, "Tripple");
		viererHighscoreListe = new GameHighscoreListePanel(highscore, Modus.VIERER);
		modusTabbedPane.add(viererHighscoreListe, "Vierer");
	}

	private void setSelectedPanel() {
		if(modus == Modus.SOLO)
			modusTabbedPane.setSelectedComponent(soloHighscoreListe);
		if(modus == Modus.DUELL)
			modusTabbedPane.setSelectedComponent(duellHighscoreListe);
		if(modus == Modus.TRIPPLE)
			modusTabbedPane.setSelectedComponent(trippleHighscoreListe);
		if(modus == Modus.VIERER)
			modusTabbedPane.setSelectedComponent(viererHighscoreListe);
	}
}
