package games.world;

import games.Game;
import games.Modus;
import games.PC;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.GeoPosition;
import sample4_fancy.FancyWaypointRenderer;

import player.Player;
import sample4_fancy.MyWaypoint;

public class World extends Game implements PC{
	private static final long serialVersionUID = -6882289976402143446L;
	static String gameName = "Wo";
	public static String getGameName(){
		return gameName;
	}
	public static int defaultNumOfRounds = 5;
	private JButton zoomButton;
	private JButton[] myTurnButton;
	private JPanel schalterPanel;
	private JLabel frageLabel;
	private JPanel hauptbereichPanel;
	public GeoPosition[] guess;
	public boolean[] turnDone;
	public double[] distance;
	public int whosTurn=-1;
	private int current;
	int last;
	int tol=5; // innerhalb von 5km Pixeln ist der richtige Ort getroffen
	public int numOfPics=4;
	int toleranz = 500;
	boolean toleranzOn = true;

	public String[] question = new String[numOfPics];
	public GeoPosition[] answer = new GeoPosition[numOfPics];
	public String[] info = new String[numOfPics];
	private JXMapKit mapViewer;
	{
		question[0] = 	"Wo liegt Berlin?";
		answer[0] = 	new GeoPosition(52.520008,13.404954);
		info[0] = 		"Berlin ist die Hauptstadt von Deutschland";

		question[1] = 	"Wo liegt Paris?";
		answer[1] = 	new GeoPosition(48.856613,2.352222);
		info[1] = 		"Paris ist die Hauptstadt von Frankreich";

		question[2] = 	"Wo liegt Rom?";
		answer[2] = 	new GeoPosition(41.890560,12.494270);
		info[2] = 		"Rom ist die Hauptstadt von Italien";

		question[3] = 	"Wo liegt das \"Aloha Stadium\"?";
		answer[3] = 	new GeoPosition(21.374170, -157.932060);
		info[3] = 		"Das Aloha Stadium liegt auf der Insel Oahu (Hawaii). " +
		"Hier wird alljährlich der Pro Bowl (American Football) ausgetragen.";
		/*
		question[4] = 	"Wo tragen die Arizona Cardinals ihre Heimspiele aus?";
		answer[4] = 	new GeoPosition(718,494);
		info[4] = 		"Die Arizona Cardinals sind ein American Football Team aus der " +
		"Stadt Phoenix.";

		question[5] = 	"Wo befindet sich die sogenannte goldene Stadt?";
		answer[5] = 	new GeoPosition(1725,328);
		info[5] = 		"Die Stadt Prag in Tschechien wird als goldene Stadt bezeichnet! ";

		question[6] = 	"Wo befindet sich der höchste Berg der Welt?";
		answer[6] = 	new GeoPosition(2434,533);
		info[6] = 		"Der höchste Berg ist der Mount Everest er liegt im Himalaya und ist 8848 m hoch!";

		question[7] = 	"Wo befindet sich das Wrack der untergegangenen Titanic?";
		answer[7] = 	new GeoPosition(1189,423);
		info[7] = 		"1912 sinkt die Titanic im atlantischen Ozean und nur jede 3. Person überlebt!";

		question[8] = 	"Wo wurde Deutschland 1990 Fußballweltmeister?";
		answer[8] = 	new GeoPosition(1727,406);
		info[8] = 		"Austragungsort des Finals war das Stadio Olympico, das in Rom steht!";

		question[9] = 	"Wo befindet sich die Insel Mauritius?";
		answer[9] = 	new GeoPosition(2134,1076);
		info[9] = 		"Mauritius ist ein Urlaubsparadies im indischen Ozean östlich von Madagaskar!";

		question[10] = 	"Wo befindet sich der nördlichste Teil des Baikalsee - Die Perle Sibiriens?";
		answer[10] = 	new GeoPosition(2326,277);
		info[10] = 		"Der Baikalsee ist der größte See der Erde und ist erstreckt sich vom Norden nach Süden mit einer Länge, die mit der Deutschlands vergleichbar ist! P.s Punkt für MO ";

		question[11] = 	"Wo befindet sich der Hauptsitz der Vereinten Nationen (UNO)?";
		answer[11] = 	new GeoPosition(1078,419);
		info[11] = 		"Das UN-Hauptquartier in New York ist der wichtigste Standort und Hauptsitz der Vereinten Nationen, gelegen am United Nations Plaza in Manhattan.";

		question[12] = 	"Jeder hat schonmal von der Katastrophe von Tschernobyl gehört aber wo genau liegt eigentlich das Kraftwerk?";
		answer[12] = 	new GeoPosition(1839,322);
		info[12] = 		"Das heute stillgelegte Kernkraftwerk Tschernobyl befindet sich im Norden der Ukraine nahe der ukrainisch-weißrussischen Grenze.";

		question[13] = 	"Wo liegt Bali?";
		answer[13] = 	new GeoPosition(2664,948);
		info[13] = 		"Bali ist eine seit 1949 zu Indonesien gehörende Insel und bildet die gleichnamige Provinz dieses Staates.";

		question[14] = 	"Wo liegt New York?";
		answer[14] = 	new GeoPosition(1078,419);
		info[14] = 		"New York ist eine Weltstadt an der Ostküste der Vereinigten Staaten. Die Stadt liegt im Bundesstaat New York und ist mit mehr als acht Millionen Einwohnern die größte Stadt der USA.";

		question[15] = 	"Wo befindet sich das Sagenumwobene Stonehenge?";
		answer[15] = 	new GeoPosition(1628,319);
		info[15] = 		"Es besteht aus einer Grabenanlage, die eine Megalithstruktur umgibt, welche wiederum aus mehreren konzentrischen Steinkreisen gebildet wird.";

		question[16] = 	"Wo befinden sich die Niagara Falls?";
		answer[16] = 	new GeoPosition(1046,398);
		info[16] = 		"Die Niagara Falls sind Wasserfälle des Niagara-Flusses an der Grenze zwischen dem US-amerikanischen Bundesstaat New York und der kanadischen Provinz Ontario.";

		question[17] = 	"Wo befinden sich die sogenannten Südshettland Inseln?";
		answer[17] = 	new GeoPosition(1310,1489);
		info[17] = 		"Die Südlichen Shetlandinseln sind eine subantarktische Inselgruppe im Südlichen Ozean, westlich der Antarktischen Halbinsel gelegen.";

		question[18] = 	"Wo liegt die Hauptstadt Australiens?";
		answer[18] = 	new GeoPosition(2795,1255);
		info[18] = 		"die Hauptstadt Australiens ist Canberra und sie liegt nahe der Süd-östlichen Küste Australiens.";

		question[19] = 	"Wo liegt die Hauptstadt Tschechiens?";
		answer[19] = 	new GeoPosition(1725,328);
		info[19] = 		"Prag ist die Hauptstadt und zugleich bevölkerungsreichste Stadt der Tschechischen Republik.";
		
		question[20] = 	"Wo fand das Finale der Fußball WM 2010 statt?";
		answer[20] = 	new GeoPosition(1872,1140);
		info[20] = 		"Das Finale fand im Soccer City Stadion in Johannesburg statt";
		
		question[21] = 	"Wo ist die größte Stadt Chinas?";
		answer[21] = 	new GeoPosition(2627,506);
		info[21] = 		"Shanghai ist mit Abstand die bevölkerungsreichste Stadt Chinas. Die Metropole am Jangtsekiang repräsentiert mit ihrer beeindruckenden Skyline das moderne China und sein Reichtum.";
		
		question[22] = 	"Wo ist der Titicacasee?";
		answer[22] = 	new GeoPosition(1028,1028);
		info[22] = 		"Der Titicaca-See (spanisch: Lago Titicaca) ist Südamerikas größter See; mit einer Fläche von 8.288 Quadratkilometern ist er etwa 15 Mal so groß wie der Bodensee.";
		
		question[23] = 	"Wo liegt das Atomkraftwerk Fukushima?";
		answer[23] = 	new GeoPosition(2740,456);
		info[23] = 		"Das Kernkraftwerk Fukushima war mit sechs Reaktorblöcken und bis zu 4,5 Gigawatt elektrischer Nettoleistung eines der leistungsstärksten Kernkraftwerke in Japan.";
		
		question[24] = 	"Osama Bin Laden versteckte sich jahrelang in einer Villa in Abbottabad, bis er 2011 dort erschossen wurde. Wo liegt Abbottabad?";
		answer[24] = 	new GeoPosition(2232,500);
		info[24] = 		"Osama Bin Laden wurde 55 km nördlich der Hauptstadt Islamabad von US-Spezialeinheiten erschossen.";		
		
		question[25] = 	"Wo fanden die Olympischen Spiele von 2004 statt?";
		answer[25] = 	new GeoPosition(1820,447);
		info[25] = 		"2004 kehrten die Spiele an ihren Ursprungsort zurück...nach Athen.";
			
		question[26] = 	"Wo wurden die Olympischen Spiele von '96 ausgetragen?";
		answer[26] = 	new GeoPosition(963,503);
		info[26] = 		"Die Olympischen Sommerspiele von 1996 fanden im US-Amerikanischen Atlanta statt.";
		
		question[27] = 	"Wo befindet sich die weltbekannte Harbor Bridge?";
		answer[27] = 	new GeoPosition(2846,1221);
		info[27] = 		"Die Harbor Bridge ist in Sydney direkt neben der weltbekannten Oper von Sydney.";
		
		question[28] = 	"Wo befindet sich der Berg (Corcovado) auf dem die estatua Cristo Redentor - Christustatue steht?";
		answer[28] = 	new GeoPosition(1260,1107);
		info[28] = 		"Die Statue ist das Wahrzeichen von Rio de Janeiro.";
		
		question[29] = 	"Wo hat der Dalai Lama sein Zuhause?";
		answer[29] = 	new GeoPosition(2316,542);
		info[29] = 		"Im Jahre 1959 floh der gegenwärtige Dalai Lama am 17. März während des Tibetaufstands ins indische Exil nach Dharamsala.";
		*/
	}

