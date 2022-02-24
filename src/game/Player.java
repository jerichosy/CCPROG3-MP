package game;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private Pokemon[] pokemon;
	private String user;
	private char rep; //representation
	private int points;
	
	public Player (String user, char rep) {
		this.user = user;
		this.rep = rep;
		pokemon = new Pokemon[5];
		this.points = 0;
	}
	
	public void setTeam(Pokemon[] pokemon) {
		this.pokemon = pokemon;
	}
	
	public void setPoints(int i) {
		this.points = i;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public Pokemon[] getTeam() {
		return this.pokemon;
	}
	
	public Pokemon getPokemon(String name) {
		int pokeIndex = 0;
		for (int i = 0; i < 5; i++) {
			if (pokemon[i].getName().equals(name))
				pokeIndex = i;
		}
		return pokemon[pokeIndex];
	}
	
	public char getRep() {
		return this.rep;
	}
	
	public Pokemon[] getActiveTeam() {
		List<Pokemon> team = new ArrayList<Pokemon>();
		for (int i = 0; i < 5; i++) {
			if (pokemon[i].getActive())
				team.add(pokemon[i]);
		}
		Pokemon[] activeTeam = new Pokemon[team.size()];
		for (int i = 0; i < team.size(); i++) {
			activeTeam[i] = team.get(i);
		}
		return activeTeam;
	}
}
