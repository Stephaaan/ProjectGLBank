package application.Controllers;

import java.util.LinkedList;

import application.ApplicationState;
import application.Database.Database;
import application.Models.Client;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class MainScreenController {
	LinkedList<Client> clients;
	
	@FXML
	ChoiceBox<String> UserSelectBox;
	
	@FXML
	Label EmployeeNameLabel;
	
	@FXML
	public void initialize() {
		System.out.println(ApplicationState.loggedEmployee);
		clients = Database.getInstance().getAllClients();
		for(Client c:clients) {
			UserSelectBox.getItems().add(c.getId() + ".) " + c.getFname() + " " + c.getLname()); 
			UserSelectBox.getSelectionModel().selectFirst();
		}
		EmployeeNameLabel.setText(ApplicationState.loggedEmployee.toString());
	}
}
