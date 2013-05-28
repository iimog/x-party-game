package settings;

import games.Modus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Profile{
	private String name;
	private List<Integer> soloSpielliste;
	private List<Integer> duellSpielliste;
	private List<Integer> trippleSpielliste;
	private List<Integer> viererSpielliste;
	private List<Integer> teamSpielliste;
	private int punkteModus;
	public static final int INCREASING = 1;
	public static final int CONSTANT = 0;
	
	public Profile(){
		soloSpielliste = Collections.synchronizedList(new ArrayList<Integer>());
		duellSpielliste = Collections.synchronizedList(new ArrayList<Integer>());
		trippleSpielliste = Collections.synchronizedList(new ArrayList<Integer>());
		viererSpielliste = Collections.synchronizedList(new ArrayList<Integer>());
		teamSpielliste = Collections.synchronizedList(new ArrayList<Integer>());
		punkteModus = INCREASING;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public List<Integer> getSpielliste(Modus modus) {
		List<Integer> spielliste = soloSpielliste;
		if(modus == Modus.DUELL) spielliste = duellSpielliste;
		if(modus == Modus.TRIPPLE) spielliste = trippleSpielliste;
		if(modus == Modus.VIERER) spielliste = viererSpielliste;
		if(modus == Modus.TEAM) spielliste = teamSpielliste;
		return spielliste;
	}
	
	public void setSpielliste(List<Integer> spielliste, Modus modus) {
		if(modus == Modus.SOLO) soloSpielliste = spielliste;
		if(modus == Modus.DUELL) duellSpielliste = spielliste;
		if(modus == Modus.TRIPPLE) trippleSpielliste = spielliste;
		if(modus == Modus.VIERER) viererSpielliste = spielliste;
		if(modus == Modus.TEAM) teamSpielliste = spielliste;
	}
	
	public File saveProfileToFile(){
		return ProfileFileHandler.saveProfile(this);
	}

	public static Profile getDefaultProfile(){
		Profile p = new Profile();
		p.setName("Standard");
		p.setSpielliste(Modus.moeglicheSpielIDs(Modus.SOLO), Modus.SOLO);
		p.setSpielliste(Modus.moeglicheSpielIDs(Modus.DUELL), Modus.DUELL);
		p.setSpielliste(Modus.moeglicheSpielIDs(Modus.TRIPPLE), Modus.TRIPPLE);
		p.setSpielliste(Modus.moeglicheSpielIDs(Modus.VIERER), Modus.VIERER);
		p.setSpielliste(Modus.moeglicheSpielIDs(Modus.TEAM), Modus.TEAM);
		return p;
	}

	public void setPunkteModus(int punkteModus) {
		if(punkteModus == INCREASING || punkteModus == CONSTANT){
			this.punkteModus = punkteModus;
		}
	}

	public int getPunkteModus() {
		return punkteModus;
	}
}
