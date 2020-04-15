package games.reaktionszeit;

import games.dialogeGUIs.GameSettingsDialog;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class ReaktionsZeitSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String ROTATION_TIME = "Rotationtime";
	public static final String DECK = "Deck";
	private ReaktionsZeit reaktionsZeit;
	private JSlider rotationTimeSlider;
	private JComboBox<ReaktionsZeitDeck> deckComboBox;

	public ReaktionsZeitSettingsDialog(ReaktionsZeit reaktionsZeit) {
		this(reaktionsZeit, false);
	}

	public ReaktionsZeitSettingsDialog(ReaktionsZeit reaktionsZeit, boolean inGame) {
		super(reaktionsZeit, inGame);
		this.reaktionsZeit = reaktionsZeit;
		initGUI();
		propertiesToSettings();
	}
	
	private void initGUI(){
		addRotationTimeSlider();
	}
	
	private void addRotationTimeSlider() {
		{
			rotationTimeSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,reaktionsZeit.getRotationTime());
			rotationTimeSlider.setMajorTickSpacing(1);
			rotationTimeSlider.setMinorTickSpacing(1);
			rotationTimeSlider.setSnapToTicks(true);
			rotationTimeSlider.setPaintTicks(true);
			rotationTimeSlider.setPaintLabels(true);
			addSettingsComponent("Rotationszeit", rotationTimeSlider);
		}
		{
			List<ReaktionsZeitDeck> rzDecks = new ArrayList<ReaktionsZeitDeck>(reaktionsZeit.getReaktionszeitDecks());
			ReaktionsZeitDeck randomDeck = new ReaktionsZeitDeck(true);
			randomDeck.setDeckName("Zufall");
			rzDecks.add(0, randomDeck);
			ComboBoxModel<ReaktionsZeitDeck> deckComboBoxModel =
					new DefaultComboBoxModel<ReaktionsZeitDeck>(rzDecks.toArray(new ReaktionsZeitDeck[0]));
				
			deckComboBox = new JComboBox<ReaktionsZeitDeck>();
			deckComboBox.setModel(deckComboBoxModel);
			addSettingsComponent("Deck", deckComboBox, false);
		}
	}
	
	@Override
	public void settingsToProperties(){
		super.settingsToProperties();
		settings.setProperty(ROTATION_TIME, ""+rotationTimeSlider.getValue());
		settings.setProperty(DECK, ""+deckComboBox.getSelectedItem().toString());
	}
	
	@Override
	public void propertiesToSettings(){
		super.propertiesToSettings();
		if(settings == null){
			return;
		}
		if(myGame == null){
			System.out.println("game ist null");
			return;
		}
		String rotationTime = settings.getProperty(ROTATION_TIME);
		if(rotationTime != null && rotationTimeSlider != null){
			rotationTimeSlider.setValue(Integer.parseInt(rotationTime));
		}
		String deck = settings.getProperty(DECK, "");
		if(deckComboBox != null){
			for(int i=0; i<deckComboBox.getItemCount(); i++){
				if(deckComboBox.getItemAt(i).toString().equals(deck)){
					deckComboBox.setSelectedIndex(i);
				}
			}
		}
	}
	
	@Override
	public void speichern() {
		settingsToProperties();
		super.speichern();
	}

}
