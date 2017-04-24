package com.jonathanzanella.githubapi.language;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.SqlTypes;
import com.jonathanzanella.githubapi.database.Table;

import static com.jonathanzanella.githubapi.database.CursorHelper.getLong;
import static com.jonathanzanella.githubapi.database.CursorHelper.getString;

public class LanguageTable implements Table<Language> {
	@Override
	public String getName() {
		return "languages";
	}

	@Override
	public String [] getProjection() {
		return new String[] {
				Fields.ID.toString(),
				Fields.NAME.toString()
		};
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableSql());
	}

	private String createTableSql() {
		return "CREATE TABLE " + getName() + " (" +
				Fields.ID + SqlTypes.PRIMARY_KEY + "," +
				Fields.NAME + SqlTypes.TEXT_UNIQUE_NOT_NULL + " )";
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
	public ContentValues fillContentValues(Language language) {
		ContentValues values = new ContentValues();
		values.put(Fields.NAME.toString(), language.getName());
		return values;
	}

	@Override
	public Language fill(Cursor c) {
		Language language = new Language();
		language.setId(getLong(c, Fields.ID));
		language.setName(getString(c, Fields.NAME));
		return language;
	}
}
