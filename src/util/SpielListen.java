package util;

import games.GameInfo;
import games.Modus;
import games.NonPC;
import games.PC;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import start.X;

public class SpielListen {
	public final static int RANDOM_GAME = 0;

	public static HashMap<Integer, GameInfo> getSpieleMap() {
		HashMap<Integer, GameInfo> spielMap = new HashMap<Integer, GameInfo>();
		spielMap.putAll(loadSystemPCGames());
		spielMap.putAll(loadSystemNonPCGames());
		spielMap.putAll(loadUserNonPCGames());
		return spielMap;
	}

	private static Map<Integer, GameInfo> loadUserNonPCGames() {
		Map<Integer, GameInfo> userNonPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			FileReader fr = new FileReader(X.getDataDir()
					+ "/games/nonPC.games");
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				userNonPC.put(counter * 4 + 3,
						new GameInfo(counter * 4 + 3, elements[0], elements[1],
								false, false, Integer.parseInt(elements[2])));
				counter++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userNonPC;
	}

	private static Map<Integer, GameInfo> loadSystemNonPCGames() {
		Map<Integer, GameInfo> systemNonPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			FileReader fr = new FileReader(X.getMainDir()
					+ "/games/nonPC.games");
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				systemNonPC.put(counter * 4 + 1,
						new GameInfo(counter * 4 + 1, elements[0], elements[1],
								false, true, Integer.parseInt(elements[2])));
				counter++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return systemNonPC;
	}

	private static Map<Integer, GameInfo> loadSystemPCGames() {
		Map<Integer, GameInfo> systemPC = new HashMap<Integer, GameInfo>();
		int counter = 1;
		try {
			FileReader fr = new FileReader(X.getMainDir()
					+ "/games/pc.games");
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String[] elements = line.split("\t");
				systemPC.put(counter * 4,
						new GameInfo(counter * 4, elements[0], elements[1],
								true, true, Integer.parseInt(elements[2])));
				counter++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return systemPC;
	}

	public static List<Integer> moeglicheSpiele(Modus modus) {
		List<Integer> spiele = Collections
				.synchronizedList(new ArrayList<Integer>());
		spiele.add(RANDOM_GAME);
		for (Object key : getSpieleMap().keySet()) {
			if (Modus.moeglicheSpielIDs(modus).contains(key))
				spiele.add((Integer) key);
		}
		return spiele;
	}

	public static List<Integer> getPCSpieleIDs(Modus modus) {
		List<Integer> spiele = moeglicheSpiele(modus);
		List<Integer> pcSpiele = Collections
				.synchronizedList(new ArrayList<Integer>());
		pcSpiele.add(RANDOM_GAME);
		HashMap<Integer, String> spieleMap = getSpieleMap();
		for (Integer i : spiele) {
			if (i == RANDOM_GAME)
				continue;
			try {
				Class<?> c = Class.forName(spieleMap.get(i));
				PC pC = new PC() {
				};
				LOOP: while (c != null) {
					for (Class<?> inter : c.getInterfaces()) {
						if (inter.isInstance(pC)) {
							pcSpiele.add(i);
							break LOOP;
						}
					}
					c = c.getSuperclass();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return pcSpiele;
	}

	public static List<Integer> getNonPCSpieleIDs(Modus modus) {
		List<Integer> spiele = moeglicheSpiele(modus);
		List<Integer> nonPcSpiele = Collections
				.synchronizedList(new ArrayList<Integer>());
		nonPcSpiele.add(RANDOM_GAME);
		HashMap<Integer, String> spieleMap = getSpieleMap();
		for (Integer i : spiele) {
			if (i == RANDOM_GAME)
				continue;
			try {
				Class<?> c = Class.forName(spieleMap.get(i));
				NonPC nonPC = new NonPC() {
				};
				LOOP: while (c != null) {
					for (Class<?> inter : c.getInterfaces()) {
						if (inter.isInstance(nonPC)) {
							nonPcSpiele.add(i);
							break LOOP;
						}
					}
					c = c.getSuperclass();
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return nonPcSpiele;
	}

	public static String getGameName(int gameID) {
		if (gameID == RANDOM_GAME)
			return "Zufall";
		String name = "Name not found";
		try {
			Class<?> c = Class.forName(getSpieleMap().get(gameID));
			name = (String) c.getMethod("getGameName").invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
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
			try {
				Class<?> c = Class.forName(getSpieleMap().get(i));
				String gameName = (String) c.getMethod("getGameName").invoke(
						null);
				if (name == gameName)
					return i;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return gameID;
	}

	public static int getTotalGameNumber() {
		return getSpieleMap().size();
	}
}