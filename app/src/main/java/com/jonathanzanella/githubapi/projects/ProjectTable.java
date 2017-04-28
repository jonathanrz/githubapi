package com.jonathanzanella.githubapi.projects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jonathanzanella.githubapi.database.Fields;
import com.jonathanzanella.githubapi.database.SqlTypes;
import com.jonathanzanella.githubapi.database.Table;

import static com.jonathanzanella.githubapi.database.CursorHelper.getBoolean;
import static com.jonathanzanella.githubapi.database.CursorHelper.getDate;
import static com.jonathanzanella.githubapi.database.CursorHelper.getInt;
import static com.jonathanzanella.githubapi.database.CursorHelper.getLong;
import static com.jonathanzanella.githubapi.database.CursorHelper.getString;

public class ProjectTable implements Table<Project> {
	@Override
	public String getName() {
		return "projects";
	}

	@Override
	public String [] getProjection() {
		return new String[] {
				Fields.ID.toString(),
				Fields.NAME.toString(),
				Fields.LANGUAGE_ID.toString(),
				Fields.CREATED_AT.toString(),
				Fields.UPDATED_AT.toString(),
				Fields.OPEN_ISSUES.toString(),
				Fields.VALID.toString()
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
				Fields.LANGUAGE_ID + SqlTypes.INT_NOT_NULL + "," +
				Fields.CREATED_AT + SqlTypes.INT_NOT_NULL + "," +
				Fields.UPDATED_AT + SqlTypes.INT_NOT_NULL + "," +
				Fields.OPEN_ISSUES + SqlTypes.INT_NOT_NULL + "," +
				Fields.VALID + SqlTypes.INT_NOT_NULL + " )";
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
	public ContentValues fillContentValues(Project project) {
		ContentValues values = new ContentValues();
		values.put(Fields.NAME.toString(), project.getName());
		values.put(Fields.LANGUAGE_ID.toString(), project.getLanguageId());
		values.put(Fields.CREATED_AT.toString(), project.getCreatedAt().getMillis());
		values.put(Fields.UPDATED_AT.toString(), project.getUpdatedAt().getMillis());
		values.put(Fields.OPEN_ISSUES.toString(), project.getOpenIssues());
		values.put(Fields.VALID.toString(), project.isValid());
		return values;
	}

	@Override
	public Project fill(Cursor c) {
		Project project = new Project();
		project.setId(getLong(c, Fields.ID));
		project.setName(getString(c, Fields.NAME));
		project.setLanguageId(getLong(c, Fields.LANGUAGE_ID));
		project.setCreatedAt(getDate(c, Fields.CREATED_AT));
		project.setUpdatedAt(getDate(c, Fields.UPDATED_AT));
		project.setOpenIssues(getInt(c, Fields.OPEN_ISSUES));
		project.setValid(getBoolean(c, Fields.VALID));
		return project;
	}
}
