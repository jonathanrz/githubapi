package com.jonathanzanella.githubapi.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface Table<T> {
	String getName();
	String [] getProjection();

	void onCreate(SQLiteDatabase db);
	void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	void onDrop(SQLiteDatabase db);

	ContentValues fillContentValues(T data);
	T fill(Cursor c);
}
