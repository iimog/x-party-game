package highscore;

import games.Modus;

import java.io.Serializable;
import java.util.Comparator;

public class GameHighscoreComparator implements Comparator<GameHighscoreElement>, Serializable {
	private static final long serialVersionUID = 1L;
	private Modus modus;
	public GameHighscoreComparator(Modus modus){
		this.modus = modus;
	}
	
	@Override
	public int compare(GameHighscoreElement o1, GameHighscoreElement o2) {
		int vergleich = 0;
		if((o1.getSiegeInProzent(modus)-o2.getSiegeInProzent(modus))<0){
			vergleich = 1;
		}
		if((o1.getSiegeInProzent(modus)-o2.getSiegeInProzent(modus))>0){
			vergleich = -1;
		}
		if(vergleich == 0){
			vergleich = o2.getSiege(modus)-o1.getSiege(modus);
		}
		return vergleich;
	}

}
