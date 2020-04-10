package start;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import games.Game;
import gui.Anzeige;
import gui.AnzeigeDialog;
import gui.EasyDialog;
import gui.components.Bildschirm;
import gui.menu.HauptMenu;
import settings.MainSettings;
import util.ConfirmListener;
import util.Version;

public class X extends javax.swing.JFrame {

	/**
	 * serialVersionUID generated by Eclipse
	 */
	private static final long serialVersionUID = 6180786189305572480L;
	public static final Version VERSION = new Version(0,9);

	private static X instance;
	public static final Font BUTTON_FONT = getStandardFont().deriveFont(22f);

	private static String currentAudioFile;

	private static Font standardFont;
	private static Font emojiFont;
	private MainSettings mainSettings = MainSettings.getMainSettings();
	
	public static String getDataDir() {
		return System.getProperty("user.home") + "/.xpartygame/";
	}
	
	private String[] quickStartPlayers = {"Player 1", "Player 2"};

	public static X getInstance() {
		if (instance == null) {
			return new X();
		} else {
			return instance;
		}
	}

	public static Font getStandardFont() {
		if (standardFont == null) {
			try {
				standardFont = Font
						.createFont(Font.TRUETYPE_FONT, X.class.getResourceAsStream("/media/ablauf/fonts/monitorica/OT-tt/Monitorica-Bd.ttf"));
			} catch (Exception e) {
				e.printStackTrace();
				// TODO set standard Font to default
			}
		}
		return standardFont;
	}

	public static Font getEmojiFont() {
		if (emojiFont == null) {
			try {
				emojiFont = Font
						.createFont(Font.TRUETYPE_FONT, X.class.getResourceAsStream("/media/ablauf/fonts/OpenSansEmoji.ttf"))
						.deriveFont(16f);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO set standard Font to default
			}
		}
		return emojiFont;
	}

