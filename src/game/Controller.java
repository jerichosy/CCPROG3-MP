package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

public class Controller {
	private View view;
	private Game game;
	private String state = " ";
	private String command = " ";
	private boolean dunk = false;
	private Pokemon currPoke = null;
	private int currPokeIndex = 0;
	private boolean gameActive = true;
	
	public Controller (View view, Game game) {
		this.view = view;
		this.game = game;
		
		this.view.addNewGameListener(new GameListener());
	}
	
	public void updateView() {
		//game.printBoard();
		view.setTurnLabel(game.getCurrPlayer());
		view.setTurnCountLabel(game.getTurnCounter());
		view.setBoardCommandsLabel(game.getBoardCommands());
		view.setBoardSquaresLabel(game.getBoardGrid());
		view.setPokeLabels(game.getPlayer().getTeam(), game.getOpponent().getTeam());
		view.setOppScore(game.getOpponent().getPoints());
		view.setPlayerScore(game.getPlayer().getPoints());
	}
	
	public void turnChange() {
		if (game.getTurnCounter() > 19) {
			gameEnd();
		}
		else {
			game.changeTurn();
			game.setTurnCounter(game.getTurnCounter() + 1);
			game.setBoardCommands(3);
			game.pokemonRevival();
			game.pokemonRegen();
			view.setBoardSquaresLabel(game.getBoardGrid());
			game.homeRegen();
		}
	}
	
	public void gameEnd() {
		gameActive = false;
		if (game.getPlayer().getPoints() > game.getOpponent().getPoints()) {
			JOptionPane.showMessageDialog(null, "YOU WON THE GAME!");
		}
		else if (game.getPlayer().getPoints() < game.getOpponent().getPoints()) {
			JOptionPane.showMessageDialog(null, "YOU LOST THE GAME!");
		}
		else {
			JOptionPane.showMessageDialog(null, "DRAW");
		}
	}
	
	public void togglePlayerCommands(boolean b) {
		view.getBtnMove().setEnabled(b);
		view.getBtnBattle().setEnabled(b);
		view.getBtnDunk().setEnabled(b);
		view.getBtnReselect().setEnabled(!b);
		if (game.hasSupporter(game.getPlayer()))
			view.getBtnHeal().setEnabled(b);
	}
	
