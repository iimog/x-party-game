package gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

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
		JPanel mainPanel = new JPanel(new GridLayout(1, 3));
		fenster.add(mainPanel);
		GameCredits gc = new GameCredits();
		GameCredits gc2 = new GameCredits(50, Color.BLUE);
		GameCredits gc3 = new GameCredits(5000, Color.RED);
		gc2.setLabelDistance(10);
		gc3.setLabelDistance(500);
		gc2.earnsCredit(23);
		gc3.earnsCredit(120);
		mainPanel.add(gc);
		mainPanel.add(gc2);
		mainPanel.add(gc3);
		fenster.pack();
		fenster.setVisible(true);
		fenster.setLocationRelativeTo(null);
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Thread.sleep(1000);
		gc.earnsCredit(1);
		gc2.earnsCredit(1);
		gc3.earnsCredit(1200);
		Thread.sleep(1000);
		gc.earnsCredit(1);
		gc2.earnsCredit(13);
		gc3.earnsCredit(250);
		Thread.sleep(1000);
		gc.earnsCredit(1);
		gc2.earnsCredit(9);
		gc3.earnsCredit(120);
		gc.ko();
	}

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
	private int labelDistance = 1;

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
		creditLabel.setText(credits + "");
		repaint();
	}

	public int hasMoreRounds() {
		return (numOfRounds - credits);
	}

	private void init() {
		removeAll();
		setOpaque(false);
		setLayout(new BorderLayout());
		creditLabel = new JLabel(credits + "");
		creditLabel.setHorizontalAlignment(SwingConstants.CENTER);
		creditLabel.setOpaque(true);
		creditLabel.setBackground(Color.darkGray);
		creditLabel.setForeground(actionCol);
		creditLabel.setFont(Game.STANDARD_FONT.deriveFont((float) 32.0));
		add(creditLabel, BorderLayout.NORTH);
		GameCreditPanel credsPanel = new GameCreditPanel();
		credsPanel.setPreferredSize(new Dimension(40, 4000));
		// credsPanel.setLayout(new GridLayout(numOfRounds, 1));
		add(credsPanel, BorderLayout.CENTER);
	}

	public class GameCreditPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public GameCreditPanel() {

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			double fac = (double) credits / (double) numOfRounds;
			int height = (int) Math.round(fac * this.getHeight());
			g.setColor(standardCol);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(actionCol);
			g.fillRect(0, this.getHeight() - height, this.getWidth(), this.getHeight());
			double oneUnit = (double) this.getHeight() / (double) numOfRounds;
			for (int i = 1; i <= numOfRounds; i++) {
				if (i % labelDistance == 0) {
					int lineHeight = this.getHeight() - (int) Math.round(oneUnit * (i));
					g.setColor(Color.DARK_GRAY);
					g.drawLine(0, lineHeight, this.getWidth(), lineHeight);
					g.setColor(i <= credits ? standardCol : actionCol);
					int bumpDown = (int) Math.round(oneUnit * .5);
					if (bumpDown < 8) {
						bumpDown = 8;
					}
					String label = String.valueOf(i);
					if (stechen) {
						label = "K.O.";
					}
					drawCenteredString(g, label, new Point(this.getWidth() / 2, lineHeight + bumpDown));
				}
			}
		}

		/**
		 * Draw a String centered in the middle of a Rectangle.
		 *
		 * @param g      The Graphics instance.
		 * @param text   The String to draw.
		 * @param center The Point to center the text around.
		 * 
		 *               inspired from https://stackoverflow.com/a/27740330
		 */
		public void drawCenteredString(Graphics g, String text, Point center) {
			// Get the FontMetrics
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			// Determine the X coordinate for the text
			int x = center.x - (metrics.stringWidth(text) / 2);
			// Determine the Y coordinate for the text (note we add the ascent, as in java
			// 2d 0 is top of the screen)
			int y = center.y - (metrics.getHeight() / 2) + metrics.getAscent();
			// Draw the String
			g.drawString(text, x, y);
		}
	}

	public boolean isStechen() {
		return stechen;
	}

	public void ko() {
		setStechen(true);
	}

	public void setLabelDistance(int labelDistance) {
		this.labelDistance = labelDistance;
	}

	public void setStechen(boolean stechen) {
		this.stechen = stechen;
	}
}
