package gui.menu;

import games.Modus;
import gui.Anzeige;
import gui.EasyDialog;
import gui.components.DefaultButton;
import gui.components.JButtonIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import player.Player;
import settings.Profile;
import start.X;
import util.SpielListen;
import ablauf.Spielablauf;


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
public class StartMatch extends Anzeige {
	/**
	 * Serial Version UID generated by Eclipse
	 */
	private static final long serialVersionUID = 823521784957645300L;

	private static String background = "/media/ablauf/iceBG2.jpg";
	private Dimension buttonSize = new Dimension(250,50);
	public Player[] myPlayer;
	private int prefNumOfGames=15;
	private ArrayList<Integer> gameList = new ArrayList<Integer>(30);
	private Profile profile;

	private JPanel mainPanel;
	private JPanel buttonsPanel;
	private JPanel rundenzahlPanel;
	private JPanel spielerauswahlPanel;
	private JPanel spielerPanel;
	private JPanel spielmodusPanel;
	private JPanel benutzeroberflaechePanel;
	private JPanel bottomPanel;
	private JPanel topPanel;
	private JButtonIcon quitButton;

	private JButton hauptMenuButton;
	private JPanel startPanel;
	private JSlider schiebereglerSlider;
	private JPanel rundenPanel;
	private JButton startButton;
	private JButton einstellungenButton;
	private JButton spieleButton;
	private JLabel rundenzahlLabel;
	private JLabel[] spielerLabel;
	private JButton spielerButton;
	private JPanel einstellungenPanel;
	private JPanel spielePanel;
	private JPanel schiebereglerPanel;

	private Modus modus;

	public Modus getModus() {
		return modus;
	}

	private JLabel spielmodusLabel;

	private int spielerZahl;

	private JPanel aktuellesOptionPanel;

	public StartMatch(Modus modus){
		this.modus = modus;
		spielerZahl = modus.getSpielerzahl();
		myPlayer = new Player[spielerZahl];
		for(int i=0; i<30; i++){
			getGameList().add(i, SpielListen.RANDOM_GAME);
		}
		if(modus == Modus.SOLO){
			myPlayer = new Player[2];
			myPlayer[1] = Player.pcPlayer();
		}
		spielerLabel = new JLabel[modus.getSpielerzahl()];
		profile = Profile.getDefaultProfile();
		
		initGUI();
		instance.changeBackground(background);
	}

	protected void changeOptions(JPanel optionPanel){
		benutzeroberflaechePanel.removeAll();
		if(optionPanel != null)
			benutzeroberflaechePanel.add(optionPanel);
		benutzeroberflaechePanel.revalidate();
		benutzeroberflaechePanel.repaint();
		aktuellesOptionPanel = optionPanel;
	}

	protected void hauptMenuButtonActionPerformed() {
		instance.changeAnzeige(new HauptMenu());
	}

