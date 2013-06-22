package gui.menu;

import gui.Anzeige;
import gui.aterai.RoundedCornerButton;
import gui.components.Bildschirm;
import highscore.HighscoreAnzeige;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import settings.MainSettingsDialog;
import start.X;


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
public class HauptMenu extends Anzeige {
	/**
	 * serialVersionUID generated by Eclipse
	 */
	private static final long serialVersionUID = 7838721137038997445L;
	private static String myBackground = "media/ablauf/iceBG.jpg";
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

	public HauptMenu(){
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
				logoBildschirm = new Bildschirm("media/ablauf/xnewk.png");
				logoPanel.add(logoBildschirm);
			}
		}
		{
			neuesSpielPanel = new JPanel();
			menuPanel.add(neuesSpielPanel);
			neuesSpielPanel.setOpaque(false);
			{
				neuesSpielButton = new RoundedCornerButton();
				neuesSpielButton.setBackground(Color.DARK_GRAY);
				neuesSpielButton.setForeground(Color.LIGHT_GRAY);
				neuesSpielButton.setFocusPainted(false);
				neuesSpielPanel.add(neuesSpielButton);
				neuesSpielButton.setText("Neues Spiel");
				neuesSpielButton.setFont(X.buttonFont);
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
				spielLadenButton = new RoundedCornerButton();
				spielLadenButton.setBackground(Color.DARK_GRAY);
				spielLadenButton.setForeground(Color.LIGHT_GRAY);
				spielLadenButton.setFocusPainted(false);
				spielLadenPanel.add(spielLadenButton);
				spielLadenButton.setText("Spiel laden");
				spielLadenButton.setFont(X.buttonFont);
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
				einstellungenButton = new RoundedCornerButton();
				einstellungenButton.setBackground(Color.DARK_GRAY);
				einstellungenButton.setForeground(Color.LIGHT_GRAY);
				einstellungenButton.setFocusPainted(false);
				einstellungenPanel.add(einstellungenButton);
				einstellungenButton.setText("Einstellungen");
				einstellungenButton.setFont(X.buttonFont);
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
				highscoreButton = new RoundedCornerButton();
				highscoreButton.setBackground(Color.DARK_GRAY);
				highscoreButton.setForeground(Color.LIGHT_GRAY);
				highscoreButton.setFocusPainted(false);
				highscorePanel.add(highscoreButton);
				highscoreButton.setText("Highscore");
				highscoreButton.setFont(X.buttonFont);
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
				beendenButton = new RoundedCornerButton();
				beendenButton.setBackground(Color.DARK_GRAY);
				beendenButton.setForeground(Color.LIGHT_GRAY);
				beendenButton.setFocusPainted(false);
				beendenPanel.add(beendenButton);
				beendenButton.setText("Beenden");
				beendenButton.setFont(X.buttonFont);
				beendenButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						beendenButtonActionPerformed(evt);
					}
				});
			}
		}
		setButtonSizes();
		logoBildschirm.requestFocusInWindow();
	}

	private void setButtonSizes() {
		int buttonHeight = 0;
		int buttonWidth = 0;
		buttonHeight = Math.max(buttonHeight, neuesSpielButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight, spielLadenButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight, einstellungenButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight, highscoreButton.getPreferredSize().height);
		buttonHeight = Math.max(buttonHeight, beendenButton.getPreferredSize().height);
		buttonWidth = Math.max(buttonWidth, neuesSpielButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth, spielLadenButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth, einstellungenButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth, highscoreButton.getPreferredSize().width);
		buttonWidth = Math.max(buttonWidth, beendenButton.getPreferredSize().width);
		neuesSpielButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		spielLadenButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		einstellungenButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		highscoreButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		beendenButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
	}

	protected void beendenButtonActionPerformed(ActionEvent evt) {
		instance.close();
	}
	protected void einstellungenButtonActionPerformed(ActionEvent evt) {
		instance.showDialog(new MainSettingsDialog());
	}

	protected void highscoreButtonActionPerformed(ActionEvent evt) {
		//GameHighscorePanel ghp = new GameHighscorePanel(HighscoreFileHandler.loadGameHighscore(new File(X.getDataDir() + "Highscore/Games/guess.ghs")));
		//EasyDialog.showMessage("",ghp);
		instance.changeAnzeige(new HighscoreAnzeige());
	}

	protected void neuesSpielButtonActionPerformed(ActionEvent evt) {
		instance.changeAnzeige(new ModusMenuPanel());
	}

	@Override
	public void nowVisible(){
		instance.changeBackground(myBackground);
	}

	protected void spielLadenButtonActionPerformed(ActionEvent evt) {
		instance.changeAnzeige(new MatchLaden());
	}
}
