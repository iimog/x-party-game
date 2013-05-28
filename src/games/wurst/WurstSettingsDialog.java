package games.wurst;

import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class WurstSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	private Wurst wurst;
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	private JLabel startGewichtLabel;
	private JTextField startGewichtTextField;

	public WurstSettingsDialog(Wurst wurst) {
		super(wurst);
		this.wurst = wurst;
		initGUI();
	}
	
	private void initGUI(){
		settingsPanel.setLayout(new GridLayout(2,2));
		rundenzahlLabel = new JLabel("Siegpunktzahl");
		settingsPanel.add(rundenzahlLabel);
		rundenzahlSlider = new JSlider(SwingConstants.HORIZONTAL,2,10,wurst.numOfRounds);
		rundenzahlSlider.setMajorTickSpacing(1);
		rundenzahlSlider.setMinorTickSpacing(1);
		rundenzahlSlider.setSnapToTicks(true);
		rundenzahlSlider.setPaintTicks(true);
		rundenzahlSlider.setPaintLabels(true);
		settingsPanel.add(rundenzahlSlider);
		startGewichtLabel = new JLabel("Startgewicht der Wurst:");
		settingsPanel.add(startGewichtLabel);
		startGewichtTextField = new JTextField(wurst.anfangsWert+"");
		settingsPanel.add(startGewichtTextField);
	}

	@Override
	public void speichern(){
		try{
			String startGewicht = startGewichtTextField.getText();
			int ausgangsWert = Integer.parseInt(startGewicht);
			if(ausgangsWert<10){
				throw new Exception("Wert zu klein!");
			}
			wurst.anfangsWert = ausgangsWert;
			wurst.numOfRounds = rundenzahlSlider.getValue();
			super.speichern();
		}
		catch(Exception e){
			EasyDialog.showMessage("Bitte gib ins Textfeld einen sinnvollen Wert (Zahl >9) ein");
		}
	}
}
