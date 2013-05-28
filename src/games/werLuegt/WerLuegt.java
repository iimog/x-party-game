package games.werLuegt;

import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.InfoDialog;
import games.dialogeGUIs.RoundDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import player.Player;
import start.X;
import util.SpielListen;

public class WerLuegt extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Wer lügt";
	private static String shortInfo = "Was ist die Wahrheit was gelogen? (Fast wie bei X-Faktor)";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);
	static Font labelFont = X.buttonFont.deriveFont(50f);
	
	private static int defaultNumOfRounds = 5;
	
	public static String getGameName(){
		return gameName;
	}

	private JPanel hauptbereichPanel;
	
	List<WerLuegtAussage> aussageListe;
	private JLabel aussageLabel;
	private JLabel aktuelleAntwortLabel;
	int timeProAussage = 5;
	int current = -1;
	int whoBuzzed;
	Set<Integer> winnerIDs;
	private JPanel aktuelleAntwortPanel;
	
	private void initAussagen(){
		aussageListe = new ArrayList<WerLuegtAussage>();
		String aussage = "Ich bin ein Euroland";
		String info = "Die Engländer haben ihre Währung damals behalten.";
		List<String> richtig = new ArrayList<String>();
		List<String> falsch = new ArrayList<String>();
		richtig.add("Deutschland");
		richtig.add("Griechenland");
		richtig.add("Frankreich");
		richtig.add("Italien");
		richtig.add("Spanien");
		falsch.add("England");
		falsch.add("Tschechien");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin ein American Football Team (nfl)";
		info = "Schau dir 'nfl.com' an";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("New Orleans Saints");
		richtig.add("San Diego Chargers");
		richtig.add("Baltimor Ravens");
		richtig.add("Houston Texans");
		richtig.add("San Francisco 49ers");
		richtig.add("Tampa Bay Buccaneers");
		richtig.add("Cleveland Browns");
		richtig.add("Cincinatti Bangels");
		richtig.add("Denver Broncos");
		richtig.add("Tennessee Titans");
		falsch.add("San Francisco Giants");
		falsch.add("New York Yankees");
		falsch.add("Berlin Adler");
		falsch.add("Orlando Magic");
		falsch.add("Dallas Mavericks");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich habe schon die Fußballweltmeisterschaft gewonnen";
		info = "wikipedia.org";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Deutschland");
		richtig.add("Frankreich");
		richtig.add("Italien");
		richtig.add("Spanien");
		richtig.add("England");
		richtig.add("Brasilien");
		richtig.add("Argentinien");
		richtig.add("Uruguay");
		falsch.add("Tschechien");
		falsch.add("USA");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich war deutscher Bundeskanzler";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Konrad Adenauer");
		richtig.add("Ludwig Erhard");
		richtig.add("Kurt Georg Kiesinger");
		richtig.add("Willy Brandt");
		richtig.add("Helmut Schmidt");
		richtig.add("Helmut Kohl");
		richtig.add("Gerhard Schröder");
		richtig.add("Angela Merkel");
		falsch.add("Edmund Stoiber");
		falsch.add("Theodor Heuss");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin/war James Bond";
		info = "wikipedia.org";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Sean Connery");
		richtig.add("George Lazenby");
		richtig.add("Roger Moore");
		richtig.add("Timothy Dalton");
		richtig.add("Pierce Brosnan");
		richtig.add("Daniel Craig");
		falsch.add("Marc Forster");
		falsch.add("Tom Cruise");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));

		aussage = "Ich wohne in Entenhausen";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Donald Duck");
		richtig.add("Gustav Gans");
		richtig.add("Daisy Duck");
		richtig.add("Dagobert Duck");
		richtig.add("Mickey Maus");
		richtig.add("Tick Trick und Track");
		richtig.add("Goofy");
		richtig.add("Pluto");
		falsch.add("Gundula Gause");
		falsch.add("Gargamel");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich war Spieler des FC Bayern München";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Lukas Podolski");
		richtig.add("Giovanne Elber");
		richtig.add("Mario Basler");
		richtig.add("Stefan Effenberg");
		richtig.add("Klaus Augenthaler");
		richtig.add("Dieter Hoeneß");
		richtig.add("Lothar Matthäus");
		richtig.add("Andreas Brehme");
		richtig.add("Alexander Zickler");
		richtig.add("Sebastian Deisler");
		richtig.add("Phillip Lahm");
		falsch.add("Fritz Walter");
		falsch.add("Jupp Heynckes");
		falsch.add("Stefan Kuntz");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin eine Millionenstadt";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Barcelona");
		richtig.add("Melbourne");
		richtig.add("Mailand");
		richtig.add("Sydney");
		richtig.add("Hong Kong");
		richtig.add("Paris");
		richtig.add("Köln");
		richtig.add("Los Angeles");
		richtig.add("Prag");
		richtig.add("Budapest");
		richtig.add("La Paz");
		falsch.add("San Francisco");
		falsch.add("Marseille");
		falsch.add("Stockholm");
		falsch.add("Ottawa");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin ein Tanz";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Mambo");
		richtig.add("Fandango");
		richtig.add("Mazurka");
		richtig.add("Lambada");
		richtig.add("Bolero");
		richtig.add("Siciliano");
		richtig.add("Tarantella");
		richtig.add("Twist");
		falsch.add("Lumumba");
		falsch.add("Daiquiri");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin ein Lied der Beatles";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Help!");
		richtig.add("I’ve Got a Feeling");
		richtig.add("Yellow Submarine");
		richtig.add("Let It Be");
		richtig.add("Yesterday");
		richtig.add("Maggie Mae");
		richtig.add("Twist and Shout");
		richtig.add("Please Mr. Postman");
		falsch.add("Saturday Night Fever");
		falsch.add("How deep is your love");
		falsch.add("Forever Young");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin eine Region in Frankreich";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Bretagne");
		richtig.add("Burgund");
		richtig.add("Lothringen");
		richtig.add("Auvergne");
		richtig.add("Côte d'azur");
		richtig.add("Martinique");
		richtig.add("Picardie");
		falsch.add("Piemont");
		falsch.add("Romandie");
		falsch.add("Lombardei");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin US-Amerikaner";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("George Clooney");
		richtig.add("Landon Donovan");
		richtig.add("Barack Obama");
		richtig.add("Michael Jackson");
		richtig.add("Michael Phelps");
		falsch.add("David Beckham");
		falsch.add("Nicole Kidman");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin ein geologisches Zeitalter";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Kreide");
		richtig.add("Jura");
		richtig.add("Perm");
		richtig.add("Quartär");
		richtig.add("Neogen");
		richtig.add("Paläogen");
		richtig.add("Devon");
		richtig.add("Silur");
		richtig.add("Kambrium");
		richtig.add("Tonium");
		richtig.add("Stenium");
		richtig.add("Ectasium");
		richtig.add("Ordovizium");
		richtig.add("Calymmium");
		richtig.add("Rhyacium");
		richtig.add("Trias");
		falsch.add("Platinum");
		falsch.add("Silicium");
		falsch.add("Prado");
		falsch.add("Praucium");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin ein Dienstgrad in der Bundeswehr";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Hauptfeldwebel");
		richtig.add("Hauptgefreiter");
		richtig.add("Fähnrich");
		richtig.add("Fahnenjunker");
		richtig.add("Oberst");
		richtig.add("Hauptmann");
		richtig.add("Leutnant");
		richtig.add("Brigadegeneral");
		richtig.add("Generalleutnant");
		falsch.add("Staatsmajor");
		falsch.add("Führer");
		falsch.add("Hauptwachtmeister");
		falsch.add("Sergeant");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich war neutrales Land im 1. Weltkrieg";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Mexiko");
		richtig.add("Spanien");
		richtig.add("Argentinien");
		richtig.add("Dänemark");
		richtig.add("Finnland");
		richtig.add("Neuseeland");
		richtig.add("Peru");
		richtig.add("Schweiz");
		falsch.add("Australien");
		falsch.add("Indien");
		falsch.add("China");
		falsch.add("Japan");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Straßen aus Monopoly";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Schlossallee");
		richtig.add("Parkstraße");
		richtig.add("Neue Straße");
		richtig.add("Schillerstraße");
		richtig.add("Lessingstraße");
		richtig.add("Goethestraße");
		richtig.add("Badstraße");
		richtig.add("Turmstraße");
		richtig.add("Chausseestraße");
		richtig.add("Elisenstraße");
		richtig.add("Poststraße");
		richtig.add("Seestraße");
		richtig.add("Hafenstraße");
		richtig.add("Münchner Straße");
		richtig.add("Berliner Straße");
		richtig.add("Wiener Straße");
		richtig.add("Theaterstraße");
		richtig.add("Museumsstraße");
		richtig.add("Opernplatz");
		richtig.add("Rathausplatz");
		richtig.add("Bahnhofstraße");
		richtig.add("Hauptstraße");
		falsch.add("Burgstraße");
		falsch.add("Mozartstraße");
		falsch.add("Elyseestraße");
		falsch.add("Bremer Straße");
		falsch.add("Domstraße");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		/*
		aussage = "";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("");
		richtig.add("");
		richtig.add("");
		richtig.add("");
		richtig.add("");
		falsch.add("");
		falsch.add("");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		 */
	}
	
	public WerLuegt(Player[] player, Modus modus) {
		super(gameName, player, defaultNumOfRounds, modus);
		initAussagen();	
		initGUI();
		registerBuzzerActions();
	}

	private void initGUI(){
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		addAussageLabel();
		addAktuelleAntwortPanel();
	}

	private void addAktuelleAntwortPanel() {
		aktuelleAntwortPanel = new JPanel();
		aktuelleAntwortPanel.setLayout(new GridLayout(2, 1));
		aktuelleAntwortPanel.setOpaque(false);
		aktuelleAntwortPanel.add(new JLabel());
		aktuelleAntwortLabel = new JLabel("Aktuelle Antwort");
		aktuelleAntwortLabel.setFont(labelFont);
		aktuelleAntwortLabel.setOpaque(true);
		aktuelleAntwortLabel.setHorizontalAlignment(JLabel.CENTER);
		aktuelleAntwortLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		aktuelleAntwortPanel.add(aktuelleAntwortLabel);
		hauptbereichPanel.add(aktuelleAntwortPanel, BorderLayout.SOUTH);
	}

	private void addAussageLabel() {
		aussageLabel = new JLabel("Aussage");
		aussageLabel.setOpaque(true);
		aussageLabel.setFont(labelFont);
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(aussageLabel, BorderLayout.CENTER);
	}
	
	private class BuzzerAction extends AbstractAction{
		private static final long serialVersionUID = 1L;
		private int playerID;

		public BuzzerAction(int playerID){
			this.playerID = playerID;
		}
		
		public void actionPerformed(ActionEvent e){
			antwortRotater.interrupted = true;
			roundEnd(playerID);
		}
	}
	
	private void registerBuzzerActions() {
		for(int i=0; i<spielerZahl; i++){
			Action action = new BuzzerAction(i);
			String actionName = "Buzzer"+i;
			// Register keystroke
			hauptbereichPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			    KeyStroke.getKeyStroke(myPlayer[i].getKey(),0), actionName);
			// Register action
			hauptbereichPanel.getActionMap().put(actionName, action);
		}	
	}
	
	private boolean aktuelleAussageWahr;
	private AntwortRotator antwortRotater;

	public void roundEnd(int whoBuzzered) {
		whoBuzzed = whoBuzzered;
		aktuelleAussageWahr = aussageListe.get(current).isWahr();
		winnerIDs = new HashSet<Integer>();	
		if(aktuelleAussageWahr || whoBuzzered == -1){
			// Leider zu Unrecht gebuzzert oder keiner gebuzzert
			for(int i=0; i<spielerZahl; i++){
				if(i != whoBuzzered) winnerIDs.add(i);
			}
		}
		else{
			// Recht gehabt, die Aussage wahr falsch
			winnerIDs.add(whoBuzzered);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
	}

	private void verbuchePunkte(Set<Integer> winnerIDs) {
		if(winnerIDs.size() < myPlayer.length){
			for(int id : winnerIDs){
				creds[id].earnsCredit(1);
				myPlayer[id].gameCredit++;
			}
		}
	}
	
	private String getWinnerText(Set<Integer> winnerIDs) {
		String winner = "";
		if(winnerIDs.size() >= myPlayer.length || winnerIDs.size()==0){
			winner = "niemanden";
		}
		else{
			int c=0;
			for(int id : winnerIDs){
				String trenner = "";
				if(c==winnerIDs.size()-3)trenner = ", ";
				if(c==winnerIDs.size()-2)trenner = " und ";
				winner += myPlayer[id].name+trenner;
				c++;
			}
		}
		return winner;
	}
	
	private void nextRound(){
		if(modus == Modus.TEAM)changeActivePlayers();
		aktuelleAussageWahr = false;
		current = nextRandom(aussageListe.size());
		if(current==-1){
			abbruch();
			// TODO besser handhaben!
		}
		else{
			aussageLabel.setText(aussageListe.get(current).getAussage());
			antwortRotater = new AntwortRotator();
			new Thread(antwortRotater).start();
		}
	}

	class AntwortRotator implements Runnable{
		boolean interrupted = false;
		
		@Override
		public void run() {
			aktuelleAntwortLabel.setText("");
			try{
				Thread.sleep(timeProAussage*1000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			while(!interrupted){
				if(!aussageListe.get(current).hasMore()){
					roundEnd(-1);
					break;
				}
				aktuelleAntwortLabel.setText(aussageListe.get(current).getNextAntwort());
				try{
					Thread.sleep(timeProAussage*1000);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	};
	
	@Override
	public String getShortInfo() {
		return shortInfo;
	}

	@Override
	public int getGameID() {
		return SpielListen.WERLUEGT;
	}

	@Override
	public void settingsChanged() {
		updateCreds();
	}

	@Override
	public void start() {
		nextRound();
	}
	
	public void nowVisible(){
		// TODO background einrichten
		instance.changeBackground("media/wer_luegt/wer_luegt.jpg");
	}
	
	@Override
	public void openRoundDialog(String winner){
		RoundDialog rd = new RoundDialog(this, winner);
		instance.showDialog(rd);
	}
	
	@Override
	public void goBack() {
		if(!isOver())nextRound();
	}

	@Override
	public void openSettingsDialog(){
		instance.showDialog(new WerLuegtSettingsDialog(this));
	}
	
	@Override
	public void openDetailsDialog(){
		instance.showDialog(new WerLuegtDetailsDialog(this));
	}
	
	@Override
	public void openInfoDialog(){
		instance.showDialog(new InfoDialog(aussageListe.get(current).getInfo()));
	}
}
