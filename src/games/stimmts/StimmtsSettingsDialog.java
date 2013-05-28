package games.stimmts;

import games.dialogeGUIs.GameSettingsDialog;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class StimmtsSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	private Stimmts stimmts;
	private GridLayout settingsPanelLayout;
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	private JLabel zeitLabel;
	private JSlider zeitSlider;

	public StimmtsSettingsDialog(Stimmts stimmts) {
		super(stimmts);
		this.stimmts = stimmts;
		initGUI();
		}

		private void initGUI(){
			settingsPanelLayout = new GridLayout(2,2);
			settingsPanel.setLayout(settingsPanelLayout);
			rundenzahlLabel = new JLabel("Siegpunktzahl");
			settingsPanel.add(rundenzahlLabel);
			rundenzahlSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,stimmts.numOfRounds);
			rundenzahlSlider.setMajorTickSpacing(1);
			rundenzahlSlider.setMinorTickSpacing(1);
			rundenzahlSlider.setSnapToTicks(true);
			rundenzahlSlider.setPaintTicks(true);
			rundenzahlSlider.setPaintLabels(true);
			settingsPanel.add(rundenzahlSlider);
			zeitLabel = new JLabel("Zeit pro Aussage (s)");
			settingsPanel.add(zeitLabel);
			zeitSlider = new JSlider(SwingConstants.HORIZONTAL,3,20,stimmts.numOfRounds);
			zeitSlider.setMajorTickSpacing(3);
			zeitSlider.setMinorTickSpacing(1);
			zeitSlider.setSnapToTicks(true);
			zeitSlider.setPaintTicks(true);
			zeitSlider.setPaintLabels(true);
			settingsPanel.add(zeitSlider);
		}

		@Override
		public void speichern(){
			stimmts.numOfRounds = rundenzahlSlider.getValue();
			stimmts.timeProAussage = zeitSlider.getValue();
			super.speichern();
		}
}
