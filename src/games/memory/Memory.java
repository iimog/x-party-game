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
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import start.X;


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
	public static int defaultNumOfRounds = 5;
	private int schwierigkeit = 5;
	public int getSchwierigkeit() {
		return schwierigkeit;
	}

	public void setSchwierigkeit(int schwierigkeit) {
		this.schwierigkeit = schwierigkeit;
	}

	private String path = "media/memory/";
	public static String[] pic = new String[69];
	{
		pic[29] = path + "architektur.jpg";
		pic[32] = path + "architektur1.jpg";
		pic[24] = path + "natur7.jpg";
		pic[27] = path + "natur8.jpg";
		pic[13] = path + "natur9.jpg";
		pic[60] = path + "natur10.jpg";
		pic[18] = path + "natur11.jpg";
		pic[20] = path + "natur12.jpg";
		pic[28] = path + "natur13.jpg";
		pic[33] = path + "natur14.jpg";
		pic[8] = path + "tier.jpg";
		pic[9] = path + "tier1.jpg";
		pic[10] = path + "tier2.jpg";
		pic[26] = path + "tier3.jpg";
		pic[5] = path + "tier4.jpg";
		pic[16] = path + "tier5.jpg";
		pic[22] = path + "tier6.jpg";
		pic[65] = path + "tier7.jpg";
		pic[61] = path + "sonstiges.jpg";
		pic[62] = path + "sonstiges1.jpg";
		pic[63] = path + "sonstiges2.jpg";
		pic[6] = path + "sonstiges3.jpg";
		pic[1] = path + "sonstiges4.jpg";
		pic[2] = path + "sonstiges5.jpg";
		pic[3] = path + "sonstiges6.jpg";
		pic[4] = path + "sonstiges7.jpg";
		pic[11] = path + "sonstiges8.jpg";
		pic[12] = path + "sonstiges9.jpg";
		pic[15] = path + "sonstiges11.jpg";
		pic[19] = path + "sonstiges10.jpg";
		pic[21] = path + "sonstiges12.jpg";
		pic[25] = path + "sonstiges13.jpg";
		pic[23] = path + "sonstiges14.jpg";
		pic[30] = path + "sonstiges15.jpg";
		pic[31] = path + "sonstiges16.jpg";
		pic[34] = path + "sonstiges17.jpg";
		pic[64] = path + "sonstiges18.jpg";
		pic[35] = path + "früchte.jpg";
		pic[36] = path + "früchte1.jpg";
		pic[37] = path + "früchte2.jpg";
		pic[38] = path + "früchte3.jpg";
		pic[39] = path + "früchte4.jpg";
		pic[40] = path + "früchte5.jpg";
		pic[41] = path + "früchte6.jpg";
		pic[42] = path + "früchte7.jpg";
		pic[43] = path + "früchte8.jpg";
		pic[44] = path + "früchte9.jpg";
		pic[45] = path + "früchte10.jpg";
		pic[46] = path + "früchte11.jpg";
		pic[47] = path + "früchte12.jpg";
		pic[48] = path + "früchte13.jpg";
		pic[49] = path + "früchte14.jpg";
		pic[50] = path + "früchte15.jpg";
		pic[51] = path + "früchte16.jpg";
		pic[52] = path + "früchte17.jpg";
		pic[53] = path + "früchte18.jpg";
		pic[54] = path + "früchte19.jpg";
		pic[55] = path + "früchte20.jpg";
		pic[56] = path + "früchte21.jpg";
		pic[57] = path + "früchte22.jpg";
		pic[58] = path + "früchte23.jpg";
		pic[59] = path + "früchte24.jpg";
		pic[14] = path + "früchte25.jpg";
		pic[17] = path + "früchte26.jpg";
		pic[66] = path + "sonstiges19.jpg";
		pic[67] = path + "architektur2.jpg";
		pic[68] = path + "sonstiges20.jpg";
		
	
		
		
				
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
	private List<MemoryDeck> memDecks;
	private Map<String, MemoryDeck> memDeckMap;
	private MemoryDeck selectedDeck;
	private JLabel deckLabel;

	public Memory(Player[] myPlayer, Modus modus, int globalGameID) {
		this(myPlayer, defaultNumOfRounds, modus, globalGameID);
	}

	public Memory(Player[] player, int numOfRounds, Modus modus, int globalGameID) {
		super(player, numOfRounds, modus, globalGameID);
		if(modus == Modus.SOLO){
			memoryRobot = new MemoryRobot(this);
			memoryRobot.setGrenzWert(schwierigkeit*10);
		}
		mischen(27);
		initGUI();
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
		{
			deckLabel = new JLabel();
			deckLabel.setOpaque(false);
			deckLabel.setFont(X.buttonFont);
			deckLabel.setText(selectedDeck.getDeckName());
			deckLabel.setHorizontalAlignment(JLabel.CENTER);
			spielBereichPanel.add(deckLabel, BorderLayout.NORTH);
		}
	}
	
	private void getRandomPairs(int numOfPairs){
		pictures = new ArrayList<String>();
		HashSet<Integer> verbraucht = new HashSet<Integer>();
		Random r = new Random();
		while(pictures.size()<numOfPairs){
			int nextPic = r.nextInt(selectedDeck.size());
			if(verbraucht.add(nextPic)){
				pictures.add(selectedDeck.getPictures().get(nextPic));
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
	
	@Override
	public void loadProperties(){
		memDecks = MemoryDeckLoader.loadMemoryDecks();
		selectedDeck = MemoryDeck.getRandomDeck(memDecks);
	}

	public List<MemoryDeck> getMemDecks() {
		return memDecks;
	}
	
	public Map<String,MemoryDeck> getMemDeckMap() {
		if(memDeckMap == null){
			memDeckMap = new HashMap<String,MemoryDeck>();
			for(MemoryDeck md : memDecks){
				memDeckMap.put(md.getDeckName(), md);
			}
		}
		return memDeckMap;
	}
	
	/**
	 * Gibt eine Liste mit Decknamen zurück. Pseudonamen sind "Zufall" und  "Alle"
	 * @param withPseudoNames
	 * @return Liste der Decknamen
	 */
	public List<String> getMemDeckNames(boolean withPseudoNames) {
		List<String> deckNames = new ArrayList<String>();
		if(withPseudoNames){
			deckNames.add("Zufall");
			deckNames.add("Alles");
		}
		deckNames.addAll(getMemDeckMap().keySet());
		return deckNames;
	}
	
	public void setSelectedDeck(String deckName){
		if(deckName.equals("Zufall")){
			selectedDeck = MemoryDeck.getRandomDeck(memDecks);
		}
		else if(deckName.equals("Alles")){
			selectedDeck = MemoryDeck.getFullDeck(memDecks);
		}
		else{			
			selectedDeck = getMemDeckMap().get(deckName);
		}
		deckLabel.setText(selectedDeck.getDeckName());
	}
}
