package gui.menu;
import gui.EasyDialog;
import gui.components.DefaultButton;
import gui.components.JButtonIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import player.Player;
import player.PlayerFileHandler;
import player.Team;
import start.X;
import util.ChangeManager;


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
public class PlayerFactory extends javax.swing.JPanel {

	private static final long serialVersionUID = -2970422219132276258L;
	private String name;
	private boolean male;
	private Color farbe=Color.red;
	private int key=0;
	private int sKey;
	private String sound;
	private String sSound;
	private JButton buzzerChooser;
	private JLabel buzzerLabel;

	private boolean teamMode = false;

	private JPanel hauptbereichPanel;

	private JPanel soundPanel;

	private JButton colorSelectionButton1;
	private JLabel colorLabel;
	private JRadioButton maleRadioButton;
	private JRadioButton femaleRadioButton;
	private ButtonGroup sexButtonGroup;
	private JPanel sexPanel;
	private JLabel sexLabel;
	private JTextField nameTextField;
	private JLabel nameLabel;
	private JButton abbrechenButton;
	private JButton okButton;
	private JPanel buttonPanel;
	private Font standardFont = new Font("Comic Sans MS",0,20);
	private Font smallFont = new Font("Comic Sans MS",0,14);
	private File dir = new File(X.getDataDir()+"Player");
	private ChangeManager cM;
	private boolean keyActive;
	private JComboBox soundComboBox;
	private JPanel mainPanel;
	public boolean abgebrochen;
	private JColorChooser colorChooser;
	private HashMap<String, String> soundMap = new HashMap<String, String>();
	private String path = "media/sounds/";
	private GridLayout hauptbereichPanelLayout;
	private JLabel soundLabel;
	private JButtonIcon soundButton;
	{
		soundMap.put("Kuh", path + "muh.wav");
		soundMap.put("Donner", path + "donner.wav");
		soundMap.put("Klirr", path + "klirr.wav");
		soundMap.put("Lache", path + "lache.wav");
		soundMap.put("Crash", path + "unfall.wav");
		soundMap.put("VAT Schsch", path + "va-schsch.wav");
		soundMap.put("VAT Bam", path + "va-bam.wav");
		soundMap.put("LP Was\'n los", path + "lp-wasnlos.wav");
		soundMap.put("INT Arsch", path + "int-arschl.wav");
		soundMap.put("INT Hast du gelernt?", path + "int-hastdugelernt.wav");
		soundMap.put("INT Hey Ho", path + "int-heyhoh1.wav");
	}
	public PlayerFactory(){
		this("");
	}
	public PlayerFactory(Player player){
		this.name = player.name;
		this.male = player.male;
		this.farbe = player.farbe;
		this.key = player.getKey();
		this.sound = player.sound;
		// unelegante Lösung um die Map rückwärts zu durchlaufen
		for(Iterator<String> it = soundMap.keySet().iterator(); it.hasNext();){
			String test = it.next();
			if(this.sound.equals(soundMap.get(test))){
				this.sound = test;
				break;
			}
		}
		initGUI();
		setName("Spieler bearbeiten");
		maleRadioButton.setSelected(male);
		femaleRadioButton.setSelected(!male);
		colorSelectionButton1.setBackground(farbe);
		getBuzzerChooser().setText(KeyEvent.getKeyText(key));
		getSoundComboBox().getModel().setSelectedItem(sound);
	}
	public PlayerFactory(String name) {
		super();
		this.name = name;
		initGUI();
		dir.mkdirs();
	}
	private void initGUI() {
		try {
			this.setLayout(new BorderLayout());
			this.add(getMainPanel());
		} catch (Exception e) {
			//add your error handling code here
			e.printStackTrace();
		}
	}
	private void abbrechen() {
		key = sKey;
		getBuzzerChooser().setText(KeyEvent.getKeyText(key));
		if(key==0)getBuzzerChooser().setText("...");
		getSoundComboBox().getModel().setSelectedItem(sSound);
		abgebrochen = true;
		if(cM!=null)cM.change();
	}

	public void activate(){
		nameTextField.setText(name);
		maleRadioButton.setSelected(male);
		femaleRadioButton.setSelected(!male);
		colorSelectionButton1.setBackground(farbe);
		setVisible(true);
	}
	private void buzzerChooserActionPerformed(ActionEvent evt) {
		buzzerChooser.setText("Taste drücken");
		keyActive = true;
	}
	private void buzzerChooserKeyPressed(KeyEvent evt) {
		if(!keyActive)return;
		if(evt.getKeyCode() == KeyEvent.VK_ESCAPE || evt.getKeyCode() == KeyEvent.VK_SPACE){
			EasyDialog.showMessage("Escape und Leerzeichen können leider nicht als Buzzertasten gewählt werden.");
			buzzerChooser.setText("...");
			if(key != 0){
				buzzerChooser.setText(KeyEvent.getKeyText(key));
			}
		}
		else{
			key = evt.getKeyCode();
			buzzerChooser.setText(KeyEvent.getKeyText(key));
			keyActive = false;			
		}
	}