	public static void playAudioFile(String filename) {
		if (!MainSettings.getMainSettings().isSoundOn())
			return;
		currentAudioFile = filename;
		new Thread() {
			@Override
			public void run() {
				URL audioFile = X.class.getResource(currentAudioFile);
				try {
					AudioInputStream ais = AudioSystem
							.getAudioInputStream(audioFile);
					AudioFormat format = ais.getFormat();
					DataLine.Info info = new DataLine.Info(Clip.class, format);
					Clip clip = (Clip) AudioSystem.getLine(info);
					clip.open(ais);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				X inst = X.getInstance();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	private Bildschirm hintergrundBild;

	private Anzeige currentAnzeige;

	private Stack<AnzeigeDialog> currentDialogs = new Stack<AnzeigeDialog>();

	private Game currentGame;

	private List<KeyStroke> registeredBuzzers;

	private X() {
		super();
		registeredBuzzers = new ArrayList<KeyStroke>();
		instance = this;
		initGUI();
	}

	public void changeAnzeige(Anzeige a) {
		currentAnzeige.destroy();
		hintergrundBild.removeAll();
		currentAnzeige = a;
		hintergrundBild.add(a, BorderLayout.CENTER);
		hintergrundBild.revalidate();
		hintergrundBild.repaint();
		a.nowVisible();
	}

	public void changeBackground(String bild) {
		try{
			URL bildURL = X.class.getResource(bild);
			if(bildURL == null){
				System.err.println("Hintergrundbild "+bild+" nicht gefunden.");
				return;
			}
		}catch (Exception e){
			System.err.println("Hintergrundbild "+bild+" nicht gefunden.");
			return;
		}
			
		hintergrundBild.changePic(bild);
	}

	public void close() {
		if (currentGame != null) {
			currentGame.autoPause();
		}
		EasyDialog.showConfirm("Wirklich beenden?", null,
				new ConfirmListener() {
					@Override
					public void confirmOptionPerformed(int optionType) {
						if (optionType == YES_OPTION) {
							dispose();
							System.exit(0);
						} else {
							if (currentGame != null) {
								currentGame.autoResume();
							}
							instance.closeDialog();
						}
					}
				});
	}

	public void closeDialog() {
		currentDialogs.pop().destroy();
		hintergrundBild.removeAll();
		if (currentDialogs.empty()) {
			hintergrundBild.add(currentAnzeige, BorderLayout.CENTER);
			hintergrundBild.revalidate();
			hintergrundBild.repaint();
			currentAnzeige.nowVisible();
		} else {
			showDialog(currentDialogs.pop());
		}
	}

	private void initGUI() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				close();
			}
		});
		try {
			Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/media/ablauf/Xicon.png"));
			ImageIcon bild1 = new ImageIcon(img);
			setIconImage(bild1.getImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (mainSettings.isFullscreen()) {
				setUndecorated(true);
			}
			// setResizable(false);
			this.setTitle("X");
			{
				hintergrundBild = new gui.components.Bildschirm(
						"/media/ablauf/iceBG.jpg", true);
				getContentPane().add(hintergrundBild, BorderLayout.CENTER);
				hintergrundBild.setLayout(new BorderLayout());
				hintergrundBild.centerMe(true);
				{
					currentAnzeige = new HauptMenu();
					hintergrundBild.add(currentAnzeige, BorderLayout.CENTER);
				}
			}
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
			GraphicsDevice gd = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			gd.setFullScreenWindow(this);
			if (!mainSettings.isFullscreen()) {
				gd.setFullScreenWindow(null);
				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();
				setSize(screenSize.width, screenSize.height - 50);
				invalidate();
			}
			setBasicKeystrokes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setBasicKeystrokes() {
		JPanel content = (JPanel) this.getContentPane();
		content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
		content.getActionMap().put("pause", new ButtonPressed("pause"));
	}

	public void addBuzzer(int playerID, int keyCode) {
		JRootPane rootPane = this.getRootPane();
		rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(keyCode, 0), "buzzer" + playerID);
		rootPane.getActionMap().put("buzzer" + playerID,
				new ButtonPressed("buzzer" + playerID));
		registeredBuzzers.add(KeyStroke.getKeyStroke(keyCode, 0));
	}

	public void forgetBuzzers() {
		JPanel content = (JPanel) this.getContentPane();
		for (KeyStroke ks : registeredBuzzers) {
			content.getInputMap().put(ks, "none");
		}
	}

	private class ButtonPressed extends AbstractAction {
		private static final long serialVersionUID = 1L;
		String action;

		public ButtonPressed(String action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (action.equals("pause")) {
				if (currentGame != null)
					currentGame.togglePause();
			}
			if (action.equals("buzzer0")) {
				if (currentGame != null)
					currentGame.triggerBuzzerEvent(0);
			}
			if (action.equals("buzzer1")) {
				if (currentGame != null && currentGame.spielerZahl > 1)
					currentGame.triggerBuzzerEvent(1);
			}
			if (action.equals("buzzer2")) {
				if (currentGame != null && currentGame.spielerZahl > 2)
					currentGame.triggerBuzzerEvent(2);
			}
			if (action.equals("buzzer3")) {
				if (currentGame != null && currentGame.spielerZahl > 3)
					currentGame.triggerBuzzerEvent(3);
			}
		}
	}

	public void showDialog(AnzeigeDialog dialog) {
		hintergrundBild.removeAll();
		hintergrundBild.add(currentDialogs.push(dialog), BorderLayout.CENTER);
		hintergrundBild.revalidate();
		hintergrundBild.repaint();
	}

	public void setGame(Game game) {
		this.currentGame = game;
	}

	public String[] getQuickStartPlayers() {
		return quickStartPlayers;
	}

	public void setQuickStartPlayers(String[] quickStartPlayers) {
		this.quickStartPlayers = quickStartPlayers;
	}
}
