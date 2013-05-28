package util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import player.Player;
import player.PlayerFileHandler;
import start.X;

public class PlayerFileConverter {
	
	private static String playerDir = X.getDataDir() + "Player";
	private static File playerFolder = new File(playerDir);
	public static void main(String[] args) {
		ArrayList<File> playerList = getPlayerList();
		for(File f: playerList){
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				if(br.readLine().startsWith("<")){
					continue;
				}
				br.close();
				Player p = loadOldPlayer(f);
				PlayerFileHandler.savePlayer(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static ArrayList<File> getPlayerList(){
		java.util.ArrayList<File> list = new java.util.ArrayList<File>();
		if(playerFolder.exists()){
			String[] arr = playerFolder.list();
			for(int i=0; i<arr.length; i++){
				if(arr[i].endsWith(".player")){
					list.add(new File(playerDir, arr[i]));
				}
			}
		}
		return list;
	}
	private static Player loadOldPlayer(File datei){
		Player player=null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(datei));
			String name = br.readLine();
			boolean male=br.readLine().equals("true");
			Color farbe = new Color(Integer.parseInt(br.readLine()));
			int key = Integer.parseInt(br.readLine());
			String sound = br.readLine();
			br.close();
			player = new Player(name,male,farbe,key,sound);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return player;
	}
}
