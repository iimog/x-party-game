package games.world;

import games.Game;
import games.Modus;
import games.PC;
import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import util.SpielListen;


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
public class World extends Game implements PC{
	private static final long serialVersionUID = -6882289976402143446L;
	static String gameName = "Wo";
	public static String getGameName(){
		return gameName;
	}
	public static String shortInfo="Kennst du die Erde wie deine Westentasche?";
	private static final int GAME_ID = SpielListen.WO;
	public int getGameID(){
		return GAME_ID;
	}
	public static int defaultNumOfRounds = 5;
	private JButton zoomButton;
	private JButton[] myTurnButton;
	private JPanel schalterPanel;
	private JLabel frageLabel;
	private Bildschirm bildschirm1;
	private JPanel hauptbereichPanel;
	public Point[] guess;
	public boolean[] turnDone;
	public double[] distance;
	public int whosTurn=-1;
	private boolean vollbild = true;
	private int centerX, centerY;
	private int current;
	int last;
	int tol=5; // innerhalb von 5 Pixeln ist der richtige Ort getroffen
	public int numOfPics=30;
	int toleranz = 500;
	boolean toleranzOn = true;

	public String[] question = new String[numOfPics];
	public Point[] answer = new Point[numOfPics];
	public String[] info = new String[numOfPics];
	private String path = "media/wo/";
	String erdeSmall = path + "erde.jpg";
	String erdeBig = path + "erdeB.jpg";
	{
		question[0] = 	"Wo liegt Berlin?";
		answer[0] = 	new Point(1719,300);
		info[0] = 		"Berlin ist die Hauptstadt von Deutschland";

		question[1] = 	"Wo liegt Paris?";
		answer[1] = 	new Point(1649,336);
		info[1] = 		"Paris ist die Hauptstadt von Frankreich";

		question[2] = 	"Wo liegt Rom?";
		answer[2] = 	new Point(1727,406);
		info[2] = 		"Rom ist die Hauptstadt von Italien";

		question[3] = 	"Wo liegt das \"Aloha Stadium\"?";
		answer[3] = 	new Point(279,621);
		info[3] = 		"Das Aloha Stadium liegt auf der Insel Oahu (Hawaii). " +
		"Hier wird alljährlich der Pro Bowl (American Football) ausgetragen.";

		question[4] = 	"Wo tragen die Arizona Cardinals ihre Heimspiele aus?";
		answer[4] = 	new Point(718,494);
		info[4] = 		"Die Arizona Cardinals sind ein American Football Team aus der " +
		"Stadt Phoenix.";

		question[5] = 	"Wo befindet sich die sogenannte goldene Stadt?";
		answer[5] = 	new Point(1725,328);
		info[5] = 		"Die Stadt Prag in Tschechien wird als goldene Stadt bezeichnet! ";

		question[6] = 	"Wo befindet sich der höchste Berg der Welt?";
		answer[6] = 	new Point(2434,533);
		info[6] = 		"Der höchste Berg ist der Mount everest er liegt im Himalaya und ist 8848 m hoch!";

		question[7] = 	"Wo befindet sich das Wrack der untergegangenen Titanic?";
		answer[7] = 	new Point(1189,423);
		info[7] = 		"1912 sinkt die Titanic im atlantischen Ozean und nur jede 3. Person überlebt!";

		question[8] = 	"Wo wurde Deutschland 1990 Fußballweltmeister?";
		answer[8] = 	new Point(1727,406);
		info[8] = 		"Austragungsort des Finals war das Stadio Olympico, das in Rom steht!";

		question[9] = 	"Wo befindet sich die Insel Mauritius?";
		answer[9] = 	new Point(2134,1076);
		info[9] = 		"Mauritius ist ein Urlaubsparadies im indischen Ozean östlich von Madagaskar!";

		question[10] = 	"Wo befindet sich der nördlichste Teil des Baikalsee - Die Perle Sibiriens?";
		answer[10] = 	new Point(2326,277);
		info[10] = 		"Der Baikalsee ist der größte See der Erde und ist erstreckt sich vom Norden nach Süden mit einer Länge, die mit der Deutschlands vergleichbar ist! P.s Punkt für MO ";

		question[11] = 	"Wo befindet sich der Hauptsitz der Vereinten Nationen (UNO)?";
		answer[11] = 	new Point(1078,419);
		info[11] = 		"Das UN-Hauptquartier in New York ist der wichtigste Standort und Hauptsitz der Vereinten Nationen, gelegen am United Nations Plaza in Manhattan.";

		question[12] = 	"Jeder hat schonmal von der Katastrophe von Tschernobyl gehört aber wo genau liegt eigentlich das Kraftwerk?";
		answer[12] = 	new Point(1839,322);
		info[12] = 		"Das heute stillgelegte Kernkraftwerk Tschernobyl befindet sich im Norden der Ukraine nahe der ukrainisch-weißrussischen Grenze.";

		question[13] = 	"Wo liegt Bali?";
		answer[13] = 	new Point(2664,948);
		info[13] = 		"Bali ist eine seit 1949 zu Indonesien gehörende Insel und bildet die gleichnamige Provinz dieses Staates.";

		question[14] = 	"Wo liegt New York?";
		answer[14] = 	new Point(1078,419);
		info[14] = 		"New York ist eine Weltstadt an der Ostküste der Vereinigten Staaten. Die Stadt liegt im Bundesstaat New York und ist mit mehr als acht Millionen Einwohnern die größte Stadt der USA.";

		question[15] = 	"Wo befindet sich das Sagenumwobene Stonehenge?";
		answer[15] = 	new Point(1628,319);
		info[15] = 		"Es besteht aus einer Grabenanlage, die eine Megalithstruktur umgibt, welche wiederum aus mehreren konzentrischen Steinkreisen gebildet wird.";

		question[16] = 	"Wo befinden sich die Niagara Falls?";
		answer[16] = 	new Point(1046,398);
		info[16] = 		"Die Niagara Falls sind Wasserfälle des Niagara-Flusses an der Grenze zwischen dem US-amerikanischen Bundesstaat New York und der kanadischen Provinz Ontario.";

		question[17] = 	"Wo befinden sich die sogenannten Südshettland Inseln?";
		answer[17] = 	new Point(1310,1489);
		info[17] = 		"Die Südlichen Shetlandinseln sind eine subantarktische Inselgruppe im Südlichen Ozean, westlich der Antarktischen Halbinsel gelegen.";

		question[18] = 	"Wo liegt die Hauptstadt Australiens?";
		answer[18] = 	new Point(2795,1255);
		info[18] = 		"die Hauptstadt Australiens ist Canberra und sie liegt nahe der Süd-östlichen Küste Australiens.";

		question[19] = 	"Wo liegt die Hauptstadt Tschechiens?";
		answer[19] = 	new Point(1725,328);
		info[19] = 		"Prag ist die Hauptstadt und zugleich bevölkerungsreichste Stadt der Tschechischen Republik.";
		
		question[20] = 	"Wo fand das Finale der Fußball WM 2010 statt?";
		answer[20] = 	new Point(1872,1140);
		info[20] = 		"Das Finale fand im Soccer City Stadion in Johannesburg statt";
		
		question[21] = 	"Wo ist die größte Stadt Chinas?";
		answer[21] = 	new Point(2627,506);
		info[21] = 		"Shanghai ist mit Abstand die bevölkerungsreichste Stadt Chinas. Die Metropole am Jangtsekiang repräsentiert mit ihrer beeindruckenden Skyline das moderne China und sein Reichtum.";
		
		question[22] = 	"Wo ist der Titicacasee?";
		answer[22] = 	new Point(1028,1028);
		info[22] = 		"Der Titicaca-See (spanisch: Lago Titicaca) ist Südamerikas größter See; mit einer Fläche von 8.288 Quadratkilometern ist er etwa 15 Mal so groß wie der Bodensee.";
		
		question[23] = 	"Wo liegt das Atomkraftwerk Fukushima?";
		answer[23] = 	new Point(2740,456);
		info[23] = 		"Das Kernkraftwerk Fukushima war mit sechs Reaktorblöcken und bis zu 4,5 Gigawatt elektrischer Nettoleistung eines der leistungsstärksten Kernkraftwerke in Japan.";
		
		question[24] = 	"Osama Bin Laden versteckte sich jahrelang in einer Villa in Abbottabad, bis er 2011 dort erschossen wurde. Wo liegt Abbottabad?";
		answer[24] = 	new Point(2232,500);
		info[24] = 		"Osama Bin Laden wurde 55 km nördlich der Hauptstadt Islamabad von US-Spezialeinheiten erschossen.";		
		
		question[25] = 	"Wo fanden die Olympischen Spiele von 2004 statt?";
		answer[25] = 	new Point(1820,447);
		info[25] = 		"2004 kehrten die Spiele an ihren Ursprungsort zurück...nach Athen.";
			
		question[26] = 	"Wo wurden die Olympischen Spiele von '96 ausgetragen?";
		answer[26] = 	new Point(963,503);
		info[26] = 		"Die Olympischen Sommerspiele von 1996 fanden im US-Amerikanischen Atlanta statt.";
		
		question[27] = 	"Wo befindet sich die weltbekannte Harbor Bridge?";
		answer[27] = 	new Point(2846,1221);
		info[27] = 		"Die Harbor Bridge ist in Sydney direkt neben der weltbekannten Oper von Sydney.";
		
		question[28] = 	"Wo befindet sich der Berg (Corcovado) auf dem die estatua Cristo Redentor - Christustatue steht?";
		answer[28] = 	new Point(1260,1107);
		info[28] = 		"Die Statue ist das Wahrzeichen von Rio de Janeiro.";
		
		question[29] = 	"Wo hat der Dalai Lama sein Zuhause?";
		answer[29] = 	new Point(2316,542);
		info[29] = 		"Im Jahre 1959 floh der gegenwärtige Dalai Lama am 17. März während des Tibetaufstands ins indische Exil nach Dharamsala.";
	}

