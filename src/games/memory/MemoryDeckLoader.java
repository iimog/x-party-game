package games.memory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import start.X;

public class MemoryDeckLoader {
	public static List<MemoryDeck> loadMemoryDecks() {
		List<MemoryDeck> decks = new ArrayList<MemoryDeck>();
		File systemFolder = new File(X.getMainDir() + "games/pc/memory/");
		File userFolder = new File(X.getDataDir() + "games/pc/memory/");
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

	private static MemoryDeck fileToDeck(String file, boolean system) {
		MemoryDeck newDeck = null;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			newDeck = new MemoryDeck(system);
			String deckName = br.readLine();
			newDeck.setDeckName(deckName);
			while (br.ready()) {
				String picture = br.readLine();
				newDeck.addPicture(picture);
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
