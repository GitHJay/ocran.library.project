package com.ocran;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	@FXML 
	private JFXTextField usernameTxt;
	@FXML
	private JFXPasswordField passwordTxt;
	@FXML
	private JFXButton loginButton;
	@FXML
	private JFXButton cancelButton;
	@FXML
	private Label messageLabel;
	
	private Preferences preferences;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		preferences = Preferences.getPreferences();
	}
	
	@FXML
	public void handleLoginButtonAction(ActionEvent event) {
		messageLabel.setText("");
		
		String uname = usernameTxt.getText();
		String pword = DigestUtils.sha1Hex(passwordTxt.getText());
		
		if(uname.equals(preferences.getUsername()) && pword.equals(preferences.getPassword())) {
			closeStage();
			loadMain();
		}else {
			messageLabel.setText("Invalid Credentials");
			messageLabel.setStyle("-fx-text-fill: #d32f2f");
		}
	}
	
	@FXML
	public void handleCancelButtonAction(ActionEvent event) {
		System.exit(0);
	}
	
	private void closeStage() {
		((Stage) usernameTxt.getScene().getWindow()).close();
	}
	
	private void loadMain() {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/com/ocran/layouts/main_view.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/com/ocran/styles/main.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Baptist Church Library Assistant");
		stage.show();
	}
}
