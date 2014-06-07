package games;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private boolean system;	
	private String deckName;
	private String deckType;
	private List<String> elements;
	
	public Deck(boolean system){
		this.system = system;
		elements = new ArrayList<String>();
	}
	
	public Deck(Deck deck){
		this.system = deck.isSystem();
		this.deckName = deck.getDeckName();
		this.deckType = deck.getDeckType();
		this.elements = deck.getElements();
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

	public void setDeckType(String deckType) {
		this.deckType = deckType;
	}

	public String getDeckType() {
		return deckType;
	}

	public List<String> getElements() {
		return elements;
	}
	
	public boolean isSystem(){
		return system;
	}
	
	public String toString(){
		if(deckType == null)
			return deckName;
		return deckName + " (" + deckType + ")";
	}
	
	public int getSize(){
		return elements.size();
	}
	
	public String getElement(int index){
		return elements.get(index);
	}
}
