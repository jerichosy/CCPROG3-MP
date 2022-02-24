package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class View extends JFrame {
    private JPanel gui = new JPanel(new BorderLayout(3, 3));  // Top level UI
    private JPanel board;
    public JButton[][] boardSquares = new JButton[5][7];
    
    // Tool bar
    private JToolBar toolBar;
    private JLabel lblCurrTurn;
    private JLabel lblTurnCnt;
    private JLabel lblBoardCommands;
    private JLabel lblPlayerScore;
    private JLabel lblOppScore;
    
    // Poke Labels
    private JLabel[] playerTeam = new JLabel[5];
    
    private JLabel[] oppTeam = new JLabel[5];
    
    // Buttons
    private JButton btnNew;
    private JButton btnReselect;
    private JButton btnMove;
    private JButton btnBattle;
    private JButton btnDunk;
    private JButton btnHeal;
    
    // Image Array
    private Image[] pieceImages = new Image[25];
    private JPanel panel_right;
    private JPanel panel_left;
    private JPanel panel_bottom;

    public View() {
        initGUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 720);
        setResizable(false);
        setVisible(true);
    }
    
    public void initGUI () {
    	gui.setBorder(new EmptyBorder(5, 5, 5, 5));
    	initToolbar();
    	
    	board = new JPanel(new GridLayout(0, 7));
        board.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new LineBorder(Color.BLACK)
        ));
        
        Color bgColor = new Color(204,119,34);
        board.setBackground(Color.BLUE);
        GridBagLayout gbl_boardConstrain = new GridBagLayout();
        gbl_boardConstrain.rowWeights = new double[]{1.0};
        gbl_boardConstrain.columnWeights = new double[]{1.0, 0.0, 1.0};
        JPanel boardConstrain = new JPanel(gbl_boardConstrain);
        boardConstrain.setBackground(Color.WHITE);
        
        panel_left = new JPanel( new GridLayout(0, 1) );
        GridBagConstraints gbc_panel_left = new GridBagConstraints();
        gbc_panel_left.insets = new Insets(0, 0, 0, 5);
        gbc_panel_left.fill = GridBagConstraints.BOTH;
        gbc_panel_left.gridx = 0;
        gbc_panel_left.gridy = 0;
        
        boardConstrain.add(panel_left, gbc_panel_left);
        GridBagConstraints gbc_board = new GridBagConstraints();
        gbc_board.insets = new Insets(0, 0, 0, 5);
        gbc_board.gridx = 1;
        gbc_board.gridy = 0;
        boardConstrain.add(board, gbc_board);
        gui.add(boardConstrain);
        
        panel_right = new JPanel( new GridLayout(0, 1) );
        GridBagConstraints gbc_panel_right = new GridBagConstraints();
        gbc_panel_right.fill = GridBagConstraints.BOTH;
        gbc_panel_right.gridx = 2;
        gbc_panel_right.gridy = 0;
        boardConstrain.add(panel_right, gbc_panel_right);
        
        panel_bottom = new JPanel( new GridLayout(0, 1) );
        GridBagConstraints gbc_panel_bottom = new GridBagConstraints();
        gbc_panel_bottom.fill = GridBagConstraints.BOTH;
        gbc_panel_bottom.gridx = 1;
        gbc_panel_bottom.gridy = 2;
        boardConstrain.add(panel_bottom, gbc_panel_bottom);
        
        // Command Buttons
        btnReselect = new JButton("Reselect");
        btnReselect.setEnabled(false);
        panel_bottom.add(btnReselect);
        
        btnMove = new JButton("MOVE");
        btnMove.setEnabled(false);
        panel_bottom.add(btnMove);
        
        btnBattle = new JButton("BATTLE");
        btnBattle.setEnabled(false);
        panel_bottom.add(btnBattle);
        
        btnDunk = new JButton("DUNK");
        btnDunk.setEnabled(false);
        panel_bottom.add(btnDunk);
        
        btnHeal = new JButton("HEAL");
        btnHeal.setEnabled(false);
        panel_bottom.add(btnHeal);

        getContentPane().add(gui);
        
        initBoardBtns();
        initPokeLabels();
    }
    
    public void initToolbar () {
    	// Toolbar stuff
    	toolBar = new JToolBar();
        toolBar.setFloatable(false);
        gui.add(toolBar, BorderLayout.PAGE_START);
        btnNew = new JButton("New");
        toolBar.add(btnNew);

        // Labels
        lblCurrTurn = new JLabel("Current Turn: ");
        lblCurrTurn.setBorder(new EmptyBorder(0,10,0,0));
		toolBar.add(lblCurrTurn);
		
		lblTurnCnt = new JLabel("Turn: 0");
		lblTurnCnt.setBorder(new EmptyBorder(0,10,0,0));
		toolBar.add(lblTurnCnt);
		
		lblBoardCommands = new JLabel("Board Commands Remaining: 3");
		lblBoardCommands.setBorder(new EmptyBorder(0,10,0,0));
		toolBar.add(lblBoardCommands);
		
		lblPlayerScore = new JLabel("Player Score: 0");
		lblPlayerScore.setBorder(new EmptyBorder(0,10,0,0));
		toolBar.add(lblPlayerScore);
		
		lblOppScore = new JLabel("Opponent Score: 0");
		lblOppScore.setBorder(new EmptyBorder(0,10,0,0));
		toolBar.add(lblOppScore);
    }
    
    public void initBoardBtns () {
    	// Create board squares/tiles
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                JButton b = new JButton();

                b.setMargin(buttonMargin);

                // set transparent image, also sets the size of the button (96px)
                ImageIcon icon = new ImageIcon(new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                b.setText("");
                b.setBackground(Color.white);
                b.setEnabled(false);

                boardSquares[i][j] = b;

                // Add board squares/tiles to board
                board.add(boardSquares[i][j]);
            }
        }
        loadImage();
    }
    
    
    private void loadImage() {
        // Texture atlas is the best way but difficult
        for (int i = 0; i < 25; i++) {
            BufferedImage img = null;
            String filePath = "/poke-" + i + ".png";
            try {
                img = ImageIO.read(getClass().getResource(filePath));
                pieceImages[i] = img;
            } catch (IllegalArgumentException | IOException e) {
                System.err.println("Error loading image: " + filePath);
            }
        }
        
    }
    
    public void initPokeLabels() {
    	for (int i = 0; i < 5; i++) {
    		playerTeam[i] = new JLabel(" ");
            playerTeam[i].setBorder(new EmptyBorder(0,10,0,0));
            playerTeam[i].setOpaque(true);
    		panel_left.add(playerTeam[i]);
    		
    		oppTeam[i] = new JLabel(" ");
    		oppTeam[i].setBorder(new EmptyBorder(0,10,0,0));
    		oppTeam[i].setOpaque(true);
    		panel_right.add(oppTeam[i]);
    	}
    }
    
    public void setPokeLabels(Pokemon[] pTeam, Pokemon[] oTeam) {
    	for (int i = 0; i < 5; i++) {
    		Pokemon pPoke = pTeam[i];
    		String pName = pPoke.getName();
    		int pHp = pPoke.getHp();
    		int pPoints = pPoke.getPoints();
    		playerTeam[i].setText("<html>" + pName + "<br>HP: " + pHp + "<br>Points: " + pPoints + "</html>");
    		
    		Pokemon oPoke = oTeam[i];
    		String oName = oPoke.getName();
    		int oHp = oPoke.getHp();
    		int oPoints = oPoke.getPoints();
    		oppTeam[i].setText("<html>" + oName + "<br>HP: " + oHp + "<br>Points: " + oPoints + "</html>");
    		
    	}
    }
    
    public void enableBattleLabels(int pIndex, int oIndex) {
    	playerTeam[pIndex].setBackground(Color.CYAN);
    	oppTeam[oIndex].setBackground(Color.ORANGE);
    }
    
    public void disableBattleLabels() {
    	for (int i = 0; i < 5; i++) {
    		playerTeam[i].setBackground(Color.WHITE);
        	oppTeam[i].setBackground(Color.WHITE);
    	}
    }
    
    public void setBoardSquaresLabel(String[][] board) {
    	for (int i = 0; i < 5; i++) {
    		for (int j = 0; j < 7; j++) {
    			int rep = getPokeRep(board[i][j]);
    			if (rep == -1)
    				boardSquares[i][j].setIcon(null);
    			else
    				boardSquares[i][j].setIcon(new ImageIcon(pieceImages[rep]));		
    		}
    	}
    }
    
    public void enableBoardSpaces(boolean b) {
		for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
            	boardSquares[i][j].setEnabled(b);
            	boardSquares[i][j].setBackground(Color.white);
            }   
    	}
    }
    
    public void enableBattleSetSpaces(int[] rowBattleSet, int[] colBattleSet) {
    	int battleCnt = 0;
    	for (int i = 0; i < rowBattleSet.length; i++) {
    		if (rowBattleSet[i] != -1 && colBattleSet[i] != -1) {
    			boardSquares[rowBattleSet[i]][colBattleSet[i]].setEnabled(true);
    			boardSquares[rowBattleSet[i]][colBattleSet[i]].setBackground(Color.RED);
    			battleCnt++;
    		}
    	}
    	if (battleCnt == 0) {
    		System.out.println("NO POKEMON TO BATTLE");
    	}
    }
    
    public void enableDunkSpace(int row) {
    	boardSquares[row][6].setEnabled(true);
		boardSquares[row][6].setBackground(Color.BLUE);
    }
    
    public void enableDefenderSpaces(Pokemon[] oppTeam) {
    	for (int i = 0; i < 5; i++) {
    		if (oppTeam[i].getType().equals("DEFENDER")) {
    			if (oppTeam[i].getCurrColPos() == 6) {
    				Pokemon poke = oppTeam[i];
        			boardSquares[poke.getCurrRowPos()][6].setEnabled(true);
        			boardSquares[poke.getCurrRowPos()][6].setBackground(Color.RED);
    			}
    		}
    	}
    }
    
    public void enableMoveSetSpaces(int[] rowMoveSet, int[] colMoveSet) {
    	for (int i = 0; i< rowMoveSet.length; i++) {
    		if (rowMoveSet[i] != -1 && colMoveSet[i] != -1) {
    			boardSquares[rowMoveSet[i]][colMoveSet[i]].setEnabled(true);
    			boardSquares[rowMoveSet[i]][colMoveSet[i]].setBackground(Color.GREEN);
    		}
    	}
    }
    
    public void enableSelectedPokeSpace(Pokemon poke) {
    	int row = poke.getCurrRowPos();
		int col = poke.getCurrColPos();
		boardSquares[row][col].setEnabled(true);
		boardSquares[row][col].setBackground(Color.WHITE);

    }
    
    public void enablePokemonSpaces(Pokemon[] team) {
    	for (int i = 0; i < 5; i++) {
    		int row = team[i].getCurrRowPos();
    		int col = team[i].getCurrColPos();
    		if (row != -1 && col != -1) {
    			boardSquares[row][col].setEnabled(true);
        		boardSquares[row][col].setBackground(Color.WHITE);
    		}
    	}
    }
    
    public void enableSupporterSpaces(Pokemon[] team) {
    	for (int i = 0; i < 5; i++) {
    		if (team[i].getType().equals("SUPPORTER")) {
    			int row = team[i].getCurrRowPos();
        		int col = team[i].getCurrColPos();
        		boardSquares[row][col].setEnabled(true);
        		boardSquares[row][col].setBackground(Color.WHITE);
    		}
    	}
    }
    
    public void enableHealSetSpaces(int[] rowHealSet, int[] colHealSet) {
    	for (int i = 0; i< rowHealSet.length; i++) {
    		if (rowHealSet[i] != -1 && colHealSet[i] != -1) {
    			boardSquares[rowHealSet[i]][colHealSet[i]].setEnabled(true);
    			boardSquares[rowHealSet[i]][colHealSet[i]].setBackground(Color.PINK);
    		}
    	}
    }
    
    public int getPokeRep(String rep) {
    	if (rep.equals("--"))
    		return -1;
    	return Integer.parseInt(rep.substring(1));
    }
    
    public void setTurnLabel (String turn) {
    	lblCurrTurn.setText("Current Turn: " + turn);
    }
    
    public void setTurnCountLabel (int i) {
    	lblTurnCnt.setText("Turn: " + i);
    }
    
    public void setBoardCommandsLabel (int i) {
    	lblBoardCommands.setText("Board Commands Remaining: " + i);
    }
    
    public JButton getBtnNew() {
    	return btnNew;
    }
    
    public JButton getBtnMove() {
    	return btnMove;
    }
    
    public JButton getBtnReselect() {
    	return btnReselect;
    }
    
    public JButton getBtnBattle() {
    	return btnBattle;
    }
    
    public JButton getBtnDunk() {
    	return btnDunk;
    }
    
    public JButton getBtnHeal() {
    	return btnHeal;
    }
    
    public void setPlayerScore(int i) {
    	lblPlayerScore.setText("Player Score: " + i);
    }
    
    public void setOppScore(int i) {
    	lblOppScore.setText("Opponent Score: " + i);
    }
    
    void addNewGameListener (ActionListener listenForButton) {
    	btnNew.addActionListener(listenForButton);
    	btnReselect.addActionListener(listenForButton);
    	btnMove.addActionListener(listenForButton);
    	btnBattle.addActionListener(listenForButton);
    	btnDunk.addActionListener(listenForButton);
    	btnHeal.addActionListener(listenForButton);
    	for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
            	boardSquares[i][j].addActionListener(listenForButton);
            }   
    	}
    }
}
