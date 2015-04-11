package games.ghosts;

import gui.components.Bildschirm;

import java.awt.FlowLayout;

import javax.swing.JPanel;


public class TotenAnzeige extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6384790240902944554L;
	public static final int GOOD = 0;
	public static final int BAD = 1;
	public int status;
	private Bildschirm[] flaeche = new Bildschirm[4];
	private String[] icon = new String[2];{
		icon[0] = "/media/ghosts/littleEngel.png";
		icon[1] = "/media/ghosts/littleTeufel.png";
	}
	private int gone = 0;

	public TotenAnzeige(int good){
		if(good!=GOOD && good!=BAD)return;
		// setLayout(new GridLayout(1,4));
		setLayout(new FlowLayout());
		status = good;
		for(int i = 0; i<4; i++){
			flaeche[i] = new Bildschirm(icon[status]);
			add(flaeche[i]);
		}
	}

	public boolean loose(){
		if(gone>=4)return false;
		else{
			flaeche[gone].hidePic(true);
			gone++;
			repaint();
			return true;
		}
	}

	public void reset(){
		for(int i = 0; i<4; i++){
			flaeche[i].changePic(icon[status]);
			gone = 0;
			repaint();
		}
	}
}
