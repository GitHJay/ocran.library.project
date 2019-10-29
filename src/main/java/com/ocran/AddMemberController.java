package com.ocran;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddMemberController implements Initializable{
	@FXML
	private Pane rootPane;
	@FXML
	private JFXTextField memberNameTextField;
	@FXML
	private JFXTextField memberIDTextField;
	@FXML
	private JFXTextField phoneNumberTextField;
	@FXML
	private JFXTextField emailTextField;
	@FXML
	private JFXButton acceptButton;
	@FXML
	private JFXButton cancelButton;
	
	private DatabaseController database;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		database = DatabaseController.getDatabaseInstance();
	}
	
	public void addMember(ActionEvent event) {
		String memberName 	= memberNameTextField.getText();
		String memberID 	= memberIDTextField.getText();
		String phoneNumber	= phoneNumberTextField.getText();
		String email		= emailTextField.getText();
		
		
		
		String sql = "INSERT INTO Member VALUES (" 
				+ "'" + memberID  	+ "',"
				+ "'" + memberName	+ "',"
				+ "'" + phoneNumber	+ "',"
				+ "'" + email		+"'"
				+ ")";
		if(memberName.isEmpty() || memberID.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
			Message.errorMessage("Error", "Please fill all fields");
		}
		else {
			if(database.executeAction(sql)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Success");
				alert.showAndWait();
				cancel();
			}
			else{
				Message.errorMessage("Error", "Failed");
			}
		}
		
	}
	
	public void cancel() {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
}
