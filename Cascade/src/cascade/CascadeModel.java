/**
 * @author Ben Anderson
 * @author Isaac Kubas
 * @version Spring 2021
 * 
 * Model Class of Cascade project
 */
package cascade;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CascadeModel {
	/** 2D Array storing current game state */
	private Square[][] gameBoard;
	/** Current board size */
	private int boardSize;
	/** Next square to be placed */
	private Square previewSquare;
	/** Stores the player whose current turn it is */
	Player currentTurn = Player.PLAYER1;
	/** PropertyChangeSupport attribute to support the PropertyChangeListener interface */
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Constructor for CascadeModel
	 */
	CascadeModel() {
		this.boardSize = 5;
		gameBoard = new Square[10][10];
		updateBoardSize(boardSize);
		this.currentTurn = Player.PLAYER1;
		this.previewSquare = new Square(Player.PLAYER1);
	}

	/**
	 * Counts the number of spaces owned by a player
	 * @param player owning the spaces
	 * @return number of spaces owned
	 */
	private int countSpaces(Player player) {
		int playerSpaces = 0; // count of how many spaces are owned
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (gameBoard[row][col].getOwner() == player)
					playerSpaces++;
			}
		}
		return playerSpaces;
	}

	/**
	 * Checks if the game is over
	 * @return true if the game is over, false otherwise
	 */
	public boolean isGameOver() {
		int p1Spaces = countSpaces(Player.PLAYER1);
		int p2Spaces = countSpaces(Player.PLAYER2);
		if (p1Spaces + p2Spaces == boardSize * boardSize) {
			if (p1Spaces > p2Spaces)
				currentTurn = Player.PLAYER1;
			else if (p2Spaces > p1Spaces)
				currentTurn = Player.PLAYER2;
			else
				currentTurn = Player.EMPTY;
			previewSquare.setOwner(Player.EMPTY);
			return true;
		}
		return false;
	}

	/**
	 * Gets the current message to be displayed to the user
	 * @return string containing the message to be displayed
	 */
	public String getGameMessage() {
		String temp = "";
		if (isGameOver()) {
			temp += "Game Over! ";
			if (currentTurn == Player.PLAYER1)
				temp += "Player 1 Wins!\n";
			else if (currentTurn == Player.PLAYER2)
				temp += "Player 2 Wins!\n";
			else
				temp += "The Game is a Draw!\n";

		} else if (currentTurn == Player.PLAYER1) {
			temp += "Player 1's Turn\n";
		} else if (currentTurn == Player.PLAYER2) {
			temp += "Player 2's Turn\n";
		}
		temp += "Score:\nPlayer 1: " + countSpaces(Player.PLAYER1) + "\nPlayer 2: " + countSpaces(Player.PLAYER2);
		return temp;
	}
	
	/**
	 * Initializes a new board of a given size
	 * @param boardSize Size of the board to be created
	 */
	public void updateBoardSize(int boardSize) {
		for (int r = 0; r < boardSize; r++) {
			for (int c = 0; c < boardSize; c++) {
				gameBoard[r][c] = new Square();
			}
		}
		this.boardSize = boardSize;
		currentTurn = Player.PLAYER1;
		previewSquare = new Square(currentTurn);
		this.pcs.firePropertyChange("size", null, null);
	}
	
	/**
	 * Resets the current game
	 */
	public void reset() {
		currentTurn = Player.PLAYER1;
		previewSquare = new Square(currentTurn);
		updateBoardSize(boardSize);
	}
	
	/**
	 * Adds a property change listener to the model
	 * @param listener PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	
	/**
	 * Removed a property change listener to the model
	 * @param listener PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
	
	/**
	 * Rotates the next square clockwise
	 */
	public void rotateNextCW() {
		previewSquare.rotateCW();
		pcs.firePropertyChange("rotate", null, null);
	}
	
	/**
	 * Rotates the next square counterclockwise
	 */
	public void rotateNextCCW() {
		previewSquare.rotateCCW();
		pcs.firePropertyChange("rotate", null, null);
	}
	
	/**
	 * Places the current square at a given position
	 * @param row to be placed at
	 * @param col to be placed at
	 */
	public void place(int row, int col) {
		//check to see if the game is over
		if(isGameOver()) {
			pcs.firePropertyChange("gameOver", null, null);
			throw new IllegalArgumentException("game is over");
		}else if (gameBoard[row][col].getOwner() != Player.EMPTY) { //check if the square is open
			pcs.firePropertyChange("occupied", null, null);
			throw new IllegalArgumentException("This space cannot be taken because it is already occupied.");
		}
		gameBoard[row][col] = previewSquare;
		flipTurn();
		previewSquare = new Square(currentTurn);
		cascade(row, col);
		pcs.firePropertyChange("placed", null, null);
	}
	
	/**
	 * Gets the current size of the board
	 * @return current board size
	 */
	public int getSize() {
		return this.boardSize;
	}
	
	/**
	 * Gets a square at a given position from the game board.
	 * @param row to get square from
	 * @param col to get square from
	 * @return Square at the given position
	 */
	public Square getSquare(int row, int col) {
		return gameBoard[row][col];
	}
	
	/**
	 * Gets the next square to be placed
	 * @return previewSquare
	 */
	public Square getPreviewSquare() {
		return this.previewSquare;
	}
	
	/**
	 * Flips the current player to be the oppositite
	 */
	public void flipTurn() {
		if (currentTurn == Player.PLAYER1)
			currentTurn = Player.PLAYER2;
		else if (currentTurn == Player.PLAYER2)
			currentTurn = Player.PLAYER1;
	}
	
	/**
	 * Cascade method which handles cascading when a square is placed
	 * @param row of the square which was placed
	 * @param col of the square which was placed
	 */
	public void cascade(int row, int col) {
		Square cur = gameBoard[row][col];
		boolean[] directions = cur.getDirections();
		for (int dir = 0; dir < 4; dir++) {
			int newRow = row;
			int newCol = col;
			int defendDir = 0;
			switch (dir) {
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

			if (!(newRow < 0 || newRow >= boardSize) && !(newCol < 0 || newCol >= boardSize)) {
				if (directions[dir]) {
					Square attacker = gameBoard[row][col];
					Square defender = gameBoard[newRow][newCol];
					boolean defenderArrows[] = defender.getDirections();
					if (defender.getOwner() != attacker.getOwner()) {
						if (defender.getOwner() != Player.EMPTY) {
							if (!defenderArrows[defendDir]) {
								defender.flip();
								cascade(newRow, newCol);
							} else if (attacker.getNumber() > defender.getNumber()) {
								defender.flip();
								cascade(newRow, newCol);
							}
						}
					}
				}
			}
		}
		return;
	}

}
