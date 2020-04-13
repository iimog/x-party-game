package games;

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
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import ablauf.MatchCredits;
import games.dialogeGUIs.GameSettingsDialog;
import games.dialogeGUIs.GameStartDialog;
import gui.Anzeige;
import gui.EasyDialog;
import gui.components.GameCredits;
import gui.components.JButtonIcon;
import player.Team;
import settings.SettingsFileHandler;
import start.X;
import util.ConfirmListener;
import util.SpielListen;

@SuppressWarnings("serial")
public abstract class Game extends Anzeige {

	public static final Font STANDARD_FONT = X.getStandardFont().deriveFont(16f);
	public static final Font PLAYER_FONT = X.getStandardFont().deriveFont(26f).deriveFont(Font.BOLD);
	private String gameName;
	public player.Player[] myPlayer;
	private Team[] myTeam = new Team[2];
	
	private int globalGameID;

	public int numOfRounds;
	public int spielerZahl;

	public String winner; // String für "beide"/"keiner"
	public int whosTurn;

	public Modus modus;

	public boolean stechen = false;
	
	private boolean buzzerActive = false;

	public static int[] getStandardBuzz(){	
		int[] standardBuzz = new int[8];
			standardBuzz[0] = KeyEvent.VK_A;
			standardBuzz[1] = KeyEvent.VK_L;
			standardBuzz[2] = KeyEvent.VK_C;
			standardBuzz[3] = KeyEvent.VK_N;
			standardBuzz[4] = KeyEvent.VK_U;
			standardBuzz[5] = KeyEvent.VK_R;
			standardBuzz[6] = KeyEvent.VK_Q;
			standardBuzz[7] = KeyEvent.VK_P;
		return standardBuzz;
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
	protected Properties customSettings;
	private JButtonIcon settingsButton;
	private String background;
	private JButton[] buzzerKeyIndicator;

	public Game(player.Player[] player, int numOfRounds,
			Modus modus, String background, int globalGameID) {
		gameName = SpielListen.getSpieleMap().get(globalGameID).getGameName();
		myPlayer = player;
		this.background = background;
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
		buzzerKeyIndicator = new JButton[spielerZahl];
		playerPanel = new JPanel[spielerZahl];
		customSettings = SettingsFileHandler.loadSettings(getGameFileName());
		loadProperties();
	}

	public void loadProperties() {
		// In Subklassen überschreiben	
	}
	
	public String getGameFileName() {
		String gameFileName = this.getClass().getPackageName();
		gameFileName = gameFileName.replaceFirst("^games.", "");
		if(this instanceof NonPC) {
			gameFileName = gameName;
		}
		return gameFileName;
	}

	private void initGUI() {
		initPauseLabel();
		this.setLayout(new BorderLayout());
		addCreds();
		{
			controlPane = new JPanel();
			controlPane.setOpaque(false);
			this.add(controlPane, BorderLayout.SOUTH);
			{
				playerBereichPanel = new JPanel();
				GridLayout playerBereichPanelLayout = new GridLayout(1, 4);
				playerBereichPanelLayout.setHgap(5);
				playerBereichPanelLayout.setVgap(5);
				playerBereichPanelLayout.setColumns(spielerZahl);
				playerBereichPanel.setOpaque(false);
				controlPane.add(playerBereichPanel);
				playerBereichPanel.setLayout(playerBereichPanelLayout);
				for (int i = 0; i < spielerZahl; i++) {
					playerPanel[i] = new JPanel();
					playerPanel[i].setBackground(Color.DARK_GRAY);
					playerBereichPanel.add(playerPanel[i]);
					playerPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.RAISED));
					playerPanel[i].setLayout(new GridBagLayout());
					if (modus == Modus.TEAM) {
						activePlayerLabel[i] = new JLabel();
						activePlayerLabel[i].setText(myTeam[i].nextMember());
						activePlayerLabel[i].setFont(X.getStandardFont().deriveFont(20f));
						activePlayerLabel[i].setForeground(Color.WHITE);
						playerPanel[i].add(activePlayerLabel[i]);
					}
					{
						playerLabel[i] = new JLabel();
						playerPanel[i].add(playerLabel[i]);
						playerLabel[i].setText(myPlayer[i].name);
						playerLabel[i].setToolTipText(KeyEvent.getKeyText(myPlayer[i].getKey()));
						playerLabel[i].setFont(PLAYER_FONT);
						playerLabel[i].setForeground(Color.WHITE);
						playerLabel[i]
								.setHorizontalAlignment(SwingConstants.CENTER);
					}
					{
						buzzerKeyIndicator[i] = new JButton();
						playerPanel[i].add(buzzerKeyIndicator[i]);
						buzzerKeyIndicator[i].setText(KeyEvent.getKeyText(myPlayer[i].getKey()));
						buzzerKeyIndicator[i].addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								showMessage("Dieser Button zeigt nur die Buzzertaste an, er hat sonst keine Funktion");
							}
						});
					}
					{
						matchCredLabel[i] = new JLabel();
						playerPanel[i].add(matchCredLabel[i]);
						matchCredLabel[i].setText(myPlayer[i].matchCredit + "");
						matchCredLabel[i].setFont(PLAYER_FONT);
						matchCredLabel[i].setForeground(Color.WHITE);
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
				settingsButton = new JButtonIcon("/media/ablauf/settings2.png",
						"Einstellungen");
				settingsButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						settingsButtonActionPerformed(evt);
					}
				});
				settingsButton.setEnabled(false);
				menuPane.add(settingsButton);
			}
			{
				anleitungButton = new JButtonIcon("/media/ablauf/hilfe.png",
						"Anleitung");
				anleitungButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						anleitungButtonActionPerformed(evt);
					}
				});
				menuPane.add(anleitungButton);
			}
			{
				quitButton = new JButtonIcon("/media/ablauf/quit.png", "Quit");
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

	protected void settingsButtonActionPerformed(ActionEvent evt) {
		openSettingsDialog(true);
	}
	
	public void enableSettingsButton(boolean enable){
		settingsButton.setEnabled(enable);
	}

	private void initPauseLabel() {
		pauseLabel = new JLabel("PAUSE");
		pauseLabel.setFont(X.BUTTON_FONT.deriveFont(100f).deriveFont(Font.BOLD));
		pauseLabel.setForeground(Color.RED);
	}

	private boolean paused = false;
	private int automaticallyPaused = 0;
	protected boolean unstoppable = false;
	
	public void triggerBuzzerEvent(int whoBuzz){
		if(buzzerActive){
			buzzeredBy(whoBuzz);
		}
	}
	
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
			// TODO Link to Wiki
			File anleitungFile = new File("/anleitungen/"
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

	// TODO remove this method when eventually all of those are replaced by the parametrized version
	public void openSettingsDialog(){
		openSettingsDialog(false);
	}
	
	public void openSettingsDialog(boolean inGame) {
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

	protected void propertiesToSettings() {
		if(customSettings == null){
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(GameSettingsDialog.NUM_OF_ROUNDS, ""+numOfRounds));
	}

	public void showMessage(String message) {
		EasyDialog.showMessage(message);
	}

	public abstract void start();

	public boolean stechen() {
		return stechen;
	}

	public void turnOver() {
		playerLabel[whosTurn].setForeground(Color.WHITE);
		playerPanel[whosTurn].setBorder(null);
		if (modus == Modus.TEAM) {
			activePlayerLabel[whosTurn].setText(myTeam[whosTurn].nextMember());
		}
		whosTurn = (whosTurn + 1) % spielerZahl;
		hebeAktivenSpielerHervor();
	}

	private void hebeAktivenSpielerHervor() {
		playerLabel[whosTurn].setForeground(myPlayer[whosTurn].farbe);
		playerPanel[whosTurn].setBorder(BorderFactory
				.createLineBorder(myPlayer[whosTurn].farbe, 10));
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

	protected void updateCreds(int labelDistance) {
		for (int i = 0; i < spielerZahl; i++) {
			creds[i].setNumOfRounds(numOfRounds);
			creds[i].setLabelDistance(labelDistance);
		}
	}

	protected void updateCreds() {
		updateCreds(1);
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
	
	public void setCustomSettings(Properties settings){
		customSettings = settings;
		if(settings != null){
			String rounds = settings.getProperty(GameSettingsDialog.NUM_OF_ROUNDS);
			if(rounds != null){
				numOfRounds = Integer.parseInt(rounds);
			}
		}
	}

	public boolean isBuzzerActive() {
		return buzzerActive;
	}

	public void setBuzzerActive(boolean buzzerActive) {
		this.buzzerActive = buzzerActive;
	}
	
	@Override
	public void nowVisible(){
		instance.changeBackground(background);
	}
	
	/**
	 * Set the winner of this round to playerID (or all others) give credits and open round dialog
	 * @param playerID
	 * @param revert
	 */
	public void winnerIs(int playerID, boolean revert){
		Set<Integer> winnerIDs = new HashSet<Integer>();
		if (revert) {
			for (int i = 0; i < spielerZahl; i++) {
				if (i != playerID)
					winnerIDs.add(i);
			}
		} else {
			winnerIDs.add(playerID);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
	}

	private void verbuchePunkte(Set<Integer> winnerIDs) {
		if (winnerIDs.size() < myPlayer.length) {
			for (int id : winnerIDs) {
				creds[id].earnsCredit(1);
				myPlayer[id].gameCredit++;
			}
		}
	}

	private String getWinnerText(Set<Integer> winnerIDs) {
		String winner = "";
		if (winnerIDs.size() >= myPlayer.length || winnerIDs.size() == 0) {
			winner = "niemanden";
		} else {
			int c = 0;
			for (int id : winnerIDs) {
				String trenner = "";
				if (c == winnerIDs.size() - 3)
					trenner = ", ";
				if (c == winnerIDs.size() - 2)
					trenner = " und ";
				winner += myPlayer[id].name + trenner;
				c++;
			}
		}
		return winner;
	}
}
