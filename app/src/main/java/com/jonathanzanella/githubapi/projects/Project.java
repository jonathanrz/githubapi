package com.jonathanzanella.githubapi.projects;

import com.jonathanzanella.githubapi.database.Model;

import org.joda.time.DateTime;

public class Project implements Model {
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private long id;
	private String name;
	private long languageId;
	private DateTime createdAt;
	private DateTime updatedAt;
	private int openIssues;
	private boolean valid = true;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(long languageId) {
		this.languageId = languageId;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	public DateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(DateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getOpenIssues() {
		return openIssues;
	}

	public void setOpenIssues(int openIssues) {
		this.openIssues = openIssues;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
