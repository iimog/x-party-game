package games.stimmts;

import java.util.ArrayList;
import java.util.List;

import games.Deck;

public class StimmtsDeck extends Deck{
	private List<StimmtsAussage> aussage;
	
	public StimmtsDeck(Deck deck){
		super(deck);
		aussage = new ArrayList<StimmtsAussage>(deck.getSize());
		for(String el : this.getElements()){
			try {
				String[] parts = el.split("\t");
				String question = parts[0];
				boolean answer = Boolean.parseBoolean(parts[1]);
				String info = parts.length>2 ? parts[2] : "Zu dieser Aussage gibt es leider keine weitere Info";
				aussage.add(new StimmtsAussage(question, answer, info));
			} catch (Exception e) {
				System.err.println("Problem parsing: "+el);
				e.printStackTrace();
			}
		}
	}
	
	public StimmtsAussage getAussage(int index){
		return aussage.get(index);
	}
}
