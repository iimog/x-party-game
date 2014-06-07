package games.skel;

import games.Game;
import gui.AnzeigeDialog;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class SkelDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	Skel skel;
	private JPanel hauptbereichPanel;
	private JPanel buttonPanel;
	private DefaultButton okButton;
	private Font labelFont;
	
	public SkelDetailsDialog(Skel skel) {
		this.skel = skel;
		initGUI();
	}

	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		hauptbereichPanel = new JPanel(new GridLayout(2,1));
		hauptbereichPanel.setBackground(Color.DARK_GRAY);
		dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
		labelFont = Game.STANDARD_FONT.deriveFont(50f);
		labelFont = labelFont.deriveFont(Font.BOLD);
		showAnswerAndSolution();
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setBackground(Color.DARK_GRAY);
		dialogPane.add(buttonPanel, BorderLayout.SOUTH);
		okButton = new DefaultButton("OK");
		okButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				instance.closeDialog();
			}
		});
		buttonPanel.add(okButton);
	}

	private void showAnswerAndSolution() {
		// Add panels/labels/... to show the given answer and the correct solution
	}
}
