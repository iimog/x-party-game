package ablauf;

import games.Ergebnis;
import games.Game;
import games.GameListener;
import games.Modus;
import gui.EasyDialog;
import highscore.HighscoreFileHandler;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import player.Player;
import settings.Profile;
import start.X;
import util.ChangeManager;
import util.SpielListen;

public class Spielablauf implements GameListener, Ablauf {

	private File speicherDatei;

	public File getSpeicherDatei() {
		return speicherDatei;
	}

	public void setSpeicherDatei(File speicherDatei) {
		this.speicherDatei = speicherDatei;
	}

	private List<Integer> gameList;

	public List<Integer> getGameList() {
		return gameList;
	}

	public void setGameList(ArrayList<Integer> gameList) {
		this.gameList = gameList;
	}

	private ArrayList<Integer> bisherigeErgebnisse;

	public ArrayList<Integer> getBisherigeErgebnisse() {
		return bisherigeErgebnisse;
	}

	public void setBisherigeErgebnisse(ArrayList<Integer> bisherigeErgebnisse) {
		this.bisherigeErgebnisse = bisherigeErgebnisse;
	}

	Player[] myPlayer;
	int numOfGames = 15;
	MatchCredits mCredits;
	private int whichGame = 0;
	private Modus modus;

	private HashSet<Integer> playedGameIDs;

	public HashSet<Integer> getPlayedGameIDs() {
		return playedGameIDs;
	}

	public void setPlayedGameIDs(HashSet<Integer> playedGameIDs) {
		this.playedGameIDs = playedGameIDs;
	}

	Game actGame;
	private ChangeManager standardChangeManager = new ChangeManager() {
		public void change() {
			if (!mCredits.beendet) {
				startNextGame();
			} else {
				matchBeenden();
			}
		}
	};
	private int spielerZahl;
	private Profile profile;

	public List<Integer> getErwuenschteSpieleIDs() {
		return profile.getSpielliste(modus);
	}

	private Spielablauf() {

	}

	public Spielablauf(Player[] pl, int numOfGames, List<Integer> gameList,
			Modus modus, Profile profile) {
		this.modus = modus;
		spielerZahl = modus.getSpielerzahl();
		if (modus == Modus.SOLO)
			spielerZahl++;
		this.myPlayer = pl;
		this.numOfGames = numOfGames;
		this.gameList = gameList;
		playedGameIDs = new HashSet<Integer>(gameList.size());
		this.setProfile(profile);
		bisherigeErgebnisse = new ArrayList<Integer>();
		this.speicherDatei = MatchFileHandler.saveMatch(this);
		mCredits = new MatchCredits(this);
		mCredits.setPunkteModus(profile.getPunkteModus());
		mCredits.addChangeManager(standardChangeManager);
		addBuzzerToKeymap(pl);
		startNextGame();
	}

	public static Spielablauf createSpielablauf(Player[] pl,
			ArrayList<Integer> gameList, Modus modus) {
		Spielablauf sa = new Spielablauf();
		sa.gameList = gameList;
		sa.myPlayer = pl;
		sa.numOfGames = gameList.size();
		sa.modus = modus;
		sa.spielerZahl = modus.getSpielerzahl();
		sa.setProfile(Profile.getDefaultProfile());
		if (modus == Modus.SOLO)
			sa.spielerZahl++;
		//addBuzzerToKeymap(pl);
		return sa;
	}

	private static void addBuzzerToKeymap(Player[] pl) {
		standardBuzzerTest(pl);
		for (int i = 0; i < pl.length; i++) {
			X.getInstance().addBuzzer(i, pl[i].getKey());
		}
	}

	// TODO bei undefinierter Buzzertaste neue zuweisen
	public static void standardBuzzerTest(Player[] pl) {
		HashSet<Integer> verbrauchteBuzzer = new HashSet<Integer>();
		ArrayList<Integer> neueZuweisen = new ArrayList<Integer>();
		for (int i = 0; i < pl.length; i++) {
			if (!verbrauchteBuzzer.add(pl[i].getKey())) {
				neueZuweisen.add(i);
			}
		}
		if (neueZuweisen.size() > 0) {
			int c = 0;
			for (int i = 0; i < neueZuweisen.size(); i++) {
				int buzz = 0;
				do {
					buzz = Game.standardBuzz[c];
					pl[neueZuweisen.get(i)].setKey(buzz);
					c++;
				} while (!verbrauchteBuzzer.add(buzz));
			}

			StringBuilder message = new StringBuilder(
					"ACHTUNG! Neue Buzzertasten: ");
			for (int i = 0; i < pl.length; i++) {
				message.append(pl[i].name + " --> "
						+ KeyEvent.getKeyText(pl[i].getKey()) + "; ");
			}
			EasyDialog.showMessage(message.toString());
		}
	}

