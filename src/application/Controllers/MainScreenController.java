package application.Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.regex.Pattern;

import application.ApplicationState;
import application.Database.Database;
import application.Models.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	Label time;
	
	@FXML
	
	private void logout () {
		System.exit(0);
	}
	
	@FXML
	private void createNewUser() {
		Stage create = new Stage();
		create.setTitle("GL Bank");
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("../View/create_user.fxml"));
			Scene scene = new Scene(root, 300, 600);
			create.setScene(scene);
			create.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void initialize() {
		refreshInfo();
		UserSelectBox.getSelectionModel().selectFirst();
		EmployeeNameLabel.setText(ApplicationState.loggedEmployee.toString());
		UserSelectBox.setOnAction(event -> {
			if(UserSelectBox.getSelectionModel().getSelectedItem() == null)
				return;
			int id = Integer.parseInt(UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0]);
			Client c = clients.stream().filter(item -> item.getId() == id).findFirst().get();
			renderInfo(c);
		});
		initTime();
	}
	
	@FXML
	public void refreshInfo() {
		
		UserSelectBox.getItems().removeAll(UserSelectBox.getItems());
		
		clients = Database.getInstance()
				  .getAllClients();
		for(Client c:clients) {
			UserSelectBox.getItems().add(c.getId() + ".) " + c.getFname() + " " + c.getLname()); 
		}
		UserSelectBox.getSelectionModel().selectFirst();
	}
	public void renderInfo(Client c) {
		System.out.println(c.getId());
		clientId.setText(c.getId() + "");
		clientName.setText(c.getFname() + " " + c.getLname());
		clientEmail.setText(c.getEmail());
	}
	public void initTime() {
		//TODO: clock formatting (add 0);
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        int second = LocalDateTime.now().getSecond();
	        int minute = LocalDateTime.now().getMinute();
	        int hour = LocalDateTime.now().getHour();
	        time.setText(hour + ":" + (minute) + ":" + second);
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	}
}
