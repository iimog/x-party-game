package games.ghosts;

import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.RoundDialog;
import gui.EasyDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import player.Player;
import util.InputListener;


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
public class Ghosts extends Game implements PC {
	private static final long serialVersionUID = -5623241990332411776L;
	public final static String gameName = "Geister";
	public static String getGameName(){
		return gameName;
	}

	public static int defaultNumOfRounds = 2;
	private Spielfeld spielfeld;
	private JPanel player0Panel;
	private JLabel[] playerLabel = new JLabel[2];
	private TotenAnzeige[] goodOnes = new TotenAnzeige[2];
	private TotenAnzeige[] badOnes = new TotenAnzeige[2];
	private JPanel player1Panel;

	public int phase = 0; // 0: gut böse Einstellung 1: Spiel
	public int whosTurn;

	private int winningReason;		// Hier wird der Grund für den letzten Sieg gespeichert
	final static int DURCH = 0;		// dazu dienen diese Konstanten... Guter Geist durchgebracht
	final static int ALL_GOOD = 1;	// Alle guten Geister des Gegners geschlagen
	final static int NO_BAD = 2;	// Selbst keine Bösen Geister mehr

	public Geist[][] geister = new Geist[2][8];
	public Geist selectedGeist;
	private JPanel buttonPanel;
	private JButton fertigButton;
	private Container hauptbereichPanel;
	
	public Ghosts(Player[] myPlayer, Modus modus, String background, int globalGameID) {
		this(myPlayer, defaultNumOfRounds, modus, background, globalGameID);
	}

	public Ghosts(Player[] player, int numOfRounds, Modus modus, String background, int globalGameID) {
		super(player, numOfRounds, modus, background, globalGameID);
		initGUI();
	}

