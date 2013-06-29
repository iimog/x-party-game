package games.memory;
import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;
import gui.components.Bildschirm;

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
	private JComboBox<String> backsideComboBox;
	private JComboBox<String> paareComboBox;
	private JComboBox<String> deckComboBox;
	private JPanel buttonPanel;
	private JButton speichernButton;
	private JButton verwerfenButton;
	private JLabel rundenzahlLabel;
	private JSlider rundenzahlSlider;
	private GridLayout hauptbereichPanelLayout;
	private JLabel schwierigkeitLabel;
	private JSlider schwierigkeitSlider;
	private JLabel deckLabel;
	String[] moeglichePaare = new String[5];
	private JLabel backsideLabel;
	private Bildschirm backsidePreview;
	{
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
			hauptbereichPanelLayout = new GridLayout(4, 2);
			hauptbereichPanelLayout.setHgap(5);
			hauptbereichPanelLayout.setVgap(5);
			hauptbereichPanelLayout.setColumns(2);
			dialogPane.setLayout(new BorderLayout());
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			{
				JPanel previewPanel = new JPanel();
				backsidePreview = new Bildschirm(game.backside);
				previewPanel.add(backsidePreview);
				dialogPane.add(previewPanel, BorderLayout.EAST);
			}
			{
				backsideLabel = new JLabel();
				hauptbereichPanel.add(backsideLabel);
				backsideLabel.setText("Rückseite:");
			}
			{
				ComboBoxModel<String> backsideComboBoxModel =
					new DefaultComboBoxModel<String>(game.getBacksides().toArray(new String[1]));
				
				backsideComboBox = new JComboBox<String>();
				hauptbereichPanel.add(backsideComboBox);
				backsideComboBox.setModel(backsideComboBoxModel);
				backsideComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						backsidePreview.changePic((String)backsideComboBox.getSelectedItem());
					}
				});
			}
			{
				deckLabel = new JLabel();
				hauptbereichPanel.add(deckLabel);
				deckLabel.setText("Deck:");
			}
			{
				ComboBoxModel<String> deckComboBoxModel =
					new DefaultComboBoxModel<String>(game.getMemDeckNames(true).toArray(new String[1]));
				
				deckComboBox = new JComboBox<String>();
				hauptbereichPanel.add(deckComboBox);
				deckComboBox.setModel(deckComboBoxModel);
				deckComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateKartenZahl();
						updateRundenzahlSlider();
					}
				});
			}
			{
				paareLabel = new JLabel();
				hauptbereichPanel.add(paareLabel);
				paareLabel.setText("Pärchen:");
			}
			{
				ComboBoxModel<String> paareComboBoxModel =
					new DefaultComboBoxModel<String>(moeglichePaare);
				paareComboBox = new JComboBox<String>();
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
	protected void updateKartenZahl() {
		if(deckComboBox.getSelectedIndex()<2) 		// Zufall oder Alles
			return;
		int karten = game.getMemDeckMap().get(deckComboBox.getSelectedItem()).getPictures().size();
		int paare = Integer.parseInt(moeglichePaare[paareComboBox.getSelectedIndex()]);
		if(karten < paare){
			paare = Integer.parseInt(moeglichePaare[4]);
			if(karten < paare){
				EasyDialog.showMessage("Achtung! Dieses Deck hat zu wenige Paare bitte wählt ein anderes");
			}
			paareComboBox.setSelectedIndex(4);
			for(int i = 3; i>=0; i--){
				int nextPossibility = Integer.parseInt(moeglichePaare[i]);
				if(nextPossibility <= karten){
					karten = nextPossibility;
					paareComboBox.setSelectedIndex(i);
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
		game.setSelectedDeck((String)deckComboBox.getSelectedItem());
		game.backside = (String)backsideComboBox.getSelectedItem();
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
