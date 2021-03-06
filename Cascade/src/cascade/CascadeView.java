/**
 * @author Ben Anderson
 * @author Isaac Kubas
 * @version Spring 2021
 * 
 * View Class of Cascade project
 */
package cascade;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CascadeView extends Application implements PropertyChangeListener, EventHandler<ActionEvent> {
	
	/** the game's model */
	private CascadeModel myModel;
	
	/** a label that will display game text */
	private Label label;
	
	/** a combo box to choose the size of the game board */
	private ComboBox<Integer> combo;
	
	/** a grid that holds each game square */
	private GridPane grid;
	
	/** the buttons that get assigned to each grid location */
	private Button[][] buttonGrid;
	
	/** a button that when pressed, will clear and restart the game */
	private Button clearButton;
	
	/** a button that when pressed will rotate a preview square clockwise */
	private Button rotateCW;
	
	/** a button that when pressed will rotate a preview square counter clockwise*/
	private Button rotateCCW;
	
	/** a button that does nothing when pressed, but will display the preview square*/
	private Button previewSquareButton;
	
	private Alert alert;
	
	
	
	@Override
	/**
	 * the starting method that will initialize the view
	 */
	public void start(Stage primaryStage) {
		try {
			myModel = new CascadeModel();
			myModel.addPropertyChangeListener(this);
			previewSquareButton = new Button();
			previewSquareButton.setPrefSize(100, 100);
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			root.setPrefSize(400, 400);
			
			combo = new ComboBox<Integer>();
			for(int i = 1; i < 10; i++) { //initializing ComboBox of integers 1-10
				combo.getItems().add(i);
			}
			combo.getSelectionModel().select(4); //set default side to 5x5
			combo.setOnAction(this); //handle method setup
			
			label = new Label();
			label.setText(myModel.getGameMessage());
			grid = new GridPane();
			grid.setHgap(5); //Adding space between spaces
			grid.setVgap(5);
			buttonGrid = new Button[10][10]; //10x10 will be max size of grid
			setGrid(5); //initialize grid size to 5
			updateGrid(); //draw board
			updatePreviewSquare();
			clearButton = new Button("Clear!");
			clearButton.setOnAction(this);
			
			//initialize rotate buttons
			rotateCW = new Button();
			rotateCW.setOnAction(this);
			rotateCW.setText("Rotate Clockwise");
			rotateCCW = new Button();
			rotateCCW.setOnAction(this);
			rotateCCW.setText("Rotate Counter Clockwise");
			
			BorderPane topPane = new BorderPane();
			topPane.setTop(rotateCW);
			topPane.setBottom(rotateCCW);
			topPane.setLeft(label);
			topPane.setCenter(previewSquareButton);
			
			root.setBottom(clearButton);
			root.setLeft(combo);
			root.setCenter(grid);
			root.setTop(topPane);
			
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Oops you messed up!");
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to launch JavaFX
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void handle(ActionEvent event) {
		//Check if it's a grid button
		//Loop and check every button grid button
		for(int row = 0; row < 10; row++) { //maybe switch to actual size
			for(int col = 0; col < 10; col++) {
				if(event.getSource() == buttonGrid[row][col]) {
					myModel.place(row, col); //the user wants to place their square
				}
			}
		}
		
		//Check if it's a comboBox
		if(event.getSource() == combo) {
			ComboBox<Integer> button = (ComboBox<Integer>) event.getSource(); //create a temporary copy
			Integer buttonPressed = (Integer) button.getValue(); //[0-9] from the combo box
			myModel.updateBoardSize(buttonPressed); //the user wants to update the board size
		}
		
		//Check if it's the reset button
		if(event.getSource() == clearButton) {
			myModel.reset(); //the user wants to clear the board
		}
		
		//Check if it's the rotate clockwise
		if(event.getSource() == rotateCW) {
			myModel.rotateNextCW(); //the user wants to rotate
		}
		
		//Check if it's the rotate counter clockwise
		if(event.getSource() == rotateCCW) {
			myModel.rotateNextCCW(); //the user wants to rotate
		}
		
	}
	
	@Override
	/**
	 * The model has sent a message telling the view to change
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("size")) {
			setGrid(myModel.getSize());
			updateGrid();
		}
		if(evt.getPropertyName().equals("placed")) {
			updateGrid();
		}
		if(evt.getPropertyName().equals("occupied")) {
			alert.setContentText("This space is already occupied");
			alert.showAndWait();
		}
		if(evt.getPropertyName().equals("gameOver")) {
			alert.setContentText("The game is over! Select a new size or clear the board.");
			alert.showAndWait();
		}
		label.setText(myModel.getGameMessage()); //update the message every time
		updatePreviewSquare(); //update the preview square every time
	}
	
	/**
	 * a method that will update the preview square
	 */
	private void updatePreviewSquare() {
		Square tempSquare = myModel.getPreviewSquare(); //ask for a copy of the preview square from the model
		previewSquareButton.setText(buttonArt(tempSquare));
		if(tempSquare.getOwner() == Player.PLAYER1) {
			previewSquareButton.setStyle("-fx-background-color: #d8bfd8");
		}else if(tempSquare.getOwner() == Player.PLAYER2) {
			previewSquareButton.setStyle("-fx-background-color: #ff6984");
		}else {
			previewSquareButton.setStyle("-fx-background-color: #e6ecf2");
		}
	}
	
	/**
	 * a method that will update each square in the grid
	 */
	private void updateGrid() {
		for(int row = 0; row < myModel.getSize(); row++) { //loop through each square
			for(int col = 0; col < myModel.getSize(); col++) {
				Square tempSquare = myModel.getSquare(row, col); //ask for a copy of the current square from the model
				buttonGrid[row][col].setText(buttonArt(tempSquare));
				if(tempSquare.getOwner() == Player.PLAYER1) {
					buttonGrid[row][col].setStyle("-fx-background-color: #d8bfd8");
				}else if(tempSquare.getOwner() == Player.PLAYER2) {
					buttonGrid[row][col].setStyle("-fx-background-color: #ff6984");
				}else {
					buttonGrid[row][col].setStyle("-fx-background-color: #e6ecf2");
				}
			}
		}
	}
	
	/**
	 * @param tempSquare
	 * @return a string containing the button art
	 * this method will create a visual based on the squares directions, and priority number
	 */
	private String buttonArt(Square tempSquare) {
		if(tempSquare.getOwner() == Player.EMPTY) {
			return "";
		}
		String north = "^";
		String south = "\\/";
		String east = ">";
		String west = "<";
		String number = "" + tempSquare.getNumber();
		boolean[] directions = tempSquare.getDirections();
		if(directions[0] == false) north = "";
		if(directions[1] == false) south = "";
		if(directions[2] == false) east = "";
		if(directions[3] == false) west = "";
		
		if(tempSquare.getNumber() == -1) {
			number = "";
		}
		
		return "      " + north + "\n"
			+ west + "     " + number + "     " + east + "\n" +
		"      " + south;
	}
	
	/**
	 * this will get called when the model tells the view to change size
	 * @param size: the new size of the grid to be set
	 */
	private void setGrid(int size) {
		for(int row = 0; row < size; row++) { //loop through the grid and initialize each button
			for(int col = 0; col < size; col++) {
				buttonGrid[row][col] = new Button();
				buttonGrid[row][col].setPrefSize(100, 100);
				buttonGrid[row][col].setOnAction(this);
			}
		}
		grid.getChildren().clear(); //different from model clear
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				grid.add(buttonGrid[i][j], j, i);
			}
		}
	}
}