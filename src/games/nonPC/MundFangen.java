package games.nonPC;

import games.Modus;

import java.io.File;

import player.Player;
import start.X;

public class MundFangen extends StandardNonPC {
	/**
	 * SerialVersioUID generated by Eclipse
	 */
	private static final long serialVersionUID = -218387602330979064L;
	public static String gameName = "Mundfangen";
	public static String getGameName(){
		return gameName;
	}
	public static String shortInfo = "Es geht darum Gummibären (oder Erdnüsse) mit dem Mund aufzufangen \n" +
	"Geworfen wird nacheinander, wenn ein Team fängt und das andere nicht bekommt es einen Punkt";
	public static int defaultNumOfRounds = 8;
	public MundFangen(Player[] player, Modus modus) {
		super(gameName, player, defaultNumOfRounds, modus);
		NonPCGameFileHandler.loadGame(new File(X.getDataDir()+"/gemes/nonPC/MundFangen.game"), player, modus);
	}
	@Override
	public String getShortInfo() {
		return shortInfo;
	}
	@Override
	public void nowVisible(){
		instance.changeBackground("media/nonPC/mundfangen/Gummibär.jpg");
	}
}
