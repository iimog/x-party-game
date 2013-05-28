package games.memory;
import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MemorySettingsDialog extends GameSettingsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6318517814751967329L;
	Memory game;
	private JPanel hauptbereichPanel;
	private JLabel paareLabel;
	private JComboBox paareComboBox;
	private JPanel buttonPanel;
	private JButton speichernButton;
	private JButton verwerfenButton;
	String[] moeglichePaare = new String[5];
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	private GridLayout hauptbereichPanelLayout;
	private JLabel schwierigkeitLabel;
	private JSlider schwierigkeitSlider;{
		moeglichePaare[0] = "27";
		moeglichePaare[1] = "24";
		moeglichePaare[2] = "20";
		moeglichePaare[3] = "15";
		moeglichePaare[4] = "10";
	}
	public MemorySettingsDialog(Memory m){
		super(m);
		game = m;
		initGUI();
	}

	private void initGUI(){
		{
			hauptbereichPanel = new JPanel();
			hauptbereichPanelLayout = new GridLayout(2, 2);
			hauptbereichPanelLayout.setHgap(5);
			hauptbereichPanelLayout.setVgap(5);
			hauptbereichPanelLayout.setColumns(2);
			dialogPane.setLayout(new BorderLayout());
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			{
				paareLabel = new JLabel();
				hauptbereichPanel.add(paareLabel);
				paareLabel.setText("Pärchen:");
			}
			{
				ComboBoxModel paareComboBoxModel =
					new DefaultComboBoxModel(moeglichePaare);
				paareComboBox = new JComboBox();
				hauptbereichPanel.add(paareComboBox);
				paareComboBox.setModel(paareComboBoxModel);
				paareComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateRundenzahlSlider();
					}
				});
			}
			{
				rundenzahlLabel = new JLabel("Siegpunktzahl");
				hauptbereichPanel.add(rundenzahlLabel);
			}
			{
				int maxRunden = Integer.parseInt(moeglichePaare[paareComboBox.getSelectedIndex()]);
				maxRunden = maxRunden/2+1;
				rundenzahlSlider = new JSlider(SwingConstants.HORIZONTAL,1,maxRunden,game.numOfRounds);
				rundenzahlSlider.setMajorTickSpacing(1);
				rundenzahlSlider.setMinorTickSpacing(1);
				rundenzahlSlider.setSnapToTicks(true);
				rundenzahlSlider.setPaintTicks(true);
				rundenzahlSlider.setPaintLabels(true);
				hauptbereichPanel.add(rundenzahlSlider);
			}
			if(game.modus == Modus.SOLO){
				addSchwierigkeitSlider();
			}
		}
		{
			buttonPanel = new JPanel();
			dialogPane.add(buttonPanel, BorderLayout.SOUTH);{
				{
					speichernButton = new JButton();
					buttonPanel.add(speichernButton);
					speichernButton.setText("Speichern");
					speichernButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							speichernButtonActionPerformed(evt);
						}
					});
				}
				{
					verwerfenButton = new JButton();
					buttonPanel.add(verwerfenButton);
					verwerfenButton.setText("Verwerfen");
					verwerfenButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							verwerfenButtonActionPerformed(evt);
						}
					});
				}
			}
		}
	}
	private void addSchwierigkeitSlider() {
		hauptbereichPanelLayout.setRows(3);
		{
			schwierigkeitLabel = new JLabel("Schwierigkeitsstufe");
			hauptbereichPanel.add(schwierigkeitLabel);
		}
		{
			schwierigkeitSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,game.getSchwierigkeit());
			schwierigkeitSlider.setMajorTickSpacing(1);
			schwierigkeitSlider.setMinorTickSpacing(1);
			schwierigkeitSlider.setSnapToTicks(true);
			schwierigkeitSlider.setPaintTicks(true);
			schwierigkeitSlider.setPaintLabels(true);
			hauptbereichPanel.add(schwierigkeitSlider);
		}
	}

	private void speichernButtonActionPerformed(ActionEvent evt) {
		int numOfPairs = Integer.parseInt(moeglichePaare[paareComboBox.getSelectedIndex()]);
		game.paarZahl(numOfPairs);
		game.numOfRounds = rundenzahlSlider.getValue();
		if(game.modus==Modus.SOLO)game.setSchwierigkeit(schwierigkeitSlider.getValue());
		super.speichern();
	}
	private void verwerfenButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}
	private void updateRundenzahlSlider(){
		int maxRunden = Integer.parseInt(moeglichePaare[paareComboBox.getSelectedIndex()]);
		maxRunden = maxRunden/2+1;
		rundenzahlSlider.setMaximum(maxRunden);
	}

}
