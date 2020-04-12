package games.guess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GuessDetailsDialog extends gui.AnzeigeDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1072126637991929210L;
	private JPanel schaltflaechenPanel;
	private JLabel[] abweichung;
	private JLabel emptyLabel;
	private JLabel[] answer;
	private JLabel rightAnswer;
	private JLabel[] player;
	private JLabel richtigLabel;
	private JPanel darstellungPanel;
	private JButton okButton;
	private Guess guess;

	public GuessDetailsDialog(Guess guess) {
		this.guess = guess;
		player = new JLabel[guess.spielerZahl];
		answer = new JLabel[guess.spielerZahl];
		abweichung = new JLabel[guess.spielerZahl];
		initGUI();
	}

	private void initGUI() {
		try {
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
			{
				darstellungPanel = new JPanel();
				GridLayout darstellungPanelLayout = new GridLayout(3, guess.spielerZahl+1);
				darstellungPanelLayout.setHgap(5);
				darstellungPanelLayout.setVgap(5);
				dialogPane.add(darstellungPanel, BorderLayout.CENTER);
				darstellungPanel.setLayout(darstellungPanelLayout);
				{
					richtigLabel = new JLabel();
					darstellungPanel.add(richtigLabel);
					richtigLabel.setText("Richtig");
					richtigLabel.setHorizontalAlignment(SwingConstants.CENTER);
					richtigLabel.setFont(new java.awt.Font("Segoe UI",1,20));
				}
				for(int i=0; i<guess.spielerZahl; i++){
					player[i] = new JLabel();
					darstellungPanel.add(player[i]);
					player[i].setText(guess.myPlayer[i].name);
					player[i].setHorizontalAlignment(SwingConstants.CENTER);
					player[i].setFont(new java.awt.Font("Segoe UI",1,20));
					player[i].setForeground(new java.awt.Color(0,0,160));
				}
				{
					rightAnswer = new JLabel();
					darstellungPanel.add(rightAnswer);
					rightAnswer.setText(String.valueOf(guess.currentDeck.getAnswer(guess.last)));
					rightAnswer.setHorizontalAlignment(SwingConstants.CENTER);
					rightAnswer.setFont(new java.awt.Font("Segoe UI",1,20));
				}
				for(int i=0; i<guess.spielerZahl; i++){
					answer[i] = new JLabel();
					darstellungPanel.add(answer[i]);
					answer[i].setText(guess.myPlayer[i].lastAnswer);
					answer[i].setHorizontalAlignment(SwingConstants.CENTER);
					answer[i].setFont(new java.awt.Font("Segoe UI",0,20));
				}
				{
					emptyLabel = new JLabel();
					darstellungPanel.add(emptyLabel);
					emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
					emptyLabel.setFont(new java.awt.Font("Segoe UI",0,20));
				}
				for(int i=0; i<guess.spielerZahl; i++){
					abweichung[i] = new JLabel();
					darstellungPanel.add(abweichung[i]);
					if(guess.myPlayer[i].lastDistance>0){abweichung[i].setText(String.valueOf("+" + guess.myPlayer[i].lastDistance)+" %");}
					else{abweichung[i].setText(String.valueOf(guess.myPlayer[i].lastDistance)+" %");}
					abweichung[i].setHorizontalAlignment(SwingConstants.CENTER);
					abweichung[i].setFont(new java.awt.Font("Segoe UI",0,16));
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
		instance.closeDialog();
	}

}
