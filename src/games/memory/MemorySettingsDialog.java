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

public class MemorySettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 6318517814751967329L;
	public static final String BACKSIDE = "Rueckseite";
	public static final String DECK = "Deck";
	public static final String PAIRS = "Paare";
	public static final String DIFFICULTY = "Schwierigkeit";
	Memory game;
	private JComboBox<String> backsideComboBox;
	private JComboBox<String> paareComboBox;
	private JComboBox<String> deckComboBox;
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
		System.out.println(myGame.modus);
		{
			{
				JPanel previewPanel = new JPanel();
				backsidePreview = new Bildschirm(game.getBackside());
				previewPanel.add(backsidePreview);
				dialogPane.add(previewPanel, BorderLayout.EAST);
			}
			{
				ComboBoxModel<String> backsideComboBoxModel =
					new DefaultComboBoxModel<String>(game.getBacksides().keySet().toArray(new String[1]));
				backsideComboBox = new JComboBox<String>();
				backsideComboBox.setModel(backsideComboBoxModel);
				backsideComboBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						backsidePreview.changePic(backsides.get(backsideComboBox.getSelectedItem()));
					}
				});
				backsideComboBox.setSelectedItem("Optisch");
				addSettingsComponent("Rückseite", backsideComboBox);
			}
			{
				ComboBoxModel<String> deckComboBoxModel =
					new DefaultComboBoxModel<String>(game.getMemDeckNames(true).toArray(new String[1]));
				
				deckComboBox = new JComboBox<String>();
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
				ComboBoxModel<String> paareComboBoxModel =
					new DefaultComboBoxModel<String>(moeglichePaare);
				paareComboBox = new JComboBox<String>();
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
			if(myGame.modus == Modus.SOLO){
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
		String backside = settings.getProperty(BACKSIDE, "Optisch");
		setSelectedElement(backsideComboBox, backside);
		if(backsides != null) {
			backsidePreview.changePic(backsides.get(backside));
		}
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
			if(schwierigkeit != null && schwierigkeitSlider != null){
				schwierigkeitSlider.setValue(Integer.parseInt(schwierigkeit));
			}
		}
	}

	private void setSelectedElement(JComboBox<String> cb, String element) {
		if(element == null || cb == null) return;
		for(int i=0; i<cb.getItemCount(); i++){
			if(cb.getItemAt(i).equals(element)){
				cb.setSelectedIndex(i);
				break;
			}
		}
	}
}
