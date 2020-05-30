package games.tron;

import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TronSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	private Tron tron;
	private JPanel hauptbereichPanel;
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	private JLabel geschwindigkeitLabel;
	private JSlider geschwindigkeitSlider;
	private JLabel gitterLabel;
	private JCheckBox gitterCheckBox;
	private JLabel breiteLabel;
	private JTextField breiteTextField;
	private JLabel hoeheLabel;
	private JTextField hoeheTextField;
	private JLabel flaechenGroesseLabel;
	private JSlider flaechenGroesseSlider;
	
	public TronSettingsDialog(Tron tron, boolean inGame){
		super(tron, inGame);
		this.tron = tron;
		initGUI();
	}
	
	private void initGUI(){
		{
			hauptbereichPanel = new JPanel();
			GridLayout hauptbereichPanelLayout = new GridLayout(6, 2);
			hauptbereichPanelLayout.setHgap(5);
			hauptbereichPanelLayout.setVgap(5);
			hauptbereichPanelLayout.setColumns(2);
			hauptbereichPanelLayout.setRows(6);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			{
				rundenzahlLabel = new JLabel("Siegpunktzahl");
				hauptbereichPanel.add(rundenzahlLabel);
				rundenzahlSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,tron.numOfRounds);
				rundenzahlSlider.setMajorTickSpacing(1);
				rundenzahlSlider.setMinorTickSpacing(1);
				rundenzahlSlider.setSnapToTicks(true);
				rundenzahlSlider.setPaintTicks(true);
				rundenzahlSlider.setPaintLabels(true);
				hauptbereichPanel.add(rundenzahlSlider);
			}
			{
				geschwindigkeitLabel = new JLabel("Geschwindigkeit");
				hauptbereichPanel.add(geschwindigkeitLabel);
				geschwindigkeitSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,500/tron.sleepTime);
				geschwindigkeitSlider.setMajorTickSpacing(1);
				geschwindigkeitSlider.setMinorTickSpacing(1);
				geschwindigkeitSlider.setSnapToTicks(true);
				geschwindigkeitSlider.setPaintTicks(true);
				geschwindigkeitSlider.setPaintLabels(true);
				hauptbereichPanel.add(geschwindigkeitSlider);
			}
			{
				gitterLabel = new JLabel("Gitter");
				hauptbereichPanel.add(gitterLabel);
				gitterCheckBox = new JCheckBox();
				gitterCheckBox.setSelected(tron.isGitterVisible());
				hauptbereichPanel.add(gitterCheckBox);
			}
			{
				breiteLabel = new JLabel("Breite");
				hauptbereichPanel.add(breiteLabel);
				breiteTextField = new JTextField(tron.colums+"");
				hauptbereichPanel.add(breiteTextField);
			}
			{
				hoeheLabel = new JLabel("Höhe");
				hauptbereichPanel.add(hoeheLabel);
				hoeheTextField = new JTextField(tron.rows+"");
				hauptbereichPanel.add(hoeheTextField);
			}
			{
				flaechenGroesseLabel = new JLabel("Flächengröße (Pixel)");
				hauptbereichPanel.add(flaechenGroesseLabel);
				flaechenGroesseSlider = new JSlider(SwingConstants.HORIZONTAL,5,25,tron.feldGroesse.height);
				flaechenGroesseSlider.setMajorTickSpacing(5);
				flaechenGroesseSlider.setMinorTickSpacing(1);
				flaechenGroesseSlider.setSnapToTicks(true);
				flaechenGroesseSlider.setPaintTicks(true);
				flaechenGroesseSlider.setPaintLabels(true);
				hauptbereichPanel.add(flaechenGroesseSlider);
			}
		}
	}

	@Override
	public void speichern(){
		super.speichern();
		try{
			tron.numOfRounds = rundenzahlSlider.getValue();
			tron.sleepTime = 500/geschwindigkeitSlider.getValue();
			tron.setGitterVisible(gitterCheckBox.isSelected());
			tron.rows = Integer.parseInt(hoeheTextField.getText().trim());
			tron.colums = Integer.parseInt(breiteTextField.getText().trim());
			tron.feldGroesse = new Dimension(flaechenGroesseSlider.getValue(),flaechenGroesseSlider.getValue());
		}
		catch(Exception e){
			EasyDialog.showMessage("Bitte nur ganze Zahlen in die Textfelder eingeben");
		}	
		int gesamtBreite = tron.colums*tron.feldGroesse.width;
		int gesamtHoehe = tron.rows*tron.feldGroesse.height;
		if(tron.isGitterVisible()){
			gesamtBreite += 2*tron.colums+2;
			gesamtHoehe += 2*tron.rows+2;
		}
		int verfuegbareBreite = Toolkit.getDefaultToolkit().getScreenSize().width-60;
		int verfuegbareHoehe = Toolkit.getDefaultToolkit().getScreenSize().height-100;
		if(gesamtHoehe>verfuegbareHoehe || gesamtBreite>verfuegbareBreite){
			EasyDialog.showMessage("Der Bildschirm ist zu klein für die gewählte Einstellung. " +
					"Nötig: " + gesamtBreite + "x" + gesamtHoehe + 
					" Vorhanden:"  + verfuegbareBreite + "x" + verfuegbareHoehe);
			return;
		}
	}
}
