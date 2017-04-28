package com.jonathanzanella.githubapi.github;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class GitHubRepository {
	private String name;
	private String language;
	@SerializedName("created_at")
	private DateTime createdAt;
	@SerializedName("updated_at")
	private DateTime updatedAt;
	@SerializedName("open_issues")
	private int openIssues;

	public String getName() {
		return name;
	}

	public String getLanguage() {
		if(language == null)
			return "Undefined";
		return language;
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

	public int getOpenIssues() {
		return openIssues;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}