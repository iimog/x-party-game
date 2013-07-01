package settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import start.X;

public class SettingsFileHandler {

	private static String gameSettingsDir = X.getDataDir() + "settings/games";

	public static void saveSettings(String gameName, Properties props){
		if(props == null){
			return;
		}
		File settingsDirectory = new File(gameSettingsDir);
		if (!settingsDirectory.exists()) {
			settingsDirectory.mkdirs();
		}
		File settingsFile = new File(settingsDirectory, gameName + ".settings");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(settingsFile);
			props.storeToXML(fos, gameName + " custom settings");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Properties loadSettings(String gameName) {
		File settingsFile = new File(gameSettingsDir, gameName + ".settings");
		if (!settingsFile.exists()) {
			return null;
		}
		System.out.println("Settings loaded from file "+settingsFile);
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
