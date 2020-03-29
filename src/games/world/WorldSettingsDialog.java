package games.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;


public class WorldSettingsDialog extends GameSettingsDialog {
	/**
	 * SerialVersionUID generated by Eclipse
	 */
	private static final long serialVersionUID = 1L;
	public static final String TOLERANZ_ON = "Toleranz aktiviert";
	public static final String TOLERANZ_KM = "Toleranz in km";

	private World world;

	private JSlider toleranzSlider;

	private JCheckBox toleranzCheckBox;
	public WorldSettingsDialog(World world) {
		super(world);
		this.world = world;
		initGUI();
		propertiesToSettings();
	}

	private void initGUI(){
		{
			toleranzSlider = new JSlider(SwingConstants.HORIZONTAL,0,500,world.toleranz);
			toleranzSlider.setValue(world.toleranz);
			toleranzSlider.setMajorTickSpacing(100);
			toleranzSlider.setMinorTickSpacing(10);
			toleranzSlider.setSnapToTicks(true);
			toleranzSlider.setPaintTicks(true);
			toleranzSlider.setPaintLabels(true);
			toleranzSlider.setEnabled(world.toleranzOn);
			addSettingsComponent("Toleranz in km", toleranzSlider);
		}
		{
			toleranzCheckBox = new JCheckBox();
			toleranzCheckBox.setSelected(world.toleranzOn);
			toleranzCheckBox.setText("Toleranz");
			if(world.modus == Modus.SOLO)toleranzCheckBox.setEnabled(false);
			toleranzCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					toleranzCheckBoxActionPerformed(evt);
				}
			});
			addSettingsComponent("Toleranz anschalten", toleranzCheckBox);
		}
	}
	
	private void toleranzCheckBoxActionPerformed(ActionEvent evt) {
		toleranzSlider.setEnabled(toleranzCheckBox.isSelected());
	}
	
	public void settingsToProperties(){
		super.settingsToProperties();
		settings.setProperty(TOLERANZ_ON, ""+toleranzCheckBox.isSelected());
		settings.setProperty(TOLERANZ_KM, ""+toleranzSlider.getValue());
	}
	
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || toleranzSlider == null){
			return;
		}
		String toleranzOn = settings.getProperty(TOLERANZ_ON, "true");
		if(world.modus != Modus.SOLO) {
			toleranzCheckBox.setSelected(Boolean.parseBoolean(toleranzOn));
		}
		String toleranzKm = settings.getProperty(TOLERANZ_KM, "500");
		toleranzSlider.setValue(Integer.parseInt(toleranzKm));
	}

	@Override
	public void speichern(){
		super.speichern();
		world.toleranzOn = toleranzCheckBox.isSelected();
		world.toleranz = toleranzSlider.getValue();
	}
}
