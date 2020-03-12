package gui.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import ablauf.MatchCredits;
import ablauf.Siegerehrung;
import games.Game;
import games.GameListener;
import games.Modus;
import gui.Anzeige;
import player.Player;
import start.X;
import util.SpielListen;

public class QuickStartPanel extends Anzeige {
	private static final long serialVersionUID = 1L;
	private static String myBackground = "/media/ablauf/iceBG.jpg";
	private List<Integer> spielListe;
	private List<String> spielNamen;
	protected Game game = null;

	public QuickStartPanel(){
		BorderLayout myLayout = new BorderLayout();
		this.setLayout(myLayout);
		this.setOpaque(false);
		this.spielListe = SpielListen.getPCSpieleIDs(Modus.DUELL);
		this.spielNamen = SpielListen.getGameNames(spielListe);
		JPanel spielPanel = new JPanel(new FlowLayout());
		this.add(spielPanel);
		for(int i=0; i<spielListe.size(); i++) {
			JButton b = new JButton(spielNamen.get(i));
			b.addActionListener(new GameButtonActionListener(spielListe.get(i)));
			spielPanel.add(b);
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
		Class<?>[] formparas = new Class[4];
		formparas[0] = Player[].class;
		formparas[1] = Modus.class;
		formparas[2] = String.class;
		formparas[3] = Integer.TYPE;
		Player[] myPlayer = { new Player("Bla"), new Player("Blub") };
		Modus modus = Modus.DUELL;
		try {
			Class<?> c = Class.forName(SpielListen.getSpieleMap().get(gameId).getPath());
			String background = SpielListen.getSpieleMap().get(gameId).getBackground();
			Constructor<?> con = c.getConstructor(formparas);
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
						X.getInstance().changeAnzeige(new Siegerehrung(game.myPlayer[0]));
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
