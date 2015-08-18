package games.dialogeGUIs;

import games.Game;
import gui.components.JButtonIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class RoundDialog extends gui.AnzeigeDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8760048075099985741L;
	private JPanel schaltflaechenPanel;
	private JButtonIcon infoButton;
	private JButtonIcon detailsButton;
	public JLabel winnerLabel;
	private JLabel jLabel1;
	private JButtonIcon okButton;
	private Game game;


	public RoundDialog(Game game, String winner) {
		// game.goBack();
		this.game = game;
		initGUI(winner);
	}

	private void detailsButtonActionPerformed(ActionEvent evt) {
		game.openDetailsDialog();
	}

	public void enableInfo(boolean enable){
		infoButton.setEnabled(enable);
	}

	private void infoButtonActionPerformed(ActionEvent evt) {
		game.openInfoDialog();
	}

	private void initGUI(String winner) {
		try {
			{
			}
			BorderLayout thisLayout = new BorderLayout();
			dialogPane.setLayout(thisLayout);
			// TODO Code zum Dialogschließen
			{
				jLabel1 = new JLabel();
				dialogPane.add(jLabel1, BorderLayout.NORTH);
				jLabel1.setText("Diese Runde geht an");
				jLabel1.setFont(new java.awt.Font("Segoe UI",1,22));
				jLabel1.setForeground(Color.WHITE);
				jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				winnerLabel = new JLabel();
				dialogPane.add(winnerLabel, BorderLayout.CENTER);
				winnerLabel.setForeground(new java.awt.Color(0,0,160));
				winnerLabel.setFont(new java.awt.Font("Segoe UI",1,72));
				winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
				winnerLabel.setText(winner);
			}
			{
				schaltflaechenPanel = new JPanel();
				FlowLayout schaltflaechenPanelLayout = new FlowLayout();
				schaltflaechenPanel.setLayout(schaltflaechenPanelLayout);
				dialogPane.add(schaltflaechenPanel, BorderLayout.SOUTH);
				// schaltflaechenPanel.setPreferredSize(new java.awt.Dimension(359, 55));
				schaltflaechenPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				schaltflaechenPanel.setOpaque(false);
				{
					infoButton = new JButtonIcon("/media/ablauf/info.png","Info");
					schaltflaechenPanel.add(infoButton);
					infoButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							infoButtonActionPerformed(evt);
						}
					});
				}
				{
					detailsButton = new JButtonIcon("/media/ablauf/details.png","Details");
					schaltflaechenPanel.add(detailsButton);
					detailsButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							detailsButtonActionPerformed(evt);
						}
					});
				}
				{
					okButton = new JButtonIcon("/media/ablauf/rightarrow.png","OK");
					schaltflaechenPanel.add(okButton);
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
		schliessen();
	}

	private void schliessen(){
		game.goBack();
		instance.closeDialog();
		if(game.isOver()){
			game.gameEnd();
		}
		// dispose(); TODO Dialog verlassen Code
	}

}
