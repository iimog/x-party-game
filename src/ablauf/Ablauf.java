package ablauf;

import games.Modus;

import java.io.File;
import java.util.List;
import java.util.Set;

import player.Player;
import settings.Profile;

public interface Ablauf {
	public int getNumOfGames();
	public Player[] getPlayers();
	public List<Integer> getGameList();
	public List<Integer> getBisherigeErgebnisse();
	public Set<Integer> getPlayedGameIDs();
	public File getSpeicherDatei();
	public Modus getModus();
	public List<Integer> getErwuenschteSpieleIDs();
	public Profile getProfile();
}