	private void initGUI(){
		try{
			setLayout(new BorderLayout());
			{
				mainPanel = new JPanel();
				GridLayout mainPanelLayout = new GridLayout(1, 2);
				mainPanelLayout.setColumns(2);
				mainPanel.setLayout(mainPanelLayout);
				mainPanel.setOpaque(false);
				add(mainPanel, BorderLayout.CENTER);
				{
					buttonsPanel = new JPanel();
					GridLayout einstellungenPanelLayout = new GridLayout(7, 1);
					einstellungenPanelLayout.setHgap(5);
					einstellungenPanelLayout.setVgap(5);
					einstellungenPanelLayout.setColumns(1);
					einstellungenPanelLayout.setRows(7);
					mainPanel.add(buttonsPanel);
					buttonsPanel.setLayout(einstellungenPanelLayout);
					buttonsPanel.setOpaque(false);
					{
						spielmodusPanel = new JPanel();
						buttonsPanel.add(spielmodusPanel);
						spielmodusPanel.setOpaque(false);
						{
							spielmodusLabel = new JLabel();
							spielmodusLabel.setFont(X.BUTTON_FONT);
							spielmodusPanel.add(spielmodusLabel);
							spielmodusLabel.setText(modus.toString()+"modus");
							spielmodusLabel.setForeground(Color.WHITE);
						}
					}
					{
						spielerPanel = new JPanel();
						buttonsPanel.add(spielerPanel);
						spielerPanel.setOpaque(false);
						{
							spielerButton = new DefaultButton();
							spielerButton.setFont(X.BUTTON_FONT);
							spielerPanel.add(spielerButton);
							if(modus != Modus.TEAM){
								spielerButton.setText("Spieler");
							}
							else{
								spielerButton.setText("Teams");
							}
							spielerButton.setForeground(new java.awt.Color(58,185,222));
							spielerButton.setPreferredSize(buttonSize);
							spielerButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									spielerButtonActionPerformed(evt);
								}
							});
						}
					}
					{
						spielerauswahlPanel = getSpielerauswahlPanel();
						buttonsPanel.add(spielerauswahlPanel);
					}
					{
						rundenPanel = new JPanel();
						buttonsPanel.add(rundenPanel);
						GridLayout rundenPanelLayout = new GridLayout(2, 1);
						rundenPanelLayout.setHgap(5);
						rundenPanelLayout.setVgap(5);
						rundenPanelLayout.setColumns(1);
						rundenPanelLayout.setRows(2);
						rundenPanel.setLayout(rundenPanelLayout);
						rundenPanel.setOpaque(false);
						{
							rundenzahlPanel = new JPanel();
							FlowLayout rundenzahlPanelLayout = new FlowLayout();
							rundenzahlPanelLayout.setAlignOnBaseline(true);
							rundenzahlPanelLayout.setHgap(0);
							rundenzahlPanel.setLayout(rundenzahlPanelLayout);
							rundenPanel.add(rundenzahlPanel);
							rundenzahlPanel.setOpaque(false);
							{
								rundenzahlLabel = new JLabel();
								rundenzahlPanel.add(rundenzahlLabel);
								rundenzahlLabel.setText("Rundenzahl: 15");
								rundenzahlLabel.setFont(new java.awt.Font("Comic Sans MS",1,26));
								rundenzahlLabel.setForeground(new java.awt.Color(255,255,255));
							}
						}
						{
							schiebereglerPanel = new JPanel();
							rundenPanel.add(schiebereglerPanel);
							schiebereglerPanel.setOpaque(false);
							{
								schiebereglerSlider = new JSlider();
								schiebereglerPanel.add(schiebereglerSlider);
								schiebereglerSlider.setOpaque(false);
								schiebereglerSlider.setMajorTickSpacing(5);
								schiebereglerSlider.setMinorTickSpacing(1);
								schiebereglerSlider.setPaintLabels(true);
								schiebereglerSlider.setPaintTicks(true);
								schiebereglerSlider.setSnapToTicks(true);
								schiebereglerSlider.setValue(15);
								schiebereglerSlider.setMaximum(30);
								schiebereglerSlider.addChangeListener(new ChangeListener() {
									public void stateChanged(ChangeEvent evt) {
										schiebereglerSliderStateChanged(evt);
									}
								});
							}
						}
					}
					{
						spielePanel = new JPanel();
						buttonsPanel.add(spielePanel);
						spielePanel.setOpaque(false);
						{
							spieleButton = new DefaultButton();
							spieleButton.setFont(X.BUTTON_FONT);
							spielePanel.add(spieleButton);
							spieleButton.setText("Spiele");
							spieleButton.setForeground(new java.awt.Color(58,185,222));
							spieleButton.setPreferredSize(buttonSize);
							spieleButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									spieleButtonActionPerformed(evt);
								}
							});
						}
					}
					{
						einstellungenPanel = new JPanel();
						buttonsPanel.add(einstellungenPanel);
						einstellungenPanel.setOpaque(false);
						{
							einstellungenButton = new DefaultButton();
							einstellungenButton.setFont(X.BUTTON_FONT);
							einstellungenPanel.add(einstellungenButton);
							einstellungenButton.setText("Einstellungen");
							einstellungenButton.setForeground(new java.awt.Color(58,185,222));
							einstellungenButton.setPreferredSize(buttonSize);
							einstellungenButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									einstellungenButtonActionPerformed(evt);
								}
							});
						}
					}
					{
						startPanel = new JPanel();
						buttonsPanel.add(startPanel);
						startPanel.setOpaque(false);
						{
							startButton = new DefaultButton();
							startButton.setFont(X.BUTTON_FONT);
							startPanel.add(startButton);
							startButton.setText("Start");
							startButton.setForeground(new java.awt.Color(58,185,222));
							startButton.setPreferredSize(buttonSize);
							startButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent evt){
									startButtonActionPerformed(evt);
								}
							});
						}
					}
				}
				{
					benutzeroberflaechePanel = new JPanel();
					BorderLayout benutzeroberflaechePanelLayout = new BorderLayout();
					mainPanel.add(benutzeroberflaechePanel);
					benutzeroberflaechePanel.setLayout(benutzeroberflaechePanelLayout);
					benutzeroberflaechePanel.setOpaque(false);
				}
			}
			{
				bottomPanel = new JPanel();
				FlowLayout bottomPanelLayout = new FlowLayout();
				bottomPanel.setLayout(bottomPanelLayout);
				bottomPanelLayout.setAlignment(FlowLayout.RIGHT);
				bottomPanel.setOpaque(false);
				add(bottomPanel, BorderLayout.SOUTH);
				{
					hauptMenuButton = new DefaultButton("Hauptmenü");
					hauptMenuButton.setFont(X.BUTTON_FONT);
					hauptMenuButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							hauptMenuButtonActionPerformed();
						}
					});
					bottomPanel.add(hauptMenuButton);
				}
			}
			{
				topPanel = new JPanel();
				FlowLayout topPanelLayout = new FlowLayout();
				topPanel.setLayout(topPanelLayout);
				topPanelLayout.setAlignment(FlowLayout.RIGHT);
				topPanel.setOpaque(false);
				add(topPanel, BorderLayout.NORTH);
				{
					quitButton = new JButtonIcon("/media/ablauf/quit.png","Quit");
					quitButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent evt){
							quitButtonActionPerformed(evt);
						}
					});
					topPanel.add(quitButton);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void einstellungenButtonActionPerformed(ActionEvent evt) {
		changeOptions(new MatchSettingsDialog(this));
	}

	private JPanel getSpielerauswahlPanel() {
		JPanel spielerauswahlPanel = new JPanel();
		FlowLayout spielerauswahlPanelLayout = new FlowLayout();
		spielerauswahlPanelLayout.setHgap(50);
		spielerauswahlPanel.setLayout(spielerauswahlPanelLayout);
		spielerauswahlPanel.setOpaque(false);
		for(int i=0; i<spielerZahl; i++){
			spielerLabel[i] = new JLabel();
			spielerauswahlPanel.add(spielerLabel[i]);
			spielerLabel[i].setText("Spieler "+(i+1));
			if(modus == Modus.TEAM){
				spielerLabel[i].setText("Team "+(i+1));
			}
			spielerLabel[i].setFont(new java.awt.Font("Comic Sans MS",0,22));
			spielerLabel[i].setForeground(new java.awt.Color(255,255,255));
		}
		return spielerauswahlPanel;
	}

	private void quitButtonActionPerformed(ActionEvent evt){
		instance.close();
	}

	private void schiebereglerSliderStateChanged(ChangeEvent evt) {
		setPrefNumOfGames(schiebereglerSlider.getValue());
		if(getPrefNumOfGames()==0){
			setPrefNumOfGames(1);
		}
		schiebereglerSlider.setValue(getPrefNumOfGames());
		schiebereglerSlider.setToolTipText(""+getPrefNumOfGames());
		rundenzahlLabel.setText("Rundenzahl: "+getPrefNumOfGames());
		if(aktuellesOptionPanel instanceof GameChoosePanel){
			changeOptions(new GameChoosePanel(this));
		}
	}

	private void spieleButtonActionPerformed(ActionEvent evt) {
		changeOptions(new GameChoosePanel(this));
	}

	private void spielerButtonActionPerformed(ActionEvent evt) {
		if(modus==Modus.DUELL);{
			changeOptions(new PlayerWahl(this));
		}
		if(modus==Modus.TEAM){
			changeOptions(new TeamWahl(this));
		}
	}

	private void startButtonActionPerformed(ActionEvent evt){
		if(modus == Modus.TEAM && (myPlayer[0] == null || myPlayer[1] == null)){
			EasyDialog.showMessage("Bitte stelle noch die Teams ein!");
			return;
		}
		if(myPlayer == null){
			new Spielablauf(new Player[]{new Player("Peter"), new Player("Lustig")},getPrefNumOfGames(),getGameList(), modus, profile);
		}
		else{
			if(myPlayer[0]==null){
				myPlayer[0] = new Player("Boney");
			}
			if(spielerZahl>1 && myPlayer[1]==null){
				myPlayer[1] = new Player("Clyde");
			}
			if(spielerZahl>2 && myPlayer[2]==null){
				myPlayer[2] = new Player("Siegfried");
			}
			if(spielerZahl>3 && myPlayer[3]==null){
				myPlayer[3] = new Player("Roy");
			}
			new Spielablauf(myPlayer,getPrefNumOfGames(),getGameList(getPrefNumOfGames()),modus, profile);
		}
	}

	public void updatePlayers(Player[] myPl){
		myPlayer = myPl;
		try{
			for(int i=0; i<spielerZahl; i++){
				if(!myPlayer[i].name.equals("")){
				spielerLabel[i].setText(myPlayer[i].name);
				}
			}
		}
		catch(Exception e){
			// Spieler wurde wohl noch nicht angelegt
		}
	}

	public void setPrefNumOfGames(int prefNumOfGames) {
		this.prefNumOfGames = prefNumOfGames;
	}

	public int getPrefNumOfGames() {
		return prefNumOfGames;
	}

	public void setGameList(ArrayList<Integer> gameList) {
		this.gameList = gameList;
	}

	public ArrayList<Integer> getGameList() {
		return gameList;
	}
	public List<Integer> getGameList(int i) {
		return gameList.subList(0, i);
	}

	public Profile getProfile(){
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
		List<Integer> erwuenschteSpiele = profile.getSpielliste(modus);
		for(int i=0; i<30; i++){
			if(!erwuenschteSpiele.contains(gameList.get(i))){
				gameList.set(i, SpielListen.RANDOM_GAME);
			}
		}
	}

	public List<Integer> getErwuenschteSpiele() {
		return profile.getSpielliste(modus);
	}
}