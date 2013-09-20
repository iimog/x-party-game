package games.wurst;

import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;

import javax.swing.JTextField;

public class WurstSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String ANFANGS_WERT = "Anfangswert";
	private Wurst wurst;
	private JTextField startGewichtTextField;

	public WurstSettingsDialog(Wurst wurst) {
		super(wurst);
		this.wurst = wurst;
		initGUI();
		propertiesToSettings();
	}
	
	private void initGUI(){
		startGewichtTextField = new JTextField(wurst.anfangsWert+"");
		addSettingsComponent("Startgewicht der Wurst:", startGewichtTextField);
	}

	@Override
	public void speichern(){
		settingsToProperties();
		super.speichern();
	}
	
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || startGewichtTextField == null){
			return;
		}
		String anfangsWert = settings.getProperty(ANFANGS_WERT, "500");
		startGewichtTextField.setText(anfangsWert);
	}
	
	public void settingsToProperties(){
		super.settingsToProperties();
		try{
			String startGewicht = startGewichtTextField.getText();
			int ausgangsWert = Integer.parseInt(startGewicht);
			if(ausgangsWert<10){
				throw new Exception("Wert zu klein!");
			}
			wurst.anfangsWert = ausgangsWert;
			settings.setProperty(ANFANGS_WERT, ""+ausgangsWert);
		}
		catch(Exception e){
			e.printStackTrace();
			EasyDialog.showMessage("Bitte gib ins Textfeld einen sinnvollen Wert (Zahl >9) ein");
		}
	}
}
