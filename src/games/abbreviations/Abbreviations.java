package games.abbreviations;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.dialogeGUIs.RoundDialog;
import gui.EasyDialog;
import gui.components.Bildschirm;
import gui.components.Countdown;
import gui.components.JButtonIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
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

public class Abbreviations extends Game {
	private static final long serialVersionUID = 1L;
	private JPanel hauptbereichPanel;
	private List<Deck> abbreviationsDecks;
	private AbbreviationsDeck currentDeck;
	private boolean inRound = false;
	private Bildschirm currentTargetPanel;
	private JLabel currentTargetLabel;
	private JPanel untenPanel;
	private JTextField antwortTextField;
	protected boolean durchEnterBeendet;
	private Countdown countdown;
	private int timeAfterBuzzer = 10;
	public int getTimeAfterBuzzer() {
		return timeAfterBuzzer;
	}

	public void setTimeAfterBuzzer(int timeAfterBuzzer) {
		this.timeAfterBuzzer = timeAfterBuzzer;
		if(countdown != null) {
			countdown.setSecs(timeAfterBuzzer);
		}
	}

	private String answer;
	private int currentIndex;
	private int whoBuzzered;
	private JButtonIcon skipButton;
	static Font standardFont = X.getStandardFont().deriveFont(40f);

	public List<Deck> getAbbreviationsDecks() {
		return abbreviationsDecks;
	}

	public Abbreviations(Player[] player, Modus modus, String background,
			int globalGameID) {
		super(player, 5, modus, background, globalGameID);
		initGUI();
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		currentDeck = new AbbreviationsDeck(abbreviationsDecks.get(new Random().nextInt(abbreviationsDecks.size())));
		currentTargetPanel = new Bildschirm("/media/abbreviations/nummernschild.png");
		currentTargetPanel.scalePic(300, 75);
		currentTargetPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 23, 7));
		hauptbereichPanel.add(currentTargetPanel, BorderLayout.CENTER);
		currentTargetLabel = new JLabel("Placeholder");
		currentTargetLabel.setFont(Abbreviations.standardFont.deriveFont(70f));
		currentTargetLabel.setForeground(Color.BLACK);
		currentTargetLabel.setHorizontalAlignment(JLabel.LEFT);
		currentTargetPanel.add(currentTargetLabel);
		addUntenPanel();
		settingsChanged();
	}
	
	private void addUntenPanel() {
		untenPanel = new JPanel();
		untenPanel.setLayout(new BorderLayout());
		hauptbereichPanel.add(untenPanel, BorderLayout.SOUTH);
		antwortTextField = new JTextField();
		antwortTextField.setFont(Abbreviations.standardFont);
		antwortTextField.setForeground(Color.BLACK);
		untenPanel.add(antwortTextField, BorderLayout.CENTER);
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
		skipButton = new JButtonIcon("/media/ablauf/skip.png","Überspringen");
		skipButton.setToolTipText("Diese Abkürzung überspringen");
		untenPanel.add(skipButton, BorderLayout.EAST);
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				nextRound();
			}
		});
	}
	
	public void roundEnd() {
		answer = antwortTextField.getText().trim();
		String[] fullWords = currentDeck.getFullWord(currentIndex).split("\\|");
		boolean wortErraten = false;
		for(String word : fullWords) {			
			if(word.equalsIgnoreCase(answer)) {
				wortErraten = true;
			}
		}
		winnerIs(whoBuzzered, !wortErraten);
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
				AbbreviationsSettingsDialog.NUM_OF_ROUNDS, "" + numOfRounds));
		String timeAfterBuzzerString = customSettings.getProperty(AbbreviationsSettingsDialog.TIME_AFTER_BUZZER);
		if (timeAfterBuzzerString != null)
			setTimeAfterBuzzer(Integer.parseInt(timeAfterBuzzerString));
		String deck = customSettings.getProperty(AbbreviationsSettingsDialog.DECK, "");
		for (int i = 0; i < abbreviationsDecks.size(); i++) {
			if (abbreviationsDecks.get(i).toString().equals(deck)) {
				currentDeck = new AbbreviationsDeck(abbreviationsDecks.get(i));
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
		playerLabel[whoBuzzered].setForeground(Color.WHITE);
		setBuzzerActive(true);
		if (modus == Modus.TEAM)
			changeActivePlayers();
		answer = "";
		unstoppable = false;
		durchEnterBeendet = false;
		antwortTextField.setEditable(false);
		currentIndex = nextRandom(currentDeck.getSize());
		if (currentIndex == -1) {
			EasyDialog
					.showMessage("Es wurden alle vorhandenen Nummernschilder in diesem Deck gespielt, das Spiel wird abgebrochen.");
		} else {
			currentTargetLabel.setText(currentDeck.getAbbreviation(currentIndex));
			antwortTextField.setText("");
			countdown.stop();
		}
	}

	@Override
	public void buzzeredBy(int whoBuzz) {
		if(!isBuzzerActive())
			return;
		setBuzzerActive(false);
		this.whoBuzzered = whoBuzz;
		unstoppable = true;
		antwortTextField.setEditable(true);
		antwortTextField.requestFocus();
		countdown.start();
		playAudioFile(myPlayer[whoBuzzered].sound);
		playerLabel[whoBuzzered].setForeground(myPlayer[whoBuzzered].farbe);
	}

	@Override
	public void openRoundDialog(String winner) {
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}

	@Override
	public void openSettingsDialog() {
		instance.showDialog(new AbbreviationsSettingsDialog(this));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new AbbreviationsDetailsDialog(this));
	}

	public String getDeckType() {
		return currentDeck.getDeckType();
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
		abbreviationsDecks = DeckLoader.loadDecks("abbreviations");
	}

	public String getCurrentAbbreviation() {
		return currentDeck.getAbbreviation(currentIndex);
	}
	
	public String getCurrentFullWord() {
		return currentDeck.getFullWord(currentIndex);
	}
	
	public String getAnswer() {
		return answer;
	}
}
