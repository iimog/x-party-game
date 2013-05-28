package highscore;

import games.Ergebnis;
import games.Modus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Properties;

import player.Player;
import start.X;

public class HighscoreFileHandler {
	private static String gameHighscoreDir = X.getDataDir() + "Highscore/Games";
	private static String highscoreDir = X.getDataDir() + "Highscore";
	
	public static File saveMatchHighscore(Modus modus, Player[] spieler, Player sieger){
		File datei = getMatchHighscoreDatei();
		MatchHighscore highscore;
		if(datei.exists()){
			highscore = loadMatchHighscore();
		}
		else{
			highscore = new MatchHighscore();
		}
		for(Player pl : spieler){
			if(!pl.isRobot())
				highscore.getElement(pl.getName()).addSpiel(modus);
		}
		if(!sieger.isRobot())
			highscore.getElement(sieger.getName()).addSieg(modus);
		Properties p = new Properties();
		for(GameHighscoreElement element: highscore.getElemente()){
			p.setProperty(element.getName(), element.getPropertyText());
		}
		try {
			FileOutputStream fos = new FileOutputStream(datei);
			p.storeToXML(fos, "Speichern erfolgreich");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return datei;
	}
	private static File getMatchHighscoreDatei() {
		File directory = new File(highscoreDir);
		if(!directory.exists()){
			directory.mkdirs();
		}
		File datei = new File(directory, "Match.mhs");
		return datei;
	}
	public static MatchHighscore loadMatchHighscore() {
		File highscoreDatei = getMatchHighscoreDatei();
		if(!highscoreDatei.exists())
			return new MatchHighscore();
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(highscoreDatei);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MatchHighscore matchHS = new MatchHighscore();
		for(Object key: p.keySet()){
			String k = (String)key;
			matchHS.addElement(new GameHighscoreElement(k, p.getProperty(k)));
		}
		return matchHS;
	}
	public static File saveGameHighscore(Ergebnis ergebnis){
		// PropertyFormat: Name = Spielername, 
		// Inhalt = Solospiele,Solosiege,Duellspiele,Duellsiege,Tripplespiele,Tripplesiege,Viererspiele,Vierersiege
		Modus modus = ergebnis.getModus();
		File datei = getGameHighscoreDatei(ergebnis.getGameName());
		GameHighscore highscore;
		if(datei.exists()){
			highscore = loadGameHighscore(datei);
		}
		else{
			highscore = new GameHighscore(ergebnis.getGameName());
		}
		for(Player pl : ergebnis.getPlayers()){
			if(!pl.isRobot())
				highscore.getElement(pl.getName()).addSpiel(modus);
		}
		Player winner = ergebnis.getWinner();
		if(!winner.isRobot())
			highscore.getElement(winner.getName()).addSieg(modus);
		Properties p = new Properties();
		for(GameHighscoreElement element: highscore.getElemente()){
			p.setProperty(element.getName(), element.getPropertyText());
		}
		try {
			FileOutputStream fos = new FileOutputStream(datei);
			p.storeToXML(fos, "Speichern erfolgreich");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return datei;
	}
	
	private static File getGameHighscoreDatei(String name) {
		File directory = new File(gameHighscoreDir);
		if(!directory.exists()){
			directory.mkdirs();
		}
		File datei = new File(directory, name+".ghs");
		return datei;
	}
	
	public static GameHighscore loadGameHighscore(String gameName){
		File datei = getGameHighscoreDatei(gameName);
		if(!datei.exists())return null;
		else return loadGameHighscore(datei);
	}
	public static GameHighscore loadGameHighscore(File f){
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(f);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String gameName = f.getName();
		gameName = gameName.substring(0, gameName.lastIndexOf('.'));
		GameHighscore gameHS = new GameHighscore(gameName);
		for(Object key: p.keySet()){
			String k = (String)key;
			gameHS.addElement(new GameHighscoreElement(k, p.getProperty(k)));
		}
		return gameHS;
	}
	
	public static File[] getGameHighscores(){
		File dir = new File(gameHighscoreDir);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".ghs")) return true;
				else return false;
			}
		};
		return dir.listFiles(filter);
	}
}
