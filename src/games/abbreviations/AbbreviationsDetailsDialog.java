package games.abbreviations;

import games.Game;
import gui.AnzeigeDialog;
import gui.components.Bildschirm;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
	private Bildschirm abbreviationPanel;
	private Bildschirm rightWordPanel;
	private Bildschirm answerPanel;
	
	public AbbreviationsDetailsDialog(Abbreviations abbreviations) {
		this.abbreviations = abbreviations;
		initGUI();
	}

	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		hauptbereichPanel = new JPanel(new BorderLayout(3,1));
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
			abbreviationPanel = new Bildschirm("/media/abbreviations/nummernschild.png");
			abbreviationPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 23, -5));
			hauptbereichPanel.add(abbreviationPanel, BorderLayout.NORTH);
			abbreviationLabel = new JLabel();
			abbreviationLabel.setFont(Abbreviations.standardFont.deriveFont(70f));
			abbreviationLabel.setText(abbreviations.getCurrentAbbreviation());
			abbreviationLabel.setForeground(Color.BLACK);
			abbreviationPanel.add(abbreviationLabel);
		}
		{
			rightWordPanel = new Bildschirm("/media/abbreviations/ortseingang.png");
			hauptbereichPanel.add(rightWordPanel,BorderLayout.CENTER);
			rightWordLabel = new JLabel();
			rightWordLabel.setFont(Abbreviations.standardFont);
			rightWordLabel.setText(abbreviations.getCurrentFullWord());
			rightWordLabel.setForeground(Color.BLACK);
			rightWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
			rightWordPanel.add(rightWordLabel);
		}
		{
			answerPanel = new Bildschirm("/media/abbreviations/ortsausgang.png");
			hauptbereichPanel.add(answerPanel,BorderLayout.SOUTH);
			answerLabel = new JLabel();
			answerLabel.setFont(Abbreviations.standardFont);
			answerLabel.setText(abbreviations.getAnswer());
			answerLabel.setForeground(Color.DARK_GRAY);
			answerLabel.setHorizontalAlignment(SwingConstants.CENTER);
			if(abbreviations.getCurrentFullWord().equalsIgnoreCase(abbreviations.getAnswer())){
				answerLabel.setForeground(Color.BLACK);
				answerPanel.changePic("/media/abbreviations/ortseingang.png");
			}
			answerPanel.add(answerLabel);
		}
	}
}
