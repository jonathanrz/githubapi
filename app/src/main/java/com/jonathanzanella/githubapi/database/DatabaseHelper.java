package com.jonathanzanella.githubapi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jonathanzanella.githubapi.language.LanguageTable;
import com.jonathanzanella.githubapi.repo.RepoTable;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "githubapi";
	private static final int DB_VERSION = 1;

	private static final Table [] tables = {new LanguageTable(), new RepoTable()};

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		for (Table table : tables) {
			table.onCreate(sqLiteDatabase);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		for (Table table : tables) {
			table.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
		}
	}

	public void runMigrations() {
		getReadableDatabase().close(); //We just need to open a database to execute the migrations
	}
}
