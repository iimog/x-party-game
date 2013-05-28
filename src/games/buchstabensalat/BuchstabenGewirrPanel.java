package games.buchstabensalat;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BuchstabenGewirrPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	BuchstabenSalat salat;

	private GridLayout myLayout;

	private int[] scrambledPositions;

	private JLabel[] lables;
	
	public BuchstabenGewirrPanel(BuchstabenSalat salat){
		this.salat = salat;
		initGUI();
	}

	private void initGUI() {
		myLayout = new GridLayout(1,1);
		this.setLayout(myLayout);
		add(new JLabel());
	}

	public void setWord(String wort) {
		removeAll();
		myLayout.setColumns(wort.length());
		scrambledPositions = scramble(wort.length());
		lables = new JLabel[wort.length()];
		for(int i=0; i<wort.length(); i++){
			lables[i] = new JLabel();
			lables[i].setHorizontalAlignment(JLabel.CENTER);
			lables[i].setText(wort.substring(scrambledPositions[i],scrambledPositions[i]+1));
			lables[i].setFont(BuchstabenSalat.standardFont);
			add(lables[i]);
		}
		revalidate();
		repaint();
	}

	private int[] scramble(int length) {
		int[] scramble = new int[length];
		HashSet<Integer> vergebenePositionen = new HashSet<Integer>();
		Random r = new Random();
		for(int i=0; i<length; i++){
			int index;
			do{
				index = r.nextInt(length);
			}while(!vergebenePositionen.add(index));
			scramble[i] = index;
		}
		return scramble;
	}

	public void hideLetterAt(int position) {
		lables[position].setForeground(lables[position].getBackground());
	}

	public String getLetterAt(int position) {
		return lables[position].getText();
	}

	public int getPositionOf(int position) {
		return scrambledPositions[position];
	}

	public String getScatteredWord() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<lables.length; i++){
			sb.append(lables[i].getText());
		}
		return sb.toString();
	}

}
