package com.jonathanzanella.githubapi.database;

public interface Model {
	Long getId();
	void setId(Long id);
	String getName();
	boolean isValid();
}
