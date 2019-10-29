package com.ocran;

public class Book {
	
	private String bookID;
	private String bookTitle;
	private String bookAuthor;
	private String publisher;
	private Boolean availability;
	
	public Book(String bookID, String bookTitle, String bookAuthor, String publisher, Boolean availability) {
		this.bookID			= bookID;
		this.bookTitle 		= bookTitle;
		this.bookAuthor 	= bookAuthor;
		this.publisher		= publisher;
		this.availability 	= availability;
	}
	
	// Getters
	public String getBookID() {
		return bookID;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public String getPublisher() {
		return publisher;
	}
	
	public Boolean getAvailability() {
		return availability;
	}
}
