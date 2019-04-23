package application.Controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import application.ApplicationState;
import application.Database.Database;
import application.Models.Client;
import javafx.event.Event;
import javafx.event.EventHandler;
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
	Label clientName;

	@FXML 
	Label clientEmail;

	@FXML 
	Label clientId;
	
	
	@FXML
	public void initialize() {
		clients = Database.getInstance()
						  .getAllClients();
		for(Client c:clients) {
			UserSelectBox.getItems().add(c.getId() + ".) " + c.getFname() + " " + c.getLname()); 
		}

		UserSelectBox.getSelectionModel().selectFirst();
		EmployeeNameLabel.setText(ApplicationState.loggedEmployee.toString());
		renderInfo(clients.stream().filter(item -> item.getId() == Integer.parseInt(UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0])).findFirst().get());

		UserSelectBox.setOnAction(event -> {
			renderInfo(clients.stream().filter(item -> item.getId() == Integer.parseInt(UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0])).findFirst().get());
		});
	}
	
	public void renderInfo(Client c) {
		System.out.println(c.getId());
		clientId.setText(c.getId() + "");
		clientName.setText(c.getFname() + " " + c.getLname());
		clientEmail.setText(c.getEmail());
	}
}
