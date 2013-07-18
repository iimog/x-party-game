package gui.components;

import games.Game;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameCredits extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7093227368252264203L;
	public static void main(String[] args) throws InterruptedException { // Nur
		// zum
		// Testen
		JFrame fenster = new JFrame("Testfenster");
		GameCredits gc = new GameCredits();
		fenster.add(gc);
		fenster.pack();
		fenster.setVisible(true);
		fenster.setLocationRelativeTo(null);
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Thread.sleep(1000);
		gc.earnsCredit(1);
		Thread.sleep(1000);
		gc.earnsCredit(1);
		Thread.sleep(1000);
		gc.earnsCredit(1);
	}
	private JButton[] creds;
	private int numOfRounds; // Anzahl der Gewinnsätzen
	public int getNumOfRounds() {
		return numOfRounds;
	}

	public void setNumOfRounds(int numOfRounds) {
		this.numOfRounds = numOfRounds;
		init();
	}
	private Color standardCol; // Standardhintergrundfarbe
	private Color actionCol; // Standardfarbe für erreichte Punkte
	private int credits = 0; // Schon gemachte Punkte
	private boolean stechen;

	private JLabel creditLabel;

	public GameCredits() {
		this(5, Color.ORANGE, Color.BLACK);
	}

	public GameCredits(int numOfRounds) {
		this(numOfRounds, Color.ORANGE, Color.BLACK);
	}

	public GameCredits(int numOfRounds, Color actionCol) {
		this(numOfRounds, actionCol, Color.BLACK);
	}

	public GameCredits(int numOfRounds, Color actionCol, Color standardCol) {
		this.numOfRounds = numOfRounds;
		this.actionCol = actionCol;
		this.standardCol = standardCol;
		init();
	}

	public void earnsCredit(int credit) {
		this.credits += credit;
		for(JButton b: creds){
			if(!b.getText().equals("K.O.") && Integer.parseInt(b.getText())<=credits){
				b.setBackground(actionCol);
			}
		}
		creditLabel.setText(credits+"");
		repaint();
	}

	public int hasMoreRounds() {
		return (numOfRounds - credits);
	}

	private void init() {
		removeAll();
		setOpaque(false);
		setLayout(new GridLayout(numOfRounds+1, 1));
		creds = new JButton[numOfRounds];
		creditLabel = new JLabel(credits+"");
		creditLabel.setHorizontalAlignment(SwingConstants.CENTER);
		creditLabel.setOpaque(true);
		creditLabel.setBackground(Color.darkGray);
		creditLabel.setForeground(actionCol);
		creditLabel.setFont(Game.STANDARD_FONT);
		add(creditLabel);
		for (int i = (numOfRounds - 1); i >= 0; i--) {
			creds[i] = new JButton("" + (i + 1));
			creds[i].setEnabled(false);
			creds[i].setBackground(standardCol);
			if(i < credits){
				creds[i].setBackground(actionCol);
			}
			add(creds[i]);
		}
	}

	public boolean isStechen() {
		return stechen;
	}

	public void ko() {
		setStechen(true);
		creds[numOfRounds - 1].setBackground(Color.BLACK);
		creds[numOfRounds - 1].setText("K.O.");
	}

	public void multiply(int factor){
		for(int i=0; i<creds.length; i++){
			creds[i].setText(Integer.parseInt(creds[i].getText())*factor+"");
		}
	}

	public void setStechen(boolean stechen) {
		this.stechen = stechen;
	}
}
