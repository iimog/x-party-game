package gui.menu;

import games.Modus;
import gui.Anzeige;
import gui.EasyDialog;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import player.Player;
import start.X;
import util.PlayerAsker;
import ablauf.MatchFileHandler;
import ablauf.Spielablauf;

public class MatchLaden extends Anzeige {
	private static final long serialVersionUID = 1L;
	private static String myBackground = "media/ablauf/fire.jpg";
	private File[] spielstaende;
	
	private MatchListe matchListe;
	private JPanel buttonPanel;
	private JButton startenButton;
	private JButton loeschenButton;
	private JButton hauptmenuButton;
	private JPanel mainPanel;
	private int unknownPlayerID = 0;
	private Spielablauf sa;
	private MatchLaden thisML;
	private File zuLadendeDatei;
	
	public MatchLaden(){
		super();
		thisML = this;
		File f = new File(X.getDataDir() + "Matchs");
		spielstaende = f.listFiles(new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".match"))return true;
				return false;
			}
		});
		initGUI();
	}
	private void initGUI() {
		addMainPanel();
		mainPanel.setLayout(new BorderLayout());
		matchListe = new MatchListe(getInfoPanels(spielstaende));
		JScrollPane scrollPane = new JScrollPane(matchListe);
		scrollPane.setMinimumSize(new Dimension(800,600));
		scrollPane.setOpaque(false);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(getButtonPanel(), BorderLayout.SOUTH);
	}
	private void addMainPanel() {
		GridBagLayout gbl      = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor             = GridBagConstraints.CENTER;
		this.setLayout(gbl);
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		gbl.setConstraints(mainPanel,gbc);
		this.add(mainPanel);
	}
	private JPanel getButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout());
		startenButton = new DefaultButton("Spiel starten");
		startenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				starteSpiel();
			}
		});
		buttonPanel.add(startenButton);
		loeschenButton = new DefaultButton("Spielstand löschen");
		loeschenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loeschespielstaende();
			}
		});
		buttonPanel.add(loeschenButton);
		hauptmenuButton = new DefaultButton("Zurück zum Hauptmenü");
		hauptmenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				X.getInstance().changeAnzeige(new HauptMenu());
			}
		});
		buttonPanel.add(hauptmenuButton);
		return buttonPanel;
	}
	protected void starteSpiel() {
		zuLadendeDatei = matchListe.getSelectedFile();
		if(zuLadendeDatei == null)return;
		sa = MatchFileHandler.loadMatch(zuLadendeDatei);
		startIfPlayersOK();
	}
	private void startIfPlayersOK() {
		if(sa.getModus() == Modus.SOLO){
			sa.getPlayers()[1] = Player.pcPlayer();
		}
		boolean allePlayerOK = true;
		for(int i=0; i<sa.getPlayers().length; i++){
			if(sa.getPlayers()[i]==null)
				allePlayerOK = false;
		}
		if(!allePlayerOK){
			for(int i=0; i<sa.getPlayers().length; i++){
				if(sa.getPlayers()[i]==null)
					unknownPlayerID = i;
			}
			String playerName = MatchFileHandler.getPlayerNames(zuLadendeDatei)[unknownPlayerID];
			PlayerWahlPanel pwp = new PlayerWahlPanel(new PlayerAsker() {

				@Override
				public void thisIsThePlayer(Player p) {
					sa.getPlayers()[unknownPlayerID] = p;
					instance.changeAnzeige(thisML);
					startIfPlayersOK();
				}
			});
			instance.changeAnzeige(pwp);
			EasyDialog.showMessage("Der Spieler mit dem Namen \"" + playerName +
					"\" existiert nicht. Bitte suche dir einen anderen aus oder" +
					" erstelle ihn neu.");
		}
		
		else{
			sa.updateErgebnisse();
			sa.showZwischenstand();
		}
	}
	protected void loeschespielstaende() {
		File spielstand = matchListe.getSelectedFile();
		if(spielstand==null)return;
		MatchFileHandler.deleteMatchFile(spielstand);
		refreshList();
	}
	private void refreshList() {
		File f = new File(X.getDataDir() + "Matchs");
		spielstaende = f.listFiles();
		matchListe.refresh(getInfoPanels(spielstaende));
	}
	public void nowVisible(){
		instance.changeBackground(myBackground);
	}
	
	private MatchInfoPanel[] getInfoPanels(File[] files){
		int fileNumber = 0;
		if(files != null){
			fileNumber = files.length;
		}
		MatchInfoPanel[] infos = new MatchInfoPanel[fileNumber];
		for(int i=0; i<fileNumber; i++){
			infos[i] = new MatchInfoPanel(MatchFileHandler.getMatchInfoForFile(files[i]));
		}
		return infos;
	}
}
