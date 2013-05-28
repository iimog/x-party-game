package start;
import games.Modus;
import gui.menu.PlayerFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;

import player.Player;
import util.ChangeManager;
import ablauf.MatchCredits;


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
@SuppressWarnings("serial")
public class TestStarter extends JFrame implements ChangeManager, games.GameListener{
	public static void main(String[] args) {
		new TestStarter();
	}
	public int numOfGames=8;
	private JPanel hauptbereichPanel;
	private JTextField player1TextField;
	private JComboBox gameComboBox;
	private JLabel gameLabel;
	private JSpinner numOfRoundsSpinner;
	private JLabel numOfRoundsLabel;
	private JTextField player2TextField;
	private JLabel player2Label;
	private Player[] myPlayers = new Player[2];
	private PlayerFactory plF1;
	private PlayerFactory plF2;
	private JLabel player1Label;
	private JButton startButton;
	public String[] spiele = new String[numOfGames];
	public games.Game actGame;

	public MatchCredits mc;

	public TestStarter(){
		super("Test Starter");
		spiele[0]="Schätz mal!";
		spiele[1]="Fehlerteufel";
		spiele[2]="Böse 6";
		spiele[3]="Wo?";
		spiele[4]="Säutreiberspiel";
		spiele[5]="Memory";
		spiele[6]="Geister";
		spiele[7]="Innere Uhr";
		BorderLayout thisLayout = new BorderLayout();
		getContentPane().setLayout(thisLayout);
		/*  for(int i=0;i<numOfGames;i++){
			add(game[i]);
		}  */
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		try{
			ImageIcon bild1 = new ImageIcon(this.getClass().getResource("/media/rest/sdr2.png"));
			setIconImage(bild1.getImage());
		}
		catch(Exception e){
			// nichts
		}
		{
			startButton = new JButton();
			getContentPane().add(startButton, BorderLayout.SOUTH);
			startButton.setText("Spiel starten");
			startButton.setFont(new java.awt.Font("Comic Sans MS",0,20));
			startButton.setForeground(new java.awt.Color(128,0,0));
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					startButtonActionPerformed(evt);
				}
			});
		}
		{
			hauptbereichPanel = new JPanel();
			GridLayout hauptbereichPanelLayout = new GridLayout(4, 2);
			hauptbereichPanelLayout.setHgap(5);
			hauptbereichPanelLayout.setVgap(5);
			hauptbereichPanelLayout.setColumns(2);
			hauptbereichPanelLayout.setRows(4);
			hauptbereichPanel.setLayout(hauptbereichPanelLayout);
			getContentPane().add(hauptbereichPanel, BorderLayout.CENTER);
			{
				player1Label = new JLabel();
				hauptbereichPanel.add(player1Label);
				player1Label.setText("Spieler 1:");
				player1Label.setFont(new java.awt.Font("Comic Sans MS",1,20));
				player1Label.setToolTipText("Spieler1 bearbeiten");
				player1Label.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent evt) {
						player1LabelMouseClicked(evt);
					}
				});
			}
			{
				player1TextField = new JTextField();
				player1TextField.setColumns(12);
				hauptbereichPanel.add(player1TextField);
				player1TextField.setFont(new java.awt.Font("Comic Sans MS",1,18));
				player1TextField.setForeground(new java.awt.Color(0,0,255));
			}
			{
				player2Label = new JLabel();
				hauptbereichPanel.add(player2Label);
				player2Label.setText("Spieler 2:");
				player2Label.setFont(new java.awt.Font("Comic Sans MS",1,20));
				player2Label.setToolTipText("Spieler2 bearbeiten");
				player2Label.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent evt) {
						player2LabelMouseClicked(evt);
					}
				});
			}
			{
				player2TextField = new JTextField();
				hauptbereichPanel.add(player2TextField);
				player2TextField.setFont(new java.awt.Font("Comic Sans MS",1,18));
				player2TextField.setForeground(new java.awt.Color(0,0,255));
			}
			{
				gameLabel = new JLabel();
				hauptbereichPanel.add(gameLabel);
				gameLabel.setText("Spiel:");
				gameLabel.setFont(new java.awt.Font("Comic Sans MS",0,16));
			}
			{
				ComboBoxModel gameComboBoxModel =
					new DefaultComboBoxModel(
							spiele);
				gameComboBox = new JComboBox();
				gameComboBox.setFont(new java.awt.Font("Comic Sans MS",0,16));
				hauptbereichPanel.add(gameComboBox);
				gameComboBox.setModel(gameComboBoxModel);
				gameComboBox.setForeground(new java.awt.Color(0,0,255));
			}
			{
				numOfRoundsLabel = new JLabel();
				hauptbereichPanel.add(numOfRoundsLabel);
				numOfRoundsLabel.setText("Gewinnsätze:");
				numOfRoundsLabel.setFont(new java.awt.Font("Comic Sans MS",0,16));
			}
			{
				SpinnerListModel numOfRoundsSpinnerModel =
					new SpinnerListModel(
							new String[] { "1", "2" , "3" , "4" , "5" , "6" , "7" });
				numOfRoundsSpinnerModel.setValue("5");
				numOfRoundsSpinner = new JSpinner();
				hauptbereichPanel.add(numOfRoundsSpinner);
				numOfRoundsSpinner.setModel(numOfRoundsSpinnerModel);
			}
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}

	@Override
	public void change() {
		if(plF1!=null){
			player1TextField.setText(plF1.getNamen());
			player1TextField.setForeground(plF1.getFarbe());
		}
		if(plF2!=null){
			player2TextField.setText(plF2.getNamen());
			player2TextField.setForeground(plF2.getFarbe());
		}
	}

	public void gameOver(){
		String win="";
		if(actGame.myPlayer[0].gameCredit > actGame.myPlayer[1].gameCredit){
			win = actGame.myPlayer[0].name;
			mc.gameWinner(0, myPlayers[0].farbe);
		}
		if(actGame.myPlayer[0].gameCredit < actGame.myPlayer[1].gameCredit){
			win = actGame.myPlayer[1].name;
			mc.gameWinner(1, myPlayers[1].farbe);
		}
		if(actGame.myPlayer[0].gameCredit == actGame.myPlayer[1].gameCredit){
			win = "keiner";
		}
		String stand = actGame.myPlayer[0].gameCredit + " zu " + actGame.myPlayer[1].gameCredit;
		setVisible(true);
		// TODO actGame.dispose();
		if(!stand.equals("0 zu 0")){
			mc.setVisible(true);
			new games.dialogeGUIs.WinnerDialog(stand, win);
		}
	}

	private void player1LabelMouseClicked(MouseEvent evt) {
		if(plF1==null){
			plF1 = new PlayerFactory(player1TextField.getText());
			plF1.setCM(this);
		}
		else{
			plF1.setNamen(player1TextField.getText());
			plF1.activate();
		}
	}

	private void player2LabelMouseClicked(MouseEvent evt) {
		if(plF2==null){
			plF2 = new PlayerFactory(player2TextField.getText());
			plF2.setCM(this);
		}
		else{
			plF2.setNamen(player2TextField.getText());
			plF2.activate();
		}
	}
	private void startButtonActionPerformed(ActionEvent evt) {
		String pl1 = player1TextField.getText();
		String pl2 = player2TextField.getText();
		if(pl1.equals(""))pl1="Dick";
		if(pl2.equals(""))pl2="Doof";
		if(plF1!=null){
			myPlayers[0] = plF1.getPlayer();
			myPlayers[0].setName(pl1);
		}
		else{
			myPlayers[0] = new Player(pl1);
		}
		if(plF2!=null){
			myPlayers[1] = plF2.getPlayer();
			myPlayers[1].setName(pl2);
		}
		else{
			myPlayers[1] = new Player(pl2);
		}
		int numOfRounds = Integer.parseInt(numOfRoundsSpinner.getValue().toString());
		if(gameComboBox.getSelectedItem().equals(spiele[0])){
			actGame = new games.guess.Guess(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[1])){
			actGame = new games.difference.Difference(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[2])){
			actGame = new games.bad6.Bad6(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[3])){
			actGame = new games.world.World(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[4])){
			actGame = new games.sautreiber.SauTreiber(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[5])){
			actGame = new games.memory.Memory(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[6])){
			actGame = new games.ghosts.Ghosts(myPlayers,numOfRounds, Modus.DUELL);
		}
		if(gameComboBox.getSelectedItem().equals(spiele[7])){
			actGame = new games.innereUhr.InnereUhr(myPlayers,numOfRounds, Modus.DUELL);
		}
		actGame.addGameListener(this);
		this.setVisible(false);
	}

}
