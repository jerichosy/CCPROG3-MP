package game;

import java.util.Scanner;

public class Board {
	
	private String[][] grid;
	
	Scanner kb = new Scanner(System.in);
	
	public Board () {
		grid = new String[5][7];
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 7; col++) {
				grid[row][col] = "--";
			}
		}
	}
	
	public String[][] getGrid() {
		return grid;
	}
	
	public void setTeamPos(Player player, Pokemon[] pokemon) {
		for (int i = 0; i < 5; i++) {
			int rowPos = pokemon[i].getCurrRowPos();
			int colPos = pokemon[i].getCurrColPos();
			char playerRep = player.getRep();
			String pokeRep = Integer.toString(pokemon[i].getRep());
			grid[rowPos][colPos] = playerRep + pokeRep;
		}
	}
	
	public void setPokemonPos(Player player, Pokemon poke, int movRow, int movCol) {
		char playerRep = player.getRep();
		String pokeRep = Integer.toString(poke.getRep());
		grid[movRow][movCol] = playerRep + pokeRep;
	}
	
	public void resetSpace(int row, int col) {
		grid[row][col] = "--";
	}
	
	public boolean checkEnemy(Player player, int row, int col) {
		char opp = 'A';
		if (player.getUser().equals("AI"))
			opp = 'H';
		if (row != -1 && col != -1) {
			if (grid[row][col].charAt(0) == opp)
				return true;
		}
		return false;
	}
	
	public boolean checkSpace(int row, int col) {
		if (row == -1 || col == -1 || !grid[row][col].equals("--"))
			return false;
		return true;
	}
}
