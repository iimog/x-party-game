package gui.components;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import start.X;

@SuppressWarnings("serial")
public class Bildschirm extends JPanel {
	public Image bild;
	public JLabel fehlerText;
	boolean drawCircle = false;
	boolean drawDot = false;
	boolean drawDots = false;
	boolean drawEpi = false;
	boolean drawPic = true;
	private int circleX, circleY, circleRadius;
	private int dotX, dotY, dotRadius = 5;
	private int dotRadius2 = 5;
	private int epiX, epiY, epiNum;
	private Color dotColor;
	private int xComp=0;
	private int yComp=0;
	private String bName;
	private boolean center=false;
	private ArrayList<Point> dots = new ArrayList<Point>();
	private ArrayList<Color> dotColors = new ArrayList<Color>();

	public Bildschirm(String bildname){
		bName = bildname;
		setLayout(new BorderLayout());
		fehlerText = new JLabel("");
		add(fehlerText,SwingConstants.CENTER);
		fehlerText.setHorizontalAlignment(SwingConstants.CENTER);
		fehlerText.setForeground(Color.RED);
		fehlerText.setFont(new Font("SergeoUI",1,20));
		setPic(bildname, true);
	}
	public void centerMe(boolean center){
		this.center = center;
		repaint();
	}
	public void changePic(String bildname){
		drawPic=true;
		xComp=0;yComp=0;
		setPic(bildname, true);
		repaint();
	}
	public void drawCircle(int x, int y, int radius){
		circleX = x-radius;
		circleY = y-radius;
		circleRadius = 2*radius;
		drawCircle = true;
		repaint();
	}
	public void drawDot(int x, int y, Color col){
		dotX = x-dotRadius;
		dotY = y-dotRadius;
		dotColor = col;
		drawDot = true;
		repaint();
	}
	
	public void addDot(int x, int y, Color farbe) {
		dots.add(new Point(x-dotRadius2,y-dotRadius2));
		dotColors.add(farbe);
		drawDots = true;
		repaint();
	}

	public void drawEpi(int x, int y, int num){
		epiX = x; epiY = y; epiNum = num;
		drawEpi = true;
		repaint();
	}
	public void hidePic(boolean hide){
		drawPic = !hide;
		repaint();
	}
	private void noPicFound(){
		setPreferredSize(new Dimension(400,300));
		fehlerText.setText("Bild wurde nicht gefunden");
		bild = null;
		bName = "";
	}
	@Override
	protected void paintComponent(Graphics g){
		if(!drawPic){
			g.clearRect(0, 0, getSize().width, getSize().height);
			return;
		}
		if(center){		// Evtl. Probleme mit showPicPart
			xComp=getSize().width/2-bild.getWidth(this)/2;
			yComp=getSize().height/2-bild.getHeight(this)/2;
		}
		g.drawImage(bild, xComp, yComp, this);
		if(drawDot){
			g.setColor(dotColor);
			g.fillOval(dotX, dotY, 2*dotRadius, 2*dotRadius);
		}
		if(drawDots){
			for(int i=0; i<dots.size(); i++){
				Point p = dots.get(i);
				g.setColor(dotColors.get(i));
				g.fillOval(p.x, p.y, 2*dotRadius2, 2*dotRadius2);
			}
		}
		if(drawEpi){
			Graphics2D g2d = (Graphics2D) g;
			BasicStroke stroke1
			= new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER);
			g2d.setStroke(stroke1);
			g2d.setColor(Color.BLUE);
			g2d.drawOval(epiX-1, epiY-1, 2, 2);
			for(int i=1; i<epiNum; i++){
				g2d.drawOval(epiX-15*i, epiY-15*i, 30*i, 30*i);
			}
		}
		if(drawCircle){
			Graphics2D g2d = (Graphics2D) g;
			BasicStroke stroke1
			= new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER);
			g2d.setStroke(stroke1);

			g2d.setColor(Color.RED);
			g2d.drawOval(circleX, circleY, circleRadius, circleRadius);
		}
	}
	private void setPic(String bildname, boolean autoSize){
		bild = getToolkit().getImage(X.getMainDir()+bildname);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(bild, 0);
		try{
			mt.waitForAll();
		}
		catch (InterruptedException e){
			// nichts
		}
		if(autoSize){
			setPreferredSize(new Dimension(bild.getWidth(this),bild.getHeight(this)));
		}
		fehlerText.setText("");
		if(bild==null){noPicFound();}
	}
	public void showPicPart(String bildname, Point mittelpunkt){
		drawPic=true;
		if(!bName.equals(bildname)){
			setPic(bildname, false);
		}
		xComp=getPreferredSize().width/2-mittelpunkt.x;
		yComp=getPreferredSize().height/2-mittelpunkt.y;

		repaint();
	}
	
}
