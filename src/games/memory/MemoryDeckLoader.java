package games.memory;

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

public class MemoryDeckLoader {
	private static File systemFolder = new File(X.getMainDir() + "games/pc/memory/");
	private static File userFolder = new File(X.getDataDir() + "games/pc/memory/");

	public static List<MemoryDeck> loadMemoryDecks() {
		List<MemoryDeck> decks = new ArrayList<MemoryDeck>();
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
	
	public static Map<String, String> loadMemoryBacksides() {
		Map<String, String> backsides = new HashMap<String, String>();
		backsides.putAll(fileToBackside(new File(systemFolder, "backsides.txt"),true));
		Map<String, String> userBS = fileToBackside(new File(userFolder, "backsides.txt"),false);
		if(userBS != null)
			backsides.putAll(userBS);			
		return backsides;
	}

	private static Map<String, String> fileToBackside(File file, boolean system) {
		if(!file.exists())
			return null;
		Map<String, String> backsides = new HashMap<String, String>();
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				if(elements.length < 2) continue;
				String name = elements[0];
				String picture = elements[1];
				String prefix = X.getDataDir();
				if(system)
					prefix = X.getMainDir();
				backsides.put(name, prefix+picture);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: " + file
					+ " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return backsides;
	}
}
