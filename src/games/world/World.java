package games.world;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import games.Deck;
import games.DeckLoader;
import games.Game;
import games.Modus;
import games.PC;
import player.Player;
import sample4_fancy.FancyWaypointRenderer;
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
	int toleranz = 500;
	boolean toleranzOn = true;

	private JXMapKit mapViewer;
	private JXMapViewer mainMap;
	protected FileBasedLocalCache localCache;
	private List<Deck> worldDecks;
	public WorldDeck currentDeck;

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
		currentDeck = new WorldDeck(worldDecks.get(new Random().nextInt(worldDecks.size())));
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
		mapViewer.setAddressLocation(new GeoPosition(20, 0));
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
					VirtualEarthTileFactoryInfo info = new VirtualEarthTileFactoryInfo(
							VirtualEarthTileFactoryInfo.SATELLITE, 10, 17);
					info.setDefaultZoomLevel(17);
			        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
			        // Setup local file cache
			        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
			        localCache = new FileBasedLocalCache(cacheDir, false);
			        tileFactory.setLocalCache(localCache);
			        mapViewer.setTileFactory(tileFactory);
			        mapViewer.setMinimumSize(new Dimension(1024,528));
			        mapViewer.setAddressLocation(new GeoPosition(20, 0));
			        //mapViewer.setZoom(17);
			        mapViewer.setAddressLocationShown(false);
			        mainMap = mapViewer.getMainMap();
			        mapViewer.getMainMap().addMouseListener(new MouseAdapter() {
			        	@Override
			        	public void mouseClicked(MouseEvent e) {
			                mapMouseClicked(e);
			        	}
					});
			        final JLabel labelAttr = new JLabel();
			        mainMap.setLayout(new BorderLayout());
			        mainMap.add(labelAttr, BorderLayout.SOUTH);
			        labelAttr.setText(tileFactory.getInfo().getAttribution() + " - " + tileFactory.getInfo().getLicense());
			        hauptbereichPanel.add(mapViewer, BorderLayout.CENTER);
			        hauptbereichPanel.setPreferredSize(new Dimension(1024,528));
			        
				}
				{
					frageLabel = new JLabel();
					frageLabel.setHorizontalAlignment(SwingConstants.CENTER);
					hauptbereichPanel.add(frageLabel, BorderLayout.NORTH);
					frageLabel.setText("Frage...");
					frageLabel.setFont(STANDARD_FONT.deriveFont((float) 36.0));
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
		current = nextRandom(currentDeck.getSize());
		if(current==-1){
			showMessage("Alle Fragen verbraucht");
			abbruch();
			return;
		}
		if(modus == Modus.TEAM){
			changeActivePlayers();
		}
		frageLabel.setText(currentDeck.getQuestion(current));
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
		instance.showDialog(new games.dialogeGUIs.InfoDialog(currentDeck.getInfo(last)));
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
			distance[i] = Math.round(geoDistance(guess[i],currentDeck.getAnswer(last))/1000);
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
		propertiesToSettings();
		updateCreds();
	}
	
	private void propertiesToSettings() {
		if(customSettings == null){
			return;
		}
		numOfRounds = Integer.parseInt(customSettings.getProperty(WorldSettingsDialog.NUM_OF_ROUNDS, ""+numOfRounds));
		String tolOn = customSettings.getProperty(WorldSettingsDialog.TOLERANZ_ON, "true");
		if(modus != Modus.SOLO) {
			toleranzOn = Boolean.parseBoolean(tolOn);
		}
		String toleranzKm = customSettings.getProperty(WorldSettingsDialog.TOLERANZ_KM, "500");
		toleranz = Integer.parseInt(toleranzKm);
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
	
	@Override
	public void loadProperties() {
		worldDecks = DeckLoader.loadDecks("world");
	}
}
