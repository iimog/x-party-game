package games.werLuegt;

import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.InfoDialog;
import games.dialogeGUIs.RoundDialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import start.X;
import util.ChangeManager;

public class WerLuegt extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Wer lügt";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);
	static Font labelFont = X.BUTTON_FONT.deriveFont(50f);
	
	private static int defaultNumOfRounds = 5;
	
	public static String getGameName(){
		return gameName;
	}

	private JPanel hauptbereichPanel;
	
	List<WerLuegtAussage> aussageListe;
	List<File> aussageFileListe;
	private JLabel aussageLabel;
	private WerLuegtRotator aktuelleAntwortRotator;
	int timeProAussage = 5;
	int current = -1;
	int whoBuzzed;
	Set<Integer> winnerIDs;
	private JPanel aktuelleAntwortPanel;
	private static File systemFolder = new File(X.getMainDir() + "games/pc/werLuegt/");
	private static File userFolder = new File(X.getDataDir() + "games/pc/werLuegt/");
	
	private void initAussagen(){
		aussageFileListe = new ArrayList<File>();
		String[] systemFiles = systemFolder.list();
		for (String file : systemFiles) {
			if (file.endsWith(".deck")) {
				aussageFileListe.add(new File(systemFolder+"/"+file));
			}
		}
		if(userFolder.exists()){
			String[] userFiles = userFolder.list();
			for (String file : userFiles) {
				if (file.endsWith(".deck")) {
					aussageFileListe.add(new File(userFolder+"/"+file));
				}
			}
		}
	/*		
		
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
		
		aussage = "Ich war schon mal 'Sexiest Woman Alive'";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Mila Kunis");
		richtig.add("Rihanna");
		richtig.add("Scarlet Johannson");
		richtig.add("Jessica Biel");
		richtig.add("Halle Berry");
		richtig.add("Kate Beckinsale");
		richtig.add("Charlize Theron");
		falsch.add("Heidi Klum");
		falsch.add("Kristen Stewart");
		falsch.add("Cameron Diaz");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich war/bin Milliardär";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Carlos Slim Helú");
		richtig.add("Bill Gates");
		richtig.add("Warren Buffet");
		richtig.add("Karl Albrecht");
		richtig.add("Stefan Quandt");
		richtig.add("Theo Albrecht");
		falsch.add("Erwin Albrecht");
		falsch.add("Jay-Z");
		falsch.add("Will Smith");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin im Periodensystem der Elemente";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Gold");
		richtig.add("Zinn");
		richtig.add("Kalium");
		richtig.add("Helium");
		richtig.add("Gallium");
		richtig.add("Cäsium");
		richtig.add("Radon");
		richtig.add("Brom");
		richtig.add("Bismut");
		richtig.add("Wolfram");
		richtig.add("Astat");
		richtig.add("Einsteinium");
		richtig.add("Darmstadtium");
		falsch.add("Oswaldium");
		falsch.add("Polybdän");
		falsch.add("Hiob");
		falsch.add("Throm");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin eine Stadt mit mehr als 1.Millionen Einwohner";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("London Ankara, Dhaka, Bagdad, Rio de Janeiro, Lima, Istanbul, Tokio, Kinshasa, Jakarta, Seoul, Karatschi");
		richtig.add("Rom");
		richtig.add("Paris");
		richtig.add("Berlin");
		richtig.add("Hamburg");
		richtig.add("München");
		richtig.add("Köln");
		richtig.add("Kiew");
		richtig.add("Toronto");
		richtig.add("Tunis");
		richtig.add("Athen");
		richtig.add("Chicago");
		richtig.add("Buenos Aires");
		richtig.add("Pjöngjang");
		richtig.add("Addis Abeba");
		richtig.add("Madrid");
		richtig.add("Johannesburg");
		richtig.add("Prag");
		richtig.add("Wien");
		richtig.add("Melbourne");
		richtig.add("Singapur");
		richtig.add("Sidney");
		richtig.add("Los Angeles");
		richtig.add("Ankara");
		richtig.add("Dhaka");
		richtig.add("Bagdad");
		richtig.add("Rio de Janeiro");
		richtig.add("Lima");
		richtig.add("Istanbul");
		richtig.add("Tokio");
		richtig.add("Kinshasa");
		richtig.add("Jakarta");
		richtig.add("Seoul");
		richtig.add("Karatschi");
		falsch.add("Washington D.C.");
		falsch.add("Frankfurt am Main");
		falsch.add("Würzburg");
		falsch.add("Lissabon");
		falsch.add("Amsterdam");
		falsch.add("San Francisco");
		falsch.add("Ottawa");
		falsch.add("Jerusalem");
		falsch.add("Nikosia");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin eine deutsche Firma:";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Telekom");
		richtig.add("BMW");
		richtig.add("Metro");
		richtig.add("BASF");
		richtig.add("Siemens");
		richtig.add("Allianz");
		richtig.add("Daimler");
		richtig.add("E.ON");
		richtig.add("Volkswagen");
		richtig.add("Rewe");
		richtig.add("Lufthansa");
		richtig.add("MAN");
		richtig.add("adidas");
		richtig.add("puma");
		falsch.add("Shell");
		falsch.add("Total");
		falsch.add("Ford");
		falsch.add("PEMEX");
		falsch.add("Exor");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin/ war Mitglied der SPD";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Sigmar Gabriel");
		richtig.add("Klaus Wowereit");
		richtig.add("Hannelore Kraft");
		richtig.add("Stephan Weil");
		richtig.add("Matthias Platzeck");
		richtig.add("Willy Brandt");
		richtig.add("Peer Steinbrück");
		richtig.add("Gerhard Schröder");
		richtig.add("Helmut Schmidt");
		richtig.add("Frank-Walter Steinmeier");
		richtig.add("Aydan Özo’guz");
		falsch.add("Gregor Gysi");
		falsch.add("Konrad Adenauer");
		falsch.add("Cem Özdemir");
		falsch.add("Sarah Wagenknecht");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich wurde in Südafrika geboren";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Nelson Mandela");
		richtig.add("Charlize Theron");
		richtig.add("J.R.R. Tolkien");
		richtig.add("Howard Carpendale");
		richtig.add("Oscar Pistorius");
		richtig.add("Desmond Tutu");
		richtig.add("Jacob Zuma");
		richtig.add("Fana Mokoena");
		richtig.add("Mandoza");
		richtig.add("Sean Dundee");
		falsch.add("Samuel L. Jackson");
		falsch.add("Akon");
		falsch.add("Samuel Koffour");
		falsch.add("Kofi Annan");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin eine Pflanze";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Gummibaum");
		richtig.add("Butterbaum");
		richtig.add("Christdorn");
		richtig.add("Dost");
		richtig.add("Drachenkopf");
		richtig.add("Eselsdistel");
		richtig.add("Explodiergurke");
		richtig.add("Farbenknöterich");
		richtig.add("Guter Heinrich");
		richtig.add("Habichtskraut");
		richtig.add("Immergrün");
		richtig.add("Joshuabaum");
		richtig.add("Klebkraut");
		richtig.add("Lärche");
		richtig.add("Magnolie");
		richtig.add("Nelke");
		richtig.add("Oleander");
		richtig.add("Puffmais");
		richtig.add("Quendelsommerwurz");
		richtig.add("Rapunzel");
		richtig.add("Safran");
		richtig.add("Tüpfelfarn");
		richtig.add("Ulme");
		richtig.add("Veilchen");
		richtig.add("Waldsauerklee");
		richtig.add("Ysander");
		richtig.add("Zeiland");
		richtig.add("Abendmohn");
		richtig.add("Büffelbeere");
		falsch.add("Barracuda");
		falsch.add("Dicklippe");
		falsch.add("Elritze");
		falsch.add("Granulin");
		falsch.add("Indigolith");
		falsch.add("Österreichischer Pinscher");
		falsch.add("Angora");
		falsch.add("Ovarien");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin ein elektrischer Leiter";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Silber");
		richtig.add("Kupfer");
		richtig.add("Gold");
		richtig.add("Aluminium");
		richtig.add("Natrium");
		richtig.add("Wolfram");
		richtig.add("Eisen");
		richtig.add("Chrom");
		richtig.add("Blei");
		richtig.add("Titan");
		richtig.add("Quecksilver");
		richtig.add("Meerwasser");
		richtig.add("Leitungswasser");
		richtig.add("Luft");
		richtig.add("Vakuum");
		richtig.add("Heiße Luft");
		richtig.add("Essig");
		falsch.add("Holz");
		falsch.add("Stein");
		falsch.add("Benzin");
		falsch.add("Gummi");
		falsch.add("Glas");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		
		aussage = "Ich bin ein Jünger Jesu";
		info = "Die Jünger Jesu waren laut Bibel 12 Stück und hatten z.T. den gleichen Namen";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Judas");
		richtig.add("Thaddäus");
		richtig.add("Jakobus");
		richtig.add("Thomas");
		richtig.add("Matthäus");
		richtig.add("Simon");
		richtig.add("Bartolomäus");
		richtig.add("Philippus");
		richtig.add("Johannes");
		richtig.add("Simon");
		richtig.add("Andreas");
		richtig.add("Simon");
		falsch.add("Markus");
		falsch.add("Lukas");
		falsch.add("Maria Magdalena");
		falsch.add("Zachäus");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		
		aussage = "Ich bin ein DAX Unternehmen";
		info = "";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Adidas Thyssen Krupp, VW");
		richtig.add("Allianz");
		richtig.add("BASF");
		richtig.add("Bayer");
		richtig.add("Beiersdorf");
		richtig.add("BMW");
		richtig.add("Commerzbank");
		richtig.add("Continental");
		richtig.add("Deutsche Bank");
		richtig.add("Deutsche Post");
		richtig.add("Deutsche Börse");
		richtig.add("Deutsche Telekom");
		richtig.add("EON");
		richtig.add("Fresenius");
		richtig.add("Heidelberg Cement");
		richtig.add("Henkel");
		richtig.add("Infineon");
		richtig.add("K+S");
		richtig.add("Lanxess");
		richtig.add("Linde");
		richtig.add("Lufthansa");
		richtig.add("Merck");
		richtig.add("Munich Re");
		richtig.add("RWE");
		richtig.add("SAP");
		richtig.add("Siemens");
		richtig.add("Thyssen Krupp");
		richtig.add("VW");
		falsch.add("Daimler, BayWa, Fraport AG, Hugo Boss, Kabel Deutschland, Puma, Sky, TUI, Wacker Chemie");
		falsch.add("BayWa");
		falsch.add("Fraport AG");
		falsch.add("Hugo Boss");
		falsch.add("Kabel Deutschland");
		falsch.add("Puma");
		falsch.add("Sky");
		falsch.add("TUI");
		falsch.add("Wacker Chemie");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
	
		aussage = "Ich bin ein Film mit Will Smith";
		info = "Will Smith ist einer der beliebtesten und best bezahlten Holliwood-Schauspieler der Welt";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("Men in Black");
		richtig.add("Wild Wild West");
		richtig.add("I Robot");
		richtig.add("Independence Day");
		richtig.add("Men in Black 2");
		richtig.add("Men in Black 3");
		richtig.add("Das Streben nach Glück");
		richtig.add("Hitch-Der Date Doktor");
		richtig.add("Bad Boys 2");
		richtig.add("Der Staatsfeind Nr. 1");
		richtig.add("Sieben Leben");
		richtig.add("Ali");
		richtig.add("Die Legende von Bagger Vance");
		richtig.add("I am Legend");
		richtig.add("After Earth");
		falsch.add("Der Prinz aus Zamunda");
		falsch.add("Beverly Hills Cop");
		falsch.add("Transporter-The Mission");
		falsch.add("The Way It Goes");
		falsch.add("The Blind Side");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		aussage = "Ich bin eine deutsche Landeshauptstadt";
		info = "Deutschland hat 16 Bundesländer und somit 16 Landeshauptstädte.";
		richtig = new ArrayList<String>();
		falsch = new ArrayList<String>();
		richtig.add("München");
		richtig.add("Stuttgart");
		richtig.add("Wiesbaden");
		richtig.add("Saarbrücken");
		richtig.add("Mainz");
		richtig.add("Erfurt");
		richtig.add("Schwerin");
		richtig.add("Dresden");
		richtig.add("Hannover");
		richtig.add("Berlin");
		richtig.add("Hamburg");
		richtig.add("Bremen");
		richtig.add("Potsdam");
		richtig.add("Kiel");
		richtig.add("Magdeburg");
		richtig.add("Düsseldorf");
		falsch.add("Frankfurt");
		falsch.add("Köln");
		falsch.add("Wolfsburg");
		falsch.add("Bonn");
		falsch.add("Nürnberg");
		aussageListe.add(new WerLuegtAussage(aussage,richtig,falsch,info));
		
		
	*/
	}
	
	public WerLuegt(Player[] player, Modus modus, int globalGameID) {
		super(player, defaultNumOfRounds, modus, globalGameID);
		initAussagen();	
		initGUI();
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
		aktuelleAntwortRotator = new WerLuegtRotator();
		aktuelleAntwortRotator.setFont(labelFont);
		aktuelleAntwortRotator.setOpaque(true);
		aktuelleAntwortRotator.addChangeManager(new ChangeManager() {			
			@Override
			public void change() {
				roundEnd(-1);
			}
		});
		aktuelleAntwortRotator.setRotationTime(timeProAussage);
//		aktuelleAntwortRotator.setHorizontalAlignment(JLabel.CENTER);
//		aktuelleAntwortRotator.setBorder(BorderFactory.createLineBorder(Color.black));
		aktuelleAntwortPanel.add(aktuelleAntwortRotator);
		hauptbereichPanel.add(aktuelleAntwortPanel, BorderLayout.SOUTH);
	}

	private void addAussageLabel() {
		aussageLabel = new JLabel("Aussage");
		aussageLabel.setOpaque(true);
		aussageLabel.setFont(labelFont);
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(aussageLabel, BorderLayout.CENTER);
	}
	
	@Override
	public void buzzeredBy(int playerID){
		setBuzzerActive(false);
		aktuelleAntwortRotator.pause();
		roundEnd(playerID);
	}
	
	private boolean aktuelleAussageWahr;

	public void roundEnd(int whoBuzzered) {
		aktuelleAntwortRotator.pause();
		whoBuzzed = whoBuzzered;
		aktuelleAussageWahr = aktuelleAntwortRotator.isCurrentTrue();
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
		current = nextRandom(aussageFileListe.size());
		if(current==-1){
			abbruch();
			// TODO besser handhaben!
		}
		else{
			setBuzzerActive(true);
			aktuelleAntwortRotator.changeDeck(aussageFileListe.get(current));
			aussageLabel.setText(aktuelleAntwortRotator.getDeckName());
			aktuelleAntwortRotator.start();
		}
	}
	
	public void pause(){
		super.pause();
		aktuelleAntwortRotator.pause();
	}
	public void resume(){
		super.resume();
		aktuelleAntwortRotator.start();
	}
	
	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		if(aktuelleAntwortRotator != null)
			aktuelleAntwortRotator.setRotationTime(timeProAussage);
	}

	private void propertiesToSettings() {
		if(customSettings == null)
			return;
		String time = customSettings.getProperty(WerLuegtSettingsDialog.AUSSAGEZEIT, "5");
		timeProAussage = Integer.parseInt(time);
	}

	@Override
	public void start() {
		nextRound();
	}
	
	public void nowVisible(){
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
	public void openSettingsDialog(boolean inGame){
		instance.showDialog(new WerLuegtSettingsDialog(this, inGame));
	}
	
	@Override
	public void openDetailsDialog(){
		instance.showDialog(new WerLuegtDetailsDialog(this));
	}
	
	@Override
	public void openInfoDialog(){
		instance.showDialog(new InfoDialog(aktuelleAntwortRotator.getDeckInfo()));
	}

	public String getCurrentAussage() {
		return aktuelleAntwortRotator.getDeckName();
	}

	public Map<String, Boolean> getCorrectAnswers() {
		return aktuelleAntwortRotator.getCorrectAnswers();
	}
	
	public List<String> getVerlauf(){
		return aktuelleAntwortRotator.getVerlauf();
	}
}
