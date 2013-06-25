package games.wurst;

import games.Game;
import games.Modus;
import games.PC;
import gui.EasyDialog;

import java.awt.GridLayout;

import javax.swing.JPanel;

import player.Player;
import util.ChangeManager;

public class Wurst extends Game implements PC{
	private static final long serialVersionUID = 1L;
	private static String shortInfo = "Wer mehr auf die Waage bringt gewinnt - aber gib nicht alles auf einmal aus.";
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

	public Wurst(Player[] player, Modus modus) {
		super(gameName, player, defaultNumOfRounds, modus);
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
	public String getShortInfo() {
		return shortInfo;
	}

	@Override
	public void settingsChanged() {
		updateCreds();
		for(int i=0; i<spielerZahl; i++){
			wurstPanel[i].setAnfangsWert(anfangsWert);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}
	
	public void nowVisible(){
		instance.changeBackground("media/wurst/wurst.jpg");
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
