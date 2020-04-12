package games.buchstabensalat;

import games.Deck;
import games.dialogeGUIs.GameSettingsDialog;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class BuchstabenSalatSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String BUZZER_ZEIT = "Zeit nach buzzern";
	public static final String BUCHSTABE_ZEIT = "Zeit pro Buchstabe";
	public static final String DECK = "Deck";
	private BuchstabenSalat salat;
	private JSlider zeitNachBuzzerSlider;
	private JSlider zeitProBuchstabeSlider;
	private JComboBox<Deck> deckComboBox;

	public BuchstabenSalatSettingsDialog(BuchstabenSalat salat) {
		super(salat);
		this.salat = salat;
		initGUI();
		propertiesToSettings();
		}

		private void initGUI(){
			/*if(salat.modus == Modus.SOLO){
				zeitBisVerlorenSlider = new JSlider(SwingConstants.HORIZONTAL,0,100,dif.getCdTime());
				zeitBisVerlorenSlider.setMajorTickSpacing(10);
			}
			else{*/
			zeitNachBuzzerSlider = new JSlider(SwingConstants.HORIZONTAL,3,21,salat.timeAfterBuzzer);
			zeitNachBuzzerSlider.setMajorTickSpacing(3);
			zeitNachBuzzerSlider.setMinorTickSpacing(1);
			zeitNachBuzzerSlider.setSnapToTicks(true);
			zeitNachBuzzerSlider.setPaintTicks(true);
			zeitNachBuzzerSlider.setPaintLabels(true);
			addSettingsComponent("Zeit nach buzzern:", zeitNachBuzzerSlider);
			/*if(dif.modus != Modus.SOLO){
				settingsPanelLayout.setRows(3);
				bildVerschwindenLabel = new JLabel("Bild nach buzzern ausblenden?");
				settingsPanel.add(bildVerschwindenLabel);
				bildVerschwindenCheckBox = new JCheckBox();
				bildVerschwindenCheckBox.setSelected(dif.bildAusblenden);
				settingsPanel.add(bildVerschwindenCheckBox);
			}*/
			zeitProBuchstabeSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,(int)salat.timePerLetter/1000);
			zeitProBuchstabeSlider.setMajorTickSpacing(1);
			zeitProBuchstabeSlider.setMinorTickSpacing(1);
			zeitProBuchstabeSlider.setSnapToTicks(true);
			zeitProBuchstabeSlider.setPaintTicks(true);
			zeitProBuchstabeSlider.setPaintLabels(true);
			addSettingsComponent("Zeit pro Buchstabe:", zeitProBuchstabeSlider);
			{
				List<Deck> salatDecks = new ArrayList<Deck>(salat.salatDecks);
				Deck randomDeckDummy = new Deck(true);
				randomDeckDummy.setDeckName("Zufall");
				randomDeckDummy.setDeckType("");
				salatDecks.add(0, randomDeckDummy);
				ComboBoxModel<Deck> deckComboBoxModel =
						new DefaultComboBoxModel<Deck>(salatDecks.toArray(new Deck[1]));

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
			settings.setProperty(BUCHSTABE_ZEIT, ""+zeitProBuchstabeSlider.getValue());
			settings.setProperty(BUZZER_ZEIT, ""+zeitNachBuzzerSlider.getValue());
			settings.setProperty(DECK, ""+deckComboBox.getSelectedItem().toString());
		}
		
		public void propertiesToSettings(){
			super.propertiesToSettings();
			if(settings == null || zeitNachBuzzerSlider == null){
				return;
			}
			String zeitNachBuzzer = settings.getProperty(BUZZER_ZEIT, "10");
			zeitNachBuzzerSlider.setValue(Integer.parseInt(zeitNachBuzzer));
			String zeitProBuchstabe = settings.getProperty(BUCHSTABE_ZEIT, "3");
			zeitProBuchstabeSlider.setValue(Integer.parseInt(zeitProBuchstabe));
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
