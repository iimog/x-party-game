package games.guess;

import games.Game;
import games.Modus;
import games.PC;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import player.Player;


// TODO führende Nullen und Leerzeichen
/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
@SuppressWarnings("serial")
public class Guess extends Game implements PC {

	public class Action implements ActionListener {
		public int loesung;
		public int[] pl = new int[spielerZahl];
		public long distance;
		@Override
		public void actionPerformed(ActionEvent e) {
			loesung = answer[current];
			last = current;			// um noch auf die letzte Antwort zugreifen zu können, nachdem current erneuert wurde
			distance = Math.round(loesung*toleranz);		// 25% Abweichung sind ok
			if(!toleranzOn){
				distance = Integer.MAX_VALUE;
			}
			for(int i=0; i<spielerZahl; i++){
			try {
				myPlayer[i].lastAnswer = String.valueOf(eingabe[i].getPassword());
				pl[i] = Integer.valueOf(myPlayer[i].lastAnswer);
				myPlayer[i].lastDistance = (Math.round(100*(pl[i]-loesung)/loesung));
			} catch (NumberFormatException e1) {
				pl[i] = -1;
				showMessage(myPlayer[i].name + ", bitte in Zukunft eine ganze Zahl eingeben!");
			}}
			// Wer ist der Gewinner
			winnerIs(whoWon());
			if(modus == Modus.TEAM){
				changeActivePlayers();
			}


			current=nextRandom(numOfPics);
			if(current!=-1){							// Nicht alle Fragen verbraucht
				if(stechen()){
					for(int i=0; i<spielerZahl; i++){
						creds[i].ko();
					}
				}
				text.setText(question[current]);
				bschirm.changePic(picture[current]);
				// text.setPreferredSize(new Dimension(bschirm.getPreferredSize().width,55));
				resizeTextArea(text, bschirm.getPreferredSize().width);
				for(int i=0; i<spielerZahl; i++){
					eingabe[i].setText("");
				}
			}
			else{
				showMessage("Es sind alle Fragen verbraucht!");
				abbruch();
			}
		}

		public int whoWon(){
			long[] distance = new long[spielerZahl];
			for(int i=0; i<spielerZahl; i++){
				distance[i]=Math.abs(loesung-pl[i]);
			}
			int winnerID = -1;
			long miniDist = Long.MAX_VALUE;
			for(int i=0; i<spielerZahl; i++){
				long dist = distance[i];
				if(dist==miniDist){
					winnerID = -1;
				}
				if(dist<miniDist){
					miniDist = dist;
					winnerID = i;
				}
			}
			if(toleranzOn && miniDist>toleranz*answer[current]){
				winnerID=-1;
			}
			if(modus == Modus.SOLO && winnerID == -1)winnerID = 1;
			return winnerID;
		}
		
