package games;

import static util.SpielListen.APFELWASSER;
import static util.SpielListen.BAD6;
import static util.SpielListen.BUCHSTABENSALAT;
import static util.SpielListen.EINBEIN;
import static util.SpielListen.EISPRUNG;
import static util.SpielListen.FEHLERTEUFEL;
import static util.SpielListen.GEISTER;
import static util.SpielListen.GUESS;
import static util.SpielListen.KRUG;
import static util.SpielListen.MEMORY;
import static util.SpielListen.MUNDFANG;
import static util.SpielListen.MUENZSCHNIPP;
import static util.SpielListen.MUENZWURF;
import static util.SpielListen.RANDOM_GAME;
import static util.SpielListen.SAUTREIBER;
import static util.SpielListen.TRON;
import static util.SpielListen.UHR;
import static util.SpielListen.WEITERGEBEN;
import static util.SpielListen.WO;
import static util.SpielListen.WURST;
import static util.SpielListen.STIMMTS;
import static util.SpielListen.WERLUEGT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Modus {
	SOLO, DUELL, TEAM, TRIPPLE, VIERER;
	
	public String toString(){
		String roh = super.toString();
		StringBuilder fertig = new StringBuilder(roh.substring(0, 1));
		fertig.append(roh.substring(1).toLowerCase());
		return fertig.toString();
	}
	
	public int getSpielerzahl(){
		if(this.equals(SOLO))
			return 1;
		else if(this.equals(DUELL) || this.equals(TEAM))
			return 2;
		else if(this.equals(TRIPPLE))
			return 3;
		else return 4;
	}
	
	public static List<Integer> moeglicheSpielIDs(Modus modus){
		List<Integer> moeglicheSpiele = Collections.synchronizedList(new ArrayList<Integer>());
		if(modus == Modus.DUELL){
			addDuellSpiele(moeglicheSpiele);
		}
		if(modus == Modus.TEAM){
			addTeamSpiele(moeglicheSpiele);
		}
		if(modus == Modus.SOLO){
			addSoloSpiele(moeglicheSpiele);
		}
		if(modus == Modus.TRIPPLE){
			addTrippleSpiele(moeglicheSpiele);
		}
		if(modus == Modus.VIERER){
			addViererSpiele(moeglicheSpiele);
		}
		return moeglicheSpiele;
	}

	private static void addViererSpiele(List<Integer> mS) {
		mS.add(RANDOM_GAME);
		mS.add(BAD6);
		mS.add(SAUTREIBER);
		mS.add(MUENZSCHNIPP);
		mS.add(EINBEIN);
		mS.add(KRUG);
		mS.add(MUNDFANG);
		mS.add(MUENZWURF);
		mS.add(EISPRUNG);
		mS.add(MEMORY);
		mS.add(WO);
		mS.add(FEHLERTEUFEL);
		mS.add(UHR);
		mS.add(GUESS);
		mS.add(TRON);
		mS.add(WURST);
		mS.add(BUCHSTABENSALAT);
		mS.add(STIMMTS);
		mS.add(WERLUEGT);
	}

	private static void addTrippleSpiele(List<Integer> mS) {
		mS.add(RANDOM_GAME);
		mS.add(BAD6);
		mS.add(SAUTREIBER);
		mS.add(MUENZSCHNIPP);
		mS.add(EINBEIN);
		mS.add(KRUG);
		mS.add(MUNDFANG);
		mS.add(MUENZWURF);
		mS.add(EISPRUNG);
		mS.add(MEMORY);
		mS.add(WO);
		mS.add(FEHLERTEUFEL);
		mS.add(UHR);
		mS.add(GUESS);
		mS.add(TRON);
		mS.add(WURST);
		mS.add(BUCHSTABENSALAT);
		mS.add(STIMMTS);
		mS.add(WERLUEGT);
	}

	private static void addSoloSpiele(List<Integer> mS) {
		mS.add(RANDOM_GAME);
		mS.add(BAD6);
		mS.add(MEMORY);
		mS.add(WO);
		mS.add(FEHLERTEUFEL);
		mS.add(UHR);
		mS.add(GUESS);
		mS.add(WURST);
		mS.add(STIMMTS);
		mS.add(WERLUEGT);
	}

	private static void addDuellSpiele(List<Integer> mS) {
		mS.add(RANDOM_GAME);
		mS.add(BAD6);
		mS.add(SAUTREIBER);
		mS.add(GUESS);
		mS.add(FEHLERTEUFEL);
		mS.add(MEMORY);
		mS.add(WO);
		mS.add(UHR);
		mS.add(GEISTER);
		mS.add(MUENZSCHNIPP);
		mS.add(EINBEIN);
		mS.add(KRUG);
		mS.add(MUNDFANG);
		mS.add(MUENZWURF);
		mS.add(EISPRUNG);		
		mS.add(TRON);
		mS.add(WURST);
		mS.add(BUCHSTABENSALAT);
		mS.add(STIMMTS);
		mS.add(WERLUEGT);
	}

	private static void addTeamSpiele(List<Integer> mS) {
		mS.add(RANDOM_GAME);
		mS.add(BAD6);
		mS.add(SAUTREIBER);
		mS.add(GUESS);
		mS.add(FEHLERTEUFEL);
		mS.add(MEMORY);
		mS.add(WO);
		mS.add(UHR);
		mS.add(GEISTER);
		mS.add(MUENZSCHNIPP);
		mS.add(EINBEIN);
		mS.add(KRUG);
		mS.add(MUNDFANG);
		mS.add(MUENZWURF);
		mS.add(EISPRUNG);		
		mS.add(TRON);
		mS.add(WEITERGEBEN);
		mS.add(APFELWASSER);
		mS.add(WURST);
		mS.add(BUCHSTABENSALAT);
		mS.add(STIMMTS);
		mS.add(WERLUEGT);
	}
	
	public static Modus getModus(String modus){
		modus = modus.toLowerCase();
		if(modus.equals("solo"))return Modus.SOLO;
		if(modus.equals("team"))return Modus.TEAM;
		if(modus.equals("tripple"))return Modus.TRIPPLE;
		if(modus.equals("vierer"))return Modus.VIERER;
		else return Modus.DUELL;
	}
}
