package games;

import games.dialogeGUIs.GameStartDialog;
import gui.Anzeige;
import gui.EasyDialog;
import gui.components.GameCredits;
import gui.components.JButtonIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import player.Team;
import settings.SettingsFileHandler;
import start.X;
import util.ConfirmListener;
import util.SpielListen;
import ablauf.MatchCredits;

@SuppressWarnings("serial")
public abstract class Game extends Anzeige {

	public static final Font STANDARD_FONT = new Font("Sergeo UI", 0, 16);
	public static final Font PLAYER_FONT = new java.awt.Font("Comic Sans MS",
			1, 26);
	public String gameName;
	public player.Player[] myPlayer;
	private Team[] myTeam = new Team[2];
	
	private int globalGameID;

	public int numOfRounds;
	public int spielerZahl;

	public String winner; // String für "beide"/"keiner"
	public int whosTurn;

	public Modus modus;

	public boolean stechen = false;

	public static final int[] standardBuzz = new int[4];
	{
		standardBuzz[0] = KeyEvent.VK_A;
		standardBuzz[1] = KeyEvent.VK_L;
		standardBuzz[2] = KeyEvent.VK_C;
		standardBuzz[3] = KeyEvent.VK_N;
	}
	public HashSet<Integer> schonWeg = new HashSet<Integer>(); // Hilfsstruktur
																// um doppelte
																// Fragen zu
																// vermeiden

	private ArrayList<games.GameListener> myListener = new ArrayList<games.GameListener>();

	public JPanel spielBereichPanel;
	public JLabel[] playerLabel;
	private JLabel[] matchCredLabel;
	private JPanel[] playerPanel;
	private JPanel playerBereichPanel;
	private JPanel controlPane;
	private JPanel menuPane;
	public GameCredits[] creds;
	private JButtonIcon quitButton;
	private JButton anleitungButton;
	private JLabel[] activePlayerLabel = new JLabel[2];
	private JPanel credLinksPanel;
	private JPanel credRechtsPanel;
	private JLabel pauseLabel;
	private JPanel centeredPanel;
	Properties customSettings;

	public Game(player.Player[] player, int numOfRounds,
			Modus modus, int globalGameID) {
		gameName = SpielListen.getSpieleMap().get(globalGameID).getGameName();
		myPlayer = player;
		if (modus == Modus.TEAM) {
			myTeam[0] = (Team) myPlayer[0];
			myTeam[1] = (Team) myPlayer[1];
		}
		this.modus = modus;
		this.numOfRounds = numOfRounds;
		this.globalGameID = globalGameID;
		spielerZahl = modus.getSpielerzahl();
		initVariablen();
		initGUI();
		instance.setGame(this);
		instance.changeAnzeige(this);
		instance.showDialog(new GameStartDialog(this));
	}

	private void initVariablen() {
		if (modus == Modus.SOLO)
			spielerZahl++;
		creds = new GameCredits[spielerZahl];
		playerLabel = new JLabel[spielerZahl];
		matchCredLabel = new JLabel[spielerZahl];
		playerPanel = new JPanel[spielerZahl];
		customSettings = SettingsFileHandler.loadSettings(gameName);
		loadProperties();
	}

	public void loadProperties() {
		// In Subklassen überschreiben	
	}

