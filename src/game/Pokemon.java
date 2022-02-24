package game;

public class Pokemon {
	private String name;
	private String type; // Attacker, Speedster, All-Rounder, Defender, Supporter
	private int origHp;
	private int hp;
	private double atk;
	private double def;
	private int speed;
	private double hpRegen;
	private int revRate;
	private int rep; // representation
	
	private int currRowPos;
	private int currColPos;
	private int homeRowPos;
	private int homeColPos;
	
	private int pointsCtr;
	private int revCtr;
	
	private boolean active;
	
	private int[] rowPathSet;
	private int[] colPathSet;
	
	private int[] rowBattleSet;
	private int[] colBattleSet;
	
	private int[] rowMoveSet;
	private int[] colMoveSet;
	
	private int[] rowHealSet;
	private int[] colHealSet;
	
	public Pokemon(String name, String type) {
		this.revCtr = 0;
		this.active = true;
		this.pointsCtr = 1;
		this.name = name;
		this.type = type;
		switch(type) {
			case "ATTACKER":
				this.hp = 75;
				this.atk = 0.4;
				this.def = 0.15;
				this.speed = 2;
				this.hpRegen = 0.05;
				this.revRate = 2;
				break;
			case "SPEEDSTER":
				this.hp = 50;
				this.atk = 0.4;
				this.def = 0.05;
				this.speed = 3;
				this.hpRegen = 0.05;
				this.revRate = 2;
				break;
			case "ALL-ROUNDER":
				this.hp = 75;
				this.atk = 0.3;
				this.def = 0.15;
				this.speed = 2;
				this.hpRegen = 0.1;
				this.revRate = 3;
				break;
			case "DEFENDER":
				this.hp = 100;
				this.atk = 0.2;
				this.def = 0.25;
				this.speed = 1;
				this.hpRegen = 0.05;
				this.revRate = 4;
				break;
			case "SUPPORTER":
				this.hp = 100;
				this.atk = 0.2;
				this.def = 0.05;
				this.speed = 1;
				this.hpRegen = 0.15;
				this.revRate = 3;
				break;
		}
		this.origHp = this.hp;
		
		rowPathSet = new int[this.speed];
		colPathSet = new int[this.speed];
		
		rowMoveSet = new int[this.speed];
		colMoveSet = new int[this.speed];
		
		rowBattleSet = new int[this.speed];
		colBattleSet = new int[this.speed];
	}
	
	// PATH
	public int[] getRowPathSet() {
		return this.rowPathSet;
	}
	
	public void setRowPathSet(int[] rowPathSet) {
		this.rowPathSet = rowPathSet;
	}
	
	public int[] getColPathSet() {
		return this.colPathSet;
	}
	
	public void setColPathSet(int[] colPathSet) {
		this.colPathSet = colPathSet;
	}
	
	// MOVE
	public int[] getRowMoveSet() {
		return this.rowMoveSet;
	}
	
	public void setRowMoveSet(int[] rowMoveSet) {
		this.rowMoveSet = rowMoveSet;
	}
	
	public int[] getColMoveSet() {
		return this.colMoveSet;
	}
	
	public void setColMoveSet(int[] colMoveSet) {
		this.colMoveSet = colMoveSet;
	}
	
	// BATTLE
	public int[] getRowBattleSet() {
		return this.rowBattleSet;
	}
	
	public void setRowBattleSet(int[] rowBattleSet) {
		this.rowBattleSet = rowBattleSet;
	}
	
	public int[] getColBattleSet() {
		return this.colBattleSet;
	}
	
	public void setColBattleSet(int[] colBattleSet) {
		this.colBattleSet = colBattleSet;
	}
	
	// HEAL
	public int[] getRowHealSet() {
		return this.rowHealSet;
	}
	
	public void setRowHealSet(int[] rowHealSet) {
		this.rowHealSet = rowHealSet;
	}
	
	public int[] getColHealSet() {
		return this.colHealSet;
	}
	
	public void setColHealSet(int[] colHealSet) {
		this.colHealSet = colHealSet;
	}
		
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean getActive() {
		return this.active;
	}
	
	public void setPoints(int points) {
		this.pointsCtr = points;
	}
	
	public int getPoints() {
		return pointsCtr;
	}
	
	public void setRep(int rep) {
		this.rep = rep;
	}
	
	public int getRep() { // make rep string of String + intrep
		return this.rep;
	}
	
	public void setPosition(int rowPos, int colPos) {
		this.currRowPos = rowPos;
		this.currColPos = colPos;
	}
	
	public void setHomePosition(int rowPos, int colPos) {
		this.homeRowPos = rowPos;
		this.homeColPos = colPos;
	}
	
	public int getCurrRowPos() {
		return this.currRowPos;
	}
	
	public int getCurrColPos() {
		return this.currColPos;
	}
	
	public int getHomeRowPos() {
		return this.homeRowPos;
	}
	
	public int getHomeColPos() {
		return this.homeColPos;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getOrigHp() {
		return this.origHp;
	}
	
	public int getHp() {
		return this.hp;
	}
	
	public double getAtk() {
		return this.atk;
	}
	
	public double getDef() {
		return this.def;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public double getHpRegen() {
		return this.hpRegen;
	}
	
	public int getRevRate() {
		return this.revRate;
	}
	
	public int getRevCtr() {
		return this.revCtr;
	}
	
	public void setRevCtr(int i) {
		revCtr = i;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
}
