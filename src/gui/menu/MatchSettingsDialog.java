package gui.menu;

import games.Modus;
import gui.EasyDialog;
import gui.components.Bildschirm;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import settings.GeneralSettingsPanel;
import settings.Profile;
import settings.ProfileFileHandler;
import start.X;
import util.ConfirmListener;
import util.InputListener;

public class MatchSettingsDialog extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private StartMatch startMatch;
	private Modus modus;
	private SpiellistePanel spielliste;
	private JPanel buttonPanel;
	private JButton verwerfenButton;
	private JButton UebernehmenButton;
	private JPanel teamPanel;
	private JPanel profilPanel;
	private DefaultComboBoxModel profilComboBoxModel;
	private JComboBox profilComboBox;
	private JButton profilManagerButton;
	private JButton profilSpeichernButton;
	private Profile currentProfile;
	private List<Integer> spiele;
	private File[] profiles;
	private String profileName;
	private GeneralSettingsPanel generalSettings;
	private JPanel hauptPanel;
	
	public MatchSettingsDialog(StartMatch startMatch){
		this.startMatch = startMatch;
		this.modus = startMatch.getModus();
		currentProfile = startMatch.getProfile();
		initGUI();
	}
	
	private void initGUI(){
		this.setOpaque(false);
		addHauptPanel();
		addHauptPanelToDummyPanel();
		addTabbedPane();
		addProfilPanel();
		addButtonPane();
	}

	private void addHauptPanel() {
		hauptPanel = new JPanel();
		hauptPanel.setLayout(new BorderLayout());
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

	private void addTabbedPane() {
		tabbedPane = new JTabbedPane();
		hauptPanel.add(tabbedPane, BorderLayout.CENTER);
		generalSettings = new GeneralSettingsPanel(currentProfile);
		tabbedPane.add(generalSettings, "Allgemein");
		spielliste = new SpiellistePanel(startMatch);
		tabbedPane.add(spielliste, "Spielliste");
		if(modus == Modus.TEAM){
			teamPanel = new JPanel();
			teamPanel.add(new Bildschirm("/media/ablauf/baustelle3.jpg"));
			tabbedPane.add(teamPanel, "Team");
		}
	}
	
	private void addProfilPanel() {
		profilPanel = new JPanel();
		hauptPanel.add(profilPanel, BorderLayout.NORTH);
		profilPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel profilLabel = new JLabel("Profil");
		profilPanel.add(profilLabel);
		profilComboBox = new JComboBox();
		setProfileComboBoxModel();
		profilComboBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				profileChanged();
			}
		});
		profilPanel.add(profilComboBox);
		profilSpeichernButton = new DefaultButton("Speichern");
		profilSpeichernButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aktuellesProfilSpeichern();
			}
		});
		profilPanel.add(profilSpeichernButton);
		profilManagerButton = new DefaultButton("Profilmanager");
		profilPanel.add(profilManagerButton);
		profilManagerButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				profilManagerButtonActionPerformed();
			}
		});
	}

	private void setProfileComboBoxModel() {
		profiles = ProfileFileHandler.getProfiles();
		int profilzahl = 0;
		if(profiles != null) profilzahl = profiles.length;
		String[] profileNames = new String[profilzahl + 1];
		profileNames[0] = "Standard";
		for(int i=0; i<profilzahl; i++){
			profileNames[i+1] = profiles[i].getName().substring(0, profiles[i].getName().length() - 5);
		}
		profilComboBoxModel = new DefaultComboBoxModel(profileNames);
		profilComboBox.setModel(profilComboBoxModel);
	}

	protected void profileChanged() {
		spielliste.setPCandNonPC();
		int index = profilComboBox.getSelectedIndex();
		if(index==0){
			currentProfile = Profile.getDefaultProfile();
		}
		else{
			File prof = profiles[index-1];
			currentProfile = ProfileFileHandler.loadProfile(prof);
		}
		spielliste.setGewaehlteSpiele(currentProfile.getSpielliste(modus));
		generalSettings.setProfile(currentProfile);
	}

	protected void aktuellesProfilSpeichern() {
		spiele = spielliste.getGewaehlteSpiele();
		if(spiele.size()<2){
			EasyDialog.showMessage("Die Spielliste ist leer, das macht keinen Sinn!");
			return;
		}
		EasyDialog.showInput("Bitte den Namen eingeben, unter dem das Profil gespeichert werden soll.", 
				currentProfile.getName(), null, new InputListener() {					
					@Override
					public void giveMeInput(String input) {
						if(input.equals("") || input.equals("Standard")){
							EasyDialog.showMessage("Der Name darf nicht leer oder \"Standard\" sein.");
						}
						else{
							aufErsetzenTestenUndSpeichern(input);
						}
					}					
					@Override
					public void abgebrochen() {
						X.getInstance().closeDialog();
					}
				});
		
	}
	protected void aufErsetzenTestenUndSpeichern(String name) {
		profileName = name;
		boolean ersetzen = false;
		if(profiles != null){
		for(File f: profiles){
			if(f.getName().equals(name+".prof")) ersetzen = true;
		}}
		if(ersetzen){
			EasyDialog.showConfirm("Das Profil mit dem Namen \""+ name + "\" existiert bereits, soll " +
					"es verändert werden?", null, new ConfirmListener() {
						@Override
						public void confirmOptionPerformed(int optionType) {
							if(optionType == ConfirmListener.YES_OPTION){
								speichernDurchfuehren(profileName, spiele);
							}
							X.getInstance().closeDialog();
						}
					});
		}
		else{
			speichernDurchfuehren(name, spiele);
		}
	}

	private void speichernDurchfuehren(String name, List<Integer> spiele){
		currentProfile.setName(name);
		X.getInstance().closeDialog();
		currentProfile.setSpielliste(spiele, modus);
		currentProfile.setPunkteModus(generalSettings.getPunkteModus());
		currentProfile.saveProfileToFile();
		setProfileComboBoxModel();
		profilComboBox.setSelectedItem(currentProfile.getName());
	}

	private void addButtonPane() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		hauptPanel.add(buttonPanel, BorderLayout.SOUTH);
		verwerfenButton = new DefaultButton("Verwerfen");
		buttonPanel.add(verwerfenButton);
		verwerfenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				verwerfenButtonActionPerformed();
			}
		});
		UebernehmenButton = new DefaultButton("Übernehmen");
		buttonPanel.add(UebernehmenButton);
		UebernehmenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				uebernehmenButtonActionPerformed();
			}
		});
	}
	
	protected void profilManagerButtonActionPerformed() {
		X.getInstance().showDialog(new ProfilManager(this));
	}

	protected void verwerfenButtonActionPerformed() {
		startMatch.changeOptions(null);
	}

	protected void uebernehmenButtonActionPerformed() {
		List<Integer> moeglicheSpiele = spielliste.getGewaehlteSpiele();
		if(moeglicheSpiele.size()<2){
			EasyDialog.showMessage("Die Spielliste ist leer, das macht keinen Sinn!");
			return;
		}
		currentProfile.setSpielliste(moeglicheSpiele, modus);
		currentProfile.setPunkteModus(generalSettings.getPunkteModus());
		startMatch.setProfile(currentProfile);
		startMatch.changeOptions(null);
	}

	public void updateProfiles() {
		setProfileComboBoxModel();
	}
}
