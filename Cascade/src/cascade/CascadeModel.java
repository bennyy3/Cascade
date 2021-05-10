package cascade;

public class CascadeModel {
	private Square[][] gameBoard;
	private int boardSize;
	private Square nextSquare;
	Player currentTurn = Player.PLAYER1;

	public boolean isGameOver() {
		int p1Spaces = 0;
		int p2Spaces = 0;
		for(int r = 0; r < gameBoard.length; r++) {
			for(int c = 0; c < gameBoard[0].length; c++) {
				if(gameBoard[r][c].getOwner() == Player.PLAYER1) p1Spaces++;
				else if(gameBoard[r][c].getOwner() == Player.PLAYER2) p2Spaces++;
			}
		}
		if(p1Spaces + p2Spaces == boardSize*boardSize) {
			if(p1Spaces > p2Spaces) currentTurn = Player.PLAYER1;
			else if(p2Spaces > p1Spaces) currentTurn = Player.PLAYER2;
			else currentTurn = Player.EMPTY;
			return true;
		}
		return false;
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
