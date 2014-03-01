package games.werLuegt;

import gui.AnzeigeDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import start.X;

public class WerLuegtDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	private WerLuegt werLuegt;
	private JPanel schaltflaechenPanel;
	private JButton okButton;
	private JPanel hauptbereichPanel;
	private JLabel aussageLabel;
	private JPanel buzzererPanel;
	private JPanel antwortenPanel;
	private JPanel topPanel;
	
	
	public WerLuegtDetailsDialog(WerLuegt werLuegt){
		this.werLuegt = werLuegt;
		initGUI();
	}
	
	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		{
			hauptbereichPanel = new JPanel();
			hauptbereichPanel.setLayout(new BorderLayout());
			dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
			{
				topPanel = new JPanel();
				topPanel.setLayout(new BorderLayout());
				hauptbereichPanel.add(topPanel,BorderLayout.NORTH);
			}
			{
				aussageLabel = new JLabel();
				aussageLabel.setFont(WerLuegt.standardFont);
				aussageLabel.setText(werLuegt.getCurrentAussage());
				aussageLabel.setHorizontalAlignment(JLabel.CENTER);
				topPanel.add(aussageLabel,BorderLayout.NORTH);
			}
			{
				buzzererPanel = new JPanel();
				buzzererPanel.setLayout(new GridLayout(1,1));
				JLabel spielerLabel = new JLabel("---");
				if(werLuegt.whoBuzzed != -1){
					spielerLabel.setText(werLuegt.myPlayer[werLuegt.whoBuzzed].name);
				}
				spielerLabel.setOpaque(true);
				spielerLabel.setFont(X.BUTTON_FONT);
				spielerLabel.setForeground(Color.RED);
				spielerLabel.setHorizontalAlignment(JLabel.CENTER);
				if(werLuegt.winnerIDs.contains(werLuegt.whoBuzzed))spielerLabel.setForeground(Color.GREEN);
				buzzererPanel.add(spielerLabel);
				topPanel.add(buzzererPanel,BorderLayout.CENTER);
			}
				topPanel.add(new JSeparator(),BorderLayout.SOUTH);
			{
				antwortenPanel = new JPanel();
				Map<String, Boolean> correctAnswers = werLuegt.getCorrectAnswers(); 
				List<String> verlauf = werLuegt.getVerlauf();
				antwortenPanel.setLayout(new GridLayout(verlauf.size(),1));
				for(int i=0; i<verlauf.size(); i++){
					JLabel antwortLabel = new JLabel(verlauf.get(i));
					antwortLabel.setOpaque(true);
					antwortLabel.setBackground(Color.RED);
					antwortLabel.setHorizontalAlignment(JLabel.CENTER);
					try{
						if(correctAnswers.get(verlauf.get(i)))
							antwortLabel.setBackground(Color.GREEN);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					antwortenPanel.add(antwortLabel);
				}
				hauptbereichPanel.add(antwortenPanel, BorderLayout.SOUTH);
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
