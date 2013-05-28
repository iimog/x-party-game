package games.nonPC;

import games.Modus;
import player.Player;
import util.SpielListen;

public class EinBein extends StandardNonPC {
	/**
	 * SerialVersioUID generated by Eclipse
	 */
	private static final long serialVersionUID = -218387602330979064L;
	public static String gameName = "Einbein";
	public static String getGameName(){
		return gameName;
	}
	public static String shortInfo = "Es geht darum möglichst lange auf einem Bein zu stehen. \n" +
	"Kann durch verbundene Augen o.ä. gesteigert werden. PS: Hüpfen verboten!";
	private static final int GAME_ID = SpielListen.EINBEIN;
	public int getGameID(){
		return GAME_ID;
	}
	public static int defaultNumOfRounds = 3;
	public EinBein(Player[] player, Modus modus) {
		super(gameName, player, defaultNumOfRounds, modus);
	}
	@Override
	public String getShortInfo() {
		return shortInfo;
	}
	@Override
	public void nowVisible(){
		instance.changeBackground("media/nonPC/einbein/flamingo.jpg");
	}
}
