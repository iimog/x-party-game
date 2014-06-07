package games.abbreviations;

import games.Game;
import games.buchstabensalat.BuchstabenSalat;
import gui.AnzeigeDialog;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class AbbreviationsDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	Abbreviations abbreviations;
	private JPanel hauptbereichPanel;
	private JPanel buttonPanel;
	private DefaultButton okButton;
	private Font labelFont;
	private JLabel rightWordLabel;
	private JLabel answerLabel;
	private JLabel abbreviationLabel;
	
	public AbbreviationsDetailsDialog(Abbreviations abbreviations) {
		this.abbreviations = abbreviations;
		initGUI();
	}

	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		hauptbereichPanel = new JPanel(new GridLayout(3,1));
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
		{
			abbreviationLabel = new JLabel();
			abbreviationLabel.setFont(Abbreviations.standardFont);
			abbreviationLabel.setText(abbreviations.getCurrentAbbreviation());
			abbreviationLabel.setForeground(Color.WHITE);
			hauptbereichPanel.add(abbreviationLabel);
		}
		{
			rightWordLabel = new JLabel();
			rightWordLabel.setFont(Abbreviations.standardFont);
			rightWordLabel.setText(abbreviations.getCurrentFullWord());
			rightWordLabel.setForeground(Color.WHITE);
			hauptbereichPanel.add(rightWordLabel);
		}
		{
			answerLabel = new JLabel();
			answerLabel.setFont(Abbreviations.standardFont);
			answerLabel.setText(abbreviations.getAnswer());
			answerLabel.setForeground(Color.RED);
			if(abbreviations.getCurrentFullWord().equalsIgnoreCase(abbreviations.getAnswer()))
				answerLabel.setForeground(Color.GREEN);
			hauptbereichPanel.add(answerLabel);
		}
	}
}
