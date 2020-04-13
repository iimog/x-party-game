package games.werLuegt;

import games.Game;
import games.Modus;
import games.PC;
import games.dialogeGUIs.InfoDialog;
import games.dialogeGUIs.RoundDialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Player;
import start.X;
import util.ChangeManager;
import util.ResourceList;
import util.ResourceURLFilter;

public class WerLuegt extends Game implements PC {
	private static final long serialVersionUID = 1L;
	private static String gameName = "Wer lügt";
	static Font standardFont = new JLabel().getFont().deriveFont(40f);
	static Font labelFont = X.BUTTON_FONT.deriveFont(50f);

	private static int defaultNumOfRounds = 5;

	public static String getGameName() {
		return gameName;
	}

	private JPanel hauptbereichPanel;

	List<WerLuegtAussage> aussageListe;
	List<File> aussageFileListe;
	List<URL> aussageSystemListe;
	private JLabel aussageLabel;
	private WerLuegtRotator aktuelleAntwortRotator;
	int timeProAussage = 5;
	int current = -1;
	int whoBuzzed;
	Set<Integer> winnerIDs;
	private JPanel aktuelleAntwortPanel;
	private static String systemRoot = "/conf/pc/werLuegt/";
	private static File userFolder = new File(X.getDataDir()
			+ "games/pc/werLuegt/");

