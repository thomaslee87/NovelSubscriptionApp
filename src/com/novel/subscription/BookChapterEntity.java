package com.novel.subscription;

import java.io.Serializable;

public class BookChapterEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int _id;
	private BookEntity book;
	private String chapterTitle;
	private String chapterUrl;
	private int isRead;
	private int isDownload;
	private int updateFlag;
	private int order;
	private int isUpdate;
	
	public BookChapterEntity (int _id, String chapterTitle, String chapterUrl, int isRead, int isDownload,
			int updateFlag, int order, int isUpdate, BookEntity book) {
		this.setPriId(_id);
		this.setChapterTitle(chapterTitle);
		this.setChapterUrl(chapterUrl);
		this.setIsRead(isRead);
		this.setIsDownload(isDownload);
		this.setUpdateFlag(updateFlag);
		this.setBook(book);
		this.setOrder(order);
		setIsUpdate(isUpdate);
	}

	public int getPriId() {
		return _id;
	}

	private void setPriId(int _id) {
		this._id = _id;
	}

	public BookEntity getBook() {
		return book;
	}

	public void setBook(BookEntity book) {
		this.book = book;
	}

	public String getChapterTitle() {
		return chapterTitle;
	}

	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}

	public String getChapterUrl() {
		return chapterUrl;
	}

	public void setChapterUrl(String chapterUrl) {
		this.chapterUrl = chapterUrl;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}

	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}
	
}
