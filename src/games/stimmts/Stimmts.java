package games.stimmts;

import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.InfoDialog;
import games.dialogeGUIs.RoundDialog;
import gui.components.Countdown;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import player.Player;
import start.X;
import util.ChangeManager;

public class Stimmts extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Stimmts";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);

	public static final int[] trueBuzz = { KeyEvent.VK_D, KeyEvent.VK_K,
			KeyEvent.VK_B, KeyEvent.VK_Z };
	public static final int[] falseBuzz = { KeyEvent.VK_F, KeyEvent.VK_L,
			KeyEvent.VK_N, KeyEvent.VK_U };

	private static int defaultNumOfRounds = 5;

	public static String getGameName() {
		return gameName;
	}

	public void pause() {
		super.pause();
		countdown.pause();
	}

	public void resume() {
		super.resume();
		countdown.resume();
	}

	private JPanel hauptbereichPanel;

	List<StimmtsAussage> aussageListe;
	private JLabel aussageLabel;
	boolean[] vermutung;
	int timeProAussage = 5;
	private Countdown countdown;
	int current = -1;
	boolean letzteAussageWahr;
	Set<Integer> winnerIDs;
	private boolean[] answerGiven;

	private void initAussagen() {
		aussageListe = new ArrayList<StimmtsAussage>();

		aussageListe
				.add(new StimmtsAussage(
						"Der Begriff Erlkönig leitet sich von der Erle ab.",
						false,
						"Bei der Übertragung einer dänischen Sage ins Deutsche unterlief Johann Gottfried Herder (1744-1803) ein Irrtum, als er Ellerkonge als Erlkönig und nicht als Elfenkönig übersetzte."));
		aussageListe
				.add(new StimmtsAussage(
						"Grünkern ist eine besonders wertvolle Getreideart.",
						false,
						"Grünkern ist keine eigene Getreideart. In milchreifem Zustand geerntete, dann langsam gedörrte und entspelzte Dinkelkörner gelangen als Grünkern in den Handel."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Azteken benutzten Kakaofrüchte als Zahlungsmittel.",
						true));
		aussageListe
				.add(new StimmtsAussage(
						"Schokolade hat eine leicht anregende Wirkung.",
						true,
						"Der Durchschnittseuropäer verzehrt etwa 5,3 kg Schokolade pro Jahr und damit etwa doppelt soviel als der Nordamerikaner."));
		aussageListe
				.add(new StimmtsAussage(
						"Die US-Amerikaner konsumieren weltweit die größte Schokoladenmenge pro Kopf.",
						false));
		aussageListe
				.add(new StimmtsAussage(
						"Das Musical entstand im 20. Jahrhundert am Broadway.",
						true,
						"Hier bildete sich diese Mischung aus europäischer Operette, amerikanischem Jazz, Schauspiel, Tanz, Tragik und Komik."));
		aussageListe
				.add(new StimmtsAussage(
						"Auch klassische Shakespeare-Dramen wurden Vorlagen für Musicals.",
						true,
						"Aus \"Der Widerspenstigen Zähmung\" wurde \"Kiss me Kate\" mit der Musik von Cole Porter. Einen wohl noch größeren Erfolg errang Leonard Bernstein mit seiner \"Westside Story\", einer modernen \"Romeo und Julia\"-Geschichte."));
		aussageListe
				.add(new StimmtsAussage(
						"Der Rüssel des Elefanten besteht aus vielen kleinen Knorpeln und Knochen.",
						false,
						"Der biegsame und lenkbare Elefantenrüssel hat weder Knochen noch Knorpel. Er besteht aus etwa 100.000 Muskeleinheiten und kann mit seiner besonderen Geschicklichkeit nicht nur grobe Arbeiten verrichten, sondern auch rund 8 Liter Wasser aufnehmen und münzgroße Objekte fassen."));
		aussageListe
				.add(new StimmtsAussage(
						"Elefanten haben die längste Embryonalentwicklung aller lebenden Säugetiere",
						true,
						"Eine Elefantenkuh trägt ihr Kalb etwa 20 Monate und säugt es dann meist noch einige Jahre"));
		aussageListe
				.add(new StimmtsAussage(
						"Koalas können sich mit ihrem grunzenden Bellen über relativ große Entfernungen verständigen.",
						true,
						"Sie verständigen sich durch unterschiedliche Laute. Bei Gefahr stoßen sie einen Angstruf aus, der fast wie der Angstschrei eines Babys klingt."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Entwicklung eines neugeborenen Koalas erfolgt binnen 1 Woche.",
						false,
						"Koalas entwickeln sich langsamer als die meisten anderen Beuteltiere. 6 - 7 Monate ernährt sich das Koalababy nur von Muttermilch. Erst im Alter von fast 1 Jahr verlässt es den mütterlichen Beutel."));
		aussageListe
				.add(new StimmtsAussage(
						"Die mittlere Lebenserwartung eines Koalas sind etwa 2 Jahre.",
						false,
						"Koalas werden durchschnittlich 10 Jahre alt. In Brisbane soll ein Koalaweibchen 19 Jahre alt geworden sein."));
		aussageListe
				.add(new StimmtsAussage(
						"Haie gibt es seit etwa 10.000 Jahren.",
						false,
						"Schon seit ungefähr 450 Millionen Jahren gibt es Haie, Millionen Jahre vor den Dinosauriern und den Menschen."));
		aussageListe
				.add(new StimmtsAussage(
						"Auch in Seen oder Flüssen können Menschen auf einen gefährlichen Hai treffen.",
						true,
						"Der Bullenhai aus der Blauhaifamilie ist der einzige Hai, der Ausflüge in Flüsse und Seen macht und dabei den völlig überraschten Menschen äußerst gefährlich werden kann."));
		aussageListe
				.add(new StimmtsAussage(
						"Korallenriffe sind oft Standorte für Haie.",
						true,
						"Berüchtigt ist der besonders gefährliche Riffhai, "
								+ "der sich im Umkreis von Korallenriffen im Indischen Ozean, im Pazifik und im Roten Meer aufhält."));
		aussageListe
				.add(new StimmtsAussage(
						"Eisbären sind die größten Landraubtiere der Welt.",
						true,
						"Männliche Eisbären werden bis zu 3 m lang. Ihr Gewicht kann mehr als 700 kg sein. Die Weibchen sind meist kleiner und leichter."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Eisbären leben in der Südpolregion.", false,
						"Rings um den Nordpol, im Packeis der Arktis, ist die Heimat der Eisbären."));
		aussageListe
				.add(new StimmtsAussage(
						"Eisbären halten einen Winterschlaf.",
						false,
						"Männliche Eisbären streifen ständig umher, machen oft weite Wanderungen und verbringen die Polarnacht oft auf Treibeis"));
		aussageListe
				.add(new StimmtsAussage(
						"Krokodile zählen zur Familie der Dinosaurier.",
						true,
						"Sie sind die letzten Überlebenden der großen Gruppe der Archosaurier, deren Blütezeit vor etwa 220 Millionen Jahren war. Die bekanntesten der ausgestorbenen Gruppen sind die gewaltigen Dinosaurier."));
		aussageListe
				.add(new StimmtsAussage(
						"Krokodile können fast 10 m lang werden.", true,
						"Das Australische Leistenkrokodil zählt zu den größten Arten."));
		aussageListe
				.add(new StimmtsAussage(
						"Der Kartoffelkäfer ist ein weit verbreiteter und sehr gefürchteter Kartoffelschädling.",
						true,
						"Er wurde im 20. Jahrhundert von Amerika nach Europa eingeschleppt."));
		aussageListe
				.add(new StimmtsAussage(
						"Deutschland verbraucht pro Kopf und Jahr mehr als 300 kg Kartoffeln.",
						true,
						"In dieser Menge ist neben dem Gebrauch als Nahrungsmittel auch die Verwendung als Futtermittel, Alkohol und Klebestoff mit eingeschlossen."));
		aussageListe
				.add(new StimmtsAussage(
						"Torfhaltiger Boden ist ideal für den Kartoffelanbau.",
						false,
						"Die besten Kartoffelanbaugebiete sind die sandigen Böden in Nord - und Nordostdeutschland"));
		aussageListe
				.add(new StimmtsAussage(
						"Die erste Taschenuhr wurde \"Nürnberger Trichter\" genannt.",
						false,
						"Sie wurde von Peter Henlein erfunden und \"Nürnberger Ei\" genannt."));
		aussageListe
				.add(new StimmtsAussage(
						"Thomas Edison erfand das Grammophon.",
						false,
						"Der Deutsch-Amerikaner Emil Berliner erfand 1887 das Grammophon, das in der Folge den Phonographen von Edison ablöste."));
		aussageListe
				.add(new StimmtsAussage(
						"Das Römische Reich war das größte in sich geschlossene Reich der Weltgeschichte.",
						true,
						"Es umfasste Teile aller 3 damals bekannten Kontinente: Europa, Afrika, Asien."));
		aussageListe
				.add(new StimmtsAussage(
						"Julius Caesar war der 1. römische Imperator.",
						false,
						"Octavian war der 1. Imperator des Römischen Reiches. Er wurde mit dem Ehrennamen Augustus (=der Erhabene) geehrt."));
		aussageListe
				.add(new StimmtsAussage(
						"Das Forum Romanum in Rom war einst der Platz der Hinrichtungen.",
						false,
						"Das Forum Romanum war ursprünglich ein Marktplatz zwischen zwei der sieben Hügel Roms - später wurde es ein prächtiges Stadtzentrum. "));
		aussageListe
				.add(new StimmtsAussage(
						"Rom war bis zum 15. Jahrhundert die größte Stadt der damals westlichen Welt.",
						false,
						"Konstantinopel war bis zu dieser Zeit die größte Stadt."));
		aussageListe
				.add(new StimmtsAussage(
						"Der Mongolenfürst Dschingis-Khan machte die Chinesische Mauer mittels Steinen dauerhaft.",
						false,
						"Nachdem sich die Chinesen 1368 erfolgreich gegen die Mongolen erhoben hatten, baute die darauf folgende Ming - Dynastie in den 300 Jahren ihrer Herrschaft die Mauer mit Natur- und Ziegelsteinen dauerhaft aus."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Chinesische Mauer erreichte im 6. Jahrhundert eine Länge von etwa 1.000 km.",
						true,
						"Während der Qi-Dynastie wurde in nur 6 Jahren - von 552 bis 557 - eine Verteidigungsmauer aus Lehm an der Nordgrenze errichtet, um China vor eindringenden Barbaren zu schützen."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Vorfahren der Indianer gelangten wahrscheinlich von Sibirien aus nach Amerika.",
						true,
						"Als während der letzten Eiszeit Sibirien und Amerika durch eine Landbrücke im Bereich der Behringstraße verbunden waren, kamen auf diesem Weg die ersten Menschen nach Amerika."));

		aussageListe
				.add(new StimmtsAussage(
						"Ein Kronkorken hat in Amerika 23 und in Deutschland 21 Zähne.",
						true));
		aussageListe
				.add(new StimmtsAussage(
						"Die größte Beere der Welt ist die Himbeere.",
						false,
						"Es ist der Kürbis. Zusatzinfo: Tomaten und Gurken sind biologisch betrachtet Beeren. Erdbeeren und Himbeeren dagegen nicht. Bei Erdbeeren handelt es sich um Sammelnussfrüchte und bei Himbeeren um Sammelsteinfrüchte."));
		aussageListe
				.add(new StimmtsAussage("Haie sind immun gegen Krebs", true));
		aussageListe
				.add(new StimmtsAussage(
						"Donnerstag Abends ist der beliebteste Tag für einen Raubüberfall.",
						false, "Freitag Morgen"));
		aussageListe.add(new StimmtsAussage(
				"Max Mustermann heißt in England Sam Sampleman.", true));
		aussageListe
				.add(new StimmtsAussage(
						"Die Biografie von Karl Dall heißt 'Auge zu und durch'.",
						true,
						"Mitte September 2006 erschien Dalls Autobiografie mit dem humoristischen Titel."));
		aussageListe
				.add(new StimmtsAussage(
						"Es gibt circa 125 Milliarden Freundschaften auf Facebook.",
						true,
						"Facebook ist das größte soziale Netzwerk der Welt und ging im Jahr 2012 an die Börse."));
		aussageListe
				.add(new StimmtsAussage(
						"Noah war im Jahr 2011 der beliebteste deutsche Vorname für einen Jungen.",
						false, "Es war Maximilian"));
		aussageListe
				.add(new StimmtsAussage(
						"Am günsitgsten sind Autos Ende Juli zu kaufen.",
						true,
						"Da die Händler zu dieser Zeit ihre Halbjahresbilanz so noch etwas aufbessern können."));
		aussageListe
				.add(new StimmtsAussage(
						"Handys heißen in Schweden 'ficktelefon'.",
						true,
						"In Deutschland heißt das Handy auch Mobiltelefon. Die zehn größten Hersteller von Handys waren im Jahr 2012 Samsung, Nokia und Apple, danach folgten ZTE, LG, Huawei, TCL, Blackberry (RIM), Motorola und HTC."));
		aussageListe.add(new StimmtsAussage(
				"Im Logo der RAF ist eine Kalaschnikow abgebildet.", false,
				"Es ist eine MP5"));
		aussageListe
				.add(new StimmtsAussage(
						"AB- ist in Deutschland die Häufigste Blutgruppe.",
						false,
						"AB- ist die seltenste Blutgruppe in Deutschland mit nur 1% A+ ist die Häufigste mit 37%"));
		aussageListe
				.add(new StimmtsAussage(
						"Der VW-Golf ist das meistverkaufte Auto der Welt.",
						false,
						"Der Toyota Corolla war es bis 2012, bis er vom Ford Focus abgelöst wurde. Einen wesentlichen Bestandteil am Erfolg des Focus, der in mehr als 100 Ländern verkauft wird, hat China. Allein 300 000 Verkäufe gingen 2012 auf das Konto des Riesen-Reiches."));
		aussageListe
				.add(new StimmtsAussage(
						"In öffentlichen Toiletten wird das Klo, das dem Ausgang am nächsten liegt, am wenigsten benutzt.",
						true, "Das Mittlere wird am häufigsten benutzt."));
		aussageListe
				.add(new StimmtsAussage(
						"In New York leben mehr Italiener als in Rom.",
						true,
						"In New York leben mehr Italiener als in Rom, mehr Iren als in Dublin und mehr Schwarze als in jeder anderen Stadt der Welt."));
		aussageListe
				.add(new StimmtsAussage(
						"Der statistisch sicherste Platz in einem Auto ist der Beifahrerplatz.",
						false,
						"Tatsächlich ist der statistisch sicherste Platz in einem Auto, hinten in der Mitte."));
		aussageListe
				.add(new StimmtsAussage(
						"Johnny Walker ist der Erfinder des Schnaps.",
						false,
						"Der Erfinder des Schnaps war Jim Beam und war eigentlich Deutscher und hieß Jakob Böhm."));
		aussageListe
				.add(new StimmtsAussage(
						"Regenwürmer kommen bei Regen an die Oberfläche, um nicht zu ertrinken.",
						true,
						"Bei Regen füllen sich ihre Wohnhöhlen mit Wasser und sie würden ertrinken, wenn sie nicht an die Oberfläche kommen würden"));
		aussageListe
				.add(new StimmtsAussage(
						"Jeder Kontinent weist mindestens eine Stadt mit dem Namen Rom auf.",
						true,
						"Die bekannteste Stadt mit diesem Namen ist die italienische Hauptstadt."));
		aussageListe
				.add(new StimmtsAussage(
						"In Kalifornien ist es gesetzlich verboten, einen Schmetterling mit dem Tode zu bedrohen.",
						true,
						"In Pacific Grove, Kalifornien, kann das Töten oder Belästigen eines Schmetterling mit bis zu $1000 Strafe geahndet werden."));
		aussageListe
				.add(new StimmtsAussage(
						"Der Kölner Dom hat den höchsten Kirchturm weltweit.",
						false,
						"Das Ulmer Münster hat mit 161,5 Metern den höchsten Kirchturm der Welt."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Blackbox eines Flugzeuges ist schwarz.", false,
						"Sie ist orange, dass man sie nach einem Flugzeugabsturz besser findet!"));
		aussageListe
				.add(new StimmtsAussage(
						"Die Blackbox eines Flugzeuges ist schwarz.", false,
						"Sie ist orange, dass man sie nach einem Flugzeugabsturz besser findet!"));
		aussageListe
				.add(new StimmtsAussage(
						"Der Blauwal ist der größte Fisch der Erde", false,
						"Der Blauwal ist ein Säugetier, der größte Fisch ist hingegen der Walhai"));
		aussageListe.add(new StimmtsAussage("79+33+10-3=129 ", false,
				"Die Rechnung ergibt 119"));
		aussageListe.add(new StimmtsAussage("33+79+17= 129 ", true));
		aussageListe
				.add(new StimmtsAussage(
						"Patrick Swayze wurde 1991 zum 'Sexiest Man Alive' gekürt ",
						true,
						"Sexiest Man Alive ist eine internationale Auszeichnung, die jährlich vom US-amerikanischen People Magazine verliehen wird."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Flagge Schwedens hat ein blaues Kreuz auf gelbem Hintergrund",
						false, "Es ist genau umgekehrt"));
		aussageListe
				.add(new StimmtsAussage(
						"Google ist das größte börsennotierte Aktienunternehmen (2013) ",
						false,
						"Apple ist das größte notierte Aktienunternehmen der Welt. Google belegt hingegen Platz 25."));
		aussageListe
				.add(new StimmtsAussage(
						"Indien ist im „British Commonwealth of Nations“ ",
						true,
						"Das Commonwealth of Nations ist eine Vereinigung unabhängiger Staaten, die heute als Nachfolger des British Empire gesehen werden kann."));
		aussageListe.add(new StimmtsAussage(
				"Europa hat mehr Einwohner als Südamerika", true,
				"Europa hat knapp doppelt so viele Einwohner wie Südamerika."));
		aussageListe.add(new StimmtsAussage(
				"Die Amtssprache von Belize ist Französisch ", false,
				"Es ist Englisch"));
		aussageListe
				.add(new StimmtsAussage(
						"'The Sun' ist eine englische Tageszeitung ",
						true,
						"The Sun ist eine täglich erscheinende englische Boulevardzeitung. Sie zählt zu den einflussreichsten Zeitungen des Landes"));
		aussageListe
				.add(new StimmtsAussage(
						"Lebron James wechselte 2010 zu den Miami Heat ",
						true,
						"James ist womöglich der Beste Basketballer dieses Jahrhunderts und wurde bis 2013 schon viermal zum MVP gewählt."));
		aussageListe
				.add(new StimmtsAussage(
						"Die Gebrüder Grimm hießen mit Vornamen Richard und August ",
						false,
						"Die Märchenschriftsteller hießen mit Vornamen Jacob und Wilhelm."));
		aussageListe
				.add(new StimmtsAussage(
						"Pink Lady ist der Name einer Apfelsorte ", true,
						"Vermarktet wird der Apfel mit einem Aufkleber, der ein pinkes Herz enthält."));
		aussageListe
				.add(new StimmtsAussage(
						"Angela Merkel ist die erste Frau im Amt des deutschen Bundespräsidenten ",
						false,
						"Merkel ist Parteimitglied der CDU und die erste deutsche Bundeskanzlerin."));
		aussageListe
				.add(new StimmtsAussage(
						"S ist der 17 Buchstabe des Alphabets ",
						false,
						"S  ist der 19. Buchstabe des lateinischen Alphabets und ein Konsonant. Der Buchstabe S hat in deutschen Texten eine durchschnittliche Häufigkeit von 7,27 %. Er ist damit der vierthäufigste Buchstabe in deutschen Texten."));
		aussageListe
				.add(new StimmtsAussage(
						"S ist der 19 Buchstabe des Alphabets ",
						true,
						"Richtig S  ist der 19. Buchstabe des lateinischen Alphabets und ein Konsonant. Der Buchstabe S hat in deutschen Texten eine durchschnittliche Häufigkeit von 7,27 %. Er ist damit der vierthäufigste Buchstabe in deutschen Texten."));
		aussageListe
				.add(new StimmtsAussage(
						"'h.M._ ist die Abkürzung für 'herrschende Meinung' ",
						true,
						"wahr Der Begriff der herrschenden Meinung bezeichnet im akademischen und insbesondere juristischen Kontext die in einem Diskurs oder zu einer konkreten Streitfrage vorwiegend eingenommene Position."));
		aussageListe
				.add(new StimmtsAussage(
						"Seit der Erfindung des Computers ist der Papierverbrauch gesunken",
						false,
						"Durch den Drucker wurde der Papierkonsum sogar noch gesteigert. Im Vergleich zum Jahr 1990 ist der Verbrauch um bis zu 20% gestiegen."));
		aussageListe
				.add(new StimmtsAussage(
						"Wenn man von Westen nach Osten fliegt ist der Jetlag schlimmer als umgekehrt",
						true));
		aussageListe
				.add(new StimmtsAussage(
						"Man kann am Geweih des Hirsches sein Alter ablesen ",
						false,
						"Die Größe des Geweihs wächst zwar zunächst von Jahr zu Jahr. Ab 8 Jahren setzt dieses aber wieder zurück/ wird wieder kleiner. "));

		aussageListe
				.add(new StimmtsAussage(
						"Auf der Flagge der Dominikanischen Republik ist eine Bibel abgebildet ",
						true,
						"Die Flagge der Dominikanischen Republik ist die einzige auf der Welt, die eine Bibel zeigt."));

		aussageListe
				.add(new StimmtsAussage(
						"New York und Neapel liegen auf dem gleichen Breitengrad",
						true,
						"Beide Städte liegen auf 40° nördlicher Breite. Das Klima ist jedoch komplett verschieden."));

		aussageListe.add(new StimmtsAussage(
				"Tel Aviv heißt übersetzt unfruchtbares Land", false,
				"Es heißt übersetzt Frühlingshügel."));

		aussageListe
				.add(new StimmtsAussage(
						"'Mam(m)a' (für Mutter) kommt aus dem Lateinischen und heißt Brust.",
						true,
						"Die Anrede für eine Mutter ist regional sehr unterschiedlich, doch ist im deutschen Sprachraum die Form 'Mama', 'Mami' oder 'Mutti' am häufigsten. Die Wörter werden dabei nicht nur von Kindern verwendet, sondern auch viele Erwachsene betiteln so ihre Mutter."));

		aussageListe.add(new StimmtsAussage("Das Nike-Symbol heißt 'Swish'",
				false, "Das Symbol heißt 'swoosh'"));

		aussageListe
				.add(new StimmtsAussage(
						"Jeder Mensch ist ein Primat",
						true,
						"Primaten werden in die beiden Unterordnungen der Feuchtnasenaffen und Trockennasenaffen eingeteilt, wobei letztere auch den Homo sapiens mit einschließen."));

		aussageListe
				.add(new StimmtsAussage(
						"Paris ist eine sogenannte Mega-City",
						false,
						"Paris hat nur rund 2 Millionen Einwohner. Laut Definition gilt eine Stadt aber erst ab 10 Millionen Einwohnern als Megastadt."));

		aussageListe.add(new StimmtsAussage(
				"Der Hippocampus liegt im Ellebogen", false,
				"Der Hippocampus ist ein Teil des Gehirns."));

		aussageListe
				.add(new StimmtsAussage(
						"Am 21 Dezember geht die Sonne in der Arktis nicht unter",
						false,
						" Am 21. Dezember geht die Sonne in der Antarktis nicht unter und in der ARKTIS nicht auf. Am 21. Juni ist es umgekehrt."));

		aussageListe.add(new StimmtsAussage(
				"Die Texte der Tora kann man in der Bibel wiederfinden", true,
				"Die Tora ist ein Teil des Alten Testaments."));

		aussageListe
				.add(new StimmtsAussage(
						"Das Antiteilchen des Elektrons heißt Positron", true,
						"Antiteilchen haben die gleiche Masse aber entgegengestzte Ladung."));

		aussageListe
				.add(new StimmtsAussage(
						"Rotes Licht hat eine Wellenlänge von 6mm",
						false,
						"Tatsächlich hat rotes Licht eine Wellenlänge von rund 6nm (Nano Meter). Also 0,0000006mm"));

		aussageListe.add(new StimmtsAussage(
				"Die Machtergreifung Hitlers war 1933", true,
				"Am 30 Januar 1933 wurde Hitler zum Reichskanzler ernannt."));

		aussageListe
				.add(new StimmtsAussage(
						"Antarktis ist die größte Wüste der Welt",
						true,
						"Wüste ist der Begriff für ein vegetationsloses Gebiet. Antarktis macht die größte zusammenhängende vegetationsfreie Zone der Welt aus."));

		aussageListe.add(new StimmtsAussage(
				"Die Telefonvorwahl für Österreich ist die 18", false,
				"Die Österreichische Vorwahl ist die 43"));

		aussageListe
				.add(new StimmtsAussage(
						"Das Berliner Olympiastadion ist das größte Fußballstadion Deutschlands",
						true,
						"Das Berliner Olympiastadion ist das zweitgrößte Deutschlands mit knapp 75.000 Sitzplätzen. Das Westfalenstadion in Dortmund gibt rund 80.000 Zuschauern Platz."));

		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));
		// aussageListe.add(new StimmtsAussage("", false, ""));

		/*
		 * aussageListe.add(new StimmtsAussage("Leo ist ein Kater.",true,
		 * "Zugegeben, es gibt mehr als einen Leo. Aber der den ich meine (Lieutenant)"
		 * +" ist tatsächlich ein Kater."));
		 */
		aussageListe
				.add(new StimmtsAussage(
						"Elefanten werden an die 120 Jahre alt", false,
						"Der älteste Elefant lebte im Zoo von Taipeh und wurde „nur“ 86 Jahre alt."));
	}

	public Stimmts(Player[] player, Modus modus, int globalGameID) {
		super(player, defaultNumOfRounds, modus, globalGameID);
		initAussagen();
		vermutung = new boolean[player.length];
		answerGiven = new boolean[player.length];
		resetVermutungen();
		initGUI();
		registerBuzzerActions();
	}

	private void registerBuzzerActions() {
		for (int i = 0; i < spielerZahl; i++) {
			Action actionTrue = new BuzzerAction(i, true);
			String actionName = "Truebuzzer" + i;
			// Register keystroke
			hauptbereichPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(trueBuzz[i], 0), actionName);
			// Register action
			hauptbereichPanel.getActionMap().put(actionName, actionTrue);

			Action actionFalse = new BuzzerAction(i, false);
			String actionName2 = "Falsebuzzer" + i;
			// Register keystroke
			hauptbereichPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(falseBuzz[i], 0), actionName2);
			// Register action
			hauptbereichPanel.getActionMap().put(actionName2, actionFalse);
		}

	}

	private void resetVermutungen() {
		for (int i = 0; i < spielerZahl; i++) {
			vermutung[i] = true;
			answerGiven[i] = false;
		}
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		spielBereichPanel.add(hauptbereichPanel);
		addAussageLabel();
		addCountdown();
	}

	private void addAussageLabel() {
		aussageLabel = new JLabel("Aussagee");
		aussageLabel.setFont(X.BUTTON_FONT);
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(aussageLabel, BorderLayout.CENTER);
	}

	private void addCountdown() {
		countdown = new Countdown(timeProAussage);
		countdown.addChangeManager(new ChangeManager() {
			@Override
			public void change() {
				roundEnd();
			}
		});
		hauptbereichPanel.add(countdown, BorderLayout.SOUTH);
	}

	public void roundEnd() {
		letzteAussageWahr = aussageListe.get(current).isWahr();
		if (modus == Modus.SOLO) {
			// Boten Ana wählt immer das Gegenteil des Spielers
			vermutung[1] = !vermutung[0];
		}
		winnerIDs = new HashSet<Integer>();
		for (int i = 0; i < spielerZahl; i++) {
			if (!answerGiven[i])
				vermutung[i] = !letzteAussageWahr;
			if (vermutung[i] == letzteAussageWahr)
				winnerIDs.add(i);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
	}

	private void verbuchePunkte(Set<Integer> winnerIDs) {
		if (winnerIDs.size() < myPlayer.length) {
			for (int id : winnerIDs) {
				creds[id].earnsCredit(1);
				myPlayer[id].gameCredit++;
			}
		}
	}

	private String getWinnerText(Set<Integer> winnerIDs) {
		String winner = "";
		if (winnerIDs.size() >= myPlayer.length || winnerIDs.size() == 0) {
			winner = "niemanden";
		} else {
			int c = 0;
			for (int id : winnerIDs) {
				String trenner = "";
				if (c == winnerIDs.size() - 3)
					trenner = ", ";
				if (c == winnerIDs.size() - 2)
					trenner = " und ";
				winner += myPlayer[id].name + trenner;
				c++;
			}
		}
		return winner;
	}

	private void nextRound() {
		if (modus == Modus.TEAM)
			changeActivePlayers();
		letzteAussageWahr = false;
		resetVermutungen();
		current = nextRandom(aussageListe.size());
		if (current == -1) {
			abbruch();
			// TODO besser handhaben!
		} else {
			aussageLabel.setText(aussageListe.get(current).getAussage());
			countdown.start();
		}
	}

	private class BuzzerAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private int playerID;
		private boolean stimmt;

		public BuzzerAction(int playerID, boolean stimmt) {
			this.playerID = playerID;
			this.stimmt = stimmt;
		}

		public void actionPerformed(ActionEvent e) {
			answerGiven[playerID] = true;
			vermutung[playerID] = stimmt;
		}
	}

	@Override
	public void settingsChanged() {
		updateCreds();
		countdown.setSecs(timeProAussage);
	}

	@Override
	public void start() {
		nextRound();
	}

	public void nowVisible() {
		instance.changeBackground("/media/stimmts/stimmts.jpg");
	}

	@Override
	public void openRoundDialog(String winner) {
		RoundDialog rd = new RoundDialog(this, winner);
		instance.showDialog(rd);
	}

	@Override
	public void goBack() {
		if (!isOver())
			nextRound();
	}

	@Override
	public void openSettingsDialog() {
		instance.showDialog(new StimmtsSettingsDialog(this));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new StimmtsDetailsDialog(this));
	}

	@Override
	public void openInfoDialog() {
		instance.showDialog(new InfoDialog(aussageListe.get(current).getInfo()));
	}

	@Override
	public void abbruch() {
		countdown.stop();
		super.abbruch();
	}
}
