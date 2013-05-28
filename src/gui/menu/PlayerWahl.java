package gui.menu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import player.Player;
import player.PlayerFileHandler;
import start.X;
import util.ChangeManager;

public class PlayerWahl extends javax.swing.JPanel {
	class PlayerFacChangeManager implements ChangeManager{
		@Override
		public void change() {
			if(!playerFac.abgebrochen){
				playerList.setModel(new DefaultComboBoxModel(getPlayerList()));
				myPlayer[activeLabel] = playerFac.getPlayer();
				doppelteSpielerEntfernen(activeLabel);
				playerLabel[activeLabel].setText(myPlayer[activeLabel].name);
				playerLabel[activeLabel].setForeground(myPlayer[activeLabel].farbe);
				playerLabel[activeLabel].setBorder(null);
				activeLabel = (activeLabel+1)%spielerZahl;
				playerLabel[activeLabel].setBorder(activeBorder);
				startMatch.updatePlayers(myPlayer);
			}
			hauptbereichPanel.removeAll();
			hauptbereichPanel.add(spielerPanel);
			hauptbereichPanel.revalidate();
			hauptbereichPanel.repaint();
		}
	}
	private static final long serialVersionUID = -7711504681154266530L;
	private JPanel hauptbereichPanel;
	private JButton auswahlButton;
	private JButton entfernenButton;
	private JButton bearbeitenButton;
	private JButton neuButton;
	private JPanel playerButtonPanel;
	private JLabel[] playerLabel;
	private JList playerList;
	private JPanel spielerObenPanel;
	private JPanel spielerPanel;
	private int activeLabel=0;
	private PlayerFactory playerFac;

	private Border activeBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	private File playerFolder = new File(X.getDataDir()+"Player");
	private Player[] myPlayer;

	private StartMatch startMatch;
	private int spielerZahl;

	public PlayerWahl(StartMatch startMatch) {
		spielerZahl = startMatch.getModus().getSpielerzahl();
		myPlayer = new Player[spielerZahl];
		playerLabel = new JLabel[spielerZahl];
		this.startMatch = startMatch;
		myPlayer = startMatch.myPlayer;
		initGUI();
	}

	private Object[] getPlayerList(){
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		if(playerFolder.exists()){
			String[] arr = playerFolder.list();
			for(int i=0; i<arr.length; i++){
				if(arr[i].endsWith(".player")){
					list.add(arr[i].substring(0, arr[i].length()-7));
				}
			}
		}
		return list.toArray();
	}