	private void changeBack(){
		this.removeAll();
		this.add(getMainPanel(), BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	private void colorSelectionButton1ActionPerformed(ActionEvent evt) {
		this.removeAll();
		colorChooser = new JColorChooser(colorSelectionButton1.getBackground());
		this.add(colorChooser, BorderLayout.CENTER);
		JPanel buttonP = new JPanel(new FlowLayout());
		this.add(buttonP, BorderLayout.SOUTH);
		JButton okB = new DefaultButton("OK");
		okB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				Color col = colorChooser.getSelectionModel().getSelectedColor();
				colorSelectionButton1.setBackground(col);
				changeBack();
			}
		});
		buttonP.add(okB);
		JButton verwerfenB = new DefaultButton("Verwerfen");
		verwerfenB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				changeBack();
			}
		});
		buttonP.add(verwerfenB);
		this.revalidate();
		this.repaint();
	}

	private JButton getBuzzerChooser() {
		if(buzzerChooser == null) {
			buzzerChooser = new DefaultButton();
			buzzerChooser.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent evt) {
					buzzerChooserKeyPressed(evt);
				}
			});
			buzzerChooser.setText("...");
			buzzerChooser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buzzerChooserActionPerformed(evt);
				}
			});
		}
		return buzzerChooser;
	}

	private JLabel getBuzzerLabel() {
		if(buzzerLabel == null) {
			buzzerLabel = new JLabel();
			buzzerLabel.setText("Buzzertaste:");
			buzzerLabel.setFont(standardFont);
		}
		return buzzerLabel;
	}

	private JLabel getColorLabel() {
		if(colorLabel == null) {
			colorLabel = new JLabel();
			colorLabel.setText("Spielfarbe:");
			colorLabel.setFont(standardFont);
		}
		return colorLabel;
	}

	private JButton getColorSelectionButton1() {
		if(colorSelectionButton1 == null) {
			colorSelectionButton1 = new DefaultButton();
			colorSelectionButton1.setText("Farbe");
			colorSelectionButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					colorSelectionButton1ActionPerformed(evt);
				}
			});
		}
		return colorSelectionButton1;
	}
	public Color getFarbe() {
		farbe = colorSelectionButton1.getBackground();
		return farbe;
	}

	private JPanel getMainPanel() {
		if(mainPanel == null) {
			mainPanel = new JPanel();
			BorderLayout mainPanelLayout = new BorderLayout();
			mainPanel.setLayout(mainPanelLayout);
			{
				buttonPanel = new JPanel();
				mainPanel.add(buttonPanel, BorderLayout.SOUTH);
				this.add(getMainPanel(), BorderLayout.NORTH);
				buttonPanel.setOpaque(false);
				{
					okButton = new DefaultButton();
					buttonPanel.add(okButton);
					okButton.setText("Speichern");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							okButtonActionPerformed(evt);
						}
					});
				}
				{
					abbrechenButton = new DefaultButton();
					buttonPanel.add(abbrechenButton);
					abbrechenButton.setText("Verwerfen");
					abbrechenButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							abbrechen();
						}
					});
				}
			}
			{
				hauptbereichPanel = new JPanel();
				mainPanel.add(hauptbereichPanel, BorderLayout.CENTER);
				hauptbereichPanelLayout = new GridLayout(5, 2);
				hauptbereichPanelLayout.setHgap(5);
				hauptbereichPanelLayout.setVgap(5);
				hauptbereichPanelLayout.setColumns(2);
				hauptbereichPanelLayout.setRows(5);
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				hauptbereichPanel.setOpaque(false);
				{
					nameLabel = new JLabel();
					hauptbereichPanel.add(nameLabel);
					nameLabel.setText("Name:");
					nameLabel.setFont(standardFont);
				}
				{
					nameTextField = new JTextField();
					hauptbereichPanel.add(nameTextField);
					nameTextField.setFont(standardFont);
					nameTextField.setColumns(10);
					nameTextField.setText(name);
					if(name != null && !name.equals(""))
						nameTextField.setEnabled(false);
				}
				{
					sexLabel = new JLabel();
					hauptbereichPanel.add(sexLabel);
					sexLabel.setText("Geschlecht:");
					sexLabel.setFont(standardFont);
				}
				{
					sexPanel = new JPanel();
					GridLayout sexPanelLayout = new GridLayout(2, 1);
					sexPanelLayout.setHgap(5);
					sexPanelLayout.setVgap(5);
					sexPanelLayout.setColumns(1);
					sexPanelLayout.setRows(2);
					hauptbereichPanel.add(sexPanel);
					hauptbereichPanel.add(getColorLabel());
					hauptbereichPanel.add(getColorSelectionButton1());
					hauptbereichPanel.add(getBuzzerLabel());
					hauptbereichPanel.add(getBuzzerChooser());
					hauptbereichPanel.add(getSoundPanel());
					hauptbereichPanel.add(getSoundComboBox());
					sexPanel.setLayout(sexPanelLayout);
					sexPanel.setOpaque(false);
					{
						femaleRadioButton = new JRadioButton();
						sexPanel.add(femaleRadioButton);
						femaleRadioButton.setText("weiblich");
						femaleRadioButton.setFont(smallFont);
						getSexButtonGroup().add(femaleRadioButton);
					}
					{
						maleRadioButton = new JRadioButton();
						sexPanel.add(maleRadioButton);
						maleRadioButton.setText("männlich");
						maleRadioButton.setFont(smallFont);
						getSexButtonGroup().add(maleRadioButton);
					}
				}
			}
		}
		return mainPanel;
	}
	public String getNamen(){
		this.name = nameTextField.getText();
		return this.name;
	}

	public Player getPlayer(){
		if(teamMode){
			if(sound==null)return new Team(name,male,farbe,key);
			return new Team(name,male,farbe,key,sound);
		}
		if(sound==null)return new Player(name,male,farbe,key);
		return new Player(name,male,farbe,key,sound);
	}
	private ButtonGroup getSexButtonGroup() {
		if(sexButtonGroup == null) {
			sexButtonGroup = new ButtonGroup();
		}
		return sexButtonGroup;
	}

	private JComboBox getSoundComboBox() {
		if(soundComboBox == null) {
			ComboBoxModel soundComboBoxModel =
				new DefaultComboBoxModel(
						soundMap.keySet().toArray());
			soundComboBox = new JComboBox();
			soundComboBox.setModel(soundComboBoxModel);
		}
		return soundComboBox;
	}

	private JPanel getSoundPanel() {
		if(soundPanel == null) {
			soundPanel = new JPanel();
			soundPanel.setOpaque(false);
			soundPanel.setLayout(new GridLayout(1,2));
			soundLabel = new JLabel();
			soundLabel.setText("Sound:");
			soundLabel.setFont(standardFont);
			soundPanel.add(soundLabel);
			soundButton = new JButtonIcon("media/ablauf/sound.jpg", "abspielen");
			soundPanel.add(soundButton);
			soundButton.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					soundButtonActionPerformed();
				}
			});
		}
		return soundPanel;
	}

	protected void soundButtonActionPerformed() {
		String sound = (String)soundComboBox.getSelectedItem();
		sound = soundMap.get(sound);
		X.playAudioFile(sound);
	}
	public boolean isTeamMode() {
		return teamMode;
	}

	private void okButtonActionPerformed(ActionEvent evt) {
		speichern();
	}

	public void setCM(ChangeManager cM){
		this.cM = cM;
	}
	public void setFarbe(Color farbe) {
		this.farbe = farbe;
	}

	public void setNamen(String name){
		this.name = name;
		nameTextField.setText(name);
	}

	public void setTeamMode(boolean teamMode) {
		if(this.teamMode==teamMode)return;
		this.teamMode = teamMode;
		if(teamMode){
			hauptbereichPanel.remove(sexPanel);
			hauptbereichPanel.remove(sexLabel);
			hauptbereichPanelLayout.setRows(hauptbereichPanelLayout.getRows()-1);
			nameTextField.setEnabled(true);
			validate();
			repaint();
		}
	}

	private void speichern(){
		String name = nameTextField.getText();
		if(name.equals("")){
			EasyDialog.showMessage("Bitte einen Namen eingeben");
			return;
		}
		if(name.contains(",") || name.contains(";") || name.contains("[")|| name.contains("]")){
			EasyDialog.showMessage("Der Name darf keines der Zeichen \',\' \';\' \'[\' oder \']\' enthalten");
			return;
		}
		this.name = name;
		this.male = maleRadioButton.isSelected();
		this.farbe = colorSelectionButton1.getBackground();
		sKey = key;
		sSound = (String) getSoundComboBox().getSelectedItem();
		this.sound = soundMap.get(sSound);
		if(!teamMode){
			try {
				Player p = getPlayer();
				PlayerFileHandler.savePlayer(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		abgebrochen = false;
		if(cM!=null)cM.change();
	}
	public static Player createDefaultPlayer() {
		Player p = null;
		Random r = new Random();
		int zufall = r.nextInt(10);
		String name = "StandardPlayer "+zufall;
		p = new Player(name);
		return p;
	}
}
