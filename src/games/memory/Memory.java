package games.memory;
import games.Modus;
import games.PC;
import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import player.Player;
import util.SpielListen;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class Memory extends games.Game implements PC {
	private static final long serialVersionUID = -8268979453023663027L;
	public static String gameName = "Memory";
	public static String getGameName(){
		return gameName;
	}
	public static String shortInfo = "Das klassische Merkspiel";
	private static final int GAME_ID = SpielListen.MEMORY;
	public int getGameID(){
		return GAME_ID;
	}
	public static int defaultNumOfRounds = 5;
	private int schwierigkeit = 5;
	public int getSchwierigkeit() {
		return schwierigkeit;
	}

	public void setSchwierigkeit(int schwierigkeit) {
		this.schwierigkeit = schwierigkeit;
	}

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Memory inst = new Memory(new Player[] {},0, Modus.DUELL); // nur für Testzwecke
				inst.setVisible(true);
			}
		});
	}
	private String path = "media/memory/";public static String[] pic = new String[35];
	{
		pic[6] = path + "audia5.jpg";
		pic[7] = path + "berg.jpg";
		pic[24] = path + "dschungel.jpg";
		pic[0] = path + "erde.jpg";
		pic[8] = path + "eisvogel.jpg";
		pic[9] = path + "eisvogel2.jpg";
		pic[10] = path + "elefant.jpg";
		pic[1] = path + "feuerwerk.jpg";
		pic[26] = path + "fische.jpg";
		pic[2] = path + "gruen.jpg";
		pic[3] = path + "holz.jpg";
		pic[4] = path + "laser.jpg";
		pic[5] = path + "loewe.jpg";
		pic[11] = path + "meer.jpg";
		pic[12] = path + "orange.jpg";
		pic[13] = path + "palme1.jpg";
		pic[14] = path + "palme2.jpg";
		pic[15] = path + "palme3.jpg";
		pic[16] = path + "robbe.jpg";
		pic[17] = path + "rose1.jpg";
		pic[18] = path + "seerose.jpg";
		pic[19] = path + "sonne1.jpg";
		pic[20] = path + "sonne2.jpg";
		pic[21] = path + "spiderman.jpg";
		pic[22] = path + "tiger.jpg";
		pic[25] = path + "tigers.jpg";
		pic[23] = path + "wasser.jpg";
		pic[27] = path + "erde-und-mond.jpg";
		pic[28] = path + "insel_1.jpg";
		pic[29] = path + "festung.jpg";
		pic[30] = path + "ornament1.jpg";
		pic[31] = path + "ornament2.jpg";
		pic[32] = path + "ornament3.jpg";
		pic[33] = path + "welle3.jpg";
		pic[34] = path + "wueste.jpg";
	}
	public String background = path + "optisch.jpg";
	Bildschirm[] karte;
	HashSet<Integer> set1, set2;
	HashSet<Integer> open = new HashSet<Integer>();
	ArrayList<String> pictures;
	boolean waiting = false;
	HashMap<Integer, Integer> loesung;
	private JPanel spielfeldPanel;
	int clicked = 0; // Zählt die Zahl der Klicks
	int[] click = new int[2];
	{
		click[0]=-1; click[1]=-1; // Speichert die zuletzt geklickten Felder
	}
	int numOfPairs=27;	// gibt die Anzahl der Pärchen an, nur durch paarZahl änderbar
	MouseAdapter mouse = new MouseAdapter(){
		@Override
		public void mouseReleased(MouseEvent evt){
			if(waiting)return;
			Bildschirm bs = (Bildschirm) evt.getSource();
			if(clicked<2){
				int clickInd = Integer.parseInt(bs.getName());
				if(!open.add(clickInd))return;
				bs.changePic(pictures.get(loesung.get(clickInd)));
				if(modus == Modus.SOLO){
					memoryRobot.setInfo(clickInd, loesung.get(clickInd));
				}				
				spielfeldPanel.repaint();
				click[clicked]=clickInd;
				clicked++;
			}
			if(clicked==2){
				if(click[0]!=click[1] && loesung.get(click[0])==loesung.get(click[1])){
					myPlayer[whosTurn].gameCredit++;
					creds[whosTurn].earnsCredit(1);
					if(isOver()){
						gameEnd();
						return;
					}
					clicked = 0; click[0] = -1; click[1] = -1;
				}
				else{
					waiting = true;
					rueckdecken();
				}
			}
		}
	};
	private MemoryRobot memoryRobot;

	public Memory(Player[] myPlayer, Modus modus) {
		this(myPlayer, defaultNumOfRounds, modus);
	}

	public Memory(Player[] player, int numOfRounds, Modus modus) {
		super(gameName, player, numOfRounds, modus);
		if(modus == Modus.SOLO){
			memoryRobot = new MemoryRobot(this);
			memoryRobot.setGrenzWert(schwierigkeit*10);
		}
		mischen(27);
		initGUI();
	}

	@Override
	public String getShortInfo() {
		return shortInfo;
	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			spielBereichPanel.setLayout(thisLayout);
			{
				paarZahl(numOfPairs);
			}
			updateCreds();
		} catch (Exception e) {
			//add your error handling code here
			e.printStackTrace();
		}
	}

	public void mischen(int paare){
		set1=new HashSet<Integer>(); set2=new HashSet<Integer>();
		loesung = new HashMap<Integer, Integer>();
		Random r = new Random();
		for(int i=0; i<paare*2; i++){
			while(true){
				int rn = r.nextInt(paare);
				if(set1.add(rn)||set2.add(rn)){
					loesung.put(i, rn);
					break;
				}
			}
		}
	}

	@Override
	public void nowVisible(){
		instance.changeBackground("media/memory/blue.jpg");
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new MemorySettingsDialog(this));
	}

	public void paarZahl(int zahl){
		if(zahl!=27 && zahl!=15 && zahl!=10 && zahl!=20 && zahl!=24)return;
		int columns=9, rows=6;
		numOfPairs = zahl;
		if(modus == Modus.SOLO)memoryRobot.setPositionsZahl(numOfPairs*2);
		karte = new Bildschirm[2*numOfPairs];
		mischen(numOfPairs);
		try{
			remove(spielfeldPanel);
		}
		catch(Exception e){
			// nichts
		}
		if(zahl == 27){
			columns = 9; rows = 6;
		}
		if(zahl == 15){
			columns = 6; rows = 5;
		}
		if(zahl == 10){
			columns = 5; rows = 4;
		}
		if(zahl == 20){
			columns = 8; rows = 5;
		}
		if(zahl == 24){
			columns = 8; rows = 6;
		}
		{
			spielfeldPanel = new JPanel();
			spielfeldPanel.setOpaque(false);
			GridLayout spielfeldPanelLayout = new GridLayout(rows, columns);
			spielfeldPanelLayout.setHgap(5);
			spielfeldPanelLayout.setVgap(5);
			spielBereichPanel.add(spielfeldPanel, BorderLayout.CENTER);
			spielfeldPanel.setLayout(spielfeldPanelLayout);
			{
				for(int i=0; i<2*numOfPairs; i++){
					karte[i] = new Bildschirm(background);
					karte[i].setName(""+i);
					karte[i].addMouseListener(mouse);
					spielfeldPanel.add(karte[i]);
				}
			}
			spielfeldPanel.setMinimumSize(new Dimension(columns*105, rows*105));
		}
	}
	
	private void getRandomPairs(int numOfPairs){
		pictures = new ArrayList<String>();
		HashSet<Integer> verbraucht = new HashSet<Integer>();
		Random r = new Random();
		while(pictures.size()<numOfPairs){
			int nextPic = r.nextInt(pic.length);
			if(verbraucht.add(nextPic)){
				pictures.add(pic[nextPic]);
			}
		}
	}
	
	public void rueckdecken(){
		Thread t = new Thread(){
			@Override
			public void run(){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// nichts
				}
				karte[click[0]].changePic(background);
				karte[click[1]].changePic(background);
				spielfeldPanel.repaint();
				open.remove(click[0]);
				open.remove(click[1]);
				clicked = 0; click[0] = -1; click[1] = -1;
				waiting = false;
				turnOver();
				if(myPlayer[whosTurn].isRobot()){
					startRobotState();
				}
			}
		};
		t.start();
	}
	protected void startRobotState() {
		waiting = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int zug = memoryRobot.getAnzugPosition();
		open.add(zug);
		karte[zug].changePic(pictures.get(loesung.get(zug)));
		click[0] = zug;
		memoryRobot.setInfo(zug, loesung.get(zug));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		zug = memoryRobot.getWeiterzugPosition();
		open.add(zug);
		karte[zug].changePic(pictures.get(loesung.get(zug)));
		click[1] = zug;
		memoryRobot.setInfo(zug, loesung.get(zug));
		if(loesung.get(click[0]) == loesung.get(click[1])){
			myPlayer[whosTurn].gameCredit++;
			creds[whosTurn].earnsCredit(1);
			if(isOver()){
				gameEnd();
				return;
			}
			startRobotState();
		}
		else{
			rueckdecken();
		}
	}

	@Override
	public void start(){
		getRandomPairs(numOfPairs);
		whosTurn = getStartPlayerID(true);
		if(myPlayer[whosTurn].isRobot()){
			startRobotState();
		}
	}

	@Override
	public void settingsChanged() {
		updateCreds();
		if(modus == Modus.SOLO)memoryRobot.setGrenzWert(schwierigkeit*10);
	}

}
