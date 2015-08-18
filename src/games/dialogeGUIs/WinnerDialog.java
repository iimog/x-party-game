package games.dialogeGUIs;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class WinnerDialog extends javax.swing.JFrame {
	private static final long serialVersionUID = 8519021886348201186L;
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {

		WinnerDialog inst = new WinnerDialog("1 zu 2","Hannah");
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);

	}
	private JPanel hauptbereichPanel;
	private JLabel winnerLabel;
	private JLabel endstandLabel;
	private String winner;
	private String endstand;
	private JLabel jLabel2;
	private JLabel jLabel1;

	private Font font = new Font("Algerian",0,28);

	public WinnerDialog(String endstand, String name) {
		super();
		this.endstand = endstand;
		winner = name;
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("And the winner is...");
			{
				hauptbereichPanel = new JPanel();
				GridLayout hauptbereichPanelLayout = new GridLayout(4, 1);
				hauptbereichPanelLayout.setHgap(5);
				hauptbereichPanelLayout.setVgap(5);
				hauptbereichPanelLayout.setColumns(1);
				hauptbereichPanelLayout.setRows(4);
				getContentPane().add(hauptbereichPanel, BorderLayout.CENTER);
				hauptbereichPanel.setLayout(hauptbereichPanelLayout);
				{
					jLabel1 = new JLabel();
					hauptbereichPanel.add(jLabel1);
					jLabel1.setText("Das Spiel gewinnt");
					jLabel1.setFont(font);
					jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					jLabel2 = new JLabel();
					hauptbereichPanel.add(jLabel2);
					jLabel2.setText("mit");
					jLabel2.setFont(font);
					jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					endstandLabel = new JLabel();
					hauptbereichPanel.add(endstandLabel);
					endstandLabel.setText(endstand);
					endstandLabel.setFont(font);
					endstandLabel.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					winnerLabel = new JLabel();
					hauptbereichPanel.add(winnerLabel);
					winnerLabel.setText(winner);
					winnerLabel.setFont(font);
					winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
					winnerLabel.setForeground(new java.awt.Color(64,0,64));
				}
			}
			pack();
			setVisible(true);
			setLocationRelativeTo(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
