package games.reaktionszeit;

import games.dialogeGUIs.GameSettingsDialog;

import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class ReaktionsZeitSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String ROTATION_TIME = "Rotationtime";
	private ReaktionsZeit reaktionsZeit;
	private JSlider rotationTimeSlider;

	public ReaktionsZeitSettingsDialog(ReaktionsZeit reaktionsZeit) {
		this(reaktionsZeit, false);
	}

	public ReaktionsZeitSettingsDialog(ReaktionsZeit reaktionsZeit, boolean inGame) {
		super(reaktionsZeit, inGame);
		this.reaktionsZeit = reaktionsZeit;
		initGUI();
		propertiesToSettings();
	}
	
	private void initGUI(){
		addRotationTimeSlider();
	}
	
	private void addRotationTimeSlider() {
		{
			rotationTimeSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,reaktionsZeit.getRotationTime());
			rotationTimeSlider.setMajorTickSpacing(1);
			rotationTimeSlider.setMinorTickSpacing(1);
			rotationTimeSlider.setSnapToTicks(true);
			rotationTimeSlider.setPaintTicks(true);
			rotationTimeSlider.setPaintLabels(true);
			addSettingsComponent("Rotationszeit", rotationTimeSlider);
		}
	}
	
	@Override
	public void settingsToProperties(){
		super.settingsToProperties();
		settings.setProperty(ROTATION_TIME, ""+rotationTimeSlider.getValue());
	}
	
	@Override
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null){
			return;
		}
		if(myGame == null){
			System.out.println("game ist null");
			return;
		}
		String rotationTime = settings.getProperty(ROTATION_TIME);
		if(rotationTime != null && rotationTimeSlider != null){
			rotationTimeSlider.setValue(Integer.parseInt(rotationTime));
		}
	}
	
	@Override
	public void speichern() {
		settingsToProperties();
		super.speichern();
	}

}
