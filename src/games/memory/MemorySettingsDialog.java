package games.memory;
import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;
import gui.EasyDialog;
import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
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
	private static final long serialVersionUID = 6318517814751967329L;
	public static final String BACKSIDE = "Rückseite";
	public static final String DECK = "Deck";
	public static final String PAIRS = "Paare";
	public static final String DIFFICULTY = "Schwierigkeit";
	Memory game;
	private JComboBox backsideComboBox;
	private JComboBox paareComboBox;
	private JComboBox deckComboBox;
	private JSlider schwierigkeitSlider;
	String[] moeglichePaare = new String[5];
	private Bildschirm backsidePreview;
	private Map<String, String> backsides;
	{
		moeglichePaare[0] = "27";
		moeglichePaare[1] = "24";
		moeglichePaare[2] = "20";
		moeglichePaare[3] = "15";
		moeglichePaare[4] = "10";
	}
	public MemorySettingsDialog(Memory m, boolean inGame){
		super(m, inGame);
		game = m;
		backsides = m.getBacksides();
		initGUI();
		propertiesToSettings();
	}

	private void initGUI(){
		{
			{
				JPanel previewPanel = new JPanel();
				backsidePreview = new Bildschirm(game.getBackside());
				previewPanel.add(backsidePreview);
				dialogPane.add(previewPanel, BorderLayout.EAST);
			}
			{
				ComboBoxModel backsideComboBoxModel =
					new DefaultComboBoxModel(game.getBacksides().keySet().toArray(new String[1]));
				backsideComboBox = new JComboBox();
				backsideComboBox.setModel(backsideComboBoxModel);
				backsideComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						backsidePreview.changePic(backsides.get(backsideComboBox.getSelectedItem()));
					}
				});
				addSettingsComponent("Rückseite", backsideComboBox);
			}
			{
				ComboBoxModel deckComboBoxModel =
					new DefaultComboBoxModel(game.getMemDeckNames(true).toArray(new String[1]));
				
				deckComboBox = new JComboBox();
				deckComboBox.setModel(deckComboBoxModel);
				deckComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateKartenZahl();
						updateRundenzahlSlider();
					}
				});
				if(isInGame()){
					deckComboBox.setEnabled(false);
				}
				addSettingsComponent("Deck", deckComboBox);
			}
			{
				ComboBoxModel paareComboBoxModel =
					new DefaultComboBoxModel(moeglichePaare);
				paareComboBox = new JComboBox();
				paareComboBox.setModel(paareComboBoxModel);
				paareComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateRundenzahlSlider();
					}
				});
				if(isInGame()){
					paareComboBox.setEnabled(false);
				}
				addSettingsComponent("Pärchen", paareComboBox);
			}
			{
				int maxRunden = Integer.parseInt(moeglichePaare[paareComboBox.getSelectedIndex()]);
				maxRunden = maxRunden/2+1;
				setMaxRunden(maxRunden);
			}
			if(game.modus == Modus.SOLO){
				addSchwierigkeitSlider();
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
		{
			schwierigkeitSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,game.getSchwierigkeit());
			schwierigkeitSlider.setMajorTickSpacing(1);
			schwierigkeitSlider.setMinorTickSpacing(1);
			schwierigkeitSlider.setSnapToTicks(true);
			schwierigkeitSlider.setPaintTicks(true);
			schwierigkeitSlider.setPaintLabels(true);
			addSettingsComponent("Schwierigkeitsstufe", schwierigkeitSlider);
		}
	}

	public void speichern() {
		settingsToProperties();
		super.speichern();
	}
	private void updateRundenzahlSlider(){
		int maxRunden = Integer.parseInt(moeglichePaare[paareComboBox.getSelectedIndex()]);
		maxRunden = maxRunden/2+1;
		setMaxRunden(maxRunden);
	}
	public void settingsToProperties(){
		super.settingsToProperties();
		settings.setProperty(BACKSIDE, ""+backsideComboBox.getSelectedItem());
		settings.setProperty(DECK, ""+deckComboBox.getSelectedItem());
		settings.setProperty(PAIRS, ""+paareComboBox.getSelectedItem());
		if(game.modus == Modus.SOLO){
			settings.setProperty(DIFFICULTY, ""+schwierigkeitSlider.getValue());
		}
	}
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null){
			return;
		}
		String backside = settings.getProperty(BACKSIDE);
		setSelectedElement(backsideComboBox, backside);
		String deck = settings.getProperty(DECK);
		setSelectedElement(deckComboBox, deck);
		String pairs = settings.getProperty(PAIRS);
		setSelectedElement(paareComboBox, pairs);
		if(myGame == null){
			System.out.println("game ist null");
			return;
		}
		if(myGame.modus == Modus.SOLO){
			String schwierigkeit = settings.getProperty(DIFFICULTY);
			if(schwierigkeit != null)
				schwierigkeitSlider.setValue(Integer.parseInt(schwierigkeit));
		}
	}

	private void setSelectedElement(JComboBox cb, String element) {
		if(element == null || cb == null) return;
		for(int i=0; i<cb.getItemCount(); i++){
			if(cb.getItemAt(i).equals(element)){
				cb.setSelectedIndex(i);
				break;
			}
		}
	}
}
