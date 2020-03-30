package games.world;

import games.Deck;

import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

public class WorldDeck extends Deck{
	private List<String> question;
	private List<GeoPosition> answer;
	private List<String> info;
	
	public WorldDeck(Deck deck){
		super(deck);
		question = new ArrayList<String>(deck.getSize());
		answer = new ArrayList<GeoPosition>(deck.getSize());
		info = new ArrayList<String>(deck.getSize());
		for(String el : this.getElements()){
			String[] parts = el.split("\t");
			question.add(parts[0]);
			answer.add(new GeoPosition(Double.parseDouble(parts[1]),Double.parseDouble(parts[2])));
			info.add(parts[3]);
		}
	}
	
	public String getQuestion(int index){
		return question.get(index);
	}
	
	public GeoPosition getAnswer(int index) {
		return answer.get(index);
	}
	
	public String getInfo(int index){
		return info.get(index);
	}
}