	public void activateGeist(Geist g) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 8; j++) {
				geister[i][j].activated = (g == geister[i][j]);
			}
		}
	}

	public void buildGeister(){
		{
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 8; j++) {
					int a = 0;
					int b = 0;
					if (i == 0 && j < 4) {
						a = 4;
						b = j + 1;
					}
					if (i == 0 && j >= 4) {
						a = 5;
						b = j - 3;
					}
					if (i == 1 && j < 4) {
						a = 1;
						b = j + 1;
					}
					if (i == 1 && j >= 4) {
						a = 0;
						b = j - 3;
					}
					geister[i][j] = new Geist(i, new Point(b, a));
				}
			}}
	}

	public Geist checkField(Point p) {
		p.x = (int) Math.floor(p.x / 100);
		p.y = (int) Math.floor(p.y / 100);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 8; j++) {
				if (geister[i][j].position.equals(p)) {
					return geister[i][j];
				}
			}
		}
		return null;
	}

	private void fertigButtonActionPerformed(ActionEvent evt) {
		if (Geist.livingBad[whosTurn] == 4) {
			spielfeld.badGhosts.removeAll(spielfeld.badGhosts);
			spielfeld.repaint();
			playerChange();
			fertigButton.setEnabled(false);
			if (Geist.livingBad[whosTurn] == 4) {
				showMessage(
						"Ihr habt beide eure bösen Geister festgelegt."
						+ " Das Spiel startet jetzt!");
				phase = 1;
			}
		}
		repaint();
	}

	private void initGUI() {
		BorderLayout thisLayout = new BorderLayout();
		spielBereichPanel.setLayout(thisLayout);
		{
			hauptbereichPanel = new JPanel();
			BorderLayout hauptbereichPanelLayout = new BorderLayout();
			spielBereichPanel.add(hauptbereichPanel, BorderLayout.CENTER);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			{
				player0Panel = new JPanel();
				hauptbereichPanel.add(player0Panel, BorderLayout.SOUTH);
				GridLayout player0PanelLayout = new GridLayout(1,3);
				player0Panel.setLayout(player0PanelLayout);
				{
					goodOnes[0] = new TotenAnzeige(TotenAnzeige.GOOD);
					player0Panel.add(goodOnes[0]);
					playerLabel[0] = new JLabel();
					player0Panel.add(playerLabel[0]);
					playerLabel[0]
					            .setHorizontalAlignment(SwingConstants.CENTER);
					playerLabel[0].setFont(Game.PLAYER_FONT);
					playerLabel[0].setText(myPlayer[0].name);
					badOnes[0] = new TotenAnzeige(TotenAnzeige.BAD);
					player0Panel.add(badOnes[0]);
				}
			}
			{
				player1Panel = new JPanel();
				hauptbereichPanel.add(player1Panel, BorderLayout.NORTH);
				GridLayout player1PanelLayout = new GridLayout(1,3);
				player1Panel.setLayout(player1PanelLayout);
				{
					goodOnes[1] = new TotenAnzeige(TotenAnzeige.GOOD);
					player1Panel.add(goodOnes[1]);
					playerLabel[1] = new JLabel();
					player1Panel.add(playerLabel[1]);
					playerLabel[1]
					            .setHorizontalAlignment(SwingConstants.CENTER);
					playerLabel[1].setFont(Game.PLAYER_FONT);
					playerLabel[1].setText(myPlayer[1].name);
					badOnes[1] = new TotenAnzeige(TotenAnzeige.BAD);
					player1Panel.add(badOnes[1]);
				}
			}
			{
				spielfeld = new Spielfeld(this);
				hauptbereichPanel.add(spielfeld, BorderLayout.CENTER);
				spielfeld.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent evt) {
						spielfeldMouseReleased(evt);
					}
				});
			}
			updateCreds();
		}
		{
			buttonPanel = new JPanel();
			FlowLayout buttonPanelLayout = new FlowLayout();
			spielBereichPanel.add(buttonPanel, BorderLayout.NORTH);
			buttonPanel.setLayout(buttonPanelLayout);
			{
				fertigButton = new JButton();
				buttonPanel.add(fertigButton);
				fertigButton.setText("Fertig");
				fertigButton.setEnabled(false);
				fertigButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						fertigButtonActionPerformed(evt);
					}
				});
			}
		}
	}
	public void nextRound(){
		Geist.reset();
		buildGeister();
		spielfeld.repaint();
		if(modus == Modus.TEAM){
			changeActivePlayers();
		}
		phase = 0;
		goodOnes[0].reset();goodOnes[1].reset();
		badOnes[0].reset(); badOnes[1].reset();
	}

	@Override
	public void nowVisible(){
		instance.changeBackground("/media/ghosts/nebel.jpg");
	}

	@Override
	public void openDetailsDialog(){
		String text="";
		if(winningReason == DURCH){
			text = "Ein guter Geist von " + winner + " hat es auf die andere Seite geschafft.";
		}
		if(winningReason == NO_BAD){
			text = winner + " hat keine bösen Geister mehr.";
		}
		if(winningReason == ALL_GOOD){
			text = winner + " hat alle guten Geister erlegt.";
		}
		showMessage(text); // Verbessern
	}

	@Override
	public void openRoundDialog(String winner){
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}
	@Override
	public void openSettingsDialog(){
		instance.showDialog(new GhostsSettingsDialog(this));
	}
	private void playerChange() {
		playerLabel[whosTurn].setForeground(Color.black);
		whosTurn = 1 - whosTurn;
		playerLabel[whosTurn].setForeground(myPlayer[whosTurn].farbe);
	}
	public boolean roundEnd() {
		int winnerID = -1;
		if (Geist.livingBad[0] == 0){
			winnerID = 0; winningReason = NO_BAD;
		}
		if(Geist.livingGood[1] == 0) {
			winnerID = 0; winningReason = ALL_GOOD;
		}
		if (Geist.livingBad[1] == 0){
			winnerID = 1; winningReason = NO_BAD;
		}
		if(Geist.livingGood[0] == 0) {
			winnerID = 1; winningReason = ALL_GOOD;
		}
		Geist goo;
		for (int i=0; i<8; i++){
			goo = geister[whosTurn][i];
			if (whosTurn == 0){
				if((goo.position.equals(new Point(0,0))||goo.position.equals(new Point(5,0)))
						&& !goo.isBad()){
					winnerID = 0; winningReason = DURCH;
				}
			}
			if (whosTurn == 1){
				if((goo.position.equals(new Point(0,5))||goo.position.equals(new Point(5,5)))
						&& !goo.isBad()){
					winnerID = 1; winningReason = DURCH;
				}
			}
		}
		if(winnerID != -1){
			myPlayer[winnerID].gameCredit++;
			creds[winnerID].earnsCredit(1);
			winner = myPlayer[winnerID].name;
			return true;
		}
		return false;
	}
	@Override
	public void settingsChanged(){
		updateCreds();
	}
	private void spielfeldMouseReleased(MouseEvent evt) {
		Geist goo = checkField(evt.getPoint());
		Point punkt = new Point((int) Math.floor(evt.getPoint().x / 100),
				(int) Math.floor(evt.getPoint().y / 100));
		if (phase == 0) { // Geistauswahl Phase
			if (goo == null || goo.team != whosTurn) {
				return;
			}
			if (evt.getButton() == MouseEvent.BUTTON3
					|| evt.getButton() == MouseEvent.BUTTON2) {
				String good = "";
				if (goo.isBad())
					good = " bösen ";
				if (!goo.isBad())
					good = " guten ";
				selectedGeist = goo;
				EasyDialog.showInput("Gib den Tipp für diesen" + good + "Geist ein", goo.tipp, null, 
						new InputListener() {							
							@Override
							public void giveMeInput(String input) {
								selectedGeist.tipp = input;
								instance.closeDialog();
							}							
							@Override
							public void abgebrochen() {
								instance.closeDialog();
							}
						});
			} else {
				if (goo.isBad()) {
					goo.setBad(false);
					spielfeld.isNotBad(goo.position);
					fertigButton.setEnabled(false);
					repaint();
					return;
				}
				if (Geist.livingBad[whosTurn] == 4) {
					showMessage("Es dürfen nur 4 Geister böse sein. "
							+ "Du kannst Geister abwählen, indem du sie erneut klickst.");
					return;
				}
				goo.setBad(true);
				spielfeld.isBad(goo.position);
				if (Geist.livingBad[whosTurn] == 4)
					fertigButton.setEnabled(true);
			}
		}

		if (phase == 1) { // Spielablauf Phase
			if (evt.getButton() == MouseEvent.BUTTON3
					|| evt.getButton() == MouseEvent.BUTTON2) {
				if(goo!=null && goo.team==whosTurn)
					showMessage("Tipp: "+goo.tipp);
			} else {
				if (selectedGeist == null) { // Es wurde noch kein Geist gewählt
					if (goo == null || goo.team != whosTurn) {
						return;
					}
					if (goo.activated) {
						goo.activated = false;
						spielfeld.chosen = false;
						spielfeld.repaint();
						selectedGeist = null;
						repaint();
						return;
					}
					activateGeist(goo);
					selectedGeist = goo;
					spielfeld.fieldChosen(goo.position,
							myPlayer[whosTurn].farbe);
				} else { // Es wurde bereits ein Geist ausgewählt
					if (goo != null && goo.team == whosTurn) { // Auf dem Feld
						// steht ein
						// eigener Geist
						activateGeist(goo);
						selectedGeist = goo;
						spielfeld.fieldChosen(goo.position,
								myPlayer[whosTurn].farbe);
					} else {
						if (punkt.x > 5 || punkt.y > 5)
							return; // Kein gültiges Feld geklickt
						if ((Math.abs(punkt.x - selectedGeist.position.x) == 1 && punkt.y == selectedGeist.position.y)
								|| (Math
										.abs(punkt.y - selectedGeist.position.y) == 1 && punkt.x == selectedGeist.position.x))
							// Das geklickte Feld befindet sich horizontal oder
							// vertikal neben dem gewählten Geist
						{
							if (goo != null && goo.team == 1 - whosTurn) { // Ein fremder Geist steht auf dem
								// gewälten Feld --> schmeißen
								if(goo.isBad())badOnes[1-whosTurn].loose();
								if(!goo.isBad())goodOnes[1-whosTurn].loose();
								goo.die();
							}
							// Egal ob Feld frei oder mit fremdem Geist besetzt
							selectedGeist.position = punkt;
							selectedGeist.activated = false;
							selectedGeist = null;
							spielfeld.chosen = false;
							playerChange();
							spielfeld.repaint();
							if (roundEnd()){
								openRoundDialog(winner);
								nextRound();
							}
						}
					}
				}
			}
		}
		repaint();
	}
	@Override
	public void start() {
		whosTurn = getStartPlayerID(false);
		playerLabel[whosTurn].setForeground(myPlayer[whosTurn].farbe);
		playerLabel[1-whosTurn].setForeground(Color.black);
		String wessen="";
		if(myPlayer[whosTurn].male)wessen = "seine";
		else{wessen="ihre";}
		showMessage( myPlayer[whosTurn].name
				+ " darf beginnen "+ wessen +" vier bösen Geister auszuwählen.");
		nextRound();
		setVisible(true);
	}
}