	private void initGUI() {
		initPauseLabel();
		this.setLayout(new BorderLayout());
		addCreds();
		{
			controlPane = new JPanel();
			controlPane.setBackground(Color.darkGray);
			this.add(controlPane, BorderLayout.SOUTH);
			{
				playerBereichPanel = new JPanel();
				GridLayout playerBereichPanelLayout = new GridLayout(1, 4);
				playerBereichPanelLayout.setHgap(5);
				playerBereichPanelLayout.setVgap(5);
				playerBereichPanelLayout.setColumns(spielerZahl);
				controlPane.add(playerBereichPanel);
				playerBereichPanel.setLayout(playerBereichPanelLayout);
				for (int i = 0; i < spielerZahl; i++) {
					playerPanel[i] = new JPanel();
					FlowLayout playerPanelLayout = new FlowLayout();
					playerBereichPanel.add(playerPanel[i]);
					playerBereichPanel.setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.RAISED));
					playerPanel[i].setLayout(playerPanelLayout);
					if (modus == Modus.TEAM) {
						activePlayerLabel[i] = new JLabel();
						activePlayerLabel[i].setText(myTeam[i].nextMember());
						activePlayerLabel[i].setFont(new java.awt.Font(
								"Comic Sans MS", 0, 20));
						playerPanel[i].add(activePlayerLabel[i]);
					}
					{
						playerLabel[i] = new JLabel();
						playerPanel[i].add(playerLabel[i]);
						playerLabel[i].setText(myPlayer[i].name);
						playerLabel[i].setFont(PLAYER_FONT);
						playerLabel[i]
								.setHorizontalAlignment(SwingConstants.CENTER);
					}
					{
						matchCredLabel[i] = new JLabel();
						playerPanel[i].add(matchCredLabel[i]);
						matchCredLabel[i].setText(myPlayer[i].matchCredit + "");
						matchCredLabel[i].setBorder(BorderFactory
								.createEtchedBorder(BevelBorder.LOWERED));
						matchCredLabel[i].setFont(PLAYER_FONT);
						matchCredLabel[i]
								.setHorizontalAlignment(SwingConstants.CENTER);
						matchCredLabel[i].setToolTipText("Match Punkte von "
								+ myPlayer[i].name);
					}
				}
			}
		}
		{
			menuPane = new JPanel();
			menuPane.setOpaque(false);
			FlowLayout menuPaneLayout = new FlowLayout();
			menuPaneLayout.setAlignment(FlowLayout.RIGHT);
			menuPane.setLayout(menuPaneLayout);
			this.add(menuPane, BorderLayout.NORTH);
			{
				anleitungButton = new JButtonIcon("media/ablauf/hilfe.png",
						"Anleitung");
				anleitungButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						anleitungButtonActionPerformed(evt);
					}
				});
				menuPane.add(anleitungButton);
			}
			{
				quitButton = new JButtonIcon("media/ablauf/quit.png", "Quit");
				quitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						quitButtonActionPerformed(evt);
					}
				});
				menuPane.add(quitButton);
			}
		}
		spielBereichPanel = new JPanel();
		spielBereichPanel.setOpaque(false);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		centeredPanel = new JPanel(gbl);
		centeredPanel.setOpaque(false);
		gbl.setConstraints(spielBereichPanel, gbc);
		centeredPanel.add(spielBereichPanel);
		spielBereichPanel.setLayout(new FlowLayout());
		this.add(centeredPanel, BorderLayout.CENTER);
	}

	private void initPauseLabel() {
		pauseLabel = new JLabel("PAUSE");
		pauseLabel.setFont(X.buttonFont.deriveFont(100f).deriveFont(Font.BOLD));
		pauseLabel.setForeground(Color.RED);
	}

	private boolean paused = false;
	private int automaticallyPaused = 0;
	protected boolean unstoppable = false;
	
	public void buzzeredBy(int whoBuzz){
		
	}

	public void togglePause() {
		if(unstoppable) return;
		automaticallyPaused = 0;
		if (isPaused() == false) {
			setPaused(true);
			pause();
		} else {
			setPaused(false);
			resume();
		}
	}

	public void autoPause() {
		if(unstoppable) return;
		if(automaticallyPaused > 0)
			automaticallyPaused++;
		else if(!isPaused()){
			pause();
			setPaused(true);
			automaticallyPaused = 1;
		}
	}
	
	public void autoResume() {
		if(automaticallyPaused > 0){			
			automaticallyPaused--;
			if(automaticallyPaused == 0){
				resume();
				setPaused(false);
			}
		}
	}
	
	public void pause() {
		centeredPanel.removeAll();
		centeredPanel.add(pauseLabel);
		revalidate();
		repaint();
	}

	public void resume() {
		centeredPanel.removeAll();
		centeredPanel.add(spielBereichPanel);
		revalidate();
		repaint();
	}

	private void addCreds() {
		credLinksPanel = new JPanel();
		credLinksPanel.setOpaque(false);
		int spaltenLinks = (spielerZahl > 2) ? 2 : 1;

		credLinksPanel.setLayout(new GridLayout(1, spaltenLinks));
		this.add(credLinksPanel, BorderLayout.WEST);
		credRechtsPanel = new JPanel();
		credRechtsPanel.setOpaque(false);
		int spaltenRechts = (spielerZahl > 3) ? 2 : 1;
		credRechtsPanel.setLayout(new GridLayout(1, spaltenRechts));
		this.add(credRechtsPanel, BorderLayout.EAST);
		for (int i = 0; i < spielerZahl; i++) {
			creds[i] = new GameCredits(numOfRounds, myPlayer[i].farbe);
			if (i > 1 || (i == 1 && spielerZahl == 2)) {
				credRechtsPanel.add(creds[i]);
			} else {
				credLinksPanel.add(creds[i]);
			}
		}
	}

	public void abbruch() {
		// TODO
		// Methode die einen Vorzeitigen Spielabbruch handled
		// z.B. Wenn keine Fragen mehr zur Verfügung stehen
		ended();
	};

	public void addGameListener(games.GameListener gl) {
		myListener.add(gl);
	}

	protected void anleitungButtonActionPerformed(ActionEvent evt) {
		getAnleitung();
	}

	public void changeActivePlayers() {
		if (modus != Modus.TEAM)
			return;
		activePlayerLabel[0].setText(myTeam[0].nextMember());
		activePlayerLabel[1].setText(myTeam[1].nextMember());
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	public void ended() {
		Iterator<games.GameListener> i = myListener.iterator();
		while (i.hasNext()) {
			i.next().gameOver();
		}
	}

	public void gameEnd() {
		// TODO
		// Methode, die das Spielende handhabt
		ended();
	}

	public void getAnleitung() {
		try {
			File anleitungFile = new File(X.getMainDir() + "anleitungen/"
					+ gameName + ".html");
			Desktop.getDesktop().open(anleitungFile);
		} catch (Exception e) {
			showMessage("Für dieses Spiel ist leider noch keine Anleitung verfügbar");
		}
	}

	public int getStartPlayerID(boolean inform) {
		Random r = new Random();
		int next = r.nextInt(spielerZahl);
		if (inform) {
			showMessage("Diesmal darf " + myPlayer[next].name + " beginnen.");
		}
		whosTurn = next;
		hebeAktivenSpielerHervor();
		return next;
	}

	public void goBack() {

	}

	public boolean isOver() {
		int tempSpielerZahl = spielerZahl;
		if (modus == Modus.SOLO) {
			tempSpielerZahl = 2;
		}
		boolean keinerFertig = true;
		int hoechstePunktzahl = 0;
		int hoechstePunktzahlZaehler = 0;
		for (int i = 0; i < tempSpielerZahl; i++) {
			if (myPlayer[i].gameCredit >= numOfRounds) {
				keinerFertig = false;
				if (myPlayer[i].gameCredit == hoechstePunktzahl) {
					hoechstePunktzahlZaehler++;
				} else if (myPlayer[i].gameCredit > hoechstePunktzahl) {
					hoechstePunktzahl = myPlayer[i].gameCredit;
					hoechstePunktzahlZaehler = 1;
				}
			}
		}
		if (keinerFertig) {
			return false;
		}
		if (hoechstePunktzahlZaehler > 1) {
			stechen = true;
			return false;
		} else {
			return true;
		}
	}

	// Diese Methode gibt zufällig den Index der nächsten Frage zurück
	// und prüft, dass keine Frage doppelt gestellt wird. Falls keine Fragen
	// mehr
	// übrig sind wird -1 zurück gegeben
	public int nextRandom(int numOfQuests) {
		if (schonWeg.size() == numOfQuests) {
			return -1;
		} else {
			Random r = new Random();
			int ret = r.nextInt(numOfQuests);
			while (schonWeg.add(ret) == false) {
				ret = r.nextInt(numOfQuests);
			}
			return ret;
		}
	}

	public void openDetailsDialog() {

	}

	public void openInfoDialog() {

	}

	public void openRoundDialog(String winner) {

	}

	public void openSettingsDialog() {
		showMessage("Für dieses Spiel gibt es (noch) keinen Einstellungsdialog");
	}

	public void playAudioFile(String filename) {
		X.playAudioFile(filename);
	}

	public void quitButtonActionPerformed(ActionEvent evt) {
		schliessen();
	}

	public void schliessen() {
		autoPause();
		EasyDialog.showConfirm("Das Minispiel \'" + this.gameName
				+ "\' wird vorzeitig beendet, soll es mit dem "
				+ "aktuellen Spielstand gewertet werden?", null,
				new ConfirmListener() {
					@Override
					public void confirmOptionPerformed(int optionType) {
						if (optionType == ConfirmListener.YES_OPTION) {
							if (unentschieden()) {
								for (int i = 0; i < spielerZahl; i++) {
									myPlayer[i].gameCredit = MatchCredits.UNENTSCHIEDEN;
								}
							}
							abbruch();
						}
						else if (optionType == ConfirmListener.NO_OPTION) {
							for (int i = 0; i < spielerZahl; i++) {
								myPlayer[i].gameCredit = 0;
							}
							abbruch();
						}
						else{
							autoResume();
						}
						instance.closeDialog();
					}
				});
	}

	protected boolean unentschieden() {
		boolean unentschieden = false;
		int winnerID = -1;
		for (int i = 0; i < myPlayer.length; i++) {
			boolean alleinigerSieger = true;
			for (int j = 0; j < myPlayer.length; j++) {
				if (i != j && myPlayer[j].gameCredit >= myPlayer[i].gameCredit) {
					alleinigerSieger = false;
				}
			}
			if (alleinigerSieger) {
				winnerID = i;
				break;
			}
		}
		if (winnerID == -1)
			unentschieden = true;
		return unentschieden;
	}

	public abstract void settingsChanged();

	public void showMessage(String message) {
		EasyDialog.showMessage(message);
	}

	public abstract void start();

	public boolean stechen() {
		return stechen;
	}

	public void turnOver() {
		playerLabel[whosTurn].setForeground(Color.black);
		playerLabel[whosTurn].setBorder(null);
		if (modus == Modus.TEAM) {
			activePlayerLabel[whosTurn].setText(myTeam[whosTurn].nextMember());
		}
		whosTurn = (whosTurn + 1) % spielerZahl;
		hebeAktivenSpielerHervor();
	}

	private void hebeAktivenSpielerHervor() {
		playerLabel[whosTurn].setForeground(myPlayer[whosTurn].farbe);
		playerLabel[whosTurn].setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * Ermittelt wer den Buzzer gedrückt hat, liefert die SpielerID oder -1
	 * falls die Taste keine Buzzertaste ist.
	 * 
	 * @return SpielerID oder -1
	 */
	public int whoBuzz(int keyCode) {
		for (int i = 0; i < spielerZahl; i++) {
			if (keyCode == myPlayer[i].getKey()) {
				return i;
			}
		}
		return -1;
	}

	protected void updateCreds() {
		for (int i = 0; i < spielerZahl; i++) {
			creds[i].setNumOfRounds(numOfRounds);
		}
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public int getGlobalGameID() {
		return globalGameID;
	}
	
	public Properties getCustomSettings(){
		return customSettings;
	}
}
