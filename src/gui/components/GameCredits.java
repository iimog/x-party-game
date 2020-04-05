package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import games.Game;

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
	private GameCreditPart[] creds;
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
		for(GameCreditPart b: creds){
			if(!b.isKO() && b.getNumber()<=credits){
				b.activate();
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
		creds = new GameCreditPart[numOfRounds];
		creditLabel = new JLabel(credits+"");
		creditLabel.setHorizontalAlignment(SwingConstants.CENTER);
		creditLabel.setOpaque(true);
		creditLabel.setBackground(Color.darkGray);
		creditLabel.setForeground(actionCol);
		creditLabel.setFont(Game.STANDARD_FONT.deriveFont((float) 32.0));
		add(creditLabel);
		for (int i = (numOfRounds - 1); i >= 0; i--) {
			creds[i] = new GameCreditPart(i + 1);
			if(i < credits){
				creds[i].activate();
			}
			add(creds[i]);
		}
	}
	
	public class GameCreditPart extends JPanel {
		private static final long serialVersionUID = 1L;
		private JLabel label;
		private int number;
		private boolean ko = false;
		
		GameCreditPart(int number){
			super();
			this.number = number;
			label = new JLabel(""+number);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			setLayout(new BorderLayout());
			add(this.label, BorderLayout.CENTER);
			setOpaque(true);
			setBackground(standardCol);
			setPreferredSize(new Dimension(40, 40));
			
			label.setForeground(actionCol);
		}
		
		public void activate() {
			setBackground(actionCol);
			label.setForeground(standardCol);
		}
		
		public void deactivate() {
			setBackground(standardCol);
			label.setForeground(actionCol);
		}
		
		public int getNumber() {
			return number;
		}
		
		public void multiply(int factor) {
			number *= factor;
			label.setText(number+"");
		}
		
		public void setKO() {
			ko = true;
			label.setText("K.O.");
		}
		
		public boolean isKO() {
			return ko;
		}
	}

	public boolean isStechen() {
		return stechen;
	}

	public void ko() {
		setStechen(true);
		creds[numOfRounds - 1].setKO();
	}

	public void multiply(int factor){
		for(int i=0; i<creds.length; i++){
			creds[i].multiply(factor);
		}
	}

	public void setStechen(boolean stechen) {
		this.stechen = stechen;
	}
}
