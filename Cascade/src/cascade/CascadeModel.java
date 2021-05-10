package cascade;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class CascadeModel {
	private Square[][] gameBoard;
	private int boardSize;
	private Square nextSquare;
	Player currentTurn = Player.PLAYER1;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/**
	 * Constructor for CascadeModel
	 */
	CascadeModel() {
		this.boardSize = 3;
		gameBoard = new Square[10][10];
		updateBoardSize(boardSize);
		this.currentTurn = Player.PLAYER1;
		this.nextSquare = new Square(Player.PLAYER1); 
	}
	
	private int countSpaces(Player player) {
		int playerSpaces = 0; //count of how many spaces are owned
		for(int row = 0; row < boardSize; row++) {
			for(int col = 0; col < boardSize; col++) {
				if(gameBoard[row][col].getOwner() == player) playerSpaces++;
			}
		}
		return playerSpaces;
	}
	
	public boolean isGameOver() {
		/**
		int p1Spaces = 0;
		int p2Spaces = 0;
		for(int r = 0; r < gameBoard.length; r++) {
			for(int c = 0; c < gameBoard[0].length; c++) {
				if(gameBoard[r][c].getOwner() == Player.PLAYER1) p1Spaces++;
				else if(gameBoard[r][c].getOwner() == Player.PLAYER2) p2Spaces++;
			}
		}
		*/
		int p1Spaces = countSpaces(Player.PLAYER1);
		int p2Spaces = countSpaces(Player.PLAYER2);
		if(p1Spaces + p2Spaces == boardSize*boardSize) {
			if(p1Spaces > p2Spaces) currentTurn = Player.PLAYER1;
			else if(p2Spaces > p1Spaces) currentTurn = Player.PLAYER2;
			else currentTurn = Player.EMPTY;
			return true;
		}
		return false;
	}
	
	public String getGameMessage() {
		String temp = "";
		if(currentTurn == Player.PLAYER1) {
			temp += "Player 1's Turn\n";
		}else if(currentTurn == Player.PLAYER2){
			temp += "Player 2's Turn\n";
		}else {
			temp += "Game Over!\n";
		}
		temp += "Score:\nPlayer 1: " + countSpaces(Player.PLAYER1) + "\nPlayer 2: " + countSpaces(Player.PLAYER2);
		return temp;
	}
	
	public void updateBoardSize(int boardSize) {
		for(int r = 0; r < boardSize; r++) {
			for(int c = 0; c < boardSize; c++) {
				gameBoard[r][c] = new Square();
			}
		}
	}
	
	public void reset() {
		updateBoardSize(boardSize);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
	}

	
	public void rotateNextCW() {
		nextSquare.rotateCW();
	}
	
	public void rotateNextCCW() {
		nextSquare.rotateCCW();
	}
	
	public void place(int row, int col) {
		if(gameBoard[row][col].getOwner() != Player.EMPTY) {
			throw new IllegalArgumentException("This space cannot be taken because it is already occupied.");
		}
		gameBoard[row][col] = nextSquare;
		nextSquare = new Square();
		flipTurn();
		cascade(row, col);
	}
	
	public void flipTurn() {
		if(currentTurn == Player.PLAYER1) currentTurn = Player.PLAYER2;
		else if(currentTurn == Player.PLAYER2) currentTurn = Player.PLAYER1;
	}
	
	public void cascade(int row, int col) {
		Square cur = gameBoard[row][col];
		boolean[] directions = cur.getDirections();
		for(int dir = 0; dir < 4; dir++) {
			int newRow = row;
			int newCol = col;
			int defendDir = 0;
			switch(dir) {
			case 0:
				newRow = row - 1;
				defendDir = 1;
				break;
			case 1:
				newRow = row + 1;
				defendDir = 0;
				break;
			case 2:
				newCol = col + 1;
				defendDir = 3;
				break;
			case 3:
				newCol = col - 1;
				defendDir = 2;
				break;
			}
			
			if(newRow < 0 || newRow >= boardSize) break;
			
			if(directions[dir]) {
				Square attacker = gameBoard[row][col];
				Square defender = gameBoard[newRow][newCol];
				boolean defenderArrows[] = defender.getDirections();
				if(defenderArrows[defendDir] && attacker.getNumber() > defender.getNumber()) {
					if(defender.getOwner() != attacker.getOwner()) {
						if(defender.getOwner() != Player.EMPTY) {
							defender.flip();
							cascade(newRow, newCol);
						} else if(!defenderArrows[defendDir]) {
							defender.flip();
							cascade(newRow, newCol);
						}
					}
				}
			}
		}
		return;
	}
	
}
