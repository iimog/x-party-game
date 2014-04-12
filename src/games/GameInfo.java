package games;

public class GameInfo {
	private int ID;
	private String gameName;
	private String path;
	private String background;
	private String shortInfo;
	private int modi;
	private int defaultNumOfRounds;
	public GameInfo(int iD, String gameName, String path, int defaultNumOfRounds, int modi, String shortInfo, String background) {
		super();
		ID = iD;
		this.gameName = gameName;
		this.path = path;
		this.defaultNumOfRounds = defaultNumOfRounds;
		this.modi = modi;
		this.shortInfo = shortInfo;
		this.background = background;
}
	public int getID() {
		return ID;
	}
	public String getGameName() {
		return gameName;
	}
	public String getPath() {
		return path;
	}
	public String getBackground() {
		return background;
	}
	public boolean isPC() {
		return ID%2 == 0;
	}
	public boolean isSystem() {
		return ID%4 < 2;
	}
	public boolean isModus(Modus modus){
		if(modus == Modus.SOLO) return isSolo();
		if(modus == Modus.DUELL) return isDuell();
		if(modus == Modus.TRIPPLE) return isTripple();
		if(modus == Modus.VIERER) return isVierer();
		return isTeam();
	}
	public boolean isSolo(){
		return modi%2 == 1;
	}
	public boolean isDuell(){
		return (modi/2)%2 == 1;
	}
	public boolean isTripple(){
		return (modi/4)%2 == 1;
	}
	public boolean isVierer(){
		return (modi/8)%2 == 1;
	}
	public boolean isTeam(){
		return (modi/16)%2 == 1;
	}
	public String getShortInfo() {
		return shortInfo;
	}
	public int getDefaultNumOfRounds() {
		return defaultNumOfRounds;
	}
}
