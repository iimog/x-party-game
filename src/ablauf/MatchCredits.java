package ablauf;

import static settings.Profile.CONSTANT;
import static settings.Profile.INCREASING;
import games.Game;
import games.Modus;
import gui.Anzeige;
import gui.EasyDialog;
import gui.components.DefaultButton;
import gui.components.JButtonIcon;
import gui.menu.HauptMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import player.Player;
import settings.Profile;
import util.ChangeManager;
import util.ConfirmListener;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MatchCredits extends Anzeige {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6638895977898659849L;
	public static final int UNENTSCHIEDEN = -999;
	private JPanel hauptBereich;
	private JPanel[] creditPanel;
	private JPanel[] detailCredits;
	private JLabel[] totalCreditLabel;
	private JLabel[] playerLabel;
	private JPanel[] playerPanel;
	private JButton nextGame;
	private JLabel[][] games;
	private JButton[] played;
	private JPanel playedGamesPanel;
	private int numOfGames;
	boolean beendet = false;
	private Ablauf ablauf;

	ArrayList<ChangeManager> cMs = new ArrayList<ChangeManager>();

	private int whichGame = 0;

	int[] totalCred;
	String[] player;
	private JPanel topPanel;
	private JButtonIcon quitButton;
	private int spielerZahl;
	private Player winner;
	private int punkteModus;
	public void setPunkteModus(int punkteModus) {
		this.punkteModus = punkteModus;
	}
	public MatchCredits(ablauf.Ablauf a) {
		ablauf = a;
		this.numOfGames = a.getNumOfGames();
		spielerZahl = a.getModus().getSpielerzahl();
		if(a.getModus() == Modus.SOLO){
			spielerZahl++;
		}
		initArrays();
		for(int i=0; i<spielerZahl; i++){
			player[i] = a.getPlayers()[i].name;
		}
		initGUI();
	}
	private void initArrays() {
		totalCred = new int[spielerZahl];
		player = new String[spielerZahl];
		playerLabel = new JLabel[spielerZahl];
		totalCreditLabel = new JLabel[spielerZahl];
		detailCredits = new JPanel[spielerZahl];
		playerPanel = new JPanel[spielerZahl];
		creditPanel = new JPanel[spielerZahl];
	}
	public void addChangeManager(ChangeManager cm) {
		cMs.add(cm);
	}

	private void createPanels() {
		createPlayedGamesPanel();
		games = new JLabel[spielerZahl][numOfGames];
		int numOfRows = (int)Math.ceil((double)numOfGames/5);
		int numOfCols = (int)Math.ceil((double)numOfGames/numOfRows);
		GridLayout detailCreditsLayout = new GridLayout();
		detailCreditsLayout.setHgap(5);
		detailCreditsLayout.setVgap(5);
		detailCreditsLayout.setColumns(numOfCols);
		detailCreditsLayout.setRows(numOfRows);
		for(int i = 0; i<spielerZahl; i++)
		{
			detailCredits[i] = new JPanel();
			detailCredits[i].setLayout(detailCreditsLayout);
			detailCredits[i].setBackground(Color.BLACK);
			detailCredits[i].setBorder(BorderFactory
					.createEtchedBorder(BevelBorder.LOWERED));
			{
				for (int j = 0; j < numOfGames; j++) {
					games[i][j] = new JLabel("" + (j + 1));
					games[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
					games[i][j].setBackground(Color.DARK_GRAY);
					games[i][j].setForeground(Color.WHITE);
					games[i][j].setHorizontalAlignment(SwingConstants.CENTER);
					games[i][j].setOpaque(true);
					detailCredits[i].add(games[i][j]);
				}
			}
		}
	}
	private void createPlayedGamesPanel() {
		played = new JButton[numOfGames];
		{
			playedGamesPanel = new JPanel();
			GridLayout playedGamesPanelLayout = new GridLayout(1, numOfGames);
			playedGamesPanelLayout.setHgap(5);
			playedGamesPanelLayout.setVgap(5);
			playedGamesPanelLayout.setColumns(numOfGames);
			playedGamesPanel.setLayout(playedGamesPanelLayout);
			playedGamesPanel.setOpaque(false);
			{
				for (int i = 0; i < numOfGames; i++) {
					played[i] = new DefaultButton("" + (i + 1));
					playedGamesPanel.add(played[i]);
					played[i].setEnabled(false);
				}
			}
		}
	}

	private void fireChange() {
		Iterator<ChangeManager> i = cMs.iterator();
		while (i.hasNext()) {
			i.next().change();
		}
	}

	public void gameWinner(int player, Color farbe) {
		if(player == UNENTSCHIEDEN){
			unentschieden();
			return;
		}
		if(whichGame<numOfGames){
			games[player][whichGame].setBackground(farbe);
			played[whichGame].setBackground(Color.darkGray);
		}
		whichGame++;
		if(whichGame>=numOfGames){
			nextGame.setText("Entscheidungsspiel");
		}
		if(punkteModus == INCREASING){
			totalCred[player] += whichGame;
		}
		else if(punkteModus == CONSTANT){
			totalCred[player]++;
		}
		ablauf.getPlayers()[player].matchCredit = totalCred[player];
		totalCreditLabel[player].setText("" + totalCred[player]);
		if (isOver()){
			winner = ablauf.getPlayers()[player];
			matchOver();
		}
	}

	private void unentschieden() {
		if(whichGame<numOfGames){
			played[whichGame].setBackground(Color.darkGray);
		}
		whichGame++;
		if(whichGame>=numOfGames){
			nextGame.setText("Entscheidungsspiel");
		}
		if (isOver()){
			winner = ablauf.getPlayers()[0];
			int meistePunkte = getMeistePunkte();
			for(int i=1; i<ablauf.getPlayers().length; i++){
				if(totalCred[i]==meistePunkte)
					winner = ablauf.getPlayers()[i];
			}
			matchOver();
		}
	}
	private boolean isOver() {
		int restPunkte = getRestPunkte();
		int meistePunkte = getMeistePunkte();
		int zweitMeistePunkte = getZweitMeistePunkte(meistePunkte);
		boolean over = (restPunkte<(meistePunkte-zweitMeistePunkte));
		return over;
	}
	private int getZweitMeistePunkte(int meistePunkte) {
		boolean meisteErreicht = false;
		int zweitMeistePunkte = 0;
		for(int i=0; i<totalCred.length; i++){
			int punkte = totalCred[i];
			if(punkte>zweitMeistePunkte && punkte<meistePunkte){
				zweitMeistePunkte = punkte;
			}
			if(punkte == meistePunkte){
				if(meisteErreicht){
					zweitMeistePunkte = punkte;
				}
				if(!meisteErreicht){
					meisteErreicht = true;
				}
			}	
		}
		
		return zweitMeistePunkte;
	}
	private int getMeistePunkte() {
		int meistePunkte = totalCred[0];
		for(int i=1; i<totalCred.length; i++){
			if(totalCred[i]>meistePunkte){
				meistePunkte = totalCred[i];
			}
		}
		return meistePunkte;
	}
	private int getRestPunkte() {
		int rest = numOfGames - whichGame;
		if(punkteModus == Profile.INCREASING){
			rest = (int)(((double)(numOfGames+whichGame+1)/2.0)*(numOfGames-whichGame));
		}
		return rest;
	}
	
	private void initGUI() {
		try {
			createPanels();
			this.setLayout(new BorderLayout());
			{
				hauptBereich = new JPanel();
				GridLayout hauptBereichLayout = new GridLayout(1, spielerZahl);
				if(ablauf.getModus() == Modus.VIERER){
					hauptBereichLayout = new GridLayout(2,2);
				}
				hauptBereichLayout.setHgap(5);
				hauptBereichLayout.setVgap(5);
				this.add(hauptBereich, BorderLayout.CENTER);
				hauptBereich.setLayout(hauptBereichLayout);
				hauptBereich.setBackground(Color.BLACK);
			//	hauptBereich.setPreferredSize(new java.awt.Dimension(384, 262));
				for(int i=0; i<spielerZahl; i++){
					playerPanel[i] = new JPanel();
					playerPanel[i].setBackground(Color.BLACK);
					BorderLayout playerPanelLayout = new BorderLayout();
					hauptBereich.add(playerPanel[i]);
					playerPanel[i].setLayout(playerPanelLayout);
					{
						playerLabel[i] = new JLabel();
						playerLabel[i].setText(player[i]);
						playerLabel[i].setFont(Game.PLAYER_FONT);
						playerLabel[i]
						            .setHorizontalAlignment(SwingConstants.CENTER);
						playerLabel[i].setForeground(Color.WHITE);
						playerPanel[i].add(playerLabel[i], BorderLayout.NORTH);
					}
					{
						creditPanel[i] = new JPanel();
						creditPanel[i].setOpaque(false);
						GridLayout creditPanelLayout = new GridLayout(2, 1);
						creditPanelLayout.setHgap(5);
						creditPanelLayout.setVgap(5);
						creditPanelLayout.setColumns(1);
						creditPanelLayout.setRows(2);
						playerPanel[i].add(creditPanel[i], BorderLayout.CENTER);
						creditPanel[i].setLayout(creditPanelLayout);
						{
							totalCreditLabel[i] = new JLabel();
							totalCreditLabel[i].setOpaque(true);
							totalCreditLabel[i].setBackground(Color.DARK_GRAY);
							creditPanel[i].add(totalCreditLabel[i]);
							totalCreditLabel[i].setText("0");
							totalCreditLabel[i].setFont(new java.awt.Font(
									"Comic Sans MS", 1, 72));
							totalCreditLabel[i]
							                 .setHorizontalAlignment(SwingConstants.CENTER);
							totalCreditLabel[i].setForeground(Color.WHITE);
							totalCreditLabel[i].setBorder(BorderFactory
									.createEtchedBorder(BevelBorder.LOWERED));
						}
						{
							creditPanel[i].add(detailCredits[i]);
						}
					}
				}
			}
			{
				nextGame = new DefaultButton();
				this.add(nextGame, BorderLayout.SOUTH);
				nextGame.setText("Nächstes Spiel");
				nextGame.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						fireChange();
					}
				});
			}
			{
				this.add(playedGamesPanel, BorderLayout.NORTH);
			}
			{
				topPanel = new JPanel();
				BorderLayout topPanelLayout = new BorderLayout();
				topPanel.setLayout(topPanelLayout);
				topPanel.setBackground(Color.BLACK);
				add(topPanel, BorderLayout.NORTH);
				{
					quitButton = new JButtonIcon("media/ablauf/quit.png","Quit");
					quitButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent evt){
							quitButtonActionPerformed(evt);
						}
					});
					topPanel.add(quitButton, BorderLayout.EAST);
				}
				topPanel.add(playedGamesPanel, BorderLayout.CENTER);
			}
			this.setSize(900, 600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void quitButtonActionPerformed(ActionEvent evt) {
		if(beendet){
			fireChange();return;
		}
		EasyDialog.showConfirm("Wollen Sie das Spiel speichern bevor Sie es verlassen " +
				"(falls nicht geht der Spielstand verloren)", null, new ConfirmListener() {
					@Override
					public void confirmOptionPerformed(int optionType) {
						if(optionType == ConfirmListener.YES_OPTION){
							instance.changeAnzeige(new HauptMenu());
						}
						if(optionType == ConfirmListener.NO_OPTION){
							MatchFileHandler.deleteMatchFile(ablauf.getSpeicherDatei());
							instance.changeAnzeige(new HauptMenu());
						}
						instance.closeDialog();
					}
				});
		
	}
	public void matchOver() {
		beendet = true;
		nextGame.setText("Siegerehrung");
	}
	
	public void aktualisierePlayer(){
		if(ablauf.getPlayers()[0]!=null){
			playerLabel[0].setText(ablauf.getPlayers()[0].name);
		}
		if(ablauf.getPlayers()[1]!=null){
			playerLabel[1].setText(ablauf.getPlayers()[1].name);
		}
	}
	public Player getWinner() {
		return winner;
	}

}
