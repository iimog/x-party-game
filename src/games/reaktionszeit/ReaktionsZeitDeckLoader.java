package games.reaktionszeit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import start.X;

public class ReaktionsZeitDeckLoader {
	private static File systemFolder = new File(X.getMainDir() + "games/pc/reaktionszeit/");
	private static File userFolder = new File(X.getDataDir() + "games/pc/reaktionszeit/");

	public static List<ReaktionsZeitDeck> loadReaktionsZeitDecks() {
		List<ReaktionsZeitDeck> decks = new ArrayList<ReaktionsZeitDeck>();
		String[] systemFiles = systemFolder.list();
		for (String file : systemFiles) {
			if (file.endsWith(".deck")) {
				decks.add(fileToDeck(systemFolder+"/"+file, true));
			}
		}
		if(userFolder.exists()){
			String[] userFiles = userFolder.list();
			for (String file : userFiles) {
				if (file.endsWith(".deck")) {
					decks.add(fileToDeck(userFolder+"/"+file, false));
				}
			}
		}
		return decks;
	}

	private static ReaktionsZeitDeck fileToDeck(String file, boolean system) {
		ReaktionsZeitDeck newDeck = null;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			newDeck = new ReaktionsZeitDeck(system);
			String deckName = br.readLine();
			newDeck.setDeckName(deckName);
			String deckType = br.readLine();
			newDeck.setDeckType(deckType);
			while (br.ready()) {
				String element = br.readLine();
				newDeck.addElement(element);
			}
			br.close();
			fr.close();
			newDeck.relativeToAbsolute();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: " + file
					+ " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newDeck;
	}
}
