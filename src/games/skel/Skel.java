package games.skel;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.dialogeGUIs.RoundDialog;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import player.Player;

public class Skel extends Game {
	private static final long serialVersionUID = 1L;
	private JPanel hauptbereichPanel;
	private List<Deck> skelDecks;
	private int currentDeck = 0;
	private boolean inRound = false;

	public List<Deck> getSkelDecks() {
		return skelDecks;
	}

	public Skel(Player[] player, Modus modus, String background,
			int globalGameID) {
		super(player, 5, modus, background, globalGameID);
		initGUI();
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		currentDeck = new Random().nextInt(skelDecks.size());
		// set up the hauptbereichPanel for the first round
		settingsChanged();
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
	}

	@Override
	protected void propertiesToSettings() {
		if (customSettings == null) {
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(
				SkelSettingsDialog.NUM_OF_ROUNDS, "" + numOfRounds));
		// String rotationTimeString = customSettings
		// .getProperty(SkelSettingsDialog.ROTATION_TIME);
		// if (rotationTimeString != null)
		// rotationTime = Integer.parseInt(rotationTimeString);
		String deck = customSettings.getProperty(SkelSettingsDialog.DECK, "");
		for (int i = 0; i < skelDecks.size(); i++) {
			if (skelDecks.get(i).toString().equals(deck)) {
				currentDeck = i;
				// set up game to use this deck
			}
		}
	}

	@Override
	public void start() {
		nextRound();
	}

	@Override
	public void goBack() {
		if (!isOver())
			nextRound();
	}

	private void nextRound() {
		// set everything up for the next round
		inRound = true;
		setBuzzerActive(true);
	}

	@Override
	public void buzzeredBy(int whoBuzz) {
		// handle buzzer events
		// eg end this round and determine winner or activate response field for
		// the one who buzzered
		if(!isBuzzerActive())
			return;
		inRound = false;
		setBuzzerActive(false);
		// winnerIs(whoBuzz, false);
	}

	@Override
	public void openRoundDialog(String winner) {
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}

	@Override
	public void openSettingsDialog() {
		instance.showDialog(new SkelSettingsDialog(this));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new SkelDetailsDialog(this));
	}

	public String getDeckType() {
		return skelDecks.get(currentDeck).getDeckType();
	}

	@Override
	public void pause() {
		super.pause();
		setBuzzerActive(false);
		if (inRound) {
		}
	}

	@Override
	public void resume() {
		super.resume();
		if (inRound) {
			setBuzzerActive(true);
		}
	}

	@Override
	public void loadProperties() {
		skelDecks = DeckLoader.loadDecks("skel");
	}
}
