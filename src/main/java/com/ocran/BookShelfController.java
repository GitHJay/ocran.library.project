package com.ocran;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookShelfController implements Initializable{
	@FXML
	private TableView<Book> tableView;
	@FXML
	private TableColumn<Book, String> bookIDColumn;
	@FXML
	private TableColumn<Book, String> bookTitleColumn;
	@FXML
	private TableColumn<Book, String> bookAuthorColumn;
	@FXML
	private TableColumn<Book, String> bookPublisherColumn;
	@FXML
	private TableColumn<Book, Boolean> bookAvailabilityColumn;
	@FXML
	private MenuItem contextDelete;
	@FXML
	private MenuItem contextEdit;
	
	private ObservableList<Book> list;
	
	private DatabaseController database;
	
	private Book book;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		database = DatabaseController.getDatabaseInstance();
		list =  FXCollections.observableArrayList();
		initColumn();
		loadData();
	}
	
	public void initColumn() {
		bookIDColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("bookID"));
		bookTitleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("bookTitle"));
		bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("bookAuthor"));
		bookPublisherColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		bookAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<Book, Boolean>("availability"));
	}
	
	public void loadData() {
		String query = "SELECT * FROM Book;";
		ResultSet resultSet = database.executeQuery(query);
		
		try {
			while(resultSet.next()) {
				String id = resultSet.getString("ID");
				String title = resultSet.getString("TITLE");
				String author = resultSet.getString("AUTHOR");
				String publisher = resultSet.getString("PUBLISHER");
				Boolean availability = resultSet.getBoolean("IS_AVAIL");
				
				// Create a book instance and add to list
				book = new Book(id, title, author, publisher, availability);
				list.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		tableView.setItems(list);
	}
	
	public void handleBookEditOperation(ActionEvent event) {
		Book selectedBook = tableView.getSelectionModel().getSelectedItem();
		if(selectedBook == null) {
			Message.errorMessage("No book selected", "Please select a book for editing");
			return;
		}
		
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ocran/layouts/add_book.fxml"));
			root = loader.load();
			
			AddBookController controller = new AddBookController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add(this.getClass().getResource("/com/ocran/styles/add_book.fxml").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Edit Book");
		stage.show();
	}
	
	public void handleBookDeleteOperation(ActionEvent event) {
		// Select item from row
		Book selectedBook = tableView.getSelectionModel().getSelectedItem();
		if(selectedBook == null) {
			Message.errorMessage("No book selected", "Please select a book for deletion");
			return;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deleting Book");
		alert.setContentText("Are you sure you want to delete the book " + selectedBook.getBookTitle() + " ?");
		Optional<ButtonType> answer = alert.showAndWait();
		if(answer.get() == ButtonType.OK) {
			Boolean result = DatabaseController.getDatabaseInstance().deleteBook(selectedBook);
			if(result) {
				Message.infoMessage("Book Deleted", selectedBook.getBookTitle() + "deleted successfully");
				list.remove(selectedBook);
			}else {
				Message.infoMessage("Failed", selectedBook.getBookTitle() + " could not be deleted");
			}
		}else {
			Message.infoMessage("Deleting Cancelled", "Deletion process cancelled");
		}
	}
}
