package application.Controllers;

import java.util.Date;

import application.ApplicationState;
import application.Tools;
import application.Database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NewCardController {
	@FXML
	Label new_pin;
	
	@FXML
	Label expiry_date;
	
	@FXML
	public void initialize(){
		new_pin.setText(Tools.generatePIN());
		Date date = new Date();
		expiry_date.setText((date.getMonth()<10?"0":"")+(date.getMonth()+1)+"/"+(date.getYear()%100+3));
	}
	@FXML
	public void createCard() {
		if(ApplicationState.NewCard.account_id < 0) {
			return;
		}
		Database.getInstance().createCard(ApplicationState.NewCard.account_id, new_pin.getText(), expiry_date.getText());
		ApplicationState.NewCard.account_id = -1;
		((Stage)new_pin.getScene().getWindow()).close();
		
	}
}
