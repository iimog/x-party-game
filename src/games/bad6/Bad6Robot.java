package games.bad6;

public class Bad6Robot {
	private Bad6 bad6;
	private int turnCredits;
	public Bad6Robot(Bad6 bad6){
		this.bad6 = bad6;
	}
	
	public void startTurn(){
		turnCredits = 0;
		bad6.dice[1].doClick();
	}

	public void continueTurn(int currentNum) {
		turnCredits += currentNum;
		boolean fertig = (bad6.myPlayer[1].gameCredit+currentNum) >= bad6.numOfRounds;
		if(fertig || turnCredits >= 10){
			bad6.stopButton.doClick();
		}
		else{
			bad6.againButton.doClick();
		}
	}

}
