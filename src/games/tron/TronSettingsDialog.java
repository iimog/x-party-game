package games.tron;

import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;

public class TronSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String GESCHWINDIGKEIT = "Geschwindigkeit";
	public static final String GITTER = "Gitter";
	public static final String HOEHE = "Hoehe";
	public static final String BREITE = "Breite";
	public static final String GROESSE = "Groesse";
	private Tron tron;
	private JSlider geschwindigkeitSlider;
	private JCheckBox gitterCheckBox;
	private JTextField breiteTextField;
	private JTextField hoeheTextField;
	private JSlider flaechenGroesseSlider;
	
	public TronSettingsDialog(Tron tron, boolean inGame){
		super(tron, inGame);
		this.tron = tron;
		initGUI();
		propertiesToSettings();
	}
	
	private void initGUI(){
		{
			{
				geschwindigkeitSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,500/tron.sleepTime);
				geschwindigkeitSlider.setMajorTickSpacing(1);
				geschwindigkeitSlider.setMinorTickSpacing(1);
				geschwindigkeitSlider.setSnapToTicks(true);
				geschwindigkeitSlider.setPaintTicks(true);
				geschwindigkeitSlider.setPaintLabels(true);
				addSettingsComponent("Geschwindigkeit", geschwindigkeitSlider);
			}
			{
				gitterCheckBox = new JCheckBox();
				gitterCheckBox.setSelected(tron.isGitterVisible());
				addSettingsComponent("Gitter", gitterCheckBox);
			}
			{
				breiteTextField = new JTextField(tron.colums+"");
				addSettingsComponent("Breite", breiteTextField);
			}
			{
				hoeheTextField = new JTextField(tron.rows+"");
				addSettingsComponent("Höhe", hoeheTextField);
			}
			{
				flaechenGroesseSlider = new JSlider(SwingConstants.HORIZONTAL,5,25,tron.feldGroesse.height);
				flaechenGroesseSlider.setMajorTickSpacing(5);
				flaechenGroesseSlider.setMinorTickSpacing(1);
				flaechenGroesseSlider.setSnapToTicks(true);
				flaechenGroesseSlider.setPaintTicks(true);
				flaechenGroesseSlider.setPaintLabels(true);
				addSettingsComponent("Flächengröße (Pixel)", flaechenGroesseSlider);
			}
		}
	}
	
	
	public void settingsToProperties(){
		super.settingsToProperties();
		settings.setProperty(GESCHWINDIGKEIT, geschwindigkeitSlider.getValue()+"");
		settings.setProperty(GITTER, gitterCheckBox.isSelected()+"");
		int feldGroesse = flaechenGroesseSlider.getValue();
		settings.setProperty(GROESSE, feldGroesse+"");
		try{
			int rows = Integer.parseInt(hoeheTextField.getText().trim());
			int columns = Integer.parseInt(breiteTextField.getText().trim());
			int gesamtBreite = columns*feldGroesse;
			int gesamtHoehe = rows*feldGroesse;
			if(gitterCheckBox.isSelected()){
				gesamtBreite += 2*columns+2;
				gesamtHoehe += 2*rows+2;
			}
			int verfuegbareBreite = Toolkit.getDefaultToolkit().getScreenSize().width-60;
			int verfuegbareHoehe = Toolkit.getDefaultToolkit().getScreenSize().height-100;
			if(gesamtHoehe>verfuegbareHoehe || gesamtBreite>verfuegbareBreite){
				EasyDialog.showMessage("Der Bildschirm ist zu klein für die gewählte Einstellung. " +
						"Nötig: " + gesamtBreite + "x" + gesamtHoehe + 
						" Vorhanden:"  + verfuegbareBreite + "x" + verfuegbareHoehe);
				return;
			}
			settings.setProperty(HOEHE, rows+"");
			settings.setProperty(BREITE, columns+"");
		}
		catch(Exception e){
			EasyDialog.showMessage("Bitte nur ganze Zahlen in die Textfelder eingeben");
		}
	}
	
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || geschwindigkeitSlider == null){
			return;
		}
		int geschwindigkeit = Integer.parseInt(settings.getProperty(TronSettingsDialog.GESCHWINDIGKEIT, "5"));
		boolean gitterVisible = (Boolean.parseBoolean(settings.getProperty(TronSettingsDialog.GITTER, "true")));
		String rows = settings.getProperty(TronSettingsDialog.HOEHE, "50");
		String columns = settings.getProperty(TronSettingsDialog.BREITE, "100");
		int groesse = Integer.parseInt(settings.getProperty(TronSettingsDialog.GROESSE, "10"));
		geschwindigkeitSlider.setValue(geschwindigkeit);
		gitterCheckBox.setSelected(gitterVisible);
		hoeheTextField.setText(rows);
		breiteTextField.setText(columns);
		flaechenGroesseSlider.setValue(groesse);
	}

	@Override
	public void speichern(){
		settingsToProperties();
		super.speichern();
	}
}
