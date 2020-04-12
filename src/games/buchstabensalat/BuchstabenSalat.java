package games.buchstabensalat;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.RoundDialog;
import gui.EasyDialog;
import gui.components.Countdown;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import player.Player;
import start.X;
import util.ChangeManager;

public class BuchstabenSalat extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Buchstabensalat";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);

	private static int defaultNumOfRounds = 5;
	int timeAfterBuzzer = 10;

	public static String getGameName() {
		return gameName;
	}

	private JPanel hauptbereichPanel;

	private Countdown countdown;
	private JLabel kategorieLabel;
	private JPanel salatPanel;
	private BuchstabenGewirrPanel buchstabenGewirrPanel;
	private LoesungsWortPanel loesungsWortPanel;

	int current = -1;
	public long timePerLetter = 3000;
	private boolean durchEnterBeendet = false;
	private Aufdecker aufdecker;
	protected int whoBuzzered;
	private List<Deck> buchstabensalatDecks;
	BuchstabenSalatDeck currentDeck;

	public BuchstabenSalat(Player[] player, Modus modus, String background, int globalGameID) {
		super(player, defaultNumOfRounds, modus, background, globalGameID);
		currentDeck = new BuchstabenSalatDeck(buchstabensalatDecks.get(new Random().nextInt(buchstabensalatDecks.size())));
		initGUI();
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setBackground(Color.BLACK);
		spielBereichPanel.add(hauptbereichPanel);
		addKategorieLabel();
		addSalatPanel();
		addUntenPanel();
	}

	private void addKategorieLabel() {
		kategorieLabel = new JLabel("Kategorie");
		kategorieLabel.setForeground(Color.LIGHT_GRAY);
		kategorieLabel.setFont(X.BUTTON_FONT);
		kategorieLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(kategorieLabel, BorderLayout.NORTH);
	}

	@Override
	public void buzzeredBy(int whoBuzzered) {
		if(!isBuzzerActive())
			return;
		setBuzzerActive(false);
		this.whoBuzzered = whoBuzzered;
		unstoppable = true;
		aufdecker.interrupt();
		antwortTextField.setEditable(true);
		antwortTextField.requestFocus();
		countdown.start();
		playAudioFile(myPlayer[whoBuzzered].sound);
		playerLabel[whoBuzzered].setForeground(myPlayer[whoBuzzered].farbe);
	}

	private JPanel untenPanel;
	private JTextField antwortTextField;
	private String answer;
	boolean wortErraten = false;

	private void addSalatPanel() {
		salatPanel = new JPanel();
		salatPanel.setOpaque(false);
		salatPanel.setLayout(new GridLayout(2, 1));
		hauptbereichPanel.add(salatPanel, BorderLayout.CENTER);
		buchstabenGewirrPanel = new BuchstabenGewirrPanel(this);
		salatPanel.add(buchstabenGewirrPanel);
		loesungsWortPanel = new LoesungsWortPanel(this);
		salatPanel.add(loesungsWortPanel);
	}

	public void roundEnd() {
		answer = antwortTextField.getText().toUpperCase().trim();
		wortErraten = currentDeck.getWord(current).equalsIgnoreCase(answer);
		int winID;
		if (wortErraten) {
			winID = whoBuzzered;
		} else {
			winID = whoBuzzered * (-1) - 1;
		}
		verbuchePunkte(winID);
		winner = getWinnerText(winID);
		openRoundDialog(winner);
	}

	private String getWinnerText(int winID) {
		String winner = "";
		if (winID >= 0) {
			winner = myPlayer[winID].name;
		} else {
			int looseID = (winID + 1) * (-1);
			int c = 0;
			for (int i = 0; i < spielerZahl; i++) {
				if (i == looseID)
					continue;
				String trenner = "";
				if (c == spielerZahl - 4)
					trenner = ", ";
				if (c == spielerZahl - 3)
					trenner = " und ";
				winner += myPlayer[i].name + trenner;
				c++;
			}
		}
		return winner;
	}

	private void verbuchePunkte(int winID) {
		if (winID >= 0) {
			creds[winID].earnsCredit(1);
			myPlayer[winID].gameCredit++;
		} else {
			int looseID = (winID + 1) * (-1);
			for (int i = 0; i < spielerZahl; i++) {
				if (i == looseID)
					continue;
				creds[i].earnsCredit(1);
				myPlayer[i].gameCredit++;
			}
		}
	}

	private void addUntenPanel() {
		untenPanel = new JPanel();
		untenPanel.setLayout(new GridLayout(2, 1));
		hauptbereichPanel.add(untenPanel, BorderLayout.SOUTH);
		antwortTextField = new JTextField();
		antwortTextField.setFont(standardFont);
		antwortTextField.setForeground(Color.DARK_GRAY);
		antwortTextField.setBackground(Color.WHITE);
		untenPanel.add(antwortTextField);
		antwortTextField.setEditable(false);
		antwortTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				durchEnterBeendet = true;
				countdown.stop();
				roundEnd();
			}
		});
		countdown = new Countdown(timeAfterBuzzer);
		untenPanel.add(countdown, BorderLayout.SOUTH);
		countdown.addChangeManager(new ChangeManager() {
			@Override
			public void change() {
				if (!durchEnterBeendet)
					roundEnd();
			}
		});
	}

	private void nextRound() {
		setBuzzerActive(true);
		if (modus == Modus.TEAM)
			changeActivePlayers();
		playerLabel[whoBuzzered].setForeground(Color.WHITE);
		answer = "";
		unstoppable = false;
		wortErraten = false;
		durchEnterBeendet = false;
		antwortTextField.setEditable(false);
		aufgedeckteBuchstaben = 0;
		current = nextRandom(currentDeck.getSize());
		if (current == -1) {
			EasyDialog
					.showMessage("Es wurden alle vorhandenen Worte gespielt, das Spiel wird abgebrochen.");
			// TODO besser handhaben!
		} else {
			kategorieLabel.setText(currentDeck.getCategory(current));
			buchstabenGewirrPanel.setWord(currentDeck.getWord(current));
			antwortTextField.setText("");
			countdown.stop();
			loesungsWortPanel.setUnsichtbarWord(currentDeck.getWord(current));
			aufdecker = new Aufdecker();
			aufdecker.start();
		}
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
	}

	@Override
	protected void propertiesToSettings() {
		super.propertiesToSettings();
		if(customSettings == null){
			return;
		}
		String zeitNachBuzzer = customSettings.getProperty(BuchstabenSalatSettingsDialog.BUZZER_ZEIT, "10");
		timeAfterBuzzer = Integer.parseInt(zeitNachBuzzer);
		countdown.setSecs(timeAfterBuzzer);
		String zeitProBuchstabe = customSettings.getProperty(BuchstabenSalatSettingsDialog.BUCHSTABE_ZEIT, "3");
		timePerLetter = Integer.parseInt(zeitProBuchstabe)*1000;
	}

	@Override
	public void start() {
		nextRound();
	}

	int aufgedeckteBuchstaben = 0;

	class Aufdecker extends Thread {
		@Override
		public void run() {
			loesungsWortPanel.requestFocus();
			try {
				Thread.sleep(timePerLetter);
			} catch (Exception e) {
				return;
			}
			while (!isInterrupted()) {
				if (!(aufgedeckteBuchstaben < currentDeck.getWord(current).length() - 2)) {
					if (!isInterrupted())
						openRoundDialog("Keinen");
					return;
				}
				try {
					int position = buchstabenGewirrPanel
							.getPositionOf(aufgedeckteBuchstaben);
					String letter = buchstabenGewirrPanel
							.getLetterAt(aufgedeckteBuchstaben);
					buchstabenGewirrPanel.hideLetterAt(aufgedeckteBuchstaben);
					aufgedeckteBuchstaben++;
					loesungsWortPanel.setLetterAt(position, letter);
					loesungsWortPanel.requestFocus();
					Thread.sleep(timePerLetter);
				} catch (Exception e) {
					return;
				}
			}
		}
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

	public String getScatteredWord() {
		return buchstabenGewirrPanel.getScatteredWord();
	}

	public String getAnswer() {
		return answer;
	}

	@Override
	public void openSettingsDialog() {
		instance.showDialog(new BuchstabenSalatSettingsDialog(this));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new BuchstabenSalatDetailsDialog(this));
	}

	@Override
	public void abbruch() {
		countdown.stop();
		if(aufdecker != null)
			aufdecker.interrupt();
		super.abbruch();
	}

	@Override
	public void pause() {
		super.pause();
		if(aufdecker != null)
			aufdecker.interrupt();
	}

	@Override
	public void resume() {
		super.resume();
		aufdecker = new Aufdecker();
		aufdecker.start();
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		buchstabensalatDecks = DeckLoader.loadDecks("buchstabensalat", true);
	}
}
