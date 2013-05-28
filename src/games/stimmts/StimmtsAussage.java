package games.stimmts;

public class StimmtsAussage {
	private String aussage;
	public String getAussage() {
		return aussage;
	}
	private boolean wahr;
	public boolean isWahr() {
		return wahr;
	}
	private String info;
	public String getInfo() {
		return info;
	}
	public StimmtsAussage(String aussage, boolean wahr, String info){
		this.aussage = aussage;
		this.wahr = wahr;
		this.info = info;
	}
	public StimmtsAussage(String aussage, boolean wahr){
		this(aussage,wahr,"Leider keine Info verfügbar");
	}
}
