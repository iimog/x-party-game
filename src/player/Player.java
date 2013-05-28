package player;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Player {
	public static int anz=0; 	// current number of players
	
	private boolean robot=false;

	public String name;			// name of the player
	public boolean male;		// male or female?
	public Color farbe;			// Spielerfarbe

	private int key;			// Buzzertaste

	public static String standardSound = "media/sounds/sirene.wav";

	public String sound;
	public int playerID;
	public int matchCredit;			// current number of credits for this player

	public int gameCredit;			// current amount of credits in this game
	public String lastAnswer;

	public int lastDistance;	// Abweichung der letzten Antwort in Prozent

	public Player (String name){
		this(name,true);
	}
	public Player(String name, boolean male){
		this(name,male,Color.ORANGE);
	}

	public Player(String name, boolean male, Color farbe){
		this(name, male, farbe, KeyEvent.VK_A);
	}
	public Player(String name, boolean male, Color farbe, int key){
		this(name,male,farbe,key,standardSound);
	}




	public Player(String name, boolean male, Color farbe, int key, String sound){
		this.name = name;
		this.male = male;
		this.farbe = farbe;
		this.key = key;
		this.sound = sound;
		matchCredit = 0;
		gameCredit = 0;
		playerID = anz;
		anz++;
	}

	public int getKey(){
		return key;
	}
	public String getName() {
		return name;
	}

	public void setKey(int key){
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Player pcPlayer(){
		Player pc = new Player("Boten Ana", false, Color.RED, KeyEvent.VK_F24);
		pc.setRobot(true);
		return pc;
	}
	public boolean isRobot() {
		return robot;
	}
	public void setRobot(boolean robot) {
		this.robot = robot;
	}
}
