package games.wurst;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WurstStatusAnzeigePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	WurstPanel wurstPanel;
	
	public WurstStatusAnzeigePanel(WurstPanel wurstPanel){
		this.wurstPanel = wurstPanel;
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double fac = (double)wurstPanel.rest/(double)wurstPanel.anfangsWert;
		int width = (int)Math.round(fac*this.getWidth());
		g.setColor(wurstPanel.player.farbe.brighter().brighter());
		g.fillRect(0, 0, width, this.getHeight());
		g.setColor(wurstPanel.player.farbe);
		g.drawRect(0, 0, width, this.getHeight());
		fac = (double)(wurstPanel.rest-wurstPanel.getAbgabe())/(double)wurstPanel.anfangsWert;
		width = (int)Math.round(fac*this.getWidth());
		g.fillRect(0, 0, width, this.getHeight());
	}
}