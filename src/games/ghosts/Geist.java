package games.ghosts;

import gui.AnzeigeDialog;
import gui.EasyDialog;
import gui.components.Bildschirm;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class Geist {

	public static int[] livingGood = new int[2];
	public static int[] livingBad = new int[2];

	public static void reset(){
		livingBad[0]=0;
		livingBad[1]=0;
		livingGood[0]=0;
		livingGood[1]=0;
	}
	private boolean bad;
	public boolean dead = false;
	public String tipp;
	public ImageIcon img;
	public Point position;
	public int team;

	public boolean activated = false;
	public Geist(int whichTeam, Point pos){
		team = whichTeam;
		livingGood[whichTeam]++;
		if(team==0){
			URL bildURL = getClass().getResource("/media/ghosts/geist3.png");
			Image image = Toolkit.getDefaultToolkit().getImage(bildURL);
			img = new ImageIcon(image);
		}
		if(team==1){
			URL bildURL = getClass().getResource("/media/ghosts/geist4.png");
			Image image = Toolkit.getDefaultToolkit().getImage(bildURL);
			img = new ImageIcon(image);
		}
		position = pos;
	}
	public AnzeigeDialog die(){
		dead = true;
		if(bad)livingBad[team]--;
		if(!bad)livingGood[team]--;
		position = new Point(-1,-1);
		Bildschirm b;
		String boese;
		if(bad){
			b = new Bildschirm("/media/ghosts/Teufel.png");
			boese = "BÖSE";
		}
		else{
			b = new Bildschirm("/media/ghosts/Engel.png");
			boese = "GUT";
		}
		return new EasyDialog("Dieser Geist ist... "+boese,b);
	}

	public boolean isBad(){
		return bad;
	}

	public void setBad(boolean bad){
		if(this.bad == bad || dead)return;
		this.bad = bad;
		if(bad){
			livingGood[team]--; livingBad[team]++;
		}
		else{
			livingGood[team]++; livingBad[team]--;
		}
	}
}
