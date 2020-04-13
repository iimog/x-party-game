package games.stimmts;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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

	public static final int[] trueBuzz = { KeyEvent.VK_D, KeyEvent.VK_K,
			KeyEvent.VK_B, KeyEvent.VK_Z };
	public static final int[] falseBuzz = { KeyEvent.VK_F, KeyEvent.VK_L,
			KeyEvent.VK_N, KeyEvent.VK_U };

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
	boolean[] vermutung;
	int timeProAussage = 5;
	private Countdown countdown;
	int current = -1;
	boolean letzteAussageWahr;
	Set<Integer> winnerIDs;
	private boolean[] answerGiven;
	List<Deck> stimmtsDecks;
	StimmtsDeck currentDeck;

	public Stimmts(Player[] player, Modus modus, String background, int globalGameID) {
		super(player, defaultNumOfRounds, modus, background, globalGameID);
		currentDeck = new StimmtsDeck(stimmtsDecks.get(new Random().nextInt(stimmtsDecks.size())));
		vermutung = new boolean[player.length];
		answerGiven = new boolean[player.length];
		resetVermutungen();
		initGUI();
		registerBuzzerActions();
		settingsChanged();
	}

	private void registerBuzzerActions() {
		for (int i = 0; i < spielerZahl; i++) {
			Action actionTrue = new BuzzerAction(i, true);
			String actionName = "Truebuzzer" + i;
			// Register keystroke
			hauptbereichPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(trueBuzz[i], 0), actionName);
			// Register action
			hauptbereichPanel.getActionMap().put(actionName, actionTrue);

			Action actionFalse = new BuzzerAction(i, false);
			String actionName2 = "Falsebuzzer" + i;
			// Register keystroke
			hauptbereichPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(falseBuzz[i], 0), actionName2);
			// Register action
			hauptbereichPanel.getActionMap().put(actionName2, actionFalse);
		}

	}

	private void resetVermutungen() {
		for (int i = 0; i < spielerZahl; i++) {
			vermutung[i] = true;
			answerGiven[i] = false;
		}
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		spielBereichPanel.add(hauptbereichPanel);
		addAussageLabel();
		addCountdown();
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
		letzteAussageWahr = currentDeck.getAussage(current).isWahr();
		if (modus == Modus.SOLO) {
			// Boten Ana wählt immer das Gegenteil des Spielers
			vermutung[1] = !vermutung[0];
		}
		winnerIDs = new HashSet<Integer>();
		for (int i = 0; i < spielerZahl; i++) {
			if (!answerGiven[i])
				vermutung[i] = !letzteAussageWahr;
			if (vermutung[i] == letzteAussageWahr)
				winnerIDs.add(i);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
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
		}
	}

	private class BuzzerAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private int playerID;
		private boolean stimmt;

		public BuzzerAction(int playerID, boolean stimmt) {
			this.playerID = playerID;
			this.stimmt = stimmt;
		}

		public void actionPerformed(ActionEvent e) {
			answerGiven[playerID] = true;
			vermutung[playerID] = stimmt;
		}
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		countdown.setSecs(timeProAussage);
		schonWeg = new HashSet<Integer>();
		current = nextRandom(currentDeck.getSize());
		//TODO deckNameLabel.setText(currentDeck.getDeckName());
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
		stimmtsDecks = DeckLoader.loadDecks("stimmts", true);
	}
}
