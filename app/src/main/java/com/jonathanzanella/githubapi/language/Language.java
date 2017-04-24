package com.jonathanzanella.githubapi.language;

import com.jonathanzanella.githubapi.Model;

public class Language implements Model {
	private long id;
	private String name;

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


}