	public void enemyTurn() {
		togglePlayerCommands(false);
		view.getBtnReselect().setEnabled(false);
		
		view.enablePokemonSpaces(game.getOpponent().getTeam());
		
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	System.out.println("ENEMY TURN");
		            	
		            	
		            	
		            	
		            	String enemyCommand = " ";
		            	
		            	            	
		            	// ENEMY SELECT POKEMON
		        		Player opp = game.getOpponent();
		        		Pokemon[] oppTeam = opp.getActiveTeam();
		        		int select = 0;
		        		Pokemon selectedPoke = null;
		        		
		        		
		        		
		        		for (int i = 0; i < oppTeam.length; i++) {
		        			if (oppTeam[i].getCurrColPos() == 1) {
		        				select = i;
		        				enemyCommand = "DUNK";
		        			}
		        		}
		        		
		        		if (enemyCommand.equals("DUNK")) {
		        			selectedPoke = oppTeam[select];
		        			if (game.checkForDefender(game.getTurn())) {
		        				Pokemon defender = game.getPlayerDefender();
		        				JOptionPane.showMessageDialog(null, "OPPONENT BATTLED YOUR DEFENDER");
		        				dunk = true;
		        				battle(opp, defender.getCurrRowPos(), defender.getCurrColPos(), selectedPoke);
		        			}
		        			else {
		        				opp.setPoints(opp.getPoints() + selectedPoke.getPoints());
		        				selectedPoke.setPoints(0);
		        				view.setOppScore(opp.getPoints());
		        				JOptionPane.showMessageDialog(null, "OPPONENT DUNKED");
		        			}
		        		}
		        		else {
		        			selectedPoke = oppTeam[ThreadLocalRandom.current().nextInt(0, oppTeam.length)];
		        			game.genPokeBattleSet(game.getOpponent(), selectedPoke);
		        			int[] rowBattleSet = selectedPoke.getRowBattleSet();
		        			int[] colBattleSet = selectedPoke.getColBattleSet();
		        			
		        			if (game.canBattle(selectedPoke.getRowBattleSet(), selectedPoke.getColBattleSet())) {
		        				for (int i = 0; i < rowBattleSet.length; i++) {
		        					if (rowBattleSet[i] != -1 && colBattleSet[i] != -1) {
		        						JOptionPane.showMessageDialog(null, "OPPONENT BATTLED YOUR POKEMON");
		        						dunk = false;
		        						battle(opp, rowBattleSet[i], colBattleSet[i], selectedPoke);
		        						break;
		        					}
		        				}
		        			}
		        			
		        			else {
		        				game.genPokeMoveSet(opp, selectedPoke);
				        		int[] rowMoveSet = selectedPoke.getRowMoveSet();
				        		int[] colMoveSet = selectedPoke.getColMoveSet();
				        		boolean hasMoved = false;
				        		do {
				        			int move = ThreadLocalRandom.current().nextInt(0, selectedPoke.getSpeed() * 8);
				        			if (rowMoveSet[move] != -1 && colMoveSet[move] != -1 && game.checkIfOpponent(rowMoveSet[move], colMoveSet[move])) {
				        				game.movePokemon(game.getOpponent(), selectedPoke, rowMoveSet[move], colMoveSet[move]);
				        				hasMoved = true;
				        			}
				        		} while (!hasMoved);
		        			}
		        		}
		        		
		        		game.setBoardCommands(game.getBoardCommands() - 1);
		        		updateView();
		        		if (game.getBoardCommands() == 0) {
		        			turnChange();
		        			updateView();
		        			togglePlayerCommands(true);
		        			view.enableBoardSpaces(false);
		        		}
		        		else 
		        			enemyTurn();    		
		            }
		        }, 
		        1000 
		);
		updateView();
	}
	
	public void battle(Player player, int i, int j, Pokemon oppSelectedPoke) {
		Pokemon oppPoke = null;
		int oppPokeIndex = 0;
		Player opp = game.getOpponent();
		Pokemon[] oppTeam = opp.getTeam();
		Pokemon[] playerTeam = game.getPlayer().getTeam();
		if (player.getUser().equals("Human")) {
			for (int k = 0; k < 5; k++) {
				if(oppTeam[k].getCurrRowPos() == i && oppTeam[k].getCurrColPos() == j) {
					oppPoke = oppTeam[k];
					oppPokeIndex = k;
				}
			}
		}
		else {
			for (int k = 0; k < 5; k++) {
				if(playerTeam[k].getCurrRowPos() == i && playerTeam[k].getCurrColPos() == j) {
					currPoke = playerTeam[k];
					currPokeIndex = k;
				}
			}
			oppPoke = oppSelectedPoke;
			for (int k = 0; k < 5; k++) {
				if(oppTeam[k].getRep() == oppPoke.getRep())
					oppPokeIndex = k;
			}
		}
		
		int outcome = 0; // 0 Game continue, 1 player win, 2 player lose, 3 Player run, 4 opp run
		view.enableBattleLabels(currPokeIndex, oppPokeIndex);
		do {
			outcome = game.battlePokemon(game.getPlayer(), currPoke, game.getOpponent(), oppPoke);
			view.setPokeLabels(game.getPlayer().getTeam(), game.getOpponent().getTeam());
			if (!dunk) {
				if (outcome == 1) {
					game.movePokemon(game.getPlayer(), currPoke, i, j);
					JOptionPane.showMessageDialog(null, "YOU WIN");
				}
				else if (outcome == 2) {
					game.movePokemon(game.getOpponent(), oppPoke, i, j);
					JOptionPane.showMessageDialog(null, "YOU LOSE");
				}
				else if (outcome == 3) {
					game.movePokemon(game.getPlayer(), currPoke, currPoke.getHomeRowPos(), currPoke.getHomeColPos());
					game.movePokemon(game.getOpponent(), oppPoke, i, j);
					JOptionPane.showMessageDialog(null, "YOU RAN");
				}
				else if (outcome == 4) {
					game.movePokemon(game.getOpponent(), oppPoke, oppPoke.getHomeRowPos(), oppPoke.getHomeColPos());
					game.movePokemon(game.getPlayer(), currPoke, i, j);
					JOptionPane.showMessageDialog(null, "OPPONENT RAN");
				}
			}
			else {
				if (outcome == 1) {
					JOptionPane.showMessageDialog(null, "YOU WIN");
				}
				else if (outcome == 2) {
					JOptionPane.showMessageDialog(null, "YOU LOSE");
				}
				else if (outcome == 3) {
					game.movePokemon(game.getPlayer(), currPoke, currPoke.getHomeRowPos(), currPoke.getHomeColPos());
					JOptionPane.showMessageDialog(null, "YOU RAN");
				}
				else if (outcome == 4) {
					game.movePokemon(game.getOpponent(), oppPoke, oppPoke.getHomeRowPos(), oppPoke.getHomeColPos());
					JOptionPane.showMessageDialog(null, "OPPONENT RAN");
				}
			}
		} while (outcome == 0);
		view.disableBattleLabels();
	}
	
	class GameListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			if (e.getSource() == view.getBtnNew()) {
				System.out.println("GAME START");
				JOptionPane.showMessageDialog(null, "GAME START!");
				
				game.resetGame();
				
				game.startTeamSelection();
				game.initMatch();
				updateView();
				view.setPokeLabels(game.getPlayer().getTeam(), game.getOpponent().getTeam());
				
				if (!game.getTurn())
					enemyTurn();
				else {
					togglePlayerCommands(true);
				}
				gameActive = true;
			}
			
			// PLAYER's TURN
			else if (game.getTurn() && gameActive) {
				
				if (e.getSource() == view.getBtnMove()) {
					view.enablePokemonSpaces(game.getPlayer().getTeam());
					togglePlayerCommands(false);
					state = "SELECT";
					command = "MOVE";
				}
				
				else if (e.getSource() == view.getBtnBattle()) {
					view.enablePokemonSpaces(game.getPlayer().getTeam());
					togglePlayerCommands(false);
					state = "SELECT";
					command = "BATTLE";
				}
				
				else if (e.getSource() == view.getBtnDunk()) {
					view.enablePokemonSpaces(game.getPlayer().getTeam());
					togglePlayerCommands(false);
					state = "SELECT";
					command = "DUNK";
				}
				
				else if (e.getSource() == view.getBtnReselect()) {
					togglePlayerCommands(true);
					view.enableBoardSpaces(false);
					state = "SELECT";
					command = " ";
				}
				
				else if (e.getSource() == view.getBtnHeal() && game.hasSupporter(game.getPlayer())) {
					view.enableSupporterSpaces(game.getPlayer().getTeam());
					togglePlayerCommands(false);
					state = "SELECT";
					command = "HEAL";
				}
				
				else if (state.equals("SELECT")) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 7; j++) {
							if (e.getSource() == view.boardSquares[i][j]) {
								Pokemon[] team = game.getPlayer().getTeam();
								for (int k = 0; k < 5; k++) {
									if (team[k].getCurrRowPos() == i && team[k].getCurrColPos() == j) {
										currPoke = team[k];
										currPokeIndex = k;
										
										if (command.equals("MOVE")) {
											game.genPokeMoveSet(game.getPlayer(), currPoke);
											view.enableBoardSpaces(false);
											view.enableMoveSetSpaces(currPoke.getRowMoveSet(), currPoke.getColMoveSet());	
											view.enableSelectedPokeSpace(currPoke);
										}
										else if (command.equals("BATTLE")) {
											game.genPokeBattleSet(game.getPlayer(), currPoke);
											view.enableBoardSpaces(false);
											view.enableBattleSetSpaces(currPoke.getRowBattleSet(), currPoke.getColBattleSet());
											view.enableSelectedPokeSpace(currPoke);
										}
										else if (command.equals("DUNK")) {
											view.enableBoardSpaces(false);
											if (j != 5) {
												JOptionPane.showMessageDialog(null, "Can't Dunk");
											}
											else if (game.checkForDefender(game.getTurn())) {
												JOptionPane.showMessageDialog(null, "Defender Present");
												view.enableDefenderSpaces(game.getOpponent().getTeam());
												command = "BATTLE";
												dunk = true;
											}
											else {
												view.enableDunkSpace(i);
												command = "DUNK";
											}
											view.enableSelectedPokeSpace(currPoke);
										}
										else if (command.equals("HEAL")) {
											game.genPokeHealSet(game.getPlayer(), currPoke);
											view.enableBoardSpaces(false);
											view.enableHealSetSpaces(currPoke.getRowHealSet(), currPoke.getColHealSet());	
											view.enableSelectedPokeSpace(currPoke);
										}
										
										
										view.getBtnReselect().setEnabled(true);
										
										state = " ";
									}
								}
							}	
						}
					}
				}
				
				else if (command.equals("MOVE")) {
					if (e.getSource() == view.boardSquares[currPoke.getCurrRowPos()][currPoke.getCurrColPos()])
						JOptionPane.showMessageDialog(null, "Can't Move to Own Tile");
					
					else {
						for (int i = 0; i < 5; i++) {
							for (int j = 0; j < 7; j++) {
								if (e.getSource() == view.boardSquares[i][j]) {
												
									Board board = game.getBoard();
									if (board.checkEnemy(game.getPlayer(), i, j)) {
										if (game.getBoardCommands() >= 2) {									
											battle(game.getPlayer(), i, j, null);

											view.enableBoardSpaces(false);
											view.setBoardSquaresLabel(game.getBoardGrid());
											
											game.setBoardCommands(game.getBoardCommands() - 2);
											if (game.getBoardCommands() == 0) {
												turnChange();
												updateView();
												togglePlayerCommands(false);
											}
										}
										else {
											JOptionPane.showMessageDialog(null, "Can't Battle, No more Board Commands");
											view.enableBoardSpaces(false);
											view.setBoardSquaresLabel(game.getBoardGrid());
										}
									}
									else {
										System.out.println("MOVE " + currPoke.getName() + " to " + i + " " + j);
										
										game.movePokemon(game.getPlayer(), currPoke, i, j);
										
										view.enableBoardSpaces(false);
										view.setBoardSquaresLabel(game.getBoardGrid());
										
										game.setBoardCommands(game.getBoardCommands() - 1);
										if (game.getBoardCommands() == 0) {
											turnChange();
											updateView();
											togglePlayerCommands(false);
										}
									}
								}
							}
						}
						if (gameActive) {
							command = " ";
							if (!game.getTurn())
								enemyTurn();
							else {
								updateView();
								togglePlayerCommands(true);
							}
						}
					}
				}
				
				else if (command.equals("BATTLE")) {
					if (e.getSource() == view.boardSquares[currPoke.getCurrRowPos()][currPoke.getCurrColPos()])
						JOptionPane.showMessageDialog(null, "Can't Battle Self");
					
					else {
						for (int i = 0; i < 5; i++) {
							for (int j = 0; j < 7; j++) {
								if (e.getSource() == view.boardSquares[i][j]) {
									battle(game.getPlayer(), i, j, null);

									view.enableBoardSpaces(false);
									view.setBoardSquaresLabel(game.getBoardGrid());
									
									game.setBoardCommands(game.getBoardCommands() - 1);
									if (game.getBoardCommands() == 0) {
										turnChange();
										updateView();
										togglePlayerCommands(false);
									}
									
								}
							}
						}
						if (gameActive) {
							command = " ";
							dunk = false;
							if (!game.getTurn())
								enemyTurn();
							else {
								updateView();
								togglePlayerCommands(true);
							}
						}
					}
				}
				
				else if (command.equals("DUNK")) {
					Player player = game.getPlayer();
					player.setPoints(player.getPoints() + currPoke.getPoints());
					currPoke.setPoints(0);
					view.setPlayerScore(player.getPoints());
					
					game.setBoardCommands(game.getBoardCommands() - 1);
					if (game.getBoardCommands() == 0) {
						turnChange();
						updateView();
						togglePlayerCommands(false);
					}
					if (gameActive) {
						command = " ";
						if (!game.getTurn())
							enemyTurn();
						else {
							updateView();
							togglePlayerCommands(true);
						}
					}
				}
				
				else if (command.equals("HEAL")) {
					if (e.getSource() == view.boardSquares[currPoke.getCurrRowPos()][currPoke.getCurrColPos()])
						JOptionPane.showMessageDialog(null, "Can't Heal Self");
					
					else {
						for (int i = 0; i < 5; i++) {
							for (int j = 0; j < 7; j++) {
								if (e.getSource() == view.boardSquares[i][j]) {
									
									Pokemon[] team = game.getPlayer().getTeam();
									for (int k = 0; k < 5; k++) {
										if (team[k].getCurrRowPos() == i && team[k].getCurrColPos() == j) {
											game.healPokemon(team[k]);
										}
									}

									view.enableBoardSpaces(false);
									view.setBoardSquaresLabel(game.getBoardGrid());
									
									game.setBoardCommands(game.getBoardCommands() - 1);
									if (game.getBoardCommands() == 0) {
										turnChange();
										updateView();
										togglePlayerCommands(false);
									}
									
								}
							}
						}
						if (gameActive) {
							command = " ";
							if (!game.getTurn())
								enemyTurn();
							else {
								updateView();
								togglePlayerCommands(true);
							}
						}
					}
				}
			}
		}
	}
}