		public void winnerIs(int wID){			// wID index of the winner (-1 if nobody won; -2 if both won)
			if(wID==-1){					// there is no winner
				openRoundDialog("keinen");
			}			
			else{
				creds[wID].earnsCredit(1);
				myPlayer[wID].gameCredit++;
				roundWinner = myPlayer[wID];
				openRoundDialog(myPlayer[wID].name);
			}
		}

	}
	static String gameName = "Guess";
	public static String getGameName(){
		return gameName;
	}
	public double toleranz = 0.95;
	public boolean toleranzOn = true;
	// the following part should get moved to an external file
	String noPic = "media/guess/noPic.jpg";
	String noInfo = "Zu dieser Frage sind leider keine zusätzlichen Informationen vorhanden";
	int numOfPics = 40;
	public String[] picture = new String[numOfPics];
	public String[] question = new String[numOfPics];
	// ACHTUNG: Antworten dürfen nicht 0 sein, da es sonst bei der Berechnung der Abweichung zu einem Fehler kommt
	public int[] answer = new int[numOfPics];
	public String[] info = new String[numOfPics];
	{
		question[0] = "Wie viele Gummibärchen befinden sich im Glas?";
		answer[0] 	= 52;
		picture[0]	= "media/guess/Gummibärchen.jpg";
		info[0] 	= noInfo;

		question[1] = "Wie viele Blatt Klopapier hat diese Klopapierschlange?";
		answer[1] = 145;
		picture[1] = "media/guess/Klopapier.jpg";
		info[1] = noInfo;

		question[2] = "Wie viele Tasten hat ein \"normales\" Klavier?";
		answer[2] = 88;
		picture[2] = noPic;
		info[2] = "Das Klavier hat üblicherweiße 52 weiße und 36 schwarze Tasten und einen Tonumfang von A2 bis c5.";

		question[3] = "Wie viele Einwohner hat die euch sicherlich bekannte Stadt Harare?";
		answer[3] = 1903510;
		picture[3] = "media/guess/harare.jpg";
		info[3] = "Harare ist die größte Stadt Simbabwes und gleichzeitig auch noch die Hauptstadt.";

		question[4] = "Zu wie viel Prozent besteht das menschliche Gehirn aus Wasser?";
		answer[4] = 80;
		picture[4] = noPic;
		info[4] = noInfo;

		question[5] = "Wie schnell prasseln Regentropfen bei einem Wolkenbruch auf die Erde? (in km/h)";
		answer[5] = 29;
		picture[5] = noPic;
		info[5] = "Regentropfen bestehen aus Wasser";

		question[6] = "Wie schnell ist die Kontinentaldrift zwischen Europa und Amerika? (in mm/Jahr)";
		answer[6] = 30;
		picture[6] = noPic;
		info[6] = noInfo;

		question[7] = "Wie lang ist das Great Barrier Reef? (in km)  ";
		answer[7] = 2300;
		picture[7] = "media/guess/GreatBarrierReef.jpg";
		info[7] = "Das Great Barrier Reef vor der Küste Australiens ist das größte Korallenriff der Erde. Im Jahre 1981 wurde es von der UNESCO zum Weltnaturerbe erklärt und wird manchmal als eines der sieben Weltwunder der Natur bezeichnet.";

		question[8] = "Wie viel wiegt der größte Schmetterling? (in Gramm)";
		answer[8] = 25;
		picture[8] = "media/guess/schmetterling.jpg";
		info[8] = noInfo;

		question[9] = "Wie alt wurde der älteste Affe?";
		answer[9] = 53;
		picture[9] = noPic;
		info[9] = noInfo;

		question[10] = "Wie viele Kätzchen brachte die Katze mit den meisten Jungen zur Welt? Insgesamt";
		answer[10] = 420;
		picture[10] = noPic;
		info[10] = noInfo;

		question[11] = "Der Wanderfalke ist das Lebewesen, das die höchsten Geschwindigkeiten erreicht! Wie schnell fliegt er maximal? (in km/h)";
		answer[11] = 270;
		picture[11] = noPic;
		info[11] = noInfo;

		question[12] = "Den schnellsten Flügelschlag der Welt hat der Kolibri! Wie viele Flügelschläge schafft er pro Sekunde?";
		answer[12] = 90;
		picture[12] = "media/guess/kolibri.jpg";
		info[12] = noInfo;

		question[13] = "Wann etwa haben die ersten Dinosaurier gelebt? (in mio Jahren)";
		answer[13] = 230;
		picture[13] = noPic;
		info[13] = noInfo;

		question[14] = "Wie schnell war die schnellste Schnecke? (in mm/s)";
		answer[14] = 7;
		picture[14] = noPic;
		info[14] = noInfo;

		question[15] = "Wie viele griechische Inseln gibt es?"; //evtl Bild
		answer[15] = 2000;
		picture[15] = noPic;
		info[15] = noInfo;

		question[16] = "Die Transsibirische Eisenbahn ist die längste Bahnstrecke der Welt wie lang ist sie? (in km)";
		answer[16] = 9299;
		picture[16] = "media/guess/transsibirische-eisenbahn.jpg";
		info[16] = noInfo;

		question[17] = "Wie lang ist die Tour de France? (in km)";
		answer[17] = 4000;
		picture[17] = noPic;
		info[17] = noInfo;

		question[18] = "Die größte Düne Europas befindet sich an der Atlantikküste Frankreichs. Wie hoch ist sie? (in m)";
		answer[18] = 114;
		picture[18] = "media/guess/Düne.jpg";
		info[18] = noInfo;

		question[19] = "Wie viele Seen gibt es in Finnland? ";
		answer[19] = 60000;
		picture[19] = noPic;
		info[19] = noInfo;

		question[20] = "Die Rallye Paris- Dakar ist die härteste der Welt. Wie viele Todesopfer hat sie bereits gefordert?";
		answer[20] = 60;
		picture[20] = "media/guess/Dakar.jpg";
		info[20] = "Quelle: http://www.zeit.de/politik/2011-01/rallye-dakar-umwelt-tote";

		question[21] = "Wie lang ist der längste Eisenbahntunnel der Erde? (in km)";
		answer[21] = 50;
		picture[21] = noPic;
		info[21] = noInfo;

		question[22] = "Wie viele englische Pound ergeben 100 kg";
		answer[22] = 220;
		picture[22] = noPic;
		info[22] = noInfo;

		question[23] = "Wie schnell fuhr 1903 die schnellste Lokomotive?";
		answer[23] = 210;
		picture[23] = noPic;
		info[23] = noInfo;

		question[24] = "Wie lang ist die Golden-Gate-Bridge? (in m)";
		answer[24] = 2150;
		picture[24] = "media/guess/golden-gate-bridge.jpg";
		info[24] = noInfo;

		question[25] = "Wie viele ml Wasser bekommt dieser junge Mann in seinen Mund?";
		answer[25] = 100;
		picture[25] = "media/guess/Drink.jpg";
		info[25] = noInfo;

		question[26] = "Wie viele Schirmchen hatte diese Pusteblume?";
		answer[26] = 146;
		picture [26] = "media/guess/Pusteblume.jpg";
		info[26] = noInfo;

		question[27] = "Wie viel Kohlendioxid wurde im Jahr 2009 in die Erdatmosphäre geblasen?Angabe in Milliarden Tonnen!";
		answer[27] = 36;
		picture [27] = noPic;
		info[27] = "Das Gas befeuert den sogenannten Treibhauseffekt.Steigt der CO2 Anteil in der Luft kann die Wärme die von der Erde abgestrahlt wird nicht mehr ins All abgegeben werden...";

		question[28] = "Wieviele Gehirne von Ameisen braucht man, um das Gehirn eines Menschen aufzuwiegen?";
		answer[28] = 13000000;
		picture[28] = "media/guess/Ameise.png";
		info[28] = "Eine Ameise wiegt etwa 10mg, davon entfallen ca. 0,1mg auf das Gehirn (1Million Nervenzellen). Ein durchschnittliches menschliches Gehirn wiegt 1,3kg";

		question[29] = "Wie alt ist das Gestein des Ayers Rock? In mio. Jahren!";
		answer[29] = 500;
		picture[29] = "media/guess/Uluru.jpg";
		info[29] = "Der Uluru ( Schatten spendender Platz), auch Ayers Rock, ist ein großer Inselberg aus Sandstein in der zentralaustralischen Wüste im Northern Territory.";

		question[30] = "Wie viele Inseln umfasst der Inselstaat Fidschi?";
		answer[30] = 332;
		picture[30] = "media/guess/fidschi.jpg";
		info[30] = "Fidschi (engl.: Fiji Islands) ist ein Inselstaat im Südpazifik nördlich von Neuseeland und östlich von Australien. Fidschi ist seit 1970 unabhängig und seit 1987 eine Republik mit der Hauptstadt Suva auf der Insel Viti Levu.";

		question[31] = "Wie viele Liter fasst der Bodensee durchschnittlich? Angabe in Miliarden Litern";
		answer[31] = 50000;
		picture[31] = "media/guess/bodensee.jpg";
		info[31] = "Unter der Bezeichnung Bodensee fasst man die drei im nördlichen Alpenvorland liegenden Gewässereinheiten Obersee, Untersee und Seerhein zusammen. Es handelt sich also um zwei selbständige Seen (Stillgewässer) und einen sie verbindenden Fluss (Fließgewässer).";

		question[32] = "Wie viele PS kann ein trainiertes Pferd kurzzeitig leisten?";
		answer[32] = 20;
		picture[32] = "media/guess/pferd.jpg";
		info[32] = "Ein Arbeitspferd leistet im Tagesdurchschnitt 1 PS.";

		question[33] = "Wie viele Olivenbäume wachsen in Tunesien? Angabe in Millionen.";
		answer[33] = 50;
		picture [33] = noPic;
		info[33] = "Die Olivenbäume werden im Herbst abgeerntet, aus den Oliven entsteht größtenteils Olivenöl.";

		question[34] = "Wie viel Prozent der tunesischen Landfläche werden von der Sahara bedeckt?";
		answer[34] = 33;
		picture [34] = "media/guess/sahara.jpg";
		info[34] = "Die Sahara ist die trockenste Sandwüste der Welt.";

		question[35] = "Wie viel Prozent der tunesischen Bevölkerung leben ca. in der Hauptstadt, Tunis?";
		answer[35] = 23;
		picture [35] = "media/guess/tunis.jpg";
		info[35] = "Tunis hat 2380500 Einwohner, demnach hat Tunesien 10.276.158 Einwohner was grob 25% entspricht.";   //TODO neu formulieren

		question[36] = "Es führt eine Treppe zum Gipfel des Berges Sinai, wie viele Stufen sind es?";
		answer[36] 	= 4000;
		picture[36]	= "media/guess/Sinai.jpg";
		info[36] 	= "Auf dem Berg Sinai soll Moses von Gott die Zehn Gebote erhalten haben. Der Berg ist 2285m hoch un die letzten Stufen sind schwindelerregend steil!";

		question[37] = "Jeder kennt 'die Rose', doch gibt es mehr als nur eine Sorte!Wie viele Rosenzüchtungen gibt es Weltweit?";
		answer[37] 	= 30000;
		picture[37]	= "media/guess/rosen.jpg";
		info[37] 	= "Die Rosenzucht nahm ihren Anfang im 18. Jhd und seitdem über 30000 Rosensorten hervorgebracht!";

		question[38] = "Schnee, mancher liebt ihn die anderen hassen ihn...aber zu wie viel Prozent der Schnee aus Luft besteht wissen die Wenigsten!?";
		answer[38] 	= 95;
		picture[38]	= "media/guess/schnee.jpg";
		info[38] 	= "Schnee besteht bis zu 95 Przent aus Luft und bildet zwischen den Eiskristallen einer Flocke unzählige Luftkammern";

		question[39] = "Aus wie vielen Ländern besteht die EU?"; // Staaten?
		answer[39] 	= 27;
		picture[39]	= "media/guess/EU.jpg";
		info[39] 	= "Die Europäische Union (EU) ist ein aus 27 europäischen Staaten bestehender Staatenverbund. Seine Bevölkerung umfasst derzeit rund 500 Millionen Einwohner. Der von den EU-Mitgliedstaaten gebildete Europäische Binnenmarkt ist der am Bruttoinlandsprodukt gemessen größte gemeinsame Markt der Welt.";
	}

