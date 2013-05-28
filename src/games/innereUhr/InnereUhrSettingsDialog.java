package games.innereUhr;
import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InnereUhrSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 210954385464236916L;
	public InnereUhr iUhr;
	private JPanel hauptbereichPanel;
	private JCheckBox toleranzCheckBox;
	private JLabel toleranzOnLabel;
	private JSlider toleranzSlider;
	private JLabel toleranzLabel;
	private JTextField maxDauerTextField;
	private JLabel maxDauerLabel;
	private JTextField minDauerTextField;
	private JLabel minDauerLabel;
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	public InnereUhrSettingsDialog(InnereUhr iUhr){
		super(iUhr);
		this.iUhr = iUhr;
		initGUI();
	}

	private void initGUI(){
		{
			hauptbereichPanel = new JPanel();
			GridLayout hauptbereichPanelLayout = new GridLayout(4, 2);
			hauptbereichPanelLayout.setHgap(5);
			hauptbereichPanelLayout.setVgap(5);
			hauptbereichPanelLayout.setColumns(2);
			hauptbereichPanelLayout.setRows(5);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			{
				minDauerLabel = new JLabel();
				hauptbereichPanel.add(minDauerLabel);
				minDauerLabel.setText("Mindestdauer in s");
			}
			{
				minDauerTextField = new JTextField();
				minDauerTextField.setText(iUhr.minGuessTime+"");
				hauptbereichPanel.add(minDauerTextField);
			}
			{
				maxDauerLabel = new JLabel();
				hauptbereichPanel.add(maxDauerLabel);
				maxDauerLabel.setText("Höchstdauer in s");
			}
			{
				maxDauerTextField = new JTextField();
				maxDauerTextField.setText(iUhr.maxGuessTime+"");
				hauptbereichPanel.add(maxDauerTextField);
			}
			{
				toleranzLabel = new JLabel();
				hauptbereichPanel.add(toleranzLabel);
				toleranzLabel.setText("Toleranz in s");
			}
			{
				toleranzSlider = new JSlider(SwingConstants.HORIZONTAL,0,50,10);
				toleranzSlider.setValue(iUhr.toleranz/1000);
				if(iUhr.modus == Modus.SOLO)toleranzSlider.setMaximum(10);
				toleranzSlider.setMajorTickSpacing(10);
				toleranzSlider.setMinorTickSpacing(1);
				toleranzSlider.setSnapToTicks(true);
				toleranzSlider.setPaintTicks(true);
				toleranzSlider.setPaintLabels(true);
				toleranzSlider.setEnabled(iUhr.toleranzOn);
				hauptbereichPanel.add(toleranzSlider);
			}
			{
				toleranzOnLabel = new JLabel();
				hauptbereichPanel.add(toleranzOnLabel);
				toleranzOnLabel.setText("Toleranz anschalten");
			}
			{
				toleranzCheckBox = new JCheckBox();
				toleranzCheckBox.setSelected(iUhr.toleranzOn);
				hauptbereichPanel.add(toleranzCheckBox);
				toleranzCheckBox.setText("Toleranz");
				toleranzCheckBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						toleranzCheckBoxActionPerformed(evt);
					}
				});
				if(iUhr.modus == Modus.SOLO)toleranzCheckBox.setEnabled(false);
			}
			{
				rundenzahlLabel = new JLabel("Siegpunktzahl");
				hauptbereichPanel.add(rundenzahlLabel);
			}
			{
				rundenzahlSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,iUhr.numOfRounds);
				rundenzahlSlider.setMajorTickSpacing(1);
				rundenzahlSlider.setMinorTickSpacing(1);
				rundenzahlSlider.setSnapToTicks(true);
				rundenzahlSlider.setPaintTicks(true);
				rundenzahlSlider.setPaintLabels(true);
				hauptbereichPanel.add(rundenzahlSlider);
			}
		}
	}

	@Override
	public void speichern(){
		try{
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
			iUhr.minGuessTime = min;
			iUhr.maxGuessTime = max;
			iUhr.toleranzOn = tol;
			iUhr.numOfRounds = rundenzahlSlider.getValue();
			if(tol)iUhr.toleranz = toleranz*1000;
			iUhr.nextRound();
		}
		catch(Exception e){
			EasyDialog.showMessage("Bitte nur ganze Zahlen in die Textfelder eingeben");
		}
		super.speichern();
	}

	private void toleranzCheckBoxActionPerformed(ActionEvent evt) {
		toleranzSlider.setEnabled(toleranzCheckBox.isSelected());
	}
}
