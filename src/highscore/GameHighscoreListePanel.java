package highscore;

import games.Modus;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import start.X;

public class GameHighscoreListePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GameHighscore highscore;
	private Modus modus;
	private List<GameHighscoreElement> highscoreListe;
	private List<GameHighscoreElementPanel> highscorePanels;
	private int anzeigeGrenzwert = 0;
	private int platzierungsGrenzwert = 3;
	
	public GameHighscoreListePanel(GameHighscore highscore, Modus modus){
		this.highscore = highscore;
		this.modus = modus;
		highscorePanels = Collections.synchronizedList(new ArrayList<GameHighscoreElementPanel>());
		initGUI();
	}

	private void initGUI() {
		highscoreListe = highscore.getElemente();
		Collections.sort(highscoreListe, new GameHighscoreComparator(modus));
		for(GameHighscoreElement ghe: highscoreListe){
			if(ghe.getSpiele(modus) > anzeigeGrenzwert ){
				highscorePanels.add(new GameHighscoreElementPanel(ghe, modus));
			}
		}
		this.setLayout(new GridLayout(highscorePanels.size()+1, 1));
		if(highscorePanels.size()>0){
			this.add(GameHighscoreElementPanel.getBeschriftungsPanel());
		}
		else{
			JLabel keineDatenLabel = new JLabel("Für den "+modus+"-Modus sind noch keine Daten vorhanden.");
			keineDatenLabel.setHorizontalAlignment(JLabel.CENTER);
			keineDatenLabel.setFont(X.buttonFont);
			this.add(keineDatenLabel);
		}
		for(GameHighscoreElementPanel ghep: highscorePanels){
			this.add(ghep);
		}
		rangnummernVergeben();
	}

	private void rangnummernVergeben() {
		if(highscorePanels.size() == 0)return;
		Comparator<GameHighscoreElement> comparator = new GameHighscoreComparator(modus);
		// Color[] farben = {new Color(255,215,0), new Color(218,165,32)};
		Color[] farben = {new Color(24,116,205), new Color(0,191,255)};
		int aktuellerPlatz = 1;
		int zaehler = 1;
		GameHighscoreElement oldGhe = highscorePanels.get(0).getHighscoreElement();
		for(int i=0; i<highscorePanels.size(); i++){
			GameHighscoreElementPanel ghep = highscorePanels.get(i);
			GameHighscoreElement ghe = ghep.getHighscoreElement();
			if(ghe.getSpiele(modus)<platzierungsGrenzwert){
				ghep.setPlatz("X");
				ghep.setBackground(new Color(220,220,220));
				continue;
			}
			else{
				if(comparator.compare(ghe, oldGhe) > 0){
					aktuellerPlatz = zaehler;
				}
				oldGhe = ghe;
				ghep.setPlatz(""+aktuellerPlatz);
				ghep.setBackground(farben[zaehler%2]);
				zaehler++;
			}
		}
	}
}
