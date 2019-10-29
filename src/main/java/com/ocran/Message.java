package com.ocran;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Message {
	
	public static void alertPopUp(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setHeaderText(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static void errorMessage(String title, String error) {
		alertPopUp(AlertType.ERROR, title, error);
	}
	
	public static void confirmationMessage(String title, String confirmation) {
		alertPopUp(AlertType.CONFIRMATION, title, confirmation);
	}
	
	public static void warningMessage(String title, String warning) {
		alertPopUp(AlertType.WARNING, title, warning);
	}
	
	public static void infoMessage(String title, String info) {
		alertPopUp(AlertType.INFORMATION, title, info);
	}
}
