package com.jonathanzanella.githubapi.database;

public enum Fields {
	ID("id"),
	NAME("name"),
	LANGUAGE_ID("languageId");

	private String fieldName;

	Fields(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String toString() {
		return fieldName;
	}
}
