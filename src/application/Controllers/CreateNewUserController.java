package application.Controllers;

import java.util.regex.Pattern;

import application.Database.Database;
import application.Models.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateNewUserController {
	
	private int passwordErrorValue = 0;
	
	private final int EMAIL_LENGTH = 5;
	@FXML
	private TextField fname;
	
	@FXML
	private TextField lname;
	
	@FXML
	private TextField email;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private PasswordField repeat_password;
	
	@FXML
	private Label register_error;
	
	@FXML
	private ProgressIndicator register_loading;
	
	@FXML
	private Label email_error;
	
	@FXML
	private Label fname_error;
	
	@FXML
	private Label lname_error;
	
	@FXML
	private Label login_error;
	
	@FXML
	private Pane password_error_first;
	
	@FXML
	private Pane password_error_second;
	
	@FXML
	private Pane password_error_third;
	
	@FXML
	private Label password_repeat_error;
	
	@FXML
	private void showPasswordError(int value) {
		// rgb(10,252,0)
		switch(value) {
			case 1:
				password_error_first.setStyle("-fx-background-color:  rgb(252,10,0)");

				password_error_second.setStyle("-fx-background-color: rgba(0,0,0,0.2)");

				password_error_third.setStyle("-fx-background-color: rgba(0,0,0,0.2)");
				break;
			case 2:
				password_error_first.setStyle("-fx-background-color:  rgb(252,70,0)");

				password_error_second.setStyle("-fx-background-color:  rgb(252,70,0)");

				password_error_third.setStyle("-fx-background-color: rgba(0,0,0,0.2)");
				break;
				
			case 3:
				password_error_first.setStyle("-fx-background-color:  rgb(10,252,0)");

				password_error_second.setStyle("-fx-background-color:  rgb(10,252,0)");

				password_error_third.setStyle("-fx-background-color: rgb(10,252,0)");
				break;
			default:
				password_error_first.setStyle("-fx-background-color: rgba(0,0,0,0.2)");

				password_error_second.setStyle("-fx-background-color: rgba(0,0,0,0.2)");

				password_error_third.setStyle("-fx-background-color: rgba(0,0,0,0.2)");
				break;
		}
	}
	
	@FXML
	private void initialize() {
		hideAllErrors();
		password.textProperty().addListener((observable, oldVal, newVal) -> {
			validatePassword();
		});
	}
	
	@FXML
	private void validatePassword() {
		
		showPasswordError(0);
		if(password.getText().length() < 6) {
			showPasswordError(0);
			return;
		}
		int errorVal = 1;
		if(Pattern.compile( "(.)*(\\d)(.)*").matcher(password.getText()).find())
			errorVal++;
		if(Pattern.compile( "[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(password.getText()).find())
			errorVal++;
		showPasswordError(errorVal);
		passwordErrorValue = errorVal;
		/*
		if(password.getText().contains("[0-9]")) {
			System.out.println("not aaaa number");
			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(password.getText());
			boolean b = m.find();
			if(b) {
				System.out.println("password ok");
				showPasswordError(3);
			}else {
				System.out.println("not special char");
				showPasswordError(2);
			}
		}else {
			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(password.getText());
			boolean b = m.find();
			if(!b) {
				System.out.println("not number");
				showPasswordError(2);
			}else {
				System.out.println("just text");
				showPasswordError(1);
			}
		} */
	}
	@FXML
	private void hideAllErrors() {
		register_error.setVisible(false);
		email_error.setVisible(false);
		fname_error.setVisible(false);
		lname_error.setVisible(false);
		login_error.setVisible(false);
		password_repeat_error.setVisible(false);
		register_loading.setVisible(false);
	}
	
	@FXML
	private void login() {
		hideAllErrors();
		if(validate()) {
			try {
				Database.getInstance().insertNewClient(new Client(fname.getText(), lname.getText(), username.getText(), 0, email.getText()), password.getText());
				((Stage) email_error.getScene().getWindow()).close();
			}catch(Exception ex) {
				System.out.println("Something went wrong, try again");
			}
			
		}
	}
	private boolean validate() {
		boolean hasError = false;
		//basic validation;
		//-email
		//TODO: error messages
		if(!email.getText().contains("@")) {
			email_error.setVisible(true);
			hasError = true;
		}
		else if(email.getText().split("@")[0].length() < EMAIL_LENGTH) {
			email_error.setVisible(true);
			hasError = true;

		}
		else if(!email.getText().split("@")[1].contains(".")) {
			hasError = true;
			email_error.setVisible(true);

		}
		else if(email.getText().split("@")[1].split(Pattern.quote("."))[0].length() < 3) {
			hasError = true;
			email_error.setVisible(true);
		}
		//6? idk
		else if(email.getText().split("@")[1].split(Pattern.quote("."))[1].length() < 2 || email.getText().split("@")[1].split(Pattern.quote("."))[1].length() > 6 ) {
			hasError = true;
			email_error.setVisible(true);

		}
		//3?
		if(fname.getText().length() < 3) {
			hasError = true;
			fname_error.setVisible(true);
			
		}
		if(lname.getText().length() < 3) {
			hasError = true;
			lname_error.setVisible(true);
		}
		if(username.getText().length() < 3) {
			hasError = true;
			login_error.setVisible(true);
		}
		if(!password.getText().equals(repeat_password.getText())) {
			hasError = true;
			password_repeat_error.setVisible(true);
		}
		if(passwordErrorValue < 2) {
			hasError = true;
		}
		System.out.println(hasError);
		
		return !hasError;
	}
}
