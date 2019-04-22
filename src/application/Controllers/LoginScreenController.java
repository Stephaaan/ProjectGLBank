package application.Controllers;

import java.io.IOException;

import application.ApplicationState;
import application.Main;
import application.Database.Database;
import application.Models.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController {
	@FXML
	private TextField UsernameField;
	
	@FXML
	private PasswordField PasswordField;
	
	@FXML
	private Button LoginButton;
	
	@FXML
	private Label UsernameErrorField;
	
	@FXML
	private Label PasswordErrorField;
	
	@FXML
	private Label LoginErrorField;
	
	
	
	@FXML
	private void login() {
		//reset error fields
		UsernameErrorField.setText("");
		PasswordErrorField.setText("");
		LoginErrorField.setText("");
		
		boolean toLogin = true;
		//if length of text in fields is less than 1 set toLogin to false;
		if(UsernameField.getText().length() < 1) {
			toLogin = false;
			UsernameErrorField.setText("Username is required!");
		}
		if(PasswordField.getText().length() < 1) {
			toLogin = false;
			PasswordErrorField.setText("Password is required!");
		}
		//if toLogin is false break the function
		if(!toLogin) {
			return;
		}
		//TODO: check in db
		Database db = Database.getInstance();
		
		Employee employee = db.EmployeeLogin(UsernameField.getText(), PasswordField.getText());
		if(employee == null) {
			LoginErrorField.setText("Username or password wrong");
		}else {
			ApplicationState.loggedEmployee = employee;
			try {
				Stage mainStage = new Stage();
				mainStage.setTitle("GL Bank");
				Parent root = FXMLLoader.load(getClass().getResource("../View/main_window.fxml"));
				Scene scene = new Scene(root, 1366, 768);
				mainStage.setScene(scene);
				((Stage) UsernameField.getScene().getWindow()).close();
				mainStage.show();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
