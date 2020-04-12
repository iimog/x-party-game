package games.buchstabensalat;

import java.util.ArrayList;
import java.util.List;

import games.Deck;

public class BuchstabenSalatDeck extends Deck{
	private List<String> word;
	private List<String> category;
	
	public BuchstabenSalatDeck(Deck deck){
		super(deck);
		word = new ArrayList<String>(deck.getSize());
		category = new ArrayList<String>(deck.getSize());
		for(String el : this.getElements()){
			String[] parts = el.split("\t");
			word.add(parts[0]);
			category.add(parts[1]);
		}
	}
	
	public String getWord(int index){
		return word.get(index);
	}
	
	public String getCategory(int index){
		return category.get(index);
	}
}
