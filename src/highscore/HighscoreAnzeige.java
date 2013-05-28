package highscore;

import gui.Anzeige;
import gui.EasyDialog;
import gui.components.JButtonIcon;
import gui.menu.HauptMenu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import start.X;

public class HighscoreAnzeige extends Anzeige {
	private static final long serialVersionUID = 1L;
	private JPanel buttonPanel;
	private JButton hauptMenuButton;
	private JPanel topPanel;
	private JButtonIcon quitButton;
	private JTabbedPane hauptPanel;
	private JList spielListe;
	private File[] highscores;
	private DefaultComboBoxModel highscoreComboBoxModel;
	private MatchHighscore matchHighscore;
	private GameHighscorePanel matchHighscorePanel;

	public HighscoreAnzeige(){
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		initButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
		initTopPanel();
		this.add(topPanel, BorderLayout.NORTH);
		initHauptPanel();
		addHauptPanelToDummyPanel();
	}
	
	private void initHauptPanel() {
		hauptPanel = new JTabbedPane();
		hauptPanel.setOpaque(false);
		initMatchHighscorePanel();
		hauptPanel.add(matchHighscorePanel, "Match");
		initSpielListe();
		hauptPanel.add(spielListe, "Spiele");
	}
	
	private void addHauptPanelToDummyPanel() {
		GridBagLayout gbl      = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor             = GridBagConstraints.CENTER;
		JPanel dummyPanel = new JPanel(gbl);
		dummyPanel.setOpaque(false);
		gbl.setConstraints(hauptPanel,gbc);
		dummyPanel.add(hauptPanel);
		this.add(dummyPanel,BorderLayout.CENTER);
	}

	private void initMatchHighscorePanel() {
		matchHighscore = HighscoreFileHandler.loadMatchHighscore();
		matchHighscorePanel = new GameHighscorePanel(matchHighscore);
	}

	private void initSpielListe() {
		spielListe = new JList();
		spielListe.setFont(X.buttonFont);
		highscores = HighscoreFileHandler.getGameHighscores();
		int highscoreZahl = 0;
		if(highscores != null)highscoreZahl = highscores.length;
		String[] highscoreNames = new String[highscoreZahl];
		for(int i=0; i<highscoreZahl; i++){
			String name = highscores[i].getName();
			highscoreNames[i] = name.substring(0, name.length() - 4);
		}
		highscoreComboBoxModel = new DefaultComboBoxModel(highscoreNames);
		spielListe.setModel(highscoreComboBoxModel);
		spielListe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = spielListe.getSelectedIndex();
				File gameHighscoreFile = highscores[index];
				GameHighscore gh = HighscoreFileHandler.loadGameHighscore(gameHighscoreFile);
				GameHighscorePanel ghp = new GameHighscorePanel(gh);
				EasyDialog.showMessage("", ghp);
			}
		});
	}

	private void initTopPanel() {
		topPanel = new JPanel();
		FlowLayout topPanelLayout = new FlowLayout();
		topPanel.setLayout(topPanelLayout);
		topPanelLayout.setAlignment(FlowLayout.RIGHT);
		topPanel.setOpaque(false);
		add(topPanel, BorderLayout.NORTH);
		{
			quitButton = new JButtonIcon("media/ablauf/quit.png","Quit");
			quitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					quitButtonActionPerformed(evt);
				}
			});
			topPanel.add(quitButton);
		}
	}

	private void initButtonPanel() {
		buttonPanel = new JPanel();
		FlowLayout bottomPanelLayout = new FlowLayout();
		buttonPanel.setLayout(bottomPanelLayout);
		bottomPanelLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.setOpaque(false);
		add(buttonPanel, BorderLayout.SOUTH);
		{
			hauptMenuButton = new JButton("Hauptmenü");
			hauptMenuButton.setFont(X.buttonFont);
			hauptMenuButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					hauptMenuButtonActionPerformed();
				}
			});
			buttonPanel.add(hauptMenuButton);
		}
	}
	protected void hauptMenuButtonActionPerformed() {
		instance.changeAnzeige(new HauptMenu());
	}
	private void quitButtonActionPerformed(ActionEvent evt){
		instance.close();
	}

	public void nowVisible(){
		instance.changeBackground("media/highscore/highscore.png");
	}
}
