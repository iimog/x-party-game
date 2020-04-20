package games.dialogeGUIs;
import games.Game;
import games.GameInfo;
import gui.EasyDialog;
import gui.components.JButtonIcon;
import highscore.GameHighscore;
import highscore.GameHighscorePanel;
import highscore.HighscoreFileHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import util.FocusRequestListener;
import util.SpielListen;

public class GameStartDialog extends gui.AnzeigeDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1233495347721279606L;
	private JPanel hauptbereichPanel;
	private JPanel mainPane;
	private JButton settingsButton;
	private JTextPane infoTextPanel;
	private JLabel nameLabel;
	private JButton startButton;
	private JButton anleitungButton;
	private JPanel schaltflaechenPanel;

	private String gameName;
	private String shortInfo;
	private Game game;
	private JButton highscoreButton;
	private JButtonIcon skipButton;

	public GameStartDialog(Game game) {
		GameInfo gi = SpielListen.getSpieleMap().get(game.getGlobalGameID());
		gameName = gi.getGameName();
		shortInfo = gi.getShortInfo();
		this.game = game;
		initGUI();
	}

	private void anleitungButtonActionPerformed(ActionEvent evt) {
		game.getAnleitung();
	}

	private void initGUI() {
		try {
			{
				mainPane = new JPanel();
				BorderLayout mainPaneLayout = new BorderLayout();
				super.dialogPane.add(mainPane);
				mainPane.setLayout(mainPaneLayout);
				mainPane.setOpaque(false);
				{
					schaltflaechenPanel = new JPanel();
					schaltflaechenPanel.setOpaque(false);
					mainPane.add(schaltflaechenPanel, BorderLayout.SOUTH);
					{
						anleitungButton = new JButtonIcon("/media/ablauf/hilfe.png","Anleitung");
						anleitungButton.setToolTipText("Anleitung");
						schaltflaechenPanel.add(anleitungButton);
						anleitungButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								anleitungButtonActionPerformed(evt);
							}
						});
					}
					{
						settingsButton = new JButtonIcon("/media/ablauf/settings2.png","Einstellungen");
						settingsButton.setToolTipText("Einstellungen");
						schaltflaechenPanel.add(settingsButton);
						settingsButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								settingsButtonActionPerformed(evt);
							}
						});
					}
					{
						highscoreButton = new JButtonIcon("/media/ablauf/highscore.png","Highscore");
						highscoreButton.setToolTipText("Highscore");
						schaltflaechenPanel.add(highscoreButton);
						highscoreButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								highscoreButtonActionPerformed(e);								
							}
						});
					}
					{
						skipButton = new JButtonIcon("/media/ablauf/skip.png","Spiel überspringen");
						skipButton.setToolTipText("Spiel überspringen");
						schaltflaechenPanel.add(skipButton);
						skipButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								skipButtonActionPerformed(evt);
							}
						});
					}
					{
						startButton = new JButtonIcon("/media/ablauf/rightarrow.png","Spiel starten");
						startButton.setToolTipText("Spiel starten");
						schaltflaechenPanel.add(startButton);
						startButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								startButtonActionPerformed(evt);
							}
						});
						startButton.addAncestorListener(new FocusRequestListener());
					}
					
				}
				{
					hauptbereichPanel = new JPanel();
					mainPane.add(hauptbereichPanel, BorderLayout.CENTER);
					BorderLayout hauptbereichPanelLayout = new BorderLayout();
					hauptbereichPanel.setLayout(hauptbereichPanelLayout);
					hauptbereichPanel.setOpaque(false);
					{
						nameLabel = new JLabel();
						hauptbereichPanel.add(nameLabel, BorderLayout.NORTH);
						nameLabel.setText(gameName);
						nameLabel.setFont(new java.awt.Font("Comic Sans MS",1,48));
						nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
						nameLabel.setForeground(new java.awt.Color(0,0,160));
					}
					{
						infoTextPanel = new JTextPane();
						hauptbereichPanel.add(infoTextPanel, BorderLayout.CENTER);
						infoTextPanel.setText(shortInfo);
						infoTextPanel.setForeground(Color.WHITE);
						infoTextPanel.setBackground(Color.BLACK);
						infoTextPanel.setFont(new java.awt.Font("Comic Sans MS",0,16));
						infoTextPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						infoTextPanel.setEditable(false);
					}
				}
			}
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void skipButtonActionPerformed(ActionEvent evt) {
		game.abbruch();
		instance.closeDialog();
	}

	private void highscoreButtonActionPerformed(ActionEvent e) {
		GameHighscore gh = HighscoreFileHandler.loadGameHighscore(game.getGameFileName());
		if(gh != null){
			GameHighscorePanel ghp = new GameHighscorePanel(gh, game.modus);
			EasyDialog.showMessage("", ghp);
		}
		else{
			EasyDialog.showMessage("Zu diesem Spiel liegen noch keine Highscoredaten vor.");
		}
	}

	private void settingsButtonActionPerformed(ActionEvent evt) {
		game.openSettingsDialog();
	}

	private void startButtonActionPerformed(ActionEvent evt) {
		startGame();
	}
	private void startGame(){
		instance.closeDialog();
		game.start();
	}

}
