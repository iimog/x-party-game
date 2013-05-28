package gui.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.ChangeManager;


public class Countdown extends JPanel {
	
	class Count extends Thread{
		final static int START = 0;
		final static int RESET = 1;
		final static int RESTART = 2;
		int whatToDo;
		Count(int whatToDo){
			this.whatToDo = whatToDo;
		}
		@Override
		public void run(){
			if(whatToDo == START)
				count();
			if(whatToDo == RESET)
				reSet();
			if(whatToDo == RESTART){
				reSet();
				count();
			}
				
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4550590415461760032L;
	// nur zum testen!
	public static void main(String[] args){
		JFrame f = new JFrame("Testfenster");
		Countdown c = new Countdown(5);
		f.add(c);
		f.setVisible(true);
		f.pack();
		c.count();
	}
	private int secs;
	public int getSecs() {
		return secs;
	}

	public void setSecs(int secs) {
		this.secs = secs;
		init();
	}
	private JLabel[] time;
	public boolean timeOver = false;

	private boolean stopped = false;

	private ArrayList<util.ChangeManager> cM = new ArrayList<util.ChangeManager>();

	public Countdown(){
		this(5);
	}

	public Countdown(int secs){
		this.secs = secs;
		init();
	}

	public void addChangeManager(util.ChangeManager change){
		cM.add(change);
	}

	public void count(){
		stopped = false;
		for(int i=0; i<secs; i++){
			if(stopped){
				stopped = false;
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("I wurd underbrochn");
			}
			int colorFade = getValue(i);
			time[i].setBackground(new Color(colorFade,00,00));
			repaint();
		}
		if(!stopped){
			timeOver = true;
			fireChange();
		}
	}

	public void fireChange(){
		java.util.Iterator<ChangeManager> it = cM.iterator();
		while(it.hasNext()){
			it.next().change();
		}
	}

	private void init(){
		removeAll();
		time = new JLabel[secs];
		setLayout(new GridLayout(1,secs));
		for(int i=0; i<secs; i++){
			int colorFade = getValue(i);
			time[i] = new JLabel(" ");
			time[i].setOpaque(true);
			time[i].setBackground(new Color(00,colorFade,00));
			add(time[i]);
		}
	}

	private int getValue(int i) {
		int step = 220/secs;
		return 240-i*step;
	}

	public void reset(){
		new Count(Count.RESET).start();
	}
	protected void reSet(){
		stop();
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){}

		for(int i=0; i<secs; i++){
			int colorFade = getValue(i);
			time[i].setBackground(new Color(00,colorFade,00));
		}
		timeOver = false;
	}
	public void start(){
		new Count(Count.START).start();
	}
	public void restart(){
		new Count(Count.RESTART).start();
	}
	public void stop(){
		stopped = true;
	}
}
