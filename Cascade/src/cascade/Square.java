package cascade;

import java.util.Random;

enum Player{
	PLAYER1, PLAYER2, EMPTY
}

public class Square {
	private Player owner;
	private int priorityNum;
	private boolean directions[] = {false, false, false, false};
	Random rand = new Random();
	
	public Square() {
		this.owner = Player.EMPTY;
		this.priorityNum = -1;
	}
	
	public Square(Player owner) {
		this.owner = owner;
		this.priorityNum = rand.nextInt(10);
		this.directions = generateDirections();
	}
	
	public void rotateCW() {
		boolean[] newDirections = {false, false, false, false};
		if(directions[0]) newDirections[2] = true;
		if(directions[1]) newDirections[3] = true;
		if(directions[2]) newDirections[1] = true;
		if(directions[3]) newDirections[0] = true;
		directions = newDirections;
	}
	public void rotateCCW() {
		boolean[] newDirections = {false, false, false, false};
		if(directions[0]) newDirections[3] = true;
		if(directions[1]) newDirections[2] = true;
		if(directions[2]) newDirections[0] = true;
		if(directions[3]) newDirections[1] = true;
		directions = newDirections;
	}
	
	public int getNumber() {
		return priorityNum;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public boolean[] getDirections() {
		return directions;
	}
	
	private void setOwner(Player owner) {
		this.owner = owner;
	}
	
	private void setPriorityNum(int priorityNum) {
		this.priorityNum = priorityNum;
	}
	
	public void flip() {
		if(owner == Player.PLAYER1) owner = Player.PLAYER2;
		else if(owner == Player.PLAYER2) owner = Player.PLAYER1;
	}
	
	private boolean[] generateDirections() {
		boolean[] randomDirections = {false, false, false, false};
		for(int i = 0; i < 4; i++) {
			randomDirections[i] = rand.nextBoolean();
		}
		return randomDirections;
	}
	
}
