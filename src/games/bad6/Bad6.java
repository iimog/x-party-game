package games.bad6;

import games.Modus;
import games.PC;
import gui.components.Dice;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import player.Player;
import util.ChangeManager;

public class Bad6 extends games.Game implements PC {
	class DiceChange implements ChangeManager {
		@Override
		public void change() {
			throwFinish();
		}
	}
	private static final long serialVersionUID = -3953537180751315641L;
	public static String gameName = "Böse 6";
	public static String getGameName(){
		return gameName;
	}
	public static int defaultNumOfRounds = 4;	// !!Wird im Constructor mit 10 multipliziert!!
	private JPanel hauptbereichPanel;
	JButton stopButton;
	JButton againButton;
	Dice[] dice;
	private JLabel[] rundenPunkteLabel;
	private JLabel[] punkteLabel;
	private JPanel schaltflaechenPanel;
	private int currentNum;
	private int roundCredit=0;
	private String lache = "/media/sounds/lache.wav";
	private Bad6Robot myRobot;

	public Bad6(Player[] myPlayer, Modus modus, String background, int globalGameID) {
		this(myPlayer, defaultNumOfRounds, modus, background, globalGameID);
	}

	public Bad6(player.Player[] myPlayer, int numOfRounds, Modus modus, String background, int globalGameID) {
		super(myPlayer, 10 * numOfRounds, modus, background, globalGameID);
		if(modus == Modus.SOLO){
			myRobot = new Bad6Robot(this);
		}
		dice = new Dice[spielerZahl];
		initGUI();
	}

	private void againButtonActionPerformed(ActionEvent evt) {
		dice[whosTurn].setEnabled(true);
		againButton.setEnabled(false);
		stopButton.setEnabled(false);
		dice[whosTurn].doClick();
	}

	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				spielBereichPanel.setLayout(thisLayout);
				{
					hauptbereichPanel = new JPanel();
					GridLayout hauptbereichPanelLayout = new GridLayout(3, 2);
					hauptbereichPanelLayout.setHgap(5);
					hauptbereichPanelLayout.setVgap(5);
					hauptbereichPanelLayout.setColumns(2);
					hauptbereichPanelLayout.setRows(3);
					hauptbereichPanel.setLayout(hauptbereichPanelLayout);
					spielBereichPanel.add(hauptbereichPanel, BorderLayout.CENTER);
					punkteLabel = new JLabel[spielerZahl];
					for(int i=0; i<spielerZahl; i++){
						punkteLabel[i] = new JLabel();
						hauptbereichPanel.add(punkteLabel[i]);
						punkteLabel[i].setText("0");
						punkteLabel[i].setFont(STANDARD_FONT);
						punkteLabel[i]
						            .setHorizontalAlignment(SwingConstants.CENTER);
					}
					rundenPunkteLabel = new JLabel[spielerZahl];
					for(int i=0; i<spielerZahl; i++){
						rundenPunkteLabel[i] = new JLabel();
						hauptbereichPanel.add(rundenPunkteLabel[i]);
						rundenPunkteLabel[i].setText("0");
						rundenPunkteLabel[i].setFont(STANDARD_FONT);
						rundenPunkteLabel[i]
						                  .setHorizontalAlignment(SwingConstants.CENTER);
					}
					for(int i=0; i<spielerZahl; i++){
						dice[i] = new Dice(myPlayer[i].farbe);
						dice[i].setEnabled(false);
						hauptbereichPanel.add(dice[i]);
						dice[i].addChangeManager(new DiceChange());
					}
				}
				{
					schaltflaechenPanel = new JPanel();
					spielBereichPanel
					.add(schaltflaechenPanel, BorderLayout.SOUTH);
					{
						againButton = new JButton();
						schaltflaechenPanel.add(againButton);
						againButton.setText("Weiter");
						againButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								againButtonActionPerformed(evt);
							}
						});
					}
					{
						stopButton = new JButton();
						schaltflaechenPanel.add(stopButton);
						stopButton.setText("Aufhören");
						stopButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								stopButtonActionPerformed(evt);
							}
						});
					}
				}
			}
			for(int i=0; i<spielerZahl; i++){
				creds[i].setNumOfRounds(numOfRounds/10);
				creds[i].multiply(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void newGame() {
		for(int i=0; i<spielerZahl; i++){
			dice[i].setEnabled(false);
		}
		dice[whosTurn].setEnabled(true);
		againButton.setEnabled(false);
		stopButton.setEnabled(false);
		if(myPlayer[whosTurn].isRobot()){
			myRobot.startTurn();
		}
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new Bad6SettingsDialog(this));
	}

	@Override
	public void settingsChanged(){
		for(int i=0; i<spielerZahl; i++){
			creds[i].setNumOfRounds(numOfRounds/10);
			creds[i].multiply(10);
		}
	}
	@Override
	public void start(){
		whosTurn = getStartPlayerID(true);
		newGame();
	}
	private void stopButtonActionPerformed(ActionEvent evt) {
		myPlayer[whosTurn].gameCredit += roundCredit;
		creds[whosTurn].earnsCredit(roundCredit);
		punkteLabel[whosTurn].setText(myPlayer[whosTurn].gameCredit+"");
		roundCredit=0;
		rundenPunkteLabel[whosTurn].setText("0");
		if(isOver())gameEnd();
		else{
			turnOver();
			dice[whosTurn].setEnabled(true);
			againButton.setEnabled(false);
			stopButton.setEnabled(false);
			if(myPlayer[whosTurn].isRobot())
				myRobot.startTurn();
		}
	}
	private void throwFinish(){
		currentNum = dice[whosTurn].getZahl();
		if(currentNum==6){
			playAudioFile(lache);
			roundCredit=0;
			dice[whosTurn].setEnabled(false);
			rundenPunkteLabel[whosTurn].setText("0");
			turnOver();
			dice[whosTurn].setEnabled(true);
			againButton.setEnabled(false);
			stopButton.setEnabled(false);
			if(myPlayer[whosTurn].isRobot())
				myRobot.startTurn();
			return;
		}
		roundCredit += currentNum;
		rundenPunkteLabel[whosTurn].setText(roundCredit+"");
		dice[whosTurn].setEnabled(false);
		againButton.setEnabled(true);
		stopButton.setEnabled(true);
		if(myPlayer[whosTurn].isRobot()){
			myRobot.continueTurn(currentNum);
		}
	}
}
