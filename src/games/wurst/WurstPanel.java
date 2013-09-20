package games.wurst;

import gui.EasyDialog;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import player.Player;
import start.X;
import util.ChangeManager;

public class WurstPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Player player;
	int anfangsWert;
	int rest;
	private int abgabe;
	public int getAbgabe() {
		return abgabe;
	}

	private boolean aktiv = true;
	private boolean offen = false;
	private Wurst wurst;
	
	private ArrayList<ChangeManager> changeManagers = new ArrayList<ChangeManager>();
	public void addChangeManager(ChangeManager cm){
		changeManagers.add(cm);
	}
	private void fireChange(){
		for(ChangeManager cm: changeManagers){
			cm.change();
		}
	}

	private JPanel hauptbereichPanel;

	private JLabel playerLabel;

	private JPanel restPanel;

	private JLabel restTextLabel;

	private JLabel restZahlLabel;

	private WurstStatusAnzeigePanel restAnzeige;

	private JPanel abgebenPanel;

	private JLabel abgebenLabel;

	private JTextField abgebenTextField;

	private JButton fertigButton;

	private JPanel abdeckPanel;

	private JLabel abdeckLabel;
	
	public WurstPanel(Wurst wurst, Player player){
		this.player = player;
		this.anfangsWert = wurst.anfangsWert;
		this.wurst = wurst;
		this.setOpaque(false);
		rest = anfangsWert;
		if(player.isRobot()){
			abgabe = berechneAbgabe();
			rest-=abgabe;
			aktiv = false;
		}
		initGUI();
	}

	int rundenZaehler = 0;
	private int berechneAbgabe() {
		if(rest == 0)return 0;
		int abg;
		do{
			abg = (int)Math.round((double)rest/(double)(wurst.numOfRounds*wurst.spielerZahl-(rundenZaehler++)));
			int zufall = (new Random().nextInt(abg*2)) - abg;
			abg+=zufall;
			boolean highbet = new Random().nextInt(100) > 74;
			if(highbet)abg*=2;
		} while (abg>rest || abg<0);
		return abg;
	}
	private void initGUI() {
		JPanel abdeckung = getAbdeckung();
		this.add(abdeckung);
	}

	private JPanel getAbdeckung() {
		if(abdeckPanel == null){
			abdeckPanel = new JPanel();
			abdeckPanel.setLayout(new BorderLayout());
			abdeckPanel.setBackground(Color.DARK_GRAY);
			abdeckPanel.setMinimumSize(new Dimension(100,50));
			abdeckLabel = new JLabel(player.getName());
			if(player.isRobot())abdeckLabel.setText(abdeckLabel.getText()+" (Fertig)");
			abdeckLabel.setForeground(player.farbe);
			abdeckLabel.setHorizontalAlignment(JLabel.CENTER);
			abdeckLabel.setFont(X.BUTTON_FONT);
			abdeckPanel.add(abdeckLabel);
			abdeckPanel.addMouseListener(new MouseAdapter() {				
				@Override
				public void mouseClicked(MouseEvent e) {
					if(aktiv && !wurst.schonEinsOffen){
						showHauptbereich();
						offen = true;
						fireChange();
					}
				}
			});
		}
		return abdeckPanel;
	}

	protected void showHauptbereich() {
		this.removeAll();
		JPanel hauptbereich = getHauptbereich();
		this.add(hauptbereich);
		this.revalidate();
		this.repaint();
	}

	private JPanel getHauptbereich() {
		if(hauptbereichPanel == null){
			hauptbereichPanel = new JPanel();
			hauptbereichPanel.setLayout(new GridLayout(5,1));
			hauptbereichPanel.setBackground(Color.DARK_GRAY);
			playerLabel = new JLabel(player.name);
			playerLabel.setForeground(player.farbe);
			playerLabel.setHorizontalAlignment(JLabel.CENTER);
			playerLabel.setFont(X.BUTTON_FONT);
			hauptbereichPanel.add(playerLabel);
			restPanel = new JPanel();
			restPanel.setOpaque(false);
			restPanel.setLayout(new GridLayout(1,2));
			hauptbereichPanel.add(restPanel);
			restTextLabel = new JLabel("Rest: ");
			restTextLabel.setFont(X.BUTTON_FONT);
			restTextLabel.setForeground(Color.WHITE);
			restPanel.add(restTextLabel);
			restZahlLabel = new JLabel(rest+"g");
			restZahlLabel.setFont(X.BUTTON_FONT);
			restZahlLabel.setForeground(Color.WHITE);
			restPanel.add(restZahlLabel);
			restAnzeige = new WurstStatusAnzeigePanel(this);
			hauptbereichPanel.add(restAnzeige);
			abgebenPanel = new JPanel();
			abgebenPanel.setOpaque(false);
			abgebenPanel.setLayout(new GridLayout(1,2));
			hauptbereichPanel.add(abgebenPanel);
			abgebenLabel = new JLabel("Abgeben: ");
			abgebenLabel.setFont(X.BUTTON_FONT);
			abgebenLabel.setForeground(Color.WHITE);
			abgebenPanel.add(abgebenLabel);
			abgebenTextField = new JTextField("0");
			abgebenTextField.setBackground(Color.DARK_GRAY);
			abgebenTextField.setFont(X.BUTTON_FONT);
			abgebenTextField.setForeground(Color.WHITE);
			abgebenTextField.addKeyListener(new KeyAdapter() {				
				@Override
				public void keyReleased(KeyEvent e) {
					textFieldActionPerformed();
				}
			});
			abgebenPanel.add(abgebenTextField);
			fertigButton = new DefaultButton("Fertig");
			fertigButton.setFont(X.BUTTON_FONT);
			fertigButton.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					fertigButtonActionPerformed();
				}
			});
			hauptbereichPanel.add(fertigButton);
		}
		return hauptbereichPanel;
	}

	protected void textFieldActionPerformed() {
		String abg = abgebenTextField.getText();
		try{
			abgabe = Integer.parseInt(abg);
			if(abgabe>rest || abgabe<0){
				throw new Exception("Unerlaubter Wert");
			}
		}
		catch(Exception e){
			abgabe = 0;
		}
		restAnzeige.repaint();
	}
	protected void fertigButtonActionPerformed() {
		String abg = abgebenTextField.getText();
		try{
			abgabe = Integer.parseInt(abg);
			if(abgabe>rest || abgabe<0){
				throw new Exception("Unerlaubter Wert");
			}
			rest -= abgabe;
			abdeckLabel.setText(abdeckLabel.getText()+" (Fertig)");
			aktiv = false;
			offen = false;
			showAbdeckung();
			fireChange();
		}
		catch(Exception e){
			EasyDialog.showMessage("Bitte ins Textfeld eine gültige Zahl eingeben.");
			abgabe = 0;
		}
	}

	private void showAbdeckung() {
		this.removeAll();
		JPanel abdeckung = getAbdeckung();
		this.add(abdeckung);
		this.revalidate();
		this.repaint();
	}
	public boolean isOffen() {
		return offen;
	}
	public boolean isAktiv() {
		return aktiv;
	}
	public void neueRunde() {
		if(player.isRobot()){
			abgabe = berechneAbgabe();
			rest-=abgabe;
		}
		else{
			abdeckLabel.setText(player.name);
			restZahlLabel.setText(rest+"g");
			aktiv = true;
			abgebenTextField.setText("0");
			abgabe = 0;
		}
		
	}
	public void setAnfangsWert(int anfangsWert) {
		this.anfangsWert = anfangsWert;
		if(restZahlLabel != null)restZahlLabel.setText(anfangsWert+"g");
		rest = anfangsWert;
		if(player.isRobot()){
			abgabe = berechneAbgabe();
			rest-=abgabe;
		}
	}

}
