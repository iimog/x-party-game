package games.world;
import player.Player;
import sample4_fancy.FancyWaypointRenderer;
import sample4_fancy.MyWaypoint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

public class WorldDetailsDialog extends gui.AnzeigeDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4134617911404152810L;
	World world;
	private JPanel hauptbereichPanel;
	private JLabel[] playerLabel;
	private JPanel anzeigenPanel;
	private JButton okButton;
	private JPanel schaltflaechenPanel;
	private JLabel[] distanceLabel;
	private JXMapKit mapViewer;
	private HashSet<GeoPosition> positions;
	private JLabel labelAttr;
	private List<TileFactory> factories;
	/**
	 * Auto-generated main method to display this JDialog
	 */

	public WorldDetailsDialog(World world) {
		this.world = world;
		playerLabel = new JLabel[world.spielerZahl];
		distanceLabel = new JLabel[world.spielerZahl];
		initGUI();
	}
	
	@Override
	public void nowVisible() {
		super.nowVisible();
		zoomToBestFit();
	}

	private void initGUI() {
		try {
			{
				dialogPane.setLayout(new BorderLayout());
				hauptbereichPanel = new JPanel();
				BorderLayout hauptbereichPanelLayout = new BorderLayout();
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
				{
					factories = new ArrayList<TileFactory>();

			        TileFactoryInfo osmInfo = new OSMTileFactoryInfo();
			        TileFactoryInfo infoSat = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
			        TileFactoryInfo infoHyb = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
			        TileFactoryInfo infoMap = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
			        //			        TileFactoryInfo googleInfo = new GoogleMapsTileFactoryInfo("<key>");

			        factories.add(new DefaultTileFactory(infoSat));
			        factories.add(new DefaultTileFactory(infoHyb));
			        factories.add(new DefaultTileFactory(infoMap));
			        factories.add(new DefaultTileFactory(osmInfo));
//			        factories.add(new DefaultTileFactory(googleInfo));
			        labelAttr = new JLabel();

					mapViewer = new JXMapKit();
					JXMapViewer mainMap = mapViewer.getMainMap();
					mainMap.setLayout(new BorderLayout());
					mainMap.add(labelAttr, BorderLayout.SOUTH);
			        mainMap.setMinimumSize(new Dimension(1024,528));
			        //mapViewer.setZoom(15);
			        //mapViewer.setAddressLocationShown(false);
			        Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>();
			        positions = new HashSet<GeoPosition>();
			        for(int i=0; i<world.spielerZahl; i++) {
			        	Player p = world.myPlayer[i];
			        	GeoPosition place = world.guess[i];
			        	positions.add(place);
			        	long dist = Math.round(world.distance[i]);
			        	waypoints.add(new MyWaypoint(dist+"km", p.farbe, place));
			        }
			        GeoPosition answer = world.answer[world.last];
			        positions.add(answer);
			        waypoints.add(new MyWaypoint("Ziel", Color.WHITE, answer));
		            
		            //crate a WaypointPainter to draw the points
		            WaypointPainter<MyWaypoint> painter = new WaypointPainter<MyWaypoint>();
		            painter.setRenderer(new FancyWaypointRenderer());
		            painter.setWaypoints(waypoints);
		            mapViewer.getMainMap().setOverlayPainter(painter);
					hauptbereichPanel.add(mapViewer, BorderLayout.CENTER);
					hauptbereichPanel.setPreferredSize(new Dimension(1024,528));
					setTileFactoryIndex(0);
				}
				{
					anzeigenPanel = new JPanel();
					GridLayout anzeigenPanelLayout = new GridLayout(2, world.spielerZahl);
					anzeigenPanelLayout.setHgap(5);
					anzeigenPanelLayout.setVgap(5);
					anzeigenPanelLayout.setColumns(world.spielerZahl);
					anzeigenPanelLayout.setRows(2);
					anzeigenPanel.setLayout(anzeigenPanelLayout);
					hauptbereichPanel.add(anzeigenPanel, BorderLayout.NORTH);
					for(int i=0;i<world.spielerZahl;i++){
						playerLabel[i] = new JLabel(world.myPlayer[i].name);
						anzeigenPanel.add(playerLabel[i]);
					}
					{
						for(int i=0;i<world.spielerZahl;i++){
							String a;
							if(world.distance[i]<=world.tol){
								a="getroffen";
							}
							else{
								a = Math.round(world.distance[i])+" km";
							}
							distanceLabel[i] = new JLabel(a);
							anzeigenPanel.add(distanceLabel[i]);
						}
					}
				}
			}
			{
				schaltflaechenPanel = new JPanel();
				dialogPane.add(schaltflaechenPanel, BorderLayout.SOUTH);
				{
					okButton = new JButton();
					schaltflaechenPanel.add(okButton);
					okButton.setText("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							instance.closeDialog();
						}
					});
				}
				{
					JButton adjustZoomButton = new JButton("adjust zoom");
					schaltflaechenPanel.add(adjustZoomButton);
					adjustZoomButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							mapViewer.getMainMap().zoomToBestFit(positions, .7);
						}
					});
				}
				{
					String[] tfLabels = new String[factories.size()];
			        for (int i = 0; i < factories.size(); i++)
			        {
			        	TileFactoryInfo info = factories.get(i).getInfo();
			            tfLabels[i] = info.getName();
			            if(info instanceof VirtualEarthTileFactoryInfo) {
			            	tfLabels[i] += " - " + ((VirtualEarthTileFactoryInfo) info).getModeName();
			            }
			        }

			        final JComboBox<String> combo = new JComboBox<String>(tfLabels);
			        combo.addItemListener(new ItemListener()
			        {
			            @Override
			            public void itemStateChanged(ItemEvent e)
			            {
			                setTileFactoryIndex(combo.getSelectedIndex());
			            }
			        });
			        schaltflaechenPanel.add(combo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void setTileFactoryIndex(int index)
    {
        TileFactory factory = factories.get(index);
        TileFactoryInfo info = factory.getInfo();
        mapViewer.setTileFactory(factory);
        labelAttr.setText(info.getAttribution() + " - " + info.getLicense());
        zoomToBestFit();
    }
	
	protected void zoomToBestFit() {
		if(mapViewer != null && positions != null)
			mapViewer.getMainMap().zoomToBestFit(positions, .7);
	}
}
