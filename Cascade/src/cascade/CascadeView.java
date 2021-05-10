package cascade;
import javafx.application.Application;
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



public class CascadeView extends Application {
	
	private CascadeModel myModel;
	
	private Label label;
	
	private ComboBox <Integer> combo;
	
	private GridPane grid;
	
	private Square previewSquare;
	
	private Button[][] buttonGrid;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			for(int row = 0; row < 9; row++) {
				for(int col = 0; col < 9; col++) {
					
				}
			}
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}