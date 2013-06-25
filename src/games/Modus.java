package games;


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
	
	public static Modus getModus(String modus){
		modus = modus.toLowerCase();
		if(modus.equals("solo"))return Modus.SOLO;
		if(modus.equals("team"))return Modus.TEAM;
		if(modus.equals("tripple"))return Modus.TRIPPLE;
		if(modus.equals("vierer"))return Modus.VIERER;
		else return Modus.DUELL;
	}
}