	public World(Player[] myPlayer, Modus modus, String background, int globalGameID) {
		this(myPlayer, defaultNumOfRounds, modus, background, globalGameID);
	}
	public World(Player[] player, int numOfRounds, Modus modus, String background, int globalGameID) {
		super(player, numOfRounds, modus, background, globalGameID);
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
		guess = new GeoPosition[spielerZahl];
		turnDone = new boolean[spielerZahl];
		distance = new double[spielerZahl];
	}
	private void mapMouseClicked(MouseEvent evt) {
		if(whosTurn>=0 && whosTurn<spielerZahl){
			GeoPosition place = mapViewer.getMainMap().convertPointToGeoPosition(new Point(evt.getX(),evt.getY()));   
            System.out.println(place.toString());
            Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>();
            waypoints.add(new MyWaypoint("", myPlayer[whosTurn].farbe, place));
            
            //crate a WaypointPainter to draw the points
            WaypointPainter<MyWaypoint> painter = new WaypointPainter<MyWaypoint>();
            painter.setRenderer(new FancyWaypointRenderer());
            painter.setWaypoints(waypoints);
            mapViewer.getMainMap().setOverlayPainter(painter);
            
			guess[whosTurn] = place;
			turnDone[whosTurn] = true;
		}
	}
	
	private void resetMap() {
		// remove markers if any
		mapViewer.getMainMap().setOverlayPainter(null);
		resetMapView();
	}
	
