package games.reaktionszeit;

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

public class ReaktionsZeitDeckLoader {
	// TODO systemFolder
	private static String systemRoot = "/conf/pc/reaktionszeit/";
	private static File userFolder = new File(X.getDataDir() + "games/pc/reaktionszeit/");

	public static List<ReaktionsZeitDeck> loadReaktionsZeitDecks() {
		List<ReaktionsZeitDeck> decks = new ArrayList<ReaktionsZeitDeck>();
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

	private static ReaktionsZeitDeck fileToDeck(String file, boolean system) {
		ReaktionsZeitDeck newDeck = null;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			newDeck = bufferedReaderToDeck(br, system);
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
	
	private static ReaktionsZeitDeck urlToDeck(URL url) {
		ReaktionsZeitDeck newDeck = null;
		try {
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			newDeck = bufferedReaderToDeck(br, true);
			br.close();
			is.close();
			newDeck.relativeToAbsolute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newDeck;
	}
	
	private static ReaktionsZeitDeck bufferedReaderToDeck(BufferedReader br, boolean system) throws IOException{
		ReaktionsZeitDeck newDeck = new ReaktionsZeitDeck(system);
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
