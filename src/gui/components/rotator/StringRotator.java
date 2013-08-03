package gui.components.rotator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class StringRotator extends Rotator {
	private String deckName;
	private static final long serialVersionUID = 1L;
	List<String> strings;
	private JLabel textLabel;
	private int currentIndex;

	public StringRotator(File stringFile) {
		super();
		strings = getStringsFromFile(stringFile);
		initGUI();
	}

	private void initGUI() {
		textLabel = new JLabel("bla");
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		add(textLabel, BorderLayout.CENTER);
	}

	private List<String> getStringsFromFile(File file) {
		if (!file.exists())
			return null;
		List<String> myStrings = new ArrayList<String>();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			setDeckName(br.readLine());
			while (br.ready()) {
				String line = br.readLine();
				myStrings.add(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: " + file + " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return myStrings;
	}

	@Override
	public void changeComponent() {
		currentIndex = nextRandom(strings.size());
		textLabel.setText(strings.get(currentIndex));
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public String getDeckName() {
		return deckName;
	}

	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}

	@Override
	public void maskComponent() {
		textLabel.setText("");
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (textLabel != null)
			textLabel.setForeground(fg);
	}

	public void setFont(Font font) {
		super.setFont(font);
		if (textLabel != null)
			textLabel.setFont(font);
	}
	
	public List<String> getStringList(){
		return strings;
	}
}