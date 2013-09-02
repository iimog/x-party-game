package gui.components.rotator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import util.ChangeManager;

public abstract class Rotator extends JPanel {
	private static final long serialVersionUID = 1L;
	private int rotationTime = 5000;
	ArrayList<ChangeManager> cMs = new ArrayList<ChangeManager>();
	private Timer timer;
	private boolean running = false;

	public int getRotationTime() {
		return rotationTime/1000;
	}

	public void setRotationTime(int rotationTime) {
		this.rotationTime = rotationTime*1000;
	}
	
	public void setRotationTime_ms(int rotationTime_ms) {
		this.rotationTime = rotationTime_ms;
	}

	public Rotator() {
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
	}

	public void start() {
		timer = new Timer(rotationTime, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeComponent();
			}
		});
		timer.setInitialDelay(rotationTime);
		running = true;
		timer.start();
	}

	public abstract void maskComponent();

	public abstract void changeComponent();

	public void pause() {
		if (timer != null){
			running = false;
			timer.stop();
		}
	}

	private void fireChange() {
		Iterator<ChangeManager> i = cMs.iterator();
		while (i.hasNext()) {
			i.next().change();
		}
	}

	public void addChangeManager(ChangeManager cm) {
		cMs.add(cm);
	}

	public HashSet<Integer> schonWeg = new HashSet<Integer>();

	public int nextRandom(int numOfQuests) {
		return nextRandom(numOfQuests, false);
	}
	
	public int nextRandom(int numOfQuests, boolean repeatEarly) {
		if (schonWeg.size() == numOfQuests) {
			fireChange();
			schonWeg = new HashSet<Integer>();
		}
		Random r = new Random();
		int ret = r.nextInt(numOfQuests);
		if(!repeatEarly){			
			while (schonWeg.add(ret) == false) {
				ret = r.nextInt(numOfQuests);
			}
		}
		return ret;
	}

	public boolean isRunning() {
		return running;
	}
}
