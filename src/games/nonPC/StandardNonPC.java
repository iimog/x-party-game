package games.nonPC;

import games.Game;
import games.Modus;
import games.NonPC;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import player.Player;


public abstract class StandardNonPC extends Game implements NonPC {

	/**
	 * serialVersionUID generated by Eclipse
	 */
	private static final long serialVersionUID = -2754430706630844851L;
	private JPanel punktePanel;
	private JButton[] punktGemachtButton;

	public StandardNonPC(Player[] player, int numOfRounds, Modus modus, int globalGameID) {
		super(player, numOfRounds, modus, globalGameID);
		punktGemachtButton = new JButton[spielerZahl];
		initGUI();
	}

	class PunktActionListener implements ActionListener{
		private int index;
		public PunktActionListener(int index){
			this.index = index;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			punktFor(index);
		}
	}
	
	private void initGUI(){
		punktePanel = new JPanel();
		GridLayout punktePanelLayout=new GridLayout(1,spielerZahl);
		punktePanelLayout.setHgap(10);
		punktePanel.setLayout(punktePanelLayout);
		spielBereichPanel.add(punktePanel);
		punktePanel.setSize(300,150);
		for(int i=0; i<spielerZahl; i++)
		{
			punktGemachtButton[i] = new JButton("Punkt für "+myPlayer[i].name);
			punktePanel.add(punktGemachtButton[i]);
			punktGemachtButton[i].addActionListener(new PunktActionListener(i));
		}
		updateCreds();
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new StandardSettingsDialog(this));
	}

	private void punktFor(int playerID){
		creds[playerID].earnsCredit(1);
		myPlayer[playerID].gameCredit++;
		if(modus == Modus.TEAM){
			changeActivePlayers();
		}
		if(isOver())gameEnd();
	}

	@Override
	public void settingsChanged(){
		updateCreds();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

}
