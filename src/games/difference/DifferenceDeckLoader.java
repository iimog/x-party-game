package games.difference;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import start.X;
import util.ResourceList;
import util.ResourceURLFilter;

public class DifferenceDeckLoader {
	private static String systemRoot = "/conf/pc/difference/";
	private static File userFolder = new File(X.getDataDir() + "games/pc/difference/");

	public static List<DifferenceDeck> loadDifferenceDecks() {
		List<DifferenceDeck> decks = new ArrayList<DifferenceDeck>();
		
		try {
			Set<URL> systemURLs = ResourceList
					.getResourceURLs(new ResourceURLFilter() {
						public @Override
						boolean accept(URL u) {
							String s = u.getFile();
							return s.endsWith(".deck")
									&& s.contains(systemRoot);
						}
					});
			for (URL url : systemURLs) {
				decks.add(urlToDeck(url));
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	private static DifferenceDeck fileToDeck(String file, boolean system) {
		DifferenceDeck newDeck = null;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			newDeck = new DifferenceDeck();
			String deckName = br.readLine();
			newDeck.setDeckName(deckName);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				newDeck.addPicture(elements[0], elements[1], new Point(Integer.parseInt(elements[2]),Integer.parseInt(elements[3])));
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: " + file
					+ " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newDeck;
	}
	
	private static DifferenceDeck urlToDeck(URL url) {
		DifferenceDeck newDeck = null;
		try {
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			newDeck = new DifferenceDeck();
			String deckName = br.readLine();
			newDeck.setDeckName(deckName);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				newDeck.addPicture(elements[0], elements[1], new Point(Integer.parseInt(elements[2]),Integer.parseInt(elements[3])));
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newDeck;
	}
}