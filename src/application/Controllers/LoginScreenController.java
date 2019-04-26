package application.Controllers;

import java.io.IOException;

import application.ApplicationState;
import application.Database.Database;
import application.Models.Employee;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
	private ProgressIndicator LoadingCircle;
	
	
	@FXML
	public void initialize() {
		LoadingCircle.setVisible(false);
	}

	
	@FXML
	private void login() {
		LoginButton.setDisable(true);
		LoadingCircle.setVisible(true);
		while(!LoadingCircle.isVisible()) {
			System.out.println("waiting for a retarded circle");
		}
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

		Runnable task = getTask();
		Thread checkOnBackground = new Thread(task);
		checkOnBackground.setDaemon(true);
		checkOnBackground.start();
	
		
		
	}
	//task that executes login
	private Runnable getTask() {
		return new Runnable() {
			Employee employee = null;
			@Override
			public void run() {
				Database db = Database.getInstance();
				try {
					employee = db.EmployeeLogin(UsernameField.getText(), PasswordField.getText());
					if(employee == null) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								LoginErrorField.setText("Username or password wrong");
								LoginButton.setDisable(false);
								LoadingCircle.setVisible(false);
							}
						});
					}else {
						ApplicationState.loggedEmployee = employee;
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Stage mainStage = new Stage();
								mainStage.setTitle("GL Bank");
								Parent root;
								try {
									root = FXMLLoader.load(getClass().getResource("../View/main_window.fxml"));
									Scene scene = new Scene(root, 1366, 768);
									mainStage.setScene(scene);
									((Stage) UsernameField.getScene().getWindow()).close();
									mainStage.show();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						});
					}
				}catch(Exception ex) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							LoadingCircle.setVisible(false);
							Alert error = new Alert(AlertType.ERROR, "Error while connecting to the database. Check if server's running...", ButtonType.CLOSE);
							error.setTitle(ex.getMessage());
							error.showAndWait();
							((Stage) UsernameField.getScene().getWindow()).close();
						}
						
					});
					
				}
				
			}
		};
	}
}
