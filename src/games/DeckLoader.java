package games;

import games.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import start.X;
import util.ResourceList;
import util.ResourceURLFilter;

public class DeckLoader {
	public static List<Deck> loadDecks(String game, boolean addAllDummy) {
		List<Deck> decks = new ArrayList<Deck>();
		final String systemRoot = "/conf/pc/"+game+"/";
		File userFolder = new File(X.getDataDir() + "games/pc/"+game+"/");
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
		
		if(addAllDummy) {
			Deck allDummy = new Deck(false);
			allDummy.setDeckName("Alles");
			Set<String> allElements = new HashSet<String>();
			for(Deck d : decks) {
				allElements.addAll(d.getElements());
			}
			for(String e : allElements) {
				allDummy.addElement(e);
			}
			decks.add(allDummy);
		}
		return decks;
	}

	private static Deck fileToDeck(String file, boolean system) {
		Deck newDeck = null;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			newDeck = bufferedReaderToDeck(br, system);
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
	
	private static Deck urlToDeck(URL url) {
		Deck newDeck = null;
		try {
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			newDeck = bufferedReaderToDeck(br, true);
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newDeck;
	}
	
	private static Deck bufferedReaderToDeck(BufferedReader br, boolean system) throws IOException{
		Deck newDeck = new Deck(system);
		String deckName = br.readLine();
		newDeck.setDeckName(deckName);
		String deckType = br.readLine();
		newDeck.setDeckType(deckType);
		while (br.ready()) {
			String element = br.readLine();
			newDeck.addElement(element);
		}
		return newDeck;
	}
}
