package highscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHighscore {
	private List<GameHighscoreElement> elemente;
	private String gameName;
	
	public GameHighscore(String gameName){
		this.gameName = gameName;
		setElemente(Collections.synchronizedList(new ArrayList<GameHighscoreElement>()));
	}

	public List<GameHighscoreElement> getElemente() {
		return elemente;
	}

	public void setElemente(List<GameHighscoreElement> elemente) {
		this.elemente = elemente;
	}
	
	public void addElement(GameHighscoreElement element){
		elemente.add(element);
	}
	
	public GameHighscoreElement getElement(String name){
		GameHighscoreElement element = null;
		for(GameHighscoreElement e: elemente){
			if(e.getName().equals(name)){
				element = e;
			}
		}
		if(element == null){
			element = new GameHighscoreElement(name);
			elemente.add(element);
		}
		return element;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
