package games.innereUhr;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;

public class InnereUhrSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 210954385464236916L;
	public static final String TOLERANZ_ON = "Toleranz aktiviert";
	public static final String TOLERANZ_S = "Toleranz in s";
	public static final String MAX_GUESS_TIME = "Max guess time";
	public static final String MIN_GUESS_TIME = "Min guess time";
	public InnereUhr iUhr;
	private JCheckBox toleranzCheckBox;
	private JSlider toleranzSlider;
	private JTextField maxDauerTextField;
	private JTextField minDauerTextField;
	public InnereUhrSettingsDialog(InnereUhr iUhr){
		super(iUhr);
		this.iUhr = iUhr;
		initGUI();
	}

	private void initGUI(){
		{
			minDauerTextField = new JTextField(iUhr.minGuessTime+"");
			addSettingsComponent("Mindestdauer in s", minDauerTextField);
			maxDauerTextField = new JTextField(iUhr.maxGuessTime+"");
			addSettingsComponent("Höchstdauer in s", maxDauerTextField);

			toleranzSlider = new JSlider(SwingConstants.HORIZONTAL,0,50,10);
			toleranzSlider.setValue(iUhr.toleranz/1000);
			if(iUhr.modus == Modus.SOLO)toleranzSlider.setMaximum(10);
			toleranzSlider.setMajorTickSpacing(10);
			toleranzSlider.setMinorTickSpacing(1);
			toleranzSlider.setSnapToTicks(true);
			toleranzSlider.setPaintTicks(true);
			toleranzSlider.setPaintLabels(true);
			toleranzSlider.setEnabled(iUhr.toleranzOn);
			addSettingsComponent("Toleranz in s", toleranzSlider);

			toleranzCheckBox = new JCheckBox();
			toleranzCheckBox.setSelected(iUhr.toleranzOn);
			toleranzCheckBox.setText("Toleranz");
			toleranzCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					toleranzCheckBoxActionPerformed(evt);
				}
			});
			if(iUhr.modus == Modus.SOLO)toleranzCheckBox.setEnabled(false);
			addSettingsComponent("Toleranz anschalten", toleranzCheckBox);
		}
	}

	@Override
	public void speichern(){
		settingsToProperties();
		super.speichern();
	}
	
	public void settingsToProperties(){
		super.settingsToProperties();
		try {
			int min = Integer.parseInt(minDauerTextField.getText());
			int max = Integer.parseInt(maxDauerTextField.getText());
			boolean tol = toleranzCheckBox.isSelected();
			int toleranz = toleranzSlider.getValue();
			if(min>max){
				int t = min;
				min = max;
				max = t;
				EasyDialog.showMessage("Du hast Mindest- und Höchstzeit vertauscht, ich habe das korrigiert ;-)");
			}
			settings.setProperty(TOLERANZ_ON, ""+tol);
			settings.setProperty(TOLERANZ_S, ""+toleranz);
			settings.setProperty(MAX_GUESS_TIME, ""+max);
			settings.setProperty(MIN_GUESS_TIME, ""+min);
		} catch(Exception e){
			EasyDialog.showMessage("Bitte nur ganze Zahlen in die Textfelder eingeben");
		}
	}

	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || toleranzSlider == null){
			return;
		}
		String toleranzOn = settings.getProperty(TOLERANZ_ON, "true");
		if(iUhr.modus != Modus.SOLO) {
			toleranzCheckBox.setSelected(Boolean.parseBoolean(toleranzOn));
		}
		String toleranzS = settings.getProperty(TOLERANZ_S, "10");
		toleranzSlider.setValue(Integer.parseInt(toleranzS));
		String maxGT = settings.getProperty(InnereUhrSettingsDialog.MAX_GUESS_TIME, "20");
		maxDauerTextField.setText(maxGT);
		String minGT = settings.getProperty(InnereUhrSettingsDialog.MIN_GUESS_TIME, "5");
		minDauerTextField.setText(minGT);
	}
	
	private void toleranzCheckBoxActionPerformed(ActionEvent evt) {
		toleranzSlider.setEnabled(toleranzCheckBox.isSelected());
	}
}