	private void initGUI() {
		try {
			this.setOpaque(false);
			{
				initHauptbereichPanel();
				this.add(hauptbereichPanel, BorderLayout.CENTER);
				{
					initSpielerPanel();
					hauptbereichPanel.add(spielerPanel);
					{
						initSpielerObenPanel();
						spielerPanel.add(spielerObenPanel);
						{
							initPlayerLabels();
							for(int i=0; i<spielerZahl; i++){
								spielerObenPanel.add(playerLabel[i]);
							}
						}
						{
							initPlayerButtonPanel();
							spielerObenPanel.add(playerButtonPanel);
							{
								initNeuButton();
								playerButtonPanel.add(neuButton);
							}
							{
								initBearbeitenButton();
								playerButtonPanel.add(bearbeitenButton);
							}
							{
								initAuswahlButton();
								playerButtonPanel.add(auswahlButton);
							}
							{
								initEntfernenButton();
								playerButtonPanel.add(entfernenButton);
							}
						}
					}
					{
						initPlayerList();
						spielerPanel.add(new JScrollPane(playerList));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHauptbereichPanel() {
		hauptbereichPanel = new JPanel();
		GridLayout hauptbereichPanelLayout = new GridLayout(2, 1);
		hauptbereichPanelLayout.setHgap(5);
		hauptbereichPanelLayout.setVgap(5);
		hauptbereichPanelLayout.setColumns(2);
		hauptbereichPanel.setLayout(hauptbereichPanelLayout);
		hauptbereichPanel.setOpaque(false);
	}

	private void initSpielerPanel() {
		spielerPanel = new JPanel();
		GridLayout spielerPanelLayout = new GridLayout(2, 1);
		spielerPanelLayout.setHgap(5);
		spielerPanelLayout.setVgap(5);
		spielerPanelLayout.setColumns(1);
		spielerPanelLayout.setRows(2);
		spielerPanel.setLayout(spielerPanelLayout);
		spielerPanel.setOpaque(false);
	}

	private void initSpielerObenPanel() {
		spielerObenPanel = new JPanel();
		GridLayout spielerObenPanelLayout = new GridLayout(spielerZahl+1, 1);
		spielerObenPanelLayout.setHgap(5);
		spielerObenPanelLayout.setVgap(5);
		spielerObenPanelLayout.setColumns(1);
		spielerObenPanelLayout.setRows(spielerZahl+1);
		spielerObenPanel.setLayout(spielerObenPanelLayout);
		spielerObenPanel.setOpaque(false);
	}

	private void initPlayerLabels() {
		for(int i=0; i<spielerZahl; i++)
			initPlayerLabel(i);
	}
	private void initPlayerLabel(int index){
		playerLabel[index] = new JLabel();
		spielerObenPanel.add(playerLabel[index]);
		if(myPlayer[index]==null){
			playerLabel[index].setText("Player " + (1+index));
			playerLabel[index].setForeground(java.awt.Color.darkGray);
		}
		else{
			playerLabel[index].setText(myPlayer[index].name);
			playerLabel[index].setForeground(myPlayer[index].farbe);
		}
		playerLabel[index].setHorizontalAlignment(SwingConstants.CENTER);
		playerLabel[index].setFont(new java.awt.Font("Comic Sans MS",1,18));
		//	playerLabel[0].setOpaque(true);
		playerLabel[index].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				playerLabelMouseClicked(evt);
			}
		});
		if(index == activeLabel){
			playerLabel[index].setBorder(activeBorder);
		}
	}

	private void initPlayerButtonPanel() {
		playerButtonPanel = new JPanel();
		GridLayout playerButtonPanelLayout = new GridLayout(1, 4);
		playerButtonPanelLayout.setHgap(5);
		playerButtonPanelLayout.setVgap(5);
		playerButtonPanelLayout.setColumns(4);
		playerButtonPanel.setLayout(playerButtonPanelLayout);
		playerButtonPanel.setOpaque(false);
	}

	private void initPlayerList() {
		ListModel playerListModel =
			new DefaultComboBoxModel(
					getPlayerList());
		playerList = new JList();
		playerList.setModel(playerListModel);
	}

	private void initNeuButton() {
		neuButton = new JButton();
		neuButton.setText("Neu");
		neuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				neuButtonActionPerformed(evt);
			}
		});
	}

	private void neuButtonActionPerformed(ActionEvent evt) {
		playerFac = new PlayerFactory();
		playerFac.setCM(new PlayerFacChangeManager());
		playerFac.setTeamMode(false);
		hauptbereichPanel.removeAll();
		hauptbereichPanel.add(playerFac);
		hauptbereichPanel.revalidate();
		hauptbereichPanel.repaint();
	}

	private void initBearbeitenButton() {
		bearbeitenButton = new JButton();
		bearbeitenButton.setText("Bearbeiten");
		bearbeitenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				bearbeitenButtonActionPerformed(evt);
			}
		});
	}

	private void bearbeitenButtonActionPerformed(ActionEvent evt) {
		if(playerList.getSelectedValue() == null)return;
		String playerName = playerList.getSelectedValue().toString();
		Player p = PlayerFactory.createDefaultPlayer();
		try {
			p = PlayerFileHandler.loadPlayer(playerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		playerFac = new PlayerFactory(p);
		playerFac.setCM(new PlayerFacChangeManager());
		hauptbereichPanel.removeAll();
		hauptbereichPanel.add(playerFac);
		hauptbereichPanel.revalidate();
		hauptbereichPanel.repaint();
	}

	private void initAuswahlButton() {
		auswahlButton = new JButton();
		auswahlButton.setText("Wählen");
		auswahlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				auswahlButtonActionPerformed(evt);
			}
		});
	}

	private void auswahlButtonActionPerformed(ActionEvent evt) {
		if(playerList.getSelectedValue() == null)return;
		String playerName = playerList.getSelectedValue().toString();
		playerLabel[activeLabel].setText(playerName);
		playerLabel[activeLabel].setBorder(null);
		try {
			myPlayer[activeLabel] = PlayerFileHandler.loadPlayer(playerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		doppelteSpielerEntfernen(activeLabel);
		playerLabel[(activeLabel+1)%spielerZahl].setBorder(activeBorder);
		
		playerLabel[activeLabel].setForeground(myPlayer[activeLabel].farbe);
		activeLabel = (activeLabel+1)%spielerZahl;
		startMatch.updatePlayers(myPlayer);
	}

	private void initEntfernenButton() {
		entfernenButton = new JButton();
		entfernenButton.setText("Löschen");
		entfernenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				entfernenButtonActionPerformed(evt);
			}
		});
	}

	private void entfernenButtonActionPerformed(ActionEvent evt) {
		if(playerList.getSelectedValue() == null)return;
		String playerName = playerList.getSelectedValue().toString();
		int i = JOptionPane.showConfirmDialog(this, "Wollen Sie den Spieler " + playerName + " wirklich löschen?","Spieler löschen",JOptionPane.WARNING_MESSAGE);
		if(i == JOptionPane.OK_OPTION){
			File lf = new File(playerFolder, playerName+".player");
			lf.delete();
			playerList.setModel(new DefaultComboBoxModel(getPlayerList()));
		}
	}

	private void playerLabelMouseClicked(MouseEvent evt) {
		JLabel sourceLabel = (JLabel)evt.getSource();
		int source = 0;
		if(spielerZahl>1 && sourceLabel == playerLabel[1]){
			source = 1;
		}
		if(spielerZahl>2 && sourceLabel == playerLabel[2]){
			source = 2;
		}
		if(spielerZahl>3 && sourceLabel == playerLabel[3]){
			source = 3;
		}
		playerLabel[activeLabel].setBorder(null);
		activeLabel = source;
		playerLabel[source].setBorder(activeBorder);
	}

	private void doppelteSpielerEntfernen(int neuerSpieler) {
		for(int i=0; i<spielerZahl; i++){
			if(i!=neuerSpieler && myPlayer[i] != null &&
					myPlayer[neuerSpieler].getName().equals(myPlayer[i].getName())){
				myPlayer[i] = null;
				playerLabel[i].setText("Spieler "+(i+1));
				playerLabel[i].setForeground(Color.BLACK);
			}
		}
	}
}
