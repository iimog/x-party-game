package util;

import games.Modus;
import games.NonPC;
import games.PC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SpielListen {
	public final static int RANDOM_GAME = 0;	
	public final static int BAD6 = 1;			
	public final static int SAUTREIBER = 2;		
	public final static int GUESS = 3;			
	public final static int FEHLERTEUFEL = 4;	
	public final static int MEMORY = 5;			
	public final static int WO = 6;				
	public final static int UHR = 7;			
	public final static int GEISTER = 8;		
	public final static int MUENZSCHNIPP = 9;	
	public final static int EINBEIN = 10;		
	public final static int KRUG = 11;			
	public final static int MUNDFANG = 12;		
	public final static int MUENZWURF = 13;
	public final static int EISPRUNG = 14;	
	public final static int TRON = 15;
	public static final int WEITERGEBEN = 16;
	public static final int APFELWASSER = 17;
	public static final int WURST = 18;
	public static final int BUCHSTABENSALAT = 19;
	public static final int STIMMTS = 20;
	public static int WERLUEGT = 21;
	
	public static HashMap<Integer, String> getSpieleMap(){
		HashMap<Integer, String> spielMap = new HashMap<Integer, String>();
		spielMap.put(BAD6, "games.bad6.Bad6");
		spielMap.put(SAUTREIBER, "games.sautreiber.SauTreiber");
		spielMap.put(GUESS, "games.guess.Guess");
		spielMap.put(FEHLERTEUFEL, "games.difference.Difference");
		spielMap.put(MEMORY, "games.memory.Memory");
		spielMap.put(WO, "games.world.World");
		spielMap.put(UHR, "games.innereUhr.InnereUhr");
		spielMap.put(GEISTER, "games.ghosts.Ghosts");
		spielMap.put(MUENZSCHNIPP, "games.nonPC.Muenzschnipsen");
		spielMap.put(EINBEIN, "games.nonPC.EinBein");
		spielMap.put(KRUG, "games.nonPC.KrugHalten");
		spielMap.put(MUNDFANG, "games.nonPC.MundFangen");
		spielMap.put(MUENZWURF, "games.nonPC.MuenzWurf");
		spielMap.put(EISPRUNG, "games.nonPC.Eisprung");
		spielMap.put(TRON, "games.tron.Tron");
		spielMap.put(WEITERGEBEN, "games.nonPC.Weitergeben");
		spielMap.put(APFELWASSER, "games.nonPC.ApfelImWasser");
		spielMap.put(WURST, "games.wurst.Wurst");
		spielMap.put(BUCHSTABENSALAT, "games.buchstabensalat.BuchstabenSalat");
		spielMap.put(STIMMTS, "games.stimmts.Stimmts");
		spielMap.put(WERLUEGT, "games.werLuegt.WerLuegt");
		return spielMap;
	}
	public static List<Integer> moeglicheSpiele(Modus modus){
		List<Integer> spiele = Collections.synchronizedList(new ArrayList<Integer>());
		spiele.add(RANDOM_GAME);
		for(Object key: getSpieleMap().keySet()){
			if(Modus.moeglicheSpielIDs(modus).contains(key))
				spiele.add((Integer)key);
		}
		return spiele;
	}
	
	public static List<Integer> getPCSpieleIDs(Modus modus){
		List<Integer> spiele = moeglicheSpiele(modus);
		List<Integer> pcSpiele = Collections.synchronizedList(new ArrayList<Integer>());
		pcSpiele.add(RANDOM_GAME);
		HashMap<Integer, String> spieleMap = getSpieleMap();
		for(Integer i : spiele){
			if(i==RANDOM_GAME)continue;
			try {
				Class<?> c = Class.forName(spieleMap.get(i));
				PC pC = new PC(){};
				LOOP: while(c != null){
					for(Class<?> inter : c.getInterfaces()){
						if(inter.isInstance(pC)){
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
	public static List<Integer> getNonPCSpieleIDs(Modus modus){
		List<Integer> spiele = moeglicheSpiele(modus);
		List<Integer> nonPcSpiele = Collections.synchronizedList(new ArrayList<Integer>());
		nonPcSpiele.add(RANDOM_GAME);
		HashMap<Integer, String> spieleMap = getSpieleMap();
		for(Integer i : spiele){
			if(i==RANDOM_GAME)continue;
			try {
				Class<?> c = Class.forName(spieleMap.get(i));
				NonPC nonPC = new NonPC(){};
				LOOP: while(c != null){
					for(Class<?> inter : c.getInterfaces()){
						if(inter.isInstance(nonPC)){
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
	public static String getGameName(int gameID){
		if(gameID == RANDOM_GAME)return "Zufall";
		String name = "Name not found";
		try {
			Class<?> c = Class.forName(getSpieleMap().get(gameID));
			name = (String)c.getMethod("getGameName").invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return name;
	}
	public static List<String> getGameNames(Collection<Integer> gameIDs){
		List<String> namen = Collections.synchronizedList(new ArrayList<String>());
		for(Integer i: gameIDs){
			namen.add(getGameName(i));
		}
		return namen;
	}
	public static int getGameIDbyName(String name){
		int gameID = RANDOM_GAME;
		for(Integer i: getSpieleMap().keySet()){
			try {
				Class<?> c = Class.forName(getSpieleMap().get(i));
				String gameName = (String)c.getMethod("getGameName").invoke(null);
				if(name==gameName)return i;
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