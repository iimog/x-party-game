package gui.components.rotator;

import java.awt.BorderLayout;
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
	
	
	public StringRotator(File stringFile){
		super();
		strings = getStringsFromFile(stringFile);
		initGUI();
	}
	
	private void initGUI(){
		textLabel = new JLabel("bla");
		add(textLabel, BorderLayout.CENTER);
	}

	private List<String> getStringsFromFile(File file) {
		if(!file.exists())
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
			System.out.println("Datei: " + file
					+ " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return myStrings;
	}

	@Override
	public void changeComponent() {
		textLabel.setText(strings.get(nextRandom(strings.size())));
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
}