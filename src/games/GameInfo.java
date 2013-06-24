package games;

public class GameInfo {
	private int ID;
	private String gameName;
	private String path;
	private boolean PC;
	private boolean system;
	private int modi;
	public GameInfo(int iD, String gameName, String path, boolean pC,
			boolean system, int modi) {
		super();
		ID = iD;
		this.gameName = gameName;
		this.path = path;
		PC = pC;
		this.system = system;
		this.modi = modi;
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
	public boolean isPC() {
		return PC;
	}
	public boolean isSystem() {
		return system;
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
}
