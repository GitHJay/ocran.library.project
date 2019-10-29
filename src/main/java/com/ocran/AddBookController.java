package com.ocran;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddBookController implements Initializable{
	@FXML
	private JFXTextField bookTitleTextField;
	@FXML
	private JFXTextField bookIDTextField;
	@FXML
	private JFXTextField bookAuthorTextField;
	@FXML
	private JFXTextField publisherTextField;
	@FXML
	private JFXButton acceptButton;
	@FXML
	private JFXButton cancelButton;
	@FXML
	private VBox rootPane;
	
	private DatabaseController database;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		database = DatabaseController.getDatabaseInstance();
	}
	
	@FXML
	public void addBook(ActionEvent event) {
		String bookTitle 	= bookTitleTextField.getText();
		String bookID 		= bookIDTextField.getText();
		String bookAuthor 	= bookAuthorTextField.getText();
		String publisher 	= publisherTextField.getText();
		String sql = "INSERT INTO Book VALUES (" 
				+ "'" + bookID  	+ "',"
				+ "'" + bookTitle	+ "',"
				+ "'" + bookAuthor	+ "',"
				+ "'" + publisher	+ "',"
				+ "true"
				+ ")";
		if(bookTitle.isEmpty() || bookID.isEmpty() || bookAuthor.isEmpty() || publisher.isEmpty()) {
			Message.errorMessage("Error", "Please fill all fields");
		}
		else {
			if(database.executeAction(sql)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Success");
				alert.showAndWait();
				cancel();
			}else{
				Message.errorMessage("Error", "Failed");
			}
		}
	}
	
	public void checkBook() {
		String query = "SELECT TITLE FROM Book;";
		ResultSet resultSet = database.executeQuery(query);
		
		try {
			while(resultSet.next()) {
				String title = resultSet.getString("TITLE");
				System.out.println(title);
			}
		} catch (SQLException e) {
			Message.errorMessage("Error", e.getMessage());
		}
	}
	
	public void inflateUI(Book book) {
		bookIDTextField.setText(book.getBookID());
		bookTitleTextField.setText(book.getBookTitle());
		bookAuthorTextField.setText(book.getBookAuthor());
		publisherTextField.setText(book.getPublisher());
	}
	
	public void clear() {
		bookTitleTextField.clear();
		bookIDTextField.clear();
		bookAuthorTextField.clear();
		publisherTextField.clear();
	}
	
	@FXML
	public void cancel() {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
}
