package ablauf;

import games.Modus;

import java.io.File;

public class MatchInfo {
	private Modus modus;
	private String datum;
	private String[] player;
	private String[][] teamMembers;
	private int[] spielstand;
	private int numOfGames;
	private File file;
	public Modus getModus() {
		return modus;
	}
	public void setModus(Modus modus) {
		this.modus = modus;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public String[] getPlayer() {
		return player;
	}
	public void setPlayer(String[] player) {
		this.player = player;
	}
	public String[][] getTeamMembers() {
		return teamMembers;
	}
	public void setTeamMembers(String[][] teamMembers) {
		this.teamMembers = teamMembers;
	}
	public int[] getSpielstand() {
		return spielstand;
	}
	public void setSpielstand(int[] spielstand) {
		this.spielstand = spielstand;
	}
	public int getNumOfGames() {
		return numOfGames;
	}
	public void setNumOfGames(int spielZahl) {
		this.numOfGames = spielZahl;
	}
	
	public MatchInfo(File file){
		this.file = file;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
