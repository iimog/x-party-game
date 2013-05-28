package ablauf;

import games.Modus;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

import player.Player;
import player.PlayerFileHandler;
import player.Team;
import settings.Profile;
import settings.ProfileFileHandler;
import start.X;

// FIXME für verschiedene Modi einrichten
public class MatchFileHandler {
	public static final String DATUM = "Datum";
	public static final String PLAYER = "Player";
	public static final String MODUS = "Modus";
	public static final String GAMES = "Games";
	public static final String VERLAUF = "Verlauf";
	public static final String GESPIELT = "Gespielt";
	public static final String TEAMMITGLIEDER = "Team Mitglieder";
	public static final String TEAMFARBEN = "Teamfarben";
	public static final String TEAMSOUNDS = "Teamsounds";
	public static final String TEAMKEYS = "Teambuzzer";
	
	public static String matchDir = X.getDataDir() + "Matchs";
	public static File saveMatch(Ablauf ablauf){
		Date datum = new Date();
		String timestamp = getTimeStamp(datum);
		File datei = createDatei(timestamp);
		String profileFileName = datei.getName().replace(".match","");
		ProfileFileHandler.saveMatchProfile(profileFileName, ablauf.getProfile());
		Properties p = new Properties();
		p.setProperty(DATUM, datum.getTime()+"");
		String playerString = getPlayerAsString(ablauf.getPlayers());
		p.setProperty(PLAYER, playerString);
		p.setProperty(MODUS, ablauf.getModus().toString());
		p.setProperty(GAMES, ablauf.getGameList().toString());
		p.setProperty(VERLAUF, ablauf.getBisherigeErgebnisse().toString());
		p.setProperty(GESPIELT, ablauf.getPlayedGameIDs().toString());
		if(ablauf.getModus() == Modus.TEAM){
			Player[] team = ablauf.getPlayers();
			String teamString = getTeamsAsString(team);
			p.setProperty(TEAMMITGLIEDER, teamString);
			p.setProperty(TEAMFARBEN, team[0].farbe.getRGB()+","+team[1].farbe.getRGB());
			p.setProperty(TEAMSOUNDS, team[0].sound+","+team[1].sound);
			p.setProperty(TEAMKEYS, team[0].getKey()+","+team[1].getKey());
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
	private static String getTimeStamp(Date d) {
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		StringBuffer sb = new StringBuffer();
		sb.append(c.get(Calendar.DAY_OF_MONTH)+"-");
		sb.append((c.get(Calendar.MONTH)+1)+"-");
		sb.append(c.get(Calendar.YEAR));
		return sb.toString();
	}
	private static File createDatei(String timestamp) {
		int counter = 0;
		File directory = new File(matchDir);
		if(!directory.exists()){
			directory.mkdirs();
		}
		File datei;
		do{
			datei = new File(matchDir, timestamp+"-"+(counter++)+".match");
		}while(datei.exists());
		return datei;
	}
	private static String getPlayerAsString(Player[] players) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0; i<players.length; i++){
			sb.append(players[i].name+",");
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), "]");
		return sb.toString();
	}
	
	private static String getTeamsAsString(Player[] players) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<players.length; i++){
			Team t = (Team)players[i];
			for(int j=0; j<t.numOfMembers; j++){
				sb.append(t.members.get(j));
				if(j<t.numOfMembers-1)
					sb.append(",");
			}
			if(i<players.length-1)
				sb.append(";");
		}
		return sb.toString();
	}
	public static Spielablauf loadMatch(File matchDatei){
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(matchDatei);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Player[] player = rebuildPlayers(p.getProperty(PLAYER));
		Modus modus = rebuildModus(p.getProperty(MODUS));
		if(modus == Modus.TEAM){
			player = rebuildTeams(p.getProperty(PLAYER), p.getProperty(TEAMMITGLIEDER), p.getProperty(TEAMFARBEN),
					p.getProperty(TEAMSOUNDS), p.getProperty(TEAMKEYS));
		}
		ArrayList<Integer> gameList = rebuildList(p.getProperty(GAMES));
		ArrayList<Integer> bisherigerSpielverlauf = rebuildList(p.getProperty(VERLAUF));
		HashSet<Integer> schonGespielt = rebuildSet(p.getProperty(GESPIELT));
		Spielablauf sa = Spielablauf.createSpielablauf(player, gameList, modus);
		sa.setSpeicherDatei(matchDatei);
		sa.setBisherigeErgebnisse(bisherigerSpielverlauf);
		sa.setPlayedGameIDs(schonGespielt);
		Profile profile = ProfileFileHandler.loadMatchProfile(matchDatei);
		sa.setProfile(profile);
		return sa;
	}
	
	private static Modus rebuildModus(String property) {
		Modus modus = null;
		for(Modus m: Modus.values()){
			if(m.toString().equals(property)){
				modus = m;
				break;
			}
		}
		return modus;
	}
	private static Player[] rebuildPlayers(String playerString) {
		String players = playerString.substring(1, playerString.lastIndexOf("]"));
		StringTokenizer st = new StringTokenizer(players, ",");
		Player[] myPlayer = new Player[st.countTokens()];
		for(int i=0; i<myPlayer.length; i++){
			try {
				myPlayer[i] = PlayerFileHandler.loadPlayer(st.nextToken());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return myPlayer;
	}
	private static Team[] rebuildTeams(String teamNames, String teamMitglieder, String teamFarben, 
			String teamSounds, String teamKeys){
		Team[] myTeams = new Team[2];
		String teams = teamNames.substring(1, teamNames.lastIndexOf("]"));
		StringTokenizer st = new StringTokenizer(teams, ",");
		StringTokenizer st2 = new StringTokenizer(teamMitglieder, ";");
		StringTokenizer farben = new StringTokenizer(teamFarben, ",");
		StringTokenizer sounds = new StringTokenizer(teamSounds, ",");
		StringTokenizer keys = new StringTokenizer(teamKeys, ",");
		for(int i=0; i<2; i++){
			StringTokenizer teamMembers = new StringTokenizer(st2.nextToken(),",");
			Color c = new Color(Integer.parseInt(farben.nextToken()));
			int key = Integer.parseInt(keys.nextToken());
			myTeams[i] = new Team(st.nextToken(), true, c, key, sounds.nextToken());
			int mitgliederZahl = teamMembers.countTokens();
			for(int j=0; j<mitgliederZahl; j++){
				myTeams[i].addMember(teamMembers.nextToken());
			}
		}
		return myTeams;
	}
	private static ArrayList<Integer> rebuildList(String property) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String rohListe = property.substring(1, property.lastIndexOf("]"));
		StringTokenizer st = new StringTokenizer(rohListe, ",");
		while(st.hasMoreTokens()){
			String zahl = st.nextToken();
			zahl = zahl.trim();
			int echteZahl = Integer.valueOf(zahl);
			list.add(echteZahl);
		}
		return list;
	}
	private static HashSet<Integer> rebuildSet(String property) {
		HashSet<Integer> set = new HashSet<Integer>();
		if(property==null)return set;
		String rohListe = property.substring(1, property.lastIndexOf("]"));
		StringTokenizer st = new StringTokenizer(rohListe, ",");
		while(st.hasMoreTokens()){
			String zahl = st.nextToken();
			zahl = zahl.trim();
			int echteZahl = Integer.valueOf(zahl);
			set.add(echteZahl);
		}
		return set;
	}
	public static File overwriteMatch(File alteDatei, Ablauf ablauf){
		alteDatei.delete();
		File neueDatei = saveMatch(ablauf);
		ProfileFileHandler.renameProfile(alteDatei, neueDatei);
		return neueDatei;
	}
	
	public static void deleteMatchFile(File alteDatei){
		alteDatei.delete();
		ProfileFileHandler.deleteMatchProfile(alteDatei);
	}
	
	public static String getInfoForFile(File file) {
		StringBuffer info = new StringBuffer();
		String fileName = file.getName();
		String datum = fileName.substring(0, fileName.lastIndexOf("-"));
		datum = datum.replace('-', '.');
		info.append(datum);
		info.append(" - ");
		info.append(getModusAsString(file));
		info.append(" - ");
		String[] names = getPlayerNames(file);
		info.append(names[0]);
		for(int i=1; i<names.length; i++){
			info.append(" VS ");
			info.append(names[i]);
		}
		return info.toString();
	}
	
	public static MatchInfo getMatchInfoForFile(File file) {
		MatchInfo info = new MatchInfo(file);
		String fileName = file.getName();
		String datum = fileName.substring(0, fileName.lastIndexOf("-"));
		datum = datum.replace('-', '.');
		info.setDatum(datum);
		info.setModus(getModus(file));
		String[] names = getPlayerNames(file);
		info.setPlayer(names);
		info.setSpielstand(getSpielstand(file));
		info.setNumOfGames(getNumOfGames(file));
		return info;
	}
	
	private static String getModusAsString(File file) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String modus = p.getProperty(MODUS);
		return modus.toUpperCase();
	}
	
	private static Modus getModus(File file) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String modus = p.getProperty(MODUS);
		return Modus.getModus(modus);
	}
	
	private static int[] getSpielstand(File file){
		int[] spielstand = new int[4];
		spielstand[0]=0;spielstand[1]=0;spielstand[2]=0;spielstand[3]=0;
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Profile profile = ProfileFileHandler.loadMatchProfile(file);
		int pModus = profile.getPunkteModus();
		ArrayList<Integer> bisherigerSpielverlauf = rebuildList(p.getProperty(VERLAUF));
		int spielwert = 1;
		for(Integer i: bisherigerSpielverlauf){
			if(i==MatchCredits.UNENTSCHIEDEN){
				if(pModus == Profile.INCREASING){
					spielwert++;
				}
			}
			if(i<0 || i>3)continue;		// ungültiges Spiel
			else{
				spielstand[i] += spielwert;
				if(pModus == Profile.INCREASING){
					spielwert++;
				}
			}
		}		
		return spielstand;
	}
	
	private static int getNumOfGames(File f){
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(f);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Integer> spiele = rebuildList(p.getProperty(GAMES));
		return spiele.size();
	}
	
	public static String[] getPlayerNames(File f){
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(f);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String playerString = p.getProperty(PLAYER);
		String players = playerString.substring(1, playerString.lastIndexOf("]"));
		StringTokenizer st = new StringTokenizer(players, ",");
		String[] names = new String[st.countTokens()];
		for(int i=0; i<names.length; i++){
			names[i] = st.nextToken();
		}
		return names;
	}
}
