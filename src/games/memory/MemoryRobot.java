package games.memory;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MemoryRobot {
	private Memory memory;
	private Map<Integer, Integer> bekannteFelder;
	private boolean amZug;
	private int positionsZahl;
	private Point paar;			// (Position1, Position2)
	private Point ersterZug;	// (Position, Karte)
	private int grenzWert = 50;
	
	public int getGrenzWert() {
		return grenzWert;
	}

	public void setGrenzWert(int grenzWert) {
		this.grenzWert = grenzWert;
	}

	public MemoryRobot(Memory memory){
		this.memory = memory;
		setPositionsZahl(memory.numOfPairs*2);
		bekannteFelder = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	}
	
	public void setInfo(int position, int karte){
		bekannteFelder.put(position, karte);
		if(amZug){
			ersterZug = new Point(position, karte);
		}
	}
	
	public int getAnzugPosition(){
		amZug = true;
		paar = getFertigesPaar();
		if(paar != null){
			return paar.x;
		}
		else{
			int unbekannt = zufaelligesUnbekanntesFeld();
			if(unbekannt != -1){
				return unbekannt;
			}
			else return zufaelligesMoeglichesFeld();
		}
	}
	
	public int getWeiterzugPosition(){
		amZug = false;
		if(paar != null){
			return paar.y;
		}
		else{
			int partner = partnerVon(ersterZug);
			if(partner != -1){
				return partner;
			}
			else{
				int unbekannt = zufaelligesUnbekanntesFeld();
				if(unbekannt != -1){
					return unbekannt;
				}
				else return zufaelligesMoeglichesFeld();
			}
		}
	}
	
	private int zufaelligesMoeglichesFeld() {
		Random r = new Random();
		int zahl = 0;
		do {
			zahl = r.nextInt(positionsZahl);
			}
		while(memory.open.contains(zahl));
		return zahl;
	}

	private int zufaelligesUnbekanntesFeld(){
		if(bekannteFelder.keySet().size()>=positionsZahl){
			System.out.println("Keine unbekannten Felder mehr vorhanden");
			return -1;
		}
		Random r = new Random();
		int zahl = 0;
		do {
			zahl = r.nextInt(positionsZahl);
			}
		while(bekannteFelder.containsKey(zahl));
		return zahl;
	}

	public void setPositionsZahl(int positionsZahl) {
		this.positionsZahl = positionsZahl;
	}
	
	private Point getFertigesPaar(){
		Point p = null;
		for(int key: bekannteFelder.keySet()){
			if(memory.open.contains(key))continue;
			int value = bekannteFelder.get(key);
			int key2 = partnerVon(new Point(key, value));
			if(key2 != -1){
				p = new Point(key, key2);
			}
		}
		Random r = new Random();
		int zufall = r.nextInt(100);
		if(zufall >= grenzWert ){
			p = null;
		}
		return p;
	}

	private int findPartnerOf(int position, int value) {
		for(int key: bekannteFelder.keySet()){
			if(key == position)continue;
			if(bekannteFelder.get(key) == value){
				Random r = new Random();
				int zufall = r.nextInt(100);
				if(zufall >= grenzWert ){
					key = -1;
				}
				return key;
			}
				
		}
		
		return -1;
	}
	
	private int partnerVon(Point ersterZug){
		int partner = -1;
		int position = ersterZug.x;
		int karte = ersterZug.y;
		partner = findPartnerOf(position, karte);
		return partner;
	}

}
