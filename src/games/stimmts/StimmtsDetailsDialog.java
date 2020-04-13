package games.stimmts;

import gui.AnzeigeDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StimmtsDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	private Stimmts stimmts;
	private JPanel schaltflaechenPanel;
	private JButton okButton;
	private JPanel hauptbereichPanel;
	private JLabel aussageLabel;
	private JLabel answerLabel;
	private JPanel werLagRichtigPanel;
	
	
	public StimmtsDetailsDialog(Stimmts stimmts){
		this.stimmts = stimmts;
		initGUI();
	}
	
	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		{
			hauptbereichPanel = new JPanel();
			hauptbereichPanel.setLayout(new GridLayout(3,1));
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			{
				aussageLabel = new JLabel();
				aussageLabel.setFont(Stimmts.standardFont.deriveFont(24f));
				aussageLabel.setText(stimmts.currentDeck.getAussage(stimmts.current).getAussage());
				aussageLabel.setHorizontalAlignment(JLabel.CENTER);
				hauptbereichPanel.add(aussageLabel);
			}
			{
				answerLabel = new JLabel();
				answerLabel.setFont(Stimmts.standardFont);
				boolean wahr = stimmts.currentDeck.getAussage(stimmts.current).isWahr();
				String richtigeAntwort = (wahr ? "Wahr" : "Falsch");
				answerLabel.setText(richtigeAntwort);
				answerLabel.setHorizontalAlignment(JLabel.CENTER);
				answerLabel.setForeground(Color.BLUE);
				hauptbereichPanel.add(answerLabel);
			}
			{
				werLagRichtigPanel = new JPanel();
				werLagRichtigPanel.setLayout(new GridLayout(1,stimmts.spielerZahl));
				for(int i=0; i<stimmts.spielerZahl; i++){
					JLabel spielerLabel = new JLabel(
							stimmts.myPlayer[i].name + " ("+stimmts.buzzerCounter[i]+")");
					spielerLabel.setOpaque(true);
					spielerLabel.setBackground(Color.RED);
					spielerLabel.setHorizontalAlignment(JLabel.CENTER);
					if(stimmts.winnerIDs.contains(i))spielerLabel.setBackground(Color.GREEN);
					werLagRichtigPanel.add(spielerLabel);
				}
				hauptbereichPanel.add(werLagRichtigPanel);
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
	}
	
	private void okButtonActionPerformed(ActionEvent evt) {
		instance.closeDialog();
	}

}
