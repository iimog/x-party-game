package games.tron;

public class TronGameLoop extends Thread {
	private Tron tron;
	private boolean isOver;
	private boolean pause = false;
	private Thread currentLoopCore;
	public TronGameLoop(Tron tron){
		this.tron = tron;
	}
	
	@Override
	public void run() {
		try {
			tron.startButton.setText("5");
			Thread.sleep(1000);
			tron.startButton.setText("4");
			Thread.sleep(1000);
			tron.startButton.setText("3");
			Thread.sleep(1000);
			tron.startButton.setText("2");
			Thread.sleep(1000);
			tron.startButton.setText("1");
			Thread.sleep(1000);
			tron.startButton.setText("Start");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		normalGameLoop();
	}
	
	private void normalGameLoop(){
		getLoopCore().start();
	}
	Thread getLoopCore(){
		Thread loopCore = new Thread(){
		public void run(){
			while(!isInterrupted() && !getPause()){
				tron.spielfeld.requestFocus();
				tron.moveSchlangen();
				tron.pruefeKollision();
				int restSchlangen = 0;
				for(int i=0; i<tron.spielerZahl; i++){
					if(!tron.kollision[i])restSchlangen++;
				}
				if(restSchlangen<=1){
					isOver = true;
					break;
				}
				tron.maleNeuenKopf();
				try {
					Thread.sleep(tron.sleepTime);
				} catch (InterruptedException e) {
					break;
				}
			}
			if(isOver){
				tron.endOfRound();
			}
		}
	};
	currentLoopCore = loopCore;
	return loopCore;
	}
	public void togglePause(){
		boolean currentState = getPause();
		setPause(!currentState);
		if(currentState == true){	// es war gerade Pause
			normalGameLoop();
		}
	}
	public void pause(){
		setPause(true);
	}
	public void interrupt(){
		super.interrupt();
		currentLoopCore.interrupt();
	}
	synchronized void setPause(boolean p){
		pause = p;
		tron.setPaused(p);
	}
	synchronized boolean getPause(){
		return pause;
	}
}
