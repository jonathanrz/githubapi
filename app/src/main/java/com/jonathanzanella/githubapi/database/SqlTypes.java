package com.jonathanzanella.githubapi.database;

public interface SqlTypes {
	String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT";
	String INT = " INTEGER";
	String INT_NOT_NULL = " INTEGER NOT NULL";
	String TEXT = " TEXT";
	String TEXT_NOT_NULL = " TEXT NOT NULL";
	String TEXT_UNIQUE = " TEXT UNIQUE";
	String TEXT_UNIQUE_NOT_NULL = " TEXT UNIQUE NOT NULL\"";
	String DATE = " INTEGER";
	String DATE_NOT_NULL = " INTEGER NOT NULL";
}
