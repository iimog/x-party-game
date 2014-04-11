package games.reaktionszeit;

import java.util.ArrayList;
import java.util.List;

import start.X;

public class ReaktionsZeitDeck {
	private String deckName;
	private List<String> elements;
	private boolean system;
	public static final String STRING = "STRING";
	public static final String PICTURE = "PICTURE";
	
	private String deckType;
	
	public ReaktionsZeitDeck(boolean system){
		this.system = system;
		this.deckType = STRING;
		elements = new ArrayList<String>();
	}

	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	
	public String getDeckName(){
		return deckName;
	}

	public void addElement(String element) {
		elements.add(element);
	}
	
	public void relativeToAbsolute(){
		if(!deckType.equals(PICTURE))
			return;
		for(int i=0; i<elements.size(); i++){
			if(system)
				elements.set(i, elements.get(i));
			else
				elements.set(i, X.getDataDir() + elements.get(i));
		}
	}

	public void setDeckType(String deckType) {
		if(!deckType.equals(PICTURE) && !deckType.equals(STRING)){
			System.err.println("Type: "+deckType+" is not a valid deckType");
		}
		else{
			this.deckType = deckType;
		}
	}

	public String getDeckType() {
		return deckType;
	}

	public List<String> getElements() {
		return elements;
	}
	
	public String toString(){
		return deckName + " (" + deckType + ")";
	}
}
