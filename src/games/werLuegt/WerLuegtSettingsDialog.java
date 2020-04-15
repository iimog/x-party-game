package games.werLuegt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import games.Deck;
import games.dialogeGUIs.GameSettingsDialog;

public class WerLuegtSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String AUSSAGEZEIT = "Zeit pro Aussage";
	public static final String DECK = "Deck";
	private WerLuegt werLuegt;
	private JSlider zeitSlider;
	private JComboBox<Deck> deckComboBox;

	public WerLuegtSettingsDialog(WerLuegt w, boolean inGame) {
		super(w, inGame);
		this.werLuegt = w;
		initGUI();
		propertiesToSettings();
	}

	private void initGUI() {
		zeitSlider = new JSlider(SwingConstants.HORIZONTAL, 3, 15,
				werLuegt.timeProAussage);
		zeitSlider.setMajorTickSpacing(1);
		zeitSlider.setMinorTickSpacing(1);
		zeitSlider.setSnapToTicks(true);
		zeitSlider.setPaintTicks(true);
		zeitSlider.setPaintLabels(true);
		addSettingsComponent("Zeit pro Aussage (s):", zeitSlider);
		{
			List<Deck> werLuegtDecks = new ArrayList<Deck>(werLuegt.werLuegtDecks);
			Deck randomDeckDummy = new Deck(true);
			randomDeckDummy.setDeckName("Zufall");
			randomDeckDummy.setDeckType("");
			werLuegtDecks.add(0, randomDeckDummy);
			ComboBoxModel<Deck> deckComboBoxModel =
					new DefaultComboBoxModel<Deck>(werLuegtDecks.toArray(new Deck[1]));

			deckComboBox = new JComboBox<Deck>();
			deckComboBox.setModel(deckComboBoxModel);
			addSettingsComponent("Deck", deckComboBox);
		}
	}
	
	@Override
	public void speichern(){
		settingsToProperties();
		super.speichern();
	}
	
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null || zeitSlider == null){
			return;
		}
		String timeProAussage = settings.getProperty(AUSSAGEZEIT, "5");
		zeitSlider.setValue(Integer.parseInt(timeProAussage));
		String deck = settings.getProperty(DECK, "");
		if(deckComboBox != null){
			for(int i=0; i<deckComboBox.getItemCount(); i++){
				if(deckComboBox.getItemAt(i).toString().equals(deck)){
					deckComboBox.setSelectedIndex(i);
				}
			}
		}
	}
	
	public void settingsToProperties(){
		super.settingsToProperties();
		try{
			String zeit = zeitSlider.getValue()+"";
			settings.setProperty(AUSSAGEZEIT, zeit);
			settings.setProperty(DECK, ""+deckComboBox.getSelectedItem().toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
