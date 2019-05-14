package application;
	
import application.Database.Database;
import application.Utils.Hash;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	public static Stage AppStage;
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("View/gl-bank-login.fxml"));
		    
	        Scene scene = new Scene(root, 1366, 768);
	        
	        stage.setTitle("GL Bank");
	        stage.setScene(scene);
	        AppStage = stage;
	        stage.show();
	 		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
