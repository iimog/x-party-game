package games.spiegelschrift;

import games.Game;
import games.Modus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import player.Player;

public class SpiegelSchrift extends Game {
	private static final long serialVersionUID = 1L;
	private JPanel hauptbereichPanel;
	private MirrorText mirrorTextField;
	private Font mirrorFont;

	public SpiegelSchrift(Player[] player, Modus modus, String background,
			int globalGameID) {
		super(player, 5, modus, background, globalGameID);
		initGUI();
	}
	
	private void initGUI(){
		hauptbereichPanel = new JPanel(new BorderLayout());
		spielBereichPanel.add(hauptbereichPanel);
		mirrorTextField = new MirrorText("Dies ist ein Teststring");
		mirrorFont = Game.STANDARD_FONT.deriveFont(60f);
		mirrorFont = mirrorFont.deriveFont(Font.ITALIC);
		mirrorTextField.setFont(mirrorFont);
		mirrorTextField.setOpaque(false);
		mirrorTextField.setForeground(Color.BLACK);
		
		hauptbereichPanel.add(mirrorTextField, BorderLayout.CENTER);
	}

	@Override
	public void settingsChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

}
