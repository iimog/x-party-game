package games.nonPC;

import games.Modus;
import player.Player;

public class RawNonPC extends StandardNonPC {
	private static final long serialVersionUID = 1L;

	public String background;
	
	public RawNonPC(Player[] player, int numOfRounds, Modus modus, String background, int globalGameID) {
		super(player, numOfRounds, modus, globalGameID);
		this.background = background;
	}
	
	@Override
	public void nowVisible(){
		instance.changeBackground(background);
	}

}
