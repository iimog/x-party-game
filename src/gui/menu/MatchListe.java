package gui.menu;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import start.X;

public class MatchListe extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel hauptPanel = new JPanel();
	private MatchInfoPanel[] infoPanels;
	private int selectedPanel = -1;
	
	public MatchListe(MatchInfoPanel[] infoPanels){
		this.infoPanels = infoPanels;
		initGUI();
	}

	private class PanelMouseAdapter extends MouseAdapter{
		int nextPanel;
		public PanelMouseAdapter(int nextPanel){
			this.nextPanel = nextPanel;
		}
		public void mouseReleased(MouseEvent e){
			changeSelectedPanelTo(nextPanel);
		}
	}
	
	private void initGUI() {
		this.setBackground(Color.DARK_GRAY);
		hauptPanel = new JPanel();
		hauptPanel.setLayout(new GridLayout(infoPanels.length,1,5,15));
		hauptPanel.setOpaque(false);
		for(int i=0; i<infoPanels.length; i++){
			hauptPanel.add(infoPanels[i]);
			infoPanels[i].addMouseListener(new PanelMouseAdapter(i));
		}
		if(infoPanels.length==0){
			hauptPanel.setLayout(new GridLayout(1,1));
			JLabel keineSpielstaendeLabel = new JLabel("Keine gespeicherten Spielstände vorhanden!");
			keineSpielstaendeLabel.setFont(X.buttonFont);
			hauptPanel.add(keineSpielstaendeLabel);
		}
		this.add(hauptPanel);
	}

	protected void changeSelectedPanelTo(int i) {
		if(selectedPanel != -1 && selectedPanel<infoPanels.length){
			infoPanels[selectedPanel].setBorder(null);
		}
		selectedPanel = i;
		infoPanels[selectedPanel].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	public File getSelectedFile() {
		if(selectedPanel == -1)return null;
		else return infoPanels[selectedPanel].getMatchInfo().getFile();
	}

	public void refresh(MatchInfoPanel[] infoPanels) {
		this.infoPanels = infoPanels;
		this.removeAll();
		initGUI();
		revalidate();
		repaint();
	}
}
