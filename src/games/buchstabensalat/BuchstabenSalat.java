package games.buchstabensalat;

import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.RoundDialog;
import gui.EasyDialog;
import gui.components.Countdown;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import player.Player;
import start.X;
import util.ChangeManager;
import util.SpielListen;

public class BuchstabenSalat extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Buchstabensalat";
	private static String shortInfo = "Behältst du den Überblick im Buchstabengewirr?";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);
	
	private static int defaultNumOfRounds = 5;
	int timeAfterBuzzer = 10;
	
	public static String getGameName(){
		return gameName;
	}

	private JPanel hauptbereichPanel;
	
	private Countdown countdown;
	private JLabel kategorieLabel;
	private JPanel salatPanel;
	private BuchstabenGewirrPanel buchstabenGewirrPanel;
	private LoesungsWortPanel loesungsWortPanel;
	
	int current = -1;
	public long timePerLetter = 3000;
	private boolean durchEnterBeendet = false;
	private Aufdecker aufdecker;
	protected int whoBuzzered;
	protected int numOfWords = 71;
	String[] kategorie = new String[numOfWords];
	String[] loesungswort = new String[numOfWords];
	{
		loesungswort[0] = "SCHLANGENBISS";
		kategorie[0] = "Unfall in der Zoohandlung";
		
		loesungswort[1] = "TAUBENSCHISS";
		kategorie[1] = "Unfall in der Zoohandlung";
		
		loesungswort[2] = "STRASSENBAHN";
		kategorie[2] = "Fortbewegungsmittel";
		
		loesungswort[3] = "SKATEBOARD";
		kategorie[3] = "Fortbewegungsmittel";
		
		loesungswort[4] = "HEISSLUFTBALLON";
		kategorie[4] = "Fortbewegungsmittel";
		
		loesungswort[5] = "ZEPPELIN";
		kategorie[5] = "Fortbewegungsmittel";
		
		loesungswort[6] = "KATAMARAN";
		kategorie[6] = "Fortbewegungsmittel";
		
		loesungswort[7] = "SEGELFLIEGER";
		kategorie[7] = "Fortbewegungsmittel";
		
		loesungswort[8] = "BUDAPEST";
		kategorie[8] = "Städte";
		
		loesungswort[9] = "PHILADELPHIA";
		kategorie[9] = "Städte";
		
		loesungswort[10] = "DÜSSELDORF";
		kategorie[10] = "Städte";
		
		loesungswort[11] = "MELBOURNE";
		kategorie[11] = "Städte";
		
		loesungswort[12] = "MARSEILLE";
		kategorie[12] = "Städte";
		
		loesungswort[13] = "CHICAGO";
		kategorie[13] = "Städte";
		
		loesungswort[14] = "AMSTERDAM";
		kategorie[14] = "Städte";
		
		loesungswort[15] = "STUTTGART";
		kategorie[15] = "Städte";
		
		loesungswort[16] = "MANCHESTER";
		kategorie[16] = "Städte";
		
		loesungswort[17] = "JOHANNESBURG";
		kategorie[17] = "Städte";
		
		loesungswort[18] = "BARCELONA";
		kategorie[18] = "Städte";
		
		loesungswort[19] = "WARSCHAU";
		kategorie[19] = "Städte";
		
		loesungswort[20] = "WÜRZBURG";
		kategorie[20] = "Städte";
		
		loesungswort[21] = "ISTANBUL";
		kategorie[21] = "Städte";
		
		loesungswort[22] = "WASHINGTON";
		kategorie[22] = "Städte";
		
		loesungswort[23] = "VALENCIA";
		kategorie[23] = "Städte";
		
		loesungswort[24] = "WELLINGTON";
		kategorie[24] = "Städte";
		
		loesungswort[25] = "STOCKHOLM";
		kategorie[25] = "Städte";
		
		loesungswort[26] = "KOPENHAGEN";
		kategorie[26] = "Städte";
		
		loesungswort[27] = "HAMBURG";
		kategorie[27] = "Städte";
		
		loesungswort[28] = "ANTANANARIVO";
		kategorie[28] = "Städte";
		
		loesungswort[29] = "MAILAND";
		kategorie[29] = "Städte";
		
		loesungswort[30] = "EDINBURGH";
		kategorie[30] = "Städte";
		
		loesungswort[31] = "JERUSALEM";
		kategorie[31] = "Städte";
		
		loesungswort[32] = "UNTERSEEBOOT";
		kategorie[32] = "Fortbewegungsmittel";
		
		loesungswort[33] = "ALEXANDER";
		kategorie[33] = "Namen";
		
		loesungswort[34] = "ALEXANDRA";
		kategorie[34] = "Namen";
		
		loesungswort[35] = "BARBARA";
		kategorie[35] = "Namen";
		
		loesungswort[36] = "BENJAMIN";
		kategorie[36] = "Namen";
		
		loesungswort[37] = "CLAUDIA";
		kategorie[37] = "Namen";
		
		loesungswort[38] = "DETLEF";
		kategorie[38] = "Namen";
		
		loesungswort[39] = "DOMINIK";
		kategorie[39] = "Namen";
		 
		loesungswort[40] = "ELISABETH";
		kategorie[40] = "Namen";
		
		loesungswort[41] = "EMILIA";
		kategorie[41] = "Namen";
		
		loesungswort[42] = "FLORIAN";
		kategorie[42] = "Namen";
		
		loesungswort[43] = "FRIEDRICH";
		kategorie[43] = "Namen";
		
		loesungswort[44] = "GABRIEL";
		kategorie[44] = "Namen";
		
		loesungswort[45] = "GERTRUD";
		kategorie[45] = "Namen";
		
		loesungswort[46] = "ANTONIA";
		kategorie[46] = "Namen";
		
		loesungswort[47] = "ISABEL";
		kategorie[47] = "Namen";
		
		loesungswort[48] = "JOHANNES";
		kategorie[48] = "Namen";
		
		loesungswort[49] = "KATHARINA";
		kategorie[49] = "Namen";
		
		loesungswort[50] = "LORENZ";
		kategorie[50] = "Namen";
		
		loesungswort[51] = "LEONARD";
		kategorie[51] = "Namen";
		
		loesungswort[52] = "MAXIMILIAN";
		kategorie[52] = "Namen";
		
		loesungswort[53] = "MARKUS";
		kategorie[53] = "Namen";
		
		loesungswort[54] = "NATALIE";
		kategorie[54] = "Namen";
		
		loesungswort[55] = "OTTMAR";
		kategorie[55] = "Namen";
		
		loesungswort[56] = "PATRICK";
		kategorie[56] = "Namen";
		
		loesungswort[57] = "RICHARD";
		kategorie[57] = "Namen";
		
		loesungswort[58] = "SUSANNE";
		kategorie[58] = "Namen";
		
		loesungswort[59] = "SEBASTIAN";
		kategorie[59] = "Namen";
		
		loesungswort[60] = "THORSTEN";
		kategorie[60] = "Namen";
		
		loesungswort[61] = "VANESSA";
		kategorie[61] = "Namen";
		
		loesungswort[62] = "VERONIKA";
		kategorie[62] = "Namen";
		
		loesungswort[63] = "WOLFGANG";
		kategorie[63] = "Namen";
		
		loesungswort[64] = "KONSTANTIN";
		kategorie[64] = "Namen";
	
		loesungswort[65] = "EIFFELTURM";
	    kategorie[65] = "Sehenswürdigkeiten";
	
	    loesungswort[66] = "FREIHEITSSTATUE";
		kategorie[66] = "Sehenswürdigkeiten";
		
		loesungswort[67] = "AKROPOLIS";
		kategorie[67] = "Sehenswürdigkeiten";
		
		loesungswort[68] = "SPHINX";
		kategorie[68] = "Sehenswürdigkeiten";
		
		loesungswort[69] = "KOLOSSEUM";
		kategorie[69] = "Sehenswürdigkeiten";
		
		loesungswort[70] = "STONEHENGE";
		kategorie[70] = "Sehenswürdigkeiten";
		
		
	}

	public BuchstabenSalat(Player[] player, Modus modus) {
		super(gameName, player, defaultNumOfRounds, modus);
		initGUI();
	}
	
	private void initGUI(){
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		spielBereichPanel.add(hauptbereichPanel);
		addKategorieLabel();
		addSalatPanel();
		addUntenPanel();
	}

	private void addKategorieLabel() {
		kategorieLabel = new JLabel("Kategorie");
		kategorieLabel.setFont(X.buttonFont);
		kategorieLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(kategorieLabel, BorderLayout.NORTH);
	}
	
	KeyListener buzzerKeyListener = new KeyAdapter() {			
		@Override
		public void keyReleased(KeyEvent e) {
			whoBuzzered = whoBuzz(e.getKeyCode());
			if(whoBuzzered == -1)return;
			else{
				aufdecker.interrupted = true;
				antwortTextField.setEditable(true);
				antwortTextField.requestFocus();
				countdown.start();
				playAudioFile(myPlayer[whoBuzzered].sound);
				playerLabel[whoBuzzered].setForeground(myPlayer[whoBuzzered].farbe);
			}
		}
	};
	private JPanel untenPanel;
	private JTextField antwortTextField;
	private String answer;
	boolean wortErraten = false;

	private void addSalatPanel() {
		salatPanel = new JPanel();
		salatPanel.setLayout(new GridLayout(2,1));
		hauptbereichPanel.add(salatPanel, BorderLayout.CENTER);
		buchstabenGewirrPanel = new BuchstabenGewirrPanel(this);
		buchstabenGewirrPanel.addKeyListener(buzzerKeyListener);
		salatPanel.add(buchstabenGewirrPanel);
		loesungsWortPanel = new LoesungsWortPanel(this);
		loesungsWortPanel.addKeyListener(buzzerKeyListener);
		salatPanel.add(loesungsWortPanel);
	}

	public void roundEnd() {
		answer = antwortTextField.getText().toUpperCase().trim();
		wortErraten = loesungswort[current].equalsIgnoreCase(answer);
		int winID;
		if(wortErraten){
			winID = whoBuzzered;
		}
		else{
			winID = whoBuzzered*(-1) -1;
		}
		verbuchePunkte(winID);
		winner = getWinnerText(winID);
		openRoundDialog(winner);
	}

	private String getWinnerText(int winID) {
		String winner = "";
		if(winID >= 0){
			winner = myPlayer[winID].name;
		}
		else{
			int looseID = (winID + 1)*(-1);
			int c=0;
			for(int i=0; i<spielerZahl; i++){
				if(i == looseID)continue;
				String trenner = "";
				if(c==spielerZahl-4)trenner = ", ";
				if(c==spielerZahl-3)trenner = " und ";
				winner += myPlayer[i].name+trenner;
				c++;
			}
		}
		return winner;
	}

	private void verbuchePunkte(int winID) {
		if(winID >= 0){
			creds[winID].earnsCredit(1);
			myPlayer[winID].gameCredit++;
		}
		else{
			int looseID = (winID + 1)*(-1);
			for(int i=0; i<spielerZahl; i++){
				if(i == looseID)continue;
				creds[i].earnsCredit(1);
				myPlayer[i].gameCredit++;
			}
		}
	}

	private void addUntenPanel() {
		untenPanel = new JPanel();
		untenPanel.setLayout(new GridLayout(2,1));
		hauptbereichPanel.add(untenPanel,BorderLayout.SOUTH);
		antwortTextField = new JTextField();
		antwortTextField.setFont(standardFont);
		untenPanel.add(antwortTextField);
		antwortTextField.setEditable(false);
		antwortTextField.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				durchEnterBeendet = true;
				countdown.stop();
				countdown.reset();
				roundEnd();
			}
		});
		countdown = new Countdown(timeAfterBuzzer);
		untenPanel.add(countdown, BorderLayout.SOUTH);
		countdown.addChangeManager(new ChangeManager(){
			@Override
			public void change() {
				if(!durchEnterBeendet) roundEnd();
			}
		});
	}
	
	private void nextRound(){
		if(modus == Modus.TEAM)changeActivePlayers();
		answer = "";
		wortErraten = false;
		durchEnterBeendet = false;
		antwortTextField.setEditable(false);
		current = nextRandom(numOfWords);
		if(current==-1){
			EasyDialog.showMessage("Es wurden alle vorhandenen Worte gespielt, das Spiel wird abgebrochen.");
			// TODO besser handhaben!
		}
		else{
		kategorieLabel.setText(kategorie[current]);
		buchstabenGewirrPanel.setWord(loesungswort[current]);
		antwortTextField.setText("");
		countdown.reset();
		loesungsWortPanel.setUnsichtbarWord(loesungswort[current]);
		aufdecker = new Aufdecker();
		new Thread(aufdecker).start();
		}
	}

	@Override
	public String getShortInfo() {
		return shortInfo;
	}

	@Override
	public int getGameID() {
		return SpielListen.BUCHSTABENSALAT;
	}

	@Override
	public void settingsChanged() {
		updateCreds();
		countdown.setSecs(timeAfterBuzzer);
	}

	@Override
	public void start() {
		nextRound();
	}
	
	public void nowVisible(){
		instance.changeBackground("media/buchstabensalat/letters.jpg");
	}
	
	class Aufdecker implements Runnable{
		boolean interrupted = false;
		int aufgedeckteBuchstaben = 0;
		@Override
		public void run() {
			loesungsWortPanel.requestFocus();
			try{Thread.sleep(timePerLetter);}catch(Exception e){};
			while(!interrupted){
				if(!(aufgedeckteBuchstaben<loesungswort[current].length()-2)){
					if(!interrupted) openRoundDialog("Keinen");
					return;
				}
				try{
					int position = buchstabenGewirrPanel.getPositionOf(aufgedeckteBuchstaben);
					String letter = buchstabenGewirrPanel.getLetterAt(aufgedeckteBuchstaben);
					buchstabenGewirrPanel.hideLetterAt(aufgedeckteBuchstaben);
					aufgedeckteBuchstaben++;
					loesungsWortPanel.setLetterAt(position, letter);
					loesungsWortPanel.requestFocus();
					Thread.sleep(timePerLetter);
				}
				catch(Exception e){
					
				}
			}
		}
	}
	
	@Override
	public void openRoundDialog(String winner){
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}
	
	@Override
	public void goBack() {
		if(!isOver())nextRound();
	}

	public String getScatteredWord() {
		return buchstabenGewirrPanel.getScatteredWord();
	}

	public String getAnswer() {
		return answer;
	}
	
	@Override
	public void openSettingsDialog(){
		instance.showDialog(new BuchstabenSalatSettingsDialog(this));
	}
	
	@Override
	public void openDetailsDialog(){
		instance.showDialog(new BuchstabenSalatDetailsDialog(this));
	}
	
	@Override
	public void abbruch(){
		countdown.stop();
		aufdecker.interrupted = true;
		super.abbruch();
	}
}
