package games.buchstabensalat;

import games.dialogeGUIs.GameSettingsDialog;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class BuchstabenSalatSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	private BuchstabenSalat salat;
	private GridLayout settingsPanelLayout;
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	private JLabel zeitNachBuzzerLabel;
	private JSlider zeitNachBuzzerSlider;
	private JLabel zeitProBuchstabeLabel;
	private JSlider zeitProBuchstabeSlider;

	public BuchstabenSalatSettingsDialog(BuchstabenSalat salat) {
		super(salat);
		this.salat = salat;
		initGUI();
		}

		private void initGUI(){
			settingsPanelLayout = new GridLayout(3,2);
			settingsPanel.setLayout(settingsPanelLayout);
			rundenzahlLabel = new JLabel("Siegpunktzahl");
			settingsPanel.add(rundenzahlLabel);
			rundenzahlSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,salat.numOfRounds);
			rundenzahlSlider.setMajorTickSpacing(1);
			rundenzahlSlider.setMinorTickSpacing(1);
			rundenzahlSlider.setSnapToTicks(true);
			rundenzahlSlider.setPaintTicks(true);
			rundenzahlSlider.setPaintLabels(true);
			settingsPanel.add(rundenzahlSlider);
			zeitNachBuzzerLabel = new JLabel("Zeit nach buzzern:");
			settingsPanel.add(zeitNachBuzzerLabel);
			/*if(salat.modus == Modus.SOLO){
				zeitBisVerlorenSlider = new JSlider(SwingConstants.HORIZONTAL,0,100,dif.getCdTime());
				zeitBisVerlorenSlider.setMajorTickSpacing(10);
			}
			else{*/
			zeitNachBuzzerSlider = new JSlider(SwingConstants.HORIZONTAL,3,21,salat.timeAfterBuzzer);
			zeitNachBuzzerSlider.setMajorTickSpacing(3);
			zeitNachBuzzerSlider.setMinorTickSpacing(1);
			zeitNachBuzzerSlider.setSnapToTicks(true);
			zeitNachBuzzerSlider.setPaintTicks(true);
			zeitNachBuzzerSlider.setPaintLabels(true);
			settingsPanel.add(zeitNachBuzzerSlider);
			/*if(dif.modus != Modus.SOLO){
				settingsPanelLayout.setRows(3);
				bildVerschwindenLabel = new JLabel("Bild nach buzzern ausblenden?");
				settingsPanel.add(bildVerschwindenLabel);
				bildVerschwindenCheckBox = new JCheckBox();
				bildVerschwindenCheckBox.setSelected(dif.bildAusblenden);
				settingsPanel.add(bildVerschwindenCheckBox);
			}*/
			zeitProBuchstabeLabel = new JLabel("Zeit pro Buchstabe:");
			settingsPanel.add(zeitProBuchstabeLabel);
			zeitProBuchstabeSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,(int)salat.timePerLetter/1000);
			zeitProBuchstabeSlider.setMajorTickSpacing(1);
			zeitProBuchstabeSlider.setMinorTickSpacing(1);
			zeitProBuchstabeSlider.setSnapToTicks(true);
			zeitProBuchstabeSlider.setPaintTicks(true);
			zeitProBuchstabeSlider.setPaintLabels(true);
			settingsPanel.add(zeitProBuchstabeSlider);
		}

		@Override
		public void speichern(){
			salat.numOfRounds = rundenzahlSlider.getValue();
			int zeit = zeitNachBuzzerSlider.getValue();
			salat.timeAfterBuzzer = zeit;
			salat.timePerLetter = zeitProBuchstabeSlider.getValue()*1000;
			/*if(dif.modus != Modus.SOLO)dif.bildAusblenden = bildVerschwindenCheckBox.isSelected();*/
			super.speichern();
		}
}
