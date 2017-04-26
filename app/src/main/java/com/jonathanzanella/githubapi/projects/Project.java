package com.jonathanzanella.githubapi.projects;

import com.jonathanzanella.githubapi.Model;

public class Project implements Model {
	private long id;
	private String name;
	private long languageId;


	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

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
}
