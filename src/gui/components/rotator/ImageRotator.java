package gui.components.rotator;

import gui.components.Bildschirm;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import start.X;

public class ImageRotator extends Rotator {
	private static final long serialVersionUID = 1L;
	private boolean system = true;
	private List<String> pictureList;
	private Bildschirm bildschirm;
	
	public ImageRotator(File pictureNameFile){
		super();
		pictureList = getPictureNamesFromFile(pictureNameFile);
		initGUI();
	}
	
	private void initGUI(){
		bildschirm = new Bildschirm("");
		add(bildschirm, BorderLayout.CENTER);
	}

	private List<String> getPictureNamesFromFile(File file) {
		if(!file.exists())
			return null;
		List<String> bildPfade = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				String prefix = X.getDataDir();
				if(system)
					prefix = X.getMainDir();
				bildPfade.add(prefix+line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei: " + file
					+ " nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bildPfade;
	}

	@Override
	public void changeComponent() {
		bildschirm.changePic(pictureList.get(nextRandom(pictureList.size())));
	}

	@Override
	public void maskComponent() {
		bildschirm.changePic("");
	}
}
