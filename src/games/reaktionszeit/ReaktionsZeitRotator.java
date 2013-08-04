package games.reaktionszeit;

import gui.components.Bildschirm;
import gui.components.rotator.Rotator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;

public class ReaktionsZeitRotator extends Rotator {
	private static final long serialVersionUID = 1L;
	private ReaktionsZeitDeck reaktionsZeitDeck;
	private JLabel textLabel;
	private int currentIndex;
	private Bildschirm bildschirm;
	private List<String> elements;

	public ReaktionsZeitRotator(ReaktionsZeitDeck reaktionsZeitDeck) {
		super();
		this.reaktionsZeitDeck = reaktionsZeitDeck;
		elements = reaktionsZeitDeck.getElements();
		initGUI();
	}

	private void initGUI() {
		if(reaktionsZeitDeck.getDeckType().equals(ReaktionsZeitDeck.STRING)){			
			textLabel = new JLabel("bla");
			textLabel.setHorizontalAlignment(JLabel.CENTER);
			add(textLabel, BorderLayout.CENTER);
		}
		else if(reaktionsZeitDeck.getDeckType().equals(ReaktionsZeitDeck.PICTURE)){
			bildschirm = new Bildschirm(elements.get(0));
			add(bildschirm, BorderLayout.CENTER);
		}
		else{
			textLabel = new JLabel("ERROR: Decktyp "+reaktionsZeitDeck.getDeckType()+" ist leider unbekannt.");
			add(textLabel, BorderLayout.CENTER);
		}
	}

	@Override
	public void changeComponent() {
		currentIndex = nextRandom(elements.size(), true);
		if(reaktionsZeitDeck.getDeckType().equals(ReaktionsZeitDeck.STRING))
			textLabel.setText(elements.get(currentIndex));
		else if(reaktionsZeitDeck.getDeckType().equals(ReaktionsZeitDeck.PICTURE)){
			bildschirm.changePic(elements.get(currentIndex));
			bildschirm.hidePic(false);
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public String getDeckName() {
		return reaktionsZeitDeck.getDeckName();
	}

	@Override
	public void maskComponent() {
		if(reaktionsZeitDeck.getDeckType().equals(ReaktionsZeitDeck.STRING))
			textLabel.setText("");
		else if(reaktionsZeitDeck.getDeckType().equals(ReaktionsZeitDeck.PICTURE))
			bildschirm.hidePic(true);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (textLabel != null)
			textLabel.setForeground(fg);
	}

	public void setFont(Font font) {
		super.setFont(font);
		if (textLabel != null)
			textLabel.setFont(font);
	}
}
