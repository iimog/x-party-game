package games.stimmts;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import games.Deck;
import games.dialogeGUIs.GameSettingsDialog;

public class StimmtsSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String AUSSAGE_ZEIT = "Zeit pro Aussage";
	public static final String DECK = "Deck";
	private Stimmts stimmts;
	private JSlider zeitSlider;
	private JComboBox<Deck> deckComboBox;

	public StimmtsSettingsDialog(Stimmts stimmts) {
		super(stimmts);
		this.stimmts = stimmts;
		initGUI();
		}

		private void initGUI(){
			zeitSlider = new JSlider(SwingConstants.HORIZONTAL,3,20,stimmts.numOfRounds);
			zeitSlider.setMajorTickSpacing(3);
			zeitSlider.setMinorTickSpacing(1);
			zeitSlider.setSnapToTicks(true);
			zeitSlider.setPaintTicks(true);
			zeitSlider.setPaintLabels(true);
			addSettingsComponent("Zeit pro Aussage (s)", zeitSlider);
			{
				List<Deck> stimmtsDecks = new ArrayList<Deck>(stimmts.stimmtsDecks);
				Deck randomDeckDummy = new Deck(true);
				randomDeckDummy.setDeckName("Zufall");
				randomDeckDummy.setDeckType("");
				stimmtsDecks.add(0, randomDeckDummy);
				ComboBoxModel<Deck> deckComboBoxModel =
						new DefaultComboBoxModel<Deck>(stimmtsDecks.toArray(new Deck[1]));

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
			settings.setProperty(AUSSAGE_ZEIT, ""+zeitSlider.getValue());
			settings.setProperty(DECK, ""+deckComboBox.getSelectedItem().toString());
		}
		
		public void propertiesToSettings(){
			super.propertiesToSettings();
			if(settings == null || zeitSlider == null){
				return;
			}
			String zeitProAussage = settings.getProperty(AUSSAGE_ZEIT, "10");
			zeitSlider.setValue(Integer.parseInt(zeitProAussage));
			String deck = settings.getProperty(DECK, "");
			if(deckComboBox != null){
				for(int i=0; i<deckComboBox.getItemCount(); i++){
					if(deckComboBox.getItemAt(i).toString().equals(deck)){
						deckComboBox.setSelectedIndex(i);
					}
				}
			}
		}
}
