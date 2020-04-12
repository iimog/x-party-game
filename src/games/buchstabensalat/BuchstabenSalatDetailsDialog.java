package games.buchstabensalat;

import gui.AnzeigeDialog;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BuchstabenSalatDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	private BuchstabenSalat salat;
	private JPanel schaltflaechenPanel;
	private JButton okButton;
	private JPanel hauptbereichPanel;
	private JLabel scatteredWordLabel;
	private JLabel rightWordLabel;
	private JLabel answerLabel;
	
	
	public BuchstabenSalatDetailsDialog(BuchstabenSalat salat){
		this.salat = salat;
		initGUI();
	}
	
	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		{
			hauptbereichPanel = new JPanel();
			hauptbereichPanel.setLayout(new GridLayout(3,1));
			hauptbereichPanel.setOpaque(false);
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			{
				scatteredWordLabel = new JLabel();
				scatteredWordLabel.setFont(BuchstabenSalat.standardFont);
				scatteredWordLabel.setForeground(Color.WHITE);
				scatteredWordLabel.setText(salat.getScatteredWord());
				hauptbereichPanel.add(scatteredWordLabel);
			}
			{
				rightWordLabel = new JLabel();
				rightWordLabel.setFont(BuchstabenSalat.standardFont);
				rightWordLabel.setForeground(Color.WHITE);
				rightWordLabel.setText(salat.currentDeck.getWord(salat.current));
				hauptbereichPanel.add(rightWordLabel);
			}
			{
				answerLabel = new JLabel();
				answerLabel.setFont(BuchstabenSalat.standardFont);
				answerLabel.setText(salat.getAnswer());
				answerLabel.setForeground(Color.RED);
				if(salat.wortErraten)answerLabel.setForeground(Color.GREEN);
				hauptbereichPanel.add(answerLabel);
			}
		}
		{
			schaltflaechenPanel = new JPanel();
			schaltflaechenPanel.setOpaque(false);
			dialogPane.add(schaltflaechenPanel, BorderLayout.SOUTH);
			{
				okButton = new DefaultButton();
				schaltflaechenPanel.add(okButton);
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						okButtonActionPerformed(evt);
					}
				});
			}
		}
	}
	
	private void okButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}

}