	private void initAussagen() {
		aussageFileListe = new ArrayList<File>();
		try {
			aussageSystemListe = new ArrayList<URL>();
			Set<URL> systemURLs = ResourceList
					.getResourceURLs(new ResourceURLFilter() {
						public @Override
						boolean accept(URL u) {
							String s = u.getFile();
							return s.endsWith(".aussage")
									&& s.contains(systemRoot);
						}
					});
			for (URL url : systemURLs) {
				aussageSystemListe.add(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userFolder.exists()) {
			String[] userFiles = userFolder.list();
			for (String file : userFiles) {
				if (file.endsWith(".deck")) {
					aussageFileListe.add(new File(userFolder + "/" + file));
				}
			}
		}
	}

	public WerLuegt(Player[] player, Modus modus, String background,
			int globalGameID) {
		super(player, defaultNumOfRounds, modus, background, globalGameID);
		initAussagen();
		initGUI();
	}

	private void initGUI() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		hauptbereichPanel.setOpaque(false);
		spielBereichPanel.add(hauptbereichPanel);
		addAussageLabel();
		addAktuelleAntwortPanel();
	}

	private void addAktuelleAntwortPanel() {
		aktuelleAntwortPanel = new JPanel();
		aktuelleAntwortPanel.setLayout(new GridLayout(2, 1));
		aktuelleAntwortPanel.setOpaque(false);
		aktuelleAntwortPanel.add(new JLabel());
		aktuelleAntwortRotator = new WerLuegtRotator();
		aktuelleAntwortRotator.setFont(labelFont);
		aktuelleAntwortRotator.setOpaque(true);
		aktuelleAntwortRotator.addChangeManager(new ChangeManager() {
			@Override
			public void change() {
				roundEnd(-1);
			}
		});
		aktuelleAntwortRotator.setRotationTime(timeProAussage);
		// aktuelleAntwortRotator.setHorizontalAlignment(JLabel.CENTER);
		// aktuelleAntwortRotator.setBorder(BorderFactory.createLineBorder(Color.black));
		aktuelleAntwortPanel.add(aktuelleAntwortRotator);
		hauptbereichPanel.add(aktuelleAntwortPanel, BorderLayout.SOUTH);
	}

	private void addAussageLabel() {
		aussageLabel = new JLabel("Aussage");
		aussageLabel.setOpaque(true);
		aussageLabel.setFont(labelFont);
		aussageLabel.setHorizontalAlignment(JLabel.CENTER);
		hauptbereichPanel.add(aussageLabel, BorderLayout.CENTER);
	}

	@Override
	public void buzzeredBy(int playerID) {
		setBuzzerActive(false);
		aktuelleAntwortRotator.pause();
		roundEnd(playerID);
	}

	private boolean aktuelleAussageWahr;

	public void roundEnd(int whoBuzzered) {
		aktuelleAntwortRotator.pause();
		whoBuzzed = whoBuzzered;
		aktuelleAussageWahr = aktuelleAntwortRotator.isCurrentTrue();
		winnerIDs = new HashSet<Integer>();
		if (aktuelleAussageWahr || whoBuzzered == -1) {
			// Leider zu Unrecht gebuzzert oder keiner gebuzzert
			for (int i = 0; i < spielerZahl; i++) {
				if (i != whoBuzzered)
					winnerIDs.add(i);
			}
		} else {
			// Recht gehabt, die Aussage wahr falsch
			winnerIDs.add(whoBuzzered);
		}
		verbuchePunkte(winnerIDs);
		winner = getWinnerText(winnerIDs);
		openRoundDialog(winner);
	}

	private void verbuchePunkte(Set<Integer> winnerIDs) {
		if (winnerIDs.size() < myPlayer.length) {
			for (int id : winnerIDs) {
				creds[id].earnsCredit(1);
				myPlayer[id].gameCredit++;
			}
		}
	}

	private String getWinnerText(Set<Integer> winnerIDs) {
		String winner = "";
		if (winnerIDs.size() >= myPlayer.length || winnerIDs.size() == 0) {
			winner = "niemanden";
		} else {
			int c = 0;
			for (int id : winnerIDs) {
				String trenner = "";
				if (c == winnerIDs.size() - 3)
					trenner = ", ";
				if (c == winnerIDs.size() - 2)
					trenner = " und ";
				winner += myPlayer[id].name + trenner;
				c++;
			}
		}
		return winner;
	}

	private void nextRound() {
		if (modus == Modus.TEAM)
			changeActivePlayers();
		aktuelleAussageWahr = false;
		current = nextRandom(aussageFileListe.size()
				+ aussageSystemListe.size());
		if (current == -1) {
			abbruch();
			// TODO besser handhaben!
		} else {
			setBuzzerActive(true);
			if (current < aussageFileListe.size()) {
				aktuelleAntwortRotator.changeDeckToFile(aussageFileListe
						.get(current));
			} else {
				aktuelleAntwortRotator.changeDeckToResource(aussageSystemListe
						.get(current - aussageFileListe.size()));
			}
			aussageLabel.setText(aktuelleAntwortRotator.getDeckName());
			aktuelleAntwortRotator.start();
		}
	}

	public void pause() {
		super.pause();
		aktuelleAntwortRotator.pause();
	}

	public void resume() {
		super.resume();
		aktuelleAntwortRotator.start();
	}

	@Override
	public void settingsChanged() {
		propertiesToSettings();
		updateCreds();
		if (aktuelleAntwortRotator != null)
			aktuelleAntwortRotator.setRotationTime(timeProAussage);
	}

	@Override
	protected void propertiesToSettings() {
		if (customSettings == null)
			return;
		String time = customSettings.getProperty(
				WerLuegtSettingsDialog.AUSSAGEZEIT, "5");
		timeProAussage = Integer.parseInt(time);
	}

	@Override
	public void start() {
		nextRound();
	}

	@Override
	public void openRoundDialog(String winner) {
		RoundDialog rd = new RoundDialog(this, winner);
		instance.showDialog(rd);
	}

	@Override
	public void goBack() {
		if (!isOver())
			nextRound();
	}

	@Override
	public void openSettingsDialog(boolean inGame) {
		instance.showDialog(new WerLuegtSettingsDialog(this, inGame));
	}

	@Override
	public void openDetailsDialog() {
		instance.showDialog(new WerLuegtDetailsDialog(this));
	}

	@Override
	public void openInfoDialog() {
		instance.showDialog(new InfoDialog(aktuelleAntwortRotator.getDeckInfo()));
	}

	public String getCurrentAussage() {
		return aktuelleAntwortRotator.getDeckName();
	}

	public Map<String, Boolean> getCorrectAnswers() {
		return aktuelleAntwortRotator.getCorrectAnswers();
	}

	public List<String> getVerlauf() {
		return aktuelleAntwortRotator.getVerlauf();
	}
}
