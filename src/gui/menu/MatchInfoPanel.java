package gui.menu;

import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ablauf.MatchInfo;

public class MatchInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MatchInfo matchInfo;
	public MatchInfo getMatchInfo() {
		return matchInfo;
	}

	private Bildschirm modusIcon;
	private JLabel spielerLabel;
	private JPanel infoPanel;
	private int spielerZahl;
	
	public MatchInfoPanel(MatchInfo matchInfo){
		this.matchInfo = matchInfo;
		spielerZahl = matchInfo.getPlayer().length;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout(15,0));
		this.setOpaque(false);
		modusIcon = new Bildschirm("/media/modus/"+matchInfo.getModus()+".png");
		modusIcon.setToolTipText(matchInfo.getModus()+"");
		this.add(modusIcon, BorderLayout.WEST);
		spielerLabel = getSpielerLabel();
		this.add(spielerLabel, BorderLayout.CENTER);
		infoPanel = getInfoPanel();
		this.add(infoPanel, BorderLayout.EAST);
	}

	private JPanel getInfoPanel() {
		JPanel iPanel = new JPanel();
		iPanel.setOpaque(false);
		iPanel.setLayout(new GridLayout(3, 1));
		JLabel datumLabel = new JLabel(matchInfo.getDatum());
		Font infoFont = datumLabel.getFont().deriveFont(15f);
		datumLabel.setFont(infoFont);
		datumLabel.setForeground(Color.WHITE);
		iPanel.add(datumLabel);
		StringBuffer spielstand = new StringBuffer(matchInfo.getSpielstand()[0]+"");
		for(int i=1; i<spielerZahl; i++){
			spielstand.append(" : ");
			spielstand.append(matchInfo.getSpielstand()[i]);
		}
		JLabel spielstandLabel = new JLabel(spielstand.toString());
		spielstandLabel.setFont(infoFont);
		spielstandLabel.setForeground(Color.WHITE);
		iPanel.add(spielstandLabel);
		JLabel numOfGameLabel = new JLabel(matchInfo.getNumOfGames() + " Sätze");
		numOfGameLabel.setFont(infoFont);
		numOfGameLabel.setForeground(Color.WHITE);
		iPanel.add(numOfGameLabel);
		return iPanel;
	}

	private JLabel getSpielerLabel() {
		StringBuffer spieler = new StringBuffer(matchInfo.getPlayer()[0]);
		for(int i=1; i<spielerZahl; i++){
			spieler.append(" vs ");
			spieler.append(matchInfo.getPlayer()[i]);
		}
		JLabel sLabel = new JLabel(spieler.toString());
		Font font = sLabel.getFont().deriveFont(30f);
		font = font.deriveFont(Font.BOLD);
		sLabel.setFont(font);
		sLabel.setForeground(Color.WHITE);
		return sLabel;
	}

}
