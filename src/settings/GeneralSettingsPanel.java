package settings;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class GeneralSettingsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel punkteModusPanel;
	private JRadioButton aufsteigendRadioButton;
	private JRadioButton konstantRadioButton;
	private Profile profile;
	
	public GeneralSettingsPanel(Profile profile){
		this.profile = profile;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new GridLayout(1, 1));
		initPunkteModusPanel();
		this.add(punkteModusPanel);
	}

	private void initPunkteModusPanel() {
		punkteModusPanel = new JPanel();
		punkteModusPanel.setLayout(new GridLayout(1, 3));
		punkteModusPanel.add(new JLabel("Punktemodus"));
		ButtonGroup bg = new ButtonGroup();
		aufsteigendRadioButton = new JRadioButton("Aufsteigend"); 
		bg.add(aufsteigendRadioButton);
		punkteModusPanel.add(aufsteigendRadioButton);
		konstantRadioButton = new JRadioButton("Konstant");
		bg.add(konstantRadioButton);
		punkteModusPanel.add(konstantRadioButton);
		if(profile.getPunkteModus() == Profile.INCREASING){
			aufsteigendRadioButton.setSelected(true);
		}
		else{
			konstantRadioButton.setSelected(true);
		}
	}
	
	public int getPunkteModus(){
		int punkteModus = Profile.INCREASING;
		if(konstantRadioButton.isSelected()){
			punkteModus = Profile.CONSTANT;
		}
		return punkteModus;
	}

	public void setProfile(Profile profile) {
		if(profile.getPunkteModus() == Profile.INCREASING){
			aufsteigendRadioButton.setSelected(true);
		}
		else{
			konstantRadioButton.setSelected(true);
		}
	}

}
