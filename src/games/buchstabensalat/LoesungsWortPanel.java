package games.buchstabensalat;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoesungsWortPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	BuchstabenSalat salat;

	private GridLayout myLayout;

	private JLabel[] lables;
	
	public LoesungsWortPanel(BuchstabenSalat salat){
		this.salat = salat;
		initGUI();
	}

	private void initGUI() {
		myLayout = new GridLayout(1,1);
		this.setLayout(myLayout);
		this.setOpaque(false);
		add(new JLabel());
	}

	public void setUnsichtbarWord(String string) {
		removeAll();
		myLayout.setColumns(string.length());
		lables = new JLabel[string.length()];
		for(int i=0; i<string.length(); i++){
			lables[i] = new JLabel();
			lables[i].setHorizontalAlignment(JLabel.CENTER);
			lables[i].setText(" ");
			lables[i].setFont(BuchstabenSalat.standardFont);
			lables[i].setForeground(Color.WHITE);
			add(lables[i]);
		}
		revalidate();
		repaint();
	}

	public void setLetterAt(int position, String letter) {
		lables[position].setText(letter);
	}

	public String getLetterAt(int position) {
		return lables[position].getText();
	}
}
