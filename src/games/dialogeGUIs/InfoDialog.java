package games.dialogeGUIs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class InfoDialog extends gui.AnzeigeDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5089669334193317133L;
	private JTextPane infoTextPane;
	private JPanel hauptPanel;
	private JButton okButton;
	private JPanel buttonPanel;
	private JScrollPane infoPanelScrollBar;

	/**
	 * Auto-generated main method to display this JDialog
	 */

	public InfoDialog(String infoTxt) {
		initGUI(infoTxt);
	}

	private void initGUI(String infoTxt) {
		try {
			{
				hauptPanel = new JPanel();
				BorderLayout hauptPanelLayout = new BorderLayout();
				super.dialogPane.add(hauptPanel);
				hauptPanel.setLayout(hauptPanelLayout);
				hauptPanel.setPreferredSize(new Dimension(400,300));
				{
					infoPanelScrollBar = new JScrollPane();
					hauptPanel.add(infoPanelScrollBar, BorderLayout.CENTER);
					infoPanelScrollBar.getVerticalScrollBar().setVisible(false);
					{
						infoTextPane = new JTextPane();
						infoPanelScrollBar.setViewportView(infoTextPane);
						infoTextPane.setText(infoTxt);
						infoTextPane.setFont(new java.awt.Font("Comic Sans MS",0,28));
						infoTextPane.setPreferredSize(new Dimension(400, 250));
						infoTextPane.setBackground(new java.awt.Color(242,255,255));
						infoTextPane.setEditable(false);
						infoTextPane.setForeground(new java.awt.Color(0,0,160));
					}
				}
				{
					buttonPanel = new JPanel();
					hauptPanel.add(buttonPanel, BorderLayout.SOUTH);
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
			}
			// TODO Code zum Schließen
			dialogPane.setBackground(new java.awt.Color(198,223,255));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void okButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}

}