	public World(Player[] myPlayer, Modus modus) {
		this(myPlayer, defaultNumOfRounds, modus);
	}
	public World(Player[] player, int numOfRounds, Modus modus) {
		super(gameName, player, numOfRounds, modus);
		if(modus == Modus.SOLO){
			spielerZahl--;
			toleranzOn = true;
			toleranz = 50;
		}
		initArrays();
		initGUI();
		nextRound();
	}

	private void initArrays() {
		myTurnButton = new JButton[spielerZahl];
		guess = new Point[spielerZahl];
		turnDone = new boolean[spielerZahl];
		distance = new double[spielerZahl];
	}
	private void bildschirm1MouseClicked(MouseEvent evt) {
		if(evt.isPopupTrigger()){
			if(vollbild){
				centerX = Math.round(evt.getPoint().x*3.2F);
				centerY = Math.round(evt.getPoint().y*3.2F);
				passeCenterKoordinatenAmRandAn();
				vollbild = false;
			}
			else{
				centerX+=evt.getPoint().x-512;
				centerY+=evt.getPoint().y-264;
				passeCenterKoordinatenAmRandAn();
			}
			bildschirm1.showPicPart(erdeBig, new Point(centerX,centerY));
		}
		else if(whosTurn>=0 && whosTurn<spielerZahl){
			bildschirm1.drawDot(evt.getX(), evt.getY(), myPlayer[whosTurn].farbe);
			if(vollbild){
				guess[whosTurn] = vollbildConverter(evt.getPoint());
			}
			else{
				guess[whosTurn] = zoombildConverter(evt.getPoint());
			}
			turnDone[whosTurn] = true;
		}
		bildschirm1.repaint();
	}

