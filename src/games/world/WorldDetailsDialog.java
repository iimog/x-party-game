package games.world;
import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


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
public class WorldDetailsDialog extends gui.AnzeigeDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4134617911404152810L;
	World world;
	private JPanel hauptbereichPanel;
	private JLabel[] playerLabel;
	private JPanel anzeigenPanel;
	private Bildschirm bildschirm1;
	private JButton okButton;
	private JPanel schaltflaechenPanel;
	private JLabel[] distanceLabel;
	/**
	 * Auto-generated main method to display this JDialog
	 */

	public WorldDetailsDialog(World world) {
		this.world = world;
		playerLabel = new JLabel[world.spielerZahl];
		distanceLabel = new JLabel[world.spielerZahl];
		initGUI();
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
					bildschirm1 = new Bildschirm("");
					bildschirm1.setPreferredSize(new Dimension(400,300));
					bildschirm1.showPicPart(world.erdeBig, world.answer[world.last]);
					bildschirm1.drawEpi(200, 150, 9);
					if(world.toleranzOn)bildschirm1.drawCircle(200, 150, world.toleranz);
					for(int i=0; i<world.spielerZahl; i++){
						int x = world.guess[i].x - world.answer[world.last].x + 200;
						int y = world.guess[i].y - world.answer[world.last].y + 150;
						bildschirm1.addDot(x, y, world.myPlayer[i].farbe);
					}
					hauptbereichPanel.add(bildschirm1, BorderLayout.CENTER);
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
								a = Math.round(world.distance[i])+" Pixel";
							}
							distanceLabel[i] = new JLabel(a);// TODO Pixel in km umrechnen
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
