package games.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryDeck {
	private String deckName;
	private List<String> pictures;
	
	public MemoryDeck(){
		pictures = new ArrayList<String>();
	}
	public String getDeckName() {
		return deckName;
	}
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	public List<String> getPictures() {
		return pictures;
	}
	public void addPicture(String picture) {
		pictures.add(picture);
	}
	
	public static MemoryDeck getFullDeck(List<MemoryDeck> deckList){
		// Da sich dieses Memory Deck aus unterschiedlichen bereits fertigen Decks 
		// (mit absoluten Pfaden) zusammensetzt ist es egal ob system oder nicht.
		MemoryDeck md = new MemoryDeck();
		md.setDeckName("Alles");
		for(MemoryDeck deck : deckList){
			for(String pic : deck.getPictures()){				
				md.addPicture(pic);
			}
		}
		return md;
	}
	
	public static MemoryDeck getRandomDeck(List<MemoryDeck> deckList){
		Random r = new Random();
		int next = r.nextInt(deckList.size());
		return deckList.get(next);
	}
	public int size() {
		return pictures.size();
	}
}
