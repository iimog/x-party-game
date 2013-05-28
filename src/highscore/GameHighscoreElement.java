package highscore;

import games.Modus;

import java.util.StringTokenizer;

public class GameHighscoreElement {
	private String name;
	private int soloSpiele;
	private int soloSiege;
	private int duellSpiele;
	private int duellSiege;
	private int trippleSpiele;
	private int trippleSiege;
	private int viererSpiele;
	private int viererSiege;
	
	public GameHighscoreElement(String name){
		this(name, "0,0,0,0,0,0,0,0");
	}
	public GameHighscoreElement(String name, String propertyText){
		this.name = name;
		StringTokenizer st = new StringTokenizer(propertyText, ",");
		soloSpiele = Integer.parseInt(st.nextToken());
		soloSiege = Integer.parseInt(st.nextToken());
		duellSpiele = Integer.parseInt(st.nextToken());
		duellSiege = Integer.parseInt(st.nextToken());
		trippleSpiele = Integer.parseInt(st.nextToken());
		trippleSiege = Integer.parseInt(st.nextToken());
		viererSpiele = Integer.parseInt(st.nextToken());
		viererSiege = Integer.parseInt(st.nextToken());
	}
	public String getPropertyText(){
		StringBuffer sb = new StringBuffer();
		sb.append(soloSpiele+",");
		sb.append(soloSiege+",");
		sb.append(duellSpiele+",");
		sb.append(duellSiege+",");
		sb.append(trippleSpiele+",");
		sb.append(trippleSiege+",");
		sb.append(viererSpiele+",");
		sb.append(viererSiege+",");
		return sb.toString();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSoloSpiele() {
		return soloSpiele;
	}
	public void setSoloSpiele(int soloSpiele) {
		this.soloSpiele = soloSpiele;
	}
	public int getSoloSiege() {
		return soloSiege;
	}
	public void setSoloSiege(int soloSiege) {
		this.soloSiege = soloSiege;
	}
	public int getDuellSpiele() {
		return duellSpiele;
	}
	public void setDuellSpiele(int duellSpiele) {
		this.duellSpiele = duellSpiele;
	}
	public int getDuellSiege() {
		return duellSiege;
	}
	public void setDuellSiege(int duellSiege) {
		this.duellSiege = duellSiege;
	}
	public int getTrippleSpiele() {
		return trippleSpiele;
	}
	public void setTrippleSpiele(int trippleSpiele) {
		this.trippleSpiele = trippleSpiele;
	}
	public int getTrippleSiege() {
		return trippleSiege;
	}
	public void setTrippleSiege(int trippleSiege) {
		this.trippleSiege = trippleSiege;
	}
	public int getViererSpiele() {
		return viererSpiele;
	}
	public void setViererSpiele(int viererSpiele) {
		this.viererSpiele = viererSpiele;
	}
	public int getViererSiege() {
		return viererSiege;
	}
	public void setViererSiege(int viererSiege) {
		this.viererSiege = viererSiege;
	}
	
	public void addSpiel(Modus modus){
		if(modus == Modus.SOLO) soloSpiele++;
		if(modus == Modus.DUELL) duellSpiele++;
		if(modus == Modus.TRIPPLE) trippleSpiele++;
		if(modus == Modus.VIERER) viererSpiele++;
	}
	public void addSieg(Modus modus){
		if(modus == Modus.SOLO) soloSiege++;
		if(modus == Modus.DUELL) duellSiege++;
		if(modus == Modus.TRIPPLE) trippleSiege++;
		if(modus == Modus.VIERER) viererSiege++;
	}
	
	public int getSpiele(Modus modus){
		if(modus == Modus.SOLO) return soloSpiele;
		if(modus == Modus.DUELL) return duellSpiele;
		if(modus == Modus.TRIPPLE) return trippleSpiele;
		if(modus == Modus.VIERER) return viererSpiele;
		else return 0;
	}
	public int getSiege(Modus modus){
		if(modus == Modus.SOLO) return soloSiege;
		if(modus == Modus.DUELL) return duellSiege;
		if(modus == Modus.TRIPPLE) return trippleSiege;
		if(modus == Modus.VIERER) return viererSiege;
		else return 0;
	}
	public double getSiegeInProzent(Modus modus){
		if(modus == Modus.SOLO) return (double)soloSiege/(double)soloSpiele;
		if(modus == Modus.DUELL) return (double)duellSiege/(double)duellSpiele;
		if(modus == Modus.TRIPPLE) return (double)trippleSiege/(double)trippleSpiele;
		if(modus == Modus.VIERER) return (double)viererSiege/(double)viererSpiele;
		else return 0;
	}
}
