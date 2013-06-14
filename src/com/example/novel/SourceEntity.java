package com.example.novel;

import java.io.Serializable;

import android.database.Cursor;

public class SourceEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int _id;
	private int id;
	private String name;
	private String domain;
	private String patternChapter;
	private String patternImg;
	private String patternLatestTitle;
	private String pattern_body;
	private String pattern_order;
	private String pattern_content;
	private String pattern_page;
	
	public SourceEntity(int _id, int id, String name, String domain, 
			String pattern_chapter, String pattern_img, String latest_title_pattern, 
			String pattern_body, String pattern_order, String pattern_content,
			String pattern_page) {
		this._id = _id;
		this.id = id;
		this.name = name;
		this.setDomain(domain);
		this.setPatternChapter(pattern_chapter);
		this.setPatternImg(pattern_img);
		this.setPatternLatestTitle(latest_title_pattern);
		this.setPatternBody(pattern_body);
		this.setPatternOrder(pattern_order);
		this.setPatternContent(pattern_content);
		this.setPatternPage(pattern_page);
	}
	
	public static SourceEntity getSourceEntityFromRecord(Cursor cursorSource) {
		int srcPriId = cursorSource.getInt(0);
		int srcId = cursorSource.getInt(1);
		String srcName = cursorSource.getString(2);
		String srcDomain = cursorSource.getString(3);
		String srcPatternChapter = cursorSource.getString(4);
		String srcPatternImg = cursorSource.getString(5);
		String srcPatternLatestTitle = cursorSource.getString(6);
		String srcPatternBody = cursorSource.getString(7);
		String srcPatternOrder = cursorSource.getString(8);
		String srcPatternContent = cursorSource.getString(9);
		String srcPatternPage = cursorSource.getString(10);
		
		return new SourceEntity(srcPriId, srcId, srcName, srcDomain,
				srcPatternChapter, srcPatternImg, srcPatternLatestTitle, srcPatternBody,
				srcPatternOrder, srcPatternContent, srcPatternPage);
	}
	
	public int getPriId() {
		return _id;
	}
	public int getId() {
		return id;
	}
	public String getName(){
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPatternChapter() {
		return patternChapter;
	}

	public void setPatternChapter(String patternChapter) {
		this.patternChapter = patternChapter;
	}

	public String getPatternImg() {
		return patternImg;
	}

	public void setPatternImg(String patternImg) {
		this.patternImg = patternImg;
	}

	public String getPatternLatestTitle() {
		return patternLatestTitle;
	}

	public void setPatternLatestTitle(String patternLatestTitle) {
		this.patternLatestTitle = patternLatestTitle;
	}

	public String getPatternBody() {
		return pattern_body;
	}

	public void setPatternBody(String pattern_body) {
		this.pattern_body = pattern_body;
	}

	public String getPatternOrder() {
		return pattern_order;
	}

	public void setPatternOrder(String pattern_order) {
		this.pattern_order = pattern_order;
	}

	public String getPatternContent() {
		return pattern_content;
	}

	public void setPatternContent(String pattern_content) {
		this.pattern_content = pattern_content;
	}

	public String getPatternPage() {
		return pattern_page;
	}

	public void setPatternPage(String pattern_page) {
		this.pattern_page = pattern_page;
	}
	
}
