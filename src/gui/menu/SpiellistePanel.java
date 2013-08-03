package gui.menu;

import games.Modus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import start.X;
import util.SpielListen;

public class SpiellistePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Modus modus;
	private StartMatch startMatch;
	private JPanel spieltypPanel;
	private ButtonGroup buttonGroup;
	private JRadioButton pcRadioButton;
	private JRadioButton beidesRadioButton;
	private JRadioButton nonPcRadioButton;
	private JPanel listePanel;
	private JCheckBox[] spielCheckBox;
	private List<Integer> gesamtMoeglicheSpieleIDs;
	private JPanel obenPanel;
	private JButton alleWaehlenButton;
	private boolean alleGewaehlt;
	private JScrollPane scrollPanel;
	
	public SpiellistePanel(StartMatch startMatch){
		this.startMatch = startMatch;
		this.modus = startMatch.getModus();
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		initSpieltypPanel();
		obenPanel = new JPanel();
		obenPanel.setLayout(new BorderLayout(15,0));
		obenPanel.add(spieltypPanel, BorderLayout.CENTER);
		initAlleWaehlenButton();
		obenPanel.add(alleWaehlenButton, BorderLayout.WEST);
		this.add(obenPanel, BorderLayout.NORTH);
		initListPanel();
		scrollPanel = new JScrollPane(listePanel);
		scrollPanel.setPreferredSize(new Dimension(scrollPanel.getPreferredSize().width, X.getInstance().getHeight()-350));
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPanel, BorderLayout.CENTER);
	}

	private void initAlleWaehlenButton() {
		alleGewaehlt = true;
		alleWaehlenButton = new JButton("Alle abwählen");
		alleWaehlenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				alleGewaehlt = !alleGewaehlt;
				for(int i=1; i<gesamtMoeglicheSpieleIDs.size(); i++){
					spielCheckBox[i].setSelected(alleGewaehlt);
				}
				pcRadioButtonSelectionChanged();
				if(alleGewaehlt){
					alleWaehlenButton.setText("Alle abwählen");
				}
				else{
					alleWaehlenButton.setText("Alle auswählen");
				}
			}
		});
	}

	private void initSpieltypPanel() {
		spieltypPanel = new JPanel();
		spieltypPanel.setLayout(new GridLayout(1,3));
		buttonGroup = new ButtonGroup();
		ActionListener pcRadioButtonListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				pcRadioButtonSelectionChanged();				
			}
		};
		pcRadioButton = new JRadioButton("nur PC");
		pcRadioButton.addActionListener(pcRadioButtonListener);
		spieltypPanel.add(pcRadioButton);
		buttonGroup.add(pcRadioButton);
		beidesRadioButton = new JRadioButton("beides");
		beidesRadioButton.addActionListener(pcRadioButtonListener);
		spieltypPanel.add(beidesRadioButton);
		buttonGroup.add(beidesRadioButton);
		nonPcRadioButton = new JRadioButton("nur NonPC");
		nonPcRadioButton.addActionListener(pcRadioButtonListener);
		spieltypPanel.add(nonPcRadioButton);
		buttonGroup.add(nonPcRadioButton);
		beidesRadioButton.setSelected(true);
	}

	private void initListPanel() {
		listePanel = new JPanel();
		gesamtMoeglicheSpieleIDs = SpielListen.moeglicheSpiele(modus);
		List<Integer> gewaehlteSpiele = startMatch.getErwuenschteSpiele();
		listePanel.setLayout(new GridLayout(gesamtMoeglicheSpieleIDs.size(),2));
		spielCheckBox = new JCheckBox[gesamtMoeglicheSpieleIDs.size()];
		for(int i=0; i<gesamtMoeglicheSpieleIDs.size(); i++){
			String gameName = SpielListen.getGameName(gesamtMoeglicheSpieleIDs.get(i));
			JLabel l = new JLabel(gameName);
			listePanel.add(l);
			spielCheckBox[i] = new JCheckBox();
			if(gewaehlteSpiele.contains(gesamtMoeglicheSpieleIDs.get(i)))
				spielCheckBox[i].setSelected(true);
			listePanel.add(spielCheckBox[i]);
		}
		spielCheckBox[0].setEnabled(false);	// Zufall nicht abwählbar
	}

	public List<Integer> getGewaehlteSpiele() {
		List<Integer> gewaehlteSpiele = Collections.synchronizedList(new ArrayList<Integer>());
		for(int i=0; i<gesamtMoeglicheSpieleIDs.size(); i++){
			if(spielCheckBox[i].isSelected())
				gewaehlteSpiele.add(gesamtMoeglicheSpieleIDs.get(i));
		}
		return gewaehlteSpiele;
	}
	
	public void setGewaehlteSpiele(List<Integer> gewaehlteSpiele){
		for(int i=0; i<gesamtMoeglicheSpieleIDs.size(); i++){
			if(gewaehlteSpiele.contains(gesamtMoeglicheSpieleIDs.get(i)))
				spielCheckBox[i].setSelected(true);
			else
				spielCheckBox[i].setSelected(false);
		}
	}
	
	private void pcRadioButtonSelectionChanged(){
		List<Integer> modusSpezifischeSpiele = gesamtMoeglicheSpieleIDs;
		if(pcRadioButton.isSelected()){
			modusSpezifischeSpiele = SpielListen.getPCSpieleIDs(modus);
		}
		if(nonPcRadioButton.isSelected()){
			modusSpezifischeSpiele = SpielListen.getNonPCSpieleIDs(modus);
		}
		for(int i=1; i<gesamtMoeglicheSpieleIDs.size(); i++){
			int aktuelleSpielID = gesamtMoeglicheSpieleIDs.get(i);
			if(!modusSpezifischeSpiele.contains(aktuelleSpielID)){
				spielCheckBox[i].setSelected(false);
				spielCheckBox[i].setEnabled(false);
			}	
			else{
				spielCheckBox[i].setEnabled(true);
			}
		}
	}
	
	public void setPCandNonPC(){
		beidesRadioButton.doClick();
	}
}