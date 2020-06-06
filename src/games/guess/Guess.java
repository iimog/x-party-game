package games.guess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.PC;
import player.Player;
import start.X;

public class Guess extends Game implements PC {

	private static final long serialVersionUID = 1L;

	public class Action implements ActionListener {
		public int loesung;
		public int[] pl = new int[spielerZahl];
		public long distance;

		@Override
		public void actionPerformed(ActionEvent e) {
			loesung = currentDeck.getAnswer(current);
			last = current; // um noch auf die letzte Antwort zugreifen zu können, nachdem current erneuert
							// wurde
			distance = Math.round(loesung * toleranz); // 25% Abweichung sind ok
			if (!toleranzOn) {
				distance = Integer.MAX_VALUE;
			}
			for (int i = 0; i < spielerZahl; i++) {
				try {
					myPlayer[i].lastAnswer = String.valueOf(eingabe[i].getPassword());
					pl[i] = Integer.valueOf(myPlayer[i].lastAnswer);
					myPlayer[i].lastDistance = (Math.round(100 * (pl[i] - loesung) / loesung));
				} catch (NumberFormatException e1) {
					pl[i] = -1;
					showMessage(myPlayer[i].name + ", bitte in Zukunft eine ganze Zahl eingeben!");
				}
			}
			// Wer ist der Gewinner
			winnerIs(whoWon());
			if (modus == Modus.TEAM) {
				changeActivePlayers();
			}

			current = nextRandom(currentDeck.getSize());
			if (current != -1) { // Nicht alle Fragen verbraucht
				if (stechen()) {
					for (int i = 0; i < spielerZahl; i++) {
						creds[i].ko();
					}
				}
				text.setText(currentDeck.getQuestion(current));
				for (int i = 0; i < spielerZahl; i++) {
					eingabe[i].setText("");
				}
			} else {
				showMessage("Es sind alle Fragen verbraucht!");
				abbruch();
			}
		}

		public int whoWon() {
			long[] distance = new long[spielerZahl];
			for (int i = 0; i < spielerZahl; i++) {
				distance[i] = Math.abs(loesung - pl[i]);
			}
			int winnerID = -1;
			long miniDist = Long.MAX_VALUE;
			for (int i = 0; i < spielerZahl; i++) {
				long dist = distance[i];
				if (dist == miniDist) {
					winnerID = -1;
				}
				if (dist < miniDist) {
					miniDist = dist;
					winnerID = i;
				}
			}
			if (toleranzOn && miniDist > toleranz * currentDeck.getAnswer(current)) {
				winnerID = -1;
			}
			if (modus == Modus.SOLO && winnerID == -1)
				winnerID = 1;
			return winnerID;
		}

		public void winnerIs(int wID) { // wID index of the winner (-1 if nobody won; -2 if both won)
			if (wID == -1) { // there is no winner
				openRoundDialog("keinen");
			} else {
				creds[wID].earnsCredit(1);
				myPlayer[wID].gameCredit++;
				roundWinner = myPlayer[wID];
				openRoundDialog(myPlayer[wID].name);
			}
		}

	}

	static String gameName = "Guess";

	public static String getGameName() {
		return gameName;
	}

	public double toleranz = 0.95;
	public boolean toleranzOn = true;
	// the following part should get moved to an external file
	String noInfo = "Zu dieser Frage sind leider keine zusätzlichen Informationen vorhanden";

	// the following part chooses a random number, that was not yet used
	public int current;
	public int last;
	public Player roundWinner;

	// frame bausteine

	JTextArea text;
	JPanel split = new JPanel();
	JPasswordField[] eingabe;
	JButton finish = new JButton("OK");
	List<Deck> guessDecks;
	GuessDeck currentDeck;
	private JLabel deckNameLabel;

	public Guess(Player[] player, Modus modus, String background, int globalGameID) {
		this(player, 5, modus, background, globalGameID);
	}

