package settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import start.X;

public class SettingsFileHandler {

	private static String gameSettingsDir = X.getDataDir() + "settings/games";

	public static void saveSettings(String gameName, Properties props)
			throws IOException {
		File settingsDirectory = new File(gameSettingsDir);
		if (!settingsDirectory.exists()) {
			settingsDirectory.mkdirs();
		}
		File settingsFile = new File(settingsDirectory, gameName + ".settings");
		FileOutputStream fos = new FileOutputStream(settingsFile);
		props.storeToXML(fos, gameName + " custom settings");
		fos.close();
	}

	public static Properties loadSettings(String gameName) {
		File settingsFile = new File(gameSettingsDir, gameName + ".settings");
		if (!settingsFile.exists()) {
			return null;
		}
		FileInputStream fis;
		Properties p = new Properties();
		try {
			fis = new FileInputStream(settingsFile);
			p.loadFromXML(fis);
		} catch (Exception e) {
			return null;
		}
		return p;
	}
}