	private void resetMapView() {
		mapViewer.setZoom(17);
		mapViewer.setAddressLocation(new GeoPosition(0, 0));
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
					mapViewer = new JXMapKit();
					VirtualEarthTileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
					info.setDefaultZoomLevel(17);
			        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
			        mapViewer.setTileFactory(tileFactory);
			        mapViewer.setMinimumSize(new Dimension(1024,528));
			        //mapViewer.setZoom(17);
			        mapViewer.setAddressLocationShown(false);
			        mapViewer.getMainMap().addMouseListener(new MouseAdapter() {
			        	@Override
			        	public void mouseClicked(MouseEvent e) {
			                mapMouseClicked(e);
			        	}
					});
			        hauptbereichPanel.add(mapViewer, BorderLayout.CENTER);
			        hauptbereichPanel.setPreferredSize(new Dimension(1024,528));
			        
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
			turnDone[index] = true;
			myTurnButton[index].setEnabled(false);
			myTurnButton[index].setText("Tip abgegeben");
			whosTurn=-1;
			resetMap();
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
			distance[i] = Math.round(geoDistance(guess[i],answer[last])/1000);
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


	private void zoomButtonActionPerformed(ActionEvent evt) {
		resetMapView();
	}
	/**
	 * Calculate distance between two points in latitude and longitude not taking
	 * into account height difference. Uses Haversine method as its base.
	 * 
	 * inspired by https://stackoverflow.com/a/16794680
	 * pos1 Start point pos2 End point
	 * @returns Distance in Meters
	 */
	public double geoDistance(GeoPosition pos1, GeoPosition pos2) {
		double lat1 = pos1.getLatitude();
		double lon1 = pos1.getLongitude();
		double lat2 = pos2.getLatitude();
		double lon2 = pos2.getLongitude();

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    return distance;
	}
}
