package games.guess;

import java.util.ArrayList;
import java.util.List;

import games.Deck;

public class GuessDeck extends Deck{
	private List<String> question;
	private List<Integer> answer;
	private List<String> info;
	
	public GuessDeck(Deck deck){
		super(deck);
		question = new ArrayList<String>(deck.getSize());
		answer = new ArrayList<Integer>(deck.getSize());
		info = new ArrayList<String>(deck.getSize());
		for(String el : this.getElements()){
			String[] parts = el.split("\t");
			question.add(parts[0]);
			answer.add(Integer.parseInt(parts[1]));
			info.add(parts[2]);
		}
	}
	
	public String getQuestion(int index){
		return question.get(index);
	}
	
	public int getAnswer(int index){
		return answer.get(index);
	}
	
	public String getInfo(int index){
		return info.get(index);
	}
}
