package games.nonPC;

import games.GameInfo;
import games.Modus;
import player.Player;
import start.X;
import util.SpielListen;

public class NonPCGameFileHandler {
	public static final String NAME = "name";
	public static final String BACKGROUND = "background";
	public static final String ANLEITUNG = "anleitung";
	public static final String NUMOFROUNDS = "numOfRounds";
	
	public static String gamesDir = X.getDataDir() + "games/nonPC";
	
	public static StandardNonPC loadGame(int globalGameID, Player[] players, Modus modus){
		GameInfo gi = SpielListen.getSpieleMap().get(globalGameID);
		String fileName = gi.getPath();
		String prefix = (gi.isSystem() ? X.getMainDir() : X.getDataDir());
		/*
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(prefix+fileName);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String gameBackground = prefix+p.getProperty(BACKGROUND);
		String gameNumOfRounds = p.getProperty(NUMOFROUNDS);
		*/
		int numOfRounds = gi.getDefaultNumOfRounds();
		String background = (prefix + fileName).replaceAll(".game$", ".png");
		RawNonPC game = new RawNonPC(players, numOfRounds, modus, background, globalGameID);
		return game;
	}
}
