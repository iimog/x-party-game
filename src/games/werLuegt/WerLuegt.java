package games.werLuegt;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.InfoDialog;
import games.dialogeGUIs.RoundDialog;
import player.Player;
import start.X;
import util.ChangeManager;

public class WerLuegt extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Wer lügt";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);
	static Font labelFont = X.BUTTON_FONT.deriveFont(50f);

	private static int defaultNumOfRounds = 5;

	public static String getGameName() {
		return gameName;
	}

	private JPanel hauptbereichPanel;

	private JLabel aussageLabel;
	private WerLuegtRotator aktuelleAntwortRotator;
	int timeProAussage = 5;
	int current = -1;
	int whoBuzzed;
	Set<Integer> winnerIDs;
	private JPanel aktuelleAntwortPanel;
	private WerLuegtDeck currentDeck;
	private JLabel deckLabel;

	public WerLuegt(Player[] player, Modus modus, String background,
			int globalGameID) {
		super(player, defaultNumOfRounds, modus, background, globalGameID);
		currentDeck = new WerLuegtDeck(werLuegtDecks.get(new Random().nextInt(werLuegtDecks.size())));
		initGUI();
		settingsChanged();
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		addDeckLabel();
		addAussageLabel();
		addAktuelleAntwortPanel();
	}

	private void addDeckLabel() {
		deckLabel = new JLabel(currentDeck.toString());
		deckLabel.setFont(STANDARD_FONT);
		deckLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hauptbereichPanel.add(deckLabel, BorderLayout.NORTH);
	}

	private void addAktuelleAntwortPanel() {
		aktuelleAntwortPanel = new JPanel();
		aktuelleAntwortPanel.setLayout(new GridLayout(2, 1));
		aktuelleAntwortPanel.setOpaque(false);
		aktuelleAntwortPanel.add(new JLabel());
		aktuelleAntwortRotator = new WerLuegtRotator();
		aktuelleAntwortRotator.setFont(labelFont);
		aktuelleAntwortRotator.setOpaque(true);
		aktuelleAntwortRotator.addChangeManager(new ChangeManager() {
			@Override
			public void change() {
				roundEnd(-1);
			}
		});
		aktuelleAntwortRotator.setRotationTime(timeProAussage);
		// aktuelleAntwortRotator.setHorizontalAlignment(JLabel.CENTER);
		// aktuelleAntwortRotator.setBorder(BorderFactory.createLineBorder(Color.black));
		aktuelleAntwortPanel.add(aktuelleAntwortRotator);
		hauptbereichPanel.add(aktuelleAntwortPanel, BorderLayout.SOUTH);
	}

	private void addAussageLabel() {
		aussageLabel = new JLabel("Aussage");
		aussageLabel.setOpaque(true);
		aussageLabel.setFont(labelFont);
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(aussageLabel, BorderLayout.CENTER);
	}

	@Override
	public void buzzeredBy(int playerID) {
		setBuzzerActive(false);
		aktuelleAntwortRotator.pause();
		roundEnd(playerID);
	}

	private boolean aktuelleAussageWahr;
	List<Deck> werLuegtDecks;

	public void roundEnd(int whoBuzzered) {
		aktuelleAntwortRotator.pause();
		whoBuzzed = whoBuzzered;
		aktuelleAussageWahr = aktuelleAntwortRotator.isCurrentTrue();
		winnerIDs = new HashSet<Integer>();
		if (aktuelleAussageWahr || whoBuzzered == -1) {
			// Leider zu Unrecht gebuzzert oder keiner gebuzzert
			for (int i = 0; i < spielerZahl; i++) {
				if (i != whoBuzzered)
					winnerIDs.add(i);
			}
		} else {
			// Recht gehabt, die Aussage wahr falsch
			winnerIDs.add(whoBuzzered);
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
		aktuelleAussageWahr = false;
		current = nextRandom(currentDeck.getSize());
		if (current == -1) {
			abbruch();
			// TODO besser handhaben!
		} else {
			setBuzzerActive(true);
			aktuelleAntwortRotator.changeDeckToAussage(currentDeck.getAussage(current));
			aussageLabel.setText(aktuelleAntwortRotator.getDeckName());
			aktuelleAntwortRotator.start();
		}
	}

	public void pause() {
		super.pause();
		aktuelleAntwortRotator.pause();
	}

	public void resume() {
		super.resume();
		aktuelleAntwortRotator.start();
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		if (aktuelleAntwortRotator != null)
			aktuelleAntwortRotator.setRotationTime(timeProAussage);
		deckLabel.setText(currentDeck.getDeckName());
	}

	@Override
	protected void propertiesToSettings() {
		super.propertiesToSettings();
		if (customSettings == null)
			return;
		String time = customSettings.getProperty(
				WerLuegtSettingsDialog.AUSSAGEZEIT, "5");
		timeProAussage = Integer.parseInt(time);
		String deck = customSettings.getProperty(WerLuegtSettingsDialog.DECK, "");
		if(deck.equals("Zufall")) {
			currentDeck = new WerLuegtDeck(werLuegtDecks.get(new Random().nextInt(werLuegtDecks.size())));
		}
		for (int i = 0; i < werLuegtDecks.size(); i++) {
			if (werLuegtDecks.get(i).toString().equals(deck)) {
				currentDeck = new WerLuegtDeck(werLuegtDecks.get(i));
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
	public void openSettingsDialog(boolean inGame) {
		instance.showDialog(new WerLuegtSettingsDialog(this, inGame));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new WerLuegtDetailsDialog(this));
	}

	@Override
	public void openInfoDialog() {
		instance.showDialog(new InfoDialog(aktuelleAntwortRotator.getDeckInfo()));
	}

	public String getCurrentAussage() {
		return aktuelleAntwortRotator.getDeckName();
	}

	public Map<String, Boolean> getCorrectAnswers() {
		return aktuelleAntwortRotator.getCorrectAnswers();
	}

	public List<String> getVerlauf() {
		return aktuelleAntwortRotator.getVerlauf();
	}
	
	@Override
	public void loadProperties() {
		werLuegtDecks = DeckLoader.loadDecks(getGameFileName(), true);
	}
}
