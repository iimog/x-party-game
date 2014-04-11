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

	private String path = "/media/memory/";
	private String backside = path + "/optisch.jpg";
	public synchronized String getBackside() {
		return backside;
	}

	public synchronized void setBackside(String backside) {
		this.backside = backside;
	}
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
	private Map<String, String> backsides;

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
		settingsChanged();
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
		instance.changeBackground("/media/memory/blue.jpg");
	}

	@Override
	public void openSettingsDialog(boolean inGame){
		instance.showDialog(new MemorySettingsDialog(this, inGame));
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
					karte[i] = new Bildschirm(getBackside());
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
			deckLabel.setFont(X.BUTTON_FONT);
			deckLabel.setText(selectedDeck.getDeckName());
			deckLabel.setHorizontalAlignment(JLabel.CENTER);
			spielBereichPanel.add(deckLabel, BorderLayout.NORTH);
		}
		revalidate();
		repaint();
	}
	
	private void getRandomPairs(){
		if(numOfPairs > selectedDeck.size()){
			numOfPairs = getBiggestFeasableNumOfPairs(selectedDeck.size());
		}
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
	
	public int getBiggestFeasableNumOfPairs(int possible) {
		if(possible >= 27)
			return 27;
		if(possible >= 24)
			return 24;
		if(possible >= 20)
			return 20;
		if(possible >= 15)
			return 15;
		if(possible >= 10)
			return 10;
		return 0;
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
				karte[click[0]].changePic(getBackside());
				karte[click[1]].changePic(getBackside());
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
		getRandomPairs();
		whosTurn = getStartPlayerID(true);
		if(myPlayer[whosTurn].isRobot()){
			startRobotState();
		}
		enableSettingsButton(true);
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		if(modus == Modus.SOLO)memoryRobot.setGrenzWert(schwierigkeit*10);
	}
	
	private synchronized void changeBackside(String newBackside){
		setBackside(newBackside);
		for(int i=0; i<karte.length; i++){
			if(open.contains(i)) continue;
			karte[i].changePic(newBackside);
		}
	}
	
	@Override
	public void loadProperties(){
		memDecks = MemoryDeckLoader.loadMemoryDecks();
		selectedDeck = MemoryDeck.getRandomDeck(memDecks);
		while(selectedDeck.getPictures().size() < numOfPairs){
			selectedDeck = MemoryDeck.getRandomDeck(memDecks);
		}
		backsides = MemoryDeckLoader.loadMemoryBacksides();
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
	
	public Map<String, String> getBacksides(){
		return backsides;
	}
	
	private void propertiesToSettings(){
		if(customSettings == null){
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(MemorySettingsDialog.NUM_OF_ROUNDS, ""+numOfRounds));
		String pairs = customSettings.getProperty(MemorySettingsDialog.PAIRS);
		if(pairs != null && Integer.parseInt(pairs) != numOfPairs)	
			paarZahl(Integer.parseInt(pairs));
		String newBackside = customSettings.getProperty(MemorySettingsDialog.BACKSIDE);
		if(newBackside != null){
			String backsidePath = backsides.get(newBackside);
			if(backsidePath != null)
				changeBackside(backsidePath);
		}
		String deck = customSettings.getProperty(MemorySettingsDialog.DECK);
		if(deck != null)	
			setSelectedDeck(deck);
		if(modus == Modus.SOLO){
			String schwierigkeit = customSettings.getProperty(MemorySettingsDialog.DIFFICULTY);
			if(schwierigkeit != null)
				setSchwierigkeit(Integer.parseInt(schwierigkeit));
		}
	}
}
