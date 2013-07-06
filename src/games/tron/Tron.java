package games.tron;

import games.Game;
import games.Modus;
import games.PC;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import player.Player;

public class Tron extends Game implements PC{
	private static final long serialVersionUID = 1L;
	final static String gameName = "Tron";
	public static String getGameName(){
		return gameName;
	}
	// up, down, right, left evtl. durch enum ersetzen
	private static final int UP = -1;
	private static final int DOWN = 1;
	private static final int RIGHT = 2;
	private static final int LEFT = -2;
	private static final int defaultNumOfRounds = 5;
	private static final int defaultColums = 100;
	private static final int defaultRows = 50;
	int colums;
	int rows;
	Point[] kopf;
	List<Set<Point>> belegteFelder;
	TronSpielfeld spielfeld;
	int[] aktuelleRichtung;
	private int[] neueRichtung;
	boolean[] kollision;
	int sleepTime = 100;
	boolean gitterVisible = true;
	Dimension feldGroesse = new Dimension(10,10);
	private TronGameLoop currentGameLoop;
	private JPanel hauptbereichPanel;
	JButton startButton;
	
	public void pause(){
		currentGameLoop.pause();
	}
	
	public void resume(){
		spielfeld.requestFocusInWindow();
	}
	
	public boolean isGitterVisible() {
		return gitterVisible;
	}
	public void setGitterVisible(boolean gitterVisible) {
		this.gitterVisible = gitterVisible;
		spielfeld.showGitter(gitterVisible);
	}

	public Tron(Player[] player, Modus modus, int globalGameID) {
		this(player, defaultNumOfRounds, modus, globalGameID);
	}
	public Tron(Player[] player, int numOfRounds, Modus modus, int globalGameID) {
		super(player, numOfRounds, modus, globalGameID);
		colums = defaultColums;
		rows = defaultRows;
		initArrays();
		initGUI();
		initGame();
	}
	
	private void initArrays() {
		kopf = new Point[spielerZahl];
		aktuelleRichtung = new int[spielerZahl];
		neueRichtung = new int[spielerZahl];
		kollision = new boolean[spielerZahl];
	}
	private void initGame() {
		spielfeld.clear();
		belegteFelder = Collections.synchronizedList(new ArrayList<Set<Point>>());
		for(int i=0; i<spielerZahl; i++){
			belegteFelder.add(Collections.synchronizedSet(new HashSet<Point>()));
		}
		initKoepfe();
		for(int i=0; i<spielerZahl; i++){
			belegteFelder.get(i).add(kopf[i]);
			spielfeld.faerbeFeld(kopf[i], myPlayer[i].farbe);
			if(i>0 && i<3){
				aktuelleRichtung[i] = RIGHT;
				neueRichtung[i] = RIGHT;
			}
			else{
				aktuelleRichtung[i] = LEFT;
				neueRichtung[i] = LEFT;
			}
			kollision[i] = false;
		}
		enableSettingsButton(true);
		startButton.setEnabled(true);
	}
	
