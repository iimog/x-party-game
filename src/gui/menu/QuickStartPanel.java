package gui.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ablauf.MatchCredits;
import ablauf.Siegerehrung;
import games.Game;
import games.GameListener;
import games.Modus;
import gui.Anzeige;
import gui.components.DefaultButton;
import player.Player;
import settings.SettingsFileHandler;
import start.X;
import util.SpielListen;

public class QuickStartPanel extends Anzeige {
	private static final long serialVersionUID = 1L;
	private static final String SETTINGS_NAME = ".quickGamePlayers";
	private static final String PLAYER1 = "Player 1";
	private static final String PLAYER2 = "Player 2";
	private static String myBackground = "/media/ablauf/iceBG.jpg";
	private List<Integer> spielListe;
	private List<String> spielNamen;
	protected Game game = null;
	private JPanel bottomPanel;
	private DefaultButton hauptMenuButton;
	private JPanel topPanel;
	private JTextField player1TextField;
	private JTextField player2TextField;

	public QuickStartPanel(){
		BorderLayout myLayout = new BorderLayout();
		this.setLayout(myLayout);
		this.setOpaque(false);
		this.spielListe = SpielListen.getPCSpieleIDs(Modus.DUELL);
		this.spielNamen = SpielListen.getGameNames(spielListe);
		JPanel spielPanel = new JPanel(new GridLayout(0,6,5,5));
		spielPanel.setOpaque(false);
		this.add(spielPanel);
		for(int i=0; i<spielListe.size(); i++) {
			JButton b = new DefaultButton(spielNamen.get(i));
			b.addActionListener(new GameButtonActionListener(spielListe.get(i)));
			spielPanel.add(b);
		}
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
			player1TextField = new JTextField("Player 1",columns);
			player1TextField.setFont(X.BUTTON_FONT);
			player1TextField.setForeground(Color.RED);
			player2TextField = new JTextField("Player 2",columns);
			player2TextField.setFont(X.BUTTON_FONT);
			player2TextField.setForeground(Color.BLUE);
			topPanel.add(player1TextField);
			topPanel.add(player2TextField);
		}
		Properties playerSettings = SettingsFileHandler.loadSettings(SETTINGS_NAME);
		if(playerSettings != null) {
			player1TextField.setText(playerSettings.getProperty(PLAYER1, "Player 1"));
			player2TextField.setText(playerSettings.getProperty(PLAYER2, "Player 2"));
		}
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
		Player[] myPlayer = { 
				new Player(player1TextField.getText(),true,Color.RED,KeyEvent.VK_A), 
				new Player(player2TextField.getText(),false,Color.BLUE,KeyEvent.VK_L) 
			};
		Properties quickgamePlayersProperties = new Properties();
		quickgamePlayersProperties.setProperty(PLAYER1, player1TextField.getText());
		quickgamePlayersProperties.setProperty(PLAYER2, player2TextField.getText());
		SettingsFileHandler.saveSettings(SETTINGS_NAME, quickgamePlayersProperties);
		Modus modus = Modus.DUELL;
		try {
			Class<?> c = Class.forName(SpielListen.getSpieleMap().get(gameId).getPath());
			String background = SpielListen.getSpieleMap().get(gameId).getBackground();
			Constructor<?> con = c.getConstructor(formparas);
			X.getInstance().addBuzzer(0, myPlayer[0].getKey());
			X.getInstance().addBuzzer(1, myPlayer[1].getKey());
			game = (Game) con.newInstance(new Object[] { myPlayer, modus, background, gameId });
			game.addGameListener(new GameListener() {
				@Override
				public void gameOver() {
					X.getInstance().forgetBuzzers();
					int winnerID = -1;
					if(game.myPlayer[0].gameCredit > game.myPlayer[1].gameCredit) {
						winnerID = 0;
					}
					if(game.myPlayer[0].gameCredit < game.myPlayer[1].gameCredit) {
						winnerID = 1;
					}
					if(winnerID != -1) {
						X.getInstance().changeAnzeige(new Siegerehrung(game.myPlayer[winnerID]));
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

	@Override
	public void nowVisible(){
		instance.changeBackground(myBackground);
	}
}
