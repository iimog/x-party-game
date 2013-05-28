package gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;

import util.ChangeManager;


public class Dice extends JButton {
	/**
	 * 
	 */
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
	public int zahl;
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
		setText("0");
		this.farbe = farbe;
		a = speed;
		setBackground(farbe);
		setFont(new Font("SergeoUI",1,30));
		addActionListener(new ActionListener(){public void actionPerformed(ActionEvent evt){if(selbstausloeser)rollTheDice();}});
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
				setText(zahl+"");
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
