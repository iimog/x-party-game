package gui.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ablauf.MatchCredits;
import ablauf.Siegerehrung;
import games.Ergebnis;
import games.Game;
import games.GameListener;
import games.Modus;
import gui.Anzeige;
import gui.components.DefaultButton;
import highscore.HighscoreFileHandler;
import player.Player;
import settings.SettingsFileHandler;
import start.X;
import util.FocusRequestListener;
import util.SpielListen;

public class QuickStartPanel extends Anzeige {
	private static final long serialVersionUID = 1L;
	private static final String SETTINGS_NAME = ".quickGamePlayers";
	private static final String NUM_PLAYERS = "Spielerzahl";
	private static final String[] PLAYER = {"Player 1", "Player 2", "Player 3", "Player 4"};
	private static String myBackground = "/media/ablauf/iceBG.jpg";
	private List<Integer> spielListe;
	private List<String> spielNamen;
	protected Game game = null;
	private JPanel bottomPanel;
	private DefaultButton hauptMenuButton;
	private JPanel topPanel;
	private JTextField playerTextField[];
	private final Color[] playerColors = { Color.RED, Color.BLUE, Color.ORANGE, Color.GREEN.darker() };
	private final int[] playerBuzzerKeys = { KeyEvent.VK_A, KeyEvent.VK_L, KeyEvent.VK_C, KeyEvent.VK_U };
	private int spielerZahl = 2;
	private Modus[] modi = {Modus.SOLO, Modus.DUELL, Modus.TRIPPLE, Modus.VIERER};
	private JPanel spielPanel;

