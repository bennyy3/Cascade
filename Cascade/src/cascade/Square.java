package cascade;

import java.util.Random;

enum Player{
	PLAYER1, PLAYER2, EMPTY
}

public class Square {
	/** Current owner of the square */
	private Player owner;
	/** Power number which the square has */
	private int priorityNum;
	/** Boolean array of direction arrows */
	private boolean directions[] = {false, false, false, false};
	/** Random object which is used to generate random numbers and directions */
	Random rand = new Random();
	
	/**
	 * Default constructor for the square class.
	 */
	public Square() {
		this.owner = Player.EMPTY;
		this.priorityNum = -1;
	}
	
	/**
	 * Overridden constructor for the square class
	 * @param owner of the new square
	 */
	public Square(Player owner) {
		this.owner = owner;
		this.priorityNum = rand.nextInt(10);
		this.directions = generateDirections();
	}
	
	/**
	 * Rotates the square clockwise
	 */
	public void rotateCW() {
		boolean[] newDirections = {false, false, false, false};
		if(directions[0]) newDirections[2] = true;
		if(directions[1]) newDirections[3] = true;
		if(directions[2]) newDirections[1] = true;
		if(directions[3]) newDirections[0] = true;
		directions = newDirections;
	}
	/**
	 * Rotates the square counterclockwise
	 */
	public void rotateCCW() {
		boolean[] newDirections = {false, false, false, false};
		if(directions[0]) newDirections[3] = true;
		if(directions[1]) newDirections[2] = true;
		if(directions[2]) newDirections[0] = true;
		if(directions[3]) newDirections[1] = true;
		directions = newDirections;
	}
	
	/**
	 * Gets the current priority number of a square
	 * @return priorityNumber
	 */
	public int getNumber() {
		return priorityNum;
	}
	
	/**
	 * Gets the owner of the square
	 * @return owner
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Gets the direction arrow array
	 * @return directions
	 */
	public boolean[] getDirections() {
		return directions;
	}
	
	/**
	 * Sets the owner of the current square
	 * @param owner to set square to
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	/**
	 * Flips the owner of the current square to be the opposite player
	 */
	public void flip() {
		if(owner == Player.PLAYER1) owner = Player.PLAYER2;
		else if(owner == Player.PLAYER2) owner = Player.PLAYER1;
	}
	
	/**
	 * Generates a boolean array containing random directions
	 * @return random boolean array
	 */
	private boolean[] generateDirections() {
		boolean[] randomDirections = {false, false, false, false};
		for(int i = 0; i < 4; i++) {
			randomDirections[i] = rand.nextBoolean();
		}
		return randomDirections;
	}
	
}
