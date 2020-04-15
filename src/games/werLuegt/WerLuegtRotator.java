package games.werLuegt;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import games.Game;
import gui.components.rotator.Rotator;

public class WerLuegtRotator extends Rotator {
	private static final long serialVersionUID = 1L;
	private Map<String, Boolean> correctAnswers;

	public Map<String, Boolean> getCorrectAnswers() {
		return correctAnswers;
	}

	private String deckInfo;
	private String deckName;
	private String currentAussage = "";
	private JLabel aussageLabel;
	private List<String> verlauf = new ArrayList<String>();

	public List<String> getVerlauf() {
		return verlauf;
	}

	public WerLuegtRotator() {
		super();
		initGUI();
	}

	public WerLuegtRotator(WerLuegtAussage aussage) {
		this();
		changeDeckToAussage(aussage);
	}

	private void resetVars() {
		correctAnswers = new HashMap<String, Boolean>();
		schonWeg = new HashSet<Integer>();
		aussageLabel.setText(" ");
		verlauf = new ArrayList<String>();
	}

	private void initGUI() {
		aussageLabel = new JLabel("blub blub");
		aussageLabel.setFont(Game.STANDARD_FONT.deriveFont(30f));
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		add(aussageLabel, BorderLayout.CENTER);
	}
	
	public void changeDeckToAussage(WerLuegtAussage aussage) {
		resetVars();
		setDeckName(aussage.getAussage());
		deckInfo = aussage.getInfo();
		correctAnswers = aussage.getAntworten();
		correctAnswers.put("", true);
	}

	public String getDeckInfo() {
		return deckInfo;
	}

	public boolean isCurrentTrue() {
		return correctAnswers.get(currentAussage);
	}

	@Override
	public void changeComponent() {
		int nextIndex = nextRandom(correctAnswers.size());
		currentAussage = (String) correctAnswers.keySet().toArray()[nextIndex];
		aussageLabel.setText(currentAussage);
		verlauf.add(currentAussage);
		validate();
		repaint();
	}

	public String getDeckName() {
		return deckName;
	}

	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}

	@Override
	public void maskComponent() {
		aussageLabel.setText("");
	}

	public String getCurrentAntwort() {
		return currentAussage;
	}
}