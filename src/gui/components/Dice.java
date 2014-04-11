package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import start.X;
import util.ChangeManager;


public class Dice extends JButtonIcon {
	private static final long serialVersionUID = -7924177327720133127L;
	public static void main(String[] args){
		JFrame fenster = new JFrame();
		fenster.setLayout(new GridLayout(2,2));
		fenster.add(new Dice(Color.GRAY, 1));
		//fenster.add(new Dice(Color.CYAN, 2));
		//fenster.add(new Dice(Color.ORANGE, 3));
		//fenster.add(new Dice(Color.PINK, 0));
		fenster.setVisible(true);
		fenster.pack();
		fenster.setLocationRelativeTo(null);
		fenster.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}
	private Map<Integer, String> numberToIcon;
	private int zahl;
	public int getZahl() {
		return zahl;
	}
	public void setZahl(int zahl) {
		this.zahl = zahl;
		changeIcon(numberToIcon.get(zahl) ,zahl+"");
	}
	public Color farbe;
	public int a; // speed coefficient
	boolean rolls = false;
	boolean selbstausloeser = true;
	boolean disabled = false;
	private ArrayList<ChangeManager> myCM = new ArrayList<ChangeManager>();
	public Dice(){
		this(Color.BLUE, 2);
	}
	public Dice(Color farbe){
		this(farbe, 2);
	}
	public Dice(Color farbe, int speed){
		super(X.getMainDir()+"/media/ablauf/dice/dice0.png", "0");
		//setText("0");
		this.farbe = farbe;
		a = speed;
		setBackground(farbe);
		setFont(new Font("SergeoUI",1,30));
		addActionListener(new ActionListener(){public void actionPerformed(ActionEvent evt){if(selbstausloeser)rollTheDice();}});
		setPreferredSize(new Dimension(200,200));
		initNumberToIcon();
	}
	private void initNumberToIcon() {
		numberToIcon = new HashMap<Integer, String>();
		numberToIcon.put(0, "/media/ablauf/dice/dice0.png");
		numberToIcon.put(1, "/media/ablauf/dice/dice1.png");
		numberToIcon.put(2, "/media/ablauf/dice/dice2.png");
		numberToIcon.put(3, "/media/ablauf/dice/dice3.png");
		numberToIcon.put(4, "/media/ablauf/dice/dice4.png");
		numberToIcon.put(5, "/media/ablauf/dice/dice5.png");
		numberToIcon.put(6, "/media/ablauf/dice/dice6.png");
	}
	public void addChangeManager(ChangeManager cm){
		myCM.add(cm);
	}
	public void disable(boolean d){
		disabled = d;
		setEnabled(!d);
	}
	private void informCM(){
		Iterator<ChangeManager> it = myCM.iterator();
		while(it.hasNext()){
			it.next().change();
		}
	}
	public boolean isDisabled(){
		return disabled;
	}
	public boolean rolls(){
		return rolls;
	}
	public void rollTheDice(){
		if(rolls)return;
		if(disabled)return;
		rolls = true;
		Thread t = new Thread(){@Override
			public void run(){
			Random r = new Random();
			for(int i=1; i<=20; i++){
				zahl = r.nextInt(6)+1;
				changeIcon(numberToIcon.get(zahl) ,zahl+"");
				try {
					Thread.sleep(a*50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			rolls = false;
			informCM();
		}};
		t.start();
	}
	public void setSelbstausloeser(boolean sa){
		selbstausloeser = sa;
	}
}
