package com.ocran;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemberListController implements Initializable{
	
	@FXML
	private TableView<Member> tableView;
	@FXML
	private TableColumn<Member, String> memberIDColumn;
	@FXML
	private TableColumn<Member, String> memberNameColumn;
	@FXML
	private TableColumn<Member, String> phoneNumberColumn;
	@FXML
	private TableColumn<Member, String> emailColumn;
	
	private DatabaseController database;
	
	private ObservableList<Member> list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		database = DatabaseController.getDatabaseInstance();
		list = FXCollections.observableArrayList();
		initColumn();
		loadData();
	}
	
	public void initColumn() {
		memberIDColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("memberID"));
		memberNameColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("memberName"));
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("phoneNumber"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("email"));
	}
	
	public void loadData() {
		String query = "SELECT * FROM Member";
		ResultSet resultSet = database.executeQuery(query);
		
		try {
			while(resultSet.next()) {
				String id = resultSet.getString("ID");
				String name = resultSet.getString("NAME");
				String phone = resultSet.getString("PHONE_NUMBER");
				String email = resultSet.getString("EMAIL");
				
				list.add(new Member(id, name, phone, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		tableView.getItems().setAll(list);
	}
}
