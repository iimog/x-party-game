package games.wurst;

import games.Game;
import games.Modus;
import games.PC;
import games.buchstabensalat.BuchstabenSalatSettingsDialog;
import gui.EasyDialog;

import java.awt.GridLayout;

import javax.swing.JPanel;

import player.Player;
import util.ChangeManager;

public class Wurst extends Game implements PC{
	private static final long serialVersionUID = 1L;
	private static String gameName = "Die Wurst";
	public static String getGameName(){
		return gameName;
	}
	private static int defaultNumOfRounds = 5;
	private JPanel wurstFlaeche;
	private WurstPanel[] wurstPanel;
	
	int anfangsWert = 500;
	
	boolean schonEinsOffen = false;
	private int[] abgabe;

	public Wurst(Player[] player, Modus modus, String background, int globalGameID) {
		super(player, defaultNumOfRounds, modus, background, globalGameID);
		wurstPanel = new WurstPanel[spielerZahl];
		abgabe = new int[spielerZahl];
		initGUI();
	}
	
	private void initGUI(){
		wurstFlaeche = new JPanel();
		wurstFlaeche.setLayout(new GridLayout(1,spielerZahl));
		wurstFlaeche.setOpaque(false);
		for(int i=0; i<spielerZahl; i++){
			wurstPanel[i] = new WurstPanel(this, myPlayer[i]);
			wurstFlaeche.add(wurstPanel[i]);
			wurstPanel[i].addChangeManager(new ChangeManager() {				
				@Override
				public void change() {
					wurstPanelStatusGeaendert();
				}
			});
		}
		spielBereichPanel.add(wurstFlaeche);
	}

	protected void wurstPanelStatusGeaendert() {
		schonEinsOffen = false;
		boolean alleFertig = true;
		for(int i=0; i<spielerZahl; i++){
			if(wurstPanel[i].isOffen()){
				schonEinsOffen = true;
			}
			if(wurstPanel[i].isAktiv()){
				alleFertig = false;
			}
		}
		if(alleFertig){
			rundenEnde();
		}
	}

	private void rundenEnde() {
		for(int i=0; i<spielerZahl; i++){
			abgabe[i] = wurstPanel[i].getAbgabe();
		}
		int winner = whoWon();
		if(winner == -1){
			openRoundDialog("keinen");
		}
		else{
			creds[winner].earnsCredit(1);
			myPlayer[winner].gameCredit++;
			openRoundDialog(myPlayer[winner].name);
		}
		if(!isOver()){
			startNextRound();
		}
	}
	
	private void startNextRound() {
		for(int i=0; i<spielerZahl; i++){
			wurstPanel[i].neueRunde();
		}
		if(modus == Modus.TEAM){
			changeActivePlayers();
		}
	}

	public int whoWon(){
		int winnerID = -1;
		long maxWert = Long.MIN_VALUE;
		for(int i=0; i<spielerZahl; i++){
			long abg = abgabe[i];
			if(abg==maxWert){
				winnerID = -1;
			}
			if(abg>maxWert){
				maxWert = abg;
				winnerID = i;
			}
		}
		return winnerID;
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
	}

	private void propertiesToSettings() {
		if(customSettings == null){
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(BuchstabenSalatSettingsDialog.NUM_OF_ROUNDS, ""+numOfRounds));
		anfangsWert = Integer.parseInt(customSettings.getProperty(WurstSettingsDialog.ANFANGS_WERT, "500"));
		for(int i=0; i<spielerZahl; i++){
			wurstPanel[i].setAnfangsWert(anfangsWert);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}
	
	public void nowVisible(){
		instance.changeBackground("/media/wurst/wurst.jpg");
	}

	public void openRoundDialog(String winner){
		games.dialogeGUIs.RoundDialog rd = new games.dialogeGUIs.RoundDialog(this,winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}
	
	public void openDetailsDialog(){
		StringBuffer message = new StringBuffer();
		for(int i=0; i<spielerZahl; i++){
			message.append(myPlayer[i].name+": "+abgabe[i]+"g; ");
		}
		EasyDialog.showMessage(message.toString());
	}
	
	public void openSettingsDialog(){
		instance.showDialog(new WurstSettingsDialog(this));
	}
}
