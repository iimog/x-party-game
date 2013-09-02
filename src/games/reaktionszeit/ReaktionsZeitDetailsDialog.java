package games.reaktionszeit;

import games.Game;
import gui.AnzeigeDialog;
import gui.components.Bildschirm;
import gui.components.DefaultButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReaktionsZeitDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	ReaktionsZeit reaktionsZeit;
	private JLabel targetLabel;
	private JLabel currentLabel;
	private JPanel hauptbereichPanel;
	private JPanel buttonPanel;
	private DefaultButton okButton;
	private Bildschirm targetBildschirm;
	private Font labelFont;
	private Bildschirm currentBildschirm;
	
	public ReaktionsZeitDetailsDialog(ReaktionsZeit reaktionsZeit) {
		this.reaktionsZeit = reaktionsZeit;
		initGUI();
	}

	private void initGUI(){
		dialogPane.setLayout(new BorderLayout());
		hauptbereichPanel = new JPanel(new GridLayout(2,1));
		hauptbereichPanel.setBackground(Color.DARK_GRAY);
		dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
		labelFont = Game.STANDARD_FONT.deriveFont(50f);
		labelFont = labelFont.deriveFont(Font.BOLD);
		setTargetAndCurrent();
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

	private void setTargetAndCurrent() {
		if(reaktionsZeit.getDeckType().equals(ReaktionsZeitDeck.STRING)){
			setStringTarget();
			setStringCurrent();
		}
		else if(reaktionsZeit.getDeckType().equals(ReaktionsZeitDeck.PICTURE)){
			setPictureTarget();
			setPictureCurrent();
		}
	}

	private void setStringTarget() {
		targetLabel = new JLabel(reaktionsZeit.getTarget());
		targetLabel.setFont(labelFont);
		targetLabel.setForeground(Color.WHITE);
		targetLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(targetLabel);
	}

	private void setPictureTarget() {
		targetBildschirm = new Bildschirm(reaktionsZeit.getTarget());
		hauptbereichPanel.add(targetBildschirm);
	}
	
	private void setStringCurrent(){
		currentLabel = new JLabel(reaktionsZeit.getCurrent());
		currentLabel.setFont(labelFont);
		currentLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(currentLabel);
		if(reaktionsZeit.getTarget().equals(reaktionsZeit.getCurrent())){
			currentLabel.setForeground(Color.GREEN);
		}
		else{
			currentLabel.setForeground(Color.RED);
		}
	}
	
	private void setPictureCurrent() {
		currentBildschirm = new Bildschirm(reaktionsZeit.getCurrent());
		if(reaktionsZeit.getTarget().equals(reaktionsZeit.getCurrent())){
			currentBildschirm.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
		}
		else{
			currentBildschirm.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
		}
		hauptbereichPanel.add(currentBildschirm);
	}
	
}
