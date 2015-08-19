package games.difference;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DifferenceDeck {
	private String deckName;
	private List<DifferencePicture> pictures;
	
	public DifferenceDeck(){
		pictures = new ArrayList<DifferencePicture>();
	}
	public String getDeckName() {
		return deckName;
	}
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	public List<DifferencePicture> getPictures() {
		return pictures;
	}
	public void addPicture(String picture, String picture2, Point coordinate) {
		this.addPicture(new DifferencePicture(picture, picture2, coordinate));
	}
	public void addPicture(DifferencePicture picture) {
		pictures.add(picture);
	}
	
	public static DifferenceDeck getFullDeck(List<DifferenceDeck> deckList){
		// Da sich dieses Memory Deck aus unterschiedlichen bereits fertigen Decks 
		// (mit absoluten Pfaden) zusammensetzt ist es egal ob system oder nicht.
		DifferenceDeck md = new DifferenceDeck();
		md.setDeckName("Alles");
		for(DifferenceDeck deck : deckList){
			for(DifferencePicture pic : deck.getPictures()){				
				md.addPicture(pic);
			}
		}
		return md;
	}
	
	public static DifferenceDeck getRandomDeck(List<DifferenceDeck> deckList){
		Random r = new Random();
		int next = r.nextInt(deckList.size());
		return deckList.get(next);
	}
	public int size() {
		return pictures.size();
	}
}
