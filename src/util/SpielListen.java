package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games.GameInfo;
import games.Modus;
import start.X;

public class SpielListen {
	public final static int RANDOM_GAME = 0;
	private static HashMap<Integer, GameInfo> spieleMap;

	/**
	 * Gibt die HashMap mit globalen IDs und zugehöriger Spielinfo zurück
	 * Die Spiellisten werden dabei aus Dateien im MainDir und DataDir gelesen
	 * @return spieleMap
	 */
	public static HashMap<Integer, GameInfo> getSpieleMap() {
		if(spieleMap == null){
			spieleMap = new HashMap<Integer, GameInfo>();
			spieleMap.put(RANDOM_GAME, new GameInfo(0, "Zufallsspiel", "", 5, 31, "Ein zufällig ausgewähltes Spiel", "/media/ablauf/iceBG.jpg"));
			spieleMap.putAll(loadSystemPCGames());
			spieleMap.putAll(loadSystemNonPCGames());
			spieleMap.putAll(loadUserNonPCGames());
			spieleMap.putAll(loadUserPCGames());
		}
		return spieleMap;
	}

	/**
	 * Diese Methode lädt die nonPC Liste aus dem Benutzerverzeichnis
	 * @return userNonPC
	 */
	private static Map<Integer, GameInfo> loadUserNonPCGames() {
		Map<Integer, GameInfo> userNonPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			String file = X.getDataDir() + "games/nonPC.games";
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				userNonPC.put(counter * 4 + 3,
						new GameInfo(counter * 4 + 3, elements[0], elements[1],
								Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4], elements[5]));
				counter++;
			}
			br.close();
			isr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: "+X.getDataDir() + "games/nonPC.games nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userNonPC;
	}
	
	/**
	 * Diese Methode lädt die PC Liste aus dem Benutzerverzeichnis
	 * @return userPC
	 */
	private static Map<Integer, GameInfo> loadUserPCGames() {
		Map<Integer, GameInfo> userPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			FileReader fr = new FileReader(X.getDataDir() + "games/pc.games");
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				userPC.put(counter * 4 + 2,
						new GameInfo(counter * 4 + 2, elements[0], elements[1],
								Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4], elements[5]));
				counter++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: "+X.getDataDir() + "games/pc.games nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userPC;
	}

	/**
	 * Diese Methode lädt die nonPC Liste aus dem Installationsverzeichnis
	 * @return systemNonPC
	 */
	private static Map<Integer, GameInfo> loadSystemNonPCGames() {
		Map<Integer, GameInfo> systemNonPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			// TODO read file inside jar
			InputStreamReader fr = new InputStreamReader(SpielListen.class.getResourceAsStream("/conf/nonPC.games"),"UTF-8");
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				systemNonPC.put(counter * 4 + 1,
						new GameInfo(counter * 4 + 1, elements[0], elements[1],
								Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4], elements[5]));
				counter++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: /games/nonPC.games nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return systemNonPC;
	}

	/**
	 * Diese Methode lädt die PC Liste aus dem Installationsverzeichnis
	 * @return systemPC
	 */
	private static Map<Integer, GameInfo> loadSystemPCGames() {
		Map<Integer, GameInfo> systemPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			// TODO read file inside jar
			InputStreamReader fr = new InputStreamReader(SpielListen.class.getResourceAsStream("/conf/pc.games"),"UTF-8");
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				systemPC.put(counter * 4,
						new GameInfo(counter * 4, elements[0], elements[1],
								Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4], elements[5]));
				counter++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: /games/pc.games nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return systemPC;
	}

	/**
	 * Diese Methode filtert die Spielliste und gibt nur globale IDs zurück, die im 
	 * gegebenen Modus spielbar sind
	 * @param modus
	 * @return spiele
	 */
	public static List<Integer> moeglicheSpiele(Modus modus) {
		spieleMap = getSpieleMap();
		List<Integer> spiele = Collections
				.synchronizedList(new ArrayList<Integer>());
		for (Object key : spieleMap.keySet()) {
			if (spieleMap.get(key).isModus(modus))
				spiele.add((Integer) key);
		}
		return spiele;
	}

	/**
	 * Filtert die Liste der möglichen Spiele für modus weiter nach nur PC Spielen.
	 * @param modus
	 * @return pcSpiele
	 */
	public static List<Integer> getPCSpieleIDs(Modus modus) {
		List<Integer> spiele = moeglicheSpiele(modus);
		List<Integer> pcSpiele = Collections
				.synchronizedList(new ArrayList<Integer>());
		spieleMap = getSpieleMap();
		for (Integer i : spiele) {
			if (!spieleMap.containsKey(i))
				continue;
			if(spieleMap.get(i).isPC()){
				pcSpiele.add(i);
			}
		}
		return pcSpiele;
	}
	
	/**
	 * Filtert die Liste der möglichen Spiele für modus weiter nach nur nonPC Spielen.
	 * @param modus
	 * @return nonPcSpiele
	 */
	public static List<Integer> getNonPCSpieleIDs(Modus modus) {
		List<Integer> spiele = moeglicheSpiele(modus);
		List<Integer> nonPcSpiele = Collections
				.synchronizedList(new ArrayList<Integer>());
		spieleMap = getSpieleMap();
		for (Integer i : spiele) {
			if (!spieleMap.containsKey(i))
				continue;
			if(!spieleMap.get(i).isPC()){
				nonPcSpiele.add(i);
			}
		}
		return nonPcSpiele;
	}

	public static String getGameName(int gameID) {
		spieleMap = getSpieleMap();
		String name = "Name not found";
		if(spieleMap.containsKey(gameID)){
			name = spieleMap.get(gameID).getGameName();
		}
		return name;
	}

	public static List<String> getGameNames(Collection<Integer> gameIDs) {
		List<String> namen = Collections
				.synchronizedList(new ArrayList<String>());
		for (Integer i : gameIDs) {
			namen.add(getGameName(i));
		}
		return namen;
	}

	public static int getGameIDbyName(String name) {
		int gameID = RANDOM_GAME;
		for (Integer i : getSpieleMap().keySet()) {
			if(getSpieleMap().get(i).getGameName().equals(name)){
				return i;
			}
		}
		return gameID;
	}

	public static int getTotalGameNumber() {
		return getSpieleMap().size();
	}
}