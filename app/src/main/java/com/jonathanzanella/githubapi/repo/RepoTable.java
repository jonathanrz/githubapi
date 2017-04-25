package com.jonathanzanella.githubapi.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.SqlTypes;
import com.jonathanzanella.githubapi.database.Table;

import static com.jonathanzanella.githubapi.database.CursorHelper.getLong;
import static com.jonathanzanella.githubapi.database.CursorHelper.getString;

public class RepoTable implements Table<Repo> {
	@Override
	public String getName() {
		return "repositories";
	}

	@Override
	public String [] getProjection() {
		return new String[] {
				Fields.ID.toString(),
				Fields.NAME.toString(),
				Fields.LANGUAGE_ID.toString()
		};
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableSql());
	}

	private String createTableSql() {
		return "CREATE TABLE " + getName() + " (" +
				Fields.ID + SqlTypes.PRIMARY_KEY + "," +
				Fields.NAME + SqlTypes.TEXT_UNIQUE_NOT_NULL + "," +
				Fields.LANGUAGE_ID + SqlTypes.INT_NOT_NULL + " )";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	@Override
	public void onDrop(SQLiteDatabase db) {
		db.execSQL(dropTableSql());
	}

	private String dropTableSql() {
		return "DROP TABLE IF EXISTS " + getName();
	}

	@Override
	public ContentValues fillContentValues(Repo repo) {
		ContentValues values = new ContentValues();
		values.put(Fields.NAME.toString(), repo.getName());
		values.put(Fields.LANGUAGE_ID.toString(), repo.getLanguageId());
		return values;
	}

	@Override
	public Repo fill(Cursor c) {
		Repo language = new Repo();
		language.setId(getLong(c, Fields.ID));
		language.setName(getString(c, Fields.NAME));
		language.setLanguageId(getLong(c, Fields.LANGUAGE_ID));
		return language;
	}
}
