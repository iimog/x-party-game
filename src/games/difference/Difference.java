package games.difference;
import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.RoundDialog;
import gui.EasyDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import util.ConfirmListener;
import ablauf.MatchCredits;

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
public class Difference extends Game implements PC {
	private static final long serialVersionUID = -6078006638537351232L;
	public static String gameName = "Fehlersuche";
	public static String getGameName(){
		return gameName;
	}
	private JPanel darstellungPanel;
	private JPanel schaltflaechenPanel;
	private gui.components.Countdown count;
	private int cdTime = 5;
	public int getCdTime() {
		return cdTime;
	}
	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}
	boolean bildAusblenden = true;
	private JButton nextPicButton;
	private boolean buzzerable = true;		// Doppelbuzzern verhindern
	private int current;		// Zufallszahl für aktuelles Bild
	int last;
	Point lastClick;
	public int whoBuzz = -1;		// Wer hat gebuzzerd? 0 für Player1, 1 für Player2
	int lastBuzz;
	boolean clickable = false; 	// Soll der Klick analysiert werden? true durch buzzern
	// false nach klick
	public String winner;		// Gewinner der letzten Runde
	private long start;
	long dauer;
	int okDist = 15;		// Zulässige Entfernung vom Fehlermittelpunkt

	int distance;
	private String path = "media/difference/";			// Standardpfad
	private int numOfPics = 23;							// Anzahl der Fehlerbilder
	private String[] pics = new String[numOfPics];		// Dateiname der richtigen Bilder
	String[] ePics = new String[numOfPics];		// Dateiname der Fehlerbilder
	Point[] coords = new Point[numOfPics];		// Ort des Fehlers
	private gui.components.Bildschirm rightPic;
	private gui.components.Bildschirm wrongPic;
	private List<Integer> uebersprungeneBilder;
	public boolean timeOver;
	{
		pics[0] = path + "villa2.jpg";
		ePics[0] = path + "villa2E.jpg";
		coords[0] = new Point(3,250);

		pics[1] = path + "simpson1.png";
		ePics[1] = path + "simpson1E.png";
		coords[1] = new Point(129,259);

		pics[2] = path + "simpson2.png";
		ePics[2] = path + "simpson2E.png";
		coords[2] = new Point(33,209);

		pics[3] = path + "africa.png";
		ePics[3] = path + "africaE.png";
		coords[3] = new Point(268,253);

		pics[4] = path + "schule.jpg";
		ePics[4] = path + "schuleE.jpg";
		coords[4] = new Point(121,171);

		pics[5] = path + "baer.png";
		ePics[5] = path + "baerE.png";
		coords[5] = new Point(269,165);

		pics[6] = path + "teddy.png";
		ePics[6] = path + "teddyE.png";
		coords[6] = new Point(164,286);

		pics[7] = path + "schmetterling.png";
		ePics[7] = path + "schmetterlingE.png";
		coords[7] = new Point(302,249);

		pics[8] = path + "flugzeug.png";
		ePics[8] = path + "flugzeugE.png";
		coords[8] = new Point(222,89);

		pics[9] = path + "mandala.png";
		ePics[9] = path + "mandalaE.png";
		coords[9] = new Point(88,191);

		pics[10] = path + "schiff.png";
		ePics[10] = path + "schiffE.png";
		coords[10] = new Point(54,89);

		pics[11] = path + "2kaengurus.png";
		ePics[11] = path + "2kaengurusE.jpg";
		coords[11] = new Point(90,81);

		pics[12] = path + "fehlerteufel.png";
		ePics[12] = path + "fehlerteufelE.png";
		coords[12] = new Point(134,61);

		pics[13] = path + "kaenguru.png";
		ePics[13] = path + "kaenguruE.png";
		coords[13] = new Point(33,321);

		pics[14] = path + "moderne.png";
		ePics[14] = path + "moderneE.jpg";
		coords[14] = new Point(226,303);

		pics[15] = path + "obstschale.jpg";
		ePics[15] = path + "obstschaleE.png";
		coords[15] = new Point(234,113);

		pics[16] = path + "deutschlandkarte.png";
		ePics[16] = path + "deutschlandkarteE.png";
		coords[16] = new Point(182,36);

		pics[17] = path + "model.jpg";
		ePics[17] = path + "modelE.png";
		coords[17] = new Point(61,173);

		pics[18] = path + "urlaub_palme1.jpg";
		ePics[18] = path + "urlaub_palme.jpg";
		coords[18] = new Point(89,238);

		pics[19] = path + "218.jpg";
		ePics[19] = path + "2181.jpg";
		coords[19] = new Point(142,164);

		pics[20] = path + "villa.jpg";
		ePics[20] = path + "villaE.jpg";
		coords[20] = new Point(220,77);

		pics[21] = path + "blumen.jpg";
		ePics[21] = path + "blumenE.jpg";
		coords[21] = new Point(38,122);

		pics[22] = path + "serpentine.jpg";
		ePics[22] = path + "serpentineE.jpg";
		coords[22] = new Point(277,47);
	}

	public Difference(Player[] player, Modus modus, int globalGameID){
		this(player, 5, modus, globalGameID);
	}

	public Difference(Player[] player, int numOfRounds, Modus modus, int globalGameID) {
		super(gameName, player, numOfRounds, modus, globalGameID);
		this.numOfRounds = numOfRounds;
		uebersprungeneBilder = Collections.synchronizedList(new ArrayList<Integer>());
		current = nextRandom(numOfPics);
		if(modus == Modus.SOLO){
			cdTime = 30;
		}
		initGUI();
	}

	@Override
	public void buzzeredBy(int player){
		if(!buzzerable)return;
		unstoppable = true;
		clickable = true;
		buzzerable = false;		// am Rundenende wieder buzzerable setzen
		if(modus == Modus.SOLO){
			wrongPic.hidePic(false);
		}
		else{
			playAudioFile(myPlayer[player].sound);
			if(bildAusblenden)rightPic.hidePic(true);
		}
		super.playerLabel[player].setForeground(myPlayer[player].farbe);
		count.start();
		whoBuzz = player;
		start = System.currentTimeMillis();
	}

	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				spielBereichPanel.setLayout(thisLayout);
				{
					darstellungPanel = new JPanel();
					GridLayout darstellungPanelLayout = new GridLayout(1, 2);
					darstellungPanelLayout.setHgap(5);
					darstellungPanelLayout.setVgap(5);
					darstellungPanelLayout.setColumns(2);
					spielBereichPanel.add(darstellungPanel, BorderLayout.CENTER);
					darstellungPanel.setLayout(darstellungPanelLayout);
					{
						rightPic = new gui.components.Bildschirm(pics[current]);
						darstellungPanel.add(rightPic);
					}
					{
						wrongPic = new gui.components.Bildschirm(ePics[current]);
						darstellungPanel.add(wrongPic);
						if(modus == Modus.SOLO)wrongPic.hidePic(true);
						wrongPic.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseReleased(MouseEvent evt) {
								wrongPicMouseReleased(evt);
							}
						});
					}
				}
				{
					schaltflaechenPanel = new JPanel();
					GridLayout schaltflaechenPanelLayout = new GridLayout(2, 1);
					schaltflaechenPanelLayout.setVgap(5);
					schaltflaechenPanelLayout.setColumns(1);
					schaltflaechenPanelLayout.setRows(2);
					spielBereichPanel.add(schaltflaechenPanel, BorderLayout.SOUTH);
					schaltflaechenPanel.setLayout(schaltflaechenPanelLayout);
					class Changer implements util.ChangeManager{
						public void change(){
							timeOver = true;
							roundEnd();
						}
					}
					{
						count = new gui.components.Countdown(cdTime);
						schaltflaechenPanel.add(count);
						count.addChangeManager(new Changer());
					}
					{
						nextPicButton = new JButton();
						schaltflaechenPanel.add(nextPicButton);
						nextPicButton.setText("Nächstes Bild");
						nextPicButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								nextPicButtonActionPerformed(evt);
							}
						});
						nextPicButton.requestFocus();
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void nextPicButtonActionPerformed(ActionEvent evt) {
		uebersprungeneBilder.add(current);
		if(buzzerable){
			if(!isOver())nextRound();
		}
	}

	private void nextRound(){
		timeOver = false;
		unstoppable = false;
		last = current;
		current = nextRandom(numOfPics);
		if(current==-1){
			if(uebersprungeneBilder.size()==0){
				EasyDialog.showConfirm("Es sind leider keine Fehlerbilder mehr vorhanden, " +
						"das Spiel endet daher. Soll es mit dem aktuellen Spielstand gewertet werden?", 
						null, new ConfirmListener() {
							@Override
							public void confirmOptionPerformed(int optionType) {
								if(optionType == ConfirmListener.YES_OPTION){
									if(unentschieden()){
										for(int i=0; i<spielerZahl; i++){
											myPlayer[i].gameCredit = MatchCredits.UNENTSCHIEDEN;
										}
									}
									abbruch();							
								}
								else{
									for(int i=0; i<spielerZahl; i++){
										myPlayer[i].gameCredit = 0;
									}
									abbruch();
								}
								instance.closeDialog();
							}
						});
			}
			showMessage("Es sind leider keine Fehlerbilder mehr vorhanden, die übersprungenen werden jetzt wiederholt.");
			schonWeg.removeAll(uebersprungeneBilder);
			uebersprungeneBilder.removeAll(uebersprungeneBilder);
		}
		else{
			if(modus == Modus.TEAM){
				changeActivePlayers();
			}
			rightPic.changePic(pics[current]);
			wrongPic.changePic(ePics[current]);
			if(modus == Modus.SOLO){
				wrongPic.hidePic(true);
			}
			rightPic.setSize(rightPic.getPreferredSize());
			wrongPic.setSize(wrongPic.getPreferredSize());
			revalidate();
			repaint();
			buzzerable = true;
			count.stop();
			for(JLabel l : super.playerLabel){
				l.setForeground(Color.black);
			}
		}
	}

	@Override
	public void nowVisible(){
		if(nextPicButton!=null){
			nextPicButton.requestFocus();
		}
		instance.changeBackground("media/difference/blue.jpg");
	}

	@Override
	public void openDetailsDialog(){
		instance.showDialog(new DifferenceDetailsDialog(this));
	}

	@Override
	public void openInfoDialog(){
		//new games.InfoDialog(this, info[last]);
	}

	@Override
	public void openRoundDialog(String winner){
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new DifferenceSettingsDialog(this));
	}
	private void roundEnd(){
		if(whoBuzz>=0 && whoBuzz<=spielerZahl){
			dauer = System.currentTimeMillis()- start;
			int winID;
			if(distance<okDist && !timeOver){
				winID = whoBuzz;
			}
			else{
				winID = whoBuzz*(-1) -1;
				if(timeOver){
					lastClick=new Point(-10,-10);
					distance = -1;
				}
			}
			verbuchePunkte(winID);
			winner = getWinnerText(winID);
			lastBuzz = whoBuzz;
			whoBuzz = -1;
			openRoundDialog(winner);
			nextRound();
		}
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

	@Override
	public void settingsChanged(){
		updateCreds();
		count.setSecs(cdTime);
	}
	@Override
	public void start(){
	}

	private void wrongPicMouseReleased(MouseEvent evt) {
		if(!clickable)return;
		clickable = false;
		lastClick = evt.getPoint();
		distance = (int)Math.round(lastClick.distance(coords[current]));
		roundEnd();
	}
	@Override
	public void abbruch(){
		count.stop();
		super.abbruch();
	}
}
