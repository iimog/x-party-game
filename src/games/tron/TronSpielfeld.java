package games.tron;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TronSpielfeld extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int colums;
	private int rows;
	private JPanel[][] flaeche;
	private Dimension feldGroesse;
	
	public TronSpielfeld(int colums, int rows){
		this(colums, rows, new Dimension(10,10));
	}

	public TronSpielfeld(int colums, int rows, Dimension feldGroesse){
		this.colums = colums;
		this.rows = rows;
		this.feldGroesse = feldGroesse;
		flaeche = new JPanel[colums][rows];
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new GridLayout(rows, colums));
		for(int i=0; i<rows; i++){
			for(int j=0; j<colums; j++){
				flaeche[j][i] = new JPanel();
				flaeche[j][i].setSize(feldGroesse);
				flaeche[j][i].setPreferredSize(feldGroesse);
				this.add(flaeche[j][i]);
			}
		}
		this.validate();
	}
	
	public void faerbeFeld(Point p, Color f){
		flaeche[p.x-1][p.y-1].setBackground(f);
	}
	public void faerbeFelder(Collection<Point> points, Color f){
		for(Point p: points)
			flaeche[p.x-1][p.y-1].setBackground(f);
	}
	
	public void showGitter(boolean gitterVisible){
		for(int i=0; i<rows; i++){
			for(int j=0; j<colums; j++){
				if(gitterVisible)
					flaeche[j][i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				if(!gitterVisible)
					flaeche[j][i].setBorder(BorderFactory.createEmptyBorder());
			}
		}
	}

	public void clear() {
		for(int i=0; i<rows; i++){
			for(int j=0; j<colums; j++){
				flaeche[j][i].setBackground(Color.LIGHT_GRAY);
			}
		}
	}
}
