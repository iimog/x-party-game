package games.nonPC;

import games.Modus;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import player.Player;
import start.X;

public class NonPCGameFileHandler {
	public static final String NAME = "name";
	public static final String BACKGROUND = "background";
	public static final String SHORTINFO = "shortInfo";
	public static final String ANLEITUNG = "anleitung";
	public static final String NUMOFROUNDS = "numOfRounds";
	
	public static String gamesDir = X.getDataDir() + "games/nonPC";
	
	public static StandardNonPC loadGame(File gameDatei, Player[] players, Modus modus){
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(gameDatei);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String gameName = p.getProperty(NAME);
		String gameBackground = p.getProperty(BACKGROUND);
		String gameShortInfo = p.getProperty(SHORTINFO);
		String gameAnleitung = p.getProperty(ANLEITUNG);
		String gameNumOfRounds = p.getProperty(NUMOFROUNDS);
		int numOfRounds = Integer.parseInt(gameNumOfRounds);
		RawNonPC game = new RawNonPC(gameName, players, numOfRounds, modus, gameShortInfo, gameBackground);
		return game;
	}
}
