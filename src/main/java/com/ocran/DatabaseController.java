package com.ocran;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseController {
	private static DatabaseController db;
	
	private static final String DB_URL = "com.mysql.jdbc.Driver";
	private static Connection connection = null;
	private static Statement statement = null;
	private static String error;
	
	private DatabaseController() {
		createConnection();
		createBookTable();
		createMemberTable();
		createIssueTable();
	}
	
	public static DatabaseController getDatabaseInstance() {
		if(db == null) {
			db = new DatabaseController();
		}
		return db;
	}
	
	public void createConnection() {
		try {
			Class.forName(DB_URL);
			connection = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "pass");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Can't load database", "Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void createBookTable() {
		String TABLE_NAME = "Book";
		
		try {
			statement = connection.createStatement();
			DatabaseMetaData dbmetaData = connection.getMetaData();
			ResultSet table = dbmetaData.getTables(null, null, TABLE_NAME, null);
			
			if(table.next()) {
				System.out.print("Table " + TABLE_NAME + " already exists.");
			}else{
				statement.executeUpdate(
						"CREATE TABLE " + TABLE_NAME + "(" +
							"ID 			varchar(150)	NOT NULL UNIQUE ,\n" +
							"TITLE 			varchar(200)	NOT NULL		,\n" +
							"AUTHOR 		varchar(200)	NOT NULL		,\n" +
							"PUBLISHER 		varchar(200)	NOT NULL		,\n" +
							"IS_AVAIL		boolean 	"
							+ "	default true," + 
							"PRIMARY KEY (ID)"	+
						")"
					);
			}
			
			
		} catch (SQLException e) {
			Message.errorMessage("Error", e.getMessage());
		}finally{
			
		}
	}
	
	public void createMemberTable() {
		String TABLE_NAME = "Member";
		
		try {
			statement = connection.createStatement();
			DatabaseMetaData dbmetaData = connection.getMetaData();
			ResultSet table = dbmetaData.getTables(null, null, TABLE_NAME, null);
			
			if(table.next()) {
				System.out.println("Table " + TABLE_NAME +" already exist");
			}else{
				statement.executeUpdate(
						"CREATE TABLE " + TABLE_NAME + "(" +
						"ID 				varchar(50)	NOT NULL UNIQUE		,\n" +
						"NAME 				varchar(200)	NOT NULL		,\n" +
						"PHONE_NUMBER 		varchar(50)		NOT NULL		,\n" +
						"EMAIL	 			varchar(200)	NOT NULL		,\n" +
						"PRIMARY KEY (ID)"										 +
					")"
				);
			}
		} catch (SQLException e) {
			error = e.getMessage();
		}
		finally{
			
		}
	}
	
	public void createIssueTable() {
		String TABLE_NAME = "Issue";
		
		try {
			statement = connection.createStatement();
			DatabaseMetaData dbMetaData = connection.getMetaData();
			ResultSet table = dbMetaData.getTables(null, null, TABLE_NAME, null);
			
			if(table.next()) {
				System.out.println("Table " + TABLE_NAME +" already exist");
			}else{
				statement.executeUpdate(
						"CREATE TABLE " + TABLE_NAME + "(" +
						"BOOK_ID		VARCHAR(10)		NOT NULL						,\n" +
						"MEMBER_ID		VARCHAR(10)		NOT NULL						,\n" +
						"ISSUE_TIME		TIMESTAMP		DEFAULT		CURRENT_TIMESTAMP	,\n" +
						"RENEW_COUNT	INTEGER			DEFAULT		0					,\n" +
						"PRIMARY KEY	(BOOK_ID)										,\n" +										
						"FOREIGN KEY	(BOOK_ID)		REFERENCES	Book(ID)			,\n" +
						"FOREIGN KEY	(MEMBER_ID)		REFERENCES	Member(ID)"				 +
						")"
					);
			}
		} catch (SQLException e) {
			Message.errorMessage("Error", e.getMessage());
		}
		
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			error = e.getMessage().toString();
			Message.errorMessage("Error", error);
			return null;
		}finally{
			
		}
		return result;
	}
	
	public boolean executeAction(String update) {
		try {
			statement = connection.createStatement();
			statement.executeUpdate(update);
			return true;
		} catch (SQLException e) {
			error = e.getMessage().toString();
			Message.errorMessage("Error", error);
			return false;
		}finally{
			
		}
	}
	
	public boolean deleteBook(Book book) {
		String deleteStatement = "DELETE FROM Book WHERE ID = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(deleteStatement);
			statement.setString(1, book.getBookID());
			int result = statement.executeUpdate();
			if(result == 1) {
				return true;
			}
			System.out.println(result);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
