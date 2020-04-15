package games.werLuegt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import games.Deck;
import start.X;

public class WerLuegtDeck extends Deck{
	private List<WerLuegtAussage> aussagen;
	
	public WerLuegtDeck(Deck deck){
		super(deck);
		aussagen = new ArrayList<WerLuegtAussage>(deck.getSize());
		for(String el : this.getElements()){
			WerLuegtAussage aussage = loadAussage(el);
			if(aussage != null) {
				aussagen.add(aussage);
			}
		}
	}
	
	@Override
	public int getSize() {
		if(aussagen == null) {
			return 0;
		}
		return aussagen.size();
	}
	
	public WerLuegtAussage getAussage(int index) {
		return aussagen.get(index);
	}
	
	public WerLuegtAussage loadAussage(String deckPath) {
		try {
			String systemRoot = "/conf/pc/werLuegt";
			File userFolder = new File(X.getDataDir() + "games/pc/werLuegt");
			String resourcePath = systemRoot+"/aussagen/"+deckPath;
			InputStream is = this.getClass().getResourceAsStream(resourcePath);
			BufferedReader br;
			if(is != null) {
				br = new BufferedReader(new InputStreamReader(is));
			} else {
				File aussageFile = new File(userFolder+"/aussagen/"+deckPath);
				if(aussageFile.canRead()) {
					br = new BufferedReader(new FileReader(aussageFile));
				} else {
					System.err.println("Aussage not found: " + deckPath);
					System.err.println("Tryed resource: " + resourcePath);
					System.err.println("Tryed file: " + aussageFile.getAbsolutePath());
					return null;
				}
			}
			String aussage = br.readLine();
			String info = br.readLine();
			HashMap<String, Boolean> correctAnswers = new HashMap<String, Boolean>();
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
			correctAnswers.put("", true);
			return new WerLuegtAussage(aussage, correctAnswers, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
