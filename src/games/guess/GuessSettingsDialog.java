package games.guess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import games.Deck;
import games.Modus;
import games.dialogeGUIs.GameSettingsDialog;


public class GuessSettingsDialog extends GameSettingsDialog {
	/**
	 * SerialVersionUID generated by Eclipse
	 */
	private static final long serialVersionUID = 1L;
	public static final String TOLERANZ_ON = "Toleranz aktiviert";
	public static final String TOLERANZ_PERCENT = "Toleranz in %";
	public static final String DECK = "Deck";

	private Guess guess;

	private JSlider toleranzSlider;
	private JCheckBox toleranzCheckBox;
	private JComboBox<Deck> deckComboBox;
	public GuessSettingsDialog(Guess guess) {
		super(guess);
		this.guess = guess;
		initGUI();
	}

	private void initGUI(){
		{
			toleranzSlider = new JSlider(SwingConstants.HORIZONTAL,0,200,10);
			toleranzSlider.setValue((int)guess.toleranz * 100);
			toleranzSlider.setMajorTickSpacing(25);
			toleranzSlider.setMinorTickSpacing(5);
			toleranzSlider.setSnapToTicks(true);
			toleranzSlider.setPaintTicks(true);
			toleranzSlider.setPaintLabels(true);
			toleranzSlider.setEnabled(guess.toleranzOn);
			addSettingsComponent("Toleranz in %", toleranzSlider);
		}
		{
			toleranzCheckBox = new JCheckBox();
			toleranzCheckBox.setSelected(guess.toleranzOn);
			toleranzCheckBox.setText("Toleranz");
			if(guess.modus == Modus.SOLO)toleranzCheckBox.setEnabled(false);
			toleranzCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					toleranzCheckBoxActionPerformed(evt);
				}
			});
			addSettingsComponent("Toleranz anschalten", toleranzCheckBox);
		}
		{
			List<Deck> guessDecks = new ArrayList<Deck>(guess.guessDecks);
			Deck randomDeckDummy = new Deck(true);
			randomDeckDummy.setDeckName("Zufall");
			randomDeckDummy.setDeckType("");
			guessDecks.add(0, randomDeckDummy);
			ComboBoxModel<Deck> deckComboBoxModel =
					new DefaultComboBoxModel<Deck>(guessDecks.toArray(new Deck[1]));

			deckComboBox = new JComboBox<Deck>();
			//deckComboBox.setBackground(Color.DARK_GRAY);
			deckComboBox.setModel(deckComboBoxModel);
			addSettingsComponent("Deck", deckComboBox);
		}
	}

	@Override
	public void speichern(){
		settingsToProperties();
		super.speichern();
	}

	public void settingsToProperties(){
		super.settingsToProperties();
		settings.setProperty(TOLERANZ_ON, ""+toleranzCheckBox.isSelected());
		settings.setProperty(TOLERANZ_PERCENT, ""+toleranzSlider.getValue());
		settings.setProperty(DECK, ""+deckComboBox.getSelectedItem().toString());
	}
	
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || toleranzSlider == null){
			return;
		}
		String toleranzOn = settings.getProperty(TOLERANZ_ON, "true");
		if(guess.modus != Modus.SOLO) {
			toleranzCheckBox.setSelected(Boolean.parseBoolean(toleranzOn));
		}
		String toleranzPercent = settings.getProperty(TOLERANZ_PERCENT, "95");
		toleranzSlider.setValue(Integer.parseInt(toleranzPercent));
		String deck = settings.getProperty(DECK, "");
		if(deckComboBox != null){
			for(int i=0; i<deckComboBox.getItemCount(); i++){
				if(deckComboBox.getItemAt(i).toString().equals(deck)){
					deckComboBox.setSelectedIndex(i);
				}
			}
		}
	}

	private void toleranzCheckBoxActionPerformed(ActionEvent evt) {
		toleranzSlider.setEnabled(toleranzCheckBox.isSelected());
	}
}
