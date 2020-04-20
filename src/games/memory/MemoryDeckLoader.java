package games.memory;

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
import java.util.List;
import java.util.Map;
import java.util.Set;

import start.X;
import util.ResourceList;
import util.ResourceURLFilter;

public class MemoryDeckLoader {
	private static String systemRoot = "/conf/pc/memory/";
	private static File userFolder = new File(X.getDataDir() + "games/pc/memory/");

	public static List<MemoryDeck> loadMemoryDecks() {
		List<MemoryDeck> decks = new ArrayList<MemoryDeck>();
		
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

	private static MemoryDeck fileToDeck(String file, boolean system) {
		MemoryDeck newDeck = null;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			newDeck = new MemoryDeck();
			String deckName = br.readLine();
			newDeck.setDeckName(deckName);
			while (br.ready()) {
				String picture = br.readLine();
				newDeck.addPicture(picture);
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
	
	private static MemoryDeck urlToDeck(URL url) {
		MemoryDeck newDeck = null;
		try {
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			newDeck = new MemoryDeck();
			String deckName = br.readLine();
			newDeck.setDeckName(deckName);
			while (br.ready()) {
				String picture = br.readLine();
				newDeck.addPicture(picture);
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newDeck;
	}
	
	public static Map<String, String> loadMemoryBacksides() {
		Map<String, String> backsides = new HashMap<String, String>();
		backsides.putAll(urlToBackside("/conf/pc/memory/backsides.txt",true));
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
					prefix = "";
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
	
	private static Map<String, String> urlToBackside(String url, boolean system) {
		Map<String, String> backsides = new HashMap<String, String>();
		
		try {
			InputStream is = X.class.getResourceAsStream(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				if(elements.length < 2) continue;
				String name = elements[0];
				String picture = elements[1];
				String prefix = X.getDataDir();
				if(system)
					prefix = "";
				backsides.put(name, prefix+picture);
			}
			br.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return backsides;
	}
}
