package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

public class Game {
	private Player player;
	private Player opp;
	private Pokemon[] pokemonListPlayer;
	private Pokemon[] pokemonListOpp;
	
	private Board board;
	
	private boolean turn; //true = player, false = ai
	private int turnCounter;
	private int BOARD_COMMANDS;

	public Game() {
		resetGame();
	}
	
	/*public void printBoard() {
		String[][] grid = board.getGrid();
		System.out.println("-----------------------------");
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				System.out.print("[" + grid[i][j] + "]");
			}
			System.out.println();
		}
		System.out.println("-----------------------------");
	}*/
	
	public void resetGame() {
		pokemonListPlayer = new Pokemon[25];
		pokemonListOpp = new Pokemon[25];
		genPokemonList(pokemonListPlayer);
		genPokemonList(pokemonListOpp);
		player = new Player("Human", 'H');
		opp = new Player("AI", 'A');
		board = new Board();
	}
	
	public void startTeamSelection() {
		selectTeam(player);
		//System.out.println("wow");
		board.setTeamPos(player, player.getTeam());
		selectTeam(opp);
		board.setTeamPos(opp, opp.getTeam());
	}
	
	public void initMatch() {
		int firstMove = ThreadLocalRandom.current().nextInt(0, 1 + 1);;
		turnCounter = 1;
		if (firstMove == 0)
			turn = true;
		else
			turn = false;
		BOARD_COMMANDS = 3;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public String[][] getBoardGrid() {
		return board.getGrid();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Player getOpponent() {
		return this.opp;
	}
	
	public int getBoardCommands() {
		return this.BOARD_COMMANDS;
	}
	
	public void setBoardCommands(int i) {
		this.BOARD_COMMANDS = i;
	}
	
	public int getTurnCounter() {
		return this.turnCounter;
	}
	
	public void setTurnCounter(int i) {
		this.turnCounter = i;
	}
	
	public boolean getTurn() {
		return this.turn;
	}
	
	public String getCurrPlayer() {
		if (turn)
			return player.getUser();
		return opp.getUser();
	}
	
	public boolean checkAlly(char ally, int row, int col) {
		String[][] grid = board.getGrid();
		if (grid[row][col].charAt(0) == ally)
			return true;
		return false;
	}
	
	public void healPokemon(Pokemon poke) {
		poke.setHp(poke.getHp() + ((int) Math.round((double) poke.getOrigHp() * 0.2)));
	}
	
	public void genPokeHealSet(Player player, Pokemon poke) {
		genPokePathSet(player, poke);
		int[] rowHealSet = poke.getRowPathSet();
		int[] colHealSet = poke.getColPathSet();
		char ally = 'H';
		if (player.getRep() == 'A')
			ally = 'A';
		for (int i = 0; i < 8; i++) {
			if (rowHealSet[i] != -1 && colHealSet[i] != -1) {
				if (!checkAlly(ally, rowHealSet[i], colHealSet[i])) {
					rowHealSet[i] = -1;
					colHealSet[i] = -1;
				}
			}
		}
		poke.setRowHealSet(rowHealSet);
		poke.setColHealSet(colHealSet);
	}
	
	public boolean hasSupporter(Player player) {
		Pokemon[] team = player.getTeam();
		for (int i = 0; i < 5; i++) {
			if (team[i].getType().equals("SUPPORTER"))
				return true;
		}
		return false;
	}
	
	public void homeRegen() {
		Pokemon[] team1 = player.getTeam();
		Pokemon[] team2 = opp.getTeam();
		for (int i = 0; i < 5; i++) {
			if (team1[i].getCurrColPos() == 0)
				team1[i].setHp(team1[i].getOrigHp());
			if (team2[i].getCurrColPos() == 6)
				team2[i].setHp(team2[i].getOrigHp());
		}
	}
	
	public void pokemonRegen() {
		for (int i = 0; i < 5; i++) {
			Pokemon[] playerTeam = player.getTeam();
			Pokemon[] oppTeam = opp.getTeam();
			Pokemon currPPoke = playerTeam[i];
			Pokemon currOPoke = oppTeam[i];
			if (currPPoke.getHp() < currPPoke.getOrigHp() && currPPoke.getActive()) {
				currPPoke.setHp(currPPoke.getHp() + ((int) Math.round((double) currPPoke.getOrigHp() * currPPoke.getHpRegen())));
				if (currPPoke.getHp() > currPPoke.getOrigHp()) {
					currPPoke.setHp(currPPoke.getOrigHp());
				}
			}
			if (currOPoke.getHp() < currOPoke.getOrigHp() && currOPoke.getActive()) {
				currOPoke.setHp(currOPoke.getHp() + ((int) Math.round((double) currOPoke.getOrigHp() * currOPoke.getHpRegen())));
				if (currOPoke.getHp() > currOPoke.getOrigHp()) {
					currOPoke.setHp(currOPoke.getOrigHp());
				}
			}
		}
	}
	
	public void pokemonRevival() {
		for (int i = 0; i < 5; i++) {
			Pokemon[] playerTeam = player.getTeam();
			Pokemon[] oppTeam = opp.getTeam();
			Pokemon currPPoke = playerTeam[i];
			Pokemon currOPoke = oppTeam[i];
			if (!currPPoke.getActive()) {
				currPPoke.setRevCtr(currPPoke.getRevCtr() + 1);
				if (currPPoke.getRevCtr() == currPPoke.getRevRate()) {
					currPPoke.setPosition(currPPoke.getHomeRowPos(), currPPoke.getHomeColPos());
					currPPoke.setRevCtr(0);
					currPPoke.setActive(true);
					currPPoke.setHp(currPPoke.getOrigHp());
				
					board.setPokemonPos(player, currPPoke, currPPoke.getHomeRowPos(), currPPoke.getHomeColPos());
				}
			}
			if (!currOPoke.getActive()) {
				currOPoke.setRevCtr(currOPoke.getRevCtr() + 1);
				if (currOPoke.getRevCtr() == currOPoke.getRevRate()) {
					currOPoke.setPosition(currOPoke.getHomeRowPos(), currOPoke.getHomeColPos());
					currOPoke.setRevCtr(0);
					currOPoke.setActive(true);
					currOPoke.setHp(currOPoke.getOrigHp());
					
					board.setPokemonPos(opp, currOPoke, currOPoke.getHomeRowPos(), currOPoke.getHomeColPos());
				}
			}
		}
	}
	
	public Pokemon getPlayerDefender() {
		Pokemon[] playerTeam = player.getTeam();
		for (int i = 0; i < 5; i++) {
			if (playerTeam[i].getType().equals("DEFENDER"))
				return playerTeam[i];
		}
		return null;
	}
	
	public boolean checkForDefender(boolean currPlayer) {		
		Pokemon[] playerTeam = player.getTeam();
		Pokemon[] oppTeam = opp.getTeam();
		
		if (!currPlayer) {
			for (int i = 0; i < 5; i++) {
				if (playerTeam[i].getType().equals("DEFENDER") && playerTeam[i].getActive())
					return true;
			}
		}
		else {
			for (int i = 0; i < 5; i++) {
				if (oppTeam[i].getType().equals("DEFENDER") && oppTeam[i].getActive())
					return true;
			}
		}
		
		return false;
	}
	
	public boolean canBattle(int[] rowBattleSet, int[] colBattleSet) {
		for (int i = 0; i < rowBattleSet.length; i++) {
			if (rowBattleSet[i] != -1 && colBattleSet[i] != -1)
				return true;
		}
		return false;
	}
	
	public boolean checkIfOpponent(int row, int col) {
		String[][] grid = board.getGrid();
		if (grid[row][col].equals("--"))
			return true;
		return false;
	}
	
	public void battleEnd(Pokemon winner, Pokemon loser) {
		winner.setPoints(winner.getPoints() + 1);
		loser.setPoints((int) Math.floor(new Double(loser.getPoints()) / 2));
		loser.setHp(0);
		loser.setRevCtr(0);
		loser.setActive(false);
		board.resetSpace(loser.getCurrRowPos(), loser.getCurrColPos());
		loser.setPosition(-1, -1);
	}
	
	public int battlePokemon(Player player, Pokemon pPoke, Player opp, Pokemon oPoke) {
		String[] moves;
		if (pPoke.getType().equals("SUPPORTER")) {
			moves = new String[4];
			moves[0] = "Attack";
			moves[1] = "Defend";
			moves[2] = "Run";
			moves[3] = "Heal";
		}
		else {
			moves = new String[3];
			moves[0] = "Attack";
			moves[1] = "Defend";
			moves[2] = "Run";
		}
		
		String title = "Your " + pPoke.getName() + " vs " + "Their " + oPoke.getName();
		String playerMove = (String) JOptionPane.showInputDialog(null, "SELECT MOVE", title, JOptionPane.QUESTION_MESSAGE, null, moves, null);
		int playerPlay = 2;
		if (playerMove.equals("Attack"))
			playerPlay = 0;
		else if (playerMove.equals("Defend"))
			playerPlay = 1;
		else if (playerMove.equals("Heal"))
			playerPlay = 3;
		
		double atkChance = 0;
		double defChance = 0;
		double runChance = 0;
		
		int oppOrigHp = oPoke.getOrigHp();
		int oppCurrHp = oPoke.getHp();
		
		if (oppCurrHp > (oppOrigHp * 0.8)) {
			atkChance = 0.75;
			defChance = 1;
		}
		else if (oppCurrHp > (oppOrigHp * 0.5)) {
			atkChance = 0.52;
			defChance = 0.95;
			runChance = 1;
		}
		else if (oppCurrHp > (oppOrigHp * 0.2)) {
			atkChance = 0.40;
			defChance = 0.85;
			runChance = 1;
		}
		else {
			defChance = 0.45;
			runChance = 1;
		}
		
		Double rand = new Random().nextDouble();
		int oppPlay;
		
		if (rand <= atkChance)
			oppPlay = 0;
		else if (rand <= defChance)
			oppPlay = 1;
		else
			oppPlay = 2;
		
		String oppMove = moves[oppPlay];
		
		System.out.println("You chose: " + playerMove + ", AI choice: " + oppMove);
		
		boolean pHasDef = false;
		boolean oHasDef = false;
		
		// DEFEND
		if (playerPlay == 1 && (oppPlay != 1 || new Random().nextDouble() <= 0.5)) {
			pHasDef = true;
			if (oppPlay == 1) {
				oHasDef = true;
			}
		}
		else if (oppPlay == 1){
			oHasDef = true;
			if (playerPlay == 1) {
				pHasDef = true;
			}
		}
		
		// ATTACK
		if (playerPlay == 0 && (oppPlay != 0 || new Random().nextDouble() <= 0.5)) {
			pokeAttack(pPoke, oPoke, oHasDef);
			
			if (oPoke.getHp() <= 0) {
				battleEnd(pPoke, oPoke);
				return 1;
			}
			
			if (oppPlay == 0) {
				pokeAttack(oPoke, pPoke, pHasDef);
				if (pPoke.getHp() <= 0) {
					battleEnd(oPoke, pPoke);
					return 2;
				}
			}
		}
		else if (oppPlay == 0){
			pokeAttack(oPoke, pPoke, pHasDef);
			
			if (pPoke.getHp() <= 0) {
				battleEnd(oPoke, pPoke);
				return 2;
			}
			
			if (playerPlay == 0) {
				pokeAttack(pPoke, oPoke, oHasDef);
				if (oPoke.getHp() <= 0) {
					battleEnd(pPoke, oPoke);
					return 1;
				}
			}
		}
		
		// HEAL if supporter
		if (playerPlay == 3) {
			healPokemon(pPoke);
		}
		
		// RUN
		if (playerPlay == 2 && (oppPlay != 2 || new Random().nextDouble() <= 0.5)) {
			rand = new Random().nextDouble();
			if (rand <= 0.4) {
				return 3;
			}
				
			if (oppPlay == 2) {
				rand = new Random().nextDouble();
				if (rand <= 0.4) {
					return 4;
				}
			}
		}
		else if (oppPlay == 2){
			rand = new Random().nextDouble();
			if (rand <= 0.4) {
				return 4;
			}
			
			if (playerPlay == 2) {
				rand = new Random().nextDouble();
				if (rand <= 0.4) {
					return 3;
				}
			}
		}
		
		return 0;
	}
	
	public void pokeAttack(Pokemon aPoke, Pokemon rPoke, boolean rHasDef) {
		int atkDmg;
		int defBuffer;
		int totalDmg;
		
		atkDmg = (int) Math.ceil((double) rPoke.getOrigHp() * aPoke.getAtk());

		defBuffer = (int) Math.ceil((double) atkDmg * rPoke.getDef());

		totalDmg = atkDmg - defBuffer;
		
		if (rHasDef) {
			rPoke.setHp(rPoke.getHp() - ((int) Math.ceil((double) totalDmg * 0.8)));
		}
		else {
			rPoke.setHp(rPoke.getHp() - totalDmg);
		}
	}
	
	public void genPokeBattleSet(Player player, Pokemon poke) {
		genPokePathSet(player, poke);
		int speed = poke.getSpeed();
		int[] rowBattleSet = poke.getRowPathSet();
		int[] colBattleSet = poke.getColPathSet();
		
		boolean isBlocked = false;
		for (int i = 0; i < speed * 8; i += speed) {
			for (int j = i; j < i + speed; j++) {
				if (isBlocked) {
					rowBattleSet[j] = -1;
					colBattleSet[j] = -1;
				}
				if (board.checkEnemy(player, rowBattleSet[j], colBattleSet[j])) {
					isBlocked = true;
				}
				else if (!board.checkSpace(rowBattleSet[j], colBattleSet[j])) {
					isBlocked = true;
					rowBattleSet[j] = -1;
					colBattleSet[j] = -1;
				}
				else {
					rowBattleSet[j] = -1;
					colBattleSet[j] = -1;
				}
			}
			isBlocked = false;
		}
		poke.setRowBattleSet(rowBattleSet);
		poke.setColBattleSet(colBattleSet);
	}
	
	public void movePokemon(Player player, Pokemon poke, int row, int col) {
		board.resetSpace(poke.getCurrRowPos(), poke.getCurrColPos());
		poke.setPosition(row,  col);
		board.setPokemonPos(player, poke, row, col);
	}
	
	public void genPokeMoveSet(Player player, Pokemon poke) {
		genPokePathSet(player, poke);
		int speed = poke.getSpeed();
		int[] rowMoveSet = poke.getRowPathSet();
		int[] colMoveSet = poke.getColPathSet();

		boolean isBlocked = false;
		for (int i = 0; i < speed * 8; i+= speed) {
			for (int j = i; j < i + speed; j++) {
				if (isBlocked) {
					rowMoveSet[j] = -1;
					colMoveSet[j] = -1;
				}
				if (board.checkEnemy(player, rowMoveSet[j], colMoveSet[j])) {
					isBlocked = true;
				}
				else if (!board.checkSpace(rowMoveSet[j], colMoveSet[j])) {
					isBlocked = true;
					rowMoveSet[j] = -1;
					colMoveSet[j] = -1;
				}		
			}
			isBlocked = false;
		}
		poke.setRowMoveSet(rowMoveSet);
		poke.setColMoveSet(colMoveSet);
	}
	
	public void genPokePathSet(Player player, Pokemon poke) {
		int currRow = poke.getCurrRowPos();
		int currCol = poke.getCurrColPos();
		int speed = poke.getSpeed();
		int[] rowPathSet = new int[speed * 8];
		int[] colPathSet = new int[speed * 8];
		int moveSetIndex = 0;
		
		// Right: currRow currCol++ until speed
		for(int i = currCol + 1; i <= currCol + speed; i++) {
			rowPathSet[moveSetIndex] = currRow;
			colPathSet[moveSetIndex] = i;
			moveSetIndex++;
		}

		// Left: currRow currCol-- until speed
		for(int i = currCol - 1; i >= currCol - speed; i--) {
			rowPathSet[moveSetIndex] = currRow;
			colPathSet[moveSetIndex] = i;
			moveSetIndex++;
		}
		
		// Up: currRow-- currCol until speed
		for(int i = currRow - 1; i >= currRow - speed; i--) {
			rowPathSet[moveSetIndex] = i;
			colPathSet[moveSetIndex] = currCol;
			moveSetIndex++;
		}
		
		// Down: currRow++ currCol until speed
		for(int i = currRow + 1; i <= currRow + speed; i++) {
			rowPathSet[moveSetIndex] = i;
			colPathSet[moveSetIndex] = currCol;
			moveSetIndex++;
		}
		
		// Up Right: currRow-- currCol++ until speed
		for(int i = currRow - 1; i >= currRow - speed; i--) {
			rowPathSet[moveSetIndex] = i;
			moveSetIndex++;
		}
		moveSetIndex -= speed;
		for(int j = currCol + 1; j <= currCol + speed; j++) {
			colPathSet[moveSetIndex] = j;
			moveSetIndex++;
		}

		// Down Right: currRow++ currCol++ until speed
		for(int i = currRow + 1; i <= currRow + speed; i++) {
			rowPathSet[moveSetIndex] = i;
			moveSetIndex++;
		}
		moveSetIndex -= speed;
		for(int j = currCol + 1; j <= currCol + speed; j++) {
			colPathSet[moveSetIndex] = j;
			moveSetIndex++;
		}
		
		// Up Left: currRow-- currCol-- until speed
		for(int i = currRow - 1; i >= currRow - speed; i--) {
			rowPathSet[moveSetIndex] = i;
			moveSetIndex++;
		}
		moveSetIndex -= speed;
		for(int j = currCol - 1; j >= currCol - speed; j--) {
			colPathSet[moveSetIndex] = j;
			moveSetIndex++;
		}

		// Down Left: currRow++ curCol-- until speed
		for(int i = currRow + 1; i <= currRow + speed; i++) {
			rowPathSet[moveSetIndex] = i;
			moveSetIndex++;
		}
		moveSetIndex -= speed;
		for(int j = currCol - 1; j >= currCol - speed; j--) {
			colPathSet[moveSetIndex] = j;
			moveSetIndex++;
		}

		// CHECK IF OUT OF BOUNDS
		for (int i = 0; i < speed * 8; i++) {
			if (rowPathSet[i] < 0 || rowPathSet[i] > 4 || colPathSet[i] < 0 || colPathSet[i] > 6) {
				rowPathSet[i] = -1;
				colPathSet[i] = -1;
			}
		}
		
		// CHECK IF ALLY/ENEMY HOME TILE
		for (int i = 0; i < speed * 8; i++) {
			if (colPathSet[i] == 0 || colPathSet[i] == 6) {
				if (colPathSet[i] == poke.getHomeColPos() && rowPathSet[i] == poke.getHomeRowPos()) {
				}
				else {
					rowPathSet[i] = -1;
					colPathSet[i] = -1;
				}
			}	
		}
		
		poke.setRowPathSet(rowPathSet);
		poke.setColPathSet(colPathSet);
	}
	
	public void changeTurn() {
		turn = !turn;
		if (turn)
			System.out.println(player.getUser() + "'s TURN!");
		else
			System.out.println(opp.getUser() + "'s TURN!");
	}
	
	public void genPokemonList(Pokemon[] pokemonList) {
		pokemonList[0] = new Pokemon("Sylveon", "ATTACKER");
		pokemonList[1] = new Pokemon("Gardevoir", "ATTACKER");
		pokemonList[2] = new Pokemon("Pikachu", "ATTACKER");
		pokemonList[3] = new Pokemon("Greninja", "ATTACKER");
		pokemonList[4] = new Pokemon("Venusaur", "ATTACKER");
		pokemonList[5] = new Pokemon("Alolan Ninetales", "ATTACKER");
		pokemonList[6] = new Pokemon("Cramorant", "ATTACKER");
		pokemonList[7] = new Pokemon("Cinderace", "ATTACKER");
		pokemonList[8] = new Pokemon("Zeraora", "SPEEDSTER");
		pokemonList[9] = new Pokemon("Talonflame", "SPEEDSTER");
		pokemonList[10] = new Pokemon("Absol", "SPEEDSTER");
		pokemonList[11] = new Pokemon("Gengar", "SPEEDSTER");
		pokemonList[12] = new Pokemon("Charizard", "ALL-ROUNDER");
		pokemonList[13] = new Pokemon("Lucario", "ALL-ROUNDER");
		pokemonList[14] = new Pokemon("Machamp", "ALL-ROUNDER");
		pokemonList[15] = new Pokemon("Garchomp", "ALL-ROUNDER");
		pokemonList[16] = new Pokemon("Mamoswine", "DEFENDER");
		pokemonList[17] = new Pokemon("Blastoise", "DEFENDER");
		pokemonList[18] = new Pokemon("Snorlax", "DEFENDER");
		pokemonList[19] = new Pokemon("Crustle", "DEFENDER");
		pokemonList[20] = new Pokemon("Slowbro", "DEFENDER");
		pokemonList[21] = new Pokemon("Blissey", "SUPPORTER");
		pokemonList[22] = new Pokemon("Eldegoss", "SUPPORTER");
		pokemonList[23] = new Pokemon("Mr. Mime", "SUPPORTER");
		pokemonList[24] = new Pokemon("Wigglytuff", "SUPPORTER");
		for (int i = 0; i < pokemonList.length; i++)
			pokemonList[i].setRep(i);
	}
	
	public void selectTeam(Player player) {
		boolean isHuman = false;
		if (player.getUser().equals("Human"))
			isHuman = true;
		
		Pokemon[] pokemonList = pokemonListOpp;
		if (isHuman)
			pokemonList = pokemonListPlayer;
		
		Pokemon[] team = new Pokemon[5];
		HashMap<String, Integer> typeCounter = new HashMap<String, Integer>();
		typeCounter.put("ATTACKER", 0);
		typeCounter.put("SPEEDSTER", 0);
		typeCounter.put("ALL-ROUNDER", 0);
		typeCounter.put("DEFENDER", 0);
		typeCounter.put("SUPPORTER", 0);
		
		String[] pokemonNames = new String[25];
		for (int i = 0; i < 25; i++) {
			pokemonNames[i] = pokemonList[i].getName();
		}
		
		int pokeCnt = 0;
		int rowPos = 0;
		do {
			int choice = 0;
			
			if (!isHuman) 
				choice = ThreadLocalRandom.current().nextInt(0, 24 + 1);
			else {
				String selected = (String) JOptionPane.showInputDialog(null, "Pokemon " + (pokeCnt+1), "SELECT 5 POKEMON", JOptionPane.QUESTION_MESSAGE, null, pokemonNames, "Poke");
				for (int i = 0; i < 25; i++) {
					if(selected.equals(pokemonList[i].getName()))
						choice = i;
				}
			}
			
			Pokemon selectedPoke = pokemonList[choice];
			
			if (checkPokemonDup(isHuman, team, selectedPoke) && checkTypeLimit(isHuman, typeCounter, selectedPoke)) {
				if (!isHuman) {
					selectedPoke.setPosition(rowPos, 6);
					selectedPoke.setHomePosition(rowPos, 6);
				}
				else {
					selectedPoke.setPosition(rowPos, 0);
					selectedPoke.setHomePosition(rowPos, 0);
				}
				rowPos++;
				team[pokeCnt] = selectedPoke;
				if (isHuman)
					System.out.println("Selected " + team[pokeCnt].getName());
				pokeCnt++;
			}
			else {
				if (isHuman)
					System.out.println(selectedPoke.getName() + " cannot be selected (already on team or reached type limit)");
			}
		} while (pokeCnt < 5);
		//SetupScreen ss = new SetupScreen();
		/*ss.getBtnSubmit().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("yes");
				ss.dispose();
			}
		});*/
		//ss.getPlayerChoices();

		player.setTeam(team);

		System.out.println(player.getUser() + " TEAM IS: ");

		for (int i = 0; i < 5; i++) {
			System.out.println(team[i].getName());
		}
	}
	
	public boolean checkPokemonDup(boolean isHuman, Pokemon[] team, Pokemon selectedPoke) {
		for (int i = 0; i < team.length; i++) {
			if (team[i] == null)
				return true;
			if (team[i].equals(selectedPoke)) {
				System.out.println(selectedPoke.getName() + " is on the team already!");
				if(isHuman)
					JOptionPane.showMessageDialog(null, selectedPoke.getName() + " is on the team already!");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkTypeLimit(boolean isHuman, HashMap<String, Integer> typeCounter, Pokemon selection) {
		String type = selection.getType();
		if (typeCounter.get(type) < 2) {
			typeCounter.put(type, typeCounter.get(type) + 1);
			return true;
		}
		System.out.println(type + " limit exceeded!");
		if (isHuman)
			JOptionPane.showMessageDialog(null, type + " limit exceeded!");
		return false;
	}
}