	private void initKoepfe() {
		kopf[0] = new Point(colums-4,rows-4);
		kopf[1] = new Point(5,5);
		if(spielerZahl>2){
			kopf[2] = new Point(5, rows-4);
			if(spielerZahl>3){
				kopf[3] = new Point(colums-4, 5);
			}
		}
	}
	private void initGUI(){
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		spielBereichPanel.add(hauptbereichPanel);
		startButton = new JButton("Start");
		hauptbereichPanel.add(startButton, BorderLayout.SOUTH);
		startButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getGameLoop().start();
				startButton.setEnabled(false);
				enableSettingsButton(false);
			}
		});
		addSpielfeld();
	}
	
	private void addSpielfeld() {
		spielfeld = new TronSpielfeld(colums, rows, feldGroesse);
		hauptbereichPanel.add(spielfeld, BorderLayout.CENTER);
		spielfeld.addKeyListener(steuerung);
		spielfeld.showGitter(isGitterVisible());
		hauptbereichPanel.validate();
		hauptbereichPanel.repaint();
	}

	private KeyAdapter steuerung = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent evt) {
			if(evt.getKeyCode() == KeyEvent.VK_LEFT && aktuelleRichtung[0] != RIGHT)
				neueRichtung[0] = LEFT;
			if(evt.getKeyCode() == KeyEvent.VK_RIGHT && aktuelleRichtung[0] != LEFT)
				neueRichtung[0] = RIGHT;
			if(evt.getKeyCode() == KeyEvent.VK_UP && aktuelleRichtung[0] != DOWN)
				neueRichtung[0] = UP;
			if(evt.getKeyCode() == KeyEvent.VK_DOWN && aktuelleRichtung[0] != UP)
				neueRichtung[0] = DOWN;
			
			if(evt.getKeyCode() == KeyEvent.VK_A && modus!=Modus.SOLO && aktuelleRichtung[1] != RIGHT)
				neueRichtung[1] = LEFT;
			if(evt.getKeyCode() == KeyEvent.VK_D && modus!=Modus.SOLO && aktuelleRichtung[1] != LEFT)
				neueRichtung[1] = RIGHT;
			if(evt.getKeyCode() == KeyEvent.VK_W && modus!=Modus.SOLO && aktuelleRichtung[1] != DOWN)
				neueRichtung[1] = UP;
			if(evt.getKeyCode() == KeyEvent.VK_S && modus!=Modus.SOLO && aktuelleRichtung[1] != UP)
				neueRichtung[1] = DOWN;
			
			if(evt.getKeyCode() == KeyEvent.VK_NUMPAD4 && spielerZahl>2 && aktuelleRichtung[2] != RIGHT)
				neueRichtung[2] = LEFT;
			if(evt.getKeyCode() == KeyEvent.VK_NUMPAD6 && spielerZahl>2 && aktuelleRichtung[2] != LEFT)
				neueRichtung[2] = RIGHT;
			if(evt.getKeyCode() == KeyEvent.VK_NUMPAD8 && spielerZahl>2 && aktuelleRichtung[2] != DOWN)
				neueRichtung[2] = UP;
			if(evt.getKeyCode() == KeyEvent.VK_NUMPAD5 && spielerZahl>2 && aktuelleRichtung[2] != UP)
				neueRichtung[2] = DOWN;
			
			if(evt.getKeyCode() == KeyEvent.VK_J && spielerZahl>3 && aktuelleRichtung[3] != RIGHT)
				neueRichtung[3] = LEFT;
			if(evt.getKeyCode() == KeyEvent.VK_L && spielerZahl>3 && aktuelleRichtung[3] != LEFT)
				neueRichtung[3] = RIGHT;
			if(evt.getKeyCode() == KeyEvent.VK_I && spielerZahl>3 && aktuelleRichtung[3] != DOWN)
				neueRichtung[3] = UP;
			if(evt.getKeyCode() == KeyEvent.VK_K && spielerZahl>3 && aktuelleRichtung[3] != UP)
				neueRichtung[3] = DOWN;
			
			if(evt.getKeyCode() == KeyEvent.VK_ESCAPE){
				currentGameLoop.togglePause();
				evt.consume();
			}
		}
	};
	private TronGameLoop getGameLoop(){
		TronGameLoop gameLoop = new TronGameLoop(this);
		currentGameLoop = gameLoop;
		return gameLoop;
	}
	protected void endOfRound() {
		int winnerID = -1;
		for(int i=0; i<spielerZahl; i++){
			if(!kollision[i])winnerID=i;
		}
		if(winnerID == -1){
			openRoundDialog("Keinen");
		}
		else{
			creds[winnerID].earnsCredit(1);
			myPlayer[winnerID].gameCredit++;
			openRoundDialog(myPlayer[winnerID].name);
		}
	}
	protected void maleNeuenKopf() {
		for(int i=0; i<spielerZahl; i++){
			if(kollision[i] == false){
				spielfeld.faerbeFeld(kopf[i], myPlayer[i].farbe);
			}
		}	
	}
	protected void pruefeKollision() {
		for(int i=0; i<spielerZahl; i++)
			kollision[i] = isKollidiert(kopf[i]);
	}
	private boolean isKollidiert(Point kopf) {
		boolean kollidiert = false;
		// Kollision mit Wand
		if(kopf.x<1 || kopf.x>colums || kopf.y<1 || kopf.y>rows){
			kollidiert = true;
		}
		int kopfrempler = 0;
		for(int i=0; i<spielerZahl; i++){
			if(belegteFelder.get(i).contains(kopf)){
				kollidiert = true;
			}
			if(this.kopf[i].equals(kopf)){
				kopfrempler++;
			}
		}			
		if(kopfrempler>1)
			kollidiert = true;
		return kollidiert;
	}
	protected void moveSchlangen() {
		for(int i=0; i<spielerZahl; i++){
			if(!kollision[i]){
				belegteFelder.get(i).add(kopf[i]);
				aktuelleRichtung[i] = neueRichtung[i];
				kopf[i] = moveSchlange(kopf[i], aktuelleRichtung[i]);
			}
		}		
	}
	private Point moveSchlange(Point kopf, int richtung){
		Point neuerKopf = new Point(kopf);
		if(richtung == RIGHT)
			neuerKopf.x += 1;
		if(richtung == LEFT)
			neuerKopf.x -= 1;
		if(richtung == UP)
			neuerKopf.y -= 1;
		if(richtung == DOWN)
			neuerKopf.y += 1;
		return neuerKopf;
	}
	Point moveSchlangeRueckwaerts(Point kopf, int richtung){
		Point zurueck = kopf;
		if(richtung == RIGHT)
			zurueck = moveSchlange(kopf, LEFT);
		if(richtung == LEFT)
			zurueck = moveSchlange(kopf, RIGHT);
		if(richtung == UP)
			zurueck = moveSchlange(kopf, DOWN);
		if(richtung == DOWN)
			zurueck = moveSchlange(kopf, UP);
		return zurueck;
	}
	
	public void openRoundDialog(String winner){
		games.dialogeGUIs.RoundDialog rd = new games.dialogeGUIs.RoundDialog(this,winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}
	public void openDetailsDialog(){
		instance.showDialog(new TronDetailsDialog(this));
	}
	public void openSettingsDialog(boolean inGame){
		instance.showDialog(new TronSettingsDialog(this, inGame));
	}
	public void settingsChanged(){
		updateCreds();
		addSpielfeld();
		initGame();
	}
	public void goBack(){
		if(!super.isOver()){
			initGame();
		}
	}
	
	public void destroy(){
		super.destroy();
		if(currentGameLoop!=null)
			currentGameLoop.interrupt();
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
	
	public void nowVisible(){
		instance.changeBackground("media/tron/Tron.jpg");
	}
}
