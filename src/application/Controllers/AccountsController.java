package application.Controllers;

import application.ApplicationState;
import javafx.fxml.FXML;

public class AccountsController {
	@FXML
	public void initialize() {
		if(ApplicationState.client != null) {
			System.out.println(ApplicationState.client.getId());
		}
	}
	@FXML
	public void createNewUser() {}
}
