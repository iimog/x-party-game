package player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Team extends Player {

	public List<String> members = Collections.synchronizedList(new ArrayList<String>());
	public int numOfMembers=0;
	public Set<String> schonDran = Collections.synchronizedSet(new HashSet<String>());

	public Team(String name) {
		super(name);
	}

	public Team(String name, boolean male) {
		super(name, male);
	}

	public Team(String name, boolean male, Color farbe) {
		super(name, male, farbe);
	}

	public Team(String name, boolean male, Color farbe, int key) {
		super(name, male, farbe, key);
	}

	public Team(String name, boolean male, Color farbe, int key, String sound) {
		super(name, male, farbe, key, sound);
	}

	public void addMember(String member){
		this.members.add(member);
		numOfMembers++;
	}

	public String nextMember(){
		Random r = new Random();
		if(schonDran.size()>=numOfMembers){
			schonDran.removeAll(schonDran);
		}
		while(true){
			int next = r.nextInt(numOfMembers);
			if(schonDran.add(members.get(next))){
				return members.get(next);
			}
		}
	}

	public void setMemberList(List<String> names) {
		members = names;
		numOfMembers = names.size();
	}

	public static Team getStandardTeam(int i) {
		Team t = null;
		if(i==0){
			t = new Team("The Poos");
			t.addMember("Tigger");
			t.addMember("Ferkel");
			t.farbe = Color.pink;
		}
		else{
			t = new Team("The Bees");
			t.addMember("Maya");
			t.addMember("Willy");
			t.farbe = Color.yellow;
		}
		return t;
	}
}
