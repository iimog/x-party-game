package settings;

import gui.EasyDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import start.X;

public class MainSettings {
	private static MainSettings ms;
	public static final String FULLSCREEN = "Fullscreen";
	public static final String SOUND = "Sound";
	private String savePath = X.getDataDir() + "settings";
	private File saveFile = new File(savePath, "main.set");
	private Properties p;
	private MainSettings(){
		if(!saveFile.exists()){
			setDefaultSettings();
		}
	}
	public static MainSettings getMainSettings(){
		if(ms == null){
			return new MainSettings();
		}
		else{
			return ms;
		}
	}
	
	private void setDefaultSettings() {
		saveFile.getParentFile().mkdirs();
		p = new Properties();
		p.setProperty(FULLSCREEN, "true");
		p.setProperty(SOUND, "true");
		saveSettings();
		EasyDialog.showMessage("Die Einstellungen wurden nicht gefunden und daher zurückgesetzt.");
	}
	
	public boolean isFullscreen(){
		boolean fullscreen;
		p = getSettingProperties();
		String full = p.getProperty(FULLSCREEN);
		fullscreen = full.equals("true");
		return fullscreen;
	}
	
	public boolean isSoundOn(){
		boolean soundOn;
		p = getSettingProperties();
		String full = p.getProperty(SOUND);
		soundOn = full.equals("true");
		return soundOn;
	}

	private Properties getSettingProperties() {
		p = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(saveFile);
			p.loadFromXML(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	public void setFullscreen(boolean value) {
		p = getSettingProperties();
		p.setProperty(FULLSCREEN, String.valueOf(value));
		saveSettings();
	}
	
	private void saveSettings(){
		try {
			FileOutputStream fos = new FileOutputStream(saveFile);
			p.storeToXML(fos, "Haupteinstellungen");
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setSound(boolean value) {
		p = getSettingProperties();
		p.setProperty(SOUND, String.valueOf(value));
		saveSettings();
	}
}
