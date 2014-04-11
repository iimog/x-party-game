package games.sautreiber;

import games.Game;
import games.Modus;
import games.PC;
import gui.components.Dice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import player.Player;
import util.ChangeManager;

// TODO Alle Würfel verbraucht
/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SauTreiber extends Game implements PC {
	private static final long serialVersionUID = 768210783997815674L;
	static String gameName = "Säutreiberspiel";
	public static String getGameName(){
		return gameName;
	}
	public static int defaultNumOfRounds = 3;		// !!Wird im Constructor mit 1000 multipliziert
	private JPanel hauptbereichPanel;
	private JLabel[] roundCred;
	private JButton aufhoerenButton;
	private JButton weiterButton;
	private JPanel buttonPanel;
	private JPanel anzeigePanel;
	private JPanel dicePanel;
	private JLabel[] playerCred;
	private Dice[] wuerfel = new Dice[6];
	private boolean wuerfelbar = true;
	private int roundCredit = 0;
	private boolean wasRaus = false;
	int[] haeufigkeit = new int[6]; // gibt für jede Zahl die Häufigkeit im
	// aktuellen Wurf an
	boolean[] mehrAlsDrei = new boolean[6];
	boolean analyzed = false;
	boolean bestaetig = false;
	int bestaetigCredit = 0;
	int bestaetigCount = 0;
	boolean strasse = false;
	public SauTreiber(Player[] myPlayer, Modus modus, int globalGameID) {
		this(myPlayer, defaultNumOfRounds, modus, globalGameID);
	}

	public SauTreiber(player.Player[] pl, int numOfRounds, Modus modus, int globalGameID) {
		super(pl, numOfRounds * 1000, modus, globalGameID);
		initElements();
		{
			hauptbereichPanel = new JPanel();
			BorderLayout hauptbereichPanelLayout = new BorderLayout();
			spielBereichPanel.add(hauptbereichPanel);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			hauptbereichPanel.setBounds(48, 0, 10, 10);
			{
				buttonPanel = new JPanel();
				buttonPanel.setLayout(new GridLayout(1,2));
				hauptbereichPanel.add(buttonPanel,BorderLayout.CENTER);
				{
					weiterButton = new JButton();
					buttonPanel.add(weiterButton);
					weiterButton.setText("Weiter");
					weiterButton.setEnabled(false);
					weiterButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							weiterButtonActionPerformed(evt);
						}
					});
				}
				{
					aufhoerenButton = new JButton();
					buttonPanel.add(aufhoerenButton);
					aufhoerenButton.setText("Verloren?");
					aufhoerenButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							aufhoerenButtonActionPerformed(evt);
						}
					});
				}
			}
			{
				anzeigePanel = new JPanel();
				GridLayout anzeigePanelLayout = new GridLayout(2, spielerZahl);
				anzeigePanelLayout.setHgap(5);
				anzeigePanelLayout.setVgap(5);
				hauptbereichPanel.add(anzeigePanel, BorderLayout.SOUTH);
				anzeigePanel.setLayout(anzeigePanelLayout);
				
				for(int i=0; i<spielerZahl; i++){
					roundCred[i] = new JLabel();
					roundCred[i].setHorizontalAlignment(SwingConstants.CENTER);
					anzeigePanel.add(roundCred[i]);
					roundCred[i].setText("0");
				}
				for(int i=0; i<spielerZahl; i++){
					playerCred[i] = new JLabel();
					anzeigePanel.add(playerCred[i]);
					playerCred[i].setText("0");
					playerCred[i].setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			{
				dicePanel = new JPanel();
				GridLayout dicePanelLayout = new GridLayout(1, 6);
				dicePanelLayout.setHgap(5);
				dicePanelLayout.setVgap(5);
				dicePanelLayout.setColumns(6);
				hauptbereichPanel.add(dicePanel, BorderLayout.NORTH);
				dicePanel.setLayout(dicePanelLayout);
				{
					ActionListener al = new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Dice d = (Dice) e.getSource();
							gewuerfelt(d);
						}
					};
					ChangeManager cm = new ChangeManager() {
						@Override
						public void change() {
							wurfAnalyzer();
						}
					};
					for (int i = 0; i < 6; i++) {
						int a = 180 + 10 * i;
						wuerfel[i] = new Dice(new Color(a, a, a));
						dicePanel.add(wuerfel[i]);
						wuerfel[i].addActionListener(al);
						wuerfel[i].setSelbstausloeser(false);
						wuerfel[i].addChangeManager(cm);
					}
				}
			}
		}
		for(int i=0; i<spielerZahl; i++){
			creds[i].setNumOfRounds(this.numOfRounds/500);
			creds[i].multiply(500);
		}
	}

	private void initElements() {
		roundCred = new JLabel[spielerZahl];
		playerCred = new JLabel[spielerZahl];
	}

	private void acreditation(Dice d){
		if(d.rolls())return;
		if (strasse){
			strasse = false;
			roundCredit += 2000;
			if(bestaetig)bestaetigCredit += 2000;
			for (int i = 0; i < 6; i++) {
				wuerfel[i].disable(true);
			}
			showMessage( "Große Straße --> 2000Punkte aber: ...Bestätigen!");
			// bestätigen();
			wasRaus = true;
			weiterButton.setEnabled(true);
			aufhoerenButton.setText("Aufhören");
			roundCred[whosTurn].setText(roundCredit+"");
		}
		else{
			if (d.getZahl()==0)return;
			if (mehrAlsDrei[d.getZahl() - 1]) {
				d.setBackground(myPlayer[whosTurn].farbe);
				d.disable(true);
				if (d.getZahl()==1) {
					roundCredit += d.getZahl() * 1000;
					if(bestaetig)bestaetigCredit += d.getZahl() * 1000;
				} else {
					roundCredit += d.getZahl() * 100;
					if(bestaetig)bestaetigCredit += d.getZahl() * 100;
				}
				int a = 0;
				for (int i = 0; i < 6; i++) {
					if (a >= 2)
						break;
					if (!wuerfel[i].isDisabled()
							&& wuerfel[i].getZahl()==d.getZahl()) {
						wuerfel[i].setBackground(myPlayer[whosTurn].farbe);
						wuerfel[i].disable(true);
						a++;
					}

				}
				wasRaus = true;
				weiterButton.setEnabled(true);
				aufhoerenButton.setText("Aufhören");
				roundCred[whosTurn].setText(roundCredit+"");
				if(haeufigkeit[d.getZahl() - 1]<6){	// gilt nur wenn nicht 6 gleiche Zahlen
					mehrAlsDrei[d.getZahl() - 1] = false;
				}
			} else {
				if (d.getZahl() == 1 || d.getZahl()==5) {
					d.setBackground(myPlayer[whosTurn].farbe);
					d.disable(true);
					if (d.getZahl()==1){
						roundCredit += 100;
						if(bestaetig)bestaetigCredit += 100;
					}
					if (d.getZahl()==5){
						if(haeufigkeit[5-1]==2){
							roundCredit+=50;
							if(bestaetig)bestaetigCredit += 50;
							for (int i = 0; i < 6; i++) {
								if (!wuerfel[i].isDisabled()
										&& wuerfel[i].getZahl()==5) {
									wuerfel[i].setZahl(0);
								}}
							d.setZahl(1);
						}
						roundCredit += 50;
						if(bestaetig)bestaetigCredit += 50;
					}
					wasRaus = true;
					weiterButton.setEnabled(true);
					aufhoerenButton.setText("Aufhören");
					roundCred[whosTurn].setText(roundCredit+"");
				}
			}
		}
		if(bestaetig && bestaetigCount<=3 && bestaetigCredit>=350){
			showMessage( "Bestätigung erfolgreich");
			bestaetig = false;
		}

		if(bestaetig && bestaetigCount<3 && bestaetigCredit<350){
			aufhoerenButton.setText("Verloren?");
		}
	}

	private void aufhoerenButtonActionPerformed(ActionEvent evt) {
		if(bestaetig && bestaetigCount==3 && bestaetigCredit<350){
			showMessage( "Bestätigung fehlgeschlagen");
		}
		if (wasRaus && !bestaetig) {
			myPlayer[whosTurn].gameCredit += roundCredit;
			creds[whosTurn].earnsCredit(roundCredit);
			playerCred[whosTurn].setText("" + myPlayer[whosTurn].gameCredit);
		}
		else{
			playAudioFile("/media/sounds/pig.wav");
		}
		bestaetig = false;
		if (isOver()) {
			ended();
			return;
		}
		roundCred[whosTurn].setText("0");
		turnOver();
		for (int i = 0; i < 6; i++) {
			int a = 180 + 10 * i;
			wuerfel[i].setBackground(new Color(a, a, a));
			wuerfel[i].disable(false);

		}
		wuerfelbar = true;
		roundCredit = 0;
		analyzed = false;
		weiterButton.setEnabled(false);
	}

	private void bestaetigen() {
		for (int i = 0; i < 6; i++) {
			if (!wuerfel[i].isDisabled())
				return;
		}
		showMessage( "Du musst jetzt in höchstens "
				+ "drei Würfen mindestens 350 Punkte holen");
		bestaetig = true;
		bestaetigCredit = 0;
		bestaetigCount = 0;
		for (int i = 0; i < 6; i++) {
			int a = 180 + 10 * i;
			wuerfel[i].setBackground(new Color(a, a, a));
			wuerfel[i].disable(false);
		}
		wuerfelbar = true;
		analyzed = false;
	}

	private void gewuerfelt(Dice d) {
		if (wuerfelbar) {
			wuerfeln();
			if(bestaetig)bestaetigCount++;
			wuerfelbar = false;
		} else {
			acreditation(d);
			bestaetigen();
		}
	}

	@Override
	public void nowVisible(){
		instance.changeBackground("/media/säutreiber/3_schweine.jpg");
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new SauTreiberSettingsDialog(this));
	}

	@Override
	public void settingsChanged(){
		for(int i=0; i<spielerZahl; i++){
			creds[i].setNumOfRounds(numOfRounds/500);
			creds[i].multiply(500);
		}
	}

	@Override
	public void start(){
		whosTurn = getStartPlayerID(true);
	}

	private void test(){
		int activeDic=0;		// Würfel die wirklich noch aktiv sind
		int verwDic=0;		// Anzahl der Würfel durch Summe der Häufigkeiten
		for(int i=0; i<6; i++){
			if(!wuerfel[i].isDisabled())activeDic++;
			verwDic += haeufigkeit[i];
		}
		if(activeDic!=verwDic){
			System.out.println("Da stimmt was nicht: scheinbar aktive Würfel: "+verwDic+" (wirklich: " + activeDic +")");
			analyzed = false;
			wurfAnalyzer();
		}
	}
	private void weiterButtonActionPerformed(ActionEvent evt) {
		if(bestaetig && bestaetigCount==3 && bestaetigCredit<350){
			showMessage("Bestätigung fehlgeschlagen");
			bestaetigCount++; // Verhindert doppeltes Anzeigen des fehlgeschlagen Dialogs
			aufhoerenButtonActionPerformed(evt);
			return;
		}
		wuerfelbar = true;
		wasRaus = false;
		weiterButton.setEnabled(false);
		aufhoerenButton.setText("Verloren?");
		analyzed = false;
		for(int i=0; i<6; i++){
			if(wuerfel[i].isEnabled()){
				wuerfel[i].doClick();
				return;
			}
		}
	}
	private void wurfAnalyzer() {
		if (analyzed)
			return;
		analyzed = true;
		for (int i = 0; i < 6; i++) {
			haeufigkeit[i] = 0;
			mehrAlsDrei[i] = false;
		}
		for (int i = 0; i < 6; i++) {
			if (!wuerfel[i].isDisabled()) {
				haeufigkeit[wuerfel[i].getZahl() - 1]++;
			}
		}
		strasse = true;
		String s = "";						// zum testen
		for (int i = 0; i < 6; i++) {
			if (haeufigkeit[i] >= 3) {
				mehrAlsDrei[i] = true;
			}
			if (haeufigkeit[i] != 1){
				strasse = false;
			}
			s = s + haeufigkeit[i] + "; "; 	// zum testen
		}
		System.out.println(s);				// zum testen
		test();
	}
	private void wuerfeln() {
		for (int i = 0; i < 6; i++) {
			wuerfel[i].rollTheDice();
		}
	}
}