	/**
	 * Diese Methode liefert das Spiel mit der entsprechenden iD
	 * 
	 * @param iD
	 * @return Game
	 */
	public Game gameGenerator(int iD) {
		Game game = null;
		Class<?>[] formparas = new Class[2];
		formparas[0] = Player[].class;
		formparas[1] = Modus.class;
		try {
			Class<?> c = Class.forName(SpielListen.getSpieleMap().get(iD).getPath());
			Constructor<?> con = c.getConstructor(formparas);
			game = (Game) con.newInstance(new Object[] { myPlayer, modus });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return game;
	}

	@Override
	/**
	 * Handled das Ende eines Spiels --> Anzeige des Punktestands und vorbereiten des nächsten Spieles
	 */
	public void gameOver() {
		int winnerID = -1;
		for (int i = 0; i < actGame.myPlayer.length; i++) {
			boolean alleinigerSieger = true;
			for (int j = 0; j < actGame.myPlayer.length; j++) {
				if (i != j
						&& actGame.myPlayer[j].gameCredit >= actGame.myPlayer[i].gameCredit) {
					alleinigerSieger = false;
				}
			}
			if (alleinigerSieger) {
				winnerID = i;
				break;
			}
		}
		if (winnerID != -1) {
			mCredits.gameWinner(winnerID, myPlayer[winnerID].farbe);
			if (modus != Modus.TEAM)
				HighscoreFileHandler.saveGameHighscore(new Ergebnis(actGame,
						myPlayer[winnerID]));
		}
		if (winnerID == -1
				&& actGame.myPlayer[0].gameCredit == MatchCredits.UNENTSCHIEDEN) {
			mCredits.gameWinner(MatchCredits.UNENTSCHIEDEN, Color.BLACK);
			winnerID = MatchCredits.UNENTSCHIEDEN;
		}
		bisherigeErgebnisse.add(winnerID);
		showZwischenstand();
		MatchFileHandler.overwriteMatch(speicherDatei, this);
		// TODO eine newGame() oder reset() Methode für Player schreiben, wo
		// alle Spielwerte zurückgesetzt werden
		for (int i = 0; i < myPlayer.length; i++) {
			myPlayer[i].gameCredit = 0;
		}
		whichGame++;
	}

	public Modus getModus() {
		return modus;
	}

	@Override
	public int getNumOfGames() {
		return numOfGames;
	}

	@Override
	public Player[] getPlayers() {
		return myPlayer;
	}

	private void matchBeenden() {
		MatchFileHandler.deleteMatchFile(speicherDatei);
		HighscoreFileHandler.saveMatchHighscore(modus, myPlayer,
				mCredits.getWinner());
		X.getInstance().forgetBuzzers();
		X.getInstance().changeAnzeige(new Siegerehrung(mCredits.getWinner()));
	}

	/**
	 * Diese Methode liefert die ID des nächsten Spiels durch Zufall. Falls
	 * wiederholen false ist dürfen sich die Spiele erst wiederholen, wenn alle
	 * einmal durch sind.
	 * 
	 * @param wiederholen
	 * @return nextID
	 */
	public int nextGameID(boolean wiederholen) {
		Random r = new Random();
		while (true) {
			int nextID = r.nextInt(SpielListen.getTotalGameNumber()) + 1;
			if (!getErwuenschteSpieleIDs().contains(nextID)) {
				continue;
			}
			if (!wiederholen) {
				if (playedGameIDs.add(nextID))
					return nextID;
				else {
					if (playedGameIDs.size() >= getErwuenschteSpieleIDs()
							.size() - 1) // -1 da Zufall ein Element ist
						playedGameIDs.removeAll(playedGameIDs);
				}
			} else {
				return nextID;
			}
		}
	}

	public MatchCredits getmCredits() {
		return mCredits;
	}

	public void startNextGame() {
		try {
			if (gameList.get(whichGame) == SpielListen.RANDOM_GAME) {
				int nextGame = nextGameID(false);
				actGame = gameGenerator(nextGame);
			} else {
				actGame = gameGenerator(gameList.get(whichGame));
				playedGameIDs.add(gameList.get(whichGame));
			}
		} catch (IndexOutOfBoundsException e) { // Zufallsspiel falls die
												// GameListe verbraucht ist
			// z.B. weil ein Spiel mit unentschieden abgebrochen wurde
			int nextGame = nextGameID(false);
			actGame = gameGenerator(nextGame);
		}
		actGame.addGameListener(this);
	}

	public void showZwischenstand() {
		addBuzzerToKeymap(myPlayer);
		mCredits.aktualisierePlayer();
		X.getInstance().changeAnzeige(mCredits);
	}

	public void updateErgebnisse() {
		mCredits = new MatchCredits(this);
		mCredits.setPunkteModus(profile.getPunkteModus());
		mCredits.addChangeManager(standardChangeManager);
		for (int i : bisherigeErgebnisse) {
			if (i >= 0 && i < spielerZahl) {
				mCredits.gameWinner(i, myPlayer[i].farbe);
			}
			if (i == MatchCredits.UNENTSCHIEDEN) {
				mCredits.gameWinner(MatchCredits.UNENTSCHIEDEN, Color.black);
			}
		}
		whichGame = bisherigeErgebnisse.size();
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		if (mCredits != null)
			mCredits.setPunkteModus(profile.getPunkteModus());
	}

	public Profile getProfile() {
		return profile;
	}
}
