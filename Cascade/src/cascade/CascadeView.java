package cascade;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * Should spawn a blank window to verify that JavaFX is working
 */



public class CascadeView extends Application implements PropertyChangeListener, ChangeListener<String>, EventHandler<ActionEvent> {
	
	private CascadeModel myModel;
	
	private Label label;
	
	private ComboBox<Integer> combo;
	
	private GridPane grid;
	
	private Square previewSquare;
	
	private Button[][] buttonGrid;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			root.setPrefSize(400, 400);
			
			combo = new ComboBox<Integer>();
			for(int i = 1; i < 10; i++) { //initializing ComboBox of integers 1-10
				combo.getItems().add(i);
			}
			combo.getSelectionModel().select(2); //set default side to 3x3
			combo.setOnAction(this); //handle method setup
			
			label = new Label();
			//TODO label.setText(myModel.getFeedback());
			grid = new GridPane();
			buttonGrid = new Button[10][10]; //10x10 will be max size of grid
			for(int row = 0; row < 9; row++) {
				for(int col = 0; col < 9; col++) {
					buttonGrid[row][col] = new Button();
					buttonGrid[row][col].textProperty().addListener(this);
					buttonGrid[row][col].setPrefSize(100, 100);
				}
			}
			setGrid(3);
			Button clearButton = new Button("Clear!");
			clearButton.setOnAction(this);
			root.setBottom(clearButton);
			root.setLeft(combo);
			root.setCenter(grid);
			root.setTop(label);
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * this will get called when the model tells the view to change size
	 * @param size: the new size of the grid to be set
	 */
	private void setGrid(int size) {
		grid.getChildren().clear(); //different from model clear
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				grid.add(buttonGrid[i][j], j, i);
			}
		}
	}
}