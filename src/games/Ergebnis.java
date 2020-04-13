package games;

import player.Player;

public class Ergebnis {
	private Player[] players;
	private Player winner;
	private String gameName;
	private Modus modus;
	
	public Ergebnis(){
		
	}
	public Ergebnis(Game g, Player winner){
		this.players = g.myPlayer;
		this.gameName = g.getGameFileName();
		this.modus = g.modus;
		this.winner = winner;
	}
	public Player[] getPlayers() {
		return players;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public Player getWinner() {
		return winner;
	}
	public void setWinner(Player winner) {
		this.winner = winner;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Modus getModus() {
		return modus;
	}
	public void setModus(Modus modus) {
		this.modus = modus;
	}
}
