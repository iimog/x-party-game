package games.bad6;

import games.Modus;
import games.PC;
import gui.components.Dice;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import player.Player;
import start.X;
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
	Dice[] dice;
	private JLabel[] rundenPunkteLabel;
	private JLabel[] punkteLabel;
	private JPanel schaltflaechenPanel;
	private int currentNum;
	private int roundCredit=0;
	private String lache = "/media/sounds/lache.wav";
	private Bad6Robot myRobot;
	private JPanel punktebereichPanel;

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
		settingsChanged();
	}

	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				spielBereichPanel.setLayout(thisLayout);
				{
					punktebereichPanel = new JPanel(new GridLayout(2, spielerZahl));
					hauptbereichPanel = new JPanel(new GridLayout(1, spielerZahl));
					spielBereichPanel.add(punktebereichPanel, BorderLayout.CENTER);
					spielBereichPanel.add(hauptbereichPanel, BorderLayout.NORTH);
					rundenPunkteLabel = new JLabel[spielerZahl];
					for(int i=0; i<spielerZahl; i++){
						rundenPunkteLabel[i] = new JLabel();
						punktebereichPanel.add(rundenPunkteLabel[i]);
						rundenPunkteLabel[i].setText("0");
						rundenPunkteLabel[i].setFont(STANDARD_FONT.deriveFont(24f));
						rundenPunkteLabel[i]
						                  .setHorizontalAlignment(SwingConstants.CENTER);
					}
					punkteLabel = new JLabel[spielerZahl];
					for(int i=0; i<spielerZahl; i++){
						punkteLabel[i] = new JLabel();
						punktebereichPanel.add(punkteLabel[i]);
						punkteLabel[i].setText("0");
						punkteLabel[i].setFont(STANDARD_FONT.deriveFont(32f));
						punkteLabel[i]
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
					JPanel explanationPanel = new JPanel(new GridLayout(2,1));
					spielBereichPanel.add(explanationPanel, BorderLayout.WEST);
					Font emoFont = X.getEmojiFont().deriveFont(32f);
					JLabel roundLabel = new JLabel("🔓");
					roundLabel.setFont(emoFont);
					JLabel totalLabel = new JLabel("🔒");
					totalLabel.setFont(emoFont);
					explanationPanel.add(roundLabel);
					explanationPanel.add(totalLabel);
				}
				{
					schaltflaechenPanel = new JPanel(new GridLayout(1,1));
					spielBereichPanel
					.add(schaltflaechenPanel, BorderLayout.SOUTH);
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
			updateCreds(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void newGame() {
		for(int i=0; i<spielerZahl; i++){
			dice[i].setEnabled(false);
		}
		dice[whosTurn].setEnabled(true);
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
		propertiesToSettings();
		updateCreds(10);
	}
	private void propertiesToSettings() {
		if(customSettings == null){
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(Bad6SettingsDialog.NUM_OF_ROUNDS, ""+numOfRounds));
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
			dice[whosTurn].setEnabled(false);
			turnOver();
			dice[whosTurn].setEnabled(true);
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
			stopButton.setEnabled(false);
			if(myPlayer[whosTurn].isRobot())
				myRobot.startTurn();
			return;
		}
		roundCredit += currentNum;
		rundenPunkteLabel[whosTurn].setText(roundCredit+"");
		stopButton.setEnabled(true);
		if(myPlayer[whosTurn].isRobot()){
			myRobot.continueTurn(currentNum);
		}
	}
}
