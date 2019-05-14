package application.Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.regex.Pattern;

import application.ApplicationState;
import application.PasswordGenerator;
import application.Tools;
import application.Database.Database;
import application.Models.Account;
import application.Models.Card;
import application.Models.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	
	/* account */
	@FXML
	ChoiceBox<String> accounts_accounts;
	
	@FXML 
	Label account_accountnumber;
	
	@FXML
	Label account_amount;
	
	/*InternetBanking*/
	
	@FXML
	Label ib_login;
	
	
	/*end of internet banking*/
	/**/
	/*Cards*/
	@FXML
	ChoiceBox<String> card_accounts;
	
	@FXML 
	ChoiceBox<String> card_cards;
	
	@FXML
	Label card_status;
	
	@FXML
	Label card_expiry;
	
	@FXML
	
	Button cards_deactivate;
	/*Transactions*/
	@FXML
	ChoiceBox<String> transaction_accounts;
	
	@FXML
	TextField transaction_deposit;
	
	@FXML
	TextField transaction_widthraw;
	/*end of transactions*/
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
	public void deposit() {
		try {
			Float.parseFloat(transaction_deposit.getText());
		}catch(Exception ex){
			Alert alert = new Alert(AlertType.ERROR,"Deposit amount must be a number!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(transaction_accounts.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR,"Account not selected!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(Float.parseFloat(transaction_deposit.getText()) < 0) {
			Alert alert = new Alert(AlertType.ERROR,"Cannot deposit negative amount of money!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(Float.parseFloat(transaction_deposit.getText()) < 0.01) {
			Alert alert = new Alert(AlertType.ERROR,"Cannot deposit less than 0.01€!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to deposit " + transaction_deposit.getText() + "€", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.YES) {
		    Database.getInstance().deposit(Float.parseFloat(transaction_deposit.getText()), transaction_accounts.getSelectionModel().getSelectedItem());
		    initialize();
		}
	}
	@FXML
	public void widthraw() {
		try {
			Float.parseFloat(transaction_widthraw.getText());
		}catch(Exception ex){
			Alert alert = new Alert(AlertType.ERROR,"Widthraw amount must be a number!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(transaction_accounts.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR,"Account not selected!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(Float.parseFloat(transaction_widthraw.getText()) < 0) {
			Alert alert = new Alert(AlertType.ERROR,"Cannot widthraw negative amount of money!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(Float.parseFloat(transaction_widthraw.getText()) < 0.01) {
			Alert alert = new Alert(AlertType.ERROR,"Cannot widthraw less than 0.01€!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		if(Database.getInstance().getAmountOfAccount(transaction_accounts.getSelectionModel().getSelectedItem()) - Float.parseFloat(transaction_widthraw.getText()) < 0) {
			Alert alert = new Alert(AlertType.ERROR,"Not enought amount of money!",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to widthraw " + transaction_widthraw.getText() + "€", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.YES) {
		    Database.getInstance().widthraw(Float.parseFloat(transaction_widthraw.getText()), transaction_accounts.getSelectionModel().getSelectedItem());
		    initialize();
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
			ApplicationState.client = c;
			
			ib_login.setText(c.getLogin());
			renderInfo(c);
		});
		
		initTime();
		
		accounts_accounts.setOnAction(event -> {
			if(UserSelectBox.getSelectionModel().getSelectedItem() == null)
				return;
			account_accountnumber.setText(accounts_accounts.getSelectionModel().getSelectedItem());
			//System.out.println(Database.getInstance().getAmountOfAccount(accounts_accounts.getSelectionModel().getSelectedItem()));
			account_amount.setText(Database.getInstance().getAmountOfAccount(accounts_accounts.getSelectionModel().getSelectedItem()) + "€");
		});
		
		card_accounts.setOnAction(event -> {
			if(UserSelectBox.getSelectionModel().getSelectedItem() == null)
				return;
			card_cards.getItems().clear();
			LinkedList<Card> cards = Database.getInstance().getCardsOfAccount(card_accounts.getSelectionModel().getSelectedItem());
			cards.stream().forEach(item -> card_cards.getItems().add(item.getId() + ".) " + (item.getExpirem() < 10?"0":"") + item.getExpirem() + "/" + (item.getExpirey() < 10?"0":"") + item.getExpirey()));
			
		});
		card_cards.setOnAction(event -> {
			if(card_cards.getSelectionModel().getSelectedItem() == null){
				return;
			}
			Card toRender = Database.getInstance().getCardInfo(Integer.parseInt(card_cards.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0]));
			renderCardActive(toRender.isActive());
			card_expiry.setText((toRender.getExpirem() < 10?"0":"") + toRender.getExpirem() + "/" + (toRender.getExpirey() < 10?"0":"") + toRender.getExpirey());
			
		});
		int id = Integer.parseInt(UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0]);
		Client c = clients.stream().filter(item -> item.getId() == id).findFirst().get();
		ib_login.setText(c.getLogin());
		
	}
	public void renderCardActive(boolean isActive) {
		cards_deactivate.setText(isActive?"Deactivate":"Activate");
		card_status.setText(isActive?"active":"deactivated");
		card_status.setStyle("-fx-text-fill:" + (isActive?"green":"red"));
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
		LinkedList<Account> accounts = Database.getInstance().getAllAccountsOfClient(c.getId());
		accounts_accounts.getItems().clear();
		card_accounts.getItems().clear();
		transaction_accounts.getItems().clear();
		for(Account a:accounts) {
			accounts_accounts.getItems().add(a.getAccNum());
			card_accounts.getItems().add(a.getAccNum());
			transaction_accounts.getItems().add(a.getAccNum());
		}
		accounts_accounts.getSelectionModel().selectFirst();
		card_accounts.getSelectionModel().selectFirst();
		transaction_accounts.getSelectionModel().selectFirst();
		ib_login.setText(c.getLogin());
	}
	public void initTime() {
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        int second = LocalDateTime.now().getSecond();
	        int minute = LocalDateTime.now().getMinute();
	        int hour = LocalDateTime.now().getHour();
	        time.setText((hour<10?"0":"") + hour + ":" + (minute<10?"0":"")+minute + ":" + (second<10?"0":"") + second);
	        
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	}

	public void resetPassword() {
		PasswordGenerator generator = new PasswordGenerator.PasswordGeneratorBuilder().useDigits(true).useLower(true).usePunctuation(true).build();
		String newPassword = generator.generate(8);
		Alert alert = new Alert(AlertType.INFORMATION,"Your new password is: " + newPassword,  ButtonType.OK);
		alert.showAndWait();
		int id = Integer.parseInt(UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0]);
		Database.getInstance().updatePassword(id, newPassword);
	}
	public void updateState() {
		boolean cardActive = card_status.getText() == "active"?false:true;
		Database.getInstance().setCardState(cardActive, Integer.parseInt(card_cards.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0]));
		renderCardActive(cardActive);
	}
	@FXML
	private void createNewCard() {
		if(card_accounts.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR,"Account not selected",  ButtonType.OK);
			alert.showAndWait();
			return;
		}
		ApplicationState.NewCard.account_id = Database.getInstance().getAccountIdByAccountNumber(card_accounts.getSelectionModel().getSelectedItem());
		Stage create = new Stage();
		create.setTitle("GL Bank");
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("../View/new_card.fxml"));
			Scene scene = new Scene(root, 250, 600);
			create.setScene(scene);
			create.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	private void createNewAccount() {
		Alert alert = new Alert(AlertType.CONFIRMATION,"Do you really want to create new account for " + UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote(")"))[1],  ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if(alert.getResult() == ButtonType.NO) {
			return;
		}
		String accountNumber = Tools.generateAccountNumber();
		int clientID = Integer.parseInt(UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote("."))[0]);
		
		alert = new Alert(AlertType.CONFIRMATION,"Create account for "+ UserSelectBox.getSelectionModel().getSelectedItem().split(Pattern.quote(")"))[1] + " with account number: " + accountNumber + "?",  ButtonType.OK, ButtonType.CANCEL);
		alert.showAndWait();
		if(alert.getResult() == ButtonType.CANCEL) {
			return;
		}
		Database.getInstance().createAccount(clientID, accountNumber);
		return;
	}
}



/*
 * TODO: check if card is selected on when deactivating card
 * TODO: re-render information about card
 * 
 * */
 