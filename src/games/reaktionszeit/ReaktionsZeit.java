package games.reaktionszeit;

import games.Game;
import games.Modus;
import games.dialogeGUIs.RoundDialog;
import gui.components.rotator.StringRotator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import start.X;

public class ReaktionsZeit extends Game {
	private static final long serialVersionUID = 1L;
	private JPanel hauptbereichPanel;
	private JLabel target;
	private JPanel queryPanel;
	private StringRotator queryRotator;
	private int targetIndex;
	private int currentIndex;
	private HashSet<Integer> winnerIDs;
	private int rotationTime = 1;

	public ReaktionsZeit(Player[] player, Modus modus,
			int globalGameID) {
		super(player, 5, modus, globalGameID);
		initGUI();
	}
	
	private void initGUI(){
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		addQuery();
		addTarget();
	}

	private void addQuery() {
		queryPanel = new JPanel(new GridLayout(1,1));
		hauptbereichPanel.add(queryPanel, BorderLayout.CENTER);
		queryRotator = new StringRotator(new File(X.getMainDir() + "games/pc/reaktionszeit/small_words.deck"));
		queryRotator.setBackground(Color.BLACK);
		queryRotator.setForeground(Color.WHITE);
		queryRotator.setFont(Game.STANDARD_FONT.deriveFont(50f));
		queryRotator.setRotationTime(rotationTime);
		queryPanel.add(queryRotator);
	}

	private void addTarget() {
		target = new JLabel("X");
		target.setFont(Game.STANDARD_FONT.deriveFont(70f));
		target.setBackground(Color.WHITE);
		hauptbereichPanel.add(target, BorderLayout.NORTH);
	}

	@Override
	public void settingsChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		nextRound();
	}

	private void nextRound() {
		List<String> strings = queryRotator.getStringList();
		targetIndex = new Random().nextInt(strings.size());
		target.setText(strings.get(targetIndex));
		queryRotator.start();
		setBuzzerActive(true);
	}
	
	@Override
	public void buzzeredBy(int whoBuzz){
		queryRotator.pause();
		currentIndex = queryRotator.getCurrentIndex();
		winnerIDs = new HashSet<Integer>();	
		if(currentIndex != targetIndex){
			for(int i=0; i<spielerZahl; i++){
				if(i != whoBuzz) winnerIDs.add(i);
			}
		}
		else{
			winnerIDs.add(whoBuzz);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
	}
	
	private void verbuchePunkte(Set<Integer> winnerIDs) {
		if(winnerIDs.size() < myPlayer.length){
			for(int id : winnerIDs){
				creds[id].earnsCredit(1);
				myPlayer[id].gameCredit++;
			}
		}
	}
	
	private String getWinnerText(Set<Integer> winnerIDs) {
		String winner = "";
		if(winnerIDs.size() >= myPlayer.length || winnerIDs.size()==0){
			winner = "niemanden";
		}
		else{
			int c=0;
			for(int id : winnerIDs){
				String trenner = "";
				if(c==winnerIDs.size()-3)trenner = ", ";
				if(c==winnerIDs.size()-2)trenner = " und ";
				winner += myPlayer[id].name+trenner;
				c++;
			}
		}
		return winner;
	}
	
	@Override
	public void openRoundDialog(String winner){
		RoundDialog rd = new RoundDialog(this, winner);
		rd.enableInfo(false);
		instance.showDialog(rd);
	}
	
	@Override
	public void goBack() {
		if(!isOver())nextRound();
	}

	@Override
	public void openSettingsDialog(){
	//	instance.showDialog(new ReaktionsZeitSettingsDialog(this));
	}
	
	@Override
	public void openDetailsDialog(){
	//	instance.showDialog(new ReaktionsZeitDetailsDialog(this));
	}

}
