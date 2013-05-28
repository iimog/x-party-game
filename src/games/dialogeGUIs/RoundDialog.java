package games.dialogeGUIs;

import games.Game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
public class RoundDialog extends gui.AnzeigeDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8760048075099985741L;
	private JPanel schaltflaechenPanel;
	private JButton infoButton;
	private JButton detailsButton;
	public JLabel winnerLabel;
	private JLabel jLabel1;
	private JButton okButton;
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
				jLabel1.setForeground(new java.awt.Color(0,0,0));
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
				schaltflaechenPanel.setPreferredSize(new java.awt.Dimension(359, 33));
				schaltflaechenPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
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
				{
					detailsButton = new JButton();
					schaltflaechenPanel.add(detailsButton);
					detailsButton.setText("Details");
					detailsButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							detailsButtonActionPerformed(evt);
						}
					});
				}
				{
					infoButton = new JButton();
					schaltflaechenPanel.add(infoButton);
					infoButton.setText("Info");
					infoButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							infoButtonActionPerformed(evt);
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
