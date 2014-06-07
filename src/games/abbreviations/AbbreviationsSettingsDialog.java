package games.abbreviations;

import games.Deck;
import games.dialogeGUIs.GameSettingsDialog;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class AbbreviationsSettingsDialog extends GameSettingsDialog {
	private static final long serialVersionUID = 1L;
	public static final String ROTATION_TIME = "Rotationtime";
	public static final String DECK = "Deck";
	private Abbreviations abbreviations;
//	private JSlider rotationTimeSlider;
	private JComboBox deckComboBox;

	public AbbreviationsSettingsDialog(Abbreviations abbreviations) {
		this(abbreviations, false);
	}

	public AbbreviationsSettingsDialog(Abbreviations abbreviations, boolean inGame) {
		super(abbreviations, inGame);
		this.abbreviations = abbreviations;
		initGUI();
		propertiesToSettings();
	}
	
	private void initGUI(){
		addRotationTimeSlider();
	}
	
	private void addRotationTimeSlider() {
//		{
//			rotationTimeSlider = new JSlider(SwingConstants.HORIZONTAL,1,10,abbreviations.getRotationTime());
//			rotationTimeSlider.setMajorTickSpacing(1);
//			rotationTimeSlider.setMinorTickSpacing(1);
//			rotationTimeSlider.setSnapToTicks(true);
//			rotationTimeSlider.setPaintTicks(true);
//			rotationTimeSlider.setPaintLabels(true);
//			addSettingsComponent("Rotationszeit", rotationTimeSlider);
//		}
		{
			ComboBoxModel deckComboBoxModel =
					new DefaultComboBoxModel(abbreviations.getAbbreviationsDecks().toArray(new Deck[1]));
				
			deckComboBox = new JComboBox();
			deckComboBox.setModel(deckComboBoxModel);	
			addSettingsComponent("Deck", deckComboBox);
		}
	}
	
	@Override
	public void settingsToProperties(){
		super.settingsToProperties();
//		settings.setProperty(ROTATION_TIME, ""+rotationTimeSlider.getValue());
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
//		String rotationTime = settings.getProperty(ROTATION_TIME);
//		if(rotationTime != null && rotationTimeSlider != null){
//			rotationTimeSlider.setValue(Integer.parseInt(rotationTime));
//		}
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