	private void passeCenterKoordinatenAmRandAn() {
		if(centerX<512)centerX=512;
		if(centerY<264)centerY=264;
		if(centerX>3276-512)centerX=3276-512;
		if(centerY>1689-264)centerY=1689-264;
	}
	@Override
	public String getShortInfo(){
		return shortInfo;
	}
	
	class ButtonListener implements ActionListener{
		int index;
		public ButtonListener(int index){
			this.index = index;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			markPlace(index);
		}
	}

	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			spielBereichPanel.setLayout(thisLayout);
			{
				hauptbereichPanel = new JPanel();
				BorderLayout hauptbereichPanelLayout = new BorderLayout();
				spielBereichPanel.add(hauptbereichPanel, BorderLayout.CENTER);
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				{
					bildschirm1 = new Bildschirm(erdeSmall);
					bildschirm1.setMinimumSize(new Dimension(1024,528));
					hauptbereichPanel.add(bildschirm1, BorderLayout.CENTER);
					bildschirm1.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent evt) {
							bildschirm1MouseClicked(evt);
						}
					});
				}
				{
					frageLabel = new JLabel();
					hauptbereichPanel.add(frageLabel, BorderLayout.NORTH);
					frageLabel.setText("Frage...");
					frageLabel.setFont(STANDARD_FONT);
				}
				{
					schalterPanel = new JPanel();
					GridLayout schalterPanelLayout = new GridLayout(1, spielerZahl);
					schalterPanelLayout.setHgap(5);
					schalterPanelLayout.setVgap(5);
					schalterPanelLayout.setColumns(spielerZahl);
					schalterPanelLayout.setRows(1);
					schalterPanel.setLayout(schalterPanelLayout);
					hauptbereichPanel.add(schalterPanel, BorderLayout.SOUTH);
					for(int i=0; i<spielerZahl; i++){
						myTurnButton[i] = new JButton();
						schalterPanel.add(myTurnButton[i]);
						myTurnButton[i].setText("Ort festlegen");	
						myTurnButton[i].addActionListener(new ButtonListener(i));
						myTurnButton[i].setBackground(myPlayer[i].farbe);
					}
				}
				updateCreds();
			}
			{
				zoomButton = new JButton();
				spielBereichPanel.add(zoomButton, BorderLayout.SOUTH);
				zoomButton.setText("Vollbild");
				zoomButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						zoomButtonActionPerformed(evt);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void markPlace(int index){
		if(index==whosTurn){
			if(!turnDone[index])return;
			bildschirm1.drawDot(-10, -10, Color.BLACK);
			turnDone[index] = true;
			myTurnButton[index].setEnabled(false);
			myTurnButton[index].setText("Tip abgegeben");
			whosTurn=-1;
			bildschirm1.changePic(erdeSmall);
			vollbild=true;
			boolean alleFertig = true;
			for(int i=0; i<spielerZahl; i++){
				if(!turnDone[i]){
					myTurnButton[i].setEnabled(true);
					alleFertig = false;
				}
			}
			if(alleFertig){
				roundEnd();
			}
		}
		else{
			whosTurn = index;
			myTurnButton[index].setText("Fertig");
			for(int i=0; i<spielerZahl; i++){
				if(i==index)continue;
				myTurnButton[i].setEnabled(false);
			}
		}
	}
	public void nextRound(){
		current = nextRandom(numOfPics);
		if(current==-1){
			showMessage("Alle Fragen verbraucht");
			abbruch();
			return;
		}
		if(modus == Modus.TEAM){
			changeActivePlayers();
		}
		frageLabel.setText(question[current]);
		whosTurn = -1;
		for(int i=0; i<spielerZahl; i++){
			turnDone[i]=false;
			myTurnButton[i].setEnabled(true);
			myTurnButton[i].setText("Ort festlegen");
		}
		
	}
	@Override
	public void nowVisible(){
		instance.changeBackground("media/wo/Flaggen.jpg");
	}

	@Override
	public void openDetailsDialog(){
		instance.showDialog(new WorldDetailsDialog(this));
	}

	@Override
	public void openInfoDialog(){
		instance.showDialog(new games.dialogeGUIs.InfoDialog(info[last]));
	}
	@Override
	public void openRoundDialog(String winner){
		instance.showDialog(new games.dialogeGUIs.RoundDialog(this,winner));
	}
	@Override
	public void openSettingsDialog(){
		instance.showDialog(new WorldSettingsDialog(this));
	}
	//TODO Spielende
	public void roundEnd(){
		last = current;
		for(int i=0; i<spielerZahl; i++){
			distance[i] = guess[i].distance(answer[last]);
			if(distance[i]<=tol)distance[i]=0;
		}
		int winnerID = whoWon();
		if(winnerID == -1){
			winner = "Keinen";
		}
		else{
			winner = myPlayer[winnerID].name;
			myPlayer[winnerID].gameCredit++;
			creds[winnerID].earnsCredit(1);
		}
		openRoundDialog(winner);
		nextRound();
	}
	
	public int whoWon(){
		int winnerID = -1;
		double miniDist = Double.MAX_VALUE;
		for(int i=0; i<spielerZahl; i++){
			double dist = distance[i];
			if(dist==miniDist){
				winnerID = -1;
			}
			if(dist<miniDist){
				miniDist = dist;
				winnerID = i;
			}
		}
		if(toleranzOn && miniDist>toleranz){
			winnerID=-1;
		}
		if(modus == Modus.SOLO && winnerID == -1)winnerID = 1;
		return winnerID;
	}
	@Override
	public void settingsChanged(){
		updateCreds();
	}
	@Override
	public void start(){
		// Code zum Spielstart-Handling
	}
	/**
	 * Rechnet die Koordinaten eines Punktes im Vollbild auf die Koordinaten
	 * im großen Bild um
	 * @param p
	 * @return
	 */
	private Point vollbildConverter(Point p){
		int x = Math.round(p.x*3.2F);
		int y = Math.round(p.y*3.2F);
		return new Point(x,y);
	}
	/**
	 * Rechnet die Koordinaten eines Punktes im 'Bildschirm' auf die Koordinaten im
	 * großen Bild um
	 * @param p
	 * @return
	 */
	private Point zoombildConverter(Point p){
		int x=centerX+p.x-512;
		int y=centerY+p.y-264;
		return new Point(x,y);
	}
	private void zoomButtonActionPerformed(ActionEvent evt) {
		bildschirm1.changePic(erdeSmall);
		vollbild=true;
		bildschirm1.repaint();
	}
}
