package games.werLuegt;

import games.dialogeGUIs.GameSettingsDialog;

import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class WerLuegtSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String AUSSAGEZEIT = "Zeit pro Aussage";
	private WerLuegt werLuegt;
	private JSlider zeitSlider;

	public WerLuegtSettingsDialog(WerLuegt w, boolean inGame) {
		super(w, inGame);
		this.werLuegt = w;
		initGUI();
		propertiesToSettings();
	}

	private void initGUI() {
		zeitSlider = new JSlider(SwingConstants.HORIZONTAL, 3, 15,
				werLuegt.timeProAussage);
		zeitSlider.setMajorTickSpacing(1);
		zeitSlider.setMinorTickSpacing(1);
		zeitSlider.setSnapToTicks(true);
		zeitSlider.setPaintTicks(true);
		zeitSlider.setPaintLabels(true);
		addSettingsComponent("Zeit pro Aussage (s):", zeitSlider);
	}
	
	@Override
	public void speichern(){
		settingsToProperties();
		super.speichern();
	}
	
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || zeitSlider == null){
			return;
		}
		String timeProAussage = settings.getProperty(AUSSAGEZEIT, "5");
		zeitSlider.setValue(Integer.parseInt(timeProAussage));
	}
	
	public void settingsToProperties(){
		super.settingsToProperties();
		try{
			String zeit = zeitSlider.getValue()+"";
			settings.setProperty(AUSSAGEZEIT, zeit);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
