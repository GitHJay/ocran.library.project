package com.ocran;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToolbar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController implements Initializable{
	@FXML
	private JFXToolbar toolbar;
	@FXML
	private Label headLabel;
	@FXML
	private Button addBookButton;
	@FXML
	private Button addMemberButton;
	@FXML
	private Button viewMembersButton;
	@FXML
	private Button bookShelfButton;
	@FXML
	private JFXButton editButton;
	@FXML
	private JFXButton settingsButton;
	@FXML
	private JFXButton signoutButton;
	@FXML
	private VBox vboxNav;
	@FXML
	private HBox hboxBookInfo;
	@FXML
	private HBox hboxMemberInfo;
	@FXML
	private VBox vboxUser;
	@FXML
	private ImageView userImageView;
	@FXML
	private TextField bookIDTextField;
	@FXML
	private TextField memberIDTextField;
	@FXML
	private TextField bookID;
	@FXML
	private Text bookTitleText;
	@FXML
	private Text bookAuthorText;
	@FXML
	private Text memberNameText;
	@FXML
	private Text memberPhoneNumberText;
	@FXML
	private Label userLbl;
	@FXML
	private Button issueButton;
	@FXML
	private ListView<String> issueDetailsList;
	
	private DatabaseController database;
	
	private boolean isReadyForSubmission = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		database = DatabaseController.getDatabaseInstance();
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(-1);
		dropShadow.setOffsetY(1);
		dropShadow.setColor(Color.rgb(50, 50, 50, 0.7));
		hboxBookInfo.setEffect(dropShadow);
		hboxMemberInfo.setEffect(dropShadow);
		vboxUser.setEffect(dropShadow);
		vboxNav.setEffect(dropShadow);
		
		Rectangle clip = new Rectangle(userImageView.getFitWidth(), userImageView.getFitHeight());
		clip.setArcWidth(200);
		clip.setArcHeight(200);
		userImageView.setClip(clip);
		
		Preferences preference = Preferences.getPreferences();
		userLbl.setText(String.valueOf(preference.getUsername()));
	}
	
	@FXML
	public void loadAddBook(ActionEvent event) {
		String layout = "/com/ocran/layouts/add_book.fxml";
		String style = "/com/ocran/styles/add_book.css";
		loadWindow(layout,style,"Add New Book");
	}
	
	@FXML
	public void loadAddMember(ActionEvent event) {
		String layout = "/com/ocran/layouts/add_member.fxml";
		String style = "/com/ocran/styles/add_member.css";
		loadWindow(layout, style, "Add New Member");
	}
	
	@FXML
	public void loadViewMembers(ActionEvent event) {
		String layout = "/com/ocran/layouts/member_list.fxml";
		String style = "";
		loadWindow(layout, style,"Members");
	}
	
	@FXML
	public void loadBookShelf(ActionEvent event) {
		String layout = "/com/ocran/layouts/book_shelf.fxml";
		String style = "";
		loadWindow(layout, style, "Book Shelf");
	}
	
	@FXML
	public void loadSettings(ActionEvent event) {
		String layout = "/com/ocran/layouts/settings.fxml";
		String style = "";
		loadWindow(layout, style, "Settings");
	}
	
	public void loadWindow(String layout, String style, String title) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(layout));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource(style).toExternalForm());
		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
	}
	
	@FXML
	public void loadBookInfo(ActionEvent event) {
		String bookID = bookIDTextField.getText();
		String query = "SELECT * FROM Book WHERE ID = '" + bookID + "'"; 
		ResultSet resultSet = database.executeQuery(query);
		Boolean flag = false;
		
		try {
			while(resultSet.next()) {
				String bookTitle = resultSet.getString("TITLE");
				String bookAuthor = resultSet.getString("AUTHOR");
				//Boolean bookStatus = resultSet.getBoolean("IS_AVAIL");
			
				bookTitleText.setText(bookTitle);
				bookAuthorText.setText(bookAuthor);	
				
				flag = true;
			}
			
			if(!flag) {
				bookTitleText.setText("No Such Book Available");
				bookAuthorText.setText(null);
			}
		} catch (SQLException e) {
			Message.errorMessage("Error", e.getMessage());
		}
	}
	
	@FXML
	public void loadBookDetails(ActionEvent event) {
		ObservableList<String> issueDetails = FXCollections.observableArrayList();
		isReadyForSubmission = false;
		
		String id = bookID.getText();
		String query = "SELECT * FROM Issue WHERE BOOK_ID = '" + id + "'";
		ResultSet resultSet = database.executeQuery(query);
		
		try {
			while(resultSet.next()) {
				String mBookID = id;
				String mMemberID = resultSet.getString("MEMBER_ID");
				Timestamp mIssueTime = resultSet.getTimestamp("ISSUE_TIME");
				int mRenewCount = resultSet.getInt("RENEW_COUNT");
				issueDetails.add("Date and Time: " + mIssueTime.toGMTString());
				issueDetails.add("Renew Count: " + mRenewCount);
				issueDetails.add("");
				
				query = "SELECT * FROM Book WHERE ID = '" + mBookID + "'";
				ResultSet resultSetBook = database.executeQuery(query);
				while(resultSetBook.next()) {
					issueDetails.add("Book Details");
					issueDetails.add("Book ID: " + resultSetBook.getString("ID"));
					issueDetails.add("Book Name: " + resultSetBook.getString("TITLE"));
					issueDetails.add("Book Author: " + resultSetBook.getString("AUTHOR"));
					issueDetails.add("Book Publisher: " + resultSetBook.getString("PUBLISHER"));
					issueDetails.add("");
				}
				
				query = "SELECT * FROM Member WHERE ID = '" + mMemberID + "'";
				ResultSet resultSetMember = database.executeQuery(query);
				while(resultSetMember.next()) {
					issueDetails.add("Member Details");
					issueDetails.add("Member ID: " + resultSetMember.getString("ID"));
					issueDetails.add("Member Name: " + resultSetMember.getString("NAME"));
					issueDetails.add("Member Phone: " + resultSetMember.getString("PHONE_NUMBER"));
					issueDetails.add("Member Email: " + resultSetMember.getString("EMAIL"));
				}
			}
			isReadyForSubmission = true;
		}catch(Exception e) {
			
		}
		issueDetailsList.getItems().setAll(issueDetails);
	}
	
	@FXML
	public void loadSubmissionOperation(ActionEvent event) {
		if(!isReadyForSubmission) {
			Message.errorMessage("Error", "Please select a book to submit");
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Submission Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to submit this book?");
		
		Optional<ButtonType> response = alert.showAndWait();
		if(response.get() == ButtonType.OK) {
			String id = bookID.getText();
			String action_1 = "DELETE FROM Issue WHERE BOOK_ID = '" + id + "'";
			String action_2 = "UPDATE Book SET IS_AVAIL = TRUE WHERE ID = '" + id + "'";
			
			if(database.executeAction(action_1) && database.executeAction(action_2)) {
				Message.infoMessage("Success", "Book Submitted");
			}else {
				Message.errorMessage("Error", "Book submission failed");
			}
		}
		else {
			Message.infoMessage("Cancel", "Submission Operation Cancelled");
		}
	}
	
	@FXML
	public void loadRenewOperation(ActionEvent event) {
		if(!isReadyForSubmission) {
			Message.errorMessage("Error", "Please select a book to renew");
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Submission Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to submit this book?");
		
		Optional<ButtonType> response = alert.showAndWait();
		if(response.get() == ButtonType.OK) {
			String action = "UPDATE ISSUE SET ISSUE_TIME = CURRENT_TIMESTAMP, RENEW_COUNT = RENEW_COUNT + 1 WHERE BOOK_ID = '" + bookID.getText() + "'";
			if(database.executeAction(action)) {
				Message.infoMessage("Success", "Book Has Been Renewed");
			}else {
				Message.errorMessage("Error", "Renewal Has Failed");
			}
		}
		else {
			Message.infoMessage("Cancel", "Renew Operation Cancelled");
		}
	}
	
	@FXML
	public void loadMemberInfo(ActionEvent event) {
		String memberID = memberIDTextField.getText();
		String query = "SELECT * FROM Member WHERE ID = '" + memberID + "'";
		ResultSet resultSet = database.executeQuery(query);
		Boolean flag = false;
		
		try {
			while(resultSet.next()) {
				String memberName = resultSet.getString("NAME");
				String memberPhoneNumber = resultSet.getString("PHONE_NUMBER");
				
				memberNameText.setText(memberName);
				memberPhoneNumberText.setText(memberPhoneNumber);
				
				flag = true;
			}
			
			if(!flag) {
				memberNameText.setText("No Such Member Available");
				memberPhoneNumberText.setText(null); 
			}
		} catch (SQLException e) {
			Message.errorMessage("Error", e.getMessage());;
		}
	}
	
	@FXML
	public void loadIssueOperation(ActionEvent event) {
		String memberID = memberIDTextField.getText();
		String bookID = bookIDTextField.getText();
		
		if(bookID.isEmpty()|| memberID.isEmpty()) {
			String message = "Book ID / Member ID cannot be empty";
			Message.errorMessage("Error", message);
		}else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Confirm Issue Operation");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure to issue the book " + bookTitleText.getText() + "\n to " + memberNameText.getText());
			
			Optional<ButtonType> response = alert.showAndWait();
			if(response.get() == ButtonType.OK) {
				String query = "INSERT INTO Issue(MEMBER_ID, BOOK_ID) VALUES (" + "'" + memberID + "',"	+ "'" + bookID   + "')";	
				String query2 = "UPDATE BOOK SET IS_AVAIL = false WHERE ID = '" + bookID + "'";
				
				if(database.executeAction(query) && database.executeAction(query2)) {
					Message.infoMessage("Success", "Book Issue Complete");
				}else{
					Message.errorMessage("Error", "Issue Operation Failed");
				}
			}
			else{
				Message.errorMessage("Error", "Issue Operation Cancel");
			}
		}	
	}
	
	public void signOut(ActionEvent event) {
		Platform.exit();
	}
}
