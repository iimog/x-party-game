package games.werLuegt;

import games.Game;
import gui.components.rotator.Rotator;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

public class WerLuegtRotator extends Rotator {
	private static final long serialVersionUID = 1L;
	private Map<String, Boolean> correctAnswers;

	public Map<String, Boolean> getCorrectAnswers() {
		return correctAnswers;
	}

	private String deckInfo;
	private String deckName;
	private String currentAussage;
	private JLabel aussageLabel;
	private List<String> verlauf = new ArrayList<String>();

	public List<String> getVerlauf() {
		return verlauf;
	}

	public WerLuegtRotator() {
		super();
		initGUI();
	}

	public WerLuegtRotator(File werLuegtFile) {
		this();
		changeDeckToFile(werLuegtFile);
	}

	public void changeDeckToFile(File werLuegtFile) {
		resetVars();
		try {
			FileReader fr = new FileReader(werLuegtFile);
			BufferedReader br = new BufferedReader(fr);
			getAussagenFromBufferedReader(br);
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: " + werLuegtFile + " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void resetVars() {
		correctAnswers = new HashMap<String, Boolean>();
		schonWeg = new HashSet<Integer>();
		aussageLabel.setText(" ");
		verlauf = new ArrayList<String>();
	}

	public void changeDeckToResource(URL url) {
		resetVars();
		try {
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			getAussagenFromBufferedReader(br);
			br.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initGUI() {
		aussageLabel = new JLabel("blub blub");
		aussageLabel.setFont(Game.STANDARD_FONT.deriveFont(30f));
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		add(aussageLabel, BorderLayout.CENTER);
	}

	private void getAussagenFromBufferedReader(BufferedReader br) {
		try {
			setDeckName(br.readLine());
			deckInfo = br.readLine();
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				if (elements.length < 2)
					continue;
				String statement = elements[0];
				String correctness = elements[1];

				correctAnswers
						.put(statement, Boolean.parseBoolean(correctness));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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