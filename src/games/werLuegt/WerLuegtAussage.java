package games.werLuegt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WerLuegtAussage {
	private int current = 0;
	private boolean currentWahr = true;
	private String aussage;
	public String getAussage() {
		return aussage;
	}
	private List<String> wahreAntworten;
	public void addWahreAntwort(String antwort){
		wahreAntworten.add(antwort);
	}
	private List<String> falscheAntworten;
	public void addFalscheAntwort(String antwort){
		falscheAntworten.add(antwort);
	}
	private String info;
	private int wahrAnz;
	private int falschAnz;
	public String getInfo() {
		return info;
	}
	
	private Set<Integer> verbraucht = new HashSet<Integer>();
	private List<Integer> verlauf = new ArrayList<Integer>();
	public List<Integer> getVerlauf(){
		return verlauf;
	}
	
	public WerLuegtAussage(String aussage, List<String> wahreAntworten, List<String> falscheAntworten, String info){
		this.aussage = aussage;
		this.wahreAntworten = wahreAntworten;
		this.falscheAntworten = falscheAntworten;
		this.info = info;
		wahrAnz = wahreAntworten.size();
		falschAnz = falscheAntworten.size();
	}
	
	public WerLuegtAussage(String aussage, List<String> wahreAntworten, List<String> falscheAntworten){
		this(aussage,wahreAntworten,falscheAntworten,"Leider keine Info verfügbar");
	}
	
	public boolean hasMore(){
		return verbraucht.size() < (wahrAnz + falschAnz);
	}
	
	public String getNextAntwort(){
		String antwort = "";
		Random r = new Random();
		current = r.nextInt(wahrAnz+falschAnz);
		while(!verbraucht.add(current)){
			current = r.nextInt(wahrAnz+falschAnz);
		}
		verlauf.add(current);
		if(current < wahrAnz){
			antwort = wahreAntworten.get(current);
			currentWahr = true;
		}
		else{
			current -= wahrAnz;
			antwort = falscheAntworten.get(current);
			currentWahr = false;
		}
		return antwort;
	}
	
	public boolean isWahr(){
		return currentWahr;
	}
	
	public boolean isWahr(int id){
		return id<wahrAnz;
	}
	
	public String getAussage(int id){
		if(id<wahrAnz){
			return wahreAntworten.get(id);
		}
		else{
			id -= wahrAnz;
			return falscheAntworten.get(id);
		}
	}
}
