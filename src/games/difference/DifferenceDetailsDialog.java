package games.difference;
import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DifferenceDetailsDialog extends gui.AnzeigeDialog {
	private static final long serialVersionUID = 8298433857342908272L;
	private JPanel hauptbereichPanel;
	private JLabel howFarLabel;
	private JLabel distanceLabel;
	private JLabel howLongLabel;
	private JLabel zeitLabel;
	private JLabel whoBuzzLabel;
	private JLabel buzzerLabel;
	private JPanel infoPanel;
	private Bildschirm bildschirm1;
	private JButton okButton;
	private JPanel schaltflaechenPanel;
	private String bild;

	private Difference dif;

	public DifferenceDetailsDialog(Difference dif) {
		this.dif = dif;
		bild = dif.ePics[dif.last];
		initGUI();
	}

	private void initGUI() {
		try {
			{
				hauptbereichPanel = new JPanel();
				BorderLayout hauptbereichPanelLayout = new BorderLayout();
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
				{
					bildschirm1 = new Bildschirm(bild);
					hauptbereichPanel.add(bildschirm1, BorderLayout.CENTER);
					bildschirm1.drawCircle(dif.coords[dif.last].x,dif.coords[dif.last].y , dif.okDist);
					bildschirm1.drawDot(dif.lastClick.x, dif.lastClick.y, dif.myPlayer[dif.lastBuzz].farbe);
				}
				{
					infoPanel = new JPanel();
					GridLayout infoPanelLayout = new GridLayout(3, 2);
					infoPanelLayout.setHgap(5);
					infoPanelLayout.setVgap(5);
					infoPanelLayout.setColumns(2);
					infoPanelLayout.setRows(3);
					infoPanel.setLayout(infoPanelLayout);
					hauptbereichPanel.add(infoPanel, BorderLayout.NORTH);
					{
						buzzerLabel = new JLabel();
						infoPanel.add(buzzerLabel);
						buzzerLabel.setText("Buzzerer:");
					}
					{
						whoBuzzLabel = new JLabel(dif.myPlayer[dif.lastBuzz].name);
						infoPanel.add(whoBuzzLabel);
					}
					{
						zeitLabel = new JLabel();
						infoPanel.add(zeitLabel);
						zeitLabel.setText("Zeit:");
					}
					{
						howLongLabel = new JLabel((double)Math.round(dif.dauer/100)/10 + " Sekunden");
						infoPanel.add(howLongLabel);
					}
					{
						distanceLabel = new JLabel();
						infoPanel.add(distanceLabel);
						distanceLabel.setText("Entfernung:");
					}
					{
						if(dif.distance==-1){
							howFarLabel = new JLabel("---");
						}
						else{
							howFarLabel = new JLabel(dif.distance+" Pixel");
						}
						infoPanel.add(howFarLabel);
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
							okButtonActionPerformed(evt);
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void okButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}
}
