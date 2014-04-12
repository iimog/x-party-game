package games.innereUhr;
import games.Modus;
import games.PC;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import player.Player;


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
public class InnereUhr extends games.Game implements PC {
	private static final long serialVersionUID = -5686342203716597780L;
	final static String gameName = "Innere Uhr";
	public static String getGameName(){
		return gameName;
	}
	public static int defaultNumOfRounds = 5;
	
	private JPanel hauptbereichPanel;
	private JLabel[] statusLabel;
	private JPanel playerPanel;
	JLabel zielzeitLabel;

	private JButton startButton;
	int minGuessTime = 5;
	int maxGuessTime = 20;
	private int guessTime;					// Zeit in Sekunden
	int lastGuessTime;
	int toleranz = 10000;			// Toleranzzeit in Millisekunden
	boolean toleranzOn = true;
	long[] playerTime;
	long[] distance;
	private long startTime;
	private boolean isRunning=false;
	private boolean[] finished;

	int rWinner;				// playerID des Rundensiegers

	public InnereUhr(Player[] myPlayer, Modus modus, String background, int globalGameID) {
		this(myPlayer, defaultNumOfRounds, modus, background, globalGameID);
	}

	public InnereUhr(Player[] myPlayer, int numOfRounds, Modus modus, String background, int globalGameID) {
		super(myPlayer, numOfRounds, modus, background, globalGameID);
		if(modus == Modus.SOLO){
			spielerZahl--;
			toleranz = 2000;
		}
		statusLabel = new JLabel[spielerZahl];
		playerTime = new long[spielerZahl];
		distance = new long[spielerZahl];
		finished = new boolean[spielerZahl];
		initGUI();
	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			spielBereichPanel.setLayout(thisLayout);
			updateCreds();
			{
				startButton = new JButton();
				spielBereichPanel.add(startButton, BorderLayout.SOUTH);
				startButton.setText("Start");
				startButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						startButtonActionPerformed(evt);
					}
				});
			}
			{
				hauptbereichPanel = new JPanel();
				GridLayout hauptbereichPanelLayout = new GridLayout(2, 1);
				hauptbereichPanelLayout.setHgap(5);
				hauptbereichPanelLayout.setVgap(5);
				hauptbereichPanelLayout.setColumns(1);
				hauptbereichPanelLayout.setRows(2);
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				spielBereichPanel.add(hauptbereichPanel, BorderLayout.CENTER);
				{
					zielzeitLabel = new JLabel();
					hauptbereichPanel.add(zielzeitLabel);
					zielzeitLabel.setText("Zielzeit");
					zielzeitLabel.setHorizontalAlignment(SwingConstants.CENTER);
					zielzeitLabel.setFont(new java.awt.Font("Algerian",0,48));
				}
				{
					playerPanel = new JPanel();
					GridLayout playerPanelLayout = new GridLayout();
					playerPanelLayout.setHgap(5);
					playerPanelLayout.setVgap(5);
					playerPanelLayout.setColumns(spielerZahl);
					playerPanelLayout.setRows(1);
					hauptbereichPanel.add(playerPanel);
					playerPanel.setLayout(playerPanelLayout);
					for(int i=0; i<spielerZahl; i++)
					{
						statusLabel[i] = new JLabel();
						playerPanel.add(statusLabel[i]);
						statusLabel[i].setText("Status");
						statusLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
						statusLabel[i].setFont(new java.awt.Font("Segoe UI",0,18));
					}
				}
			}
			setSize(500, 300);
		} catch (Exception e) {
			//add your error handling code here
			e.printStackTrace();
		}
	}

	void nextRound(){
		unstoppable = false;
		startButton.setEnabled(true);
		if(modus == Modus.TEAM){
			changeActivePlayers();
		}
		for(int i=0; i<spielerZahl; i++){
			finished[i]=false;
			statusLabel[i].setText("Start drücken");
		}
		guessTime = nextTime();
		zielzeitLabel.setText(guessTime + " Sekunden");
	}

	int nextTime(){
		return new Random().nextInt(maxGuessTime-minGuessTime+1)+minGuessTime;
	}

	@Override
	public void nowVisible(){
		instance.changeBackground("/media/innereuhr/Sanduhr.jpg");
	}

	@Override
	public void openDetailsDialog(){
		instance.showDialog(new InnereUhrDetailsDialog(this));
	}

	@Override
	public void openRoundDialog(String winner){
		games.dialogeGUIs.RoundDialog rd = new games.dialogeGUIs.RoundDialog(this,winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new InnereUhrSettingsDialog(this));
	}
	
	public void settingsChanged(){
		updateCreds();
	}

	private void roundEnd(){
		isRunning=false;
		setBuzzerActive(false);
		rWinner = roundWinner();
		if(rWinner==-1){
			openRoundDialog("keinen");
		}
		else{
			openRoundDialog(myPlayer[rWinner].name);
			myPlayer[rWinner].gameCredit++;
			creds[rWinner].earnsCredit(1);
		}
		lastGuessTime = guessTime;
		nextRound();
	}

	/**
	 * liefert die PlayerID des Gewinners zurück; falls beide gleich gut sind liefert er -1
	 * wenn beide außerhalb der Toleranz liefert er -1
	 * @return PlayerID des Gewinners
	 */
	public int roundWinner(){
		for(int i=0; i<spielerZahl; i++){
			distance[i]=playerTime[i]-guessTime*1000;
		}
		int winnerID = -1;
		long miniDist = Long.MAX_VALUE;
		for(int i=0; i<spielerZahl; i++){
			long dist = Math.abs(distance[i]);
			if(dist==miniDist){
				winnerID = -1;
			}
			if(dist<miniDist){
				miniDist = dist;
				winnerID = i;
			}
		}
		if(toleranzOn && miniDist>toleranz){
			winnerID=-1;
		}
		if(modus == Modus.SOLO && winnerID == -1)winnerID = 1;
		return winnerID;
	}
	@Override
	public void start(){
		nextRound();
	}
	private void startButtonActionPerformed(ActionEvent evt) {
		startTime = System.currentTimeMillis();
		startButton.setEnabled(false);
		unstoppable = true;
		isRunning = true;
		setBuzzerActive(true);
		for(int i=0; i<spielerZahl; i++){
			statusLabel[i].setText(KeyEvent.getKeyText(myPlayer[i].getKey())+" drücken");
		}
		this.requestFocus();
	}
	
	@Override
	public void buzzeredBy(int playerID) {
		if(!isRunning)return;
		playerTime[playerID] = System.currentTimeMillis()-startTime;
		finished[playerID]=true;
		statusLabel[playerID].setText("Fertig");
		for(int i=0; i<spielerZahl; i++){
			if(!finished[i])return;
		}
		roundEnd();
	}

}
