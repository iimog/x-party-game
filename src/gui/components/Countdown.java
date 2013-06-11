package gui.components;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.ChangeManager;

public class Countdown extends JPanel {
	private static final long serialVersionUID = 4550590415461760032L;

	class Counter extends Thread {
		@Override
		public void run() {
			for (int i = vergangen; i < secs; i++) {
				if (interrupted())
					return;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				countUp(i);
			}
			if (interrupted())
				return;
			timeOver();
		}
	}

	private Counter myCounter;

	private enum Zustand {
		BEREIT, PAUSE, LAUFEND, ANGEHALTEN, ABGELAUFEN
	}

	private Zustand myZustand;

	private int secs;

	public int getSecs() {
		return secs;
	}

	public void setSecs(int secs) {
		this.secs = secs;
		init();
	}

	private int vergangen = 0;
	private JLabel[] time;
	private boolean runAfterPause = false;

	private ArrayList<util.ChangeManager> cM = new ArrayList<util.ChangeManager>();

	public void addChangeManager(util.ChangeManager change) {
		cM.add(change);
	}

	public void fireChange() {
		java.util.Iterator<ChangeManager> it = cM.iterator();
		while (it.hasNext()) {
			it.next().change();
		}
	}

	public Countdown() {
		this(5);
	}

	public Countdown(int secs) {
		this.secs = secs;
		init();
	}

	private void init() {
		removeAll();
		time = new JLabel[secs];
		setLayout(new GridLayout(1, secs));
		for (int i = 0; i < secs; i++) {
			int colorFade = getColorValue(i);
			time[i] = new JLabel(" ");
			time[i].setOpaque(true);
			time[i].setBackground(new Color(00, colorFade, 00));
			add(time[i]);
		}
		myZustand = Zustand.BEREIT;
	}

	private int getColorValue(int i) {
		int step = 220 / secs;
		return 240 - i * step;
	}

	public synchronized void start() {
		if (myZustand == Zustand.BEREIT) {
			myCounter = new Counter();
			myZustand = Zustand.LAUFEND;
			myCounter.start();
		} else if (myZustand == Zustand.PAUSE) {
			runAfterPause = true;
		}
	}

	public synchronized void stop() {
		if (myZustand == Zustand.LAUFEND) {
			myZustand = Zustand.ANGEHALTEN;
			myCounter.interrupt();
			reSet();
		} else if (myZustand == Zustand.PAUSE) {
			myZustand = Zustand.ANGEHALTEN;
			reSet();
		}
	}

	public synchronized void pause() {
		if (myZustand == Zustand.BEREIT) {
			myZustand = Zustand.PAUSE;
			runAfterPause = false;
		} else if (myZustand == Zustand.LAUFEND) {
			myZustand = Zustand.PAUSE;
			runAfterPause = true;
			myCounter.interrupt();
		}
	}

	public synchronized void resume() {
		if (myZustand != Zustand.PAUSE)
			return;
		myZustand = Zustand.BEREIT;
		if(runAfterPause){
			start();
		}
	}

	private synchronized void countUp(int i) {
		int colorFade = getColorValue(i);
		time[i].setBackground(new Color(colorFade, 00, 00));
		repaint();
		vergangen++;
	}

	private void timeOver() {
		myZustand = Zustand.ABGELAUFEN;
		fireChange();
		reSet();
	}

	private synchronized void reSet() {
		for (int i = 0; i < secs; i++) {
			int colorFade = getColorValue(i);
			time[i].setBackground(new Color(00, colorFade, 00));
		}
		runAfterPause = false;
		myZustand = Zustand.BEREIT;
		vergangen = 0;
	}

}
