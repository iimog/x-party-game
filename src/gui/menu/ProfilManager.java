package gui.menu;

import gui.AnzeigeDialog;
import gui.EasyDialog;
import gui.components.Bildschirm;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import settings.Profile;
import settings.ProfileFileHandler;
import start.X;
import util.ConfirmListener;
import util.InputListener;

public class ProfilManager extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	private MatchSettingsDialog matchSettingsDialog;
	private JList<String> profilListe;
	private JPanel hauptPanel;
	private File[] profiles;
	private DefaultComboBoxModel<String> profilComboBoxModel;
	private JPanel buttonPanel;
	private JButton neuButton;
	private JButton bearbeitenButton;
	private JButton umbenennenButton;
	private JButton loeschenButton;
	private JButton fertigButton;
	private String neuProfileName;
	
	public ProfilManager(){
		this(null);
	}
	public ProfilManager(MatchSettingsDialog matchSettingsDialog){
		this.matchSettingsDialog = matchSettingsDialog;
		initGUI();
	}
	
	private void initGUI(){
		hauptPanel = new JPanel();
		hauptPanel.setLayout(new BorderLayout());
		dialogPane.add(hauptPanel);
		initProfilListe();
		hauptPanel.add(profilListe, BorderLayout.CENTER);
		initButtonPanel();
		hauptPanel.add(buttonPanel, BorderLayout.EAST);
		fertigButton = new DefaultButton("Fertig");
		fertigButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				instance.closeDialog();
				if(matchSettingsDialog != null)matchSettingsDialog.updateProfiles();
			}
		});
		hauptPanel.add(fertigButton, BorderLayout.SOUTH);
	}
	
	private void initProfilListe() {
		profilListe = new JList<String>();
		profilListe.setFont(X.BUTTON_FONT);
		updateProfiles();
	}
	private void initButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3,1));
		initNeuButton();
		buttonPanel.add(neuButton);
		initBearbeitenButton();
		//buttonPanel.add(bearbeitenButton);
		initUmbenennenButton();
		buttonPanel.add(umbenennenButton);
		initLoeschenButton();
		buttonPanel.add(loeschenButton);
	}
	private void initNeuButton() {
		neuButton = new DefaultButton("Neu");
		neuButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EasyDialog.showInput("Bitte gib einen Namen für das neue Profil ein", "", null, new InputListener() {					
					@Override
					public void giveMeInput(String input) {
						aufErsetzenTestenUndSpeichern(input);
					}					
					@Override
					public void abgebrochen() {
						X.getInstance().closeDialog();
					}
				});
			}
		});
	}
	private void initBearbeitenButton() {
		bearbeitenButton = new DefaultButton("Bearbeiten");
		bearbeitenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				bearbeitenButtonActionPerformed();
			}
		});
	}
	private void initUmbenennenButton() {
		umbenennenButton = new DefaultButton("Umbenennen");
		umbenennenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(profilListe.getSelectedIndex() == -1) return;
				String defText = profiles[profilListe.getSelectedIndex()].getName();
				defText = defText.substring(0, defText.length()-5);
				EasyDialog.showInput("Bitte gib den neuen Namen für das Profil '"
						+defText+"' ein", defText, null, new InputListener() {					
					@Override
					public void giveMeInput(String input) {
						aufErsetzenTestenUndUmbenennen(input);
					}					
					@Override
					public void abgebrochen() {
						X.getInstance().closeDialog();
					}
				});
			}
		});
	}
	private void initLoeschenButton() {
		loeschenButton = new DefaultButton("Löschen");
		loeschenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(profilListe.getSelectedIndex() == -1) return;
				String name = profiles[profilListe.getSelectedIndex()].getName();
				name = name.substring(0, name.length()-5);
				EasyDialog.showConfirm("Das Profil '"+name+"' wirklich löschen?", null, new ConfirmListener() {					
					@Override
					public void confirmOptionPerformed(int optionType) {
						if(optionType == ConfirmListener.YES_OPTION){
							loeschenButtonActionPerformed();
						}
						X.getInstance().closeDialog();
					}
				});
				
			}
		});
	}
	
	protected void bearbeitenButtonActionPerformed() {
		EasyDialog.showMessage("Der Bearbeiten Button ist noch in Bearbeitung", 
				new Bildschirm("/media/ablauf/baustelle2.jpg"));
	}
	protected void umbenennenDurchfuehren(String newName) {
		int index = profilListe.getSelectedIndex();
		profiles[index].renameTo(new File(profiles[index].getParentFile(), newName+".prof"));
		profiles[index].delete();
		X.getInstance().closeDialog();
		updateProfiles();
	}
	protected void loeschenButtonActionPerformed() {
		int index = profilListe.getSelectedIndex();
		profiles[index].delete();
		updateProfiles();
	}
	private void updateProfiles(){
		profiles = ProfileFileHandler.getProfiles();
		int profilZahl = 0;
		if(profiles != null)profilZahl = profiles.length;
		String[] profileNames = new String[profilZahl];
		for(int i=0; i<profilZahl; i++){
			profileNames[i] = profiles[i].getName().substring(0, profiles[i].getName().length() - 5);
		}
		profilComboBoxModel = new DefaultComboBoxModel<String>(profileNames);
		profilListe.setModel(profilComboBoxModel);
	}

	protected void aufErsetzenTestenUndSpeichern(String name) {
		boolean ersetzen = aufErsetzenPruefen(name);
		if(ersetzen){
			EasyDialog.showConfirm("Das Profil mit dem Namen \""+ name + "\" existiert bereits, soll " +
					"es überschrieben werden?", null, new ConfirmListener() {
						@Override
						public void confirmOptionPerformed(int optionType) {
							if(optionType == ConfirmListener.YES_OPTION){
								speichernDurchfuehren(neuProfileName);
							}
							X.getInstance().closeDialog();
						}
					});
		}
		else{
			speichernDurchfuehren(name);
		}
	}
	protected void aufErsetzenTestenUndUmbenennen(String name) {
		boolean ersetzen = aufErsetzenPruefen(name);
		if(ersetzen){
			EasyDialog.showConfirm("Das Profil mit dem Namen \""+ name + "\" existiert bereits, soll " +
					"es überschrieben werden?", null, new ConfirmListener() {
						@Override
						public void confirmOptionPerformed(int optionType) {
							if(optionType == ConfirmListener.YES_OPTION){
								profiles[getIndexOf(neuProfileName)].delete();
								umbenennenDurchfuehren(neuProfileName);
								
							}
							X.getInstance().closeDialog();
						}
					});
		}
		else{
			umbenennenDurchfuehren(name);
		}
	}
	protected int getIndexOf(String name) {
		int ersetzen = -1;
		if(profiles != null){
		for(int i=0; i<profiles.length; i++){
			File f = profiles[i];
			if(f.getName().equals(name+".prof")) ersetzen = i;
		}}
		return ersetzen;
	}
	private boolean aufErsetzenPruefen(String name) {
		neuProfileName = name;
		boolean ersetzen = false;
		if(profiles != null){
		for(File f: profiles){
			if(f.getName().equals(name+".prof")) ersetzen = true;
		}}
		return ersetzen;
	}

	private void speichernDurchfuehren(String name){
		Profile currentProfile = Profile.getDefaultProfile();
		currentProfile.setName(name);
		X.getInstance().closeDialog();
		currentProfile.saveProfileToFile();
		updateProfiles();
	}
}