	public Guess(Player[] players, int numOfRounds, Modus modus, String background, int globalGameID) {
		super(players, numOfRounds, modus, background, globalGameID);
		// numOfPlayers = players.length; --> if there are more than two Players
		this.numOfRounds = numOfRounds;
		currentDeck = new GuessDeck(guessDecks.get(new Random().nextInt(guessDecks.size())));

		if (modus == Modus.SOLO) {
			spielerZahl--;
			toleranz = 0.5;
		}
		eingabe = new JPasswordField[spielerZahl];
		current = nextRandom(currentDeck.getSize());
		spielBereichPanel.setLayout(new BorderLayout());
		spielBereichPanel.setOpaque(true);
		text = new JTextArea(currentDeck.getQuestion(current));
		text.setEditable(false);
		text.setFont(X.getStandardFont(48));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		spielBereichPanel.add(text, BorderLayout.CENTER);
		deckNameLabel = new JLabel(currentDeck.getDeckName());
		deckNameLabel.setFont(X.getStandardFont(48));
		spielBereichPanel.add(deckNameLabel, BorderLayout.NORTH);
		updateCreds();
		spielBereichPanel.add(split, BorderLayout.SOUTH);
		split.setLayout(new GridLayout(1, spielerZahl + 1));
		for (int i = 0; i < spielerZahl; i++) {
			eingabe[i] = new JPasswordField();
			split.add(eingabe[i]);
		}
		split.add(finish);
		finish.addActionListener(new Action());
		settingsChanged();
	}

	public void openDetailsDialog() {
		instance.showDialog(new GuessDetailsDialog(this));
	}

	public void openInfoDialog() {
		instance.showDialog(new games.dialogeGUIs.InfoDialog(currentDeck.getInfo(last)));
	}

	public void openRoundDialog(String winner) {
		instance.showDialog(new games.dialogeGUIs.RoundDialog(this, winner));
	}

	public void openSettingsDialog() {
		instance.showDialog(new GuessSettingsDialog(this));
	}

	// mit dieser Methode wird die Höhe und Breite des Textfeldes gesetzt, da sonst
	// das
	// Fenster nicht mehr kleiner wird und somit das Bild nicht mittig sitzt
	protected void resizeTextArea(JTextArea tA, int prefWidth) {
		int length = tA.getText().length();
		int size = tA.getFont().getSize();
		double rows = Math.ceil((length * (size - 3)) / prefWidth);
		if (rows == 0)
			rows = 1;
		if (rows >= 4)
			rows--;
		tA.setPreferredSize(new Dimension(prefWidth, (int) rows * (size + 8)));
		// --> evtl. elegantere Lösung...
		/*
		 * FontMetrics fm = getFontMetrics(tA.getFont()); int width =
		 * fm.stringWidth(tA.getText()); int height = fm.getHeight(); double rows =
		 * Math.ceil(width/prefWidth); tA.setPreferredSize(new Dimension(prefWidth,
		 * (int) (rows)*height));
		 */
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		deckNameLabel.setText(currentDeck.toString());
		schonWeg = new HashSet<Integer>();
		current = nextRandom(currentDeck.getSize());
		text.setText(currentDeck.getQuestion(current));
	}

	@Override
	protected void propertiesToSettings() {
		if (customSettings == null) {
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(GuessSettingsDialog.NUM_OF_ROUNDS, "" + numOfRounds));
		String tolOn = customSettings.getProperty(GuessSettingsDialog.TOLERANZ_ON, "true");
		if (modus != Modus.SOLO) {
			toleranzOn = Boolean.parseBoolean(tolOn);
		}
		String toleranzPercent = customSettings.getProperty(GuessSettingsDialog.TOLERANZ_PERCENT, "95");
		toleranz = Integer.parseInt(toleranzPercent) / 100f;
		String deck = customSettings.getProperty(GuessSettingsDialog.DECK, "");
		if (deck.equals("Zufall")) {
			currentDeck = new GuessDeck(guessDecks.get(new Random().nextInt(guessDecks.size())));
		}
		for (int i = 0; i < guessDecks.size(); i++) {
			if (guessDecks.get(i).toString().equals(deck)) {
				currentDeck = new GuessDeck(guessDecks.get(i));
			}
		}
	}

	@Override
	public void start() {
		// Spielstart Code hier
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		guessDecks = DeckLoader.loadDecks(getGameFileName(), true);
	}
}
