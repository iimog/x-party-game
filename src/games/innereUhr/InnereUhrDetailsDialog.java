package games.innereUhr;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InnereUhrDetailsDialog extends gui.AnzeigeDialog {
	private static final long serialVersionUID = -771814795452414108L;
	private JPanel hauptbereichPanel;
	private JLabel[] playerLabel;
	private JLabel[] distanceLabel;
	private JLabel[] playerTimeLabel;
	private JPanel spielerPanel;
	private JLabel zielzeitLabel;
	private JButton okButton;
	private JPanel buttonPanel;

	private InnereUhr uhr;

	public InnereUhrDetailsDialog(InnereUhr uhr) {
		this.uhr=uhr;
		playerLabel = new JLabel[uhr.spielerZahl];
		distanceLabel = new JLabel[uhr.spielerZahl];
		playerTimeLabel = new JLabel[uhr.spielerZahl];
		initGUI();
	}

	private void initGUI() {
		try {
			{
				hauptbereichPanel = new JPanel();
				GridLayout hauptbereichPanelLayout = new GridLayout(2, 1);
				hauptbereichPanelLayout.setColumns(1);
				hauptbereichPanelLayout.setHgap(5);
				hauptbereichPanelLayout.setVgap(5);
				hauptbereichPanelLayout.setRows(2);
				dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				{
					zielzeitLabel = new JLabel();
					zielzeitLabel.setFont(uhr.zielzeitLabel.getFont());
					zielzeitLabel.setText(uhr.lastGuessTime+" s");
					hauptbereichPanel.add(zielzeitLabel);
					zielzeitLabel.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					spielerPanel = new JPanel();
					GridLayout spielerLabelLayout = new GridLayout(3, uhr.spielerZahl);
					spielerLabelLayout.setColumns(uhr.spielerZahl);
					spielerLabelLayout.setRows(3);
					spielerLabelLayout.setHgap(5);
					spielerLabelLayout.setVgap(5);
					hauptbereichPanel.add(spielerPanel);
					spielerPanel.setLayout(spielerLabelLayout);
					for(int i=0; i<uhr.spielerZahl; i++){
						playerTimeLabel[i] = new JLabel();
						spielerPanel.add(playerTimeLabel[i]);
						playerTimeLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
						playerTimeLabel[i].setFont(games.Game.STANDARD_FONT);
						double t = uhr.playerTime[i];
						t=Math.round(t/100);
						t = t/10;
						playerTimeLabel[i].setText(t+" s");
					}
					for(int i=0; i<uhr.spielerZahl; i++){
						distanceLabel[i] = new JLabel();
						spielerPanel.add(distanceLabel[i]);
						distanceLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
						distanceLabel[i].setFont(games.Game.STANDARD_FONT);
						String s; if(uhr.distance[i]>0)s="+";else s="";
						distanceLabel[i].setText(s+uhr.distance[i]+" ms");
						if(uhr.rWinner==i){
							distanceLabel[i].setForeground(new Color(0,100,0));
						}
						else{
							distanceLabel[i].setForeground(Color.red);
						}
					}
					for(int i=0; i<uhr.spielerZahl; i++){
						playerLabel[i] = new JLabel();
						playerLabel[i].setFont(uhr.playerLabel[i].getFont());
						playerLabel[i].setText(uhr.playerLabel[i].getText());
						playerLabel[i].setForeground((uhr.playerLabel[i].getForeground()));
						spielerPanel.add(playerLabel[i]);
						playerLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
					}
				}
			}
			{
				buttonPanel = new JPanel();
				dialogPane.add(buttonPanel, BorderLayout.SOUTH);
				{
					okButton = new JButton();
					buttonPanel.add(okButton);
					okButton.setText("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							okButtonActionPerformed(evt);
						}
					});
				}
			}
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void okButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}

}
