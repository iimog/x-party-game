package games.werLuegt;

import java.util.Map;

public class WerLuegtAussage {
	private String aussage;
	public String getAussage() {
		return aussage;
	}
	private Map<String, Boolean> antworten;
	private String info;
	public String getInfo() {
		return info;
	}
	
	public WerLuegtAussage(String aussage, Map<String, Boolean> antworten, String info){
		this.aussage = aussage;
		this.antworten = antworten;
		this.info = info;
	}
	
	public WerLuegtAussage(String aussage, Map<String, Boolean> antworten){
		this(aussage,antworten,"Leider keine Info verfügbar");
	}
	
	public Map<String, Boolean> getAntworten(){
		return antworten;
	}
}
