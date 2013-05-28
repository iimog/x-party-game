package games.ghosts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

public class Spielfeld extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3695262990721317704L;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	Color choseColor;
	Point chosePoint = new Point();
	ArrayList<Point> badGhosts = new ArrayList<Point>();
	boolean chosen = false;
	Geist[][] geister;
	Ghosts game;
	public Spielfeld(Ghosts spiel){
		game = spiel;
		this.geister = spiel.geister;
		setPreferredSize(new Dimension(600,600));
		setMinimumSize(new Dimension(600,600));
	}
	public void fieldChosen(Point p, Color c){
		if(p.x>600||p.x<0||p.y>600||p.y<0)return;
		choseColor = c;
		chosePoint = p;
		chosen = true;
		this.repaint();
	}
	public void isBad(Point p){
		badGhosts.add(p);
		this.repaint();
	}
	public void isNotBad(Point p){
		badGhosts.remove(p);
		this.repaint();
	}
	@Override
	protected void paintComponent(Graphics g){
		g.clearRect(0, 0, 600, 600);
		g.setColor(Color.gray);
		for(int i=0; i<3; i++){
			for(int j=0; j<6; j++){
				if(j%2==0){
					g.fillRect(200*i, 100*j, 100, 100);
				}
				else{
					g.fillRect(200*i+100, 100*j, 100, 100);
				}
			}
		}
		for(Iterator<Point> it = badGhosts.iterator(); it.hasNext(); ){
			g.setColor(game.myPlayer[game.whosTurn].farbe);
			Point current = it.next();
			g.fillRect(current.x*100, current.y*100,100,100);
		}
		if(chosen){
			g.setColor(choseColor);
			g.fillRect(chosePoint.x*100, chosePoint.y*100, 100, 100);
			// chosen = false;
		}
		for(int i=0; i<2; i++){
			for(int j=0; j<8; j++){
				Geist goo = geister[i][j];
				// TODO Images können nicht geöffnet werden
				g.drawImage(goo.img.getImage(), goo.position.x*100+6, goo.position.y*100, this);
			}
		}
	}

}
