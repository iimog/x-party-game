package settings;

import gui.EasyDialog;
import gui.components.DefaultButton;
import gui.menu.ProfilManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import start.X;

public class MainSettingsDialog  extends gui.AnzeigeDialog {
	private static final long serialVersionUID = 210954385464236916L;
	public MainSettings ms = MainSettings.getMainSettings();
	private JPanel hauptbereichPanel;
	private JCheckBox soundCheckBox;
	private JLabel soundLabel;
	private JLabel fullscreenLabel;
	private JButton verwerfenButton;
	private JButton speichernButton;
	private JPanel buttonPanel;
	private JCheckBox fullscreenCheckBox;
	private JLabel profilLabel;
	private JButton profilButton;
	public MainSettingsDialog(){
		initGUI();
	}

	private void initGUI(){
		{
			dialogPane.setLayout(new BorderLayout());
			hauptbereichPanel = new JPanel();
			GridLayout hauptbereichPanelLayout = new GridLayout(2, 2);
			hauptbereichPanelLayout.setHgap(5);
			hauptbereichPanelLayout.setVgap(5);
			hauptbereichPanelLayout.setColumns(2);
			hauptbereichPanelLayout.setRows(3);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			hauptbereichPanel.setOpaque(false);
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			initFullscreenOption();
			initSoundOption();
			initProfilOption();
		}
		{
			buttonPanel = new JPanel();
			buttonPanel.setOpaque(false);
			dialogPane.add(buttonPanel, BorderLayout.SOUTH);
			{
				speichernButton = new DefaultButton();
				buttonPanel.add(speichernButton);
				speichernButton.setText("Speichern");
				speichernButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						speichernButtonActionPerformed(evt);
					}
				});
			}
			{
				verwerfenButton = new DefaultButton();
				buttonPanel.add(verwerfenButton);
				verwerfenButton.setText("Verwerfen");
				verwerfenButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						verwerfenButtonActionPerformed(evt);
					}
				});
			}
		}
	}

	private void initProfilOption() {
		{
			profilLabel = new JLabel("Profile");
			profilLabel.setFont(X.BUTTON_FONT);
			profilLabel.setForeground(Color.WHITE);
			hauptbereichPanel.add(profilLabel);
		}
		{
			profilButton = new DefaultButton("Profil Manager");
			profilButton.addActionListener(new ActionListener() {					
				@Override
				public void actionPerformed(ActionEvent arg0) {
					X.getInstance().showDialog(new ProfilManager());
				}
			});
			hauptbereichPanel.add(profilButton);
		}
	}

	private void initSoundOption() {
		{
			soundLabel = new JLabel();
			soundLabel.setFont(X.BUTTON_FONT);
			soundLabel.setForeground(Color.white);
			hauptbereichPanel.add(soundLabel);
			soundLabel.setText("Sound");
		}
		{
			soundCheckBox = new JCheckBox();
			soundCheckBox.setOpaque(false);
			soundCheckBox.setFont(X.BUTTON_FONT);
			soundCheckBox.setForeground(Color.white);
			setSoundCheckBoxSelected(ms.isSoundOn());
			soundCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSoundCheckBoxSelected(soundCheckBox.isSelected());
				}
			});
			hauptbereichPanel.add(soundCheckBox);
		}
	}

	private void initFullscreenOption() {
		{
			fullscreenLabel = new JLabel();
			fullscreenLabel.setFont(X.BUTTON_FONT);
			fullscreenLabel.setForeground(Color.white);
			hauptbereichPanel.add(fullscreenLabel);
			fullscreenLabel.setText("Fullscreen Modus");
		}
		{
			fullscreenCheckBox = new JCheckBox();
			fullscreenCheckBox.setOpaque(false);
			fullscreenCheckBox.setFont(X.BUTTON_FONT);
			fullscreenCheckBox.setForeground(Color.white);
			boolean full = ms.isFullscreen();
			setFullscreenCheckboxSelected(full);
			fullscreenCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					fullscreenCheckBoxActionPerformed(evt);
				}
			});
			hauptbereichPanel.add(fullscreenCheckBox);
		}
	}

	protected void fullscreenCheckBoxActionPerformed(ActionEvent evt) {
		setFullscreenCheckboxSelected(fullscreenCheckBox.isSelected());
		EasyDialog.showMessage("Eine Änderung des Vollbildstatus wird" +
				" erst beim nächsten Start wirksam.");
	}

	private void setSoundCheckBoxSelected(boolean soundOn) {
		soundCheckBox.setSelected(soundOn);
		if(soundOn){
			soundCheckBox.setText("on");
		}
		else{
			soundCheckBox.setText("off");
		}
	}

	private void setFullscreenCheckboxSelected(boolean full) {
		fullscreenCheckBox.setSelected(full);
		if(full){
			fullscreenCheckBox.setText("on");
		}
		else{
			fullscreenCheckBox.setText("off");
		}
	}

	private void speichernButtonActionPerformed(ActionEvent evt) {
		ms.setFullscreen(fullscreenCheckBox.isSelected());
		ms.setSound(soundCheckBox.isSelected());
		instance.closeDialog();
	}
	private void verwerfenButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}
}