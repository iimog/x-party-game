package games.stimmts;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.InfoDialog;
import games.dialogeGUIs.RoundDialog;
import gui.components.Countdown;
import player.Player;
import start.X;
import util.ChangeManager;

public class Stimmts extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Stimmts";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);

	private static int defaultNumOfRounds = 5;

	public static String getGameName() {
		return gameName;
	}

	public void pause() {
		super.pause();
		countdown.pause();
	}

	public void resume() {
		super.resume();
		countdown.resume();
	}

	private JPanel hauptbereichPanel;

	private JLabel aussageLabel;
	int timeProAussage = 5;
	private Countdown countdown;
	int current = -1;
	boolean letzteAussageWahr;
	Set<Integer> winnerIDs;
	int[] buzzerCounter;
	List<Deck> stimmtsDecks;
	StimmtsDeck currentDeck;
	private JLabel deckNameLabel;

	public Stimmts(Player[] player, Modus modus, String background, int globalGameID) {
		super(player, defaultNumOfRounds, modus, background, globalGameID);
		currentDeck = new StimmtsDeck(stimmtsDecks.get(new Random().nextInt(stimmtsDecks.size())));
		buzzerCounter = new int[player.length];
		resetVermutungen();
		initGUI();
		settingsChanged();
	}

	private void resetVermutungen() {
		for (int i = 0; i < spielerZahl; i++) {
			buzzerCounter[i] = 0;
		}
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		spielBereichPanel.add(hauptbereichPanel);
		addTopPanel();
		addAussageLabel();
		addCountdown();
	}

	private void addTopPanel() {
		JPanel topPanel = new JPanel(new GridLayout(2,1));
		hauptbereichPanel.add(topPanel, BorderLayout.NORTH);
		deckNameLabel = new JLabel(currentDeck.toString());
		deckNameLabel.setFont(X.BUTTON_FONT);
		deckNameLabel.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(deckNameLabel);
		JLabel explanationLabel = new JLabel("Buzzern: gelogen");
		topPanel.add(explanationLabel);
		//JLabel explanationLabel1 = new JLabel("Wenn Aussage wahr: nicht buzzern (oder 2x, 4x, ...)");
		//topPanel.add(explanationLabel1);
		//JLabel explanationLabel2 = new JLabel("Wenn Aussage falsch: 1x buzzern (oder 3x, 5x, ...)");
		//topPanel.add(explanationLabel2);
	}

	private void addAussageLabel() {
		aussageLabel = new JLabel("Aussagee");
		aussageLabel.setFont(X.BUTTON_FONT);
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(aussageLabel, BorderLayout.CENTER);
	}

	private void addCountdown() {
		countdown = new Countdown(timeProAussage);
		countdown.addChangeManager(new ChangeManager() {
			@Override
			public void change() {
				roundEnd();
			}
		});
		hauptbereichPanel.add(countdown, BorderLayout.SOUTH);
	}

	public void roundEnd() {
		setBuzzerActive(false);
		letzteAussageWahr = currentDeck.getAussage(current).isWahr();
		if (modus == Modus.SOLO) {
			// Boten Ana wählt immer das Gegenteil des Spielers
			buzzerCounter[1] = (buzzerCounter[0]+1)%2;
		}
		winnerIDs = new HashSet<Integer>();
		for (int i = 0; i < spielerZahl; i++) {
			if (buzzerCountToVermutung(buzzerCounter[i]) == letzteAussageWahr)
				winnerIDs.add(i);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
	}

	/**
	 * Infer vermutung from number of buzzer events
	 * no buzzer (or even number) assumes true, one buzzer (or odd number) assumes false
	 * @param n number of buzzer events for player
	 * @return vermutung
	 */
	private boolean buzzerCountToVermutung(int n) {
		return n%2 == 0;
	}

	private void verbuchePunkte(Set<Integer> winnerIDs) {
		if (winnerIDs.size() < myPlayer.length) {
			for (int id : winnerIDs) {
				creds[id].earnsCredit(1);
				myPlayer[id].gameCredit++;
			}
		}
	}

	private String getWinnerText(Set<Integer> winnerIDs) {
		String winner = "";
		if (winnerIDs.size() >= myPlayer.length || winnerIDs.size() == 0) {
			winner = "niemanden";
		} else {
			int c = 0;
			for (int id : winnerIDs) {
				String trenner = "";
				if (c == winnerIDs.size() - 3)
					trenner = ", ";
				if (c == winnerIDs.size() - 2)
					trenner = " und ";
				winner += myPlayer[id].name + trenner;
				c++;
			}
		}
		return winner;
	}

	private void nextRound() {
		if (modus == Modus.TEAM)
			changeActivePlayers();
		letzteAussageWahr = false;
		resetVermutungen();
		current = nextRandom(currentDeck.getSize());
		if (current == -1) {
			abbruch();
			// TODO besser handhaben!
		} else {
			aussageLabel.setText(currentDeck.getAussage(current).getAussage());
			countdown.start();
			setBuzzerActive(true);
		}
	}

	@Override
	public void buzzeredBy(int playerID) {
		buzzerCounter[playerID]++;
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		countdown.setSecs(timeProAussage);
		schonWeg = new HashSet<Integer>();
		current = nextRandom(currentDeck.getSize());
		deckNameLabel.setText(currentDeck.getDeckName());
		aussageLabel.setText(currentDeck.getAussage(current).getAussage());
	}

	@Override
	protected void propertiesToSettings() {
		super.propertiesToSettings();
		if(customSettings == null){
			return;
		}
		String zeitProAussage = customSettings.getProperty(StimmtsSettingsDialog.AUSSAGE_ZEIT, "10");
		timeProAussage = Integer.parseInt(zeitProAussage);
		countdown.setSecs(timeProAussage);
		String deck = customSettings.getProperty(StimmtsSettingsDialog.DECK, "");
		if(deck.equals("Zufall")) {
			currentDeck = new StimmtsDeck(stimmtsDecks.get(new Random().nextInt(stimmtsDecks.size())));
		}
		for (int i = 0; i < stimmtsDecks.size(); i++) {
			if (stimmtsDecks.get(i).toString().equals(deck)) {
				currentDeck = new StimmtsDeck(stimmtsDecks.get(i));
			}
		}
	}

	@Override
	public void start() {
		nextRound();
	}
	
	@Override
	public void openRoundDialog(String winner) {
		RoundDialog rd = new RoundDialog(this, winner);
		instance.showDialog(rd);
	}

	@Override
	public void goBack() {
		if (!isOver())
			nextRound();
	}

	@Override
	public void openSettingsDialog() {
		instance.showDialog(new StimmtsSettingsDialog(this));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new StimmtsDetailsDialog(this));
	}

	@Override
	public void openInfoDialog() {
		instance.showDialog(new InfoDialog(currentDeck.getAussage(current).getInfo()));
	}

	@Override
	public void abbruch() {
		countdown.stop();
		super.abbruch();
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		stimmtsDecks = DeckLoader.loadDecks(getGameFileName(), true);
	}
}