	public QuickStartPanel(){
		BorderLayout myLayout = new BorderLayout();
		this.setLayout(myLayout);
		this.setOpaque(false);
		spielPanel = new JPanel(new GridLayout(0,6,5,5));
		spielPanel.setOpaque(false);
		this.add(spielPanel);
		bottomPanel = new JPanel();
		FlowLayout bottomPanelLayout = new FlowLayout();
		bottomPanel.setLayout(bottomPanelLayout);
		bottomPanelLayout.setAlignment(FlowLayout.RIGHT);
		bottomPanel.setOpaque(false);
		this.add(bottomPanel, BorderLayout.SOUTH);
		{
			hauptMenuButton = new DefaultButton("Hauptmenü");
			hauptMenuButton.setFont(X.BUTTON_FONT);
			hauptMenuButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					X.getInstance().changeAnzeige(new HauptMenu());;
				}
			});
			bottomPanel.add(hauptMenuButton);
		}
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.setOpaque(false);
		this.add(topPanel, BorderLayout.NORTH);
		{
			int columns = 10;
			playerTextField = new JTextField[4];
			for(int i=0; i<4; i++) {
				playerTextField[i] = new JTextField("Player "+(i+1),columns);
				playerTextField[i].setFont(X.BUTTON_FONT);
				playerTextField[i].setForeground(playerColors[i]);
				if(i>=spielerZahl) {
					playerTextField[i].setVisible(false);
				}
				topPanel.add(playerTextField[i]);
			}
			playerTextField[0].addAncestorListener(new FocusRequestListener());
		}
		Properties playerSettings = SettingsFileHandler.loadSettings(SETTINGS_NAME);
		if(playerSettings != null) {
			for(int i=0; i<4; i++) {
				playerTextField[i].setText(playerSettings.getProperty(PLAYER[i], "Player "+(i+1)));
			}
			spielerZahl = Integer.parseInt(playerSettings.getProperty(NUM_PLAYERS, "2"));
		}
		JLabel playersLabel = new JLabel("Spieler: ");
		playersLabel.setFont(X.BUTTON_FONT);
		playersLabel.setForeground(Color.WHITE);
		topPanel.add(playersLabel);
		for(int i=0; i<4; i++) {
			JButton playerCountButton = new JButton(""+(i+1));
			topPanel.add(playerCountButton);
			playerCountButton.addActionListener(new SpielerZahlActionListener(i+1));
		}
		setSpielerZahl(spielerZahl);
	}
	
	private class SpielerZahlActionListener implements ActionListener{
		private int neueSpielerZahl;
		public SpielerZahlActionListener(int neueSpielerZahl) {
			this.neueSpielerZahl = neueSpielerZahl;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			setSpielerZahl(neueSpielerZahl);
		}
	}
	
	private void setSpielerZahl(int spielerZahl) {
		this.spielerZahl = spielerZahl;
		this.spielListe = SpielListen.getPCSpieleIDs(modi[spielerZahl-1]);
		this.spielNamen = SpielListen.getGameNames(spielListe);
		spielPanel.removeAll();
		for(int i=0; i<spielListe.size(); i++) {
			JButton b = new DefaultButton(spielNamen.get(i));
			b.addActionListener(new GameButtonActionListener(spielListe.get(i)));
			spielPanel.add(b);
		}
		for(int i=0; i<4; i++) {
			if(i<spielerZahl) {
				playerTextField[i].setVisible(true);
			} else {
				playerTextField[i].setVisible(false);
			}
		}
		revalidate();
		repaint();
	}

	class GameButtonActionListener implements ActionListener{
		int gameId;
		public GameButtonActionListener(int gameId) {
			this.gameId = gameId;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			startGame(gameId);
		}
	}
	protected void startGame(int gameId) {
		if(gameId == SpielListen.RANDOM_GAME) {
			startGame(spielListe.get((int) Math.round(Math.random()*spielListe.size())));
			return;
		}
		Class<?>[] formparas = new Class[4];
		formparas[0] = Player[].class;
		formparas[1] = Modus.class;
		formparas[2] = String.class;
		formparas[3] = Integer.TYPE;
		Player[] myPlayer = new Player[spielerZahl];
		for(int i=0; i<spielerZahl; i++){ 
			myPlayer[i] = new Player(playerTextField[i].getText(),false,playerColors[i],playerBuzzerKeys[i]); 
		};
		Properties quickgamePlayersProperties = new Properties();
		for(int i=0; i<4; i++) {
			quickgamePlayersProperties.setProperty(PLAYER[i], playerTextField[i].getText());
		}
		quickgamePlayersProperties.setProperty(NUM_PLAYERS, spielerZahl+"");
		SettingsFileHandler.saveSettings(SETTINGS_NAME, quickgamePlayersProperties);
		Modus modus = modi[spielerZahl-1];
		try {
			Class<?> c = Class.forName(SpielListen.getSpieleMap().get(gameId).getPath());
			String background = SpielListen.getSpieleMap().get(gameId).getBackground();
			Constructor<?> con = c.getConstructor(formparas);
			for(int i=0; i<myPlayer.length; i++) {
				X.getInstance().addBuzzer(i, myPlayer[i].getKey());
			}
			game = (Game) con.newInstance(new Object[] { myPlayer, modus, background, gameId });
			game.addGameListener(new GameListener() {
				@Override
				public void gameOver() {
					X.getInstance().forgetBuzzers();
					List<Player> playersWithMaxScore = getPlayersWithMaxScore(game);
					if(playersWithMaxScore.size() == 1) {
						Player winner = playersWithMaxScore.get(0);
						X.getInstance().changeAnzeige(new Siegerehrung(winner));
						HighscoreFileHandler.saveGameHighscore(new Ergebnis(game, winner));
					} else {
						if (game.myPlayer[0].gameCredit == MatchCredits.UNENTSCHIEDEN) {
							X.getInstance().changeAnzeige(new Siegerehrung(new Player("Unentschieden",false,Color.DARK_GRAY)));
						} else {
							X.getInstance().changeAnzeige(new HauptMenu());
						}
					}
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private List<Player> getPlayersWithMaxScore(Game game) {
		List<Player> playersWithMaxScore = new ArrayList<Player>();
		List<Integer> scores = new ArrayList<Integer>();
		for(Player p : game.myPlayer) {
			scores.add(p.gameCredit);
		}
		int maxScore = Collections.max(scores);
		for(Player p : game.myPlayer) {
			if(p.gameCredit == maxScore) {
				playersWithMaxScore.add(p);
			}
		}
		return playersWithMaxScore;
	}

	@Override
	public void nowVisible(){
		instance.changeBackground(myBackground);
	}
}
