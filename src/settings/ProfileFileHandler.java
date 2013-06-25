package settings;

import games.Modus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import start.X;

public class ProfileFileHandler {
	private static String profileDir = X.getDataDir() + "settings/profiles";
	public static final String SOLO = "Solo";
	public static final String DUELL = "Duell";
	public static final String TRIPPLE = "Tripple";
	public static final String VIERER = "Vierer";
	public static final String TEAM = "Team";
	public static final String PUNKT_MODUS = "Punktmodus";
	
	public static File saveProfile(Profile profile){
		File datei = createDatei(profile.getName(), false);
		saveProfileToFile(profile, datei);
		return datei;
	}
	
	public static File saveMatchProfile(String name, Profile profile){
		File datei = createDatei(name, true);
		saveProfileToFile(profile, datei);
		return datei;
	}
	
	private static void saveProfileToFile(Profile profile, File datei){
		Properties p = new Properties();
		p.setProperty(SOLO, profile.getSpielliste(Modus.SOLO).toString());
		p.setProperty(DUELL, profile.getSpielliste(Modus.DUELL).toString());
		p.setProperty(TRIPPLE, profile.getSpielliste(Modus.TRIPPLE).toString());
		p.setProperty(VIERER, profile.getSpielliste(Modus.VIERER).toString());
		p.setProperty(TEAM, profile.getSpielliste(Modus.TEAM).toString());
		p.setProperty(PUNKT_MODUS, profile.getPunkteModus()+"");
		try {
			FileOutputStream fos = new FileOutputStream(datei);
			p.storeToXML(fos, "Speichern erfolgreich");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static File createDatei(String name, boolean temp){
		File directory = new File(profileDir);
		if(temp){
			directory = new File(profileDir+"/temp");
		}
		if(!directory.exists()){
			directory.mkdirs();
		}
		File datei = new File(directory, name+".prof");
		return datei;
	}
	
	public static Profile loadMatchProfile(File matchFile){
		String profileFileName = matchFile.getName().replace("match","prof");
		File profileFile = new File(profileDir+"/temp", profileFileName);
		Profile profile = Profile.getDefaultProfile();
		if(profileFile.exists()){
			profile = loadProfile(profileFile);
		}
		return profile;
	}
	public static Profile loadProfile(File f){
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(f);
			p.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Profile prof = new Profile();
		prof.setName(f.getName().substring(0, f.getName().lastIndexOf('.')));
		prof.setSpielliste(rebuildList(p.getProperty(SOLO)), Modus.SOLO);
		prof.setSpielliste(rebuildList(p.getProperty(DUELL)), Modus.DUELL);
		prof.setSpielliste(rebuildList(p.getProperty(TRIPPLE)), Modus.TRIPPLE);
		prof.setSpielliste(rebuildList(p.getProperty(VIERER)), Modus.VIERER);
		prof.setSpielliste(rebuildList(p.getProperty(TEAM)), Modus.TEAM);
		String punktModus = p.getProperty(PUNKT_MODUS, "1");
		int pM = Integer.parseInt(punktModus);
		prof.setPunkteModus(pM);
		return prof;
	}
	
	private static List<Integer> rebuildList(String property) {
		List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
		String rohListe = property.substring(1, property.lastIndexOf("]"));
		StringTokenizer st = new StringTokenizer(rohListe, ",");
		while(st.hasMoreTokens()){
			String zahl = st.nextToken();
			zahl = zahl.trim();
			int echteZahl = Integer.valueOf(zahl);
			list.add(echteZahl);
		}
		return list;
	}
	
	public static File[] getProfiles(){
		File dir = new File(profileDir);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".prof")) return true;
				else return false;
			}
		};
		return dir.listFiles(filter);
	}

	public static void renameProfile(File alteMatchDatei, File neueMatchDatei) {
		String profileFileName = alteMatchDatei.getName().replace("match","prof");
		String neuerFileName = neueMatchDatei.getName().replace("match", "prof");
		File altesProfileFile = new File(profileDir+"/temp", profileFileName);
		File neuesProfileFile = new File(profileDir+"/temp", neuerFileName);
		if(altesProfileFile.exists())
			altesProfileFile.renameTo(neuesProfileFile);
	}

	public static void deleteMatchProfile(File alteMatchDatei) {
		String profileFileName = alteMatchDatei.getName().replace("match","prof");
		File profileFile = new File(profileDir+"/temp", profileFileName);
		if(profileFile.exists())profileFile.delete();
	}
}