	// the following part chooses a random number, that was not yet used
	public int current;
	public int last;
	public Player roundWinner;

	// frame bausteine

	JTextArea text;
	gui.components.Bildschirm bschirm;
	JPanel split = new JPanel();
	JPasswordField[] eingabe;
	JButton finish = new JButton("OK");

	public Guess(Player[] player, Modus modus, int globalGameID){
		this(player, 5, modus, globalGameID);
	}

	public Guess(Player[] players, int numOfRounds, Modus modus, int globalGameID){
		super(gameName, players, numOfRounds, modus, globalGameID);
		// numOfPlayers = players.length; --> if there are more than two Players
		this.numOfRounds = numOfRounds;
		if(modus == Modus.SOLO){
			spielerZahl--;
			toleranz = 0.5;
		}
		eingabe = new JPasswordField[spielerZahl];
		current=nextRandom(numOfPics);
		spielBereichPanel.setLayout(new BorderLayout());
		text = new JTextArea(question[current]);
		text.setEditable(false);
		text.setFont(new Font("Comic Sans MS",0,20));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		spielBereichPanel.add(text,"North");
		updateCreds();
		bschirm = new gui.components.Bildschirm(picture[current]);
		spielBereichPanel.add(bschirm);
		spielBereichPanel.add(split,"South");
		split.setLayout(new GridLayout(1,spielerZahl+1));
		for(int i=0; i<spielerZahl; i++){
			eingabe[i] = new JPasswordField();
			split.add(eingabe[i]);
		}
		split.add(finish);
		finish.addActionListener(new Action());
	}

