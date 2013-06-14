package com.novel.subscription;

import java.io.Serializable;

	
public class BookEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private String author = "";
	private String url = "";
	private String latestTitle = "";
	private long _id;
	private int updateOrder;
	private SourceEntity srcEntity;
	private String site;
	
	public BookEntity (long _id, String name, String author, String url, 
			String latest_title, int updateOrder, String site, SourceEntity srcEntity) {
		this._id = _id;
		this.name = name;
		this.author = author;
		this.url = url;
		this.latestTitle = latest_title;
		this.srcEntity = srcEntity;
		this.setSite(site);
		this.setUpdateOrder(updateOrder);
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getLatestTitle() {
		return latestTitle;
	}
	
	public void setLatestTitle(String title) {
		latestTitle = title;
	}

	public long getPriId() {
		return _id;
	}

	public void setPriId(long _id) {
		this._id = _id;
	}

	public SourceEntity getSrcEntity() {
		return srcEntity;
	}

	public void setSrcEntity(SourceEntity srcEntity) {
		this.srcEntity = srcEntity;
	}

	public int getUpdateOrder() {
		return updateOrder;
	}

	public void setUpdateOrder(int updateOrder) {
		this.updateOrder = updateOrder;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
}
