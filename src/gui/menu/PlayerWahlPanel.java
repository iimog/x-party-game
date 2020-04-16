package gui.menu;

import gui.Anzeige;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import player.Player;
import player.PlayerFileHandler;
import start.X;
import util.ChangeManager;
import util.PlayerAsker;

public class PlayerWahlPanel extends Anzeige {
	class PlayerFacChangeManager implements ChangeManager{
		@Override
		public void change() {
			if(!playerFac.abgebrochen){
				playerList.setModel(new DefaultComboBoxModel<String>(getPlayerList()));
				playerAsker.thisIsThePlayer(myPlayer);
			}
			hauptbereichPanel.removeAll();
			hauptbereichPanel.add(playerButtonPanel, BorderLayout.NORTH);
			hauptbereichPanel.add(new JScrollPane(playerList), BorderLayout.CENTER);
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
	private JList<String> playerList;
	private PlayerFactory playerFac;

	private File playerFolder = new File(X.getDataDir()+"Player");
	private Player myPlayer;


	private PlayerAsker playerAsker;

	public PlayerWahlPanel(PlayerAsker playerAsker) {
		this.playerAsker = playerAsker;
		initGUI();
	}

	private String[] getPlayerList(){
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		if(playerFolder.exists()){
			String[] arr = playerFolder.list();
			for(int i=0; i<arr.length; i++){
				if(arr[i].endsWith(".player")){
					list.add(arr[i].substring(0, arr[i].length()-7));
				}
			}
		}
		return list.toArray(new String[0]);
	}

	private void initGUI() {
		try {
			this.setOpaque(false);
			{
				initHauptbereichPanel();
				this.add(hauptbereichPanel, BorderLayout.CENTER);
				{
					initPlayerButtonPanel();
					hauptbereichPanel.add(playerButtonPanel, BorderLayout.NORTH);
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
					{
						initPlayerList();
						hauptbereichPanel.add(new JScrollPane(playerList), BorderLayout.CENTER);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHauptbereichPanel() {
		hauptbereichPanel = new JPanel();
		BorderLayout hauptbereichPanelLayout = new BorderLayout();
		hauptbereichPanel.setLayout(hauptbereichPanelLayout);
		hauptbereichPanel.setOpaque(false);
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
		ListModel<String> playerListModel =
			new DefaultComboBoxModel<String>(
					getPlayerList());
		playerList = new JList<String>();
		playerList.setModel(playerListModel);
	}

	private void initNeuButton() {
		neuButton = new DefaultButton();
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
		bearbeitenButton = new DefaultButton();
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
		auswahlButton = new DefaultButton();
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
		try {
			myPlayer = PlayerFileHandler.loadPlayer(playerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		playerAsker.thisIsThePlayer(myPlayer);
	}

	private void initEntfernenButton() {
		entfernenButton = new DefaultButton();
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
			playerList.setModel(new DefaultComboBoxModel<String>(getPlayerList()));
		}
	}
}
