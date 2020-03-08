package gui.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.Anzeige;
import gui.components.Bildschirm;
import gui.components.DefaultButton;
import highscore.HighscoreAnzeige;
import settings.MainSettingsDialog;
import start.X;

public class HauptMenu extends Anzeige {
	/**
	 * serialVersionUID generated by Eclipse
	 */
	private static final long serialVersionUID = 7838721137038997445L;
	private static String myBackground = "/media/ablauf/iceBG.jpg";
	private JPanel neuesSpielPanel;
	private JButton neuesSpielButton;
	private JPanel spielLadenPanel;
	private JButton spielLadenButton;
	private Bildschirm logoBildschirm;
	private JPanel logoPanel;
	private JPanel einstellungenPanel;
	private JButton einstellungenButton;
	private JPanel highscorePanel;
	private JButton highscoreButton;
	private JPanel beendenPanel;
	private JButton beendenButton;
	private JPanel menuPanel;
	private JPanel updatePanel;
	private JLabel updateInfoLabel;

	public HauptMenu() {
		GridLayout myLayout = new GridLayout(6, 1);
		myLayout.setHgap(5);
		myLayout.setVgap(5);
		myLayout.setColumns(1);
		myLayout.setRows(6);
		this.setLayout(myLayout);
		this.setLayout(new BorderLayout());
		menuPanel = new JPanel(new GridLayout(5, 1));
		menuPanel.setOpaque(false);
		this.add(menuPanel, BorderLayout.CENTER);
		this.setOpaque(false);
		{
			logoPanel = new JPanel();
			this.add(logoPanel, BorderLayout.NORTH);
			logoPanel.setOpaque(false);
			{
				logoBildschirm = new Bildschirm("/media/ablauf/Xmenu.png");
				logoPanel.add(logoBildschirm);
			}
		}
		{
			neuesSpielPanel = new JPanel();
			menuPanel.add(neuesSpielPanel);
			neuesSpielPanel.setOpaque(false);
			{
				neuesSpielButton = new DefaultButton("Neues Spiel");
				neuesSpielPanel.add(neuesSpielButton);
				neuesSpielButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						neuesSpielButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			spielLadenPanel = new JPanel();
			menuPanel.add(spielLadenPanel);
			spielLadenPanel.setOpaque(false);
			{
				spielLadenButton = new DefaultButton("Spiel laden");
				spielLadenPanel.add(spielLadenButton);
				spielLadenButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						spielLadenButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			einstellungenPanel = new JPanel();
			menuPanel.add(einstellungenPanel);
			einstellungenPanel.setOpaque(false);
			{
				einstellungenButton = new DefaultButton("Einstellungen");
				einstellungenPanel.add(einstellungenButton);
				einstellungenButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						einstellungenButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			highscorePanel = new JPanel();
			menuPanel.add(highscorePanel);
			highscorePanel.setOpaque(false);
			{
				highscoreButton = new DefaultButton("Highscore");
				highscorePanel.add(highscoreButton);
				highscoreButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						highscoreButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			beendenPanel = new JPanel();
			menuPanel.add(beendenPanel);
			beendenPanel.setOpaque(false);
			{
				beendenButton = new DefaultButton("Beenden");
				beendenPanel.add(beendenButton);
				beendenButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						beendenButtonActionPerformed(evt);
					}
				});
			}
		}
		setButtonSizes();
		{
			updatePanel = new JPanel();
			updatePanel.setOpaque(false);
			this.add(updatePanel, BorderLayout.SOUTH);
			updateInfoLabel = new JLabel();
			updateInfoLabel.setText("Version: " + X.VERSION.toString());
			updateInfoLabel.setForeground(Color.WHITE);
			updatePanel.add(updateInfoLabel);
		}
		logoBildschirm.requestFocusInWindow();
		instance.changeBackground(myBackground);
	}

	private void setButtonSizes() {
		int buttonHeight = 0;
		int buttonWidth = 0;
		buttonHeight = Math.max(buttonHeight,
				neuesSpielButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight,
				spielLadenButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight,
				einstellungenButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight,
				highscoreButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight,
				beendenButton.getPreferredSize().height);
		buttonWidth = Math.max(buttonWidth,
				neuesSpielButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth,
				spielLadenButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth,
				einstellungenButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth,
				highscoreButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth,
				beendenButton.getPreferredSize().width);
		neuesSpielButton.setPreferredSize(new Dimension(buttonWidth,
				buttonHeight));
		spielLadenButton.setPreferredSize(new Dimension(buttonWidth,
				buttonHeight));
		einstellungenButton.setPreferredSize(new Dimension(buttonWidth,
				buttonHeight));
		highscoreButton.setPreferredSize(new Dimension(buttonWidth,
				buttonHeight));
		beendenButton
				.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
	}

	protected void beendenButtonActionPerformed(ActionEvent evt) {
		instance.close();
	}

	protected void einstellungenButtonActionPerformed(ActionEvent evt) {
		instance.showDialog(new MainSettingsDialog());
	}

	protected void highscoreButtonActionPerformed(ActionEvent evt) {
		// GameHighscorePanel ghp = new
		// GameHighscorePanel(HighscoreFileHandler.loadGameHighscore(new
		// File(X.getDataDir() + "Highscore/Games/guess.ghs")));
		// EasyDialog.showMessage("",ghp);
		instance.changeAnzeige(new HighscoreAnzeige());
	}

	protected void neuesSpielButtonActionPerformed(ActionEvent evt) {
		instance.changeAnzeige(new ModusMenuPanel());
	}

	@Override
	public void nowVisible() {
		instance.changeBackground(myBackground);
	}

	protected void spielLadenButtonActionPerformed(ActionEvent evt) {
		instance.changeAnzeige(new MatchLaden());
	}
}
