package games.reaktionszeit;

import games.Game;
import games.Modus;
import games.dialogeGUIs.RoundDialog;
import games.memory.MemorySettingsDialog;
import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;

public class ReaktionsZeit extends Game {
	private static final long serialVersionUID = 1L;
	private JPanel hauptbereichPanel;
	private JLabel targetLabel;
	private Bildschirm targetBildschirm;
	private JPanel queryPanel;
	private ReaktionsZeitRotator queryRotator;
	private int targetIndex;
	private int currentIndex;
	private HashSet<Integer> winnerIDs;
	private int rotationTime = 1;
	private List<String> allAnswers;
	private List<ReaktionsZeitDeck> reaktionszeitDecks;

	public List<ReaktionsZeitDeck> getReaktionszeitDecks() {
		return reaktionszeitDecks;
	}

	private int currentDeck = 0;
	private JPanel targetPanel;

	public ReaktionsZeit(Player[] player, Modus modus, int globalGameID) {
		super(player, 5, modus, globalGameID);
		initGUI();
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		currentDeck = new Random().nextInt(reaktionszeitDecks.size());
		addQuery();
		targetPanel = new JPanel();
		hauptbereichPanel.add(targetPanel, BorderLayout.NORTH);
		setTarget();
		settingsChanged();
	}

	private void addQuery() {
		queryPanel = new JPanel(new GridLayout(1, 1));
		hauptbereichPanel.add(queryPanel, BorderLayout.CENTER);
		updateQuery();
	}
	
	private void updateQuery(){	
		queryPanel.removeAll();
		queryRotator = new ReaktionsZeitRotator(
				reaktionszeitDecks.get(currentDeck));
		queryRotator.setBackground(Color.BLACK);
		queryRotator.setForeground(Color.WHITE);
		queryRotator.setFont(Game.STANDARD_FONT.deriveFont(50f));
		queryRotator.setRotationTime(rotationTime);
		queryRotator.maskComponent();
		queryPanel.add(queryRotator);
		revalidate();
		repaint();
	}

	private void setTarget() {
		targetPanel.removeAll();
		if (reaktionszeitDecks.get(currentDeck).getDeckType()
				.equals(ReaktionsZeitDeck.STRING)) {
			targetLabel = new JLabel("X");
			targetLabel.setFont(Game.STANDARD_FONT.deriveFont(70f));
			targetLabel.setBackground(Color.WHITE);
			targetPanel.add(targetLabel, BorderLayout.NORTH);
		} else if (reaktionszeitDecks.get(currentDeck).getDeckType()
				.equals(ReaktionsZeitDeck.PICTURE)) {
			targetBildschirm = new Bildschirm(reaktionszeitDecks
					.get(currentDeck).getElements().get(0), true);
			targetBildschirm.hidePic(true);
			targetPanel.add(targetBildschirm, BorderLayout.NORTH);
		}
		revalidate();
		repaint();
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		if (queryRotator != null)
			queryRotator.setRotationTime(rotationTime);

	}

	private void propertiesToSettings() {
		if (customSettings == null) {
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(
				MemorySettingsDialog.NUM_OF_ROUNDS, "" + numOfRounds));
		String rotationTimeString = customSettings
				.getProperty(ReaktionsZeitSettingsDialog.ROTATION_TIME);
		if (rotationTimeString != null)
			rotationTime = Integer.parseInt(rotationTimeString);
		String deck = customSettings.getProperty(
				ReaktionsZeitSettingsDialog.DECK, "");
		for (int i = 0; i < reaktionszeitDecks.size(); i++) {
			if (reaktionszeitDecks.get(i).toString().equals(deck)) {
				currentDeck = i;
				updateQuery();
				setTarget();
			}
		}
	}

	@Override
	public void start() {
		nextRound();
	}

	private void nextRound() {
		queryRotator.maskComponent();
		allAnswers = reaktionszeitDecks.get(currentDeck).getElements();
		targetIndex = new Random().nextInt(allAnswers.size());
		if (reaktionszeitDecks.get(currentDeck).getDeckType()
				.equals(ReaktionsZeitDeck.STRING)) {
			targetLabel.setText(allAnswers.get(targetIndex));
		} else if (reaktionszeitDecks.get(currentDeck).getDeckType()
				.equals(ReaktionsZeitDeck.PICTURE)) {
			targetBildschirm.changePic(allAnswers.get(targetIndex));
			targetBildschirm.hidePic(false);
		}
		queryRotator.start();
		setBuzzerActive(true);
	}

	@Override
	public void buzzeredBy(int whoBuzz) {
		queryRotator.pause();
		setBuzzerActive(false);
		currentIndex = queryRotator.getCurrentIndex();
		winnerIDs = new HashSet<Integer>();
		if (currentIndex != targetIndex) {
			for (int i = 0; i < spielerZahl; i++) {
				if (i != whoBuzz)
					winnerIDs.add(i);
			}
		} else {
			winnerIDs.add(whoBuzz);
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

	public void nowVisible() {
		instance.changeBackground("media/reaktionszeit/reaktionszeit.jpg");
	}

	@Override
	public void openRoundDialog(String winner) {
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}

	@Override
	public void goBack() {
		if (!isOver())
			nextRound();
	}

	@Override
	public void openSettingsDialog() {
		instance.showDialog(new ReaktionsZeitSettingsDialog(this));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new ReaktionsZeitDetailsDialog(this));
	}

	public String getCurrent() {
		return allAnswers.get(currentIndex);
	}

	public String getTarget() {
		return allAnswers.get(targetIndex);
	}
	
	public String getDeckType(){
		return reaktionszeitDecks.get(currentDeck).getDeckType();
	}

	@Override
	public void pause() {
		super.pause();
		setBuzzerActive(false);
		queryRotator.pause();
		queryRotator.maskComponent();
	}

	@Override
	public void resume() {
		super.resume();
		queryRotator.start();
		setBuzzerActive(true);
	}

	@Override
	public void loadProperties() {
		reaktionszeitDecks = ReaktionsZeitDeckLoader.loadReaktionsZeitDecks();
		// selectedDeck = MemoryDeck.getRandomDeck(memDecks);
		// while(selectedDeck.getPictures().size() < numOfPairs){
		// selectedDeck = MemoryDeck.getRandomDeck(memDecks);
		// }
		// backsides = MemoryDeckLoader.loadMemoryBacksides();
	}

	public int getRotationTime() {
		return rotationTime;
	}

}
