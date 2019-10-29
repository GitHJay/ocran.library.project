package com.ocran;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SettingsController implements Initializable{

	@FXML
	private JFXTextField nDaysWithoutFineTxt;
	@FXML
	private JFXTextField finePerDayTxt;
	@FXML
	private JFXTextField usernameTxt;
	@FXML
	private JFXPasswordField passwordTxt;
	@FXML
	private JFXButton saveButton;
	@FXML
	private JFXButton cancelButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initDefaultValue();
	}

	@FXML
	public void handleSaveButtonAction(ActionEvent event) {
		int numberOfDays = Integer.parseInt(nDaysWithoutFineTxt.getText());
		float fine = Float.parseFloat(finePerDayTxt.getText());
		String uname = usernameTxt.getText();
		String pass = passwordTxt.getText();
		
		Preferences preference = Preferences.getPreferences();
		preference.setnDaysWithoutFine(numberOfDays);
		preference.setFinePerDay(fine);
		preference.setUsername(uname);
		preference.setPassword(pass);
		
		Preferences.writePreferencesToFile(preference);
	}
	
	@FXML
	public void handleCancelButtonAction(ActionEvent event) {
		
	}
	
	public void initDefaultValue() {
		Preferences preference = Preferences.getPreferences();
		nDaysWithoutFineTxt.setText(String.valueOf(preference.getnDaysWithoutFine()));
		finePerDayTxt.setText(String.valueOf(preference.getFinePerDay()));
		usernameTxt.setText(String.valueOf(preference.getUsername()));
		passwordTxt.setText(String.valueOf(preference.getPassword()));
	}
}