	public void nowVisible(){
		instance.changeBackground("media/guess/fragezeichen.jpg");
	}

	public void openDetailsDialog(){
		instance.showDialog(new GuessDetailsDialog(this));
	}
	public void openInfoDialog(){
		instance.showDialog(new games.dialogeGUIs.InfoDialog(info[last]));
	}
	public void openRoundDialog(String winner){
		instance.showDialog(new games.dialogeGUIs.RoundDialog(this,winner));
	}

	public void openSettingsDialog(){
		instance.showDialog(new GuessSettingsDialog(this));
	}
	// mit dieser Methode wird die Höhe und Breite des Textfeldes gesetzt, da sonst das
	// Fenster nicht mehr kleiner wird und somit das Bild nicht mittig sitzt
	protected void resizeTextArea(JTextArea tA, int prefWidth){
		int length = tA.getText().length();
		int size = tA.getFont().getSize();
		double rows = Math.ceil((length*(size-3))/prefWidth);
		if(rows==0)rows=1;
		if(rows>=4)rows--;
		tA.setPreferredSize(new Dimension(prefWidth,(int)rows*(size+8)));
		// --> evtl. elegantere Lösung...
		/*
		FontMetrics fm = getFontMetrics(tA.getFont());
		int width = fm.stringWidth(tA.getText());
		int height = fm.getHeight();
		double rows = Math.ceil(width/prefWidth);
		tA.setPreferredSize(new Dimension(prefWidth, (int) (rows)*height));
		 */
	}
	public void settingsChanged(){
		updateCreds();
	}
	@Override
	public void start(){
		// Spielstart Code hier
	}
}

