package games.nonPC;

import games.Modus;
import player.Player;

public class RawNonPC extends StandardNonPC {
	private static final long serialVersionUID = 1L;

	public String shortInfo;
	public String background;
	
	public RawNonPC(String name, Player[] player, int numOfRounds, Modus modus, String shortInfo, String background) {
		super(name, player, numOfRounds, modus);
		this.background = background;
		this.shortInfo = shortInfo;
	}

	@Override
	public String getShortInfo() {
		return shortInfo;
	}
	
	@Override
	public void nowVisible(){
		instance.changeBackground(background);
	}

}
