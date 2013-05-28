package player;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import start.X;

public class PlayerFileHandler {
	public static final String NAME = "name";
	public static final String MALE = "male";
	public static final String COLOR = "color";
	public static final String BUZZER = "buzzer";
	public static final String SOUND = "sound";

	private static String playerDir = X.getDataDir() + "Player";

	public static void savePlayer(Player player) throws IOException {
		File playerDirectory = new File(playerDir);
		if(!playerDirectory.exists()){
			playerDirectory.mkdirs();
		}
		File playerFile = new File(playerDir, player.name + ".player");
		FileOutputStream fos = new FileOutputStream(playerFile);
		Properties p = new Properties();
		p.setProperty(NAME, player.name);
		p.setProperty(MALE, String.valueOf(player.male));
		p.setProperty(COLOR, String.valueOf(player.farbe.getRGB()));
		p.setProperty(BUZZER, String.valueOf(player.getKey()));
		p.setProperty(SOUND, player.sound);
		p.storeToXML(fos, "Player Datei");
		fos.close();
	}

	public static Player loadPlayer(String name) throws IOException {
		File playerFile = new File(playerDir, name+".player");
		return loadPlayer(playerFile);
	}
	public static Player loadPlayer(File datei) throws IOException {
		if(!datei.exists()){
			return null;
		}
		Player player = null;
		FileInputStream fis = new FileInputStream(datei);
		Properties p = new Properties();
		p.loadFromXML(fis);
		String name = p.getProperty(NAME);
		String sex = p.getProperty(MALE);
		boolean male = sex.equals("true");
		Color farbe = new Color(Integer.parseInt(p.getProperty(COLOR)));
		int key = Integer.parseInt(p.getProperty(BUZZER));
		String sound = p.getProperty(SOUND);
		fis.close();
		player = new Player(name, male, farbe, key, sound);
		return player;
	}
}
