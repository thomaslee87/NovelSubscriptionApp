package com.example.novel;

import java.io.Serializable;

public class SubscriptionEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int _id;
	private int bookId;
	private String latestTitle;
	private String latestUrl;
	private String readTitle;
	private String readUrl;
	private int valid;
	private BookEntity book;
	private boolean isUpdating;
	
	public SubscriptionEntity(int _id, int bookId, String latestTitle, String latestUrl, String readTitle, String readUrl, int valid, BookEntity book) {
		this._id = _id;
		this.bookId = bookId;
		this.latestTitle = latestTitle;
		this.latestUrl = latestUrl;
		this.readTitle = readTitle;
		this.readUrl = readUrl;
		this.valid = valid;
		this.setBook(book);
		isUpdating = false;
	}
	
	public void setIsUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}
	public boolean getIsUpdating() {
		return this.isUpdating;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getLatestTitle() {
		return latestTitle;
	}
	public void setLatestTitle(String latestTitle) {
		this.latestTitle = latestTitle;
	}
	public String getLatestUrl() {
		return latestUrl;
	}
	public void setLatestUrl(String latestUrl) {
		this.latestUrl = latestUrl;
	}
	public String getReadTitle() {
		return readTitle;
	}
	public void setReadTitle(String readTitle) {
		this.readTitle = readTitle;
	}
	public String getReadUrl() {
		return readUrl;
	}
	public void setReadUrl(String readUrl) {
		this.readUrl = readUrl;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}

	public BookEntity getBook() {
		return book;
	}

	public void setBook(BookEntity book) {
		this.book = book;
	}
	
	
}
