package games.abbreviations;

import games.Deck;

import java.util.ArrayList;
import java.util.List;

public class AbbreviationsDeck extends Deck{
	private List<String> abbreviation;
	private List<String> full;
	
	public AbbreviationsDeck(Deck deck){
		super(deck);
		abbreviation = new ArrayList<String>(deck.getSize());
		full = new ArrayList<String>(deck.getSize());
		for(String el : this.getElements()){
			String[] parts = el.split("\t");
			abbreviation.add(parts[0]);
			full.add(parts[1]);
		}
	}
	
	public String getAbbreviation(int index){
		return abbreviation.get(index);
	}
	
	public String getFullWord(int index){
		return full.get(index);
	}
}
